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
              <a-col :md="6" :sm="24">
                <a-form-item label="货品编号" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入货品编号" v-model="queryParam.commodityNo"></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="6" :sm="24">
                <a-form-item label="名称" :labelCol="labelCol" :wrapperCol="wrapperCol">
                  <a-input placeholder="请输入名称" v-model="queryParam.name"></a-input>
                </a-form-item>
              </a-col>
              <span style="float: left;overflow: hidden;" class="table-page-search-submitButtons">
                <a-col :md="6" :sm="24">
                  <a-button type="primary" @click="searchQuery">查询</a-button>
                </a-col>
              </span>
            </a-row>
          </a-form>
        </div>
        <!-- 操作按钮区域 -->
        <div class="table-operator"  style="margin-top: 5px">
          <a-button @click="handleAdd" type="primary" icon="plus">新增</a-button>
          <a-popover trigger="click" placement="right">
            <template slot="content">
              <a-checkbox-group @change="onColChange" v-model="settingColumns" :defaultValue="settingColumns">
                <a-row style="width: 500px">
                  <template v-for="(item,index) in defColumns">
                    <template>
                      <a-col :span="8">
                        <a-checkbox :value="item.dataIndex">
                          <j-ellipsis :value="item.title" :length="10"></j-ellipsis>
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
            :rowSelection="{selectedRowKeys: selectedRowKeys, onChange: onSelectChange, columnWidth:'40px'}"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider v-if="btnEnableList.indexOf(1)>-1" type="vertical" />
              <a-popconfirm v-if="btnEnableList.indexOf(1)>-1" title="确定删除吗?" @confirm="() => handleDelete(record.id)">
                <a>删除</a>
              </a-popconfirm>
            </span>
            <template slot="createTime" slot-scope="text, record">
              {{record.createTime|formatDate}}
            </template>
            <template slot="updateTime" slot-scope="text, record">
              {{record.updateTime|formatDate}}
            </template>
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <commodity-modal ref="modalForm" @ok="modalFormOk"></commodity-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import CommodityModal from './modules/CommodityModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JEllipsis from '@/components/jeecg/JEllipsis'
  import JDate from '@/components/jeecg/JDate'
  import dayjs from 'dayjs'
  import Vue from 'vue'

  export default {
    name: "CommodityList",
    mixins:[JeecgListMixin],
    components: {
      CommodityModal,
      JEllipsis,
      JDate
    },
    data () {
      return {
        categoryTree:[],
        mPropertyListShort: '',
        model: {},
        labelCol: {
          span: 5
        },
        wrapperCol: {
          span: 18,
          offset: 1
        },
        // 查询条件
        queryParam: {
          commodityNo:'',
          name:''
        },
        ipagination:{
          pageSizeOptions: ['10', '20', '30', '50', '100', '200']
        },
        // 实际表头
        columns:[],
        // 初始化设置的表头
        settingColumns:['id','name','commodityNo','remark','price','createBy','createTime','updateBy','updateTime'],
        // 默认的列
        defColumns: [
          {
            title: '操作',
            dataIndex: 'action',
            align:"center",
            width: 100,
            scopedSlots: { customRender: 'action' },
          },
          {title: '序号', dataIndex: 'id', width: 20},
          {title: '名称', dataIndex: 'name', width: 50},
          {title: '货品编号', dataIndex: 'commodityNo', width: 60},
          {title: '备注', dataIndex: 'remark', width: 50},
          {title: '单价(元)', dataIndex: 'price', width: 70, ellipsis:true},
          {title: '录入人员', dataIndex: 'createBy', width: 40, ellipsis:true},
          {title: '录入时间', dataIndex: 'createTime', width: 60, ellipsis:true,scopedSlots: { customRender: 'createTime' }},
          {title: '更新人员', dataIndex: 'updateBy', width: 40, ellipsis:true},
          {title: '更新时间', dataIndex: 'updateTime', width: 60, ellipsis:true,scopedSlots: { customRender: 'updateTime' }}
        ],
        url: {
          list: "/commodity/list",
          delete: "/commodity/delete"
        }
      }
    },

    filters: {
      //处理函数
      formatDate(value) {
        return dayjs(value).format("YYYY-MM-DD")
      }
    },
    created() {
      this.model = Object.assign({}, {});
      this.initColumnsSetting()
      // this.loadTreeData();
    },
    computed: {

    },
    methods: {
      //加载初始化列
      initColumnsSetting(){
        let columnsStr = Vue.ls.get('materialColumns')
        if(columnsStr && columnsStr.indexOf(',')>-1) {
          this.settingColumns = columnsStr.split(',')
        }
        this.columns = this.defColumns.filter(item => {
          if (this.settingColumns.includes(item.dataIndex)) {
            return true
          }
          return false
        })
      },
      //列设置更改事件
      onColChange (checkedValues) {
        this.columns = this.defColumns.filter(item => {
          if (checkedValues.includes(item.dataIndex)) {
            return true
          }
          return false
        })
        let columnsStr = checkedValues.join()
        Vue.ls.set('materialColumns', columnsStr)
      },
      handleEdit: function (record) {
        this.$refs.modalForm.edit(record);
        this.$refs.modalForm.title = "编辑";
        this.$refs.modalForm.disableSubmit = false;
        if(this.btnEnableList.indexOf(1)===-1) {
          this.$refs.modalForm.showOkFlag = false
        }
      },

    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>