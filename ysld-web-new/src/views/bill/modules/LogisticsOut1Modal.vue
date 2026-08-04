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
          <!--          <template #buttonAfter>-->
          <!--            <a-row :gutter="24" style="float:left;" data-step="4" data-title="扫码录入" data-intro="此功能支持扫码枪扫描商品条码进行录入">-->
          <!--              <a-col v-if="scanStatus" :md="6" :sm="24">-->
          <!--                <a-button @click="scanEnter">扫码录入</a-button>-->
          <!--              </a-col>-->
          <!--              <a-col v-if="!scanStatus" :md="16" :sm="24" style="padding: 0 6px 0 12px">-->
          <!--                <a-input placeholder="请扫描商品条码并回车" v-model="scanBarCode" @pressEnter="scanPressEnter" ref="scanBarCode"/>-->
          <!--              </a-col>-->
          <!--              <a-col v-if="!scanStatus" :md="6" :sm="24" style="padding: 0px">-->
          <!--                <a-button @click="stopScan">收起扫码</a-button>-->
          <!--              </a-col>-->
          <!--            </a-row>-->
          <!--          </template>-->
          <!--          <template #depotBatchSet>-->
          <!--            <a-icon type="down" @click="handleBatchSetDepot" />-->
          <!--          </template>-->
          <!--          <template #depotAdd>-->
          <!--            <a-divider v-if="isTenant" style="margin: 4px 0;" />-->
          <!--            <div v-if="isTenant" style="padding: 4px 8px; cursor: pointer;" @click="addDepot"><a-icon type="plus" /> 新增仓库</div>-->
          <!--          </template>-->
          <template slot="logisticsDetailsJson" slot-scope="slotProps">
            <div class="batch-logistics-cell">
              <a-button size="small" type="link" @click="openBatchLogisticsDetailModal(slotProps)">编辑明细</a-button>
              <span class="batch-logistics-cell-summary">{{ formatLogisticsDetailsCell(slotProps.value) }}</span>
            </div>
          </template>
        </j-editable-table>
        <!--        <a-row class="form-row" :gutter="24">-->
        <!--          <a-col :lg="24" :md="24" :sm="24">-->
        <!--            <a-form-item :labelCol="labelCol" :wrapperCol="{xs: { span: 24 },sm: { span: 24 }}" label="">-->
        <!--              <a-textarea :rows="1" placeholder="请输入备注" v-decorator="[ 'remark' ]" style="margin-top:8px;"/>-->
        <!--            </a-form-item>-->
        <!--          </a-col>-->
        <!--        </a-row>-->
        <!--        <a-row class="form-row" :gutter="24">-->
        <!--          <a-col :lg="6" :md="12" :sm="24">-->
        <!--            <a-form-item :labelCol="labelCol" :wrapperCol="wrapperCol" label="附件">-->
        <!--              <j-upload v-model="fileList" bizPath="bill"></j-upload>-->
        <!--            </a-form-item>-->
        <!--          </a-col>-->
        <!--        </a-row>-->
      </a-form>
    </a-spin>
    <div v-if="batchLogisticsDetailVisible" class="batch-logistics-floating-panel">
      <div class="batch-logistics-floating-header">
        <span>物流明细</span>
        <a-button size="small" type="link" @click="cancelBatchLogisticsDetailModal">关闭</a-button>
      </div>
      <div class="batch-logistics-detail-card">
        <div class="batch-logistics-detail-title">
          <span>物流明细</span>
          <a-button
              size="small"
              type="primary"
              icon="plus"
              :disabled="batchLogisticsDetails.length >= logisticsMaxDetailCount"
              @click="addBatchLogisticsDetail"
          >新增明细</a-button>
        </div>
        <table class="batch-logistics-detail-table">
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
          <tr v-for="(detail, index) in batchLogisticsDetails" :key="detail.uid">
            <td>
              <a-select v-model="detail.logisticsMode" placeholder="请选择物流方式">
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
              <a-input v-model="detail.shippingOrderNumber" placeholder="请输入物流单号" />
            </td>
            <td>
              <a-input-number
                  v-model="detail.deliveryQuantity"
                  :min="0"
                  :precision="0"
                  placeholder="请输入发货数量"
                  style="width: 100%"
              />
            </td>
            <td>
              <a-input-number
                  v-model="detail.freight"
                  :min="0"
                  :precision="4"
                  placeholder="请输入运费"
                  style="width: 100%"
              />
            </td>
            <td class="batch-logistics-detail-action">
              <a-button
                  size="small"
                  type="link"
                  :disabled="batchLogisticsDetails.length <= 1"
                  @click="removeBatchLogisticsDetail(index)"
              >删除</a-button>
            </td>
          </tr>
          </tbody>
        </table>
        <div class="batch-logistics-detail-summary">
          最多 8 条物流明细；当前汇总：{{ getBatchLogisticsSummaryText(batchLogisticsDetails) }}。
        </div>
      </div>
      <div class="batch-logistics-floating-footer">
        <a-button @click="cancelBatchLogisticsDetailModal">取消</a-button>
        <a-button type="primary" @click="confirmBatchLogisticsDetails">确定</a-button>
      </div>
    </div>
  </j-modal>
