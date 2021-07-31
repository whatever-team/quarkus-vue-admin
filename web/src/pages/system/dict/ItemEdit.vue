<template>
  <a-drawer
      :title="title"
      :width="720"
      :visible="visible"
      :body-style="{ paddingBottom: '80px' }"
      @close="onClose"
  >
    <div>
      <a-form-model ref="editForm" :model="form" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">

        <a-form-model-item label="字典编码" prop="dictCode">
          <a-input v-model="form.dictCode" disabled />
        </a-form-model-item>

        <a-form-model-item label="值编码" prop="itemCode">
          <a-input v-model="form.itemCode" />
        </a-form-model-item>

        <a-form-model-item label="值名称" prop="itemName">
          <a-input v-model="form.itemName" />
        </a-form-model-item>

        <a-form-model-item label="状态" prop="enabled">
          <a-switch v-model="form.enabled" checked-children="显示" un-checked-children="隐藏"/>
        </a-form-model-item>

      </a-form-model>
      <div class="drawer-form-button-group">
        <a-button :style="{ marginRight: '8px' }" @click="onClose">
          取消
        </a-button>
        <a-button type="primary" @click="onSubmit" :loading="submitLoading">
          提交
        </a-button>
      </div>
    </div>
  </a-drawer>
</template>

<script>
const rules = {
  dictCode: [
    { required: true, message: '请输入字典编码', trigger: 'blur' },
  ],
  itemCode: [
    { required: true, message: '请输入值编码', trigger: 'blur' },
  ],
  enabled: [
    { required: true, message: '请选择状态', trigger: 'blur' },
  ],
  itemName: [
    { required: true, message: '请输入值名称', trigger: 'blur' },
  ],
};

import {updateItem, addItem} from '@/services/dict'
export default {
  name: "Edit",
  props: {
    dictCode: {
      required: false,
      type: String
    }
  },
  data() {
    return {
      title: '新增字典值',
      visible: false,
      isAdd: false,
      submitLoading: false,
      roleData: [],
      deptData: [],
      form: {
        id: null,
        dictCode: '',
        itemCode: '',
        itemName: '',
        enabled: true,
      },
      formDefault: {},
      rules,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
    };
  },
  mounted() {
    this.formDefault = JSON.parse(JSON.stringify(this.form))
  },
  methods: {
    add() {
      this.title = '新增字典值'
      this.form = Object.assign({}, this.formDefault);
      this.isAdd = true
      this.show()
    },
    update(data) {
      this.title = '编辑字典值'
      this.isAdd = false
      this.form = Object.assign({}, data);
      this.show()
    },
    show() {
      this.form.dictCode = this.dictCode
      this.visible = true;
    },
    onClose() {
      this.visible = false;
    },
    onSubmit() {
      this.$refs.editForm.validate(async valid => {
        if (valid) {
          this.submitLoading = true
          let response
          if (this.isAdd) {
            response = await addItem(this.form)
          } else {
            response = await updateItem(this.form)
          }
          this.submitLoading = false

          if (response.data.code === 'SUCCESS') {
            this.$emit('change', this.form)
            this.onClose()
          } else {
            this.$message.error(response.data.message)
          }
        } else {
          return false;
        }
      });
    },
  }
}
</script>

<style scoped>

</style>
