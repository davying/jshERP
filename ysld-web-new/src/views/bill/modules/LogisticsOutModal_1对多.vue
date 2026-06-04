<template>
  <j-modal
      :title="title"
      :width="width"
      :visible="visible"
      :confirmLoading="confirmLoading"
      :keyboard="false"
      :forceRender="true"
      switchFullscreen
      @cancel="handleCancel"
      style="top:20px;height: 95%;">
    <template slot="footer">
      <a-button @click="handleCancel">取消</a-button>
      <!--      <a-button v-if="checkFlag && isCanCheck" :loading="confirmLoading" @click="handleOkAndCheck">保存并审核</a-button>-->
      <a-button type="primary" :loading="confirmLoading" @click="handleOk">保存</a-button>
      <!--发起多级审核-->
      <!--      <a-button v-if="!checkFlag" @click="handleWorkflow()" type="primary">提交流程</a-button>-->
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-row class="form-row" :gutter="24">
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="订单编号">
              <a-input disabled v-decorator.trim="['depotId']"/>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="货品编码">
              <a-input disabled v-decorator.trim="['commodityNo']">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="颜色">
              <a-input disabled v-decorator.trim="['color']">
              </a-input>
            </a-form-item>
          </a-col>
          <a-col :lg="6" :md="12" :sm="24">
            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="业务员">
              <a-input disabled v-decorator.trim="['salesMan']">
              </a-input>
            </a-form-item>
          </a-col>
        </a-row>
        <j-editable-table id="billModal"
                          :ref="refKeys[0]"
                          :loading="materialTable.loading"
                          :columns="materialTable.columns"
                          :dataSource="materialTable.dataSource"
                          :minWidth="minWidth"
                          :maxHeight="300"
                          :rowNumber="false"
                          :rowSelection="true"
                          :actionButton="true"
                          :dragSort="true"
                          @valueChange="onValueChange"
                          @added="onAdded"
                          @deleted="onDeleted">
        </j-editable-table>
      </a-form>
    </a-spin>
  </j-modal>
