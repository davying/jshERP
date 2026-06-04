<!-- by 7527189 2 0 -->
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
                <a-form-item label="货品编码" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="货品编码" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">

                <a-form-item label="业务员" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <!--                  <a-input placeholder="请输入业务员姓名" v-model="queryParam.salesMan"></a-input>-->
                  <a-select placeholder="请选择业务员" v-model="queryParam.salesMan">
                    <a-select-option v-for="(person,index) in personList" :value="person.text" :key="index">
                      {{ person.text }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="领样日期"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                >
<!--                  <j-date placeholder="请输入领样日期"-->
<!--                          v-model="queryParam.createTime"-->
<!--                          :dateFormat="'YYYY-MM-DD'"-->
<!--                  />-->
                  <a-range-picker
                      style="width:100%"
                      format="YYYY-MM-DD"
                      :placeholder="['开始时间', '结束时间']"
                      @change="onDateChange"
                      @ok="onDateOk"
                  />
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="单价"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                >
                <a-input
                  placeholder="请输入单价"
                  v-model="queryParam.price"
                ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="金额"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                >
                  <a-input
                      placeholder="请输入金额"
                      v-model="queryParam.totalPrice"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="样品编号"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                >
                  <a-input
                      placeholder="请输入样品编号"
                      v-model="queryParam.sampleId"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="样衣单编号"
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                >
                  <a-input
                      placeholder="请输入样衣单编号"
                      v-model="queryParam.sampleNumber"
                  ></a-input>
                </a-form-item>
              </a-col>
<!--              <a-col :md="6" :sm="24">-->
<!--                <a-form-item-->
<!--                  label="颜色"-->
<!--                  :labelCol="labelCol"-->
<!--                  :wrapperCol="wrapperCol"-->
<!--                >-->
<!--                  <a-input-->
<!--                    placeholder="请输入颜色"-->
<!--                    v-model="queryParam.color"-->
<!--                  ></a-input>-->
<!--                </a-form-item>-->
<!--              </a-col>-->
<!--              <a-col :md="6" :sm="24">-->
<!--                <a-form-item-->
<!--                  label="样衣单价"-->
<!--                  :labelCol="labelCol"-->
<!--                  :wrapperCol="wrapperCol"-->
<!--                >-->
<!--                  <a-input-->
<!--                    placeholder="请输入样衣单价"-->
<!--                    v-model="queryParam.price"-->
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
            :rowSelection="{
              selectedRowKeys: selectedRowKeys,
              onChange: onSelectChange,
              columnWidth: '40px',
            }"
            @change="handleTableChange"
          >
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
<!--              <a-divider v-if="btnEnableList.indexOf(1) > -1" type="vertical" />-->
<!--              <a-popconfirm-->
<!--                v-if="btnEnableList.indexOf(1) > -1"-->
<!--                title="确定删除吗?"-->
<!--                @confirm="() => handleDelete1(record.id)"-->
<!--              >-->
<!--                <a>删除</a>-->
<!--              </a-popconfirm>-->
            </span>
            <template slot="createTime" slot-scope="text, record">
              {{ record.createTime | formatDate }}
            </template>
<!--            <template slot="updateTime" slot-scope="text, record">-->
<!--              {{ record.updateTime | formatDate }}-->
<!--            </template>-->
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <sampleManagementLog-modal
          ref="modalForm"
          @ok="modalFormOk"
        ></sampleManagementLog-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import SampleManagementLogModal from "./modules/SampleManagementLogModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JEllipsis from "@/components/jeecg/JEllipsis";
import JDate from "@/components/jeecg/JDate";
import { BillListMixin } from '../bill/mixins/BillListMixin'
import dayjs from "dayjs";
import Vue from "vue";
import { getCurrentSystemConfig,getPersonByNumType } from '@/api/api'
import {getAction,postAction,downFile} from "../../api/manage";
import { filterObj,getNowFormatStr } from '@/utils/util';

