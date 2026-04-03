package com.jsh.erp.datasource.entitie.custom;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 样品出库日志表
 * </p>
 *
 * @author dawei
 * @since 2024-02-29 13:32:08
 */
@Getter
@Setter
@TableName("jsh_sample_management_log_old")
@ApiModel(value = "SampleManagementLogOld对象", description = "样品出库日志表")
public class SampleManagementLogOld {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("样品编号")
    @TableField("sample_id")
    private Long sampleId;

    @ApiModelProperty("S尺码")
    @TableField("size1")
    private Integer size1;

    @ApiModelProperty("M尺码")
    @TableField("size2")
    private Integer size2;

    @ApiModelProperty("L尺码")
    @TableField("size3")
    private Integer size3;

    @ApiModelProperty("XL尺码")
    @TableField("size4")
    private Integer size4;

    @ApiModelProperty("2XL尺码")
    @TableField("size5")
    private Integer size5;

    @ApiModelProperty("3XL尺码")
    @TableField("size6")
    private Integer size6;

    @ApiModelProperty("4XL尺码")
    @TableField("size7")
    private Integer size7;

    @ApiModelProperty("5XL尺码")
    @TableField("size8")
    private Integer size8;

    @ApiModelProperty("6XL尺码")
    @TableField("size9")
    private Integer size9;

    @ApiModelProperty("7XL尺码")
    @TableField("size10")
    private Integer size10;

    @ApiModelProperty("XS尺码")
    @TableField("size11")
    private Integer size11;

    @ApiModelProperty("数量")
    @TableField("numbers")
    private Integer numbers;

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
    private String deleteFlag;

    @ApiModelProperty("创建者")
    @TableField("create_by")
    private String createBy;

    @ApiModelProperty("更新者")
    @TableField("update_by")
    private String updateBy;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("单价")
    @TableField("price")
    private BigDecimal price;

    @ApiModelProperty("金额")
    @TableField("total_price")
    private BigDecimal totalPrice;

    @ApiModelProperty("已收样衣费")
    @TableField("sample_charge")
    private BigDecimal sampleCharge;

    @ApiModelProperty("货品编号")
    @TableField("commodity_no")
    private String commodityNo;

    @ApiModelProperty("货品名称")
    @TableField("commodity_name")
    private String commodityName;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("样衣单编号")
    @TableField("sample_number")
    private String sampleNumber;

    @TableField(exist = false)
    private String createTimeStr;
}
