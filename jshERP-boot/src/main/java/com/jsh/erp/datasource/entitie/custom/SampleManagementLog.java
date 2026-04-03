package com.jsh.erp.datasource.entitie.custom;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 样品出库日志表
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:54
 */
@Getter
@Setter
@TableName("jsh_sample_management_log")
@ApiModel(value = "SampleManagementLog对象", description = "样品出库日志表")
public class SampleManagementLog {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("样品编号")
    @TableField("sample_id")
    private Long sampleId;

    @ApiModelProperty("样衣单编号")
    @TableField("sample_number")
    private String sampleNumber;

    @ApiModelProperty("S")
    @TableField("size1")
    private Integer size1;

    @ApiModelProperty("M")
    @TableField("size2")
    private Integer size2;

    @ApiModelProperty("L")
    @TableField("size3")
    private Integer size3;

    @ApiModelProperty("XL")
    @TableField("size4")
    private Integer size4;

    @ApiModelProperty("2XL")
    @TableField("size5")
    private Integer size5;

    @ApiModelProperty("3XL")
    @TableField("size6")
    private Integer size6;

    @ApiModelProperty("4XL")
    @TableField("size7")
    private Integer size7;

    @ApiModelProperty("5XL")
    @TableField("size8")
    private Integer size8;

    @ApiModelProperty("6XL")
    @TableField("size9")
    private Integer size9;

    @ApiModelProperty("7XL")
    @TableField("size10")
    private Integer size10;

    @ApiModelProperty("XS")
    @TableField("size11")
    private Integer size11;

    @ApiModelProperty("数量")
    @TableField("numbers")
    private Integer numbers;

    @ApiModelProperty("单价")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("金额")
    @TableField("total_price")
    private BigDecimal totalPrice;

    @ApiModelProperty("已收样衣费")
    @TableField("sample_charge")
    private BigDecimal sampleCharge;

    @ApiModelProperty("业务员人")
    @TableField("member")
    private String member;

    @ApiModelProperty("快递费(元)")
    @TableField("express_price")
    private BigDecimal expressPrice;

    @ApiModelProperty("租户id")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    @TableField("delete_flag")
    @TableLogic
    private String deleteFlag;

    @ApiModelProperty("创建者")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("更新者")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    @TableField(exist = false)
    private String createTimeStr;

    @ApiModelProperty("货品编号")
    @TableField("commodity_no")
    private String commodityNo;

    @ApiModelProperty("货品名称")
    @TableField("commodity_name")
    private String commodityName;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @TableField(exist = false)
    private String sampleColor;


}