<template>
  <a-card>
    <div class="">
      <a-form layout="horizontal">
        <div class="fold">
          <a-row >
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="类"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.clazz" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="函数"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.method" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="消息"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.message" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="请求号"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-input placeholder="请输入" v-model="queryParam.serial" allow-clear @keyup.enter.native="loadData"/>
              </a-form-item>
            </a-col>
            <a-col :md="8" :sm="24" >
              <a-form-item
                  label="请求时间"
                  :labelCol="{span: 5}"
                  :wrapperCol="{span: 18, offset: 1}"
              >
                <a-range-picker
                    v-model="queryParam.createTime"
                    format="YYYY-MM-DD HH:mm:ss"
                    :show-time="{
                      defaultValue: [moment('00:00:00', 'HH:mm:ss'), moment('23:59:59', 'HH:mm:ss')],
                    }"
                />
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
      <a-table
          :loading="loading"
          :columns="columns"
          :data-source="data"
          :row-key="record => record.id"
          :pagination="pagination"
          @change="handlePageChange"
      >
        <template slot="duration" slot-scope="text">
          <a-tag color="blue">
            {{text}}ms
          </a-tag>
        </template>
        <template slot="expandedRowRender" slot-scope="record" style="margin: 0">
          <a-alert type="success" class="mgb12">
            <template slot="message">
              <p style="word-break: break-all;">{{record.parameter}}</p>
            </template>
          </a-alert>
          <a-alert type="info">
            <template slot="message">
              <p style="word-break: break-all;">{{record.response}}</p>
            </template>
          </a-alert>
        </template>
      </a-table>
    </div>
  </a-card>
</template>

<script>
import moment from 'moment';
import {list} from '@/services/businessLog'

const columns = [
  {
    title: '请求号',
    dataIndex: 'serial',
    width: 341
  },
  {
    title: '请求时间',
    dataIndex: 'createTime',
    sorter: true,
    width: 200
  },
  {
    title: '类',
    dataIndex: 'clazz',
  },
  {
    title: '方法',
    dataIndex: 'method',
  },
  {
    title: '信息',
    dataIndex: 'message',
  },
  {
    title: '耗时',
    dataIndex: 'duration',
    scopedSlots: { customRender: 'duration' },
  },
];

export default {
  name: "index",
  data() {
    return {
      loading: false,
      queryParam: {
        username: '',
        phone: '',
        createTime: []
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
      selectedRowKeys: []
    }
  },
  mounted() {
    this.loadData()
  },
  methods: {
    moment,
    async loadData() {
      this.loading = true;
      console.log(this.queryParam.createTime)
      if (this.queryParam.createTime.length === 2) {
        this.queryParam.createTimeStart = this.queryParam.createTime[0].format('YYYY-MM-DD HH:mm:ss')
        this.queryParam.createTimeEnd = this.queryParam.createTime[1].format('YYYY-MM-DD HH:mm:ss')
      }

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
