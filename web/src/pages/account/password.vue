<template>
  <a-modal v-model="modalVisible" title="修改密码" :footer="null" @cancel="handleCancel">
    <a-form-model ref="editForm" :model="form" :rules="rules" :label-col="labelCol" :wrapper-col="wrapperCol">

      <a-form-model-item label="旧密码" prop="oldPassword">
        <a-input v-model="form.oldPassword" type="password"/>
      </a-form-model-item>

      <a-form-model-item label="新密码" prop="newPassword">
        <a-input v-model="form.newPassword" type="password"/>
      </a-form-model-item>

      <a-form-model-item label="确认密码" prop="confirmNewPassword">
        <a-input v-model="form.confirmNewPassword" type="password"/>
      </a-form-model-item>

      <a-form-model-item :wrapper-col="{ span: 14, offset: 4 }">
        <a-button type="primary" @click="onSubmit" :loading="submitLoading"  v-auth="'system:change:password'">
          提交
        </a-button>
      </a-form-model-item>

    </a-form-model>
  </a-modal>
</template>

<script>
import {updatePassword} from '@/services/user'
export default {
  name: "password",
  props: {
    visible: {
      required: false,
      defaultValue: false,
      type: Boolean
    }
  },
  watch: {
    visible(newV) {
      this.modalVisible = newV
    }
  },
  data() {
    let validatePass2 = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('Please input the password again'));
      } else if (value !== this.form.newPassword) {
        callback(new Error("Two inputs don't match!"));
      } else {
        callback();
      }
    };
    return {
      modalVisible: false,
      submitLoading: false,
      form: {
      },
      rules: {
        oldPassword: [
          { required: true, message: '请输入旧密码', trigger: 'blur' },
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
        ],
        confirmNewPassword: [
          { validator: validatePass2, trigger: 'change' },
        ],
      },
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
    };
  },
  methods: {
    show() {
      this.modalVisible = true
    },
    onSubmit() {
      this.$refs.editForm.validate(async valid => {
        if (valid) {
          this.submitLoading = true
          let response = await updatePassword(this.form)
          this.submitLoading = false
          if (response.data.code === 'SUCCESS') {
            this.$message.success(response.data.message)
          } else {
            this.$message.error(response.data.message)
          }
        } else {
          return false;
        }
      });
    },
    handleCancel() {
      this.$emit('close')
    }
  }
}
</script>

<style scoped>

</style>
