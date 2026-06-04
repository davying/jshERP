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
<!--    <a-row :gutter="24">-->
<!--      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">-->
<!--        <a-card :loading="loading" :bordered="false" :body-style="{paddingRight: '5'}" data-step="5" data-title="销售统计"-->
<!--                data-intro="统计往前6个月每月销售的总金额">-->
<!--          <bar title="销售统计(元)" :height="barHeight" :yaxisText="yaxisText" :dataSource="salePriceData"/>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">-->
<!--        <a-card :loading="loading" :bordered="false" :body-style="{paddingRight: '5'}" data-step="6" data-title="订单物流费统计"-->
<!--                data-intro="统计往前6个月每月物流费的总金额">-->
<!--          <bar title="订单物流费统计(元)" :height="barHeight" :yaxisText="yaxisText" :dataSource="retailPriceData"/>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">-->
<!--        <a-card :loading="loading" :bordered="false" :body-style="{paddingRight: '5'}" data-step="4" data-title="样品物流费统计"-->
<!--                data-intro="统计往前6个月每月样品物流费的总金额">-->
<!--          <bar title="样品物流费统计(元)" :height="barHeight" :yaxisText="yaxisText" :dataSource="buyPriceData"/>-->
<!--        </a-card>-->
<!--      </a-col>-->
<!--    </a-row>-->
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <div id="reservation" style="width: 1600px;height: 300px"></div>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <div id="salesManList" style="width: 1600px;height: 300px"></div>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <div id="logisticsList" style="width: 1600px;height: 300px"></div>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <div id="commodityList" style="width: 1600px;height: 300px"></div>
      </a-col>
    </a-row>
    <a-row :gutter="24">
      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">
        <div id="commodityNoList" style="width: 1600px;height: 300px"></div>
      </a-col>
    </a-row>

