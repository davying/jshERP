<template>
  <div class="page-header-index-wide">
    <a-row :gutter="24" v-if="isNeedDeptName">
      <a-col :md="12" :sm="24">
        <a-form-item
            :labelCol="labelCol"
            :wrapperCol="wrapperCol"
            label="销售部门"
        >
          <a-select
              @focus="focus"
              v-model:value="deptName"
              @change="handleDeptChange"
              style="width: 300px">
            <a-select-option value="全部">全部</a-select-option>
            <a-select-option value="销售一部">销售一部(英诗澜得)</a-select-option>
            <a-select-option value="销售二部">销售二部(岭泽)</a-select-option>
            <a-select-option value="销售三部">销售三部(雪狼)</a-select-option>
            <a-select-option value="财务仓储">财务仓储</a-select-option>
          </a-select>
        </a-form-item>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计销售总额(元)" data-step="2" data-title="今年累计销售总额(元)" data-intro="统计今年销售单据的总金额">
          <a-tooltip title="统计今年销售单据的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearFinancialPrice"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计印刷总额(元)" data-step="3" data-title="今年累计印刷总额(元)" data-intro="统计今年零售单据印刷的总金额">
          <a-tooltip title="统计今日零售单据印刷的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearPrintingPrice"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计烫画总额(元)" data-step="1" data-title="今年累计烫画总额(元)" data-intro="统计今年采购单据烫画的总金额">
          <a-tooltip title="统计今年采购单据烫画的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearHotStampingPaintingPrice"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计叠总额(元)">
          <a-tooltip title="统计今年销售单据叠的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearStackPrice"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计刺绣总额(元)">
          <a-tooltip placement="left" title="统计今年销售单据刺绣的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearEmbroideryPrice"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计扣款、费用总额(元)">
          <a-tooltip title="统计今年采购单据扣款、费用的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearFinancialDeductionsFee"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计回扣总额(元)">
          <a-tooltip title="统计今年销售单据回扣的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearFinancialRebate"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计成本总额(元)">
          <a-tooltip title="统计今年销售单据成本的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearFinancialCostAmount"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计其他支出费用总额(元)">
          <a-tooltip title="统计今年采购单据其他支出费用的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearOtherExpensesInFinance"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计税额(元)">
          <a-tooltip title="统计今年销售单据税的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearFinancialTaxAmount"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计订单物流费(元)">
          <a-tooltip placement="left" title="统计今年零售单据订单物流费的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearLogisticsFreight"></head-info>
        </chart-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="4" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <chart-card :loading="loading" title="今年累计样品物流费(元)">
          <a-tooltip title="统计今年采购单据样品物流费的总金额" slot="action">
            <a-icon type="info-circle-o" />
          </a-tooltip>
          <head-info :content="statistics.yearExpressPrice"></head-info>
        </chart-card>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <a-card :loading="loading" :bordered="false" :body-style="{paddingRight: '5'}" data-step="5" data-title="销售统计"
                data-intro="统计往前6个月每月销售的总金额">
          <bar title="销售统计(元)" :height="barHeight" :yaxisText="yaxisText" :dataSource="salePriceData"/>
        </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <a-card :loading="loading" :bordered="false" :body-style="{paddingRight: '5'}" data-step="6" data-title="订单物流费统计"
                data-intro="统计往前6个月每月物流费的总金额">
          <bar title="订单物流费统计(元)" :height="barHeight" :yaxisText="yaxisText" :dataSource="retailPriceData"/>
        </a-card>
      </a-col>
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <a-card :loading="loading" :bordered="false" :body-style="{paddingRight: '5'}" data-step="4" data-title="样品物流费统计"
                data-intro="统计往前6个月每月样品物流费的总金额">
          <bar title="样品物流费统计(元)" :height="barHeight" :yaxisText="yaxisText" :dataSource="buyPriceData"/>
        </a-card>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">

