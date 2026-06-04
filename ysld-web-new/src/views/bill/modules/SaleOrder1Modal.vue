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
      <!-- <div v-if="loginUser.loginName!='yangxiaolu' || loginUser.loginName!='yangshuai'"> -->
        <a-button
          :loading="confirmLoading"
          @click="handleOkAndCheck"
          :disabled="loginUser.loginName!='yangxiaolu' || loginUser.loginName!='yangshuai'"
        >保存并审核</a-button>
<!--      </div>-->
<!--      <div v-else>-->
<!--        <a-button-->
<!--          :loading="confirmLoading"-->
<!--          @click="handleOkAndCheck"-->
<!--          disabled="isCanCheck"-->
<!--        >保存并审核</a-button>-->

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
              <a-input disabled v-decorator.trim="['customer']" />
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
              <a-input disabled v-decorator.trim="['color']" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="S"
                data-step="2"
                data-title="S"
            >
              <a-input disabled v-decorator.trim="['size1']" />
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
              <a-input disabled v-decorator.trim="['size2']" />
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
              <a-input disabled v-decorator.trim="['size3']" />
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
              <a-input disabled v-decorator.trim="['size4']" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="2XL"
                data-step="2"
                data-title="2XL"
            >
              <a-input disabled v-decorator.trim="['size5']" />
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
              <a-input disabled v-decorator.trim="['size6']" />
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
              <a-input disabled v-decorator.trim="['size7']" />
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
              <a-input disabled v-decorator.trim="['size8']" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
        <a-col :md="6" :sm="6">
          <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="6XL"
              data-step="2"
              data-title="6XL"
          >
            <a-input disabled v-decorator.trim="['size9']" />
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
              <a-input disabled v-decorator.trim="['size10']" />
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
              <a-input disabled v-decorator.trim="['size11']" />
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
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="单位"
                data-step="2"
                data-title="单位"
            >
              <a-input disabled v-decorator.trim="['unit']" />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6" >
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

          <div v-if="loginUser.loginName!='yangxiaolu'">
            <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="运费付款方"
                data-step="2"
                data-title="运费付款方"
            >
              <a-input disabled v-decorator.trim="['freightPayer']" />
            </a-form-item>
           </a-col>
           <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="LOGO制作"
            >
              <a-input
                  placeholder="LOGO制作"
                  v-decorator.trim="['logoMark']"
              />
            </a-form-item>
           </a-col>
          </div>

          <div v-if="loginUser.loginName=='yangxiaolu'">
            <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="付款日款"
                data-step="2"
                data-title="付款日款"
            >
            <j-date
                      v-decorator.trim="['xlPayDate']"
                      :dateFormat="'YYYY-MM-DD HH:mm:ss'"
                  />
            </a-form-item>
           </a-col>
           <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="收款单位"
            >
              <a-input
                  placeholder="收款单位"
                  v-decorator.trim="['xlReceiveUnit']"
              />
            </a-form-item>
           </a-col>
           <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="小路备注"
            >
              <a-textarea
                  rows="4"
                  placeholder="小路备注"
                  v-decorator.trim="['xlRemark']"
              />
            </a-form-item>
           </a-col>
          </div>

        </a-row>
        <div v-if="loginUser.loginName!='yangxiaolu'">
          <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="是否换拉链袋"
            >
              <a-input disabled="true"
                  v-decorator.trim="['isChangeBag']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="仓库备注"
            >
              <a-input
                  placeholder="仓库备注"
                  v-decorator.trim="['warehouseRemark']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷单价"
            >
              <a-input
                  placeholder="印刷单价"
                  v-decorator.trim="['printingUnitPrice']"
                  @change="handlePrintChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷总价"
            >
              <a-input disabled
                  placeholder="印刷总价"
                  v-decorator.trim="['totalPrintingPrice']"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷人员"
            >
              <a-input
                  placeholder="印刷人员"
                  v-decorator.trim="['printer']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="印刷对帐日期"
            >
              <j-date
                  style="width:240px"
                  v-decorator="['printingReconciliationDate']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫画人员"
            >
              <a-input
                  placeholder="烫画人员"
                  v-decorator.trim="['ironingStaff']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫画单价"
            >
              <a-input
                  placeholder="烫画单价"
                  v-decorator.trim="['hotPaintingUnitPrice']"
                  @change="handleHotPaintingChange"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫画总价"
            >
              <a-input disabled
                  v-decorator.trim="['hotStampingPaintingTotalPrice']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="叠人员"
            >
              <a-input
                  placeholder="叠人员"
                  v-decorator.trim="['stackStaff']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="叠单价"
            >
              <a-input
                  placeholder="叠单价"
                  v-decorator.trim="['stackedUnitPrice']"
                  @change="handleStackedChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="叠总价"
            >
              <a-input disabled
                  v-decorator.trim="['totalStackPrice']"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="烫叠对帐日期"
            >
              <j-date placeholder="烫叠对帐日期"
                      style="width:240px"
                  v-decorator.trim="['stampingReconciliationDate']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺绣人员"
            >
              <a-input
                  placeholder="刺绣人员"
                  v-decorator.trim="['rustStaff']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺锈单价"
            >
              <a-input
                  placeholder="刺锈单价"
                  v-decorator.trim="['rustUnitPrice']"
                  @change="handleEmbroideryChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺绣总价"
            >
              <a-input disabled
                  v-decorator.trim="['embroideryTotalPrice']"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </div>
        <a-row class="form-row" :gutter="24">
          <div v-if="loginUser.loginName!='yangxiaolu'">

            <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺绣对账日期"
            >
              <j-date
                  placeholder="刺绣对账日期"
                  style="width:240px"
                  v-decorator.trim="['embroideryReconciliationDate']"
              />
            </a-form-item>
           </a-col>
           <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="刺绣备注"
            >
              <a-input
                  placeholder="刺绣备注"
                  v-decorator.trim="['embroideryRemark']"
              />
            </a-form-item>
           </a-col>
           <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="销售类型"
             >
              <a-select v-decorator="['saleType', validatorRules.saleType]" disabled>
                <a-select-option value="自有仓库">自有仓库</a-select-option>
                <a-select-option value="转销单">转销单</a-select-option>
                <a-select-option value="定制款">定制款</a-select-option>
              </a-select>
            </a-form-item>
           </a-col>
          </div>


          <div v-if="loginUser.loginName=='wangxianglian'">
            <a-col :md="6" :sm="6">
              <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="开票抬头"
              >
                <a-input disabled v-decorator.trim="['financialInvoicePayable']"
                />
              </a-form-item>
            </a-col>
            <a-col :md="6" :sm="6">
              <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="开票税种"
              >
                <a-select v-decorator.trim="['billingRate']" placeholder="请选择开票种类" :disabled="true">
                  <a-select-option value="不开票">不开票</a-select-option>
                  <a-select-option value="增票11个点">增票11个点</a-select-option>
                  <a-select-option value="普票2个点">普票2个点</a-select-option>
                  <a-select-option value="普票3个点">普票3个点</a-select-option>
                </a-select>
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
                  label="开票备注"
              >
                <a-input v-decorator.trim="['invoiceRemark']" />
              </a-form-item>
            </a-col>

          </div>


          <div v-if="loginUser.loginName=='yangshuai'">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="成本单价"
            >
              <a-input disabled
                       v-decorator.trim="['financialCostUnitPrice',validatorRules.changeAmount]"
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
          </div>

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
import { DatePicker } from "ant-design-vue";
export default {
  name: "SaleOrder1Modal",
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
          { title: "叠单价", dataIndex: "stackedUnitPrice", width: 50 },
          { title: "叠人员", dataIndex: "stackStaff", width: 50 },
          { title: "烫叠对帐日期", dataIndex: "stampingReconciliationDate", width: 50 },
          { title: "刺锈单价", dataIndex: "rustUnitPrice", width: 50 },
          { title: "刺绣人员", dataIndex: "rustStaff", width: 50 },
          { title: "刺绣总价", dataIndex: "embroideryTotalPrice", width: 50 },
          { title: "烫叠对帐日期", dataIndex: "embroideryReconciliationDate", width: 50 },
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
        operTime: {
          rules: [{ required: true, message: "请输入单据日期!" }],
        },
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
      },
    };
  },
  created() {},
  methods: {
    // 刺绣总价
    handleEmbroideryChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("rustUnitPrice")
        this.form.setFieldsValue({
          embroideryTotalPrice: num,
        });
      });
    },
    // 叠总价
    handleStackedChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("stackedUnitPrice")
        this.form.setFieldsValue({
          totalStackPrice: num,
        });
      });
    },
    // 烫画总价
    handleHotPaintingChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("hotPaintingUnitPrice")
        this.form.setFieldsValue({
          hotStampingPaintingTotalPrice: num,
        });
      });
    },

// 印刷总价
    handlePrintChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("printingUnitPrice")
        this.form.setFieldsValue({
          totalPrintingPrice: num,
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
              "stackStaff",
              "rustStaff",
              "logoMark",
              "isChangeBag",
              "financialCostUnitPrice",
              "financialCostAmount",
              "totalFinancialPrice",
              "billingRate",
              "invoiceRemark",
              "financialInvoicePayable",
              "xlPayDate",
              "xlReceiveUnit",
              "xlRemark",
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
            status:4,
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
            status:9,
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
