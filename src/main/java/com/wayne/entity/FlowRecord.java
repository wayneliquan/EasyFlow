package com.wayne.entity;

import lombok.Data;

@Data
public class FlowRecord {
    Long recordId;
    Long mainFlowId;
    Long mainFlowNodeId;
    Long parentFlowNodeId;
    Long flowId;
    Long flowNodeId;

}