<!--            <div id="echarts" style="width: 800px;height: 800px"></div>-->
<!--        <div id="reservation" style="width: 640px; height: 350px"></div>-->
        <div id="initTest" style="width: 800px;height: 800px"></div>

      </a-col>
    </a-row>

    <a-row :gutter="24">
      <a-col :sm="24" :md="24" :xl="24" :style="{ paddingRight: '0px',marginBottom: '6px' }">
        <a-card :bordered="false" :body-style="{padding: '5'}" data-step="7" data-title="服务和版权"
                data-intro="展示服务到期时间（快到期时会出现续费链接，请注意及时续费）、
          用户数量（是指最多可以录入的用户数量）、版权信息">
          <div class="hidden-xs" style="float:right;">
            <a-popover
                trigger="hover"
                :visible="hovered"
                @visibleChange="handleHoverChange">
              <div slot="content">
                <img src="/static/weixin.jpg" style="width:258px" />
              </div>
              <a-button type="link" v-if="showWeixinSpan()">英诗澜得ERP微信小程序</a-button>
            </a-popover>
            &copy; 2015-2030 {{systemTitle}} V1.0
          </div>
          <a-tag v-if="tenant.type==0" color="blue">试用到期：{{tenant.expireTime}}</a-tag>
          <a-tag v-if="tenant.type==0" color="blue">试用用户：{{tenant.userCurrentNum}}/{{tenant.userNumLimit}}</a-tag>
          <a-tag v-if="tenant.type==1" color="blue">服务到期：{{tenant.expireTime}}</a-tag>
          <a-tag v-if="tenant.type==1" color="blue">授权用户：{{tenant.userCurrentNum}}/{{tenant.userNumLimit}}</a-tag>
          <a v-if="hasExpire" style="color: red;" :href="payFeeUrl" target="_blank">立即续费</a>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>
<script>
import ChartCard from '@/components/ChartCard'
import ACol from "ant-design-vue/es/grid/Col"
import ATooltip from "ant-design-vue/es/tooltip/Tooltip"
import MiniArea from '@/components/chart/MiniArea'
import MiniBar from '@/components/chart/MiniBar'
import MiniProgress from '@/components/chart/MiniProgress'
import Bar from '@/components/chart/Bar'
import LineChartMultid from '@/components/chart/LineChartMultid'
import HeadInfo from '@/components/tools/HeadInfo.vue'
import Trend from '@/components/Trend'
import { getBuyAndSaleStatistics, buyOrSalePrice, getPlatformConfigByKey } from '@/api/api'
import { handleIntroJs } from "@/utils/util"
import { getAction,postAction } from '../../api/manage'
import Vue from 'vue'
import echarts from "echarts"

