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
      <a-button
        v-if="checkFlag && isCanCheck"
        :loading="confirmLoading"
        @click="handleOkAndCheck"
        >保存并审核</a-button
      >
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
              label="销售类型"
            >
              <a-select v-decorator="['saleType', validatorRules.saleType]"
                        @change="saleTypeChange"
              >
                <a-select-option value="自有仓库">自有仓库</a-select-option>
                <a-select-option value="转销单">转销单</a-select-option>
                <a-select-option value="定制款">定制款</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="单据日期"
            >
              <j-date
                  format="YYYY-MM-DD HH:mm:ss"
                  style="width: 240px"
                  v-decorator="['operTime', validatorRules.operTime]"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
<!--          <a-col :md="12" :sm="24">-->
<!--              <a-form-item-->
<!--                  :labelCol="labelCol"-->
<!--                  :wrapperCol="wrapperCol"-->
<!--                  label="单据编号"-->
<!--                  data-step="2"-->
<!--                  data-title="单据编号"-->
<!--                  data-intro="单据编号自动生成、自动累加、开头是单据类型的首字母缩写，累加的规则是每次打开页面会自动占用一个新的编号"-->
<!--              >-->
<!--                <a-input-->
<!--                    placeholder="请输入单据编号"-->
<!--                    v-decorator.trim="['number']"-->
<!--                    :readOnly="true"-->
<!--                />-->
<!--              </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->

<!--        <a-row v-if="this.roleName!='业务员'" class="form-row" :gutter="24">-->
<!--          <a-col :md="12" :sm="24">-->
<!--            <a-form-item-->
<!--              :labelCol="labelCol"-->
<!--              :wrapperCol="wrapperCol"-->
<!--              label="业务员"-->
<!--              data-step="3"-->
<!--              data-title="业务员"-->
<!--              data-intro="业务员的数据来自【经手人管理】菜单中的业务员"-->
<!--            >-->
<!--                <j-select-multiple-->
<!--                    placeholder="请选择业务员"-->
<!--                    v-model="personList.value"-->
<!--                    :options="personList.options"-->
<!--                />-->
<!--            </a-form-item>-->
<!--          </a-col>-->
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="货品编号"
                data-step="3"
                data-title="货品编号"
                data-intro="货品编号"
            >
              <!-- <j-select-multiple placeholder="请选择货品编号" v-model="commodityList.value" :options="commodityList.options"/> -->
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
                label="客户"
                data-step="2"
                data-title="客户"
            >
              <a-input placeholder="客户" v-decorator.trim="['customer', validatorRules.customer]" />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
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
                  @focus="commodityChange"
                  v-decorator.trim="['color', validatorRules.color]"
              >
                <a-select-option
                    v-for="(site, index) in colorList.options"
                    :key="nativeName"
                    :value="site.nativeName"
                >{{ site.nativeName }}</a-select-option>
              </a-select>
<!--              <a-input placeholder="颜色" v-decorator.trim="['color']" />-->
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
              <a-input placeholder="S" v-decorator.trim="['size1', validatorRules.numbers]" @change="handleSizeChange"/>
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
              <a-input placeholder="M" v-decorator.trim="['size2', validatorRules.numbers]" @change="handleSizeChange"/>
            </a-form-item>

          </a-col>
          <!--        </a-row>-->
          <!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="L"
                data-step="2"
                data-title="L"
            >
              <a-input placeholder="L" v-decorator.trim="['size3', validatorRules.numbers]" @change="handleSizeChange" />
            </a-form-item>
          </a-col>

        </a-row>
        <a-row class="form-row" :gutter="24">

<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="XL"
                data-step="2"
                data-title="XL"
            >
              <a-input placeholder="XL" v-decorator.trim="['size4', validatorRules.numbers]" @change="handleSizeChange" />
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
              <a-input placeholder="2XL" v-decorator.trim="['size5', validatorRules.numbers]" @change="handleSizeChange" />
            </a-form-item>

          </a-col>
<!--        </a-row>-->


<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="3XL"
              data-step="2"
              data-title="3XL"
            >
              <a-input placeholder="3XL" v-decorator.trim="['size6', validatorRules.numbers]" @change="handleSizeChange" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->

