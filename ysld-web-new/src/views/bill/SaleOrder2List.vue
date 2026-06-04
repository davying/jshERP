<!-- create jishenghua-->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
<!--              <a-col :md="6" :sm="24">-->
<!--                <a-form-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                  <a-input placeholder="请输入单据编号" v-model="queryParam.number"></a-input>-->
<!--                </a-form-item>-->
<!--              </a-col>-->
              <a-col :md="5" :sm="24">
                <a-form-item label="业务员" :labelCol="labelCol" :wrapperCol="wrapperCol">
<!--                  <a-input placeholder="请输入业务员姓名" v-model="queryParam.salesMan"></a-input>-->
                  <a-select v-model="queryParam.salesMan" :allowClear="true" showSearch>
                    <a-select-option v-for="(person,index) in personList" :value="person.text" :key="index">
                      {{ person.text }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="印刷员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="queryParam.printer"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="印刷对帐日" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date
                          v-model="queryParam.printingReconciliationDate"
                          :dateFormat="'YYYY-MM-DD'"
                  />
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="烫画员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input v-model="queryParam.ironingStaff"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="叠人员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input  v-model="queryParam.stackStaff"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="烫叠对帐日" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date
                          v-model="queryParam.stampingReconciliationDate"
                          :dateFormat="'YYYY-MM-DD'"
                  />
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="刺绣员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入刺绣员" v-model="queryParam.rustStaff"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="刺绣对账日" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <j-date
                          v-model="queryParam.embroideryReconciliationDate"
                          :dateFormat="'YYYY-MM-DD'"
                  />
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="尾款是否结清" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-radio-group name="radioGroup" v-model="queryParam.isFinalPay">
                    <a-radio value="是">是</a-radio>
                    <a-radio value="否">否</a-radio>
                  </a-radio-group>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="订单状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                      ref="select"
                      v-model="queryParam.status"
                      @focus="focus"
                      :allowClear="true"
                  >
                    <a-select-option value="9">业务员已提交</a-select-option>
                    <a-select-option value="4">仓库已提交</a-select-option>
                    <a-select-option value="1">财务已审核</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入客户" v-model="queryParam.customer"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="销售部门"
                >
                  <a-select
                      @focus="focus"
                      v-model="queryParam.deptName">
                    <a-select-option value="全部">全部</a-select-option>
                    <a-select-option value="销售一部">销售一部(英诗澜得)</a-select-option>
                    <a-select-option value="销售二部">销售二部(岭泽)</a-select-option>
                    <a-select-option value="销售三部">销售三部(雪狼)</a-select-option>
                    <a-select-option value="财务仓储">财务仓储</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="颜色" v-model="queryParam.color"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="货品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="货品编码" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="单据编号" v-model="queryParam.number"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="订单日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-range-picker
                      style="width:100%"
                      format="YYYY-MM-DD"
                      :placeholder="['开始时间', '结束时间']"
                      @change="onDateChange"
                      @ok="onDateOk"
                  />
                </a-form-item>
              </a-col>
<!--              <a-col :md="6" :sm="24">-->
<!--                <a-form-item label="商品信息" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                  <a-input placeholder="请输入条码、名称、规格、型号、颜色、扩展信息" v-model="queryParam.materialParam"></a-input>-->
<!--                </a-form-item>-->
<!--              </a-col>-->
<!--              <a-col :md="4" :sm="24">-->
<!--                <a-form-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                  <a-range-picker-->
<!--                    style="width:100%"-->
<!--                    v-model="queryParam.createTimeRange"-->
<!--                    format="YYYY-MM-DD"-->
<!--                    :placeholder="['开始时间', '结束时间']"-->
<!--                    @change="onDateChange"-->
<!--                    @ok="onDateOk"-->
<!--                  />-->
<!--                </a-form-item>-->
<!--              </a-col>-->
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
<!--                  <a @click="handleToggleSearch" style="margin-left: 8px">-->
<!--                    {{ toggleSearchStatus ? '收起' : '展开' }}-->
<!--                    <a-icon :type="toggleSearchStatus ? 'up' : 'down'"/>-->
<!--                  </a>-->
                </a-col>
              </span>
<!--              <template v-if="toggleSearchStatus">-->
<!--&lt;!&ndash;                <a-col :md="6" :sm="24">&ndash;&gt;-->
<!--&lt;!&ndash;                  <a-form-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol">&ndash;&gt;-->
<!--&lt;!&ndash;                    <a-select placeholder="选择客户" showSearch optionFilterProp="children" v-model="queryParam.organId">&ndash;&gt;-->
<!--&lt;!&ndash;                      <a-select-option v-for="(item,index) in cusList" :key="index" :value="item.id">&ndash;&gt;-->
<!--&lt;!&ndash;                        {{ item.supplier }}&ndash;&gt;-->
<!--&lt;!&ndash;                      </a-select-option>&ndash;&gt;-->
<!--&lt;!&ndash;                    </a-select>&ndash;&gt;-->
<!--&lt;!&ndash;                  </a-form-item>&ndash;&gt;-->
<!--&lt;!&ndash;                </a-col>&ndash;&gt;-->
<!--                <a-col :md="6" :sm="24">-->
<!--                  <a-form-item label="操作员" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-select placeholder="选择操作员" showSearch optionFilterProp="children" v-model="queryParam.creator">-->
<!--                      <a-select-option v-for="(item,index) in userList" :key="index" :value="item.id">-->
<!--                        {{ item.userName }}-->
<!--                      </a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :md="6" :sm="24">-->
<!--                  <a-form-item label="单据状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-select placeholder="选择单据状态" v-model="queryParam.status">-->
<!--                      <a-select-option value="0">未审核</a-select-option>-->
<!--                      <a-select-option value="1">已审核</a-select-option>-->
<!--                      <a-select-option value="3">部分销售</a-select-option>-->
<!--                      <a-select-option value="2">完成销售</a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :md="6" :sm="24">-->
<!--                  <a-form-item label="单据备注" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-input placeholder="请输入单据备注" v-model="queryParam.remark"></a-input>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--              </template>-->
            </a-row>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator"  style="margin-top: 5px">
<!--          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="myHandleAdd" type="primary" icon="plus">新增</a-button>-->
          <a-dropdown>
            <a-menu slot="overlay">
<!--              <a-menu-item key="1" v-if="btnEnableList.indexOf(1)>-1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>-->
              <a-menu-item key="1" @click="batchSetStatus(0)"><a-icon type="delete"/>退回业务员</a-menu-item>
              <a-menu-item key="2" @click="batchSetStatus(1)"><a-icon type="check"/>审核</a-menu-item>
              <a-menu-item key="3" @click="batchSetStatus(9)"><a-icon type="stop"/>退回仓库</a-menu-item>
            </a-menu>
            <a-button>
              批量操作 <a-icon type="down" />
            </a-button>
          </a-dropdown>
          <a-button type="primary" @click="retailExportExcel()">导出订单明细</a-button>
          <a-button type="primary" @click="commodityExportExcel()">导出货品信息汇总</a-button>
          <a-button type="primary" @click="salesManExportExcel()">导出业务员汇总</a-button>
          <!--          <a-tooltip placement="left" title="销售订单不涉及收款金额，销售订单可以转销售出库单，但需要先对销售订单进行审核。-->
<!--          勾选单据之后可以进行批量操作（删除、审核、反审核）" slot="action">-->
<!--            <a-icon v-if="btnEnableList.indexOf(1)>-1" type="question-circle" style="font-size:20px;float:right;" />-->
<!--          </a-tooltip>-->
        </div>
        <!-- table区域-begin -->
        <div>
          <a-table
            ref="table"
            :key="tableKey"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :components="handleDrag(columns)"
            :scroll="scroll"
            :pagination="false"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a v-if="record.status" @click="myHandleDetail(record, '销售订单', prefixNo)">查看</a>
              <a-divider v-if="record.status!=0 && record.action!='total'" type="vertical" />
              <a v-if="record.status!=0 && record.action!='total'" @click="myHandleEdit1(record)">编辑</a>
<!--              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />-->
<!--&lt;!&ndash;              <a v-if="btnEnableList.indexOf(1)>-1" @click="myHandleCopyAdd(record)">复制</a>&ndash;&gt;-->
<!--              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />-->
<!--              <a-popconfirm title="确定删除吗?" @confirm="() => handleDelete(record)">-->
<!--                <a>删除</a>-->
<!--              </a-popconfirm>-->
            </span>

            <template slot="customRenderStatus" slot-scope="text, record">
              <a-tag v-if="record.status == '0'" color="red">业务员未提交</a-tag>
              <a-tag v-if="record.status == '1'" color="green">财务已审核</a-tag>
              <a-tag v-if="record.status == '2'" color="cyan">完成销售</a-tag>
              <a-tag v-if="record.status == '3'" color="blue">部分销售</a-tag>
              <a-tag v-if="record.status == '4'" color="blue">仓库已提交</a-tag>
              <a-tag v-if="record.status == '9'" color="orange">业务员已提交</a-tag>
            </template>
            <template slot="customRenderPurchaseStatus" slot-scope="logisticsStatus">
              <a-tag v-if="logisticsStatus == '0'" color="red">未发货</a-tag>
<!--              <a-tag v-if="purchaseStatus == '2'" color="cyan">完成采购</a-tag>-->
<!--              <a-tag v-if="purchaseStatus == '3'" color="blue">部分采购</a-tag>-->
              <a-tag v-if="logisticsStatus == '1'" color="cyan">部分发货</a-tag>
              <a-tag v-if="logisticsStatus == '2'" color="orange">全部发货</a-tag>
            </template>
<!--            <template v-if="record.printingReconciliationDate" slot="printingReconciliationDate" slot-scope="text, record">-->
<!--              {{ record.printingReconciliationDate | formatDate }}-->
<!--            </template>-->
<!--            <template v-if="record.stampingReconciliationDate" slot="stampingReconciliationDate" slot-scope="text, record">-->
<!--              {{ record.stampingReconciliationDate | formatDate }}-->
<!--            </template>-->
<!--            <template v-if="record.embroideryReconciliationDate" slot="embroideryReconciliationDate" slot-scope="text, record">-->
<!--              {{ record.embroideryReconciliationDate | formatDate }}-->
<!--            </template>-->
          </a-table>
          <a-row :gutter="24" style="margin-top: 8px;text-align:right;">
            <a-col :md="24" :sm="24">
              <a-pagination @change="paginationChange" @showSizeChange="paginationShowSizeChange"
                            size="small"
                            show-size-changer
                            :showQuickJumper="true"
                            :current="ipagination.current"
                            :page-size="ipagination.pageSize"
                            :page-size-options="ipagination.pageSizeOptions"
                            :total="ipagination.total"
                            :show-total="(total, range) => `共 ${total} 条`">
                <template slot="buildOptionText" slot-scope="props">
                  <span>{{ props.value }}条/页</span>
                </template>
              </a-pagination>
            </a-col>
          </a-row>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <sale-order2-modal ref="modalForm" @ok="modalFormOk" @close="modalFormClose"></sale-order2-modal>
        <bill-detail ref="modalDetail" @ok="modalFormOk" @close="modalFormClose"></bill-detail>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import SaleOrder2Modal from './modules/SaleOrder2Modal'
  import BillDetail from './dialog/BillDetail'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { BillListMixin } from './mixins/BillListMixin'
  import { getCurrentSystemConfig,getPersonByNumType } from '@/api/api'
  import JDate from '@/components/jeecg/JDate'
  import { getAction,postAction,downFile } from '@/api/manage'
  import { filterObj,getNowFormatStr } from '@/utils/util';

  import Vue from 'vue'
  import {BillModalMixin} from "./mixins/BillModalMixin";
  import { dayjs,moment } from "dayjs";
  export default {
    name: "SaleOrder2List",
    mixins:[JeecgListMixin,BillListMixin],
    components: {
      SaleOrder2Modal,
      BillDetail,
      JDate,
      getPersonByNumType
    },
    data () {
      return {
        // 查询条件
        queryParam: {
          number: "",
          materialParam: "",
          salesMan:"",
          type: "销售订单",
          subType: "销售订单",
          roleType: Vue.ls.get('roleType'),
          organId: "",
          depotId: "",
          creator: "",
          status: "",
          remark: "",
          ironingStaff:"",
          printer:""
        },
        ipagination:{
          pageSize: 10,
          pageSizeOptions: ['10', '50', '100','200','1000','2000','5000']
        },
        personList: {
          text: '',
          value: ''
        },
        prefixNo: 'XSDD',
        labelCol: {
          span: 5
        },
        wrapperCol: {
          span: 18,
          offset: 1
        },
        // 添加一个 key 用于强制刷新表格
        tableKey: Date.now(),
        // 表头
        columns: [
          {
            title: '操作',
            dataIndex: 'action',
            align:"center", width: 150,
            scopedSlots: { customRender: 'action' },
            fixed:true,
          },
          { title: '单据编号', dataIndex: 'number', width: 80, ellipsis:true,fixed:true,align: "center"},
          { title: '订单日期', dataIndex: 'operTime',width:100,ellipsis:true,align: "center"},
          { title: '业务员', dataIndex: 'salesMan', width: 70, ellipsis:true,align: "center"},
          { title: '客户', dataIndex: 'customer', width: 100, ellipsis:true,align: "center"},
          { title: '货品信息', dataIndex: 'commodityNo', width: 130, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
            if(text){
              text = record.commodityNo?text+"("+record.commodityName+")":text
            }else{
              text = '合计：'
            }
              return text
            }},
          // { title: '客户', dataIndex: 'customer', width: 100, ellipsis:true,align: "center"},
          // {title: '颜色', dataIndex: 'color', width: 50, ellipsis:true,align: "center"},
          // {title: 'S', dataIndex: 'size1', width: 50, ellipsis:true,align: "center"},
          // {title: 'M', dataIndex: 'size2', width: 50, ellipsis:true,align: "center"},
          // {title: 'L', dataIndex: 'size3', width: 50, ellipsis:true,align: "center"},
          // {title: 'XL', dataIndex: 'size4', width: 50, ellipsis:true,align: "center"},
          // {title: '2XL', dataIndex: 'size5', width: 50, ellipsis:true,align: "center"},
          // {title: '3XL', dataIndex: 'size6', width: 50, ellipsis:true,align: "center"},
          // {title: '4XL', dataIndex: 'size7', width: 50, ellipsis:true,align: "center"},
          // {title: '5XL', dataIndex: 'size8', width: 50, ellipsis:true,align: "center"},
          // {title: '6XL', dataIndex: 'size9', width: 50, ellipsis:true,align: "center"},
          // {title: '7XL', dataIndex: 'size10', width: 50, ellipsis:true,align: "center"},
          // {title: 'XS', dataIndex: 'size11', width: 50, ellipsis:true,align: "center"},
          {title: '数量', dataIndex: 'numbers', width: 90, ellipsis:true,align: "center"},
          {title: '销售总价', dataIndex: 'totalFinancialPrice', width: 100, ellipsis:true,align: "center"},
          {title: '定金', dataIndex: 'financialDeposit', width: 100, ellipsis:true,align: "center"},
          {title: '应收尾款', dataIndex: 'financialBalance', width: 100, ellipsis:true,align: "center"},
          {title: '实收尾款', dataIndex: 'financialStaff', width: 100, ellipsis:true,align: "center"},
          {title: '尾款结清', dataIndex: 'endStaff', width: 100, ellipsis:true,align: "center"},
          {title: '扣款、费用', dataIndex: 'financialDeductionsFee', width: 90, ellipsis:true,align: "center"},
          {title: '印刷总价', dataIndex: 'totalPrintingPrice', width: 80, ellipsis:true,align: "center"},
          {title: '印刷人员', dataIndex: 'printer', width: 80, ellipsis:true,align: "center"},
          {title: '印刷对帐日', dataIndex: 'printingReconciliationDate', width: 100, ellipsis:true,align: "center"},
          {title: '烫画总价', dataIndex: 'hotStampingPaintingTotalPrice', width: 80, ellipsis:true,align: "center"},
          {title: '烫画人员', dataIndex: 'ironingStaff', width: 80, ellipsis:true,align: "center"},
          {title: '叠总价', dataIndex: 'totalStackPrice', width: 70, ellipsis:true,align: "center"},
          {title: '叠人员', dataIndex: 'stackStaff', width: 80, ellipsis:true,align: "center"},
          {title: '烫叠对帐日', dataIndex: 'stampingReconciliationDate', width: 110, ellipsis:true,align: "center"},
          {title: '刺绣总价', dataIndex: 'embroideryTotalPrice', width: 80, ellipsis:true,align: "center"},
          {title: '刺绣人员', dataIndex: 'rustStaff', width: 80, ellipsis:true,align: "center"},
          {title: '刺绣对账日', dataIndex: 'embroideryReconciliationDate', width: 110, ellipsis:true,align: "center"},
          {title: '回扣', dataIndex: 'financialRebate', width: 70, ellipsis:true,align: "center"},
          {title: '其他支出费用', dataIndex: 'otherExpensesInFinance', width: 110, ellipsis:true,align: "center"},
          {title: '其他支出费用2', dataIndex: 'otherExpensesInFinance2', width: 110, ellipsis:true,align: "center"},
          {title: '税额', dataIndex: 'financialTaxAmount', width: 70, ellipsis:true,align: "center"},
          {title: '开票税种', dataIndex: 'billingRate', width: 90, ellipsis:true,align: "center"},
          {title: '开票抬头', dataIndex: 'financial_invoice_payable', width: 120, ellipsis:true,align: "center"},
          {title: '成本金额', dataIndex: 'financialCostAmount', width: 80, ellipsis:true,align: "center"},
          {title: '订单物流费', dataIndex: 'logisticsFreight', width: 90, ellipsis:true,align: "center"},
          {title: '订单提成', dataIndex: 'orderCommission', width: 90, ellipsis:true,align: "center"},
          // {title: '单位', dataIndex: 'unit', width: 50, ellipsis:true,align: "center"},
          // {title: '下单备注', dataIndex: 'orderRemark', width: 90, ellipsis:true,align: "center"},
          // {title: '运费付款方', dataIndex: 'freightPayer', width: 90, ellipsis:true,align: "center"},
          // {title: '地址', dataIndex: 'address', width: 50, ellipsis:true,align: "center"},
          // {title: '电话', dataIndex: 'telephone', width: 50, ellipsis:true,align: "center"},
          // {title: '联系人', dataIndex: 'contactPerson', width: 50, ellipsis:true,align: "center"},
          { title: '订单状态', dataIndex: 'status', width: 120, align: "center",
            scopedSlots: { customRender: 'customRenderStatus' }
          }
          ,{ title: '物流状态', dataIndex: 'logisticsStatus', width: 80, align: "center",
            scopedSlots: { customRender: 'customRenderPurchaseStatus' }
          }
        ],
        url: {
          list: "/depotHead/list",
          delete: "/depotHead/delete",
          deleteNew: "/depotHead/deleteNew",
          deleteBatch: "/depotHead/deleteBatch",
          batchSetStatusUrl: "/depotHead/batchSetStatus",
          getDetailByNumberNew:"/depotHead/getDetailByNumberNew",
          exportXlsUrl: "/depotHead/exportDebtHeadList",
          exportCommodityList: "/depotHead/exportCommodityList",
          exportSalesManList: "/depotHead/exportSalesManList",
        }
      }
    },
    created() {
      this.initSystemConfig()
      this.initCustomer()
      this.initUser()
      this.getSystemConfig()
      this.initSalesman();
    },
    activated() {
      // keep-alive 缓存的组件被激活时执行
      // 强制重新加载数据并刷新表格，确保新字段能正常显示
      console.log('SaleOrder2List activated - force refresh');
      // 更新 tableKey 强制表格重新渲染
      this.tableKey = Date.now();
      this.$nextTick(() => {
        this.loadData();
      });
    },
    deactivated() {
      // 组件失活时清理
      console.log('SaleOrder2List deactivated');
    },
    filters: {
      //处理函数
      formatDate(value) {
        return dayjs(value).format("YYYY-MM-DD");
      },
    },
    mount:{
    },
    computed: {
    },
    methods: {
      focus() {},
      handleDelete: function (record) {
        if(!this.url.deleteNew){
          this.$message.error("请设置url.delete属性!")
          return
        }
        var that = this;
        deleteAction(that.url.deleteNew, {id: record.id}).then((res) => {
          if(res.code === 200){
            that.loadData();
          } else {
            that.$message.warning(res.data.message);
          }
        });
      },
      retailExportExcel() {
        let param ={}
        param=this.queryParam
        if (this.selectedRowKeys.length <= 0) {
          // this.$message.warning('请选择一条记录！');
          downFile(this.url.exportXlsUrl, param).then(res => {
            const url = window.URL.createObjectURL(new Blob([res],{type: 'application/vnd.ms-excel'}));
            const link = document.createElement('a');
            link.href = url;
            // link.download = '销售订单-' + formatDate+ '.xls';
            link.style.display = 'none'
            link.setAttribute('download',  '销售订单_'+getNowFormatStr()+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          })
          return;
        } else {
          var ids = "";
          for (var a = 0; a < this.selectedRowKeys.length; a++) {
            ids += this.selectedRowKeys[a] + ",";
          }
          var that = this;
          this.$confirm({
            title: "确认导出",
            content: "是否导出选中数据?",
            onOk: function () {
              that.loading = true;
              that.$set(param, 'ids',ids)
              downFile(that.url.exportXlsUrl, param).then(res => {
                const url = window.URL.createObjectURL(new Blob([res],{type: 'application/vnd.ms-excel'}));
                const link = document.createElement('a');
                link.href = url;
                // link.download = '销售订单-' + formatDate+ '.xls';
                link.style.display = 'none'
                link.setAttribute('download',  '销售订单_'+getNowFormatStr()+'.xls')
                document.body.appendChild(link)
                link.click()
                document.body.removeChild(link); //下载完成移除元素
                window.URL.revokeObjectURL(url); //释放掉blob对象
              })
              that.loading = false;
            }
          });
        }


      },
      commodityExportExcel() {
        let param ={}
        param=this.queryParam
          // this.$message.warning('请选择一条记录！');
        downFile(this.url.exportCommodityList, param).then(res => {
          const url = window.URL.createObjectURL(new Blob([res],{type: 'application/vnd.ms-excel'}));
            // const url = window.URL.createObjectURL(new Blob([res],{type: 'application/vnd.ms-excel'}));
            const link = document.createElement('a');
            link.href = url;
            // link.download = '销售订单-' + formatDate+ '.xls';
            link.style.display = 'none'
            link.setAttribute('download',  '货品信息汇总_'+getNowFormatStr()+'.xls')
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link); //下载完成移除元素
            window.URL.revokeObjectURL(url); //释放掉blob对象
          })
          return;
      },
      salesManExportExcel() {
        let param ={}
        param=this.queryParam
        // this.$message.warning('请选择一条记录！');
        downFile(this.url.exportSalesManList, param).then(res => {
          const url = window.URL.createObjectURL(new Blob([res],{type: 'application/vnd.ms-excel'}));
          const link = document.createElement('a');
          link.href = url;
          // link.download = '销售订单-' + formatDate+ '.xls';
          link.style.display = 'none'
          link.setAttribute('download',  '业务员汇总_'+getNowFormatStr()+'.xlsx')
          document.body.appendChild(link)
          link.click()
          document.body.removeChild(link); //下载完成移除元素
          window.URL.revokeObjectURL(url); //释放掉blob对象
        })
        return;
      },
      // //零售出库|零售退货入库
      // retailExportExcel() {
      //   let aoa = []
      //   aoa = [['会员卡号：', this.model.organName, '', '单据日期：', this.model.operTime, '', '单据编号：', this.model.number],[]]
      //   let title = ['仓库名称', '条码', '名称', '规格', '型号', '颜色', '扩展信息', '库存', '单位', '序列号', '批号', '有效期', '多属性', '数量', '单价', '金额', '备注']
      //   aoa.push(title)
      //   for (let i = 0; i < this.dataSource.length; i++) {
      //     let ds = this.dataSource[i]
      //     let item = [ds.depotName, ds.barCode, ds.name, ds.standard, ds.model, ds.color, ds.materialOther, ds.stock, ds.unit,
      //       ds.snList, ds.batchNumber, ds.expirationDate, ds.sku, ds.operNumber, ds.unitPrice, ds.allPrice, ds.remark]
      //     aoa.push(item)
      //   }
      //   openDownloadDialog(sheet2blob(aoa), this.billType + '_' + this.model.number)
      // },
      initSalesman() {
        getPersonByNumType({type:1}).then((res)=>{
          if(res) {
            this.personList = res;
          }
        });
      },
      myHandleEdit1(record) {
        if(record.status != '0') {
          this.$refs.modalForm.action = "edit";
          if(this.btnEnableList.indexOf(2)===-1) {
            this.$refs.modalForm.isCanCheck = false
          }
          //查询单条单据信息
          getAction(this.url.getDetailByNumberNew, { number: record.number }).then(res => {
            if (res && res.code == 1) {
              console.log(res.data)
              let item = res.data
              this.handleEdit(item)
            }
          })
        } else {
          this.$message.warning("抱歉，只有业务员提交的单据才能编辑！")
        }
      },
      getSystemConfig() {
        let statusIndex = 0
        for(let i=0; i<this.columns.length; i++){
          if(this.columns[i].dataIndex === 'purchaseStatus') {
            statusIndex = i
          }
        }
      },
      searchQuery() {
        this.loadData(1)
        this.getSystemConfig()
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>
