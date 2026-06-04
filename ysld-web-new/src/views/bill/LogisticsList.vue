<!-- create j i s h e n g h u a -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="4" :sm="24">
                <a-form-item label="业务员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入业务员" v-model="queryParam.salesMan"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入单据编号" v-model="queryParam.number"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item label="货品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入货品编号" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item label="货品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入货品名称" v-model="queryParam.commodityName"></a-input>
                </a-form-item>
              </a-col>
<!--              <a-col :md="6" :sm="24">-->
<!--                <a-form-item label="商品信息" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                  <a-input placeholder="请输入条码、名称、规格、型号、颜色、扩展信息" v-model="queryParam.materialParam"></a-input>-->
<!--                </a-form-item>-->
<!--              </a-col>-->
<!--              <a-col :md="6" :sm="24">-->
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
<!--                <a-form-item label="单据日期" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                  <a-range-picker-->
<!--                      style="width:100%"-->
<!--                      format="YYYY-MM-DD"-->
<!--                      :placeholder="['开始时间', '结束时间']"-->
<!--                      @change="onDateChange"-->
<!--                      @ok="onDateOk"-->
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
<!--                  <a-form-item label="仓库名称" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-select placeholder="请选择仓库" showSearch optionFilterProp="children" v-model="queryParam.depotId">-->
<!--                      <a-select-option v-for="(depot,index) in depotList" :value="depot.id">-->
<!--                        {{ depot.depotName }}-->
<!--                      </a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
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
<!--                  <a-form-item label="关联订单" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-input placeholder="请输入关联订单" v-model="queryParam.linkNumber"></a-input>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :md="6" :sm="24">-->
<!--                  <a-form-item label="结算账户" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-select placeholder="选择结算账户" showSearch optionFilterProp="children" v-model="queryParam.accountId">-->
<!--                      <a-select-option v-for="(item,index) in accountList" :key="index" :value="item.id">-->
<!--                        {{ item.name }}-->
<!--                      </a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :md="6" :sm="24">-->
<!--                  <a-form-item label="有无欠款" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-select placeholder="选择有无欠款" v-model="queryParam.hasDebt">-->
<!--                      <a-select-option value="1">有欠款</a-select-option>-->
<!--                      <a-select-option value="0">无欠款</a-select-option>-->
<!--                    </a-select>-->
<!--                  </a-form-item>-->
<!--                </a-col>-->
<!--                <a-col :md="6" :sm="24">-->
<!--                  <a-form-item label="单据状态" :labelCol="labelCol" :wrapperCol="wrapperCol">-->
<!--                    <a-select placeholder="选择单据状态" v-model="queryParam.status">-->
<!--                      <a-select-option value="0">未审核</a-select-option>-->
<!--                      <a-select-option value="1">已审核</a-select-option>-->
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
          <a-button @click="myHandleAdd1" type="primary" icon="plus">新增</a-button>
          <a-button type="primary" @click="retailExportExcel()">导出</a-button>
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
<!--              <a @click="myHandleDetail(record, '销售出库', prefixNo)">查看</a>-->
<!--              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />-->
              <a v-if="btnEnableList.indexOf(1)>-1" @click="myHandleEdit1(record)">编辑</a>