</template>
<script>
import pick from 'lodash.pick'
import { FormTypes } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { BillModalMixin } from '../mixins/BillModalMixin'
import JUpload from '@/components/jeecg/JUpload'
import JDate from '@/components/jeecg/JDate'
import { getAction,postAction } from '@/api/manage'
import Vue from 'vue'
export default {
  name: "LogisticsOutModal",
  mixins: [JEditableTableMixin, BillModalMixin],
  components: {
    JUpload,
    JDate
  },
  data () {
    return {
      title:"操作",
      width: '1600px',
      moreStatus: false,
      // 新增时子表默认添加几行空数据
      addDefaultRowNum: 1,
      visible: false,
      prefixNo: 'DBCK',
      fileList:[],
      model: {},
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      refKeys: ['materialDataTable', ],
      activeKey: 'materialDataTable',
      materialTable: {
        loading: false,
        dataSource: [],
        columns: [
          // { title: "发货订单编号", key: "depotId", width:'10%',type: FormTypes.normal},
          // { title: '货品编码', key: 'commodityNo', width: '8%', type: FormTypes.normal },
          { title: '类型', key: 'type', width: '8%', type: FormTypes.select, placeholder: '请选择${title}', options: [],
            allowSearch:true, validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          // { title: "颜色", key: "color", width:'6%',type: FormTypes.normal,
          //   validateRules: [{ required: true, message: '${title}不能为空' }]},
          // { title: "业务员", key: "salesMan", width:'6%',type: FormTypes.normal},
          { title: "发货日期", key: "deliveryDate", width:'9%',type: FormTypes.date},
          { title: "物流运费", key: "logisticsFreight", width:'6%',type: FormTypes.inputNumber},
          { title: "物流方式", key: "logisticsMode", width:'6%',type: FormTypes.input},
          { title: "实发数量", key: "actualQuantity", width:'6%',type: FormTypes.inputNumber},
          { title: "发货单号", key: "shippingOrderNumber", width:'8%',type: FormTypes.input},
          { title: "S", key: "size1", width:'4%',type: FormTypes.inputNumber},
          { title: "M", key: "size2", width:'4%',type: FormTypes.inputNumber},
          { title: "L", key: "size3", width:'4%',type: FormTypes.inputNumber},
          { title: "XL", key: "size4", width:'4%',type: FormTypes.inputNumber},
          { title: "2XL", key: "size5", width:'4%',type: FormTypes.inputNumber},
          { title: "3XL", key: "size6", width:'4%',type: FormTypes.inputNumber},
          { title: "4XL", key: "size7", width:'4%',type: FormTypes.inputNumber},
          { title: "5XL", key: "size8", width:'4%',type: FormTypes.inputNumber},
          { title: "6XL", key: "size9", width:'4%',type: FormTypes.inputNumber},
          { title: "7XL", key: "size10", width:'4%',type: FormTypes.inputNumber},
          { title: "XS", key: "size11", width:'4%',type: FormTypes.inputNumber},
          { title: "备注", key: "remark", width:'10%',type: FormTypes.input}
        ]
      },
      confirmLoading: false,
      url: {
        getDepotHeadIds: "/depotHead/getDepotHeadIds",
        add: '/warehouseManagement/wmLogisticsOutRows',
        // add: "/warehouseManagement/add",
        edit: "/warehouseManagement/update",
        searchLinkNumber: "/depotHead/list",
        checkNumberOutInfo:"/depotHead/checkNumberOutInfo",
        // detailList: '/depotItem/getDetailList'
        detailList: '/warehouseManagement/getWMByNumber'
      }
    }
  },
  created () {
  },
  methods: {
    //调用完edit()方法之后会自动调用此方法
    editAfter() {
      // this.billStatus = '0'
      // this.currentSelectDepotId = ''
      // this.changeColumnHide()
      // this.changeFormTypes(this.materialTable.columns, 'snList', 0)
      // this.changeFormTypes(this.materialTable.columns, 'batchNumber', 0)
      // this.changeFormTypes(this.materialTable.columns, 'expirationDate', 0)
      if (this.action === 'add') {
        // this.addInit(this.prefixNo)
        this.fileList = []
      } else {
        this.fileList = this.model.fileName
        this.$nextTick(() => {
          this.form.setFieldsValue(pick(this.model,
              "depotId",
              "salesMan",
              "color",
              "size1",
              "price",
              "depotIds",
              "type",
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
              "numbers"
          ))
        });
      }
      // 加载子表数据
      let params = {
        headerId: this.model.depotId,
        // mpList: getMpListShort(Vue.ls.get('materialPropertyList')),  //扩展属性
        // linkType: 'basic'
      }
      let url = this.readOnly ? this.url.detailList : this.url.detailList;
      this.requestSubTableData(url, params, this.materialTable);
      // //复制新增单据-初始化单号和日期
      // if(this.action === 'copyAdd') {
      //   this.model.id = ''
      //   this.model.tenantId = ''
      //   // this.copyAddInit(this.prefixNo)
      // }
      // this.initSystemConfig()
      // this.initDepot()
      // this.initCommoditys()
      this.initLogisticsOut();
    },
    //提交单据时整理成formData
    classifyIntoFormData(allValues) {

      // let totalPrice = 0
      // let billMain = Object.assign(this.model, allValues.formValue)
      let detailArr = allValues.tablesValue[0].values
      // billMain.type = '出库'
      // billMain.subType = '调拨'
      // billMain.defaultNumber = billMain.number
      // for(let item of detailArr){
      //   totalPrice += item.allPrice-0
      // }
      // billMain.totalPrice = totalPrice
      // if(this.fileList && this.fileList.length > 0) {
      //   billMain.fileName = this.fileList
      // } else {
      //   billMain.fileName = ''
      // }
      // if(this.model.id){
      //   billMain.id = this.model.id
      // }
      // billMain.status = this.billStatus
      return {
        // info: JSON.stringify(billMain),
        rows: JSON.stringify(detailArr),
      }
    },
  }
}
</script>
<style scoped>

</style>