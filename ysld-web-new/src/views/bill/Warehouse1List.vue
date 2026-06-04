<!-- by 7527189 2 0 -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery1">
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
<!--              <a-col :md="6" :sm="24">-->
<!--                <a-form-item-->
<!--                  label="货品名称"-->
<!--                  :labelCol="labelCol"-->
<!--                  :wrapperCol="wrapperCol"-->
<!--                >-->
<!--                  <a-input-->
<!--                    placeholder="请输入货品名称"-->
<!--                    v-model="queryParam.commodityName"-->
<!--                  ></a-input>-->
<!--                </a-form-item>-->
<!--              </a-col>-->
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
<!--          <a-button @click="handleAdd" type="primary" icon="plus"-->
<!--            >新增</a-button-->
<!--          >-->
<!--          <a-popover trigger="click" placement="right">-->
<!--            <template slot="content">-->
<!--              <a-checkbox-group-->
<!--                @change="onColChange"-->
<!--                v-model="settingColumns"-->
<!--                :defaultValue="settingColumns"-->
<!--              >-->
<!--                <a-row style="width: 500px">-->
<!--                  <template v-for="(item, index) in defColumns">-->
<!--                    <template>-->
<!--                      <a-col :span="8">-->
<!--                        <a-checkbox :value="item.dataIndex">-->
<!--                          <j-ellipsis-->
<!--                            :value="item.title"-->
<!--                            :length="10"-->
<!--                          ></j-ellipsis>-->
<!--                        </a-checkbox>-->
<!--                      </a-col>-->
<!--                    </template>-->
<!--                  </template>-->
<!--                </a-row>-->
<!--              </a-checkbox-group>-->
<!--            </template>-->
<!--            <a-button icon="setting">列设置</a-button>-->
<!--          </a-popover>-->
          <a-button type="primary" @click="exportWarehouseInOut()">导出</a-button>
        </div>
        <!-- table区域-begin -->
        <div>
          <!--          :pagination="ipagination"  注释这个，pagination变成false 多合计-->
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
            :rowSelection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
              columnWidth: '40px',
            }"
            @change="handleTableChange"
          >
            <span v-if="btnEnableList.indexOf(7) > -1" slot="action" slot-scope="text, record">
              <a v-if="text!='total'" @click="myHandleAdd1(record)">调拨</a>

              <!--              <a @click="handleEdit(record)">编辑</a>-->
<!--              <a-divider v-if="btnEnableList.indexOf(1) > -1" type="vertical" />-->
<!--              <a-popconfirm-->
<!--                v-if="btnEnableList.indexOf(1) > -1"-->
<!--                title="确定删除吗?"-->
<!--                @confirm="() => handleDelete(record.id)"-->
<!--              >-->
<!--                <a>删除</a>-->
<!--              </a-popconfirm>-->

<!--              <a-divider v-if="btnEnableList.indexOf(1) > -1" type="vertical" />-->
<!--              <a-popconfirm-->
<!--                title="确定调拨到样品库存吗?"-->
<!--                @confirm="() => handleDelete1(record.commodityNo,record.color)"-->
<!--              >-->
<!--                <a>调拨</a>-->
<!--              </a-popconfirm>-->
            </span>
            <template slot="inTime" slot-scope="text, record">
              {{ record.inTime | formatDate }}
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
        <warehouse-modal ref="modalForm" @ok="modalFormOk"></warehouse-modal>
        <warehouse-db-modal ref="modalForm1" @ok="modalFormOk"></warehouse-db-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import WarehouseModal from "./modules/WarehouseModal";
import WarehouseDbModal from "./modules/WarehouseDbModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JEllipsis from "@/components/jeecg/JEllipsis";
import JDate from "@/components/jeecg/JDate";
import dayjs from "dayjs";
import Vue from "vue";
import {getAction, postAction,downFile} from "../../api/manage";
import { filterObj,getNowFormatStr } from '@/utils/util';

