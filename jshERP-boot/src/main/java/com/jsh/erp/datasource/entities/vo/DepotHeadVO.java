package com.jsh.erp.datasource.entities.vo;

import lombok.Data;

import java.util.List;

@Data
public class DepotHeadVO {
    private String beginTime;
    private String endTime;
    private String commodityNo;
    private String commodityName;
    private String color;
    private String inTime;
    private List<String> creators;
    private Integer limit;
    private String years;
}
