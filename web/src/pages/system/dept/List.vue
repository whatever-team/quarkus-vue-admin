<template>
  <a-card>
    <div class="">
      <a-form layout="horizontal">
        <div class="fold">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="部门名称"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.deptName" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
          </a-row>
        </div>
        <span style="float: right; margin-top: 3px;">
          <a-button type="primary" @click="search">查询</a-button>
          <a-button style="margin-left: 8px">重置</a-button>
        </span>
      </a-form>
    </div>
    <div>
      <a-space class="operator">
        <a-button type="primary" @click="toAdd">新建</a-button>
        <a-button :disabled="selectedRowKeys.length === 0" @click="onDeleteBatch" v-auth="'system:delete'">批量删除</a-button>
      </a-space>
      <a-table
          :loading="loading"
          :columns="columns"
          :data-source="data"
          :row-key="record => record.id"
          :row-selection="rowSelection"
          :default-expand-all-rows="true"
      >
        <template slot="enabled" slot-scope="text">
          <a-tag :color="text ? 'blue' : 'pink'">
            {{text ? '显示' : '隐藏'}}
          </a-tag>
        </template>
        <template slot="operation" slot-scope="text, record">
          <a-button class="mgr12" size="small" @click="toUpdate(record)">编辑</a-button>
          <a-popconfirm
              title="是否确认删除?"
              @confirm="() => onDelete(record.id)"
          >
            <a-button size="small" v-auth="'system:delete'">删除</a-button>
          </a-popconfirm>
        </template>

      </a-table>
    </div>
    <edit-form ref="form" @change="onFormChange"/>
  </a-card>
</template>

<script>
import {list, remove, removeBatch} from '@/services/dept'
import EditForm from "@/pages/system/dept/Edit";

const columns = [
  {
    title: '部门名称',
    dataIndex: 'deptName',
  },

  {
    title: '负责人',
    dataIndex: 'leader',
  },
  {
    title: '联系方式',
    dataIndex: 'contact',
  },
  {
    title: '启用',
    dataIndex: 'enabled',
    scopedSlots: { customRender: 'enabled' },
  },
  {
    title: '顺序',
    dataIndex: 'seq',
  },
  {
    title: '操作',
    dataIndex: 'id',
    scopedSlots: { customRender: 'operation' },
  },
];

export default {
  name: "index",
  components: {EditForm},
  data() {
    return {
      loading: false,
      queryParam: {
        deptName: '',
        pageSize: 999
      },
      data: [],
      columns,
      selectedRowKeys: []
    }
  },
  computed: {
    rowSelection() {
      return {
        onChange: (selectedRowKeys) => {
          this.selectedRowKeys = selectedRowKeys
        },
      }
    },
  },
  mounted() {
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true;
      const response = await list(this.queryParam)
      if (response.data.code === 'SUCCESS') {
        this.data = response.data.result.data
      } else {
        this.$message.error(response.data.message)
      }
      this.loading = false
    },
    search() {
      this.queryParam.pageIndex = 1
      this.loadData()
    },
    toAdd() {
      this.$refs.form.add()
    },
    toUpdate(row) {
      this.$refs.form.update(row)
    },
    async onDelete(id) {
      const response = await remove(id)
      if (response.data.code === 'SUCCESS') {
        await this.loadData()
      } else {
        this.$message.error(response.data.message)
      }
    },
    async onDeleteBatch() {
      const response = await removeBatch(this.selectedRowKeys.join(','))
      if (response.data.code === 'SUCCESS') {
        this.selectedRowKeys = []
        await this.loadData()
      } else {
        this.$message.error(response.data.message)
      }
    },
    onFormChange() {
      this.loadData()
    }
  }
}
</script>

<style lang="less" scoped>
.search{
  margin-bottom: 54px;
}
.fold{
  width: calc(100% - 216px);
  display: inline-block
}
.operator{
  margin-bottom: 18px;
}
@media screen and (max-width: 900px) {
  .fold {
    width: 100%;
  }
}
</style>
