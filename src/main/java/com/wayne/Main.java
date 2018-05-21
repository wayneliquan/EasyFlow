package com.wayne;

import com.wayne.entity.Flow;
import com.wayne.entity.FlowNode;
import com.wayne.entity.FlowNodeBO;
import com.wayne.entity.FlowRecord;
import com.wayne.manager.FlowManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;


public class Main {
    public static void main(String[] args) throws Exception {
        FlowManager manager = new FlowManager();
        manager.init();

        List<FlowRecord> records = new ArrayList<>();
        Flow flow = manager.findFlow(1L);

        Stack<Long> callStack = new Stack<>();
        System.out.println(callStack);
        FlowNodeBO sNodeBO = manager.startFlow(1L, callStack);
        System.out.println(sNodeBO);
        FlowNode node = sNodeBO.getFlowNode();
        FlowRecord record = new FlowRecord();
        record.setFlowId(node.getFlowId());
        record.setFlowNodeId(node.getNodeId());
        record.setMainFlowId(flow.getFlowId());
        record.setMainFlowNodeId(node.getNodeId());
        record.setParentFlowNodeId(0L);
        records.add(record);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(sNodeBO.getCallStack());
        System.out.println(callStack);
        FlowNodeBO nextNodeBO = manager.nextNode(sNodeBO.getFlowNode(), callStack);
        System.out.println(nextNodeBO);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(nextNodeBO.getCallStack());
        System.out.println(callStack);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), callStack);
        System.out.println(nextNodeBO);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(nextNodeBO.getCallStack());
        System.out.println(callStack);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), callStack);
        System.out.println(nextNodeBO);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(nextNodeBO.getCallStack());
        System.out.println(callStack);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), callStack);
        System.out.println(nextNodeBO);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(nextNodeBO.getCallStack());
        System.out.println(callStack);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), callStack);
        System.out.println(nextNodeBO);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(nextNodeBO.getCallStack());
        System.out.println(callStack);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), callStack);
        System.out.println(nextNodeBO);

        System.out.println("=====================================");

        System.out.println("back");
        callStack = new Stack<>();
        callStack.addAll(nextNodeBO.getCallStack());
        System.out.println(callStack);
        FlowNodeBO backNode = manager.preNode(nextNodeBO.getFlowNode(), callStack);
        System.out.println(backNode);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(backNode.getCallStack());
        System.out.println(callStack);
        backNode = manager.preNode(backNode.getFlowNode(), callStack);
        System.out.println(backNode);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(backNode.getCallStack());
        System.out.println(callStack);
        backNode = manager.preNode(backNode.getFlowNode(), callStack);
        System.out.println(backNode);

        System.out.println();
        callStack = new Stack<>();
        callStack.addAll(backNode.getCallStack());
        System.out.println(callStack);
        backNode = manager.preNode(backNode.getFlowNode(), callStack);
        System.out.println(backNode);

        saveRecord(records);
    }

    public static void saveRecord(List<FlowRecord> records) {
        for (FlowRecord record: records) {
            System.out.println(record);
        }
    }
}
