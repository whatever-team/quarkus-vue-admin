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
        <a-form-model-item label="菜单类型" prop="type">
          <a-radio-group v-model="form.type">
            <a-radio :value="0">
              目录
            </a-radio>
            <a-radio :value="1">
              菜单
            </a-radio>
            <a-radio :value="2">
              按钮/权限
            </a-radio>
          </a-radio-group>
        </a-form-model-item>

        <a-form-model-item label="上级菜单" prop="parentId" v-show="form.type !== 0">
          <a-tree-select
              v-model="form.parentId"
              style="width: 100%"
              :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
              :tree-data="treeData"
              :replace-fields="{children: 'children', title: 'menuName', key: 'id', value: 'id'}"
              placeholder="请选择"
              tree-default-expand-all
          >
            <span v-if="key === '01231231'" slot="title" slot-scope="{ key }" style="color: #08c">
              根节点
            </span>
          </a-tree-select>
        </a-form-model-item>

        <a-form-model-item label="菜单名称" prop="menuName">
          <a-input v-model="form.menuName" />
        </a-form-model-item>

        <a-form-model-item label="路由名称" prop="routerName">
          <a-input v-model="form.routerName" />
        </a-form-model-item>

        <a-form-model-item label="权限标识">
          <a-input v-model="form.permission" />
        </a-form-model-item>

        <a-form-model-item label="图标">
          <a-input v-model="form.icon" placeholder="andv图标"/>
        </a-form-model-item>

        <a-form-model-item label="状态" prop="enabled">
          <a-switch v-model="form.enabled" checked-children="显示" un-checked-children="隐藏"/>
        </a-form-model-item>

        <a-form-model-item label="打开方式" prop="target">
          <a-radio-group v-model="form.target">
            <a-radio :value="0">
              内部页面
            </a-radio>
            <a-radio :value="1">
              外部链接
            </a-radio>
          </a-radio-group>
        </a-form-model-item>

        <a-form-model-item label="链接地址" v-show="form.target === 1">
          <a-input v-model="form.path" />
        </a-form-model-item>

        <a-form-model-item label="序号" prop="seq">
          <a-input-number v-model="form.seq" />
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
import {update, add, listTree} from '@/services/menu'
export default {
  name: "Edit",
  data() {
    return {
      title: '新增菜单',
      visible: false,
      isAdd: false,
      submitLoading: false,
      treeData: [],
      form: {
        id: null,
        type: 0,
        target: 0,
        menuName: '',
        routerName: '',
        path: '',
        permission: '',
        icon: '',
        enabled: true,
        seq: 0,
        parentId: 1000000000000000
      },
      formDefault: {},
      rules: {
        menuName: [
          { required: true, message: '请输入菜单名称', trigger: 'blur' },
        ],
        enabled: [
          { required: true, message: '请选择状态', trigger: 'blur' },
        ],
        routerName: [
          { required: true, message: '请输入路由名称', trigger: 'blur' },
        ],
        target: [
          { required: true, message: '请选择打开方式', trigger: 'blur' },
        ],
        type: [
          { required: true, message: '请选择菜单类型', trigger: 'blur' },
        ],
        seq: [
          { required: true, message: '请输入序号', trigger: 'blur' },
        ],
      },
      labelCol: { span: 4 },
      wrapperCol: { span: 14 },
    };
  },
  mounted() {
    this.formDefault = JSON.parse(JSON.stringify(this.form))
    this.loadMenuData()
  },
  methods: {
    add() {
      this.loadMenuData()
      this.title = '新增菜单'
      this.form = Object.assign({}, this.formDefault);
      this.isAdd = true
      this.show()
    },
    update(data) {
      this.loadMenuData()
      this.title = '编辑菜单'
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
    async loadMenuData() {
      let data = [{
        id: 1000000000000000,
        menuName: '请选择',
        children: null
      }]
      const response = await listTree({pageSize: 99999})
      if (response.data.code === 'SUCCESS') {
        data = data.concat(response.data.result.data)
        this.treeData = data
      } else {
        this.$message.error('菜单树加载失败：' + response.data.message)
      }
    },
  }
}
</script>

<style scoped>

</style>
