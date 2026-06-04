<template>
  <j-modal
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :keyboard="false"
      :forceRender="true"
      v-bind:prefixNo="prefixNo"
      switchHelp
      switchFullscreen
      @cancel="handleCancel"
      :id="prefixNo"
      style="top:20px;height: 95%;"
  >
    <template slot="footer">
      <a-button @click="handleCancel">取消</a-button>
      <!--      <a-button-->
      <!--        v-if="checkFlag && isCanCheck"-->
      <!--        :loading="confirmLoading"-->
      <!--        @click="handleOkAndCheck"-->
      <!--        >保存并审核</a-button-->
      <!--      >-->
<!--      <a-button-->
<!--          :loading="confirmLoading"-->
<!--          @click="handleOkAndCheck"-->
<!--      >保存并审核</a-button>-->
      <a-button type="primary" :loading="confirmLoading" @click="handleOk"
      >保存</a-button
      >
      <!--发起多级审核-->
      <a-button v-if="!checkFlag" @click="handleWorkflow()" type="primary"
      >提交流程</a-button
      >
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="订单编号"
            >
              <a-input
                  placeholder="订单编号"
                  v-decorator.trim="['number']"
                  :readOnly="true"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="货品编号"
                data-step="3"
                data-title="货品编号"
                data-intro="货品编号"
            >
              <a-select
                  placeholder="请选择货品编号"
                  showSearch
                  v-decorator.trim="['commodityNo', validatorRules.commodityNo]"
              >
                <a-select-option
                    v-for="(site, index) in commodityList.options"
                    :key="index"
                    :value="site.value"
                >{{ site.label }}</a-select-option
                >
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="业务员"
                data-step="2"
                data-title="业务员"
            >
              <a-input disabled v-decorator.trim="['salesMan']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="客户"
                data-step="2"
                data-title="客户"
            >
              <a-input v-decorator.trim="['customer']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="颜色"
                data-step="2"
                data-title="颜色"
            >
              <a-select
                  placeholder="请选择颜色"
                  showSearch
                  v-decorator.trim="['color']"
              >
                <a-select-option
                    v-for="(site, index) in colorList.options"
                    :key="nativeName"
                    :value="site.nativeName"
                >{{ site.nativeName }}</a-select-option>
              </a-select>