export default {
  name: "SampleManagementLogListOld",
  mixins: [JeecgListMixin,BillListMixin],
  components: {
    SampleManagementLogModal,
    JEllipsis,
    JDate,
    getPersonByNumType
  },
  data() {
    return {
      categoryTree: [],
      mPropertyListShort: "",
      model: {},
      personList: {
        text: '',
        value: ''
      },
      searchOptions: {
        value: "",
        options: [],
      },
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
        salesMan:"",
        createTime:'',
        color: "",
        price: "",
      },
      ipagination: {
        pageSizeOptions: ["10", "20", "30", "50", "100", "200"],
      },
      // 初始化设置的表头
      settingColumns: [
        "id",
        "commodityNo",
          "sampleId",
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
        "price",
          "remark",
          "sampleNumber"
      ],
      // 默认的列
      columns: [
        // {
        //   title: "操作",
        //   dataIndex: "action",
        //   align: "center",
        //   width: 100,
        //   scopedSlots: { customRender: "action" },
        // },
        { title: "货品信息", dataIndex: "commodityNo", width: 120,
          customRender:function (text,record,index) {
            text = record.commodityNo?text+"("+record.commodityName+")":text
            return text
          }},
        { title: "样品编号", dataIndex: "sampleId", width: 80},
        { title: "样衣单编号", dataIndex: "sampleNumber", width: 120},
        { title: "S", dataIndex: "size1", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size1==0?'':record.size1
            return text
          }},
        { title: "M", dataIndex: "size2", width: 50,
          customRender:function (text,record,index) {
            text = record.size2==0?'':record.size2
            return text
          } },
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
        { title: "2XL", dataIndex: "size5", width: 50,
          customRender:function (text,record,index) {
            text = record.size5==0?'':record.size5
            return text
          } },
        { title: "3XL", dataIndex: "size6", width: 50,
          customRender:function (text,record,index) {
            text = record.size6==0?'':record.size6
            return text
          } },
        { title: "4XL", dataIndex: "size7", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size7==0?'':record.size7
            return text
          }},
        { title: "5XL", dataIndex: "size8", width: 50,
          customRender:function (text,record,index) {
            text = record.size8==0?'':record.size8
            return text
          } },
        { title: "6XL", dataIndex: "size9", width: 50 ,
          customRender:function (text,record,index) {
            text = record.size9==0?'':record.size9
            return text
          }},
        { title: "7XL", dataIndex: "size10", width: 50,
          customRender:function (text,record,index) {
            text = record.size10==0?'':record.size10
            return text
          } },
        { title: "XS", dataIndex: "size11", width: 50,
          customRender:function (text,record,index) {
            text = record.size11==0?'':record.size11
            return text
          } },
        { title: "数量", dataIndex: "numbers", width: 50,
          customRender:function (text,record,index) {
            text = record.numbers==0?'':record.numbers
            return text
          } },
        { title: "单价", dataIndex: "price", width: 50 },
        { title: "金额", dataIndex: "totalPrice", width: 50 },
        { title: "已收样衣费", dataIndex: "sampleCharge", width: 100 },
        { title: "业务员", dataIndex: "member", width: 70 },
        { title: "领样日期", dataIndex: "createTime", width: 100 ,ellipsis:true, scopedSlots: { customRender: "createTime" } },
        { title: "快递费(元)", dataIndex: "expressPrice", width: 80 },
        { title: "备注", dataIndex: "remark", width: 100 }
      ],
      url: {
        list: "/sampleManagementLogOld/list",
        delete: "/sampleManagementLogOld/delete",
        exportXlsUrl: "/sampleManagementLogOld/exportSampleLogList",
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
    // this.loadTreeData();
    this.initSalesman();

  },
  computed: {},
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
          link.setAttribute('download',  '样衣出库记录_'+getNowFormatStr()+'.xls')
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
              link.setAttribute('download',  '样衣出库记录_'+getNowFormatStr()+'.xls')
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
    handleDelete1(ids){
      getAction(this.url.delete,{'id':ids}).then(res=>{
        if(res.code == 1){
          this.$message.success("删除成功！");
          this.searchQuery()
        }
      })
    },
    initSalesman() {
      getPersonByNumType({type:1}).then((res)=>{
        if(res) {
          this.personList = res;
        }
      });
    },
    handleEdit: function(record) {
      this.$refs.modalForm.edit(record);
      this.$refs.modalForm.title = "编辑";
      this.$refs.modalForm.disableSubmit = false;
      if (this.btnEnableList.indexOf(1) === -1) {
        this.$refs.modalForm.showOkFlag = false;
      }
    },
  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
