package com.jsh.erp.datasource.entities;

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
 * 单据主表
 * </p>
 *
 * @author dawei
 * @since 2023-07-11 22:42:34
 */
@Getter
@Setter
@TableName("jsh_depot_head")
@ApiModel(value = "DepotHead对象", description = "单据主表")
public class DepotHead {

    @ApiModelProperty("主键")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    @ApiModelProperty("类型(出库/入库)")
    @TableField("`type`")
    private String type;

    @ApiModelProperty("销售类型(自有仓库/转销单/定制款)")
    @TableField("`sale_type`")
    private String saleType;

    @ApiModelProperty("出入库分类")
    @TableField("sub_type")
    private String subType;

    @ApiModelProperty("初始票据号")
    @TableField("default_number")
    private String defaultNumber;

    @ApiModelProperty("票据号")
    @TableField("`number`")
    private String number;

    @ApiModelProperty("创建时间")
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private Date createTime;

    @ApiModelProperty("出入库时间")
    @TableField("oper_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date operTime;

    @ApiModelProperty("供应商id")
    @TableField("organ_id")
    private Long organId;

    @ApiModelProperty("操作员")
    @TableField("creator")
    private Long creator;

    @ApiModelProperty("账户id")
    @TableField("account_id")
    private Long accountId;

    @ApiModelProperty("变动金额(收款/付款)")
    @TableField("change_amount")
    private BigDecimal changeAmount;

    @ApiModelProperty("找零金额")
    @TableField("back_amount")
    private BigDecimal backAmount;

    @ApiModelProperty("合计金额")
    @TableField("total_price")
    private BigDecimal totalPrice;

    @ApiModelProperty("付款类型(现金、记账等)")
    @TableField("pay_type")
    private String payType;

    @ApiModelProperty("单据类型")
    @TableField("bill_type")
    private String billType;

    @ApiModelProperty("备注")
    @TableField("remark")
    private String remark;

    @ApiModelProperty("附件名称")
    @TableField("file_name")
    private String fileName;

    @ApiModelProperty("业务员（可以多个）")
    @TableField("sales_man")
    private String salesMan;

    @ApiModelProperty("多账户ID列表")
    @TableField("account_id_list")
    private String accountIdList;

    @ApiModelProperty("多账户金额列表")
    @TableField("account_money_list")
    private String accountMoneyList;

    @ApiModelProperty("优惠率")
    @TableField("discount")
    private BigDecimal discount;

    @ApiModelProperty("优惠金额")
    @TableField("discount_money")
    private BigDecimal discountMoney;

    @ApiModelProperty("优惠后金额")
    @TableField("discount_last_money")
    private BigDecimal discountLastMoney;

    @ApiModelProperty("销售或采购费用合计")
    @TableField("other_money")
    private BigDecimal otherMoney;

    @ApiModelProperty("订金")
    @TableField("deposit")
    private BigDecimal deposit;

    @ApiModelProperty("状态，0未审核、1已审核、2完成采购|销售、3部分采购|销售、4仓库已提交、9业务员已提交")
    @TableField("`status`")
    private String status;

    @ApiModelProperty("采购状态，0未采购、2完成采购、3部分采购、4部分出库、5全部出库")
    @TableField("purchase_status")
    private String purchaseStatus;

    @ApiModelProperty("物流状态，0未发货、1部分发货、2全部发货")
    @TableField("logistics_status")
    private Integer logisticsStatus;

    @ApiModelProperty("单据来源，0-pc，1-手机")
    @TableField("`source`")
    private String source;

    @ApiModelProperty("关联订单号")
    @TableField("link_number")
    private String linkNumber;

    @ApiModelProperty("租户id")
    @TableField("tenant_id")
    private Long tenantId;

    @ApiModelProperty("删除标记，0未删除，1删除")
    @TableField("delete_flag")
    @TableLogic
    private String deleteFlag;

    @ApiModelProperty("客户")
    @TableField("customer")
    private String customer;

    @ApiModelProperty("颜色")
    @TableField("color")
    private String color;

    @ApiModelProperty("货品编码")
    @TableField("commodity_no")
    private String commodityNo;

    @ApiModelProperty("货品名称")
    @TableField("commodity_name")
    private String commodityName;

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

    @ApiModelProperty("单位")
    @TableField("unit")
    private String unit;

    @ApiModelProperty("下单备注")
    @TableField("order_remark")
    private String orderRemark;

    @ApiModelProperty("运费付款方")
    @TableField("freight_payer")
    private String freightPayer;

    @ApiModelProperty("地址")
    @TableField("address")
    private String address;

    @ApiModelProperty("电话")
    @TableField("telephone")
    private String telephone;

    @ApiModelProperty("联系人")
    @TableField("contact_person")
    private String contactPerson;

    @ApiModelProperty("LOGO制作")
    @TableField("logo_mark")
    private String logoMark;

    @ApiModelProperty("开票税种")
    @TableField("billing_rate")
    private String billingRate;


    @ApiModelProperty("仓库备注")
    @TableField("warehouse_remark")
    private String warehouseRemark;

    @ApiModelProperty("印刷单价")
    @TableField("printing_unit_price")
    private BigDecimal printingUnitPrice;

    @ApiModelProperty("印刷总价")
    @TableField("total_printing_price")
    private BigDecimal totalPrintingPrice;

    @ApiModelProperty("印刷人员")
    @TableField("printer")
    private String printer;

    @ApiModelProperty("印刷对帐日期")
    @TableField("printing_reconciliation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date printingReconciliationDate;

    @ApiModelProperty("烫画人员")
    @TableField("ironing_staff")
    private String ironingStaff;

    @ApiModelProperty("烫画单价")
    @TableField("hot_painting_unit_price")
    private BigDecimal hotPaintingUnitPrice;

    @ApiModelProperty("烫画总价")
    @TableField("hot_stamping_painting_total_price")
    private BigDecimal hotStampingPaintingTotalPrice;

    @ApiModelProperty("叠单价")
    @TableField("stacked_unit_price")
    private BigDecimal stackedUnitPrice;

    @ApiModelProperty("叠总价")
    @TableField("total_stack_price")
    private BigDecimal totalStackPrice;

    @ApiModelProperty("叠人员")
    @TableField("stack_staff")
    private String stackStaff;

    @ApiModelProperty("刺绣人员")
    @TableField("rust_staff")
    private String rustStaff;

    @ApiModelProperty("烫叠对帐日期")
    @TableField("stamping_reconciliation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date stampingReconciliationDate;

    @ApiModelProperty("刺锈单价")
    @TableField("rust_unit_price")
    private BigDecimal rustUnitPrice;

    @ApiModelProperty("刺绣总价")
    @TableField("embroidery_total_price")
    private BigDecimal embroideryTotalPrice;

    @ApiModelProperty("刺绣对账日期")
    @TableField("embroidery_reconciliation_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date embroideryReconciliationDate;

    @ApiModelProperty("刺绣备注")
    @TableField("embroidery_remark")
    private String embroideryRemark;

    @ApiModelProperty("销售单价")
    @TableField("financial_unit_price")
    private BigDecimal financialUnitPrice;

    @ApiModelProperty("销售总价")
    @TableField("total_financial_price")
    private BigDecimal totalFinancialPrice;

    @ApiModelProperty("订单定金")
    @TableField("financial_deposit")
    private BigDecimal financialDeposit;

    @ApiModelProperty("财务定金到账日期")
    @TableField("financial_deposit_arrival_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date financialDepositArrivalDate;

    @ApiModelProperty("财务定金到账方式")
    @TableField("financial_deposit_to_account_method")
    private String financialDepositToAccountMethod;

    @ApiModelProperty("财务应收尾款")
    @TableField("financial_balance")
    private BigDecimal financialBalance;

    @ApiModelProperty("财务实收尾款")
    @TableField("financial_staff")
    private BigDecimal financialStaff;

    @ApiModelProperty("财务备注")
    @TableField("financial_remarks")
    private String financialRemarks;

    @ApiModelProperty("财务扣款、费用")
    @TableField("financial_deductions_fee")
    private BigDecimal financialDeductionsFee;

    @ApiModelProperty("财务尾款到账日期")
    @TableField("financial_final_payment_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date financialFinalPaymentDate;

    @ApiModelProperty("财务尾款到账方式")
    @TableField("financial_final_payment_to_account_method")
    private String financialFinalPaymentToAccountMethod;

    @ApiModelProperty("财务回扣")
    @TableField("financial_rebate")
    private BigDecimal financialRebate;

    @ApiModelProperty("财务回扣备注")
    @TableField("financial_rebate_remarks")
    private String financialRebateRemarks;

    @ApiModelProperty("财务其他支出费用")
    @TableField("other_expenses_in_finance")
    private BigDecimal otherExpensesInFinance;

    @ApiModelProperty("财务其他支出费用2")
    @TableField("other_expenses_in_finance2")
    private BigDecimal otherExpensesInFinance2;

    @ApiModelProperty("财务其他支出备注")
    @TableField("financial_other_Expenditure_remarks")
    private String financialOtherExpenditureRemarks;

    @ApiModelProperty("财务是否含税")
    @TableField("does_financial_affairs_include_tax")
    private String doesFinancialAffairsIncludeTax;

    @ApiModelProperty("财务开票抬头")
    @TableField("financial_invoice_payable")
    private String financialInvoicePayable;

    @ApiModelProperty("财务税额")
    @TableField("financial_tax_amount")
    private BigDecimal financialTaxAmount;

    @ApiModelProperty("财务成本单价")
    @TableField("financial_cost_unit_price")
    private BigDecimal financialCostUnitPrice;

    @ApiModelProperty("财务成本金额")
    @TableField("financial_cost_amount")
    private BigDecimal financialCostAmount;

    @ApiModelProperty("订单提成")
    @TableField("order_commission")
    private BigDecimal orderCommission;

    @ApiModelProperty("订单物流费")
    @TableField(exist = false)
    private BigDecimal logisticsFreight;

    @TableField(exist = false)
    private BigDecimal logisticsFreightNew;

    @ApiModelProperty("尾款是否结清")
    @TableField("end_staff")
    private String endStaff;

    @ApiModelProperty("是否换拉链袋")
    @TableField("is_change_bag")
    private String isChangeBag;

    @ApiModelProperty("状态名")
    @TableField("status_name")
    private String statusName;

    @ApiModelProperty("开票备注")
    @TableField("invoice_remark")
    private String invoiceRemark;

    @TableField(exist = false)
    private String action;

    @TableField(exist = false)
    private String commodityNoStr;

    @ApiModelProperty("分成类型 1:55分成，2:46分成")
    @TableField("divide_type")
    private Integer divideType;

    @ApiModelProperty("付款日款")
    @TableField("xl_pay_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date xlPayDate;

    @ApiModelProperty("收款单位")
    @TableField("xl_receive_unit")
    private String xlReceiveUnit;

    @ApiModelProperty("小路备注")
    @TableField("xl_remark")
    private String xlRemark;

    @ApiModelProperty("1业务员已确认0未确认")
    @TableField("sales_confirmed")
    private Integer salesConfirmed;

    @ApiModelProperty("财务备注 2")
    @TableField("financial_remarks2")
    private Integer financialRemarks2;
}
