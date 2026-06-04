<template>
  <j-modal
    :title="title"
    :width="width"
    :visible="visible"
    :confirmLoading="confirmLoading"
    v-bind:prefixNo="prefixNo"
    switchFullscreen
    @cancel="handleCancel"
    :id="prefixNo"
    :style="modalStyle"
  >
    <template slot="footer">
      <a-button key="back" @click="handleCancel">关闭</a-button>
      <a-button
        type="primary"
        v-if="showOkFlag"
        :loading="confirmLoading"
        @click="handleOk"
        >确定</a-button
      >
    </template>
    <a-spin :spinning="confirmLoading">
      <a-form :form="form">
        <a-tabs default-active-key="1" size="small">
          <a-tab-pane key="1" tab="基本信息" id="sampleHeadModal" forceRender>
<!--            <a-row class="form-row" :gutter="24">-->
<!--              <a-col :md="24" :sm="0">-->
<!--              <a-form-item-->
<!--                  :labelCol="labelCol"-->
<!--                  :wrapperCol="wrapperCol"-->
<!--                  label="样品编号"-->
<!--                  data-step="1"-->
<!--                  data-title="样品编号"-->
<!--                  data-intro="样品编号必填"-->
<!--              >-->
<!--                &lt;!&ndash; <j-select-multiple-->
<!--                  placeholder="请选择货品编号"-->
<!--                  v-model="searchOptions.commodityNo"-->
<!--                  :options="searchOptions.options"-->
<!--                /> &ndash;&gt;-->
<!--                <a-select placeholder="请选择样品编号" showSearch  :disabled="sampleFlag" v-decorator.trim="['sampleId', validatorRules.sampleId]" @change="sampleChange">-->
<!--                  <a-select-option-->
<!--                      v-for="(site, index) in searchOptions.options"-->
<!--                      :key="index"-->
<!--                      :value="site.value"-->
<!--                  >{{ site.label }}</a-select-option-->
<!--                  >-->
<!--                </a-select>-->
<!--              </a-form-item>-->
<!--              </a-col>>-->
<!--            </a-row>-->
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="样品编号"
                  data-step="1"
                  data-title="样品编号"
                  data-intro="样品编号必填"
                >
                  <!-- <j-select-multiple
                    placeholder="请选择货品编号"
                    v-model="searchOptions.commodityNo"
                    :options="searchOptions.options"
                  /> -->
                  <a-select placeholder="请选择样品编号" showSearch  :disabled="sampleFlag" v-decorator.trim="['sampleId', validatorRules.sampleId]" @change="sampleChange">
                    <a-select-option
                      v-for="(site, index) in searchOptions.options"
                      :key="index"
                      :value="site.value"
                      >{{ site.label }}</a-select-option
                    >
                  </a-select>
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="样衣单编号"
                    data-step="1"
                    data-title="样衣单编号"
                    data-intro="样衣单编号不必填"
                >
                  <a-input
                      placeholder="请输入样衣单编号"
                      v-decorator.trim="['sampleNumber']"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="S"
                  data-step="1"
                  data-title="S"
                  data-intro="S不必填"
                >
                  <a-input
                    placeholder="请输入S"
                    v-decorator.trim="['size1', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="M"
                  data-step="1"
                  data-title="M"
                  data-intro="M不必填"
                >
                  <a-input
                    placeholder="请输入M"
                    v-decorator.trim="['size2', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="L"
                  data-step="1"
                  data-title="L"
                  data-intro="L不必填"
                >
                  <a-input
                    placeholder="请输入L"
                    v-decorator.trim="['size3', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="XL"
                  data-step="1"
                  data-title="XL"
                  data-intro="XL不必填"
                >
                  <a-input
                    placeholder="请输入XL"
                    v-decorator.trim="['size4', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="2XL"
                  data-step="1"
                  data-title="2XL"
                  data-intro="2XL不必填"
                >
                  <a-input
                    placeholder="请输入2XL"
                    v-decorator.trim="['size5', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="3XL"
                  data-step="1"
                  data-title="3XL"
                  data-intro="3XL不必填"
                >
                  <a-input
                    placeholder="请输入3XL"
                    v-decorator.trim="['size6', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="4XL"
                  data-step="1"
                  data-title="4XL"
                  data-intro="4XL不必填"
                >
                  <a-input
                    placeholder="请输入4XL"
                    v-decorator.trim="['size7', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="5XL"
                  data-step="1"
                  data-title="5XL"
                  data-intro="5XL不必填"
                >
                  <a-input
                    placeholder="请输入5XL"
                    v-decorator.trim="['size8', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="6XL"
                  data-step="1"
                  data-title="6XL"
                  data-intro="6XL不必填"
                >
                  <a-input
                    placeholder="请输入6XL"
                    v-decorator.trim="['size9', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="7XL"
                  data-step="1"
                  data-title="7XL"
                  data-intro="7XL不必填"
                >
                  <a-input
                    placeholder="请输入7XL"
                    v-decorator.trim="['size10', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="XS"
                  data-step="1"
                  data-title="XS"
                  data-intro="XS不必填"
                >
                  <a-input
                    placeholder="请输入XS"
                    v-decorator.trim="['size11', validatorRules.numbers]"
                    @change="handleSizeChange"
                  />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="数量"
                  data-step="1"
                  data-title="数量"
                >
                  <a-input
                    disabled
                    v-decorator.trim="['numbers', validatorRules.numbers]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="单价"
                    data-step="1"
                    data-title="单价"
                >
                  <a-input v-decorator.trim="['price']" />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="金额"
                    data-step="1"
                    data-title="金额"
                >
                  <a-input v-decorator.trim="['totalPrice']" />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="已收样衣费"
                    data-step="1"
                    data-title="已收样衣费"
                >
                  <a-input v-decorator.trim="['sampleCharge',validatorRules.price]" />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="业务员"
                  data-step="1"
                  data-title="业务员"
                >
