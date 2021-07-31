<template>
  <a-card>
    <a-row :gutter="16">
      <a-col :span="5">
        <a-tree
            :checkable="false"
            :default-expand-all="true"
            :selectable="true"
            :tree-data="dept.data"
            :replace-fields="{children:'children', title:'deptName', key:'id' }"
            @select="onDeptSelect"
        />
      </a-col>
      <a-col :span="19">
        <div class="">
          <a-form layout="horizontal" :form="queryParam">
            <div class="fold">
              <a-row >
                <a-col :md="8" :sm="24" >
                  <a-form-item
                      label="用户名称"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}"
                  >
                    <a-input placeholder="请输入" v-model="queryParam.username" allow-clear @keyup.enter.native="loadData"/>
                  </a-form-item>
                </a-col>
                <a-col :md="8" :sm="24" >
                  <a-form-item
                      label="手机号"
                      :labelCol="{span: 5}"
                      :wrapperCol="{span: 18, offset: 1}"
                  >
                    <a-input placeholder="请输入" v-model="queryParam.phone" allow-clear @keyup.enter.native="loadData"/>
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
            <a-popconfirm
                title="是否确认删除?"
                @confirm="onDeleteBatch"
            >
              <a-button :disabled="selectedRowKeys.length === 0">批量删除</a-button>
            </a-popconfirm>

          </a-space>
          <a-table
              :loading="loading"
              :columns="columns"
              :data-source="data"
              :row-key="record => record.id"
              :row-selection="rowSelection"
              :pagination="pagination"
              @change="handlePageChange"
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
                <a-button size="small">删除</a-button>
              </a-popconfirm>
            </template>

          </a-table>
        </div>
      </a-col>
    </a-row>

    <edit-form ref="form" @change="onFormChange"/>
  </a-card>
</template>

<script>
import {list, remove, removeBatch} from '@/services/user'
import {list as deptList} from '@/services/dept'
import EditForm from "./Edit";

const columns = [
  {
    title: '手机号',
    dataIndex: 'phone',
    sorter: true,
  },
  {
    title: '用户名',
    dataIndex: 'username',
  },
  {
    title: '邮箱',
    dataIndex: 'email',
  },
  {
    title: '启用',
    dataIndex: 'enabled',
    scopedSlots: { customRender: 'enabled' },
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
        username: '',
        phone: ''
      },
      pagination: {
        current: 1,
        total: 0,
        pageSize: 20,
        showTotal: total => `共 ${total} 条数据`,
        showQuickJumper: true,
        showSizeChanger: true,
        pageSizeOptions: ['10', '20', '50', '100']
      },
      data: [],
      columns,
      selectedRowKeys: [],
      dept: {
        data: [],
      }
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
    this.loadDeptTree()
    this.loadData()
  },
  methods: {
    async loadData() {
      this.loading = true;
      this.queryParam.pageSize = this.pagination.pageSize
      const response = await list(this.queryParam)
      if (response.data.code === 'SUCCESS') {
        this.data = response.data.result.data
        const page = response.data.result.page
        this.pagination.current = page.pageIndex
        this.pagination.total = page.totalCount
      } else {
        this.$message.error(response.data.message)
      }
      this.loading = false
    },
    async loadDeptTree() {
      const response = await deptList({pageSize: 99999});
      if (response.data.code === 'SUCCESS') {
        this.dept.data = response.data.result.data
      } else {
        this.$message.error(response.data.message)
      }
    },
    search() {
      this.queryParam.pageIndex = 1
      this.loadData()
    },
    handlePageChange(pagination, filters, sorter) {
      if (sorter.order) {
        this.queryParam.sortField = sorter.field
        this.queryParam.sortType = sorter.order === 'ascend' ? 'Ascending' : 'Descending'
      } else {
        this.queryParam.sortField = null
        this.queryParam.sortType = null
      }
      this.queryParam.pageIndex = pagination.current
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
    },
    onDeptSelect(v) {
      this.queryParam.deptId = v[0]
      this.search()
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