export default {
  name: "IndexChart",
  components: {
    ATooltip,
    ACol,
    ChartCard,
    MiniArea,
    MiniBar,
    MiniProgress,
    Bar,
    Trend,
    LineChartMultid,
    HeadInfo
  },
  data() {
    return {
      deptName: '',
      isNeedDeptName:false,
      roleName: Vue.ls.get('roleName'),
      hovered: false,
      systemTitle: window.SYS_TITLE,
      systemUrl: window.SYS_URL,
      loading: true,
      center: null,
      statistics: {},
      barHeight: document.documentElement.clientHeight-585,
      yaxisText: '金额',
      buyPriceData: [],
      salePriceData: [],
      retailPriceData: [],
      visitFields:['ip','visit'],
      visitInfo:[],
      hasExpire: false,
      payFeeUrl: '',
      tenant: {
        type: '',
        expireTime: '',
        userCurrentNum: '',
        userNumLimit: '',
        tenantId: ''
      },
      chart: null,
      option:null,
    }
  },
  created() {
    if(this.roleName == '财务'||this.roleName == '超级管理员'){
      this.deptName='全部'
      this.isNeedDeptName =true
    }
    setTimeout(() => {
      this.loading = !this.loading
    }, 1000)
    this.initInfo()
    this.initWithTenant()
  },
  mounted() {
    handleIntroJs('indexChart', 1)
    this.initTest();
  },
  methods: {
    // 用户数据展示
    initTest() {
      var myChart = echarts.init(document.getElementById("initTest"));
      var option = {
        title : {
          text: '某地区蒸发量和降水量',
          subtext: '纯属虚构'
        },
        tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['蒸发量','降水量']
        },
        // toolbox: {
        //   show : true,
        //   feature : {
        //     mark : {show: true},
        //     dataView : {show: true, readOnly: false},
        //     magicType : {show: true, type: ['line', 'bar']},
        //     restore : {show: true},
        //     saveAsImage : {show: true}
        //   }
        // },
        calculable : true,
        xAxis : [
          {
            type : 'category',
            data : ['1月','2月','3月','4月','5月','6月','7月','8月','9月','10月','11月','12月']
          }
        ],
        yAxis : [
          {
            type : 'value'
          }
        ],
        series : [
          {
            name:'蒸发量',
            type:'bar',
            data:[2.0, 4.9, 7.0, 23.2, 25.6, 76.7, 135.6, 162.2, 32.6, 20.0, 6.4, 3.3],
            markPoint : {
              data : [
                {type : 'max', name: '最大值'},
                {type : 'min', name: '最小值'}
              ]
            },
            markLine : {
              data : [
                {type : 'average', name: '平均值'}
              ]
            }
          },
          {
            name:'降水量',
            type:'bar',
            data:[2.6, 5.9, 9.0, 26.4, 28.7, 70.7, 175.6, 182.2, 48.7, 18.8, 6.0, 2.3],
            markPoint : {
              data : [
                {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
                {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
              ]
            },
            markLine : {
              data : [
                {type : 'average', name : '平均值'}
              ]
            }
          }
        ]
      };

      myChart.dispose();
      //白色模式
      myChart = echarts.init(document.getElementById("initTest"));
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },

    // 用户数据展示
    initReservation() {
  var myChart = echarts.init(document.getElementById("reservation"));
  var option = {
    xAxis: {
      type: "category",
      data: [
        "星期一",
        "星期二",
        "星期三",
        "星期四",
        "星期五",
        "星期六",
        "星期日",
      ],
    },
    yAxis: {
      type: "value",
    },
    series: [
      {
        data: [120, 200, 150, 80, 70, 110, 130],
        type: "bar",
      },
    ],
  };

    myChart.dispose();
    //白色模式
    myChart = echarts.init(document.getElementById("reservation"));
    myChart.setOption(option);
    //随着屏幕大小调节图表
    window.addEventListener("resize", () => {
      myChart.resize();
    });
},
//其他全部数据展示
     initThreeData() {
  var myChart = echarts.init(document.getElementById("threeData"));
  var option = {
    title: {
      text: "其他内容",
      subtext: "实时数据",
      left: "center",
    },
    tooltip: {
      trigger: "item",
    },
    legend: {
      orient: "vertical",
      left: "left",
    },
    series: [
      {
        name: "数据来源",
        type: "pie",
        radius: "50%",
        data: [
          { value: 1048, name: "部门" },
          { value: 735, name: "公告" },
          { value: 580, name: "通知" },
          { value: 484, name: "签到" },
          { value: 300, name: "会议室" },
        ],
        emphasis: {
          itemStyle: {
            shadowBlur: 10,
            shadowOffsetX: 0,
            shadowColor: "rgba(0, 0, 0, 0.5)",
          },
        },
      },
    ],
  };
    myChart.dispose();
    myChart = echarts.init(document.getElementById("threeData"));
    myChart.setOption(option);
    //随着屏幕大小调节图表
    window.addEventListener("resize", () => {
      myChart.resize();
    });
},
    // getEchartData() {
    //   const chart = this.$refs.chart
    //   if (chart) {
    //     const myChart = this.$echarts.init(chart)
    //     const option = { legend: {},
    //       tooltip: {},
    //       dataset: {
    //         source: [
    //           ['订单', 43.3, 85.8],
    //           ['订单1', 83.1, 73.4],
    //           ['订单2', 86.4, 65.2],
    //           ['订单3', 72.4, 53.9],
    //           ['订单4', 82.4, 53.9],
    //           ['订单5', 42.4, 53.9],
    //           ['订单6', 72.4, 53.9],
    //           ['订单7', 72.4, 53.9]
    //         ]
    //       },
    //       xAxis: { type: 'category' },
    //       yAxis: {},
    //       series: [ { type: 'bar' }, { type: 'bar' }]}
    //     myChart.setOption(option)
    //     window.addEventListener("resize", function() {
    //       myChart.resize()
    //     })
    //   }
    //   this.$on('hook:destroyed',()=>{
    //     window.removeEventListener("resize", function() {
    //       myChart.resize();
    //     });
    //   })
    // },

    // initChart() {
    //   if (!this.chart) {
    //     this.chart = echarts.init(document.getElementById("echarts"));
    //   }
    //   for (var i = 0; i < this.option.series[0].data.length; i++) {
    //     if (i === 0) {
    //       this.option.series[0].data[i].itemStyle = {
    //         normal: {
    //           color: this.colors[0],
    //         },
    //       };
    //     } else {
    //       this.option.series[0].data[i].itemStyle = {
    //         normal: {
    //           color: this.colors[1],
    //         },
    //       };
    //     }
    //   }
    //   this.chart.setOption(this.option);
    // },
    handleDeptChange(){
      getBuyAndSaleStatistics({"roleType": Vue.ls.get('roleType'),"deptName":this.deptName}).then((res)=>{
        if(res.code === 200){
          this.statistics = res.data;
        }
      })
      buyOrSalePrice({"roleType": Vue.ls.get('roleType'),"deptName":this.deptName}).then(res=>{
        if(res.code === 200){
          this.salePriceData = res.data.financialList;
          this.retailPriceData = res.data.logisticsList;
          this.buyPriceData = res.data.expressPriceList;
        }
      })
    },
    initInfo () {
      getBuyAndSaleStatistics({"roleType": Vue.ls.get('roleType')}).then((res)=>{
        if(res.code === 200){
          this.statistics = res.data;
        }
      })
      buyOrSalePrice({"roleType": Vue.ls.get('roleType')}).then(res=>{
        if(res.code === 200){
          this.salePriceData = res.data.financialList;
          this.retailPriceData = res.data.logisticsList;
          this.buyPriceData = res.data.expressPriceList;
        }
      })
      getPlatformConfigByKey({"platformKey": "pay_fee_url"}).then((res)=> {
        if (res && res.code === 200) {
          this.payFeeUrl = res.data.platformValue
        }
      })
    },
    initWithTenant() {
      getAction("/user/infoWithTenant",{}).then(res=>{
        if(res && res.code === 200) {
          this.tenant = res.data
          let currentTime = new Date(); //新建一个日期对象，默认现在的时间
          let expireTime = new Date(res.data.expireTime); //设置过去的一个时间点，"yyyy-MM-dd HH:mm:ss"格式化日期
          let difftime = expireTime - currentTime; //计算时间差
          //如果距离到期还剩5天就进行提示续费
          if(difftime<86400000*5) {
            this.hasExpire = true
            //针对免费租户发送试用到期的消息提醒
            if(res.data.type === '0') {
              //先检查有无发送过，只发送一次
              getAction("/msg/getMsgCountByType",{'type': '试用到期'}).then(res=>{
                if(res && res.code === 200) {
                  if(res.data.count === 0) {
                    //发送消息
                    let msgParam = {
                      'msgTitle': '试用到期提醒',
                      'msgContent': '试用期即将结束，请您及时续费，过期将会影响正常使用！',
                      'type': '试用到期',
                      'userId': this.tenant.tenantId
                    }
                    postAction("/msg/add",msgParam).then(res=>{
                      if(res && res.code === 200) {

                      }
                    })
                  }
                }
              })
            }
          }
        }
      })
    },
    handleHoverChange(visible) {
      this.hovered = visible
    },
    showWeixinSpan() {
      let host = window.location.host
      if(host === 'cloud.huaxiaerp.vip' || host === 'cloud.huaxiaerp.com') {
        return true
      } else {
        return false
      }
    }
  }
}
</script>
<style lang="less" scoped>
.circle-cust{
  position: relative;
  top: 28px;
  left: -100%;
}
.extra-wrapper {
  line-height: 55px;
  padding-right: 24px;

  .extra-item {
    display: inline-block;
    margin-right: 24px;

    a {
      margin-left: 24px;
    }
  }
}
/* 首页访问量统计 */
.head-info {
  position: relative;
  text-align: left;
  padding: 0 32px 0 0;
  min-width: 125px;
  &.center {
    text-align: center;
    padding: 0 32px;
  }
  span {
    color: rgba(0, 0, 0, .45);
    display: inline-block;
    font-size: .95rem;
    line-height: 42px;
    margin-bottom: 4px;
  }
  p {
    line-height: 42px;
    margin: 0;
    a {
      font-weight: 600;
      font-size: 1rem;
    }
  }
}
</style>