<!--                  <j-select-multiple-->
<!--                    style="width:185px;"-->
<!--                    placeholder="业务员"-->
<!--                    v-model="personList.value"-->
<!--                    :options="personList.options"-->
<!--                  />-->

                  <a-select showSearch placeholder="请选择业务员" v-decorator.trim="['member']">
                    <a-select-option v-for="(person,index) in personList" :value="person.text" :key="index">
                      {{ person.text }}
                    </a-select-option>
                  </a-select>
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="快递费(元)"
                  data-step="1"
                  data-title="快递费(元)"
                >
                  <a-input v-decorator.trim="['expressPrice',validatorRules.price]" />
                </a-form-item>
              </a-col>
<!--            </a-row>-->
<!--            <a-row class="form-row" :gutter="24">-->
              <a-col :md="12" :sm="12">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="备注"
                    data-step="1"
                    data-title="备注"
                    data-intro="备注不必填"
                >
                  <a-input
                      placeholder="请输入备注"
                      v-decorator.trim="['remark']"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="12">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="领样日期"
                    data-step="1"
                    data-title="领样日期"
                >
                  <j-date
                      date-format="YYYY-MM-DD"
                      style="width: 100%"
                      v-decorator.trim="['createTime',validatorRules.createTime]" />
                </a-form-item>
              </a-col>

              <a-col :md="12" :sm="12">

              </a-col>


            </a-row>
          </a-tab-pane>
        </a-tabs>
      </a-form>
    </a-spin>
    <!--    <unit-modal ref="unitModalForm" @ok="unitModalFormOk"></unit-modal>-->
  </j-modal>
</template>
<script>
import pick from "lodash.pick";
import JSelectMultiple from "@/components/jeecg/JSelectMultiple";
import JEditableTable from "@/components/jeecg/JEditableTable";
import { getRefPromise } from "@/utils/JEditableTableUtil";
import { autoJumpNextInput } from "@/utils/util";
import { getAction, httpAction } from "@/api/manage";
import JDate from "@/components/jeecg/JDate";
import { BillModalMixin } from "../../bill/mixins/BillModalMixin";
import { getCurrentSystemConfig,getPersonByNumType } from '@/api/api'

