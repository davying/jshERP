<!-- by 7527189 2 0 -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
                  <a-input hidden
                           v-model="queryParam.type"
                  ></a-input>
            <a-row :gutter="24">
              <a-col :md="4" :sm="24">
                <a-form-item
                  label="货品编号"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                >
                  <a-input
                    placeholder="请输入货品编号"
                    v-model="queryParam.commodityNo"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="颜色"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                >
                  <a-input
                      placeholder="请输入颜色"
                      v-model="queryParam.color"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                    label="入库日期"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                >
                  <j-date
                      placeholder="请输入入库日期"
                      v-model="queryParam.inTime"
                      :dateFormat="'YYYY-MM-DD'"
                  />
                </a-form-item>
              </a-col>
              <span
                style="float: left;overflow: hidden;"
                class="table-page-search-submitButtons"
              >
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                  <a-button style="margin-left: 8px" @click="searchReset">重置</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator" style="margin-top: 5px">
<!--          <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>-->
          <a-popover trigger="click" placement="right">
            <template slot="content">
              <a-checkbox-group
                @change="onColChange"
                v-model="settingColumns"
                :defaultValue="settingColumns"
              >
                <a-row style="width: 500px">
                  <template v-for="(item, index) in defColumns">
                    <template>
                      <a-col :span="8">
                        <a-checkbox :value="item.dataIndex">
                          <j-ellipsis
                            :value="item.title"
                            :length="10"
                          ></j-ellipsis>
                        </a-checkbox>
                      </a-col>
                    </template>
                  </template>
                </a-row>
              </a-checkbox-group>
            </template>
            <a-button icon="setting">列设置</a-button>
          </a-popover>
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
            :rowSelection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
              columnWidth: '40px',
            }"
            @change="handleTableChange"
          >
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider v-if="btnEnableList.indexOf(1) > -1" type="vertical" />
              <a-popconfirm
                v-if="btnEnableList.indexOf(1) > -1"
                title="确定删除吗?"
                @confirm="() => handleDelete(record.id)"
              >
                <a>删除</a>
              </a-popconfirm>

<!--              <a-divider v-if="btnEnableList.indexOf(1) > -1" type="vertical" />-->
<!--              <a-popconfirm-->
<!--                title="确定调拨到样品库存吗?"-->
<!--                @confirm="() => handleDelete1(record.id)"-->
<!--              >-->
<!--                <a>调拨</a>-->
<!--              </a-popconfirm>-->
              <!--              <a-divider v-if="btnEnableList.indexOf(1) > -1" type="vertical" />-->
<!--              <a v-if="btnEnableList.indexOf(1)>-1" @click="myHandleCopyAdd(record)">复制</a>-->
            </span>
            <template slot="inTime" slot-scope="text, record">
              {{ record.inTime | formatDate }}
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <warehouse-modal ref="modalForm1" @ok="modalFormOk"></warehouse-modal>
        <warehouse1-modal ref="modalForm" @ok="modalFormOk"></warehouse1-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import WarehouseModal from "./modules/WarehouseModal";
import Warehouse1Modal from "./modules/Warehouse1Modal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import { BillListMixin } from './mixins/BillListMixin';
import JEllipsis from "@/components/jeecg/JEllipsis";
import JDate from "@/components/jeecg/JDate";
import dayjs from "dayjs";
import Vue from "vue";
import {getAction} from "../../api/manage";
import $ from "jquery";
import JEditableTable from "../../components/jeecg/JEditableTable";
// import { cloneDeep } from 'lodash-es';
// import { defineComponent, reactive, ref, UnwrapRef } from 'vue';

