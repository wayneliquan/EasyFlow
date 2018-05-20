package com.wayne;

import com.wayne.entity.Flow;
import com.wayne.entity.FlowNode;
import com.wayne.entity.FlowNodeBO;
import com.wayne.entity.FlowRecord;
import com.wayne.manager.FlowManager;

import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws Exception {
        FlowManager manager = new FlowManager();
        manager.init();

        List<FlowRecord> records = new ArrayList<>();
        Flow flow = manager.findFlow(1L);

        FlowNodeBO nodeBO = manager.startFlow(1L, null);
        System.out.println(nodeBO);
        FlowNode node = nodeBO.getFlowNode();
        FlowRecord record = new FlowRecord();
        record.setFlowId(node.getFlowId());
        record.setFlowNodeId(node.getNodeId());
        record.setMainFlowId(flow.getFlowId());
        record.setMainFlowNodeId(node.getNodeId());
        record.setParentFlowNodeId(0L);
        records.add(record);

        FlowNodeBO nextNodeBO = manager.nextNode(record.getFlowNodeId(),record.getParentFlowNodeId());
        System.out.println(nextNodeBO);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), nextNodeBO.getParentFlowNode());
        System.out.println(nextNodeBO);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), nextNodeBO.getParentFlowNode());
        System.out.println(nextNodeBO);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), nextNodeBO.getParentFlowNode());
        System.out.println(nextNodeBO);
        nextNodeBO = manager.nextNode(nextNodeBO.getFlowNode(), nextNodeBO.getParentFlowNode());
        System.out.println(nextNodeBO);

        System.out.println("=====================================");
        saveRecord(records);
    }

    public static void saveRecord(List<FlowRecord> records) {
        for (FlowRecord record: records) {
            System.out.println(record);
        }
    }
}
