<template>
  <div>
    <a-form-model
        ref="ruleForm"
        :model="form"
        :rules="rules"
        :label-col="labelCol"
        :wrapper-col="wrapperCol"
    >
      <a-form-model-item label="手机号" prop="phone">
        <a-input v-model="form.phone" type="text"/>
      </a-form-model-item>
      <a-form-model-item label="用户名" prop="username">
        <a-input v-model="form.username" type="text"/>
      </a-form-model-item>
      <a-form-model-item label="邮箱" prop="email">
        <a-input v-model="form.email" />
      </a-form-model-item>
      <a-form-model-item label="简介" prop="description">
        <a-input v-model="form.description" :rows="4" />
      </a-form-model-item>
      <a-form-model-item :wrapper-col="{ span: 14, offset: 2 }">
        <a-button type="primary" @click="onSubmit" :loading="submitLoading">
          提交
        </a-button>
      </a-form-model-item>
    </a-form-model>
  </div>
</template>

<script>
import {mapGetters} from "vuex";
import {update} from "@/services/user";

export default {
  name: "basic",
  computed: {
    ...mapGetters('account', ['user']),
  },
  data() {
    return {
      submitLoading: false,
      labelCol: { span: 2 },
      wrapperCol: { span: 14 },
      form: {
        phone: '',
        username: '',
        email: '',
        description: '',
      },
      rules: {
        phone: [
          { required: true, message: '请输入手机号', trigger: 'blur' }
        ],
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' }
        ],
      },
    };
  },
  mounted() {
    this.form = {
      phone: this.user.phone,
      username: this.user.username,
      description: this.user.desc,
      email: this.user.email
    }
  },
  methods: {
    onSubmit() {
      this.$refs.ruleForm.validate(async valid => {
        if (valid) {
          this.submitLoading = true
          let response = await update(this.form)
          this.submitLoading = false
          if (response.data.code === 'SUCCESS') {
            this.$message.success(response.data.message)
            this.user.username = this.form.username
            this.user.phone = this.form.phone
            this.user.email = this.form.email
            this.user.desc = this.form.description
          } else {
            this.$message.error(response.data.message)
          }
        } else {
          return false;
        }
      });
    },
    resetForm() {
      this.$refs.ruleForm.resetFields();
    },
  },
}
</script>

<style scoped>

</style>
