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
                <a-form-item
                    label="款式"
                >
                  <a-input
                      placeholder="请输入款式编号"
                      v-model="queryParam.commodityNo"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="货品名称"
                >
                  <a-input
                      placeholder="请输入货品名称"
                      v-model="queryParam.commodityName"
                  ></a-input>
                </a-form-item>
              </a-col>
              <a-col :md="4" :sm="24">
                <a-form-item
                    label="年份"
                >
                  <a-input
                      placeholder="请输入年份"
                      v-model="queryParam.years"
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
        <div>
          <a-table
              :columns="defColumns"
              :data-source="dataSource"
          >
          </a-table>
        </div>
      </a-card>
    </a-col>
  </a-row>
</template>
<script>
import {getAction} from "../../api/manage";

export default {
  name: "StyleStatisticsLayout",
  components: {
  },
  data() {
    return {
      dataSource:[],
      // 查询条件
      queryParam: {
        commodityNo: "",
        commodityName: "",
      },
      // pagination:{
      //   pageSizeOptions: ["10", "20", "30", "50"],
      //   current: 1,
      //   pageSize: 10,
      //   total: 5000,
      // },
      // 初始化设置的表头
      settingColumns: [
        "commodityNo",
        "commodityName",
        "color",
        "numbers",
        "amount",
        "depots"
      ],
      // 默认的列
      defColumns: [
        { title: "货品", dataIndex: "commodityName", width: 100 },
        { title: "款式", dataIndex: "commodityNo", width: 100 },
        // { title: "款式信息", dataIndex: "commodityNo", width: 100,ellipsis:true,
        //   customRender:function (text,record,index) {
        //     text = record.commodityNo?text+"("+record.commodityName+")":text
        //     return text
        //   }
        // },
        { title: "年份", dataIndex: "years", width: 100,
          defaultSortOrder: 'descend',
          sorter: (a, b) => a.years - b.years,
        },
        { title: "颜色", dataIndex: "color", width: 100,
        },
        { title: "销售订单数", dataIndex: "depots", width:  100,
          defaultSortOrder: 'descend',
          sorter: (a, b) => a.depots - b.depots,
        },
        { title: "销售件数", dataIndex: "numbers", width: 100,
          defaultSortOrder: 'descend',
          sorter: (a, b) => a.numbers - b.numbers,
        },
        { title: "销售金额", dataIndex: "amount", width: 100,
          defaultSortOrder: 'descend',
          sorter: (a, b) => a.amount - b.amount,
        },
      ],
      url: {
        getStatisticsStyleList: "/depotHead/getStatisticsStyleList"
      },
    };
  },

  filters: {
  },
  created() {
    this.getStatisticsStyleList(this.queryParam);
  },
  computed: {},
  methods: {
    onShowSizeChange(current, pageSize) {
      this.pageSize = pageSize;
    },
    getStatisticsStyleList(params) {
      getAction(this.url.getStatisticsStyleList, params).then((res) => {
        if (res.code == 1) {
          this.dataSource = res.data;
        }
        if (res.code == 0) {
          this.$message.warning(res.msg);
        }
      });
    },
    searchReset() {
      let that = this;
      that.queryParam.commodityNo=''
      that.queryParam.commodityName=''
      that.getStatisticsStyleList(that.queryParam);
    },
    searchQuery(){
      this.getStatisticsStyleList(this.queryParam);
    }
  },
}
</script>
<style scoped>
@import "~@assets/less/common.less";
</style>