<!--              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />-->
<!--              <a v-if="btnEnableList.indexOf(1)>-1" @click="myHandleCopyAdd(record)">复制</a>-->
<!--              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />-->
<!--              <a-popconfirm v-if="btnEnableList.indexOf(1)>-1" title="确定删除吗?" @confirm="() => myHandleDelete(record)">-->
<!--                <a>删除</a>-->
<!--              </a-popconfirm>-->
            </span>
            <template slot="customRenderDebt" slot-scope="value, record">
              <a-tooltip title="有收款单">
                <span style="color:green" v-if="value>0 && record.hasFinancialFlag">{{value}}</span>
              </a-tooltip>
              <a-tooltip title="暂未收款">
                <span style="color:red" v-if="value>0 && !record.hasFinancialFlag">{{value}}</span>
              </a-tooltip>
              <span v-if="value===0">{{value}}</span>
            </template>
            <template slot="customRenderStatus" slot-scope="status">
              <a-tag v-if="status == '0'" color="red">未审核</a-tag>
              <a-tag v-if="status == '1'" color="green">已审核</a-tag>
              <a-tag v-if="status == '9'" color="orange">审核中</a-tag>
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <logistics-out-modal ref="modalForm" @ok="modalFormOk" @close="modalFormClose"></logistics-out-modal>
        <logistics-out1-modal ref="modalForm1" @ok="modalFormOk" @close="modalFormClose"></logistics-out1-modal>
        <bill-detail ref="modalDetail" @ok="modalFormOk" @close="modalFormClose"></bill-detail>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import LogisticsOutModal from './modules/LogisticsOutModal'
  import LogisticsOut1Modal from './modules/LogisticsOut1Modal'
  import BillDetail from './dialog/BillDetail'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { BillListMixin } from './mixins/BillListMixin'
  import JDate from '@/components/jeecg/JDate'
  import { getAction,postAction,downFile } from '@/api/manage'
  import { filterObj,getNowFormatStr } from '@/utils/util';
  import Vue from 'vue'
  export default {
    name: "LogisticsList",
    mixins:[JeecgListMixin,BillListMixin],
    components: {
      LogisticsOutModal,
      LogisticsOut1Modal,
      BillDetail,
      JDate
    },
    data () {
      return {
        // 查询条件
        queryParam: {
          number: "",
          materialParam: "",
          type: "发货",
          // subType: "销售",
          roleType: Vue.ls.get('roleType'),
          organId: "",
          depotId: "",
          creator: "",
          linkNumber: "",
          accountId: "",
          hasDebt: "",
          status: "",
          remark: ""
        },
        prefixNo: 'XSCK',
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
            align:"center", width: 80,
            scopedSlots: { customRender: 'action' },
          },
          // { title: '客户', dataIndex: 'organName',width:120, ellipsis:true,align: "center"},
          { title: '单据编号', dataIndex: 'depotId',width:120,
            customRender:function (text,record,index) {
              text = record.depotId?text+"[物流出]":text
              // text = record.hasBackFlag?text+"[退]":text
              return text
            }
          },
          { title: '商品信息', dataIndex: 'commodityNo',width:120, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.commodityNo?text+"("+record.commodityName+")":text
              // text = record.hasBackFlag?text+"[退]":text
              return text
            }
          },
          { title: '业务员', dataIndex: 'salesMan',width:80},
          { title: '类型', dataIndex: 'type',width:100},
          // {title: '客户', dataIndex: 'customer', width: 100, ellipsis:true,align: "center"},
          {title: '颜色', dataIndex: 'color', width: 80, ellipsis:true,align: "center"},
          {title: 'S', dataIndex: 'size1', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size1==0?'':record.size1
              return text
            }
          },
          {title: 'M', dataIndex: 'size2', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size2==0?'':record.size2
              return text
            }
          },
          {title: 'L', dataIndex: 'size3', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size3==0?'':record.size3
              return text
            }
          },
          {title: 'XL', dataIndex: 'size4', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size4==0?'':record.size4
              return text
            }
          },
          {title: '2XL', dataIndex: 'size5', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size5==0?'':record.size5
              return text
            }
          },
          {title: '3XL', dataIndex: 'size6', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size6==0?'':record.size6
              return text
            }
          },
          {title: '4XL', dataIndex: 'size7', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size7==0?'':record.size7
              return text
            }
          },
          {title: '5XL', dataIndex: 'size8', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size8==0?'':record.size8
              return text
            }
          },
          {title: '6XL', dataIndex: 'size9', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size9==0?'':record.size9
              return text
            }
          },
          {title: '7XL', dataIndex: 'size10', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size10==0?'':record.size10
              return text
            }
          },
          {title: 'XS', dataIndex: 'size11', width: 50, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size11==0?'':record.size11
              return text
            }
          },
          {title: '总数量', dataIndex: 'numbers', width: 60, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.numbers==0?'':record.numbers
              return text
            }
          },
          // {title: '单位', dataIndex: 'unit', width: 50, ellipsis:true,align: "center"},
          // {title: '下单备注', dataIndex: 'orderRemark', width: 100, ellipsis:true,align: "center"},
          // {title: '运费付款方', dataIndex: 'freightPayer', width: 100, ellipsis:true,align: "center"},
          {title: '发货日期', dataIndex: 'deliveryDate', width: 80,ellipsis:true,align: "center"},
          {title: '物流运费', dataIndex: 'logisticsFreight', width: 80,ellipsis:true,align: "center"},
          {title: '物流方式', dataIndex: 'logisticsMode', width: 80,ellipsis:true,align: "center"},
          {title: '实发数量', dataIndex: 'actualQuantity', width: 80,ellipsis:true,align: "center"},
          {title: '发货单号', dataIndex: 'shippingOrderNumber', width: 80,ellipsis:true,align: "center"},
          {title: '备注', dataIndex: 'remark', width: 100,ellipsis:true}
          // { title: '含税合计', dataIndex: 'totalTaxLastMoney',width:80,
          //   customRender:function (text,record,index) {
          //     return (record.discountMoney + record.discountLastMoney).toFixed(2);
          //   }
          // },
          // { title: '待收金额', dataIndex: 'needOutMoney',width:80,
          //   customRender:function (text,record,index) {
          //     let needOutMoney = record.discountLastMoney + record.otherMoney - record.deposit
          //     return needOutMoney? needOutMoney.toFixed(2):0
          //   }
          // },
          // { title: '收款', dataIndex: 'changeAmount',width:60},
          // { title: '欠款', dataIndex: 'debt',width:60,
          //   scopedSlots: { customRender: 'customRenderDebt' }
          // },
          // { title: '状态', dataIndex: 'status', width: 80, align: "center",
          //   scopedSlots: { customRender: 'customRenderStatus' }
          // }
        ],
        url: {
          list: "/warehouseManagement/list",
          delete: "/warehouseManagement/delete",
          deleteBatch: "/warehouseManagement/deleteBatch",
          batchSetStatusUrl: "/warehouseManagement/batchSetStatus",
          getOne: "/warehouseManagement/info",
          exportXlsUrl: "/warehouseManagement/exportWarehouseManagementList",
        }
      }
    },
    computed: {
    },
    created() {
      // this.initSystemConfig()init
      this.initActiveBtnStr()
      this.initCustomer()
      this.getDepotData()
      this.initUser()
      this.initAccount()
    },
    methods: {
      // 导出
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
            link.setAttribute('download',  '订单物流信息_'+ getNowFormatStr()+'.xls')
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
                link.setAttribute('download',  '订单物流信息_'+ getNowFormatStr()+'.xls')
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
      myHandleEdit1(record) {
        if(record.status != '1') {
          this.$refs.modalForm.action = "edit";
          if(this.btnEnableList.indexOf(2)===-1) {
            this.$refs.modalForm.isCanCheck = false
          }
          //查询单条单据信息
          getAction(this.url.getOne, { id:record.id }).then(res => {
            if (res && res.code == 200) {
              console.log(res.data)
              let item = res.data.info
              this.handleEdit(item)
            }
          })
        } else {
          this.$message.warning("抱歉，只有未审核的单据才能编辑！")
        }
      },
      myHandleAdd1() {
        this.$refs.modalForm1.action = "add";
        if(this.btnEnableList.indexOf(2)===-1) {
          this.$refs.modalForm1.isCanCheck = false
        }
        this.$refs.modalForm1.add();
        this.$refs.modalForm1.title = "新增";
        this.$refs.modalForm1.disableSubmit = false;
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>