<!--              <a-input v-decorator.trim="['color']" />-->
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="S"
                data-step="2"
                data-title="S"
            >
              <a-input v-decorator.trim="['size1', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="M"
                data-step="2"
                data-title="M"
            >
              <a-input v-decorator.trim="['size2', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="L"
                data-step="2"
                data-title="L"
            >
              <a-input v-decorator.trim="['size3', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="XL"
                data-step="2"
                data-title="XL"
            >
              <a-input v-decorator.trim="['size4', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="2XL"
                data-step="2"
                data-title="2XL"
            >
              <a-input v-decorator.trim="['size5', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="3XL"
                data-step="2"
                data-title="3XL"
            >
              <a-input v-decorator.trim="['size6', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="4XL"
                data-step="2"
                data-title="4XL"
            >
              <a-input v-decorator.trim="['size7', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="5XL"
                data-step="2"
                data-title="5XL"
            >
              <a-input v-decorator.trim="['size8', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="6XL"
                data-step="2"
                data-title="6XL"
            >
              <a-input v-decorator.trim="['size9', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="7XL"
                data-step="2"
                data-title="7XL"
            >
              <a-input v-decorator.trim="['size10', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="XS"
                data-step="2"
                data-title="XS"
            >
              <a-input v-decorator.trim="['size11', validatorRules.numbers]"  @change="handleSizeChange" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="总数量"
                data-step="2"
                data-title="总数量"
            >
              <a-input disabled v-decorator.trim="['numbers']" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="单位"
                data-step="2"
                data-title="单位"
            >
              <a-input v-decorator.trim="['unit']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="下单备注"
                data-step="2"
                data-title="下单备注"
            >
              <a-input disabled v-decorator.trim="['orderRemark']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="运费付款方"
                data-step="2"
                data-title="运费付款方"
            >
              <a-input v-decorator.trim="['freightPayer']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="地址"
                data-step="2"
                data-title="地址"
            >
              <a-input v-decorator.trim="['address']" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="电话"
                data-step="2"
                data-title="电话"
            >
              <a-input v-decorator.trim="['telephone']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="联系人"
                data-step="2"
                data-title="联系人"
            >
              <a-input v-decorator.trim="['contactPerson']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="仓库备注"
            >
              <a-input v-decorator.trim="['warehouseRemark']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷单价"
            >
              <a-input v-decorator.trim="['printingUnitPrice',validatorRules.changeAmount]" @change="handlePrintChange"/>
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷总价"
            >
              <a-input disabled v-decorator.trim="['totalPrintingPrice']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷人员"
            >
              <a-input v-decorator.trim="['printer']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷对帐日期"
            >
              <j-date
                  style="width: 240px"
                  v-decorator.trim="['printingReconciliationDate']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫画人员"
            >
              <a-input v-decorator.trim="['ironingStaff']" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫画单价"
            >
              <a-input v-decorator.trim="['hotPaintingUnitPrice',validatorRules.changeAmount]" @change="handleHotPaintingChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫画总价"
            >
              <a-input disabled v-decorator.trim="['hotStampingPaintingTotalPrice']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="叠单价"
            >
              <a-input v-decorator.trim="['stackedUnitPrice',validatorRules.changeAmount]" @change="handleStackedChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="叠总价"
            >
              <a-input disabled v-decorator.trim="['totalStackPrice']" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫叠对帐日期"
            >
              <j-date
                  style="width: 240px"
                  v-decorator.trim="['stampingReconciliationDate']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺锈单价"
            >
              <a-input v-decorator.trim="['rustUnitPrice',validatorRules.changeAmount]" @change="handleEmbroideryChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺绣总价"
            >
              <a-input disabled v-decorator.trim="['embroideryTotalPrice']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫叠对帐日期"
            >
              <j-date
                  style="width: 240px"
                  v-decorator.trim="['embroideryReconciliationDate']" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺绣备注"
            >
              <a-input v-decorator.trim="['embroideryRemark']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="销售单价"
            >

              <a-input placeholder="请输入销售单价" v-decorator.trim="['financialUnitPrice',validatorRules.changeAmount]"
                       @change="handleUnitChange"/>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="销售总价"
            >
              <a-input disabled v-decorator.trim="['totalFinancialPrice']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="开票税种"
            >
              <a-select v-decorator.trim="['billingRate']" placeholder="请选择开票种类" @change="changeMode">
                <a-select-option value="不开票">不开票</a-select-option>
                <a-select-option value="增票11个点">增票11个点</a-select-option>
                <a-select-option value="普票2个点">普票2个点</a-select-option>
                <a-select-option value="普票3个点">普票3个点</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="税额"
            >
              <a-input v-decorator.trim="['financialTaxAmount']" />
              默认按照：税率*订单总价
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="定金"
            >
              <a-input placeholder="请输入定金"
                       v-decorator.trim="['financialDeposit',validatorRules.changeAmount]"
                       @change="getFinancialBalance()"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="定金到账日期"
            >
              <j-date placeholder="请选择定金到账日期"
                      style="width: 240px"
                      v-decorator.trim="['financialDepositArrivalDate']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="定金到账方式"
            >
              <a-input placeholder="请输入定金到账方式"
                       v-decorator.trim="['financialDepositToAccountMethod']"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="应收尾款"
            >
              <a-input disabled
                       v-decorator.trim="['financialBalance']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="实收尾款"
            >
              <a-input placeholder="请输入实收尾款"
                       v-decorator.trim="['financialStaff',validatorRules.changeAmount]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="尾款是否结清"
            >
              <a-select v-decorator="['endStaff']">
                <a-select-option value="是">是</a-select-option>
                <a-select-option value="否">否</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="财务备注"
            >
              <a-textarea placeholder="请输入财务备注" onmouseover="this.title=this.value"
                       v-decorator.trim="['financialRemarks']"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="扣款、费用"
            >
              <a-input placeholder="请输入扣款、费用"
                       v-decorator.trim="['financialDeductionsFee',validatorRules.changeAmount]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="尾款到账日期"
            >
              <j-date placeholder="请选择尾款到账日期"
                      v-decorator.trim="['financialFinalPaymentDate']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="尾款到账方式"
            >
              <a-input placeholder="请输入尾款到账方式"
                      v-decorator.trim="['financialFinalPaymentToAccountMethod']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="订单回扣"
            >
              <a-input placeholder="请输入订单回扣"
                       v-decorator.trim="['financialRebate',validatorRules.changeAmount]"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="订单回扣备注"
            >
              <a-input placeholder="请输入订单回扣备注"
                       v-decorator.trim="['financialRebateRemarks']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="其他支出费用"
            >
              <a-input placeholder="请输入其他支出费用"
                       v-decorator.trim="['otherExpensesInFinance',validatorRules.changeAmount]"
              />
            </a-form-item>
          </a-col>
            <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="其他支出费用2"
            >
              <a-input placeholder="请输入其他支出费用2"
                       v-decorator.trim="['otherExpensesInFinance2',validatorRules.changeAmount]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="其他支出备注"
            >
              <a-input placeholder="请输入其他支出备注"
                       v-decorator.trim="['financialOtherExpenditureRemarks']"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="开票抬头"
            >
              <a-input placeholder="请输入开票抬头"
                       v-decorator.trim="['financialInvoicePayable']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="成本单价"
            >
              <a-input placeholder="请输入成本单价"
                       v-decorator.trim="['financialCostUnitPrice',validatorRules.changeAmount]"
                       @change="handleCostUnitChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="成本金额"
            >
              <a-input disabled
                       v-decorator.trim="['financialCostAmount',validatorRules.changeAmount]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="订单物流费"
            >
              <a-input disabled
                       v-decorator.trim="['logisticsFreight']"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="是否换拉链袋"
            >
              <a-select v-decorator.trim="['isChangeBag',validatorRules.isChangeBag]" placeholder="请选择是否换拉链袋">
                <a-select-option value="换拉链袋">换拉链袋</a-select-option>
                <a-select-option value="不换拉链袋 ">不换拉链袋 </a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="销售类型"
            >
              <a-select v-decorator="['saleType', validatorRules.saleType]">
                <a-select-option value="自有仓库">自有仓库</a-select-option>
                <a-select-option value="转销单">转销单</a-select-option>
                <a-select-option value="定制款">定制款</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="开票备注"
            >
              <a-input disabled v-decorator.trim="['invoiceRemark']"
              />
            </a-form-item>
          </a-col>
        </a-row>

      </a-form>
    </a-spin>
    <many-account-modal
        ref="manyAccountModalForm"
        @ok="manyAccountModalFormOk"
    ></many-account-modal>
    <import-item-modal
        ref="importItemModalForm"
        @ok="importItemModalFormOk"
    ></import-item-modal>
    <customer-modal
        ref="customerModalForm"
        @ok="customerModalFormOk"
    ></customer-modal>
    <account-modal
        ref="accountModalForm"
        @ok="accountModalFormOk"
    ></account-modal>
    <history-bill-list ref="historyBillListModalForm"></history-bill-list>
    <workflow-iframe ref="modalWorkflow"></workflow-iframe>
  </j-modal>
