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
                  <a-form-model-item label="varCode" prop="varCode">
            <a-input v-model="form.varCode" />
          </a-form-model-item>
          <a-form-model-item label="varName" prop="varName">
            <a-input v-model="form.varName" />
          </a-form-model-item>
          <a-form-model-item label="varValue" prop="varValue">
            <a-input v-model="form.varValue" />
          </a-form-model-item>
          <a-form-model-item label="createTime" prop="createTime">
            <a-input v-model="form.createTime" />
          </a-form-model-item>
          <a-form-model-item label="createBy" prop="createBy">
            <a-input v-model="form.createBy" />
          </a-form-model-item>
          <a-form-model-item label="lastModifyTime" prop="lastModifyTime">
            <a-input v-model="form.lastModifyTime" />
          </a-form-model-item>
          <a-form-model-item label="lastModifyBy" prop="lastModifyBy">
            <a-input v-model="form.lastModifyBy" />
          </a-form-model-item>
          <a-form-model-item label="remark" prop="remark">
            <a-input v-model="form.remark" />
          </a-form-model-item>
          <a-form-model-item label="seq" prop="seq">
            <a-input v-model="form.seq" />
          </a-form-model-item>
          <a-form-model-item label="serialVersionUID" prop="serialVersionUID">
            <a-input v-model="form.serialVersionUID" />
          </a-form-model-item>
          <a-form-model-item label="id" prop="id">
            <a-input v-model="form.id" />
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
import {update, add} from '@/services/sys-var'
export default {
  name: "Edit",
  data() {
    return {
      title: '新增sys-var',
      visible: false,
      isAdd: false,
      submitLoading: false,
      treeData: [],
      form: {
          varCode: null,
          varName: null,
          varValue: null,
          createTime: null,
          createBy: null,
          lastModifyTime: null,
          lastModifyBy: null,
          remark: null,
          seq: null,
          serialVersionUID: null,
          id: null,
      },
      formDefault: {},
      rules: {
          varCode: [
            { required: true, message: '请输入varCode', trigger: 'blur' },
          ],
          varName: [
            { required: true, message: '请输入varName', trigger: 'blur' },
          ],
          varValue: [
            { required: true, message: '请输入varValue', trigger: 'blur' },
          ],
          createTime: [
            { required: true, message: '请输入createTime', trigger: 'blur' },
          ],
          createBy: [
            { required: true, message: '请输入createBy', trigger: 'blur' },
          ],
          lastModifyTime: [
            { required: true, message: '请输入lastModifyTime', trigger: 'blur' },
          ],
          lastModifyBy: [
            { required: true, message: '请输入lastModifyBy', trigger: 'blur' },
          ],
          remark: [
            { required: true, message: '请输入remark', trigger: 'blur' },
          ],
          seq: [
            { required: true, message: '请输入seq', trigger: 'blur' },
          ],
          serialVersionUID: [
            { required: true, message: '请输入serialVersionUID', trigger: 'blur' },
          ],
          id: [
            { required: true, message: '请输入id', trigger: 'blur' },
          ],
      },
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
    };
  },
  mounted() {
    this.formDefault = JSON.parse(JSON.stringify(this.form))
  },
  methods: {
    add() {
      this.title = '新增sys-var'
      this.form = Object.assign({}, this.formDefault);
      this.isAdd = true
      this.show()
    },
    update(data) {
      this.title = '编辑sys-var'
      this.isAdd = false
      this.form = Object.assign({}, data);
      this.show()
    },
    show() {
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
            response = await add(this.form)
          } else {
            response = await update(this.form)
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
