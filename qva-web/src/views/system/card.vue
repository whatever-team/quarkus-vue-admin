<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', 'menu.list.orderList']" />
    <a-row :gutter="20" align="stretch">
      <a-col :span="24">
        <a-card class="general-card" title="订单列表">
          <a-row>
            <a-col :flex="1">
              <a-form
                :model="formModel"
                :label-col-props="{ span: 6 }"
                :wrapper-col-props="{ span: 18 }"
                label-align="left"
              >
                <a-row :gutter="16">
                  <a-col :span="8">
                    <a-form-item field="orderId" label="订单号">
                      <a-input
                        v-model="formModel.orderId"
                        placeholder="请输入订单号"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item field="subject" label="商品名称">
                      <a-input
                        v-model="formModel.subject"
                        placeholder="请输入商品名称"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item field="body" label="描述">
                      <a-input
                        v-model="formModel.body"
                        placeholder="请输入描述"
                      />
                    </a-form-item>
                  </a-col>
                  <a-col :span="8">
                    <a-form-item field="createTime" label="创建时间">
                      <a-range-picker
                        v-model="formModel.createTime"
                        style="width: 100%"
                      />
                    </a-form-item>
                  </a-col>
                </a-row>
              </a-form>
            </a-col>
            <a-divider style="height: 84px" direction="vertical" />
            <a-col :flex="'86px'" style="text-align: right">
              <a-space direction="vertical" :size="18">
                <a-button type="primary" @click="search">
                  <template #icon>
                    <icon-search />
                  </template>
                  查询
                </a-button>
                <a-button @click="reset">
                  <template #icon>
                    <icon-refresh />
                  </template>
                  重置
                </a-button>
              </a-space>
            </a-col>
          </a-row>
          <a-row justify="space-between">
            <a-col :span="24">
              <a-tabs :default-active-tab="1" type="rounded">
                <a-tab-pane
                  v-for="x in orderStatusList"
                  :key="x.code"
                  :title="x.name"
                >
                  <Orders />
                </a-tab-pane>
              </a-tabs>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script lang="ts" setup>
  import { computed, ref, reactive } from 'vue';
  import type { SelectOptionData } from '@arco-design/web-vue/es/select/interface';
  import useLoading from '@/hooks/loading';
  import { useI18n } from 'vue-i18n';
  import Orders from './components/orders.vue';

  const { t } = useI18n();

  const orderStatusList: any = ref([
    {
      code: '',
      name: '全部',
    },
    {
      code: '0',
      name: '未支付',
    },
    {
      code: '2',
      name: '待发货',
    },
    {
      code: '3',
      name: '待收货',
    },
    {
      code: '4',
      name: '交易完成',
    },
    {
      code: '5',
      name: '已退款',
    },
    {
      code: '1',
      name: '已取消',
    },
  ]);

  const generateFormModel = () => {
    return {
      orderId: '',
      subject: '',
      body: '',
      okUid: '',
      status: '',
      consumerId: '',
      providerId: '',
      createTime: [],
    };
  };
  const formModel = ref(generateFormModel());

  const { loading, setLoading } = useLoading(true);
  // console.log(loading);

  const search = () => {
    console.log('search');
    
  };

  const reset = () => {
    formModel.value = generateFormModel();
  };
</script>

<script lang="ts">
  export default {
    name: 'Card',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
    :deep(.arco-list-content) {
      overflow-x: hidden;
    }

    :deep(.arco-card-meta-title) {
      font-size: 14px;
    }
  }
  :deep(.arco-list-col) {
    display: flex;
    flex-direction: row;
    flex-wrap: wrap;
    justify-content: space-between;
  }

  :deep(.arco-list-item) {
    width: 33%;
  }

  :deep(.block-title) {
    margin: 0 0 12px 0;
    font-size: 14px;
  }
  :deep(.list-wrap) {
    // min-height: 140px;
    .list-row {
      align-items: stretch;
      .list-col {
        margin-bottom: 16px;
      }
    }
    :deep(.arco-space) {
      width: 100%;
      .arco-space-item {
        &:last-child {
          flex: 1;
        }
      }
    }
  }
</style>
