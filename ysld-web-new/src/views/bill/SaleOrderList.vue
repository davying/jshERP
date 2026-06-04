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
              <a-col :md="6" :sm="24">
                <a-form-item label="客户名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入客户名称" v-model="queryParam.customer"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="货品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="货品编码" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-range-picker
                    style="width:100%"
                    format="YYYY-MM-DD"
                    :placeholder="['开始时间', '结束时间']"
                    @change="onDateChange"
                    @ok="onDateOk"
                  />
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator"  style="margin-top: 5px">
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="myHandleAdd" type="primary" icon="plus">新增</a-button>
          <a-button type="primary" @click="retailExportExcel()">导出订单明细</a-button>
          <a-dropdown>
            <a-menu slot="overlay">
              <a-menu-item key="1" v-if="btnEnableList.indexOf(1)>-1" @click="batchDel"><a-icon type="delete"/>删除</a-menu-item>
              <a-menu-item key="2" v-if="checkFlag && btnEnableList.indexOf(2)>-1" @click="batchSetStatus(9)"><a-icon type="check"/>提交</a-menu-item>
            </a-menu>
            <a-button>
              批量操作 <a-icon type="down" />
            </a-button>
          </a-dropdown>
          <a-tooltip placement="left" title="销售订单不涉及收款金额，销售订单可以转销售出库单，但需要先对销售订单进行审核。
          勾选单据之后可以进行批量操作（删除、审核、反审核）" slot="action">
            <a-icon v-if="btnEnableList.indexOf(1)>-1" type="question-circle" style="font-size:20px;float:right;" />
          </a-tooltip>
        </div>
        <!-- table区域-begin -->
        <div>
          <a-table
            ref="table"
            size="middle"
            bordered
            rowKey="id"
            :columns="columns"
            :dataSource="dataSource"
            :components="handleDrag(columns)"
            :pagination="false"
            :scroll="scroll"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <!-- <a v-if="record.status" @click="salesConfirmed(record)">业务员确认</a>
              <a-divider type="vertical" /> -->
              <a v-if="record.status" @click="myHandleDetail(record, '销售订单', prefixNo)">查看</a>
              <a-divider v-if="record.status==0" type="vertical" />
              <a v-if="record.status==0" @click="myHandleEdit(record)">编辑</a>
              <a-divider v-if="record.status==0" type="vertical" />
              <a-popconfirm v-if="record.status==0&&!record.number" title="确定删除吗?" @confirm="() => myHandleDelete(record)">
                <a>删除</a>
              </a-popconfirm>
            </span>
            <template slot="customRenderStatus" slot-scope="status, record">
              <div>
                <a-tag v-if="status == '0'" color="red">暂存</a-tag>
                <a-tag v-if="status == '1'" color="green">财务已审核</a-tag>
                <a-tag v-if="status == '2'" color="cyan">完成销售</a-tag>
                <a-tag v-if="status == '3'" color="blue">部分销售</a-tag>
                <a-tag v-if="status == '4'" color="cyan">仓库已提交</a-tag>
                <a-tag v-if="status == '9'" color="orange">业务员已提交</a-tag>
                <a-tag v-if="record.salesConfirmed == '1'" color="purple">业务员已确认</a-tag>
                <a-tag v-if="record.salesConfirmed == '0'" color="gray">业务员未确认</a-tag>
              </div>
            </template>
            <template slot="customRenderPurchaseStatus" slot-scope="logisticsStatus">
              <a-tag v-if="logisticsStatus == '0'" color="red">未发货</a-tag>
              <a-tag v-if="logisticsStatus == '1'" color="cyan">部分发货</a-tag>
              <a-tag v-if="logisticsStatus == '2'" color="blue">全部发货</a-tag>
            </template>
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
        <sale-order-modal ref="modalForm" @ok="modalFormOk" @close="modalFormClose"></sale-order-modal>
        <bill-detail ref="modalDetail" @ok="modalFormOk" @close="modalFormClose"></bill-detail>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import SaleOrderModal from './modules/SaleOrderModal'
  import BillDetail from './dialog/BillDetail'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { BillListMixin } from './mixins/BillListMixin'
  import {getAction,downFile } from '@/api/manage'
  import JDate from '@/components/jeecg/JDate'
  import { getNowFormatStr } from '@/utils/util';

  export default {
    name: "SaleOrderList",
    mixins:[JeecgListMixin,BillListMixin],
    components: {
      SaleOrderModal,
      BillDetail,
      JDate
    },
    data () {
      return {
        // 查询条件
        createTimeRange:'',
        queryParam: {
          customer:'',
          number: "",
          materialParam: "",
          type: "销售订单",
          subType: "销售订单",
          roleType: "个人数据",
          organId: "",
          depotId: "",
          creator: "",
          status: "",
          remark: ""
        },
        prefixNo: 'XSDD',
        labelCol: {
          span: 5
        },
        wrapperCol: {
          span: 18,
          offset: 1
        },
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
          { title: '业务员', dataIndex: 'salesMan', width: 90, ellipsis:true, fixed:true,align: "center"},
          { title: '货品信息', dataIndex: 'commodityNo', width: 130, fixed:true, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              if(text){
                text = record.commodityNo?text+"("+record.commodityName+")":text
              }else{
                text = '合计：'
              }
              return text
            }},
          { title: '客户', dataIndex: 'customer', width: 100, fixed:true, ellipsis:true,align: "center"},
          {title: '颜色', dataIndex: 'color', width: 50, ellipsis:true,align: "center"},
          {title: 'S', dataIndex: 'size1', width: 60, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size1==0?'':record.size1
              return text
            }},
          {title: 'M', dataIndex: 'size2', width: 60, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size2==0?'':record.size2
              return text
            }
          },
          {title: 'L', dataIndex: 'size3', width: 60, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size3==0?'':record.size3
              return text
            }
          },
          {title: 'XL', dataIndex: 'size4', width: 70, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size4==0?'':record.size4
              return text
            }
          },
          {title: '2XL', dataIndex: 'size5', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size5==0?'':record.size5
              return text
            }
          },
          {title: '3XL', dataIndex: 'size6', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size6==0?'':record.size6
              return text
            }
          },
          {title: '4XL', dataIndex: 'size7', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size7==0?'':record.size7
              return text
            }
          },
          {title: '5XL', dataIndex: 'size8', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size8==0?'':record.size8
              return text
            }
          },
          {title: '6XL', dataIndex: 'size9', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size9==0?'':record.size9
              return text
            }
          },
          {title: '7XL', dataIndex: 'size10', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size10==0?'':record.size10
              return text
            }
          },
          {title: 'XS', dataIndex: 'size11', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.size11==0?'':record.size11
              return text
            }
          },
          {title: '总数', dataIndex: 'numbers', width: 80, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.numbers==0?'':record.numbers
              return text
            }
          },
          {title: '单位', dataIndex: 'unit', width: 50, ellipsis:true,align: "center"},
          {title: '下单备注', dataIndex: 'orderRemark', width: 100, ellipsis:true,align: "center"},
          {title: '运费付款方', dataIndex: 'freightPayer', width: 100, ellipsis:true,align: "center"},
          {title: '销售单价', dataIndex: 'financialUnitPrice', width: 100, ellipsis:true,align: "center"},
          {title: '成本单价', dataIndex: 'financialCostUnitPrice', width: 100, ellipsis:true,align: "center"},
          {title: '销售总价', dataIndex: 'totalFinancialPrice', width: 100, ellipsis:true,align: "center"},
          {title: '订单物流费', dataIndex: 'logisticsFreight', width: 90, ellipsis:true,align: "center"},
          {title: '地址', dataIndex: 'address', width: 100, ellipsis:true,align: "center"},
          {title: '电话', dataIndex: 'telephone', width: 120,ellipsis:true,align: "center"},
          {title: '联系人', dataIndex: 'contactPerson', width: 60,ellipsis:true,align: "center"},
          {title: '是否换拉链袋', dataIndex: 'isChangeBag', width: 120,ellipsis:true,align: "center"},
          {title: '印刷总价', dataIndex: 'totalPrintingPrice', width: 80, ellipsis:true,align: "center"},
          {title: '印刷人员', dataIndex: 'printer', width: 80, ellipsis:true,align: "center"},
          {title: '印刷对帐日', dataIndex: 'printingReconciliationDate', width: 110, ellipsis:true,align: "center"},
          {title: '烫画总价', dataIndex: 'hotStampingPaintingTotalPrice', width: 80, ellipsis:true,align: "center"},
          {title: '烫画人员', dataIndex: 'ironingStaff', width: 80, ellipsis:true,align: "center"},
          {title: '叠总价', dataIndex: 'totalStackPrice', width: 70, ellipsis:true,align: "center"},
          {title: '叠人员', dataIndex: 'stackStaff', width: 80, ellipsis:true,align: "center"},
          {title: '烫叠对帐日', dataIndex: 'stampingReconciliationDate', width: 110, ellipsis:true,align: "center"},
          {title: '刺绣总价', dataIndex: 'embroideryTotalPrice', width: 80, ellipsis:true,align: "center"},
          {title: '刺绣人员', dataIndex: 'rustStaff', width: 80, ellipsis:true,align: "center"},
          {title: '刺绣对账日', dataIndex: 'embroideryReconciliationDate', width: 110, ellipsis:true,align: "center"},
          {title: '定金', dataIndex: 'financialDeposit', width: 80, ellipsis:true,align: "center"},
          {title: '应收尾款', dataIndex: 'financialBalance', width: 80, ellipsis:true,align: "center"},
          {title: '实收尾款', dataIndex: 'financialStaff', width: 80, ellipsis:true,align: "center"},
          {title: '尾款是否结清', dataIndex: 'endStaff', width: 120, ellipsis:true,align: "center"},
          {title: '扣款、费用', dataIndex: 'financialDeductionsFee', width: 90, ellipsis:true,align: "center"},
          {title: '回扣', dataIndex: 'financialRebate', width: 70, ellipsis:true,align: "center"},
          {title: '其他支出费用', dataIndex: 'otherExpensesInFinance', width: 110, ellipsis:true,align: "center"},
          {title: '税额', dataIndex: 'financialTaxAmount', width: 70, ellipsis:true,align: "center"},
          {title: '开票税种', dataIndex: 'billingRate', width: 90, ellipsis:true,align: "center"},
          {title: '开票抬头', dataIndex: 'financial_invoice_payable', width: 120, ellipsis:true,align: "center"},
          {title: '成本金额', dataIndex: 'financialCostAmount', width: 80, ellipsis:true,align: "center"},
          {title: '订单提成', dataIndex: 'orderCommission', width: 90, ellipsis:true,align: "center"},
          { title: '订单状态', dataIndex: 'status', width: 100, align: "center",
            scopedSlots: { customRender: 'customRenderStatus' }
          },
          { title: '物流状态', dataIndex: 'logisticsStatus', width: 80, align: "center",
            scopedSlots: { customRender: 'customRenderPurchaseStatus' }
          }
        ],
        url: {
          list: "/depotHead/list",
          delete: "/depotHead/delete",
          deleteBatch: "/depotHead/deleteBatch",
          batchSetStatusUrl: "/depotHead/batchSetStatus",
          getDetailByNumberNew:"/depotHead/getDetailByNumberNew",
          exportXlsUrl: "/depotHead/exportDebtHeadList",
          updSalesConfirmed: "/depotHead/updSalesConfirmed",
        }
      }
    },
    created() {
      this.initSystemConfig()
      this.initCustomer()
      this.initUser()
      // this.getSystemConfig()
    },
    computed: {
    },
    methods: {
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
            link.setAttribute('download',  '业务员销售订单_'+getNowFormatStr()+'.xls')
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

      salesConfirmed(record){

      },

      myHandleEdit1(record) {
        if(record.status == '0') {
          this.$refs.modalForm.action = "edit";
          // if(this.btnEnableList.indexOf(2)===-1) {
          //   this.$refs.modalForm.isCanCheck = false
          // }
          //查询单条单据信息
          getAction(this.url.getDetailByNumberNew, { number: record.number }).then(res => {
            if (res && res.code == 1) {
              console.log(res.data)
              let item = res.data
              this.handleEdit(item)
            }
          })
        } else {
          this.$message.warning("抱歉，只有未审核的单据才能编辑！")
        }
      },
      searchQuery() {
        this.loadData(1)
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>