<template>
  <a-card>
    <div class="">
      <a-form layout="horizontal">
        <div class="fold">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="varCode"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.varCode" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="varName"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.varName" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="varValue"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.varValue" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="remark"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.remark" allow-clear @keyup.enter.native="loadData"/>
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
        <a-upload
            name="file"
            :multiple="false"
            :showUploadList="false"
            :action="importAction"
            :headers="headers"
            @change="handleImport"
        >
          <a-button type="primary" :loading="importLoading">导入</a-button>
        </a-upload>
        <a-button type="primary" @click="toExport" :loading="exportLoading">导出</a-button>
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
          <editable-cell :text="text" @change="onCellChange(record.key, 'name', $event)" />
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
import {list, remove, removeBatch, exportExcel, importExcel} from '@/services/sys-var'
import EditForm from "./Edit"
import {OssMixin} from "@/mixin/OssMixin";

const columns = [
  {
    title: 'varCode',
    dataIndex: 'varCode',
  },
  {
    title: 'varName',
    dataIndex: 'varName',
  },
  {
    title: 'varValue',
    dataIndex: 'varValue',
  },
  {
    title: 'remark',
    dataIndex: 'remark',
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
  mixins: [OssMixin],
  data() {
    return {
      importAction: importExcel,
      loading: false,
      exportLoading: false,
      importLoading: false,
      queryParam: {
        roleName: ''
      },
      pagination: {
        current: 1,
        total: 20,
        pageSize: 20,
        showTotal: total => `共${total}条数据`,
        showQuickJumper: true,
        showSizeChanger: true,
        pageSizeOptions: ['10', '20', '50', '100'],
        onShowSizeChange:(current, pageSize)=>this.pagination.pageSize = pageSize
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
      this.loading = true
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
    search() {
      this.queryParam.pageIndex = 1
      this.loadData()
    },
    handlePageChange(pagination) {
      this.queryParam.pageIndex = pagination.current
      this.loadData()
    },
    toAdd() {
      this.$refs.form.add()
    },
    handleImport(info) {
      console.log(info)
      if (info.file.status === 'uploading') {
        this.importLoading = true;
        return;
      }
      if (info.file.status === 'done') {
        this.importLoading = false;
        this.handleUpload(info, async () => {
          this.search()
          return true
        }, (response) => {
          this.$message.error(response.data.message)
          return false
        })
      }
    },
    async toExport() {
      this.exportLoading = true
      const response = await exportExcel(this.queryParam)
      const { data, headers } = response
      const fileName = headers['content-disposition'].replace(/\w+;filename=(.*)/, '$1')
      const blob = new Blob([data], {type: headers['content-type']})
      let dom = document.createElement('a')
      let url = window.URL.createObjectURL(blob)
      dom.href = url
      dom.download = decodeURI(fileName)
      dom.style.display = 'none'
      document.body.appendChild(dom)
      dom.click()
      dom.parentNode.removeChild(dom)
      window.URL.revokeObjectURL(url)
      this.exportLoading = false
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
    onCellChange(key, dataIndex, value) {
      const dataSource = [...this.dataSource];
      const target = dataSource.find(item => item.key === key);
      if (target) {
        target[dataIndex] = value;
        this.dataSource = dataSource;
      }
    },
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
