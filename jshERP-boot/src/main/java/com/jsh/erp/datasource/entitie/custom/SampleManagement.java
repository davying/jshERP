package com.jsh.erp.datasource.entitie.custom;

import com.baomidou.mybatisplus.annotation.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * <p>
 * 样品记录表
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 19:52:53
 */
@Getter
@Setter
@TableName("jsh_sample_management")
@ApiModel(value = "SampleManagement对象", description = "样品记录表")
public class SampleManagement {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("货品编号")
    @TableField("commodity_no")
    private String commodityNo;

    @ApiModelProperty("货品名称")
    @TableField("commodity_name")
    private String commodityName;

    @ApiModelProperty("颜色")
    @TableField("color")
    private String color;

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

    @ApiModelProperty("到货时间")
    @TableField("in_time")
    private Date inTime;

    @ApiModelProperty("样衣单价(元)")
    @TableField("price")
    private Double price;

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
    private Date createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("当前页")
    @TableField(exist = false)
    private Integer currentPage = 1;

    @ApiModelProperty("页数量")
    @TableField(exist = false)
    private Integer pageSize = 10;

    @ApiModelProperty("入库日期")
    @TableField(exist = false)
    private String inTimeStr;
}
