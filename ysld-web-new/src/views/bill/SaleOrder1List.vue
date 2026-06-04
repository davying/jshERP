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
              <a-col :md="5" :sm="24">
                <a-form-item label="业务员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select v-model="queryParam.salesMan" allowClear="true" showSearch>
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
                <a-form-item label="订单状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                      ref="select"
                      v-model="queryParam.status"
                      @focus="focus"
                      allowClear="true"
                  >
                    <a-select-option value="9">业务员已提交</a-select-option>
                    <a-select-option value="4">仓库已提交</a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="货品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="货品编码" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="颜色" v-model="queryParam.color"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="客户" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入客户" v-model="queryParam.customer"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="物流状态" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-select
                      ref="select"
                      v-model="queryParam.logisticsStatus"
                      @focus="focus"
                      allowClear="true"
                  >
                    <a-select-option value="0">未发货</a-select-option>
                    <a-select-option value="1">部分发货</a-select-option>
                    <a-select-option value="2">全部发货</a-select-option>
                  </a-select>
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
          <a-dropdown v-if="btnEnableList.indexOf(1)>-1">
            <a-menu slot="overlay">
              <a-menu-item key="2"  @click="batchSetStatus(4)"><a-icon type="check"/>审核</a-menu-item>
              <a-menu-item key="3"  @click="batchSetStatus(0)"><a-icon type="stop"/>退回业务员</a-menu-item>
            </a-menu>
            <a-button>
              批量操作 <a-icon type="down" />
            </a-button>
          </a-dropdown>
          <a-button type="primary" @click="retailExportExcel()">导出</a-button>
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
            :pagination="ipagination"
            :scroll="scroll"
            :loading="loading"
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="myHandleDetail(record, '销售订单', prefixNo)">查看</a>
              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />
              <a v-if="record.status!=0 && btnEnableList.indexOf(1)>-1" @click="myHandleEdit1(record)">编辑</a>
            </span>
            <template slot="customRenderStatus" slot-scope="status">
              <a-tag v-if="status == '0'" color="red">未审核</a-tag>
              <a-tag v-if="status == '1'" color="green">财务已审核</a-tag>
              <a-tag v-if="status == '2'" color="cyan">完成销售</a-tag>
              <a-tag v-if="status == '3'" color="blue">部分销售</a-tag>
              <a-tag v-if="status == '4'" color="blue">仓库已提交</a-tag>
              <a-tag v-if="status == '9'" color="orange">业务员已提交</a-tag>
            </template>
            <template slot="customRenderPurchaseStatus" slot-scope="logisticsStatus">
              <a-tag v-if="logisticsStatus == '0'" color="red">未发货</a-tag>
              <a-tag v-if="logisticsStatus == '1'" color="cyan">部分发货</a-tag>
              <a-tag v-if="logisticsStatus == '2'" color="blue">全部发货</a-tag>
            </template>
            <template v-if="record.operTime" slot="operTime" slot-scope="text, record">
              {{ record.operTime | formatDate }}
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <sale-order1-modal ref="modalForm" @ok="modalFormOk" @close="modalFormClose"></sale-order1-modal>
        <bill-detail ref="modalDetail" @ok="modalFormOk" @close="modalFormClose"></bill-detail>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import SaleOrder1Modal from './modules/SaleOrder1Modal'
  import BillDetail from './dialog/BillDetail'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { BillListMixin } from './mixins/BillListMixin'
  import JDate from '@/components/jeecg/JDate'
  import { getAction,postAction,downFile } from '@/api/manage'
  import { getCurrentSystemConfig,getPersonByNumType } from '@/api/api'
  import Vue from 'vue'
  import dayjs from "dayjs";
  export default {
    name: "SaleOrder1List",
    mixins:[JeecgListMixin,BillListMixin],
    components: {
      SaleOrder1Modal,
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
          type: "销售订单",
          subType: "销售订单",
          roleType: Vue.ls.get('roleType'),
          organId: "",
          depotId: "",
          creator: "",
          status: "",
          remark: ""
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
          { title: '业务员', dataIndex: 'salesMan', width: 100, ellipsis:true,align: "center"},
          { title: '货品编码', dataIndex: 'commodityNo', width: 150, ellipsis:true,align: "center",
            customRender:function (text,record,index) {
              text = record.commodityNo?text+"("+record.commodityName+")":text
              return text
            }},
          { title: '客户', dataIndex: 'customer', width: 100, ellipsis:true,align: "center"},
          {title: '颜色', dataIndex: 'color', width: 50, ellipsis:true,align: "center"},
          {title: 'S', dataIndex: 'size1', width: 60, ellipsis:true,align: "center"},
          {title: 'M', dataIndex: 'size2', width: 60, ellipsis:true,align: "center"},
          {title: 'L', dataIndex: 'size3', width: 60, ellipsis:true,align: "center"},
          {title: 'XL', dataIndex: 'size4', width: 70, ellipsis:true,align: "center"},
          {title: '2XL', dataIndex: 'size5', width: 80, ellipsis:true,align: "center"},
          {title: '3XL', dataIndex: 'size6', width: 80, ellipsis:true,align: "center"},
          {title: '4XL', dataIndex: 'size7', width: 80, ellipsis:true,align: "center"},
          {title: '5XL', dataIndex: 'size8', width: 80, ellipsis:true,align: "center"},
          {title: '6XL', dataIndex: 'size9', width: 80, ellipsis:true,align: "center"},
          {title: '7XL', dataIndex: 'size10', width: 80, ellipsis:true,align: "center"},
          {title: 'XS', dataIndex: 'size11', width: 80, ellipsis:true,align: "center"},
          {title: '总数量', dataIndex: 'numbers', width: 80, ellipsis:true,align: "center"},
          {title: '单位', dataIndex: 'unit', width: 50, ellipsis:true,align: "center"},
          {title: '下单备注', dataIndex: 'orderRemark', width: 100, ellipsis:true,align: "center"},
          {title: '下单日期', dataIndex: 'operTime', width: 100, ellipsis:true,align: "center", scopedSlots: { customRender: "operTime" }},
          {title: '运费付款方', dataIndex: 'freightPayer', width: 100, ellipsis:true,align: "center"},
          {title: 'LOGO制作', dataIndex: 'logoMark', width: 100, ellipsis:true,align: "center"},
          {title: '是否换拉链袋', dataIndex: 'isChangeBag', width: 120, ellipsis:true,align: "center"},
          { title: '订单状态', dataIndex: 'status', width: 120, align: "center",
            scopedSlots: { customRender: 'customRenderStatus' }
          },
          { title: '物流状态', dataIndex: 'logisticsStatus', width: 70, align: "center",
            scopedSlots: { customRender: 'customRenderPurchaseStatus' }
          }
        ],
        url: {
          list: "/depotHead/list",
          delete: "/depotHead/delete",
          deleteBatch: "/depotHead/deleteBatch",
          batchSetStatusUrl: "/depotHead/batchSetStatus",
          getDetailByNumberNew:"/depotHead/getDetailByNumberNew",
          exportXlsUrl: "/depotHead/exportDebtHeadListByWare",
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
    filters: {
      //处理函数
      formatDate(value) {
        return dayjs(value).format("YYYY-MM-DD");
      },
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
            link.setAttribute('download',  '销售订单_2023.xls')
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
                link.setAttribute('download',  '销售订单_2023.xls')
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
      initSalesman() {
        getPersonByNumType({type:1}).then((res)=>{
          if(res) {
            this.personList = res;
          }
        });
      },
      myHandleEdit1(record) {
        console.log(record)
        // if(record.status == '9') {
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
        // } else {
        //   this.$message.warning("抱歉，只有未审核的单据才能编辑！")
        // }
      },
      getSystemConfig() {
        let statusIndex = 0
        for(let i=0; i<this.columns.length; i++){
          if(this.columns[i].dataIndex === 'purchaseStatus') {
            statusIndex = i
          }
        }
        getCurrentSystemConfig().then((res) => {
          if(res.code === 200 && res.data){
            let purchaseBySaleFlag = res.data.purchaseBySaleFlag
            if(purchaseBySaleFlag === "0") {
              if(statusIndex>0) {
                //移除采购进度列
                this.columns.splice(statusIndex, 1)
              }
            } else {
              if(statusIndex===0) {
                let purchaseStatusObj = { title: '采购进度', dataIndex: 'purchaseStatus', width: 70, align: "center",
                  scopedSlots: { customRender: 'customRenderPurchaseStatus' }
                }
                //添加采购进度列
                this.columns.splice(statusIndex-1, 0, purchaseStatusObj)
              }
            }
          } else {
            if(statusIndex>0) {
              //移除采购进度列
              this.columns.splice(statusIndex, 1)
            }
          }
        })
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