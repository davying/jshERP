<template>
  <j-modal
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      v-bind:prefixNo="prefixNo"
      switchFullscreen
      @cancel="handleCancel"
      :id="prefixNo"
      :style="modalStyle"
  >
    <template slot="footer">
      <a-button key="back" @click="handleCancel">关闭</a-button>
      <a-button
          type="primary"
          v-if="showOkFlag"
          :loading="confirmLoading"
          :disabled="handleOkDisabled"
          @click="handleOk"
      >确定</a-button
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
                data-step="1"
                data-title="订单编号"
                data-intro="订单编号必填"
            >
              <a-select
                  showSearch
                  placeholder="请选择订单编号"
                  optionFilterProp="children"
                  v-decorator.trim="[
                      'depotId',validatorRules.depotId
                    ]"
                  @change="getCommodityNo"
              >
                <a-select-option
                    v-for="(site, index) in depotListOptions.options"
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
                label="客户名称"
                data-step="1"
                data-title="客户名称"
                data-intro="选择订单后自动带出客户名称"
            >
              <a-input disabled v-decorator.trim="['customer']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="货品编码"
                data-step="1"
                data-title="货品编码"
                data-intro="货品编码必填"
            >
              <a-input disabled v-decorator.trim="['commodityNo']">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="类型"
                data-step="2"
                data-title="类型"
                data-intro="类型必填"
            >
              <a-select v-decorator="['type',validatorRules.type]"  @blur="handleTypeChange" >
                <a-select-option value="部分发货">部分发货</a-select-option>
                <a-select-option value="全部发货">全部发货</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="颜色"
                data-step="2"
                data-title="颜色"
                data-intro="颜色必填"
            >
              <a-input disabled v-decorator.trim="['color']"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="S"
                data-step="1"
                data-title="S"
                data-intro="S不必填"
            >
              <a-input
                  placeholder="请输入S"
                  :disabled="size1Disabled"
                  v-decorator.trim="['size1', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="M"
                data-step="1"
                data-title="M"
                data-intro="M不必填"
            >
              <a-input
                  placeholder="请输入M"
                  :disabled="size2Disabled"
                  v-decorator.trim="['size2', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="L"
                data-step="1"
                data-title="L"
                data-intro="L不必填"
            >
              <a-input
                  placeholder="请输入L"
                  :disabled="size3Disabled"
                  v-decorator.trim="['size3', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="XL"
                data-step="1"
                data-title="XL"
                data-intro="XL不必填"
            >
              <a-input
                  placeholder="请输入XL"
                  :disabled="size4Disabled"
                  v-decorator.trim="['size4', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="2XL"
                data-step="1"
                data-title="2XL"
                data-intro="2XL不必填"
            >
              <a-input
                  placeholder="请输入2XL"
                  :disabled="size5Disabled"
                  v-decorator.trim="['size5', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="3XL"
                data-step="1"
                data-title="3XL"
                data-intro="3XL不必填"
            >
              <a-input
                  placeholder="请输入3XL"
                  :disabled="size6Disabled"
                  v-decorator.trim="['size6', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="4XL"
                data-step="1"
                data-title="4XL"
                data-intro="4XL不必填"
            >
              <a-input
                  placeholder="请输入4XL"
                  :disabled="size7Disabled"
                  v-decorator.trim="['size7', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="5XL"
                data-step="1"
                data-title="5XL"
                data-intro="5XL不必填"
            >
              <a-input
                  placeholder="请输入5XL"
                  :disabled="size8Disabled"
                  v-decorator.trim="['size8', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="6XL"
                data-step="1"
                data-title="6XL"
                data-intro="6XL不必填"
            >
              <a-input
                  placeholder="请输入6XL"
                  :disabled="size9Disabled"
                  v-decorator.trim="['size9', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="7XL"
                data-step="1"
                data-title="7XL"
                data-intro="7XL不必填"
            >
              <a-input
                  placeholder="请输入7XL"
                  :disabled="size10Disabled"
                  v-decorator.trim="['size10', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="XS"
                data-step="1"
                data-title="XS"
                data-intro="XS不必填"
            >
              <a-input
                  placeholder="请输入XS"
                  :disabled="size11Disabled"
                  v-decorator.trim="['size11', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="数量"
                data-step="1"
                data-title="数量"
                data-intro="数量必填"
            >
              <a-input
                  disabled
                  v-decorator.trim="['numbers']"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="发货日期"
                data-step="1"
                data-title="发货日期"
                data-intro="发货日期不必填"
            >
              <j-date
                  placeholder="请选择发货日期"
                  style="width: 215px"
                  v-decorator.trim="['deliveryDate']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="运费付款方"
                data-step="1"
                data-title="运费付款方"
                data-intro="运费付款方非必填"
            >
              <a-input v-decorator.trim="['freightPayer']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="物流运费"
                data-step="1"
                data-title="物流运费"
                data-intro="物流运费必填"
            >
              <a-input v-decorator.trim="['logisticsFreight']"
              />
            </a-form-item>
          </a-col>
          <a-col v-if="model.id" :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="物流方式"
                data-step="1"
                data-title="物流方式"
                data-intro="物流方式必填"
            >
              <a-textarea v-decorator.trim="['logisticsMode']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="实发数量"
                data-step="1"
                data-title="实发数量"
                data-intro="实发数量必填"
            >
              <a-input v-decorator.trim="['actualQuantity', validatorRules.numbers]"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col v-if="model.id" :md="6" :sm="12">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="发货单号"
                data-step="1"
                data-title="发货单号"
                data-intro="发货单号必填"
            >
              <a-textarea v-decorator.trim="['shippingOrderNumber']"
              />
            </a-form-item>
          </a-col>
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="备注"
                data-step="1"
                data-title="备注"
                data-intro="备注非必填"
            >
              <a-textarea v-decorator.trim="['remark']" onmouseover="this.title=this.value"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="24" :sm="24">
            <div class="logistics-detail-card">
              <div class="logistics-detail-title">
                <span>物流明细</span>
                <a-button
                    size="small"
                    type="primary"
                    icon="plus"
                    :disabled="logisticsDetails.length >= logisticsMaxDetailCount"
                    @click="addLogisticsDetail"
                >新增明细</a-button>
              </div>
              <table class="logistics-detail-table">
                <thead>
                <tr>
                  <th>物流方式</th>
                  <th>发货日期</th>
                  <th>物流单号</th>
                  <th>发货数量</th>
                  <th>运费</th>
                  <th>操作</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="(detail, index) in logisticsDetails" :key="detail.uid">
                  <td>
                    <a-select
                        v-model="detail.logisticsMode"
                        placeholder="请选择物流方式"
                        @change="handleLogisticsDetailChange"
                    >
                      <a-select-option
                          v-for="mode in logisticsDetailModes"
                          :key="mode"
                          :value="mode"
                      >{{ mode }}</a-select-option>
                    </a-select>
                  </td>
                  <td>
                    <j-date
                        v-model="detail.deliveryDate"
                        placeholder="请选择日期"
                        :dateFormat="'YYYY-MM-DD'"
                        style="width: 100%"
                    />
                  </td>
                  <td>
                    <a-input
                        v-model="detail.shippingOrderNumber"
                        placeholder="请输入物流单号"
                        @change="handleLogisticsDetailChange"
                    />
                  </td>
                  <td>
                    <a-input
                        v-model="detail.deliveryQuantity"
                        placeholder="请输入发货数量"
                        @change="handleLogisticsDetailChange"
                    />
                  </td>
                  <td>
                    <a-input
                        v-model="detail.freight"
                        placeholder="请输入运费"
                        @change="handleLogisticsDetailChange"
                    />
                  </td>
                  <td class="logistics-detail-action">
                    <a-button
                        size="small"
                        type="link"
                        :disabled="logisticsDetails.length <= 1"
                        @click="removeLogisticsDetail(index)"
                    >删除</a-button>
                  </td>
                </tr>
                </tbody>
              </table>
              <div class="logistics-detail-summary">
                最多 8 条物流明细；汇总后自动回显到“物流运费”和“实发数量”。
              </div>
            </div>
          </a-col>
        </a-row>
      </a-form>
    </a-spin>
    <!--    <unit-modal ref="unitModalForm" @ok="unitModalFormOk"></unit-modal>-->
  </j-modal>
