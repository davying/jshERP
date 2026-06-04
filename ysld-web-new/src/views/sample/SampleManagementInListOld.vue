<!-- by 7527189 2 0 -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery1">
            <a-row :gutter="24">
              <a-col :md="6" :sm="24">
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
              <a-col :md="6" :sm="24">
                <a-form-item
                  label="样衣单价"
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                >
                  <a-input
                    placeholder="请输入样衣单价"
                    v-model="queryParam.price"
                  ></a-input>
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
<!--          <a-button @click="handleAdd" type="primary" icon="plus"-->
<!--            >新增</a-button-->
<!--          >-->
          <a-button type="primary" @click="retailExportExcel()">导出</a-button>
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
<!--                @confirm="() => handleDelete(record.id)"-->
<!--              >-->
<!--                <a>删除</a>-->
<!--              </a-popconfirm>-->
            </span>
            <template slot="inTime" slot-scope="text, record">
              {{ record.inTime | formatDate }}
            </template>
<!--            <template slot="createTime" slot-scope="text, record">-->
<!--              {{ record.createTime | formatDate }}-->
<!--            </template>-->
<!--            <template slot="updateTime" slot-scope="text, record">-->
<!--              {{ record.updateTime | formatDate }}-->
<!--            </template>-->
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <sampleManagement-modal
          ref="modalForm"
          @ok="modalFormOk"
        ></sampleManagement-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import SampleManagementModal from "./modules/SampleManagementModal";
import { JeecgListMixin } from "@/mixins/JeecgListMixin";
import JEllipsis from "@/components/jeecg/JEllipsis";
import JDate from "@/components/jeecg/JDate";
import dayjs from "dayjs";
import {getAction, postAction,downFile} from "../../api/manage";
import Vue from "vue";
import { filterObj,getNowFormatStr } from '@/utils/util';

export default {
  name: "SampleManagementInListOld",
  mixins: [JeecgListMixin],
  components: {
    SampleManagementModal,
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
        color: "",
        price: "",
      },
      ipagination: {
        pageSizeOptions: ["10", "20", "30", "50", "100", "200"],
      },
      // // 实际表头
      // columns: [],
      // 初始化设置的表头
      settingColumns: [
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
      columns: [
        // {
        //   title: "操作",
        //   dataIndex: "action",
        //   align: "center",
        //   width: 30,
        //   scopedSlots: { customRender: "action" }
        // },
        { title: "样衣编号", dataIndex: "id", width: 35,align: "center" },
        { title: "货品信息", dataIndex: "commodityNo", width: 60,align: "center",
          customRender:function (text,record,index) {
            text = record.commodityNo?text+"("+record.commodityName+")":text
            return text
          }},
        { title: "颜色", dataIndex: "color", width: 80 },
        { title: "S", dataIndex: "size1", width:  25,
          customRender:function (text,record,index) {
            text = record.size1==0?'':record.size1
            return text
          } },
        { title: "M", dataIndex: "size2", width: 25,
          customRender:function (text,record,index) {
            text = record.size2==0?'':record.size2
            return text
          } },
        { title: "L", dataIndex: "size3", width: 25,
          customRender:function (text,record,index) {
            text = record.size3==0?'':record.size3
            return text
          } },
        { title: "XL", dataIndex: "size4", width: 25 ,
          customRender:function (text,record,index) {
            text = record.size4==0?'':record.size4
            return text
          }},
        { title: "2XL", dataIndex: "size5", width: 25 ,
          customRender:function (text,record,index) {
            text = record.size5==0?'':record.size5
            return text
          }},
        { title: "3XL", dataIndex: "size6", width: 25,
          customRender:function (text,record,index) {
            text = record.size6==0?'':record.size6
            return text
          } },
        { title: "4XL", dataIndex: "size7", width: 25 ,
          customRender:function (text,record,index) {
            text = record.size7==0?'':record.size7
            return text
          }},
        { title: "5XL", dataIndex: "size8", width: 25,
          customRender:function (text,record,index) {
            text = record.size8==0?'':record.size8
            return text
          } },
        { title: "6XL", dataIndex: "size9", width: 25,
          customRender:function (text,record,index) {
            text = record.size9==0?'':record.size9
            return text
          } },
        { title: "7XL", dataIndex: "size10", width: 25,
          customRender:function (text,record,index) {
            text = record.size10==0?'':record.size10
            return text
          } },
        { title: "XS", dataIndex: "size11", width: 25,
          customRender:function (text,record,index) {
            text = record.size11==0?'':record.size11
            return text
          } },
        { title: "数量", dataIndex: "numbers", width: 25,
          customRender:function (text,record,index) {
            text = record.numbers==0?'':record.numbers
            return text
          } },
        {
          title: "到货时间",
          dataIndex: "inTime",
          width: 50,
          ellipsis: true,
          scopedSlots: { customRender: "inTime" },
        },
        {
          title: "样衣单价(元)",
          dataIndex: "price",
          width: 50,
          ellipsis: true,
        },
        {
          title: "备注",
          dataIndex: "remark",
          width: 100,
          ellipsis: true,
        }
      ],
      url: {
        list: "/sampleManagementOld/list",
        delete: "/sampleManagementOld/delete",
        exportXlsUrl: "/sampleManagementOld/exportSampleList",
      },
    };
  },

  filters: {
    //处理函数
    formatDate(value) {
      if(value){
        return dayjs(value).format("YYYY-MM-DD");
      }else{
        return '';
      }

    },
  },
  created() {
    this.model = Object.assign({}, {});
    // this.initColumnsSetting();
    // this.loadTreeData();
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
          link.setAttribute('download',  '样衣入库记录_'+getNowFormatStr()+'.xls')
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
              link.setAttribute('download',  '样衣入库记录_'+getNowFormatStr()+'.xls')
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
        if (res.code==200) {
          this.dataSource = res.data.rows;
          this.ipagination.total = res.data.total;
          this.tableAddTotalRow(this.columns, this.dataSource)
        }
        if(res.code===510){
          this.$message.warning(res.data)
        }
        this.loading = false;
      })
    },

  },
};
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
