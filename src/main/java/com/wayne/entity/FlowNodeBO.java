package com.wayne.entity;

import lombok.Data;

import java.util.List;

@Data
public class FlowNodeBO {
    private FlowNode flowNode;
    private List<Long> callStack;
}