<!--    <a-row :gutter="24">-->
<!--      <a-col :sm="24" :md="12" :xl="8" :style="{ paddingRight: '0px',marginBottom: '12px' }">-->
<!--        <div id="expressPriceList" style="width: 1600px;height: 300px"></div>-->
<!--      </a-col>-->
<!--    </a-row>-->
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
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 }
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 12 }
      },
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
    this.initInfo()
    this.initWithTenant()

  },
  mounted() {
    handleIntroJs('indexChart', 1)

  },
  methods: {
    focus() {},
    // 销售额
    initReservation(salePriceData) {
      var x=[];
      var y=[];
      for (var i = 0; i < salePriceData.length; i++) {
        x.push(salePriceData[i].x)
        y.push(salePriceData[i].y)
      }
      var option = {
      title : {
        text: '销售统计',
        subtext: '单位（万元）'
      },
      color: ['#0072E3'],
      toolbox: {
        show : true,
        feature : {
          mark : {show: true},
          dataView : {show: true, readOnly: false},
          magicType: {show: true, type: ['line', 'bar']},
          restore : {show: true},
          saveAsImage : {show: true}
        }
      },
      tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['最近12月销售额']
        },
        calculable : true,
        xAxis : [
          {
            type : 'category',
            boundaryGap : false,
            data : x
          }
        ],
        yAxis : [
          {
            type : 'value',
            axisLabel : {
              formatter: '{value} 万元'
            }
          }
        ],
        series : [
          {
            name:'最近12月销售额',
            type:'line',
            data:y,
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
          }
        ]
      };
      var myChart = echarts.init(document.getElementById("reservation"));
      //白色模式
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    // 订单物流费统计
    initLogisticsList(logisticsList) {
      var x=[];
      var y=[];
      for (var i = 0; i < logisticsList.length; i++) {
        x.push(logisticsList[i].x)
        y.push(logisticsList[i].y)
      }
      var option = {
        title : {
          text: '订单物流费统计',
          subtext: '单位（万元）'
        },
        color: ['#0072E3'],
        toolbox: {
          show : true,
          feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
          }
        },
        tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['最近12月订单物流费']
        },
        calculable : true,
        xAxis : [
          {
            type : 'category',
            boundaryGap : false,
            data : x
          }
        ],
        yAxis : [
          {
            type : 'value',
            axisLabel : {
              formatter: '{value} 万元'
            }
          }
        ],
        series : [
          {
            name:'最近12月订单物流费',
            type:'line',
            data:y,
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
          }
        ]
      };
      var myChart = echarts.init(document.getElementById("logisticsList"));
      //白色模式
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    // // 样品物流费统计
    // initExpressPriceList(expressPriceList) {
    //   var x=[];
    //   var y=[];
    //   for (var i = 0; i < expressPriceList.length; i++) {
    //     x.push(expressPriceList[i].x)
    //     y.push(expressPriceList[i].y)
    //   }
    //   var option = {
    //     title : {
    //       text: '样品物流费统计',
    //       subtext: '单位（万元）'
    //     },
    //     toolbox: {
    //       show : true,
    //       feature : {
    //         magicType : {show: true, type: ['line', 'bar']},
    //       }
    //     },
    //     color: ['#0072E3'],
    //     tooltip : {
    //       trigger: 'axis'
    //     },
    //     legend: {
    //       data:['最近12月样品物流费']
    //     },
    //     calculable : true,
    //     xAxis : [
    //       {
    //         type : 'category',
    //         boundaryGap : false,
    //         data : x
    //       }
    //     ],
    //     yAxis : [
    //       {
    //         type : 'value',
    //         axisLabel : {
    //           formatter: '{value} 万元'
    //         }
    //       }
    //     ],
    //     series : [
    //       {
    //         name:'最近12月样品物流费',
    //         type:'line',
    //         data:y,
    //         markPoint : {
    //           data : [
    //             {type : 'max', name: '最大值'},
    //             {type : 'min', name: '最小值'}
    //           ]
    //         },
    //         markLine : {
    //           data : [
    //             {type : 'average', name: '平均值'}
    //           ]
    //         }
    //       }
    //     ]
    //   };
    //   var myChart = echarts.init(document.getElementById("expressPriceList"));
    //   //白色模式
    //   myChart.setOption(option);
    //   //随着屏幕大小调节图表
    //   window.addEventListener("resize", () => {
    //     myChart.resize();
    //   });
    // },
    // 今年业务员汇总统计
    initSalesManList(salesMan) {
      var x=[];
      var y0=[];
      var y1=[];
      var y2=[];
      for (var i = 0; i < salesMan.length; i++) {
        x.push(salesMan[i].salesMan)
        y0.push(salesMan[i].depots)
        y1.push(salesMan[i].numbers)
        y2.push(salesMan[i].amount)
      }
      var option = {
        title : {
          text: '今年业务员汇总统计',
          subtext: '业务员汇总'
        },
        tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['订单数（笔）','数量（件）','销售额（元）']
        },
        toolbox: {
          show : true,
          feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
          }
        },
        calculable : true,
        xAxis : [
          {
            type : 'category',
            data : x
          }
        ],
        yAxis : [
          {
            type : 'value'
          }
        ],
        series : [
          {
            name:'订单数（笔）',
            type:'bar',
            data:y0,
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
            name:'数量（件）',
            type:'bar',
            data:y1,
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
            name:'销售额（元）',
            type:'bar',
            data:y2,
            markPoint : {
              data : [
                {type : 'max', name: '最大值'},
                {type : 'min', name: '最小值'}
              ]
              // data : [
              //   {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
              //   {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
              // ]
            },
            markLine : {
              data : [
                {type : 'average', name : '平均值'}
              ]
            }
          }
        ]
      };
      var myChart = echarts.init(document.getElementById("salesManList"));
      //白色模式
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    // 今年货品信息汇总统计
    initCommodityList(commodityList) {
      var x=[];
      var y0=[];
      var y1=[];
      var y2=[];
      for (var i = 0; i < commodityList.length; i++) {
        x.push(commodityList[i].commodityName)
        y0.push(commodityList[i].depots)
        y1.push(commodityList[i].numbers)
        y2.push(commodityList[i].amount)
      }
      var option = {
        title : {
          text: '今年货品信息汇总统计',
          subtext: '货品信息汇总'
        },
        tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['订单数（笔）','数量（件）','销售额（元）']
        },
        toolbox: {
          show : true,
          feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
          }
        },
        calculable : true,
        xAxis : [
          {
            type : 'category',
            data : x
          }
        ],
        yAxis : [
          {
            type : 'value'
          }
        ],
        series : [
          {
            name:'订单数（笔）',
            type:'bar',
            data:y0,
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
            name:'数量（件）',
            type:'bar',
            data:y1,
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
            name:'销售额（元）',
            type:'bar',
            data:y2,
            markPoint : {
              data : [
                {type : 'max', name: '最大值'},
                {type : 'min', name: '最小值'}
              ]
              // data : [
              //   {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
              //   {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
              // ]
            },
            markLine : {
              data : [
                {type : 'average', name : '平均值'}
              ]
            }
          }
        ]
      };
      var myChart = echarts.init(document.getElementById("commodityList"));
      //白色模式
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    // 今年款式信息汇总统计
    initCommodityNoList(commodityNoList) {
      var x=[];
      var y0=[];
      var y1=[];
      var y2=[];
      for (var i = 0; i < commodityNoList.length; i++) {
        x.push(commodityNoList[i].commodityName+'('+commodityNoList[i].commodityNo+')')
        y0.push(commodityNoList[i].depots)
        y1.push(commodityNoList[i].numbers)
        y2.push(commodityNoList[i].amount)
      }
      var option = {
        title : {
          text: '今年款式销量前20统计',
          subtext: '款式汇总'
        },
        tooltip : {
          trigger: 'axis'
        },
        legend: {
          data:['订单数（笔）','数量（件）','销售额（元）']
        },
        toolbox: {
          show : true,
          feature : {
            mark : {show: true},
            dataView : {show: true, readOnly: false},
            magicType: {show: true, type: ['line', 'bar']},
            restore : {show: true},
            saveAsImage : {show: true}
          }
        },
        calculable : true,
        xAxis : [
          {
            type : 'category',
            data : x
          }
        ],
        yAxis : [
          {
            type : 'value'
          }
        ],
        series : [
          {
            name:'订单数（笔）',
            type:'bar',
            data:y0,
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
            name:'数量（件）',
            type:'bar',
            data:y1,
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
            name:'销售额（元）',
            type:'bar',
            data:y2,
            markPoint : {
              data : [
                {type : 'max', name: '最大值'},
                {type : 'min', name: '最小值'}
              ]
              // data : [
              //   {name : '年最高', value : 182.2, xAxis: 7, yAxis: 183, symbolSize:18},
              //   {name : '年最低', value : 2.3, xAxis: 11, yAxis: 3}
              // ]
            },
            markLine : {
              data : [
                {type : 'average', name : '平均值'}
              ]
            }
          }
        ]
      };
      var myChart = echarts.init(document.getElementById("commodityNoList"));
      //白色模式
      myChart.setOption(option);
      //随着屏幕大小调节图表
      window.addEventListener("resize", () => {
        myChart.resize();
      });
    },
    handleDeptChange(){
      let salePriceData;
      let logisticsList;
      let expressPriceList;
      getBuyAndSaleStatistics({"roleType": Vue.ls.get('roleType'),"deptName":this.deptName}).then((res)=>{
        if(res.code === 200){
          this.statistics = res.data;
        }
      })
      buyOrSalePrice({"roleType": Vue.ls.get('roleType'),"deptName":this.deptName}).then(res=>{
        if(res.code === 200){
          salePriceData=res.data.financialList;
          logisticsList=res.data.logisticsList;
          expressPriceList=res.data.expressPriceList;
          this.initReservation(salePriceData)
          this.initLogisticsList(logisticsList)
          // this.initExpressPriceList(expressPriceList)
          this.initSalesManList(res.data.salesMan)
          this.initCommodityList(res.data.commodityList)
          this.initCommodityNoList(res.data.commodityNoList)
          // this.salePriceData = salePriceData;
          // this.retailPriceData = logisticsList;
          // this.buyPriceData = expressPriceList;
        }
      })
    },
    initInfo () {
      this.loading = true
      let salePriceData;
      let logisticsList;
      let expressPriceList;
      const statisticsReq = getBuyAndSaleStatistics({"roleType": Vue.ls.get('roleType')}).then((res)=>{
        if(res.code === 200){
          this.statistics = res.data;
        }
      })
      const trendReq = buyOrSalePrice({"roleType": Vue.ls.get('roleType')}).then(res=>{
        if(res.code === 200){
          salePriceData=res.data.financialList;
          logisticsList=res.data.logisticsList;
          expressPriceList=res.data.expressPriceList;
          this.initReservation(salePriceData)
          this.initLogisticsList(logisticsList)
          // this.initExpressPriceList(expressPriceList)
          this.initSalesManList(res.data.salesMan)
          this.initCommodityList(res.data.commodityList)
          this.initCommodityNoList(res.data.commodityNoList)
          // this.salePriceData = salePriceData;
          // this.retailPriceData = logisticsList;
          // this.buyPriceData = expressPriceList;
        }
      })
      const payFeeReq = getPlatformConfigByKey({"platformKey": "pay_fee_url"}).then((res)=> {
        if (res && res.code === 200) {
          this.payFeeUrl = res.data.platformValue
        }
      })
      Promise.all([statisticsReq, trendReq, payFeeReq]).finally(() => {
        this.loading = false
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