export default {
  name: "Warehouse1List",
  mixins: [JeecgListMixin],
  components: {
    WarehouseModal,
    WarehouseDbModal,
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
        // type:"入库"
      },
      ipagination: {
        pageSizeOptions: ['10', '50', '100','200','1000','2000','5000']
      },
      // 实际表头
      columns: [],
      // 初始化设置的表头
      settingColumns: [
          "action",
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
      ],
      // 默认的列
      defColumns: [
        {
          title: "操作",
          dataIndex: "action",
          align: "center",
          width: 100,
          scopedSlots: { customRender: "action" },
        },
        { title: "货品信息", dataIndex: "commodityNo", width: 100,ellipsis:true,
          customRender:function (text,record,index) {
            if(text){
              text = record.commodityNo?text+"("+record.commodityName+")":text
            }else{
              text = '合计：'
            }
            return text
          }
        },
        { title: "颜色", dataIndex: "color", width: 50 },
        { title: "S", dataIndex: "size1", width: 50,
          customRender:function (text,record,index) {
            text = record.size1==0?'':record.size1
            return text
          }
        },
        { title: "M", dataIndex: "size2", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size2==0?'':record.size2
            return text
          }},
        { title: "L", dataIndex: "size3", width: 50,
          customRender:function (text,record,index) {
            text = record.size3==0?'':record.size3
            return text
          } },
        { title: "XL", dataIndex: "size4", width: 50,
          customRender:function (text,record,index) {
            text = record.size4==0?'':record.size4
            return text
          } },
        { title: "2XL", dataIndex: "size5", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size5==0?'':record.size5
            return text
          }},
        { title: "3XL", dataIndex: "size6", width: 50,
          customRender:function (text,record,index) {
            text = record.size6==0?'':record.size6
            return text
          } },
        { title: "4XL", dataIndex: "size7", width: 50,
          customRender:function (text,record,index) {
            text = record.size7==0?'':record.size7
            return text
          } },
        { title: "5XL", dataIndex: "size8", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size8==0?'':record.size8
            return text
          }},
        { title: "6XL", dataIndex: "size9", width: 50,
          customRender:function (text,record,index) {
            text = record.size9==0?'':record.size9
            return text
          } },
        { title: "7XL", dataIndex: "size10", width: 50,
          customRender:function (text,record,index) {
            text = record.size10==0?'':record.size10
            return text
          } },
        { title: "XS", dataIndex: "size11", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size11==0?'':record.size11
            return text
          }},
        { title: "数量", dataIndex: "numbers", width: 50,
          customRender:function (text,record,index) {
            text = record.numbers==0?'':record.numbers
            return text
          } }
        // ,{
        //   title: "单价(元)",
        //   dataIndex: "price",
        //   width: 70,
        //   ellipsis: true,
        // }
      ],
      url: {
        add: "/warehouseManagement/addDb",
        list: "/warehouseManagement/getInOutFinalList",
        delete: "/warehouseManagement/delete",
        reSend:"/warehouseManagement/sendToSampleById",
        reSendAll:"/warehouseManagement/sendToSampleByCommodityNo",
        exportWarehouseInOut:"/warehouseManagement/exportWarehouseInOut",
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
    this.searchQuery1();
    // this.loadTreeData();
  },
  computed: {},
  methods: {
    myHandleAdd1(record) {
      this.$refs.modalForm1.action = "add";
      if(this.btnEnableList.indexOf(2)===-1) {
        this.$refs.modalForm1.isCanCheck = false
      }
      this.$refs.modalForm1.add(record);
      this.$refs.modalForm1.title = "新增";
      this.$refs.modalForm1.disableSubmit = false;
    },
    searchQuery1() {
      this.loadData(1);
    },
    loadData(arg) {
      if(!this.url.list){
        this.$message.error("请设置url.list属性!")
        return
      }
      //加载数据 若传入参数1则加载第一页的内容
      if (arg == 1) {
        this.ipagination.current = 1;
      }
      var params = this.getQueryParams();//查询条件
      this.loading = true;
      getAction(this.url.list, params).then((res) => {
        if (res.code==1) {
          this.dataSource = res.data.records;
          this.ipagination.total = res.data.total;
          this.tableAddTotalRow(this.columns, this.dataSource)
        }
        if(res.code===510){
          this.$message.warning(res.data)
        }
        this.loading = false;
      })
    },

    // 调拨到样品库存
    handleDelete1(idstr,colorStr){
      if(!colorStr){
        this.$message.error("颜色为空不能调拨！");
        return
      }
      getAction(this.url.reSendAll, {commodityNo:idstr,color:colorStr}).then(res=>{
        console.log(res);
        if(res.code=='1'){
          this.$message.success("调拨成功！");
          this.loadData();
        }
      })

    },
    //加载初始化列
    initColumnsSetting() {
      let columnsStr = Vue.ls.get("warehouse1Columns");
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
      Vue.ls.set("warehouse1Columns", columnsStr);
    },
    handleEdit: function(record) {
      console.log("record: ", record);
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
      console.log("this.btnEnableList: ", this.btnEnableList);
      if (this.btnEnableList.indexOf(1) === -1) {
        this.$refs.modalForm.showOkFlag = false;
      }
    },
    exportWarehouseInOut() {
      let param ={}
      param=this.queryParam
      // this.$message.warning('请选择一条记录！');
      downFile(this.url.exportWarehouseInOut, param).then(res => {
        const url = window.URL.createObjectURL(new Blob([res],{type: 'application/vnd.ms-excel'}));
        const link = document.createElement('a');
        link.href = url;
        link.style.display = 'none'
        link.setAttribute('download',  '库存盘点_'+getNowFormatStr()+'.xlsx')
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link); //下载完成移除元素
        window.URL.revokeObjectURL(url); //释放掉blob对象
      })
      return;
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
