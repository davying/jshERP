<!-- from 7 5 2 7 1 8 9 2 0 -->
<template>
  <a-row :gutter="24">
    <a-col :md="24">
      <a-card :style="cardStyle" :bordered="false">
        <!-- 查询区域 -->
        <div class="table-page-search-wrapper">
          <!-- 搜索区域 -->
          <a-form layout="inline" @keyup.enter.native="searchQuery">
            <a-row :gutter="24">
              <a-col :md="6" :sm="8">
                <a-form-item label="名称" :labelCol="{span: 5}" :wrapperCol="{span: 18, offset: 1}">
                  <a-input placeholder="请输入名称查询" v-model="queryParam.name"></a-input>
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
          <a-button @click="myHandleAdd" type="primary" icon="plus">新增</a-button>
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
            :pagination="ipagination"
            :loading="loading"
            @change="handleTableChange">
            <span slot="action" slot-scope="text, record">
              <a @click="handleEdit(record)">编辑</a>
              <a-divider type="vertical" />
              <a-popconfirm title="确定删除11吗?" @confirm="() => handleDelete(record)">
                <a>删除</a>
              </a-popconfirm>
            </span>
<!--            &lt;!&ndash; 状态渲染模板 &ndash;&gt;-->
<!--            <template slot="customRenderFlag" slot-scope="enabled">-->
<!--              <a-tag v-if="enabled==1" color="green">启用</a-tag>-->
<!--              <a-tag v-if="enabled==0" color="orange">禁用</a-tag>-->
<!--            </template>-->
          </a-table>
        </div>
        <!-- table区域-end -->
        <!-- 表单区域 -->
        <material-property-modal ref="modalForm" @ok="modalFormOk"></material-property-modal>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
  import MaterialPropertyModal from './modules/MaterialPropertyModal'
  import { JeecgListMixin } from '@/mixins/JeecgListMixin'
  import JDate from '@/components/jeecg/JDate'
  import {getAction} from "../../api/manage";

  export default {
    name: "MaterialPropertyList",
    mixins:[JeecgListMixin],
    components: {
      MaterialPropertyModal,
      JDate
    },
    data () {
      return {
        labelCol: {
          span: 5
        },
        wrapperCol: {
          span: 18,
          offset: 1
        },
        // 查询条件
        queryParam: {name:'',type:''},
        // 表头
        columns: [
          // {
          //   title: '#',
          //   dataIndex: '',
          //   key:'rowIndex',
          //   width:40,
          //   align:"center",
          //   customRender:function (t,r,index) {
          //     return parseInt(index)+1;
          //   }
          // },
          {
            title: '操作',
            dataIndex: 'action',
            width: 200,
            align:"center",
            scopedSlots: { customRender: 'action' },
          },
          {title: '名称', dataIndex: 'nativeName', width: 100},
          // {
          //   title: '是否启用', dataIndex: 'enabled', width: 100, align: "center",
          //   scopedSlots: { customRender: 'customRenderFlag' }
          // },
          // {title: '排序', dataIndex: 'sort', width: 100},
          {title: '别名', dataIndex: 'anotherName', width: 100}
        ],
        url: {
          list: "/materialProperty/list",
          delete: "/materialProperty/deleteById",
          deleteBatch: "/materialProperty/deleteBatch"
        }
      }
    },
    computed: {

    },
    methods: {
      searchQuery() {
        this.loadData(1)
        // this.getSystemConfig()
      },
      myHandleAdd() {
        this.$refs.modalForm.action = "add";
        this.$refs.modalForm.isCanCheck = false
        this.handleAdd();
      },
      handleAdd: function () {
        this.$refs.modalForm.add();
        this.$refs.modalForm.title = "新增";
        this.$refs.modalForm.disableSubmit = false;
      },
      handleDelete: function (record) {
        if(!this.url.delete){
          this.$message.error("请设置url.delete属性!")
          return
        }
        var that = this;
        getAction(that.url.delete, {id: record.id}).then((res) => {
            that.loadData();

        });
      },
    }
  }
</script>
<style scoped>
  @import '~@assets/less/common.less'
</style>