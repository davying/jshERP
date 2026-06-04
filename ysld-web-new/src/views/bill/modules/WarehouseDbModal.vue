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
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="货品编号"
                    data-step="1"
                    data-title="货品编号"
                    data-intro="货品编号必填"
                >
                  <a-input v-model="record.commodityNo" :readOnly="true"></a-input>
<!--                  <a-select-->
<!--                      placeholder="请选择货品编号"-->
<!--                      showSearch-->
<!--                      v-decorator.trim="[-->
<!--                      'commodityNo',-->
<!--                      validatorRules.commodityNo,-->
<!--                    ]"-->
<!--                  >-->
<!--                    <a-select-option-->
<!--                        v-for="(site, index) in searchOptions.options"-->
<!--                        :key="index"-->
<!--                        :value="site.value"-->
<!--                    >{{ site.label }}</a-select-option-->
<!--                    >-->
<!--                  </a-select>-->
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="颜色"
                    data-step="2"
                    data-title="颜色"
                    data-intro="颜色必填"
                >
                  <a-input
                      placeholder="请输入颜色"
                      v-model="record.color"
                      :readOnly="true"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
              <a-col :md="12" :sm="24">
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
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
              <a-col :md="12" :sm="24">
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
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
              <a-col :md="12" :sm="24">
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
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
              <a-col :md="12" :sm="24">
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
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
              <a-col :md="12" :sm="24">
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
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
                <a-form-item
                    :labelCol="labelCol"
                    :wrapperCol="wrapperCol"
                    label="数量"
                    data-step="1"
                    data-title="数量"
                    data-intro="数量必填"
                >
                  <a-input
                      disabled
                      v-decorator.trim="['numbers']"
                  />
                </a-form-item>
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
import { getAction, httpAction ,postAction} from "@/api/manage";
import JDate from "@/components/jeecg/JDate";
import moment from "moment";

export default {
  name: "WarehouseDbModal",
  components: {
    JDate,
    JSelectMultiple,
    JEditableTable,
    VNodes: {
      functional: true,
      render: (h, ctx) => ctx.props.vnodes,
    },
  },
  data() {
    return {
      modalStyle: {},
      prefixNo: "YPGL",
      searchOptions: {
        value: "",
        options: [],
      },
      record:{},
      // commodityNo:'',
      // color:'',
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
        commodityNo: {
          rules: [{ required: true, message: "请选择!" }],
        },
        numbers:{
          rules:[
            { pattern: /^[0-9]\d*$/, message: '请输入正整数，无请输入0!' }
          ]
        },
      },
      url: {
        add: "/warehouseManagement/add",
        edit: "/warehouseManagement/update",
        commodityList: "/commodity/getCommodityList",
        // materialsExtendList: '/materialsExtend/getDetailList',
        // depotWithStock: '/depot/getAllListWithStock'
      },
    };
  },
  created() {
    let realScreenWidth = window.screen.width;
    this.width = realScreenWidth < 1500 ? "1200px" : "1400px";
    this.searchCommodity();
  },
  computed: {},
  methods: {
    searchCommodity() {
      let lists = [];
      getAction(this.url.commodityList, null).then((res) => {
        if (res.code == 1) {
          for (let i of res.data) {
            lists.push(i);
          }
          this.searchOptions.options = res.data.map((item) => {
            return {
              value: item.commodityNo,
              label: item.name + "（" + item.commodityNo + "）",
              name:item.name
            };
          });
        }

        if (res.code == 0) {
          this.$message.warning(res.msg);
        }
      });
    },
    // 获取所有的editableTable实例
    // 获取所有的editableTable实例
    getAllTable() {
      return Promise.all([
        getRefPromise(this, "editableMeTable"),
        getRefPromise(this, "editableDepotTable"),
      ]);
    },
    add(record) {
      //隐藏多属性
      // 默认新增一条数据
      this.record=record
      this.getAllTable().then((editableTables) => {
        editableTables[0].add();
      });
      this.edit({});
    },
    edit(record) {
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
        this.form.setFieldsValue(
            pick(
                this.model,
                "color",
                "size1",
                "price",
                "commodityNo",
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
                "numbers"
            )
        );
        autoJumpNextInput("warehouseDbModal");
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
      });
    },
    /** 触发表单验证 */
    validateFields() {
      if(this.form.getFieldValue("size1")>this.record.size1){
        this.$message.error("S库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size2")>this.record.size2){
        this.$message.error("L库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size3")>this.record.size3){
        this.$message.error("M库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size4")>this.record.size4){
        this.$message.error("XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size5")>this.record.size5){
        this.$message.error("2XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size6")>this.record.size6){
        this.$message.error("3XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size7")>this.record.size7){
        this.$message.error("4XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size8")>this.record.size8){
        this.$message.error("5XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size9")>this.record.size9){
        this.$message.error("6XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size10")>this.record.size10){
        this.$message.error("7XL库存不足，无法调拨！");
        return
      }
      if(this.form.getFieldValue("size11")>this.record.size11){
        this.$message.error("XS库存不足，无法调拨！");
        return
      }
      this.form.validateFields((err, values) => {
        if (!err) {
          let param = {
            type: '调拨',
            commodityNo: this.record.commodityNo,
            commodityName: this.record.commodityName,
            color: this.record.color,
            size1: this.form.getFieldValue("size1"),
            size2: this.form.getFieldValue("size2"),
            size3: this.form.getFieldValue("size3"),
            size4: this.form.getFieldValue("size4"),
            size5: this.form.getFieldValue("size5"),
            size6: this.form.getFieldValue("size6"),
            size7: this.form.getFieldValue("size7"),
            size8: this.form.getFieldValue("size8"),
            size9: this.form.getFieldValue("size9"),
            size10: this.form.getFieldValue("size10"),
            size11: this.form.getFieldValue("size11"),
            numbers: this.form.getFieldValue("numbers")
          };
          // 发起请求
          return this.requestAddOrEdit(param);
        }
      });
    },
    /** 发起新增或修改的请求 */
    requestAddOrEdit(formData) {
      //接口调用
      let url = this.url.add,
          method = "post";
      if (this.model.id) {
        url = this.url.add;
        method = "post";
      }
      const that = this;
      this.confirmLoading = true;
      // return;
      httpAction(url, formData, method)
          .then((res) => {
            if (res.code === 200) {
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
