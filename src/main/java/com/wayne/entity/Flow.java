package com.wayne.entity;

import lombok.Data;

import java.util.List;

@Data
public class Flow {
    private Long flowId;
    private String name;
    private List<FlowNode> flowNodeList;
    private Long startNodeId;
    private Long backNodeId;
}
