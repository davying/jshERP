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
 * 入出库记录表
 * </p>
 *
 * @author dawei
 * @since 2023-07-12 21:59:29
 */
@Getter
@Setter
@TableName("jsh_warehouse_management")
@ApiModel(value = "WarehouseManagement对象", description = "入出库记录表")
public class WarehouseManagement {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("货品编号")
    @TableField("commodity_no")
    private String commodityNo;

    @ApiModelProperty("货品名称")
    @TableField("commodity_name")
    private String commodityName;

    @ApiModelProperty("类型(全部出库/部分出库/入库/全部发货/部分发货)")
    @TableField("`type`")
    private String type;

    @ApiModelProperty("订单编号")
    @TableField("depot_id")
    private String depotId;

    @ApiModelProperty("颜色")
    @TableField("color")
    private String color;

    @ApiModelProperty("S尺码")
    @TableField("size1")
    private Integer size1 = 0;

    @ApiModelProperty("M尺码")
    @TableField("size2")
    private Integer size2 = 0;

    @ApiModelProperty("L尺码")
    @TableField("size3")
    private Integer size3 = 0;

    @ApiModelProperty("XL尺码")
    @TableField("size4")
    private Integer size4 = 0;

    @ApiModelProperty("2XL尺码")
    @TableField("size5")
    private Integer size5 = 0;

    @ApiModelProperty("3XL尺码")
    @TableField("size6")
    private Integer size6 = 0;

    @ApiModelProperty("4XL尺码")
    @TableField("size7")
    private Integer size7 = 0;

    @ApiModelProperty("5XL尺码")
    @TableField("size8")
    private Integer size8 = 0;

    @ApiModelProperty("6XL尺码")
    @TableField("size9")
    private Integer size9 = 0;

    @ApiModelProperty("7XL尺码")
    @TableField("size10")
    private Integer size10 = 0;

    @ApiModelProperty("XS尺码")
    @TableField("size11")
    private Integer size11 = 0;

    @ApiModelProperty("数量")
    @TableField("numbers")
    private Integer numbers = 0;

    @ApiModelProperty("单价(元)")
    @TableField("price")
    private Double price;

    @ApiModelProperty("到货时间")
    @TableField("in_time")
    private Date inTime;

    @ApiModelProperty("发货日期")
    @TableField("delivery_date")
    private Date deliveryDate;

    @ApiModelProperty("物流运费(元)")
    @TableField("logistics_freight")
    private BigDecimal logisticsFreight;

    @ApiModelProperty("物流方式")
    @TableField("logistics_mode")
    private String logisticsMode;
    @ApiModelProperty("实发数量")
    @TableField("actual_quantity")
    private Integer actualQuantity;
    @ApiModelProperty("发货单号")
    @TableField("shipping_order_number")
    private String shippingOrderNumber;

    @ApiModelProperty("租户id")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    @TableField("delete_flag")
    @TableLogic
    private String deleteFlag;

    @ApiModelProperty("入库备注")
    @TableField("remark")
    private String remark;

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

    @ApiModelProperty("当前页")
    @TableField(exist = false)
    private Integer currentPage = 1;

    @ApiModelProperty("页数量")
    @TableField(exist = false)
    private Integer pageSize = 10;
    @TableField(exist = false)
    private String depotIds;

    @ApiModelProperty("入库日期")
    @TableField(exist = false)
    private String inTimeStr;

    @ApiModelProperty("业务员")
    @TableField("sales_man")
    private String salesMan;

    @TableField(exist = false)
    private String action;
}
