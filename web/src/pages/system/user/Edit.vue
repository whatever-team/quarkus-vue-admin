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

        <a-form-model-item label="手机号" prop="phone">
          <a-input v-model="form.phone" />
        </a-form-model-item>

        <a-form-model-item label="用户名称" prop="username">
          <a-input v-model="form.username" />
        </a-form-model-item>

        <a-form-model-item label="邮箱" prop="email">
          <a-input v-model="form.email" />
        </a-form-model-item>

        <a-form-model-item label="状态" prop="enabled">
          <a-switch v-model="form.enabled" checked-children="显示" un-checked-children="隐藏"/>
        </a-form-model-item>

        <a-form-model-item label="部门" prop="deptId">
          <a-tree-select
              v-model="form.deptId"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="deptData"
              :replace-fields="{children: 'children', title: 'deptName', key: 'id', value: 'id'}"
              placeholder="请选择"
              tree-default-expand-all
          >
          </a-tree-select>
        </a-form-model-item>

        <a-form-model-item label="角色" prop="roleIds">
          <a-select
              v-model="form.roleIds"
              mode="multiple"
              style="width: 100%"
              placeholder="Please select"
          >
            <a-select-option v-for="x in roleData" :key="x.id" :value="x.id">
              {{ x.roleName }}
            </a-select-option>
          </a-select>
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
  phone: [
    { required: true, message: '请输入手机号', trigger: 'blur' },
  ],
  username: [
    { required: true, message: '请输入用户名称', trigger: 'blur' },
  ],
  enabled: [
    { required: true, message: '请选择状态', trigger: 'blur' },
  ],
  deptId: [
    { required: true, message: '请选择部门', trigger: 'blur' },
  ],
  roleIds: [
    { required: true, message: '请选择角色', trigger: 'blur' },
  ],
};

import {update, add} from '@/services/user'
import {list} from '@/services/role'
import {list as listDept} from '@/services/dept'
export default {
  name: "Edit",
  data() {
    return {
      title: '新增用户',
      visible: false,
      isAdd: false,
      submitLoading: false,
      roleData: [],
      deptData: [],
      form: {
        id: null,
        phone: '',
        username: '',
        email: '',
        enabled: true,
        roleIds: [],
        deptId: null,
      },
      formDefault: {},
      rules,
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
    };
  },
  mounted() {
    this.formDefault = JSON.parse(JSON.stringify(this.form))
    this.loadRole()
    this.loadDept()
  },
  methods: {
    add() {
      this.title = '新增用户'
      this.form = Object.assign({}, this.formDefault);
      this.isAdd = true
      this.show()
    },
    update(data) {
      this.title = '编辑用户'
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
    async loadRole() {
      const response = await list({pageSize: 999})
      if (response.data.code === 'SUCCESS') {
        this.roleData = response.data.result.data
      } else {
        this.$message.error(response.data.message)
      }
    },
    async loadDept() {
      const response = await listDept({pageSize: 999})
      if (response.data.code === 'SUCCESS') {
        this.deptData = response.data.result.data
      } else {
        this.$message.error(response.data.message)
      }
    },
  }
}
</script>

<style scoped>

</style>