<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="4XL"
                data-step="2"
                data-title="4XL"
            >
              <a-input placeholder="4XL" v-decorator.trim="['size7', validatorRules.numbers]" @change="handleSizeChange" />
            </a-form-item>

          </a-col>
        </a-row>

        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="5XL"
                data-step="2"
                data-title="5XL"
            >
              <a-input placeholder="5XL" v-decorator.trim="['size8', validatorRules.numbers]" @change="handleSizeChange" />
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
              <a-input placeholder="6XL" v-decorator.trim="['size9', validatorRules.numbers]" @change="handleSizeChange" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="7XL"
                data-step="2"
                data-title="7XL"
            >
              <a-input
                  placeholder="7XL"
                  v-decorator.trim="['size10', validatorRules.numbers]"
                  @change="handleSizeChange"
              />
            </a-form-item>

          </a-col>
<!--        </a-row>-->

<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="XS"
                data-step="2"
                data-title="XS"
            >
              <a-input placeholder="XS" v-decorator.trim="['size11', validatorRules.numbers]" @change="handleSizeChange" />
            </a-form-item>

          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
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
              <a-input placeholder="单位" v-decorator.trim="['unit',validatorRules.unit]" />
            </a-form-item>

          </a-col>
<!--        </a-row>-->

<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="下单备注"
                data-step="2"
                data-title="下单备注"
            >
              <a-input
                  placeholder="下单备注"
                  v-decorator.trim="['orderRemark',validatorRules.orderRemark]"
              />
            </a-form-item>

          </a-col>
<!--        </a-row>-->

<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
              :labelCol="labelCol"
              :wrapperCol="wrapperCol"
              label="运费付款方"
              data-step="2"
              data-title="运费付款方"
            >
              <a-input
                placeholder="运费付款方"
                v-decorator.trim="['freightPayer',validatorRules.freightPayer]"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
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
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="销售总价"
            >
              <a-input disabled v-decorator.trim="['totalFinancialPrice']" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->

<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="LOGO制作"
            >
              <a-input v-decorator.trim="['logoMark',validatorRules.logoMark]" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->

<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="开票种类"
            >
              <a-select v-decorator.trim="['billingRate',validatorRules.billingRate]" placeholder="请选择开票种类">
                <a-select-option value="不开票">不开票</a-select-option>
                <a-select-option value="增票11个点">增票11个点</a-select-option>
                <a-select-option value="普票2个点">普票2个点</a-select-option>
                <a-select-option value="普票3个点">普票3个点</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
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
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="成本总价"
            >
              <a-input disabled
                       v-decorator.trim="['financialCostAmount',validatorRules.changeAmount]"
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
                       v-decorator.trim="['financialInvoicePayable',validatorRules.financialInvoicePayable]"
              />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="地址"
                data-step="2"
                data-title="地址"
            >
              <a-input placeholder="地址" v-decorator.trim="['address',validatorRules.address]" />
            </a-form-item>

          </a-col>
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="电话"
                data-step="2"
                data-title="电话"
            >
              <a-input placeholder="电话" v-decorator.trim="['telephone',validatorRules.telephone]" />
            </a-form-item>
          </a-col>
<!--        </a-row>-->
<!--        <a-row class="form-row" :gutter="24">-->
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="联系人" data-step="2" data-title="联系人">
              <a-input
                placeholder="联系人"
                v-decorator.trim="['contactPerson',validatorRules.contactPerson]"
              />
            </a-form-item>
          </a-col>
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
        </a-row>
        <a-row class="form-row" :gutter="24">
          <a-col :md="6" :sm="6">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="印刷图片信息" data-step="2" data-title="印刷图片信息">
              <a-upload
                  :action="uploadAction"
                  list-type="picture-card"
                  :file-list="fileList"
                  :headers="headers"
                  @preview="handlePreview"
                  @change="handleChange"
                  :beforeUpload="beforeUpload"
              >
                <div v-if="fileList.length < 4">
                  <a-icon type="plus" />
                  <div class="ant-upload-text">
                    Upload
                  </div>
                </div>
              </a-upload>
              <a-modal :visible="previewVisible" :footer="null" @cancel="handleCancel1">
                <img alt="example" style="width: 100%" :src="previewImage" />
              </a-modal>
              请上传图片，最多4张
            </a-form-item>
          </a-col>
          <!-- <a-col :md="6" :sm="6">
            <a-form-item
                :labelCol="labelCol"
                :wrapperCol="wrapperCol"
                label="业务员是否确认"
            >
              <a-select v-decorator.trim="['salesConfirmed',validatorRules.salesConfirmed]" placeholder="请选择是否确认">
                <a-select-option value="0">未确认</a-select-option>
                <a-select-option value="1">已确认</a-select-option>
              </a-select>
            </a-form-item>
          </a-col> -->
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
import moment from "moment";
import JImageUpload from '@/components/jeecg/JImageUpload'
import { getFileAccessHttpUrl } from '@/api/manage'
import { ACCESS_TOKEN } from '@/store/mutation-types'