</template>
<script>
import pick from 'lodash.pick'
import { FormTypes } from '@/utils/JEditableTableUtil'
import { JEditableTableMixin } from '@/mixins/JEditableTableMixin'
import { BillModalMixin } from '../mixins/BillModalMixin'
import JUpload from '@/components/jeecg/JUpload'
import JDate from '@/components/jeecg/JDate'
export default {
  name: "LogisticsOut1Modal",
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
      logisticsDetailModes: ['三轮车', '壹米滴答', '百世快运', '报销', '其他'],
      logisticsMaxDetailCount: 8,
      logisticsDetailUid: 0,
      batchLogisticsDetailVisible: false,
      batchLogisticsDetails: [],
      batchLogisticsEditing: null,
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
          { title: '发货订单编号', key: 'depotIds', width: '10%', kind: 'depotIds',type: FormTypes.select, placeholder: '请选择${title}', options: [],
            allowSearch:true, validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          { title: '货品编码', key: 'commodityNo', width: '8%', type: FormTypes.normal },
          { title: '客户', key: 'customer', width: '8%', type: FormTypes.normal },
          { title: '类型', key: 'type', width: '8%', type: FormTypes.select, placeholder: '请选择${title}', options: [],
            allowSearch:true, validateRules: [{ required: true, message: '${title}不能为空' }]
          },
          // { title: '条码', key: 'barCode', width: '12%', type: FormTypes.popupJsh, kind: 'material', multi: true,
          //   validateRules: [{ required: true, message: '${title}不能为空' }]
          // },
          { title: "颜色", key: "color", width:'6%',type: FormTypes.normal,
            validateRules: [{ required: true, message: '${title}不能为空' }]},
          { title: "业务员", key: "salesMan", width:'6%',type: FormTypes.normal},
          { title: "发货日期", key: "deliveryDate", width:'12%',type: FormTypes.date},
          { title: "运费付款方", key: "freightPayer", width:'9%',type: FormTypes.normal},
          { title: "物流明细", key: "logisticsDetailsJson", width:'12%', type: FormTypes.hidden },
          { title: "物流运费", key: "logisticsFreight", width:'6%',type: FormTypes.inputNumber},
          { title: "物流方式", key: "logisticsMode", width:'6%',type: FormTypes.hidden},
          { title: "实发数量", key: "actualQuantity", width:'6%',type: FormTypes.inputNumber},
          { title: "发货单号", key: "shippingOrderNumber", width:'15%',type: FormTypes.hidden},
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
          // { title: '名称', key: 'name', width: '10%', type: FormTypes.normal },
          // { title: '规格', key: 'standard', width: '9%', type: FormTypes.normal },
          // { title: '型号', key: 'model', width: '9%', type: FormTypes.normal },
          // { title: '颜色', key: 'color', width: '5%', type: FormTypes.normal },
          // { title: '扩展信息', key: 'materialOther', width: '5%', type: FormTypes.normal },
          // { title: '库存', key: 'stock', width: '5%', type: FormTypes.normal },
          // { title: '调入仓库', key: 'anotherDepotId', width: '8%', type: FormTypes.select, placeholder: '请选择${title}', options: [], allowSearch:true},
          // { title: '单位', key: 'unit', width: '4%', type: FormTypes.normal },
          // { title: '多属性', key: 'sku', width: '9%', type: FormTypes.normal },
          // { title: '数量', key: 'operNumber', width: '5%', type: FormTypes.inputNumber, statistics: true,
          //   validateRules: [{ required: true, message: '${title}不能为空' }]
          // },
          // { title: '单价', key: 'unitPrice', width: '5%', type: FormTypes.inputNumber},
          // { title: '金额', key: 'allPrice', width: '5%', type: FormTypes.inputNumber, statistics: true },
          // { title: '备注', key: 'remark', width: '5%', type: FormTypes.input }
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
        detailList: '/depotItem/getDetailList'
      }
    }
  },
  created () {
  },
  methods: {
    nextLogisticsDetailUid() {
      this.logisticsDetailUid += 1
      return `batch_logistics_detail_${this.logisticsDetailUid}`
    },
    normalizeLogisticsInput(value) {
      if (value === null || value === undefined || value === '') {
        return ''
      }
      return value
    },
    toNumber(value) {
      const number = Number(value)
      return Number.isFinite(number) ? number : 0
    },
    toQuantity(value) {
      return Math.trunc(this.toNumber(value))
    },
    formatAmount(value) {
      const amount = this.toNumber(value)
      return Number.isInteger(amount) ? String(amount) : String(Number(amount.toFixed(4)))
    },
    createEmptyLogisticsDetail(detail, defaultDeliveryDate) {
      const source = detail || {}
      const deliveryQuantity = source.deliveryQuantity !== undefined && source.deliveryQuantity !== null
        ? source.deliveryQuantity
        : source.quantity
      return {
        uid: source.uid || this.nextLogisticsDetailUid(),
        logisticsMode: source.logisticsMode || '',
        deliveryDate: source.deliveryDate || defaultDeliveryDate || '',
        shippingOrderNumber: source.shippingOrderNumber || source.trackingNo || '',
        deliveryQuantity: this.normalizeLogisticsInput(deliveryQuantity),
        freight: this.normalizeLogisticsInput(source.freight),
      }
    },
    createEmptyLogisticsDetails(defaultDeliveryDate) {
      return [this.createEmptyLogisticsDetail({}, defaultDeliveryDate)]
    },
    parseLogisticsDetails(value) {
      if (Array.isArray(value)) {
        return value
      }
      if (!value) {
        return []
      }
      try {
        const parsed = JSON.parse(value)
        return Array.isArray(parsed) ? parsed : []
      } catch (e) {
        return []
      }
    },
    normalizeLogisticsDetails(value, defaultDeliveryDate) {
      const details = this.parseLogisticsDetails(value)
        .filter((detail) => detail)
        .slice(0, this.logisticsMaxDetailCount)
        .map((detail) => this.createEmptyLogisticsDetail(detail, defaultDeliveryDate))
      return details.length > 0 ? details : this.createEmptyLogisticsDetails(defaultDeliveryDate)
    },
    buildCleanLogisticsDetails(details) {
      return (details || [])
        .map((detail) => ({
          logisticsMode: detail.logisticsMode || '',
          deliveryDate: detail.deliveryDate || '',
          shippingOrderNumber: detail.shippingOrderNumber || detail.trackingNo || '',
          deliveryQuantity: this.toQuantity(detail.deliveryQuantity),
          freight: this.toNumber(detail.freight),
        }))
        .filter((detail) => detail.shippingOrderNumber || detail.deliveryQuantity || detail.freight)
    },
    calculateLogisticsSummary(details) {
      const cleanDetails = this.buildCleanLogisticsDetails(details)
      return {
        count: cleanDetails.length,
        logisticsFreight: cleanDetails.reduce((total, detail) => total + detail.freight, 0),
        actualQuantity: cleanDetails.reduce((total, detail) => total + detail.deliveryQuantity, 0),
      }
    },
    formatLogisticsDetailsCell(value) {
      const details = this.buildCleanLogisticsDetails(this.parseLogisticsDetails(value))
      if (details.length === 0) {
        return '未填写'
      }
      const summary = this.calculateLogisticsSummary(details)
      return `${summary.count}条 / ${summary.actualQuantity}件 / ${this.formatAmount(summary.logisticsFreight)}元`
    },
    getBatchLogisticsSummaryText(details) {
      const summary = this.calculateLogisticsSummary(details)
      return `${summary.count}条 / ${summary.actualQuantity}件 / ${this.formatAmount(summary.logisticsFreight)}元`
    },
    openBatchLogisticsDetailModal(slotProps) {
      const row = typeof slotProps.getValue === 'function' ? slotProps.getValue() : {}
      const logisticsDetailsJson = row.logisticsDetailsJson || slotProps.value || ''
      this.batchLogisticsEditing = {
        rowId: slotProps.rowId,
        target: slotProps.target,
        handleChange: slotProps.handleChange,
      }
      this.batchLogisticsDetails = this.normalizeLogisticsDetails(logisticsDetailsJson, row.deliveryDate)
      this.batchLogisticsDetailVisible = true
    },
    addBatchLogisticsDetail() {
      if (this.batchLogisticsDetails.length >= this.logisticsMaxDetailCount) {
        this.$message.warning(`最多只能添加${this.logisticsMaxDetailCount}条物流明细`)
        return
      }
      this.batchLogisticsDetails.push(this.createEmptyLogisticsDetail())
    },
    removeBatchLogisticsDetail(index) {
      if (this.batchLogisticsDetails.length <= 1) {
        return
      }
      this.batchLogisticsDetails.splice(index, 1)
    },
    cancelBatchLogisticsDetailModal() {
      this.batchLogisticsDetailVisible = false
      this.batchLogisticsDetails = []
      this.batchLogisticsEditing = null
    },
    confirmBatchLogisticsDetails() {
      if (!this.batchLogisticsEditing || !this.batchLogisticsEditing.target) {
        this.cancelBatchLogisticsDetailModal()
        return
      }
      const details = this.buildCleanLogisticsDetails(this.batchLogisticsDetails)
      const logisticsDetailsJson = JSON.stringify(details)
      const summary = this.calculateLogisticsSummary(details)
      const values = {
        logisticsDetailsJson,
        logisticsFreight: details.length > 0 ? this.formatAmount(summary.logisticsFreight) : '',
        actualQuantity: details.length > 0 ? summary.actualQuantity : '',
      }
      try {
        if (typeof this.batchLogisticsEditing.handleChange === 'function') {
          this.batchLogisticsEditing.handleChange(logisticsDetailsJson)
        }
        if (typeof this.batchLogisticsEditing.target.setValues !== 'function') {
          this.$message.error('物流明细回写失败，请重新编辑')
          return
        }
        this.batchLogisticsEditing.target.setValues([{ rowKey: this.batchLogisticsEditing.rowId, values }])
      } finally {
        this.cancelBatchLogisticsDetailModal()
      }
    },
    buildSubmitRow(row) {
      const submitRow = Object.assign({}, row)
      const details = this.buildCleanLogisticsDetails(this.parseLogisticsDetails(submitRow.logisticsDetailsJson))
      const hasLogisticsDetails = details.length > 0
      if (hasLogisticsDetails) {
        const summary = this.calculateLogisticsSummary(details)
        submitRow.logisticsDetailsJson = JSON.stringify(details)
        submitRow.logisticsFreight = this.formatAmount(summary.logisticsFreight)
        submitRow.actualQuantity = summary.actualQuantity
      } else if (submitRow.logisticsDetailsJson) {
        submitRow.logisticsDetailsJson = '[]'
      }
      this.clearLogisticsTemporaryFields(submitRow)
      return submitRow
    },
    clearLogisticsTemporaryFields(row) {
      [
        'tricycleShippingOrderNumber',
        'tricycleDeliveryQuantity',
        'tricycleFreight',
        'ymddShippingOrderNumber',
        'ymddDeliveryQuantity',
        'ymddFreight',
        'bestShippingOrderNumber',
        'bestDeliveryQuantity',
        'bestFreight',
        'reimburseShippingOrderNumber',
        'reimburseDeliveryQuantity',
        'reimburseFreight',
      ].forEach((key) => {
        delete row[key]
      })
    },
    onValueChange(event) {
      BillModalMixin.methods.onValueChange.call(this, event)
    },
    onAdded(event) {
      BillModalMixin.methods.onAdded.call(this, event)
    },
    onDeleted(ids, target) {
      BillModalMixin.methods.onDeleted.call(this, ids, target)
    },
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
              "numbers",
              "freightPayer"
          ))
        });
      }
      // 加载子表数据
      let params = {
        headerId: this.model.id,
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
      let detailArr = allValues.tablesValue[0].values.map((row) => this.buildSubmitRow(row))
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
.batch-logistics-cell {
  display: flex;
  align-items: center;
  gap: 8px;
  min-height: 32px;
}
.batch-logistics-cell-summary {
  color: #666;
  white-space: nowrap;
}
.batch-logistics-floating-panel {
  position: fixed;
  top: 96px;
  left: 50%;
  z-index: 1002;
  width: 920px;
  max-width: calc(100vw - 72px);
  max-height: calc(100vh - 160px);
  overflow: auto;
  padding: 16px;
  background: #fff;
  border: 1px solid #e8e8e8;
  border-radius: 4px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.18);
  transform: translateX(-50%);
}
.batch-logistics-floating-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 12px;
  font-size: 16px;
  font-weight: 600;
  color: #333;
}
.batch-logistics-floating-footer {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 16px;
}
.batch-logistics-detail-card {
  padding: 4px 0;
}
.batch-logistics-detail-title {
  display: flex;
  align-items: center;
  margin-bottom: 12px;
  font-weight: 600;
  color: #333;
}
.batch-logistics-detail-title span {
  font-size: 16px;
  line-height: 24px;
}
.batch-logistics-detail-title .ant-btn {
  height: 28px;
  margin-left: 16px;
  padding: 0 12px;
  font-size: 13px;
  line-height: 26px;
}
.batch-logistics-detail-table {
  width: 100%;
  border-collapse: collapse;
  background: #fff;
}
.batch-logistics-detail-table th,
.batch-logistics-detail-table td {
  padding: 8px;
  border: 1px solid #e8e8e8;
}
.batch-logistics-detail-table th {
  background: #f5f5f5;
  color: #333;
}
.batch-logistics-detail-table td:first-child {
  width: 140px;
}
.batch-logistics-detail-table td:nth-child(2) {
  width: 150px;
}
.batch-logistics-detail-table td:nth-child(4),
.batch-logistics-detail-table td:nth-child(5) {
  width: 130px;
}
.batch-logistics-detail-table td:last-child {
  width: 70px;
  text-align: center;
}
.batch-logistics-detail-table .ant-select,
.batch-logistics-detail-table .ant-input,
.batch-logistics-detail-table .ant-calendar-picker {
  width: 100%;
}
.batch-logistics-detail-summary {
  margin-top: 8px;
  color: #888;
}
</style>
