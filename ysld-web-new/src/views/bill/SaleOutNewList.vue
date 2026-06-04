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
              <a-col :md="5" :sm="24">
                <a-form-item label="单据编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入单据编号" v-model="queryParam.number"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="货品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入货品编号" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="货品名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入货品名称" v-model="queryParam.commodityName"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="5" :sm="24">
                <a-form-item label="颜色" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入颜色" v-model="queryParam.color"></a-input>
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
          <a-button v-if="btnEnableList.indexOf(1)>-1" @click="myHandleAdd1" type="primary" icon="plus">新增</a-button>
          <a-tooltip placement="left" title="销售出库单可以由销售订单转过来，也可以单独创建。
          销售出库单据中的仓库列表只显示当前用户有权限的仓库。销售出库单可以使用多账户收款。
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
<!--              <a @click="myHandleDetail(record, '销售出库', prefixNo)">查看</a>-->
<!--              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />-->
                  <a v-if="text!='total' && btnEnableList.indexOf(1) > -1" @click="handleEdit1(record)">编辑</a>

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
        <sale-out-new-modal ref="modalForm" @ok="modalFormOk" @close="modalFormClose"></sale-out-new-modal>
        <sale-out-new1-modal ref="modalForm1" @ok="modalFormOk" @close="modalFormClose"></sale-out-new1-modal>
        <bill-detail ref="modalDetail" @ok="modalFormOk" @close="modalFormClose"></bill-detail>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import SaleOutNewModal from './modules/SaleOutNewModal'
  import SaleOutNew1Modal from './modules/SaleOutNew1Modal'
  import BillDetail from './dialog/BillDetail'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import { BillListMixin } from './mixins/BillListMixin'
  import JDate from '@/components/jeecg/JDate'
  import { getAction,postAction,downFile } from '@/api/manage'
  import Vue from 'vue'
  export default {
    name: "SaleOutNewList",
    mixins:[JeecgListMixin,BillListMixin],
    components: {
      SaleOutNewModal,
      SaleOutNew1Modal,
      BillDetail,
      JDate
    },
    data () {
      return {
        // 查询条件
        queryParam: {
          number: "",
          materialParam: "",
          type: "出库",
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
            align:"center", width: 180,
            scopedSlots: { customRender: 'action' },
          },
          // { title: '客户', dataIndex: 'organName',width:120, ellipsis:true,align: "center"},
          { title: '单据编号', dataIndex: 'depotId',width:160,
            customRender:function (text,record,index) {
              text = record.depotId?text+"[出]":text
              // text = record.hasBackFlag?text+"[退]":text
              return text
            }
          },
          { title: '商品信息', dataIndex: 'commodityNo',width:150, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.commodityNo?text+"("+record.commodityName+")":text
              // text = record.hasBackFlag?text+"[退]":text
              return text
            }
          },
          { title: '类型', dataIndex: 'type',width:80},
          {title: '客户', dataIndex: 'customer', width: 100, ellipsis:true,align: "center"},
          {title: '颜色', dataIndex: 'color', width: 50, ellipsis:true,align: "center"},
          {title: 'S', dataIndex: 'size1', width: 60, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size1==0?'':record.size1
              return text
            }
          },
          {title: 'M', dataIndex: 'size2', width: 60, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size2==0?'':record.size2
              return text
            }
          },
          {title: 'L', dataIndex: 'size3', width: 60, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size3==0?'':record.size3
              return text
            }
          },
          {title: 'XL', dataIndex: 'size4', width: 70, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size4==0?'':record.size4
              return text
            }
          },
          {title: '2XL', dataIndex: 'size5', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size5==0?'':record.size5
              return text
            }
          },
          {title: '3XL', dataIndex: 'size6', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size6==0?'':record.size6
              return text
            }
          },
          {title: '4XL', dataIndex: 'size7', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size7==0?'':record.size7
              return text
            }
          },
          {title: '5XL', dataIndex: 'size8', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size8==0?'':record.size8
              return text
            }
          },
          {title: '6XL', dataIndex: 'size9', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size9==0?'':record.size9
              return text
            }
          },
          {title: '7XL', dataIndex: 'size10', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size10==0?'':record.size10
              return text
            }
          },
          {title: 'XS', dataIndex: 'size11', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.size11==0?'':record.size11
              return text
            }
          },
          {title: '总数量', dataIndex: 'numbers', width: 80, ellipsis:true,
            customRender:function (text,record,index) {
              text = record.numbers==0?'':record.numbers
              return text
            }
          },
          {title: '出库备注', dataIndex: 'remark', width: 100, ellipsis:true,align: "center"},
        ],
        url: {
          list: "/warehouseManagement/list",
          delete: "/warehouseManagement/delete",
          deleteBatch: "/warehouseManagement/deleteBatch",
          batchSetStatusUrl: "/warehouseManagement/batchSetStatus",
          getOne: "/warehouseManagement/info",
        }
      }
    },
    computed: {
    },
    created() {
      // this.initSystemConfig()
      this.initCustomer()
      this.getDepotData()
      this.initUser()
      this.initAccount()
    },
    methods: {
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