export default {
  name: "WarehouseNoPageListOld",
  mixins:[JeecgListMixin,BillListMixin],
  components: {
    JEditableTable,
    WarehouseModal,
    Warehouse1Modal,
    JEllipsis,
    JDate,
  },
  data() {
    return {
      categoryTree: [],
      mPropertyListShort: "",
      model: {},
      labelCol: {
        span: 5,
      },
      wrapperCol: {
        span: 18,
        offset: 1,
      },
      // 查询条件
      queryParam: {
        commodityNo: "",
        commodityName: "",
        type:"入库"
      },
      ipagination: {
        pageSizeOptions: ["10", "20", "30", "50", "100", "200"],
      },
      // 实际表头
      columns: [],
      // 初始化设置的表头
      settingColumns: [
        "type",
        "commodityNo",
        "commodityName",
        "color",
        "size1",
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
        "price"
      ],
      // 默认的列
      defColumns: [
        // {
        //   title: "操作",
        //   dataIndex: "action",
        //   align: "center",
        //   width: 140,
        //   scopedSlots: { customRender: "action" },
        // },
        { title: "类型", dataIndex: "type", width: 50 },
        { title: "货品信息", dataIndex: "commodityNo", width: 100,ellipsis:true,
          customRender:function (text,record,index) {
            text = record.commodityNo?text+"("+record.commodityName+")":text
            return text
          }},
        { title: "颜色", dataIndex: "color", width: 50 },
        { title: "S", dataIndex: "size1", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size1==0?'':record.size1
            return text
          }
        },
        { title: "M", dataIndex: "size2", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size2==0?'':record.size2
            return text
          }
        },
        { title: "L", dataIndex: "size3", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size3==0?'':record.size3
            return text
          }
        },
        { title: "XL", dataIndex: "size4", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size4==0?'':record.size4
            return text
          }
        },
        { title: "2XL", dataIndex: "size5", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size5==0?'':record.size5
            return text
          }
        },
        { title: "3XL", dataIndex: "size6", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size6==0?'':record.size6
            return text
          }
        },
        { title: "4XL", dataIndex: "size7", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size7==0?'':record.size7
            return text
          }
        },
        { title: "5XL", dataIndex: "size8", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size8==0?'':record.size8
            return text
          }
        },
        { title: "6XL", dataIndex: "size9", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size9==0?'':record.size9
            return text
          }
        },
        { title: "7XL", dataIndex: "size10", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size10==0?'':record.size10
            return text
          }
        },
        { title: "XS", dataIndex: "size11", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size11==0?'':record.size11
            return text
          }
        },
        { title: "数量", dataIndex: "numbers", width: 50 ,
          customRender:function (text,record,index) {
            text = record.numbers==0?'':record.numbers
            return text
          }
        },
        {
          title: "到货时间",
          dataIndex: "inTime",
          width: 80,
          ellipsis: true,
          scopedSlots: { customRender: "inTime" },
        },
        {
          title: "单价(元)",
          dataIndex: "price",
          width: 70,
          ellipsis: true,
        },
        { title: "入库备注", dataIndex: "remark", width: 100 , ellipsis:true,align: "center"},
        // {
        //   title: 'operation',
        //   dataIndex: 'operation',
        //   slots: { customRender: 'operation' },
        // },
      ],
      url: {
        list: "/warehouseManagementOld/list",
        delete: "/warehouseManagementOld/delete",
        reSend:"/warehouseManagementOld/sendToSampleById",
      },
    };
  },

  filters: {
    //处理函数
    formatDate(value) {
      return dayjs(value).format("YYYY-MM-DD");
    },
  },
  created() {
    this.model = Object.assign({}, {});
    this.initColumnsSetting();
    // this.loadTreeData();
  },
  computed: {},
  methods: {
    edit(key) {

      editableData[key] = cloneDeep(dataSource.value.filter(item => key === item.key)[0]);
    },
    // // 调拨到样品库存
    // handleDelete1(idstr){
    //   getAction(this.url.reSend, {id:idstr}).then(res=>{
    //     console.log(res);
    //     if(res.code=='1'){
    //       this.$message.success("调拨成功！");
    //       this.loadData();
    //     }
    //   })
    //
    // },
    //加载初始化列
    initColumnsSetting() {
      let columnsStr = Vue.ls.get("warehouseColumns");
      if (columnsStr && columnsStr.indexOf(",") > -1) {
        this.settingColumns = columnsStr.split(",");
      }
      this.columns = this.defColumns.filter((item) => {
        if (this.settingColumns.includes(item.dataIndex)) {
          return true;
        }
        return false;
      });
    },
    //列设置更改事件
    onColChange(checkedValues) {
      this.columns = this.defColumns.filter((item) => {
        if (checkedValues.includes(item.dataIndex)) {
          return true;
        }
        return false;
      });
      let columnsStr = checkedValues.join();
      Vue.ls.set("warehouseColumns", columnsStr);
    },
    handleEdit: function(record) {
      console.log("record: ", record);
      this.$refs.modalForm1.edit(record);
      this.$refs.modalForm1.title = "编辑";
      this.$refs.modalForm1.disableSubmit = false;
      console.log("this.btnEnableList: ", this.btnEnableList);
      if (this.btnEnableList.indexOf(1) === -1) {
        this.$refs.modalForm1.showOkFlag = false;
      }
    },
  },
}
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