const uidGenerator=()=>{
  return '-'+parseInt(Math.random()*10000+1,10);
}
const getFileName=(path)=>{
  if(path.lastIndexOf("\\")>=0){
    let reg=new RegExp("\\\\","g");
    path = path.replace(reg,"/");
  }
  return path.substring(path.lastIndexOf("/")+1);
}
export default {
  name: "SaleOrderModal",
  mixins: [JEditableTableMixin, BillModalMixin],
  components: {
    ManyAccountModal,
    ImportItemModal,
    CustomerModal,
    AccountModal,
    HistoryBillList,
    WorkflowIframe,
    JImageUpload,
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
      uploadAction:window._CONFIG['domianURL']+"/systemConfig/upload?biz=saleOrder",
      previewVisible: false,
      headers:{},
      previewImage:"",
      title: "操作",
      width: "1600px",
      moreStatus: false,
      roleName: Vue.ls.get('roleName'),
      loginUser: Vue.ls.get('loginUser'),
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      visible: false,
      operTimeStr: "",
      prefixNo: "XSDD",
      fileList: [],
      model: {},
      colorList: {
        options: [],
        value: ''
      },
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
            title: "销售类型",
            dataIndex: "saleType",
            width: "10%",
            type: FormTypes.normal,
          },
          {
            title: "订单号",
            dataIndex: "number",
            width: "10%",
            type: FormTypes.normal,
          },
          {
            title: "业务员",
            dataIndex: "salesMan",
            width: "10%",
            type: FormTypes.normal,
          },
          {
            title: "货品编码",
            dataIndex: "commodityNo",
            width: "10%",
            type: FormTypes.normal
          },
          {
            title: "货品名称",
            dataIndex: "commodityName",
            width: "10%",
            type: FormTypes.normal
          },
          {
            title: "客户",
            key: "customer",
            width: "10%",
            type: FormTypes.normal,
          },
          { title: "颜色", dataIndex: "color", width: 50,ellipsis:true },
          { title: "S尺寸", dataIndex: "size1", width: 50,ellipsis:true },
          { title: "M尺寸", dataIndex: "size2", width: 50,ellipsis:true },
          { title: "L尺寸", dataIndex: "size3", width: 50,ellipsis:true },
          { title: "XL尺寸", dataIndex: "size4", width: 50,ellipsis:true },
          { title: "2XL尺寸", dataIndex: "size5", width: 50,ellipsis:true },
          { title: "3XL尺寸", dataIndex: "size6", width: 50,ellipsis:true },
          { title: "4XL尺寸", dataIndex: "size7", width: 50,ellipsis:true },
          { title: "5XL尺寸", dataIndex: "size8", width: 50,ellipsis:true },
          { title: "6XL尺寸", dataIndex: "size9", width: 50,ellipsis:true },
          { title: "7XL尺寸", dataIndex: "size10", width: 50,ellipsis:true },
          { title: "XS尺寸", dataIndex: "size11", width: 50,ellipsis:true },
          { title: "数量", dataIndex: "numbers", width: 50,ellipsis:true },
          { title: "单位", dataIndex: "unit", width: 50,ellipsis:true },
          { title: "下单备注", dataIndex: "orderRemark", width: 50,ellipsis:true },
          { title: "运费付款方", dataIndex: "freightPayer", width: 50,ellipsis:true },
          { title: "开票抬头", dataIndex: "financialInvoicePayable", width: 50,ellipsis:true },
          { title: "地址", dataIndex: "address", width: 50,ellipsis:true },
          { title: "电话", dataIndex: "telephone", width: 50,ellipsis:true },
          { title: "联系人", dataIndex: "contactPerson", width: 50,ellipsis:true },
        ],
      },
      confirmLoading: false,
      validatorRules: {
        saleType:{
          rules: [
            { required: true, message: '请选择销售类型！' },
          ]
        },
        customer:{
          rules: [
            { required: true, message: '请输入客户！' },
          ]
        },
        color:{
          rules: [
            { required: true, message: '请输入颜色！' },
          ]
        },
        unit:{
          rules: [
            { required: true, message: '请输入单位！' },
          ]
        },
        orderRemark:{
          rules: [
            { required: true, message: '请输入下单备注！' },
          ]
        },
        freightPayer:{
          rules: [
            { required: true, message: '请输入运费付款方！' },
          ]
        },
        address:{
          rules: [
            { required: true, message: '请输入地址！' },
          ]
        },
        telephone:{
          rules: [
            { required: true, message: '请输入电话！' },
          ]
        },
        contactPerson:{
          rules: [
            { required: true, message: '请输入联系人！' },
          ]
        },
        logoMark:{
          rules: [
            { required: true, message: '请输入LOGO制作！' },
          ]
        },
        billingRate:{
          rules: [
            { required: true, message: '请选择开票种类！' },
          ]
        },
        financialInvoicePayable:{
          rules: [
            { required: true, message: '请输入开票抬头！' },
          ]
        },
        commodityNo:{
          rules: [
            { required: true, message: '请选择货品编码！' },
          ]
        },
        isChangeBag:{
          rules: [
            { required: true, message: '请选择是否换拉链袋！' },
          ]
        },
        changeAmount:{
          rules: [
            { required: true, message: '请输入金额，如果为空请填0！' },
            { pattern: /^(([0-9][0-9]*)|([0]\.\d{0,4}|[0-9][0-9]*\.\d{0,4}))$/, message: '金额格式不正确!' }
          ]
        },
        numbers:{
          rules:[
            { required: true, message: '请输入数量，如果为空请填0！' },
            { pattern: /^[0-9]\d*$/, message: '请输入正整数，无请输入0!' }
          ]
        },
        operTime: {
          rules: [{ required: true, message: "请输入单据日期!" }],
        },
      },
      url: {
        add: "/depotHead/addDepotHead",
        edit: "/depotHead/addDepotHead",
        detailList: "/depotItem/getDetailList",
        getCommodityNoColors:"/warehouseManagement/getCommodityNoColors",
      },
    };
  },
  beforeCreate(){
  },
  created() {
    const token = Vue.ls.get(ACCESS_TOKEN);
    this.headers = {"X-Access-Token":token}
  },
  beforeMount(){
  },
  mounted(){
  },
  methods: {
    // 预览
    handlePreview (file) {
      this.previewImage = file.url || file.thumbUrl
      this.previewVisible = true
    },
    compressImage(file, quality = 0.5) {
      return new Promise((resolve, reject) => {
        const reader = new FileReader();
        reader.onload = (event) => {
          const img = new Image();
          img.src = event.target.result;

          img.onload = () => {
            const canvas = document.createElement("canvas");
            const ctx = canvas.getContext("2d");

            // 设置压缩后的宽高
            const MAX_WIDTH = 800; // 最大宽度
            const MAX_HEIGHT = 800; // 最大高度
            let {width, height} = img;

            if (width > MAX_WIDTH || height > MAX_HEIGHT) {
              if (width > height) {
                height *= MAX_WIDTH / width;
                width = MAX_WIDTH;
              } else {
                width *= MAX_HEIGHT / height;
                height = MAX_HEIGHT;
              }
            }
            canvas.width = width;
            canvas.height = height;
            // 绘制压缩图像
            ctx.drawImage(img, 0, 0, width, height);

            // 导出为 Blob
            canvas.toBlob(
                (blob) => {
                  if (blob) {
                    const compressedFile = new File([blob], file.name, {
                      type: file.type,
                      lastModified: Date.now(),
                    });
                    resolve(compressedFile);
                  } else {
                    reject(new Error("Image compression failed."));
                  }
                },
                file.type,
                quality // 压缩质量
            );
          };

          img.onerror = (error) => reject(error);
        };

        reader.onerror = (error) => reject(error);

        // 开始读取文件
        reader.readAsDataURL(file);
      });
    },
    beforeUpload: async function (file) {
      this.uploadGoOn = true
      let fileType = file.type;
      let fileSize = file.size;
      if (fileType.indexOf('image') < 0) {
        this.$message.warning('请上传图片');
        this.uploadGoOn = false
        return false;
      }
      if(fileSize>3145728){
        const compressedFile = await this.compressImage(file);
        return compressedFile
      }
      return false
    },
    handleChange({ fileList }) {
      fileList.forEach((file) => {
        if(!file.uid) {
          file.uid = uidGenerator();
        }
      });
      this.fileList = fileList;
    },
    handleCancel1() {
      this.previewVisible = false;
    },
    // handleChange(info) {
    //   console.log("--文件列表改变--")
    //   if(!info.file.status && this.uploadGoOn === false){
    //     info.fileList.pop();
    //   }
    //   this.picUrl = false;
    //   let fileList = info.fileList
    //   if(info.file.status==='done'){
    //     if(info.file.response.code === 200){
    //       this.picUrl = true;
    //       fileList = fileList.map((file) => {
    //         if (file.response) {
    //           file.url = file.response.data;
    //         }
    //         return file;
    //       });
    //     }
    //     //this.$message.success(`${info.file.name} 上传成功!`);
    //   }else if (info.file.status === 'error') {
    //     this.$message.error(`${info.file.name} 上传失败.`);
    //   }else if(info.file.status === 'removed'){
    //     this.handleDelete(info.file)
    //   }
    //   this.fileList = fileList
    //   if(info.file.status==='done' || info.file.status === 'removed'){
    //     this.handlePathChange()
    //   }
    // },
    // handlePathChange(){
    //   let uploadFiles = this.fileList
    //   let path = ''
    //   if(!uploadFiles || uploadFiles.length==0){
    //     path = ''
    //   }
    //   let arr = [];
    //   if(!this.isMultiple){
    //     arr.push(uploadFiles[uploadFiles.length-1].response.data)
    //   }else{
    //     for(var a=0;a<uploadFiles.length;a++){
    //       arr.push(uploadFiles[a].response.data)
    //     }
    //   }
    //   if(arr.length>0){
    //     path = arr.join(",")
    //   }
    //   this.$emit('change', path);
    // },
    // handleDelete(file){
    //   //如有需要新增 删除逻辑
    //   console.log(file)
    // },
    // handleCancel() {
    //   this.close();
    //   this.previewVisible = false;
    // },
    getImgUrl(fileName,size) {

      if(fileName) {
        console.log(fileName)
        return fileName[size].url
      } else {
        return ''
      }
    },
    // 财务成本总价
    handleCostUnitChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("financialCostUnitPrice")
        this.form.setFieldsValue({
          financialCostAmount: num.toFixed(4),
        });
      });
      // this.handleOrderChange();
    },
    // 销售总价
    handleUnitChange(e) {
      this.$nextTick(() => {
        let num = 0;
        num = this.form.getFieldValue("numbers")*this.form.getFieldValue("financialUnitPrice")
        this.form.setFieldsValue({
          totalFinancialPrice: num.toFixed(4),
        });
      });
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
      this.handleUnitChange() // 销售总价
      this.handleCostUnitChange() // 成本金额
    },
    initFileList(paths){

      if(!paths || paths.length==0){
        this.fileList = [];
        this.picUrl = false;
        return;
      }
      this.picUrl = true;
      let fileList = [];
      let arr = paths.split(",")
      for(var a=0;a<arr.length;a++){
         var domain=window.location.host;   //获取当前域名(ip)和端口号
        // var urlPre = 'http://'+domain+'/upload/avatar/'
        // let url = urlPre+ arr[a];
        let url = getFileAccessHttpUrl('systemConfig/static/' + arr[a]);
        // let url = arr[a];
        console.log(url)
        fileList.push({
          uid: uidGenerator(),
          name: getFileName(arr[a]),
          status: 'done',
          url: url,
          response:{
            code:"history",
            data:arr[a]
          }
        })
      }

      console.log(fileList)
      this.fileList = fileList
    },
    //调用完edit()方法之后会自动调用此方法
    editAfter() {
      this.billStatus = "0";
      this.currentSelectDepotId = "";
      this.changeColumnHide();
      if (this.action === "add") {
        // 自动生成编号
        this.addInit(this.prefixNo);
        this.personList.value = "";
        this.fileList = [];
        this.$nextTick(() => {
          handleIntroJs(this.prefixNo, 1);
        });
      } else {
        // this.model.operTime = this.model.operTimeStr;
        this.model.operTime = moment(this.model.operTime).format("YYYY-MM-DD HH:mm:ss");
        if (this.model.accountId == null && this.model.accountIdList) {
          this.model.accountId = 0;
          this.manyAccountBtnStatus = true;
          this.accountIdList = this.model.accountIdList;
          this.accountMoneyList = this.model.accountMoneyList;
        } else {
          this.manyAccountBtnStatus = false;
        }
        this.personList.value = this.model.salesMan;
        this.$nextTick(() => {
          this.form.setFieldsValue(
            pick(
              this.model,
                "type",
                "operTime",
                "saleType",
                "subType",
                "customer",
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
                "freightPayer",
                "address",
                "telephone",
                "contactPerson",
                "financialUnitPrice",
                "totalFinancialPrice",
                "logoMark",
                "isChangeBag",
                "billingRate",
                "financialCostUnitPrice",
                "financialCostAmount",
                "financialInvoicePayable",
                "remark",
                "orderRemark"
            )
          );
        });

        this.initFileList(this.model.fileName)
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
      // this.initSystemConfig();
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
        "其它",
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
            status:9,
            // defaultNumber:this.form.getFieldValue("number"),
            commodityName: this.commodityList.options.find(
                (item) =>
                    item.value == this.form.getFieldValue("commodityNo")
            ).name,
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
            type:'销售订单',
            subType:'销售订单',
            salesMan:this.form.getFieldValue("salesMan")|| this.loginUser.username,
            status:0,
            defaultNumber:this.form.getFieldValue("number"),
            commodityName: this.commodityList.options.find(
                (item) =>
                    item.value == this.form.getFieldValue("commodityNo")
            ).name,
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
      //图片校验
      if(this.fileList && this.fileList.length > 0) {
        let files= []
        this.fileList.forEach(function (item) {
          files.push(item.response.data)
        })
        let fileStr = files.join(',')
        console.log(fileStr)
        formData.fileName = fileStr
        if(this.fileList.length > 4) {
          this.$message.warning('抱歉，图片最多4张！');
          return
        }
      } else {
        formData.fileName = ''
      }
      //接口调用
      let url = this.url.add,
        method = "post";
      if (this.model.id) {

        console.log(this.model.id)
        console.log(formData)
        url = this.url.edit;
        method = "post";
      }
      const that = this;
      this.confirmLoading = true;
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
    commodityChange(){
      if(!this.form.getFieldValue('saleType') || !this.form.getFieldValue('commodityNo')){
        this.$message.error('请先选择销售类型以及货品编号！');
      }else{
        if(this.form.getFieldValue('saleType')=='自有仓库'){
          this.form.getFieldValue('commodityNo')
          let that = this;
          // this.form.getFieldValue('commodityNo')
          let commodityStr = this.form.getFieldValue('commodityNo')
          getAction(this.url.getCommodityNoColors, {commodityNo:commodityStr}).then(res=>{
            console.log(res);
            if(res.code=='1'){
              that.colorList.options = res.data;
            }
          })
        }
      }
    },
    saleTypeChange(){
      let that = this;
      that.colorList.options = Vue.ls.get('materialPropertyList');
    }
  },
};
</script>
<style scoped></style>
