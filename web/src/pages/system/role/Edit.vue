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

        <a-form-model-item label="角色名称" prop="roleName">
          <a-input v-model="form.roleName" />
        </a-form-model-item>

        <a-form-model-item label="角色编码" prop="roleCode">
          <a-input v-model="form.roleCode" />
        </a-form-model-item>

        <a-form-model-item label="状态" prop="enabled">
          <a-switch v-model="form.enabled" checked-children="显示" un-checked-children="隐藏"/>
        </a-form-model-item>

        <a-form-model-item label="配置权限" prop="menuIds">
          <a-button type="default" size="small" @click="checkAll" class="mgr12">全选</a-button>
          <a-button type="default" size="small" @click="unCheckAll">全不选</a-button>
          <a-tree
              v-model="form.menuIds"
              checkable
              checkStrictly
              :selectable="false"
              :tree-data="treeData"
              :replace-fields="{children:'children', title:'menuName', key:'id' }"
              @check="onTreeCheck"
          />
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
import {update, add} from '@/services/role'
import {listTree} from "@/services/menu";
export default {
  name: "Edit",
  data() {
    return {
      title: '新增角色',
      visible: false,
      isAdd: false,
      submitLoading: false,
      treeData: [],
      form: {
        id: null,
        roleName: '',
        roleCode: '',
        enabled: true,
        menuIds: []
      },
      formDefault: {},
      rules: {
        roleName: [
          { required: true, message: '请输入角色名称', trigger: 'blur' },
        ],
        enabled: [
          { required: true, message: '请选择状态', trigger: 'blur' },
        ],
        roleCode: [
          { required: true, message: '请输入角色编码', trigger: 'blur' },
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
      this.title = '新增角色'
      this.form = Object.assign({}, this.formDefault);
      this.isAdd = true
      this.show()
    },
    update(data) {
      this.title = '编辑角色'
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
      const response = await listTree({pageSize: 99999})
      if (response.data.code === 'SUCCESS') {
        this.treeData = response.data.result.data
      } else {
        this.$message.error('菜单树加载失败：' + response.data.message)
      }
    },
    onTreeCheck(checkedKeys) {
      this.form.menuIds = checkedKeys.checked
    },
    checkAll() {
      this.checkTreeData(this.treeData);
    },
    unCheckAll() {
      this.form.menuIds = []
    },
    checkTreeData(tree) {
      tree.forEach(o => {
        const id = o.id
        if (!this.form.menuIds.includes(id)) {
          this.form.menuIds.push(id)
        }
        if (o.children) {
          this.checkTreeData(o.children)
        }
      })
    }
  }
}
</script>

<style scoped>

</style>
