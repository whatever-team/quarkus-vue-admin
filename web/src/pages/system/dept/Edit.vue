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

          <a-form-model-item label="上级部门" prop="parentId">
            <a-tree-select
                v-model="form.parentId"
                style="width: 100%"
                :dropdown-style="{ maxHeight: '400px', overflow: 'auto' }"
                :tree-data="treeData"
                :replace-fields="{children: 'children', title: 'deptName', key: 'id', value: 'id'}"
                placeholder="请选择"
                tree-default-expand-all
            >
            </a-tree-select>
          </a-form-model-item>

          <a-form-model-item label="部门名称" prop="deptName">
            <a-input v-model="form.deptName" />
          </a-form-model-item>

          <a-form-model-item label="负责人" prop="leader">
            <a-input v-model="form.leader" />
          </a-form-model-item>

          <a-form-model-item label="联系方式" prop="contact">
            <a-input v-model="form.contact" />
          </a-form-model-item>

          <a-form-model-item label="状态" prop="enabled">
            <a-switch v-model="form.enabled" checked-children="显示" un-checked-children="隐藏"/>
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
  import {update, add, list} from '@/services/dept'
  export default {
    name: "Edit",
    data() {
      return {
        title: '新增部门',
        visible: false,
        isAdd: false,
        submitLoading: false,
        treeData: [],
        form: {
          id: null,
          parentId: 0,
          deptName: '',
          leader: '',
          contact: '',
          enabled: true,
          seq: 0,
        },
        formDefault: {},
        rules: {
          deptName: [
            { required: true, message: '请输入部门名称', trigger: 'blur' },
          ],
          enabled: [
            { required: true, message: '请选择状态', trigger: 'blur' },
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
        this.title = '新增部门'
        this.form = Object.assign({}, this.formDefault);
        this.isAdd = true
        this.show()
      },
      update(data) {
        this.loadMenuData()
        this.title = '编辑部门'
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
          id: 0,
          deptName: '请选择',
          children: null
        }]
        const response = await list({pageSize: 99999})
        if (response.data.code === 'SUCCESS') {
          data = data.concat(response.data.result.data)
          this.treeData = data
        } else {
          this.$message.error('部门树加载失败：' + response.data.message)
        }
      },
    }
  }
  </script>

  <style scoped>

  </style>
