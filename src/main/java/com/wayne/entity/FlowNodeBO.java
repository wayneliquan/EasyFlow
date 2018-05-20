package com.wayne.entity;

import lombok.Data;

@Data
public class FlowNodeBO {
    private FlowNode flowNode;
    private FlowNode parentFlowNode;
}