export default {
  name: "SampleManagementLogModal",
  mixins: [BillModalMixin],
  components: {
    JDate,
    JSelectMultiple,
    JEditableTable,
    getPersonByNumType,
    VNodes: {
      functional: true,
      render: (h, ctx) => ctx.props.vnodes,
    },
  },
  data() {
    return {
      personList: {
        text: '',
        value: ''
      },
      modalStyle: {},
      sampleId:'',
      sampleFlag:true,
      prefixNo: "YPGL",
      searchOptions: {
        value: "",
        options: [],
      },
      title: "操作",
      width: "1300px",
      visible: false,
      model: {},
      columns: {},
      showOkFlag: true,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 8 },
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 16 },
      },
      depotTable: {
        loading: false,
        dataSource: [],
        columns: [],
      },
      confirmLoading: false,
      form: this.$form.createForm(this),
      validatorRules: {
        sampleId: {
          rules: [{ required: true, message: "请选择样品编号!" }],
        },
        numbers: {
          rules: [
            { pattern: /^[0-9]\d*$/, message: "请输入正整数，无请输入0!" },
          ],
        },
        price: {
          rules: [
            { required: true, message: "请输入金额，如果为空请填0！" },
            {
              pattern: /^(([0-9][0-9]*)|([0]\.\d{0,4}|[0-9][0-9]*\.\d{0,4}))$/,
              message: "金额格式不正确!",
            },
          ],
        },
        createTime: {
          rules: [
            { required: true, message: "请输入领样日期！" },
          ],
        },
      },
      url: {
        add: "/sampleManagementLog/add",
        edit: "/sampleManagementLog/add",
        commodityList: "/commodity/getCommodityList",
        sampleList: "/sampleManagement/getSampleManagementList",
        // materialsExtendList: '/materialsExtend/getDetailList',
        // depotWithStock: '/depot/getAllListWithStock'
      },
    };
  },
  created() {
    let realScreenWidth = window.screen.width;
    this.width = realScreenWidth < 1500 ? "1200px" : "1400px";
    // this.searchCommodity();
    this.searchSapmle();
    this.initSalesman();
  },
  methods: {
    sampleChange(record){

      console.log('sampleChange:'+record)
    },
    initSalesman() {
      getPersonByNumType({type:1}).then((res)=>{
        if(res) {
          this.personList = res;
        }
      });
    },
    countTotalPrice(num){
      console.log(this.form.getFieldValue("sampleId"))
      let sampleIdStr =this.form.getFieldValue("sampleId")+''
      if(sampleIdStr.includes('#')){
        this.form.setFieldsValue({
          price: this.searchOptions.options.find(
              (item) =>
                  item.id == this.form.getFieldValue("sampleId").split('#')[0]
          ).price
        });
      }else{
        this.form.setFieldsValue({
          price: this.searchOptions.options.find(
              (item) =>
                  item.id == this.form.getFieldValue("sampleId")
          ).price
        });
      }
      this.form.setFieldsValue({
        totalPrice: this.form.getFieldValue("price")*num
      });
    },
    handleSizeChange(e) {
      this.$nextTick(() => {
        let num = 0;
        for (let i = 1; i < 12; i++) {
          if (this.form.getFieldValue("size" + i)) {
            num += Number(this.form.getFieldValue("size" + i));
          }
        }
        this.form.setFieldsValue({
          numbers: num,
        });
        this.countTotalPrice(num);
      });
    },
    searchSapmle() {
      getAction(this.url.sampleList, null).then((res) => {
        if (res.code == 1) {
          this.searchOptions.options = res.data.map((item) => {
            return {
              id: item.id,
              // sampleId:item.commodityNo,
              value: item.id+'#'+item.commodityNo,
              label: '样品编号:'+item.id+',货品编码:'+item.commodityNo + ',颜色:'+ item.color+',库存:'+item.numbers,
              price:item.price
            };
          });
        }
        if (res.code == 0) {
          this.$message.warning(res.msg);
        }
      });
    },
    // searchCommodity() {
    //   let lists = [];
    //   getAction(this.url.commodityList, null).then((res) => {
    //     if (res.code == 1) {
    //       for (let i of res.data) {
    //         lists.push(i);
    //       }
    //       this.searchOptions.options = res.data.map((item) => {
    //         return {
    //           value: item.commodityNo,
    //           label: item.name + "(" + item.commodityNo + ")",
    //         };
    //       });
    //     }
    //     if (res.code == 0) {
    //       this.$message.warning(res.msg);
    //     }
    //   });
    // },
    // 获取所有的editableTable实例
    getAllTable() {
      return Promise.all([
        getRefPromise(this, "editableMeTable"),
        getRefPromise(this, "editableDepotTable"),
      ]);
    },
    add() {
      //隐藏多属性
      this.sampleFlag=false;
      // 默认新增一条数据
      this.getAllTable().then((editableTables) => {
        editableTables[0].add();
      });
      this.edit({});
    },
    edit(record) {
      if(record.id){
        this.sampleFlag=false;
      }
      this.form.resetFields();
      this.model = Object.assign({}, record);
      this.activeKey = "1";
      this.visible = true;
      this.modalStyle = "top:20px;height: 95%;";
      if (JSON.stringify(record) === "{}") {
        this.fileList = [];
      } else {
        setTimeout(() => {
          this.fileList = record.imgName;
        }, 5);
      }
      this.$nextTick(() => {
        this.form.setFieldsValue(pick(
            this.model,
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
            "price",
            "totalPrice",
            "sampleCharge",
            "member",
            "expressPrice",
            "remark",
            "createTime"
        ));
        autoJumpNextInput("sampleHeadModal");
      });
    },
    close() {
      this.$emit("close");
      this.visible = false;
      this.modalStyle = "";
      this.getAllTable().then((editableTables) => {
        editableTables[0].initialize();
        editableTables[1].initialize();
      });
    },

    handleOk() {
      this.validateFields();
    },
    handleCancel() {
      this.close();
    },
    /** 触发表单验证 */
    validateFields() {
      this.form.validateFields((err, values) => {
        if (!err) {
          let param = {};
          param = this.form.getFieldsValue();
          param.id = this.model.id;
          //  param.member = this.personList.options.find(
          // (item) =>
          //     item.value == this.personList.value
          // ).text
          console.log('param: ', param);
          // 发起请求
          return this.requestAddOrEdit(param);
        }
      });
    },
    /** 发起新增或修改的请求 */
    requestAddOrEdit(formData) {
      const that = this;
      //接口调用
      let url = that.url.add,
        method = "post";
      if (this.model.id) {
        url = that.url.edit;
        method = "post";
      }

      this.confirmLoading = true;
      let sampleIdStr =formData.sampleId+''
      if(sampleIdStr.includes('#')){
      // if (!this.model.id) {
        let sampleId=that.searchOptions.options.find(
            (item) =>
                item.id == formData.sampleId.split('#')[0]
        ).id;
        formData.sampleId=sampleId;
      // }
      }
      // return;
      httpAction(url, formData, method)
        .then((res) => {
          if (res.code === 200) {
            that.searchSapmle();
            that.$emit("ok");
            that.confirmLoading = false;
            that.close();
          } else {
            that.$message.warning(res.data.message);
            that.confirmLoading = false;
          }
        })
        .finally(() => {});
    },
    parseParam(param) {
      return param ? param : "";
    },
  },
};
</script>
<style scoped>
.input-table {
  max-width: 100%;
  min-width: 1200px;
}
.tag-info {
  font-size: 14px;
  height: 32px;
  line-height: 32px;
  width: 100%;
  padding: 0px 11px;
  color: #bbb;
  background-color: #ffffff;
}
</style>