</template>
<script>
import pick from "lodash.pick";
import ManyAccountModal from "../dialog/ManyAccountModal";
import ImportItemModal from "../dialog/ImportItemModal";
import CustomerModal from "../../system/modules/CustomerModal";
import AccountModal from "../../system/modules/AccountModal";
import HistoryBillList from "../dialog/HistoryBillList";
import WorkflowIframe from "@/components/tools/WorkflowIframe";
import { FormTypes } from "@/utils/JEditableTableUtil";
import { JEditableTableMixin } from "@/mixins/JEditableTableMixin";
import { BillModalMixin } from "../mixins/BillModalMixin";
import { getMpListShort, handleIntroJs } from "@/utils/util";
import JSelectMultiple from "@/components/jeecg/JSelectMultiple";
import JUpload from "@/components/jeecg/JUpload";
import JDate from "@/components/jeecg/JDate";
import { getAction, httpAction } from "@/api/manage";
import Vue from "vue";
import JInput from "../../../components/jeecg/JInput";
export default {
  name: "SaleOrder2Modal",
  mixins: [JEditableTableMixin, BillModalMixin],
  components: {
    JInput,
    ManyAccountModal,
    ImportItemModal,
    CustomerModal,
    AccountModal,
    HistoryBillList,
    WorkflowIframe,
    JUpload,
    JDate,
    JSelectMultiple,
    VNodes: {
      functional: true,
      render: (h, ctx) => ctx.props.vnodes,
    },
  },
  data() {
    return {
      title: "操作",
      width: "1600px",
      moreStatus: false,
      loginUser: Vue.ls.get('loginUser'),
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      visible: false,
      operTimeStr: "",
      prefixNo: "XSDD",
      fileList: [],
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      refKeys: ["materialDataTable"],
      activeKey: "materialDataTable",
      orderLogisticsFee:'',
      materialTable: {
        loading: false,
        dataSource: [],
        columns: [
          {
            title: "订单编号",
            key: "number",
            width: 50,
            type: FormTypes.normal,
          },
          {
            title: "销售类型",
            key: "saleType",
            width: 50,
            type: FormTypes.normal,
          },
          {
            title: "业务员",
            key: "salesMan",
            width: 50,
            type: FormTypes.normal,
          },
          { title: "仓库备注", dataIndex: "warehouseRemark", width: 50 },
          { title: "印刷单价", dataIndex: "printingUnitPrice", width: 50 },
          { title: "印刷总价", dataIndex: "totalPrintingPrice", width: 50 },
          { title: "印刷人员", dataIndex: "printer", width: 50 },
          { title: "印刷对帐日期", dataIndex: "printingReconciliationDate", width: 50 },
          { title: "烫画人员", dataIndex: "ironingStaff", width: 50 },
          { title: "烫画单价", dataIndex: "hotPaintingUnitPrice", width: 50 },
          { title: "烫画总价", dataIndex: "hotStampingPaintingTotalPrice", width: 50 },
          { title: "叠单价", dataIndex: "stackedUnitPrice", width: 50 },
          { title: "叠人员", dataIndex: "stackStaff", width: 50 },
          { title: "叠总价", dataIndex: "totalStackPrice", width: 50 },
          { title: "烫叠对帐日期", dataIndex: "stampingReconciliationDate", width: 50 },
          { title: "刺锈单价", dataIndex: "rustUnitPrice", width: 50 },
          { title: "刺锈人员", dataIndex: "rustStaff", width: 50 },
          { title: "刺绣总价", dataIndex: "embroideryTotalPrice", width: 50 },
          { title: "刺绣对帐日期", dataIndex: "embroideryReconciliationDate", width: 50 },
          { title: "刺绣备注", dataIndex: "embroideryRemark", width: 50 },
          { title: "运费付款方", dataIndex: "freightPayer", width: 50 },
          { title: "地址", dataIndex: "address", width: 50 },
          { title: "电话", dataIndex: "telephone", width: 50 },
          { title: "联系人", dataIndex: "contactPerson", width: 50 },
          // { title: '规格', key: 'standard', width: '9%', type: FormTypes.normal },
          // { title: '型号', key: 'model', width: '9%', type: FormTypes.normal },
          // { title: '颜色', key: 'color', width: '5%', type: FormTypes.normal },
          // { title: '扩展信息', key: 'materialOther', width: '5%', type: FormTypes.normal },
          // { title: '库存', key: 'stock', width: '5%', type: FormTypes.normal },
          // { title: '单位', key: 'unit', width: '4%', type: FormTypes.normal },
          // { title: '多属性', key: 'sku', width: '4%', type: FormTypes.normal },
          // { title: '数量', key: 'operNumber', width: '5%', type: FormTypes.inputNumber, statistics: true,
          //   validateRules: [{ required: true, message: '${title}不能为空' }]
          // },
          // { title: '单价', key: 'unitPrice', width: '5%', type: FormTypes.inputNumber},
          // { title: '金额', key: 'allPrice', width: '5%', type: FormTypes.inputNumber, statistics: true },
          // { title: '税率', key: 'taxRate', width: '4%', type: FormTypes.inputNumber,placeholder: '%'},
          // { title: '税额', key: 'taxMoney', width: '5%', type: FormTypes.inputNumber, readonly: true, statistics: true },
          // { title: '价税合计', key: 'taxLastMoney', width: '7%', type: FormTypes.inputNumber, statistics: true },
          // { title: '备注', key: 'remark', width: '6%', type: FormTypes.input }
        ],
      },
      confirmLoading: false,
      validatorRules: {
        saleType:{
          rules: [
            { required: true, message: '请选择销售类型！' },
          ]
        },
        numbers:{
          rules:[
            { required: true, message: '请输入数量，如果为空请填0！' },
            { pattern: /^[0-9]\d*$/, message: '请输入正整数，无请输入0!' }
          ]
        },
        operTime: {
          rules: [{ required: true, message: "请输入订单日期!" }],
        },
        changeAmount:{
          rules: [
            { required: true, message: '请输入金额，如果为空请填0！' },
            { pattern: /^(([0-9][0-9]*)|([0]\.\d{0,4}|[0-9][0-9]*\.\d{0,4}))$/, message: '金额格式不正确!' }
          ]
        }
        // organId:{
        //   rules: [
        //     { required: true, message: '请选择客户!' }
        //   ]
        // }
      },
      url: {
        add: "/depotHead/addDepotHead",
        edit: "/depotHead/addDepotHead",
        detailList: "/depotItem/getDetailList",
        getDepotHeadByNumber: "/warehouseManagement/getDepotHeadByNumber",
      },
    };
  },
  created() {
  },
  methods: {
    handleSizeChange(e) {
      this.$nextTick(() => {
        let num = 0;
        for (let i = 1; i < 12; i++) {
          if (this.form.getFieldValue("size" + i)) {
            num += Number(this.form.getFieldValue("size" + i));
          }
        }
        this.form.setFieldsValue({
          numbers: num,
        });
      });
      this.handleUnitChange() // 销售总价
      var billingRate = this.form.getFieldValue("billingRate")
      this.changeMode(billingRate) // 税额
      this.getFinancialBalance() //应收尾款
      // this.handleOrderChange() // 订单提成
      this.handleCostUnitChange() // 成本金额
      this.handleEmbroideryChange() // 刺绣总价
      this.handleStackedChange() // 叠总价
      this.handleHotPaintingChange() // 烫画总价
      this.handlePrintChange() // 印刷总价
    },
    changeMode (val) {
      this.$nextTick(() => {
       let totalFinancialPrice= this.form.getFieldValue("totalFinancialPrice");
        let financialTaxAmount= 0;

        if(val=='普票2个点'){
            financialTaxAmount=totalFinancialPrice*0.02;
          } else if(val=='普票3个点'){
            financialTaxAmount=totalFinancialPrice*0.03;
          }else if(val=='增票11个点'){
            financialTaxAmount=totalFinancialPrice*0.11;
          }else{
            financialTaxAmount=0;
          }
        this.form.setFieldsValue({financialTaxAmount: financialTaxAmount.toFixed(4)});
      });
    },
    // 计算财务尾款-财务应收尾款
    getFinancialBalance(){
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("totalFinancialPrice")-this.form.getFieldValue("financialDeposit")
        this.form.setFieldsValue({
          financialBalance: num.toFixed(4),
        });
      });
    },

    // 获取订单物流费
    getOrderLogisticsFee(number){
      let param ={
        depotId : number
      }
      getAction(this.url.getDepotHeadByNumber, param).then((res) => {
        console.log(res)
        if (res.code == 1) {
          this.orderLogisticsFee = res.data
        }
      });
    },

    // // 订单提成
    // handleOrderChange() {
    //   this.$nextTick(() => {
    //     let num = 0;
    //     // 订单提成：总价-物流运费-印刷总价-烫画总价-叠总价-刺绣总价—扣款费用-回扣-（总价✖️税点）-成本金额-其他支出费用-其他支出费用2 单位：元
    //     // 订单物流费
    //     // 样品物流费
    //     num =
    //         (
    //         this.form.getFieldValue("totalFinancialPrice")-this.form.getFieldValue("totalPrintingPrice")
    //         -this.form.getFieldValue("hotStampingPaintingTotalPrice")-this.form.getFieldValue("totalStackPrice")
    //         -this.form.getFieldValue("embroideryTotalPrice")-this.form.getFieldValue("financialDeductionsFee")
    //         -this.form.getFieldValue("financialRebate")-this.form.getFieldValue("financialCostAmount")
    //         -this.form.getFieldValue("otherExpensesInFinance")-this.orderLogisticsFee
    //         )*0.5
    //     this.form.setFieldsValue({
    //       orderCommission: num.toFixed(4),
    //     });
    //   });
    // },
    // 销售总价
    handleUnitChange() {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("financialUnitPrice")
        this.form.setFieldsValue({
          totalFinancialPrice: num.toFixed(4),
        });
      });
      var billingRate = this.form.getFieldValue("billingRate")
      this.changeMode(billingRate) // 税额
      this.getFinancialBalance() //应收尾款
      // this.handleOrderChange() // 订单提成
    },
    // 成本金额总价
    handleCostUnitChange() {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("financialCostUnitPrice")
        this.form.setFieldsValue({
          financialCostAmount: num.toFixed(4),
        });
      });
      // this.handleOrderChange();
    },
    // 刺绣总价
    handleEmbroideryChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("rustUnitPrice")
        this.form.setFieldsValue({
          embroideryTotalPrice: num.toFixed(4),
        });
      });
    },
    // 叠总价
    handleStackedChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("stackedUnitPrice")
        this.form.setFieldsValue({
          totalStackPrice: num.toFixed(4),
        });
      });
    },
    // 烫画总价
    handleHotPaintingChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("hotPaintingUnitPrice")
        this.form.setFieldsValue({
          hotStampingPaintingTotalPrice: num.toFixed(4),
        });
      });
    },

