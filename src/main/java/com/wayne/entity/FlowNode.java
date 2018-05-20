package com.wayne.entity;

import lombok.Data;

@Data
public class FlowNode {
    public static final int TYPE_NODE = 0;
    public static final int TYPE_FLOW = 1;

    public static final int START_NODE_FLAG = -1;
    long nodeId;
    String name;
    Long flowId;
    int index;
    int type; // 流程或节点

    int backIndex;
    boolean flowBack;
    Long subFlowId;
    Flow flow;
    String roleIds;

    public boolean isFlow(){
        return TYPE_FLOW == type;
    }

    public boolean isNode(){
        return TYPE_NODE == type;
    }

    public boolean isStartNode() {
        return START_NODE_FLAG == backIndex;
    }
}
