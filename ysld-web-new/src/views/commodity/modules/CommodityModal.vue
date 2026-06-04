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
          <a-tab-pane
            key="1"
            tab="基本信息"
            id="commodityHeadModal"
            forceRender
          >
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="货品名称"
                  data-step="1"
                  data-title="货品名称"
                  data-intro="货品名称必填，可以重复"
                >
                  <a-input
                    placeholder="请输入货品名称"
                    v-decorator.trim="['name', validatorRules.name]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="货品编号"
                  data-step="2"
                  data-title="货品编号"
                  data-intro="货品编号必填，不可以重复"
                >
                  <a-input
                    placeholder="请输入货品编号"
                    v-decorator.trim="[
                      'commodityNo',
                      validatorRules.commodityNo,
                    ]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
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
                    v-decorator.trim="['remark', validatorRules.remark]"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row class="form-row" :gutter="24">
              <a-col :md="12" :sm="24">
                <a-form-item
                  :labelCol="labelCol"
                  :wrapperCol="wrapperCol"
                  label="单价(元)"
                  data-step="1"
                  data-title="单价(元)"
                  data-intro="单价(元)不必填"
                >
                  <a-input
                    placeholder="请输入单价(元)"
                    v-decorator.trim="['price', validatorRules.price]"
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
import JEditableTable from "@/components/jeecg/JEditableTable";
import { getRefPromise } from "@/utils/JEditableTableUtil";
import { autoJumpNextInput, handleIntroJs } from "@/utils/util";
import { httpAction } from "@/api/manage";
import JDate from "@/components/jeecg/JDate";

export default {
  name: "CommodityModal",
  components: {
    JDate,
    JEditableTable,
    VNodes: {
      functional: true,
      render: (h, ctx) => ctx.props.vnodes,
    },
  },
  data() {
    return {
      modalStyle: {},
      prefixNo: "HPGL",
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
        name: {
          rules: [
            { required: true, message: "请输入名称!" },
            { max: 100, message: "长度请小于100个字符", trigger: "blur" },
          ],
        },
      },
      url: {
        add: "/commodity/add",
        edit: "/commodity/update",
      },
    };
  },
  created() {
    let realScreenWidth = window.screen.width;
    this.width = realScreenWidth < 1500 ? "1200px" : "1400px";
  },
  methods: {
    // 获取所有的editableTable实例
    getAllTable() {
      return Promise.all([
        getRefPromise(this, "editableMeTable"),
        getRefPromise(this, "editableDepotTable"),
      ]);
    },
    add() {
      //隐藏多属性
      // 默认新增一条数据
      this.getAllTable().then((editableTables) => {
        editableTables[0].add();
      });
      this.edit({});
      // this.$nextTick(() => {
      //   handleIntroJs("commodity", 11);
      // });
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
          pick(this.model, "name", "commodityNo", "remark", "price")
        );
        autoJumpNextInput("commodityHeadModal");
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
      // console.log(this.form.getFieldsValue())
      this.form.validateFields((err, values) => {
        if (!err) {
          let param = {
            id: this.model.id,
            name: this.form.getFieldValue("name"),
            commodityNo: this.form.getFieldValue("commodityNo"),
            remark: this.form.getFieldValue("remark"),
            price: this.form.getFieldValue("price"),
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
        url = this.url.edit;
        method = "put";
      }
      const that = this;
      this.confirmLoading = true;
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