// 印刷总价
    handlePrintChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("printingUnitPrice")
        this.form.setFieldsValue({
          totalPrintingPrice: num.toFixed(4),
        });
      });
    },
    //调用完edit()方法之后会自动调用此方法
    editAfter() {
      this.billStatus = "0";
      this.currentSelectDepotId = "";
      this.changeColumnHide();
      console.log("this.action: ", this.action);
      if (this.action === "add") {
        this.addInit(this.prefixNo);
        this.personList.value = "";
        this.fileList = [];
        this.$nextTick(() => {
          handleIntroJs(this.prefixNo, 1);
        });
      } else {
        this.getOrderLogisticsFee(this.model.number)
        this.model.operTime = this.model.operTimeStr;
        if (this.model.accountId == null && this.model.accountIdList) {
          this.model.accountId = 0;
          this.manyAccountBtnStatus = true;
          this.accountIdList = this.model.accountIdList;
          this.accountMoneyList = this.model.accountMoneyList;
        } else {
          this.manyAccountBtnStatus = false;
        }
        this.personList.value = this.model.salesMan;
        this.fileList = this.model.fileName;
        if(this.model.financialTaxAmount==0){
          if(this.model.billingRate=='普票2个点'){
            this.model.financialTaxAmount=this.model.totalFinancialPrice*0.02.toFixed(4);
          } else if(this.model.billingRate=='普票3个点'){
            this.model.financialTaxAmount=this.model.totalFinancialPrice*0.03.toFixed(4);
          }else if(this.model.billingRate=='增票11个点'){
            this.model.financialTaxAmount=this.model.totalFinancialPrice*0.11.toFixed(4);
          }else{
            this.model.financialTaxAmount=0;
          }
        }else{
          this.model.financialTaxAmount=this.model.financialTaxAmount.toFixed(4);
        }
        this.$nextTick(() => {
          this.form.setFieldsValue(
              pick(
                  this.model,
                  "type",
                  "saleType",
                  "subType",
                  "customer",
                  "operTime",
                  "number",
                  "accountId",
                  "salesMan",
                  "color",
                  "size1",
                  "commodityNo",
                  "size2",
                  "size3",
                  "size4",
                  "size5",
                  "size6",
                  "size7",
                  "size8",
                  "size9",
                  "size10",
                  "size11",
                  "numbers",
                  "unit",
                  "orderRemark",
                  "freightPayer",
                  "warehouseRemark",
                  "printingUnitPrice",
                  "totalPrintingPrice",
                  "printer",
                  "printingReconciliationDate",
                  "ironingStaff",
                  "hotPaintingUnitPrice",
                  "hotStampingPaintingTotalPrice",
                  "stackedUnitPrice",
                  "totalStackPrice",
                  "stampingReconciliationDate",
                  "rustUnitPrice",
                  "embroideryTotalPrice",
                  "embroideryReconciliationDate",
                  "embroideryRemark",
                  "financialTaxAmount",
                  "financialUnitPrice",
                  "totalFinancialPrice",
                  "financialDeposit",
                  "financialDepositArrivalDate",
                  "financialDepositToAccountMethod",
                  "financialBalance",
                  "financialStaff",
                  "endStaff",
                  "financialRemarks",
                  "financialDeductionsFee",
                  "financialFinalPaymentDate",
                  "financialFinalPaymentToAccountMethod",
                  "financialRebate",
                  "financialRebateRemarks",
                  "otherExpensesInFinance",
                  "otherExpensesInFinance2",
                  "financialOtherExpenditureRemarks",
                  "doesFinancialAffairsIncludeTax",
                  "financialInvoicePayable",
                  "financialCostUnitPrice",
                  "financialCostAmount",
                  // "orderCommission",
                  "logisticsFreight",
                  "billingRate",
                  "address",
                  "telephone",
                  "contactPerson",
                  "isChangeBag",
                  "invoiceRemark"
              )
          );
        });

        // 加载子表数据
        let params = {
          headerId: this.model.id,
          mpList: getMpListShort(Vue.ls.get("materialPropertyList")), //扩展属性
          linkType: "basic",
        };
        let url = this.readOnly ? this.url.detailList : this.url.detailList;
        this.requestSubTableData(url, params, this.materialTable);
      }
      //复制新增单据-初始化单号和日期
      if (this.action === "copyAdd") {
        this.model.id = "";
        this.model.tenantId = "";
        this.copyAddInit(this.prefixNo);
      }
      this.initSystemConfig();
      this.initCustomer();
      this.initSalesman();
      this.initCommodityList();
      this.initAccount();
      this.initMpList();
    },
    //提交单据时整理成formData
    classifyIntoFormData(allValues) {
      let totalPrice = 0;
      let billMain = Object.assign(this.model, allValues.formValue);
      let detailArr = allValues.tablesValue[0].values;
      billMain.type = "销售订单";
      billMain.subType = "销售订单";
      billMain.defaultNumber = billMain.number;
      for (let item of detailArr) {
        item.depotId = ""; //订单不需要仓库
        totalPrice += item.allPrice - 0;
      }
      billMain.totalPrice = totalPrice;
      if (billMain.accountId === 0) {
        billMain.accountId = "";
      }
      billMain.accountIdList =
          this.accountIdList.length > 0 ? JSON.stringify(this.accountIdList) : "";
      billMain.accountMoneyList =
          this.accountMoneyList.length > 0
              ? JSON.stringify(this.accountMoneyList)
              : "";
      if (this.fileList && this.fileList.length > 0) {
        billMain.fileName = this.fileList;
      } else {
        billMain.fileName = "";
      }
      if (this.model.id) {
        billMain.id = this.model.id;
      }
      billMain.salesMan = this.personList.value;
      billMain.status = this.billStatus;
      return {
        info: JSON.stringify(billMain),
        rows: JSON.stringify(detailArr),
      };
    },
    handleHistoryBillList() {
      let organId = this.form.getFieldValue("organId");
      this.$refs.historyBillListModalForm.show(
          "销售订单",
          "销售订单",
          "客户",
          organId
      );
      this.$refs.historyBillListModalForm.disableSubmit = false;
    },
    handleOkAndCheck() {
      this.form.validateFields((err, values) => {
        if (!err) {
          let param = {
            ...values,
            type:'销售订单',
            subType:'销售订单',
            status:1,
            defaultNumber:this.form.getFieldValue("number"),
            id: this.model.id,
            // commodityNo: this.form.getFieldValue("commodityNo"),
            // commodityName: this.commodityList.options.find(
            //   (item) => item.value == this.form.getFieldValue("commodityNo")
            // ).name,
          };
          // 发起请求
          return this.requestAddOrEdit(param);
        }
      });
    },


    handleOk() {
      this.form.validateFields((err, values) => {
        if (!err) {
          let param = {
            ...values,
            status:4,
            id: this.model.id,
            // commodityNo: this.form.getFieldValue("commodityNo"),
            // commodityName: this.commodityList.options.find(
            //   (item) => item.value == this.form.getFieldValue("commodityNo")
            // ).name,
          };
          // 发起请求
          return this.requestAddOrEdit(param);
        }
      });
    },
    requestAddOrEdit(formData) {
      //接口调用
      let url = this.url.add,
          method = "post";
      if (this.model.id) {
        url = this.url.edit;
        method = "post";
      }
      const that = this;
      this.confirmLoading = true;
      console.log("formData: ", formData);
      // return;
      httpAction(url, formData, method)
          .then((res) => {
            if (res.code === 200) {
              that.$emit("ok");
              that.confirmLoading = false;
              that.close();
            } else {
              that.$message.warning(res.data.message);
              that.confirmLoading = false;
            }
          })
          .finally(() => {});
    },
  },
};
</script>
<style scoped></style>