</template>
<script>
import pick from "lodash.pick";
import JSelectMultiple from "@/components/jeecg/JSelectMultiple";
import JEditableTable from "@/components/jeecg/JEditableTable";
import { getRefPromise } from "@/utils/JEditableTableUtil";
import { autoJumpNextInput } from "@/utils/util";
import { getAction, httpAction,postAction } from "@/api/manage";
import JDate from "@/components/jeecg/JDate";
import moment from "moment";

export default {
  name: "LogisticsOutModal",
  components: {
    JDate,
    JSelectMultiple,
    JEditableTable,
    VNodes: {
      functional: true,
      render: (h, ctx) => ctx.props.vnodes,
    },
  },
  data() {
    return {
      commodityNo:'',
      logisticsDetailModes: ["三轮车", "壹米滴答", "百世快运", "报销", "其他"],
      logisticsMaxDetailCount: 8,
      logisticsDetailUid: 0,
      logisticsDetails: [],
      modalStyle: {},
      prefixNo: "YPGL",
      searchOptions: {
        value: "",
        options: [],
      },
      size1Disabled:false,
      size2Disabled:false,
      size3Disabled:false,
      size4Disabled:false,
      size5Disabled:false,
      size6Disabled:false,
      size7Disabled:false,
      size8Disabled:false,
      size9Disabled:false,
      size10Disabled:false,
      size11Disabled:false,
      handleOkDisabled:false,
      canSaleOutObj:{},
      depotListOptions: {
        value: "",
        options: [],
      },
      title: "操作",
      width: "1300px",
      visible: false,
      model: {},
      columns: {},
      showOkFlag: true,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      depotTable: {
        loading: false,
        dataSource: [],
        columns: [],
      },
      confirmLoading: false,
      form: this.$form.createForm(this),
      validatorRules: {
        depotId: {
          rules: [{ required: true, message: "请选择销售订单!" }],
        },
        type: {
          rules: [{ required: true, message: "请选择类型!" }],
        },
        numbers:{
          rules:[
            { pattern: /^[0-9]\d*$/, message: '请输入正整数，无请输入0!' }
          ]
        },
        changeAmount:{
          rules: [
            { required: true, message: '请输入金额，如果为空请填0！' },
            { pattern: /^(([0-9][0-9]*)|([0]\.\d{0,4}|[0-9][0-9]*\.\d{0,4}))$/, message: '金额格式不正确!' }
          ]
        }
      },
      url: {
        add: "/warehouseManagement/add",
        edit: "/warehouseManagement/update",
        searchLinkNumber: "/depotHead/list",
        getDepotHeadIds: "/depotHead/getDepotHeadIds",
        checkNumberOutInfo:"/depotHead/checkNumberOutInfo",
        // materialsExtendList: '/materialsExtend/getDetailList',
        // depotWithStock: '/depot/getAllListWithStock'
      },
    };
  },
  created() {
    let realScreenWidth = window.screen.width;
    this.width = realScreenWidth < 1500 ? "1200px" : "1400px";
    this.getDepotHeadIds();
    // this.searchLinkNumber();
  },
  computed: {},
  methods: {
    nextLogisticsDetailUid() {
      this.logisticsDetailUid += 1;
      return `logistics_detail_${this.logisticsDetailUid}`;
    },
    createEmptyLogisticsDetail(detail) {
      const source = detail || {};
      return {
        uid: source.uid || this.nextLogisticsDetailUid(),
        logisticsMode: source.logisticsMode || "",
        deliveryDate: source.deliveryDate || "",
        shippingOrderNumber: source.shippingOrderNumber || source.trackingNo || "",
        deliveryQuantity: this.normalizeLogisticsInput(source.deliveryQuantity || source.quantity),
        freight: this.normalizeLogisticsInput(source.freight),
      };
    },
    createEmptyLogisticsDetails() {
      return [this.createEmptyLogisticsDetail()];
    },
    normalizeLogisticsDetails(value) {
      let source = [];
      if (Array.isArray(value)) {
        source = value;
      } else if (value) {
        try {
          source = JSON.parse(value);
        } catch (e) {
          source = [];
        }
      }
      const details = source
          .filter((detail) => detail)
          .slice(0, this.logisticsMaxDetailCount)
          .map((detail) => this.createEmptyLogisticsDetail(detail));
      return details.length > 0 ? details : this.createEmptyLogisticsDetails();
    },
    normalizeLogisticsInput(value) {
      if (value === null || value === undefined || value === "") {
        return "";
      }
      return value;
    },
    toNumber(value) {
      const number = Number(value);
      return Number.isFinite(number) ? number : 0;
    },
    toQuantity(value) {
      return Math.trunc(this.toNumber(value));
    },
    formatAmount(value) {
      const amount = this.toNumber(value);
      return Number.isInteger(amount) ? String(amount) : String(Number(amount.toFixed(4)));
    },
    buildLogisticsDetailsJson() {
      const details = this.logisticsDetails
          .map((detail) => ({
            logisticsMode: detail.logisticsMode,
            deliveryDate: detail.deliveryDate || "",
            shippingOrderNumber: detail.shippingOrderNumber || "",
            deliveryQuantity: this.toQuantity(detail.deliveryQuantity),
            freight: this.toNumber(detail.freight),
          }))
          .filter((detail) => detail.shippingOrderNumber || detail.deliveryQuantity || detail.freight);
      return JSON.stringify(details);
    },
    handleLogisticsDetailChange() {
      const totalFreight = this.logisticsDetails.reduce((total, detail) => total + this.toNumber(detail.freight), 0);
      const totalQuantity = this.logisticsDetails.reduce((total, detail) => total + this.toQuantity(detail.deliveryQuantity), 0);
      this.$nextTick(() => {
        this.form.setFieldsValue({
          logisticsFreight: this.formatAmount(totalFreight),
          actualQuantity: totalQuantity,
        });
      });
    },
    addLogisticsDetail() {
      if (this.logisticsDetails.length >= this.logisticsMaxDetailCount) {
        this.$message.warning(`最多只能添加${this.logisticsMaxDetailCount}条物流明细`);
        return;
      }
      this.logisticsDetails.push(this.createEmptyLogisticsDetail());
    },
    removeLogisticsDetail(index) {
      if (this.logisticsDetails.length <= 1) {
        return;
      }
      this.logisticsDetails.splice(index, 1);
      this.handleLogisticsDetailChange();
    },
    getSelectedDepot(depotId) {
      return this.depotListOptions.options.find((item) => item.value == depotId) || {};
    },
    isSuccessResult(res) {
      return res && (res.code === 200 || res.code == 1);
    },
    getSizeFieldKeys() {
      return ["size1", "size2", "size3", "size4", "size5", "size6", "size7", "size8", "size9", "size10", "size11"];
    },
    buildSizeFieldValues(source) {
      return this.getSizeFieldKeys().reduce((values, key) => {
        values[key] = source && source[key] ? source[key] : 0;
        return values;
      }, {});
    },
    calculateSizeTotal(sizeValues) {
      return this.getSizeFieldKeys().reduce((total, key) => total + Number(sizeValues[key] || 0), 0);
    },
    buildCommodityLabel(source, selectedDepot) {
      const commodityNo = source.commodityNo || selectedDepot.commodityNo || "";
      const commodityName = source.commodityName || selectedDepot.commodityName || "";
      if (!commodityNo) {
        return selectedDepot.name || "";
      }
      return commodityName ? `${commodityNo}（${commodityName}）` : commodityNo;
    },
    applyDepotOutInfo(source, selectedDepot) {
      const orderInfo = source || {};
      const depotInfo = selectedDepot || {};
      const sizeValues = this.buildSizeFieldValues(orderInfo);
      const numbers = orderInfo.numbers === undefined || orderInfo.numbers === null
          ? this.calculateSizeTotal(sizeValues)
          : orderInfo.numbers;
      this.canSaleOutObj = orderInfo;
      this.form.setFieldsValue({
        commodityNo: this.buildCommodityLabel(orderInfo, depotInfo),
        customer: orderInfo.customer || depotInfo.customer || "",
        color: orderInfo.color || depotInfo.color || "",
        freightPayer: orderInfo.freightPayer || depotInfo.freightPayer || "",
        ...sizeValues,
        numbers,
      });
      this.handleOkDisabled = false;
    },
    loadDepotOutInfo(depotId, selectedDepot) {
      if (!depotId) {
        return Promise.resolve();
      }
      return getAction(this.url.checkNumberOutInfo, {type:"发货",depotId}).then((res) => {
        if (this.isSuccessResult(res) && res.data) {
          this.applyDepotOutInfo(res.data, selectedDepot || this.getSelectedDepot(depotId));
          return;
        }
        if (res && res.code == 0) {
          this.handleOkDisabled = true;
          this.$message.error(res.msg);
        }
      });
    },
    handleTypeChange(){
      let typeStr = this.form.getFieldValue("type");
      let depotIdStr = this.form.getFieldValue("depotId");
      if(typeStr=='全部发货'){
        this.size1Disabled=true;
        this.size2Disabled=true;
        this.size3Disabled=true;
        this.size4Disabled=true;
        this.size5Disabled=true;
        this.size6Disabled=true;
        this.size7Disabled=true;
        this.size8Disabled=true;
        this.size9Disabled=true;
        this.size10Disabled=true;
        this.size11Disabled=true;
      }else{
        this.form.setFieldsValue({"size1": 0});
        this.form.setFieldsValue({"size2": 0});
        this.form.setFieldsValue({"size3": 0});
        this.form.setFieldsValue({"size4": 0});
        this.form.setFieldsValue({"size5": 0});
        this.form.setFieldsValue({"size6": 0});
        this.form.setFieldsValue({"size7": 0});
        this.form.setFieldsValue({"size8": 0});
        this.form.setFieldsValue({"size9": 0});
        this.form.setFieldsValue({"size10": 0});
        this.form.setFieldsValue({"size11": 0});
        this.size1Disabled=false;
        this.size2Disabled=false;
        this.size3Disabled=false;
        this.size4Disabled=false;
        this.size5Disabled=false;
        this.size6Disabled=false;
        this.size7Disabled=false;
        this.size8Disabled=false;
        this.size9Disabled=false;
        this.size10Disabled=false;
        this.size11Disabled=false;
      }
      this.loadDepotOutInfo(depotIdStr, this.getSelectedDepot(depotIdStr));
    },
    getCommodityNo(value){
      const depotId = value || this.form.getFieldValue("depotId");
      const selectedDepot = this.getSelectedDepot(depotId);
      if (selectedDepot.value) {
        this.form.setFieldsValue({
          commodityNo: selectedDepot.name,
          customer: selectedDepot.customer || "",
          color: selectedDepot.color,
          freightPayer: selectedDepot.freightPayer || ""
        });
      }
      this.loadDepotOutInfo(depotId, selectedDepot);
    },
    getDepotHeadIds() {
      return postAction(this.url.getDepotHeadIds, {status:'4,9', type:'发货'}).then((res) => {
        if (this.isSuccessResult(res) && Array.isArray(res.data)) {
          this.depotListOptions.options = res.data.map((item) => {
            return {
              value: item.number + '',
              label: item.number + "（" + item.salesMan + "）",
              name:item.commodityNo + "（" + item.commodityName + "）",
              commodityNo: item.commodityNo,
              commodityName: item.commodityName,
              customer: item.customer,
              freightPayer: item.freightPayer,
              color:item.color,
            };
          });
          return;
        }
        this.depotListOptions.options = [];
        if (res && res.msg) {
          this.$message.warning(res.msg);
        }
      });
    },
    searchLinkNumber() {
      let lists = [];
      getAction(this.url.commodityList, null).then((res) => {
        if (res.code == 1) {
          for (let i of res.data) {
            lists.push(i);
          }
          this.searchOptions.options = res.data.map((item) => {
            return {
              value: item.commodityNo,
              label: item.name + "（" + item.commodityNo + "）",
              name:item.name
            };
          });
        }

        if (res.code == 0) {
          this.$message.warning(res.msg);
        }
      });
    },
    // 获取所有的editableTable实例
    // 获取所有的editableTable实例
    getAllTable() {
      return Promise.all([
        getRefPromise(this, "editableMeTable"),
        getRefPromise(this, "editableDepotTable"),
      ]);
    },
    add() {
      this.getDepotHeadIds();
      //隐藏多属性
      // 默认新增一条数据
      this.getAllTable().then((editableTables) => {
        editableTables[0].add();
      });
      this.edit({});
    },
    edit(record) {
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.logisticsDetails = this.normalizeLogisticsDetails(this.model.logisticsDetailsJson);
      this.activeKey = "1";
      this.visible = true;
      this.modalStyle = "top:20px;height: 95%;";
      if (JSON.stringify(record) === "{}") {
        this.fileList = [];
      } else {
        setTimeout(() => {
          this.fileList = record.imgName;
        }, 5);
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(
            pick(
                this.model,
                "color",
                "size1",
                "type",
                "depotId",
                "commodityNo",
                "commodityName",
                "customer",
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
                "inTime",
                "price",
                "deliveryDate",
                "freightPayer",
                "logisticsFreight",
                "logisticsMode",
                "actualQuantity",
                "shippingOrderNumber",
                "remark"
            )
        );
        autoJumpNextInput("sampleHeadModal");
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
      this.modalStyle = "";
      this.getAllTable().then((editableTables) => {
        editableTables[0].initialize();
        editableTables[1].initialize();
      });
    },

    handleOk() {
      this.validateFields();
    },
    handleCancel() {
      this.close();
    },
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
    },
    /** 触发表单验证 */
    validateFields() {
      this.form.validateFields((err, values) => {
        if (!err) {
          if(this.form.getFieldValue("size1")>this.canSaleOutObj.size1){
            this.$message.error("S出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size2")>this.canSaleOutObj.size2){
            this.$message.error("M出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size3")>this.canSaleOutObj.size3){
            this.$message.error("L出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size4")>this.canSaleOutObj.size4){
            this.$message.error("XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size5")>this.canSaleOutObj.size5){
            this.$message.error("2XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size6")>this.canSaleOutObj.size6){
            this.$message.error("3XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size7")>this.canSaleOutObj.size7){
            this.$message.error("4XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size8")>this.canSaleOutObj.size8){
            this.$message.error("5XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size9")>this.canSaleOutObj.size9){
            this.$message.error("6XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size10")>this.canSaleOutObj.size10){
            this.$message.error("7XL出库数量大于订单剩余数量，请重新输入!");
            return
          }
          if(this.form.getFieldValue("size11")>this.canSaleOutObj.size11){
            this.$message.error("XS出库数量大于订单剩余数量，请重新输入!");
            return
          }
          let param={}
          const logisticsDetailsJson = this.buildLogisticsDetailsJson();
          if(this.model.id){
            param = {
              ...values,
              id: this.model.id,
              logisticsDetailsJson,
            };
          }else{
            param = {
              ...values,
              id: this.model.id,
              logisticsDetailsJson,
              commodityNo: this.form.getFieldValue("commodityNo").split("（")[0],
              commodityName: this.form.getFieldValue("commodityNo").split("（")[1].replace("）",""),
            };
          }

          // 发起请求
          return this.requestAddOrEdit(param);
        }
      });
    },
    /** 发起新增或修改的请求 */
    requestAddOrEdit(formData) {
      //接口调用
      let url = this.url.add,method = "post";
      if (this.model.id) {
        url = this.url.add;
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
    parseParam(param) {
      return param ? param : "";
    },
  },
};
</script>
<style scoped>
.input-table {
  max-width: 100%;
  min-width: 1200px;
}
.tag-info {
  font-size: 14px;
  height: 32px;
  line-height: 32px;
  width: 100%;
  padding: 0px 11px;
  color: #bbb;
  background-color: #ffffff;
}
.logistics-detail-card {
  margin: 0 20px 18px 20px;
  padding: 12px 16px;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  background: #fafafa;
}
.logistics-detail-title {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-weight: 600;
  color: #333;
}
.logistics-detail-title span {
  font-size: 16px;
  line-height: 24px;
}
.logistics-detail-title .ant-btn {
  height: 28px;
  margin-left: 16px;
  padding: 0 12px;
  font-size: 13px;
  line-height: 26px;
}
.logistics-detail-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
}
.logistics-detail-table th,
.logistics-detail-table td {
  padding: 8px;
  border: 1px solid #e8e8e8;
}
.logistics-detail-table th {
  background: #f5f5f5;
  color: #333;
}
.logistics-detail-table td:first-child {
  width: 130px;
  text-align: center;
  color: #333;
}
.logistics-detail-table td:nth-child(2) {
  width: 150px;
}
.logistics-detail-table td:last-child {
  width: 70px;
  text-align: center;
}
.logistics-detail-table .ant-select,
.logistics-detail-table .ant-input,
.logistics-detail-table .ant-calendar-picker {
  width: 100%;
}
.logistics-detail-summary {
  margin-top: 8px;
  color: #888;
}
</style>
