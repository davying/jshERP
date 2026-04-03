//package com.jsh.erp.datasource.entitie.custom;
//
//import com.baomidou.mybatisplus.annotation.*;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
///**
// * <p>
// * 单据主表
// * </p>
// *
// * @author dawei
// * @since 2023-07-11 22:42:34
// */
//@Getter
//@Setter
//@TableName("jsh_depot_head")
//@ApiModel(value = "DepotHead对象", description = "单据主表")
//public class DepotHead {
//
//    @ApiModelProperty("主键")
//    @TableId(value = "id", type = IdType.AUTO)
//    private Long id;
//
//    @ApiModelProperty("类型(出库/入库)")
//    @TableField("`type`")
//    private String type;
//
//    @ApiModelProperty("出入库分类")
//    @TableField("sub_type")
//    private String subType;
//
//    @ApiModelProperty("初始票据号")
//    @TableField("default_number")
//    private String defaultNumber;
//
//    @ApiModelProperty("票据号")
//    @TableField("`number`")
//    private String number;
//
//    @ApiModelProperty("创建时间")
//    @TableField(value = "create_time", fill = FieldFill.INSERT)
//    private Date createTime;
//
//    @ApiModelProperty("出入库时间")
//    @TableField("oper_time")
//    private Date operTime;
//
//    @ApiModelProperty("供应商id")
//    @TableField("organ_id")
//    private Long organId;
//
//    @ApiModelProperty("操作员")
//    @TableField("creator")
//    private Long creator;
//
//    @ApiModelProperty("账户id")
//    @TableField("account_id")
//    private Long accountId;
//
//    @ApiModelProperty("变动金额(收款/付款)")
//    @TableField("change_amount")
//    private BigDecimal changeAmount;
//
//    @ApiModelProperty("找零金额")
//    @TableField("back_amount")
//    private BigDecimal backAmount;
//
//    @ApiModelProperty("合计金额")
//    @TableField("total_price")
//    private BigDecimal totalPrice;
//
//    @ApiModelProperty("付款类型(现金、记账等)")
//    @TableField("pay_type")
//    private String payType;
//
//    @ApiModelProperty("单据类型")
//    @TableField("bill_type")
//    private String billType;
//
//    @ApiModelProperty("备注")
//    @TableField("remark")
//    private String remark;
//
//    @ApiModelProperty("附件名称")
//    @TableField("file_name")
//    private String fileName;
//
//    @ApiModelProperty("业务员（可以多个）")
//    @TableField("sales_man")
//    private String salesMan;
//
//    @ApiModelProperty("多账户ID列表")
//    @TableField("account_id_list")
//    private String accountIdList;
//
//    @ApiModelProperty("多账户金额列表")
//    @TableField("account_money_list")
//    private String accountMoneyList;
//
//    @ApiModelProperty("优惠率")
//    @TableField("discount")
//    private BigDecimal discount;
//
//    @ApiModelProperty("优惠金额")
//    @TableField("discount_money")
//    private BigDecimal discountMoney;
//
//    @ApiModelProperty("优惠后金额")
//    @TableField("discount_last_money")
//    private BigDecimal discountLastMoney;
//
//    @ApiModelProperty("销售或采购费用合计")
//    @TableField("other_money")
//    private BigDecimal otherMoney;
//
//    @ApiModelProperty("订金")
//    @TableField("deposit")
//    private BigDecimal deposit;
//
//    @ApiModelProperty("状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、9审核中")
//    @TableField("`status`")
//    private String status;
//
//    @ApiModelProperty("采购状态，0未采购、2完成采购、3部分采购")
//    @TableField("purchase_status")
//    private String purchaseStatus;
//
//    @ApiModelProperty("单据来源，0-pc，1-手机")
//    @TableField("`source`")
//    private String source;
//
//    @ApiModelProperty("关联订单号")
//    @TableField("link_number")
//    private String linkNumber;
//
//    @ApiModelProperty("租户id")
//    @TableField("tenant_id")
//    private Long tenantId;
//
//    @ApiModelProperty("删除标记，0未删除，1删除")
//    @TableField("delete_flag")
//    private String deleteFlag;
//
//    @ApiModelProperty("客户")
//    @TableField("customer")
//    private String customer;
//
//    @ApiModelProperty("颜色")
//    @TableField("color")
//    private String color;
//
//    @ApiModelProperty("货品编码")
//    @TableField("commodity_no")
//    private String commodityNo;
//
//    @ApiModelProperty("S尺码")
//    @TableField("size1")
//    private String size1;
//
//    @ApiModelProperty("M尺码")
//    @TableField("size2")
//    private String size2;
//
//    @ApiModelProperty("L尺码")
//    @TableField("size3")
//    private String size3;
//
//    @ApiModelProperty("XL尺码")
//    @TableField("size4")
//    private String size4;
//
//    @ApiModelProperty("2XL尺码")
//    @TableField("size5")
//    private String size5;
//
//    @ApiModelProperty("3XL尺码")
//    @TableField("size6")
//    private String size6;
//
//    @ApiModelProperty("4XL尺码")
//    @TableField("size7")
//    private String size7;
//
//    @ApiModelProperty("5XL尺码")
//    @TableField("size8")
//    private String size8;
//
//    @ApiModelProperty("6XL尺码")
//    @TableField("size9")
//    private String size9;
//
//    @ApiModelProperty("7XL尺码")
//    @TableField("size10")
//    private String size10;
//
//    @ApiModelProperty("XS尺码")
//    @TableField("size11")
//    private String size11;
//
//    @ApiModelProperty("数量")
//    @TableField("numbers")
//    private String numbers;
//
//    @ApiModelProperty("单位")
//    @TableField("unit")
//    private String unit;
//
//    @ApiModelProperty("下单备注")
//    @TableField("order_remark")
//    private String orderRemark;
//
//    @ApiModelProperty("运费付款方")
//    @TableField("freight_payer")
//    private String freightPayer;
//
//    @ApiModelProperty("地址")
//    @TableField("address")
//    private String address;
//
//    @ApiModelProperty("电话")
//    @TableField("telephone")
//    private String telephone;
//
//    @ApiModelProperty("联系人")
//    @TableField("contact_person")
//    private String contactPerson;
//
//    @ApiModelProperty("仓库备注")
//    @TableField("warehouse_remark")
//    private String warehouseRemark;
//
//    @ApiModelProperty("印刷单价")
//    @TableField("printing_unit_price")
//    private BigDecimal printingUnitPrice;
//
//    @ApiModelProperty("印刷总价")
//    @TableField("total_printing_price")
//    private BigDecimal totalPrintingPrice;
//
//    @ApiModelProperty("印刷人员")
//    @TableField("printer")
//    private String printer;
//
//    @ApiModelProperty("印刷对帐日期")
//    @TableField("printing_reconciliation_date")
//    private Date printingReconciliationDate;
//
//    @ApiModelProperty("烫画人员")
//    @TableField("ironing_staff")
//    private String ironingStaff;
//
//    @ApiModelProperty("烫画单价")
//    @TableField("hot_painting_unit_price")
//    private BigDecimal hotPaintingUnitPrice;
//
//    @ApiModelProperty("烫画总价")
//    @TableField("hot_stamping_painting_total_price")
//    private BigDecimal hotStampingPaintingTotalPrice;
//
//    @ApiModelProperty("叠单价")
//    @TableField("stacked_unit_price")
//    private BigDecimal stackedUnitPrice;
//
//    @ApiModelProperty("叠总价")
//    @TableField("total_stack_price")
//    private BigDecimal totalStackPrice;
//
//    @ApiModelProperty("烫叠对帐日期")
//    @TableField("stamping_reconciliation_date")
//    private Date stampingReconciliationDate;
//
//    @ApiModelProperty("刺锈单价")
//    @TableField("rust_unit_price")
//    private BigDecimal rustUnitPrice;
//
//    @ApiModelProperty("刺绣总价")
//    @TableField("embroidery_total_price")
//    private BigDecimal embroideryTotalPrice;
//
//    @ApiModelProperty("烫叠对帐日期")
//    @TableField("embroidery_reconciliation_date")
//    private Date embroideryReconciliationDate;
//
//    @ApiModelProperty("刺绣备注")
//    @TableField("embroidery_remark")
//    private String embroideryRemark;
//
//    @ApiModelProperty("财务单价")
//    @TableField("financial_unit_price")
//    private BigDecimal financialUnitPrice;
//
//    @ApiModelProperty("财务总价")
//    @TableField("total_financial_price")
//    private BigDecimal totalFinancialPrice;
//
//    @ApiModelProperty("财务定金")
//    @TableField("financial_deposit")
//    private BigDecimal financialDeposit;
//
//    @ApiModelProperty("财务定金到账日期")
//    @TableField("financial_deposit_arrival_date")
//    private Date financialDepositArrivalDate;
//
//    @ApiModelProperty("财务定金到账方式")
//    @TableField("financial_deposit_to_account_method")
//    private String financialDepositToAccountMethod;
//
//    @ApiModelProperty("财务尾款")
//    @TableField("financial_balance")
//    private BigDecimal financialBalance;
//
//    @ApiModelProperty("财务实收尾款")
//    @TableField("financial_staff")
//    private BigDecimal financialStaff;
//
//    @ApiModelProperty("财务备注")
//    @TableField("financial_remarks")
//    private String financialRemarks;
//
//    @ApiModelProperty("财务扣款、费用")
//    @TableField("financial_deductions_fee")
//    private BigDecimal financialDeductionsFee;
//
//    @ApiModelProperty("财务尾款到账日期")
//    @TableField("financial_final_payment_date")
//    private Date financialFinalPaymentDate;
//
//    @ApiModelProperty("财务尾款到账方式")
//    @TableField("financial_final_payment_to_account_method")
//    private String financialFinalPaymentToAccountMethod;
//
//    @ApiModelProperty("财务回扣")
//    @TableField("financial_rebate")
//    private BigDecimal financialRebate;
//
//    @ApiModelProperty("财务回扣备注")
//    @TableField("financial_rebate_remarks")
//    private String financialRebateRemarks;
//
//    @ApiModelProperty("财务其他支出费用")
//    @TableField("other_expenses_in_finance")
//    private BigDecimal otherExpensesInFinance;
//
//    @ApiModelProperty("财务其他支出备注")
//    @TableField("financial_other_Expenditure_remarks")
//    private String financialOtherExpenditureRemarks;
//
//    @ApiModelProperty("财务是否含税")
//    @TableField("does_financial_affairs_include_tax")
//    private String doesFinancialAffairsIncludeTax;
//
//    @ApiModelProperty("财务开票抬头")
//    @TableField("financial_invoice_payable")
//    private String financialInvoicePayable;
//
//    @ApiModelProperty("财务税额")
//    @TableField("financial_tax_amount")
//    private BigDecimal financialTaxAmount;
//
//    @ApiModelProperty("财务成本单价")
//    @TableField("financial_cost_unit_price")
//    private BigDecimal financialCostUnitPrice;
//
//    @ApiModelProperty("财务成本金额")
//    @TableField("financial_cost_amount")
//    private BigDecimal financialCostAmount;
//
//    @ApiModelProperty("订单提成")
//    @TableField("order_commission")
//    private BigDecimal orderCommission;
//
//
//}
