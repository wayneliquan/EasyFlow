package com.wayne.manager;

import com.alibaba.fastjson.JSON;
import com.wayne.entity.Flow;
import com.wayne.entity.FlowNode;
import com.wayne.entity.FlowNodeBO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlowManager {
    private Map<Long, Flow> flowMap;
    private Map<Long, FlowNode> flowNodeMap;


    public void init() throws IOException {
        this.flowMap = new HashMap<Long, Flow>();
        this.flowNodeMap = new HashMap<Long, FlowNode>();

        Flow flow = new Flow();
        flow.setFlowId(1L);
        flow.setName("县级申请");
        flowMap.put(flow.getFlowId(), flow);

        flow = new Flow();
        flow.setFlowId(2L);
        flow.setName("市级申请");
        flowMap.put(flow.getFlowId(), flow);

        String flowNodeJson = new String(Files.readAllBytes(Paths.get("flownode.json")));
        List<FlowNode> flowNodeList = JSON.parseArray(flowNodeJson, FlowNode.class);
        System.out.println(flowNodeList);

        for (FlowNode node: flowNodeList) {
            this.flowNodeMap.put(node.getNodeId(), node);
            Flow tempFlow = flowMap.get(node.getFlowId());
            List<FlowNode> nodeList = tempFlow.getFlowNodeList();
            if (nodeList == null) {
                nodeList = new ArrayList<>();
                tempFlow.setFlowNodeList(nodeList);
            }
            nodeList.add(node);
            if (node.isFlow()){
                node.setFlow(flowMap.get(node.getSubFlowId()));
            }
            // 回退节点
            if (node.isFlowBack()) {
                tempFlow.setBackNodeId(node.getNodeId());
            }
        }
        System.out.println(flowMap);
    }

    public FlowNodeBO startFlow(Long flowId, Long parentFlowNodeId) {
        Flow flow = flowMap.get(flowId);
        FlowNode flowNode = flowNodeMap.get(parentFlowNodeId);
        return startFlow(flow, flowNode);
    }

    public FlowNodeBO startFlow(Flow flow, FlowNode parentFlowNode) {
        List<FlowNode> nodeList = flow.getFlowNodeList();
        FlowNode flowNode = nodeList.get(0);
        return getFlowNodeBO(parentFlowNode, flowNode);
    }

    public FlowNode nextRealNode(FlowNode currNode) {
        Long flowId = currNode.getFlowId();
        Flow flow = flowMap.get(flowId);
        int index = currNode.getIndex();
        List<FlowNode> nodeList = flow.getFlowNodeList();
        if (currNode.getIndex() + 1 < nodeList.size()) {
            return nodeList.get(index + 1);
        }
        return null;
    }

    public Flow findFlow(long flowId) {
        return flowMap.get(flowId);
    }

    /**
     * 如果没有下一个则返回null, 递归如果本流程是最后一个流程，则回溯到上一个流程。
     * @param currFlowNodeId
     * @param parentFlowNodeId
     * @return
     */
    @Deprecated
    public FlowNodeBO nextNode(Long currFlowNodeId, Long parentFlowNodeId) {
        FlowNode currNode = flowNodeMap.get(currFlowNodeId);
        FlowNode parentFlowNode = flowNodeMap.get(parentFlowNodeId);
        Long flowId = currNode.getFlowId();
        Flow currFlow = flowMap.get(flowId);
        List<FlowNode> nodeList = currFlow.getFlowNodeList();
        if (currNode.getIndex() + 1 < nodeList.size()) {
            FlowNode nextNode = nodeList.get(currNode.getIndex() + 1);
            // 这里需要递归
            return getFlowNodeBO(parentFlowNode, nextNode);
        } else {
            //如果currFlowNodeId是当前流程的最后一个节点，则回溯到上一个流程的下一个节点
            if (parentFlowNodeId != null && parentFlowNodeId > 0) {
                FlowNode pNode = flowNodeMap.get(parentFlowNodeId);
                Flow pFlow = flowMap.get(pNode.getFlowId());
                int index = pNode.getIndex();
                List<FlowNode> pFlowNodeList =  pFlow.getFlowNodeList();
                if (index + 1 < pFlowNodeList.size()) {
                    nextNode(pNode.getNodeId(), pFlowNodeList.get(index +1).getNodeId());
                }
            }
            // 如果上一个流程就是当前流程，则流程结束了
            return null;
        }
    }

    private FlowNodeBO getFlowNodeBO(FlowNode parentFlowNode, FlowNode nextNode) {
        if (nextNode.isFlow()) {
            return startFlow(nextNode.getSubFlowId(), nextNode.getNodeId());
        } else {
            FlowNodeBO flowNodeBO = new FlowNodeBO();
            flowNodeBO.setFlowNode(nextNode);
            flowNodeBO.setParentFlowNode(parentFlowNode);
            return flowNodeBO;
        }
    }

    public FlowNodeBO nextNode(FlowNode currNode, FlowNode parentNode) {
        Long flowId = currNode.getFlowId();
        Flow currFlow = flowMap.get(flowId);
        List<FlowNode> nodeList = currFlow.getFlowNodeList();
        if (currNode.getIndex() + 1 < nodeList.size()) {
            FlowNode nextNode = nodeList.get(currNode.getIndex() + 1);
            // 这里需要递归
            if (nextNode.isFlow()) {
                return startFlow(nextNode.getSubFlowId(), nextNode.getNodeId());
            } else {
                return getFlowNodeBO(parentNode, nextNode);
            }
        } else {
            //如果currFlowNodeId是当前流程的最后一个节点，则回溯到上一个流程的下一个节点
            if (parentNode != null) {
                Flow pFlow = flowMap.get(parentNode.getFlowId());
                int index = parentNode.getIndex();
                List<FlowNode> pFlowNodeList =  pFlow.getFlowNodeList();
                if (index + 1 < pFlowNodeList.size()) {
                    return nextNode(parentNode, pFlowNodeList.get(index +1));
                }
            }
            // 如果上一个流程就是当前流程，则流程结束了
            return null;
        }
    }

    public FlowNodeBO preNode(FlowNode currNode, FlowNode parentNode) {
        Long flowId = currNode.getFlowId();
        Flow currFlow = flowMap.get(flowId);
        List<FlowNode> nodeList = currFlow.getFlowNodeList();
        int backIndex = currNode.getBackIndex();
        if (backIndex >= 0) { //在当前流程回退
            FlowNode backNode = nodeList.get(backIndex);
            // 如果是回退的节点是流程节点，则进入流程的回退节点
            if (backNode.isFlow()) {
                Flow backFlow = flowMap.get(backNode.getSubFlowId());
                Long subBackNodeId = backFlow.getBackNodeId();
                backNode = flowNodeMap.get(subBackNodeId);
            }

        } else {

        }
        return null;
    }
}
