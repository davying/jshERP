package com.jsh.erp.datasource.entitie.custom;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * <p>
 * 样品记录表
 * </p>
 *
 * @author dawei
 * @since 2024-02-29 13:32:09
 */
@Getter
@Setter
@TableName("jsh_sample_management_old")
@ApiModel(value = "SampleManagementOld对象", description = "样品记录表")
public class SampleManagementOld {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("货品编号")
    @TableField("commodity_no")
    private String commodityNo;

    @ApiModelProperty("颜色")
    @TableField("color")
    private String color;

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

    @ApiModelProperty("到货时间")
    @TableField("in_time")
    private LocalDateTime inTime;

    @ApiModelProperty("样衣单价(元)")
    @TableField("price")
    private BigDecimal price;

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
    private LocalDateTime createTime;

    @ApiModelProperty("更新时间")
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @ApiModelProperty("货品名称")
    @TableField("commodity_name")
    private String commodityName;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;


}
