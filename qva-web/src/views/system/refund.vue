<template>
  <div class="container">
    <Breadcrumb :items="['menu.list', '退款订单']" />
    <a-card class="general-card" title="退款订单">
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
                    :allow-clear="true"
                    @press-enter="search"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="orderId" label="退款单号">
                  <a-input
                    v-model="formModel.refundId"
                    placeholder="请输入退款单号"
                    :allow-clear="true"
                    @press-enter="search"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="createTime" label="退款时间">
                  <a-range-picker
                    v-model="formModel.refundTime"
                    style="width: 100%"
                    @change="search"
                  />
                </a-form-item>
              </a-col>
            </a-row>
            <a-row :gutter="16" style="margin-bottom: 16px">
              <a-col :span="24">
                <a-radio-group
                  v-model="formModel.refundStatus"
                  type="button"
                  size="large"
                  @change="search"
                >
                  <a-radio value="">全部</a-radio>
                  <a-radio
                    v-for="(x, i) in orderStatusOptions"
                    :key="i"
                    :value="x.code"
                  >{{ x.desc }}</a-radio
                  >
                </a-radio-group>
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
              {{ $t('searchTable.form.search') }}
            </a-button>
            <a-button @click="reset">
              <template #icon>
                <icon-refresh />
              </template>
              {{ $t('searchTable.form.reset') }}
            </a-button>
          </a-space>
        </a-col>
      </a-row>
      <a-divider style="margin-top: 0" />
      <a-row style="margin-bottom: 16px">
        <a-col :span="12">
          <a-space>
            <a-button type="primary" @click="onExport">
              <template #icon>
                <icon-export />
              </template>
              导出
            </a-button>
          </a-space>
        </a-col>
        <a-col
          :span="12"
          style="display: flex; align-items: center; justify-content: end"
        >
          <a-button>
            <template #icon>
              <icon-download />
            </template>
            {{ $t('searchTable.operation.download') }}
          </a-button>
          <a-tooltip :content="$t('searchTable.actions.refresh')">
            <div class="action-icon" @click="search"
              ><icon-refresh size="18"
            /></div>
          </a-tooltip>
          <a-dropdown @select="handleSelectDensity">
            <a-tooltip :content="$t('searchTable.actions.density')">
              <div class="action-icon"><icon-line-height size="18" /></div>
            </a-tooltip>
            <template #content>
              <a-doption
                v-for="item in densityList"
                :key="item.value"
                :value="item.value"
                :class="{ active: item.value === size }"
              >
                <span>{{ item.name }}</span>
              </a-doption>
            </template>
          </a-dropdown>
          <a-tooltip :content="$t('searchTable.actions.columnSetting')">
            <a-popover
              trigger="click"
              position="bl"
              @popup-visible-change="popupVisibleChange"
            >
              <div class="action-icon"><icon-settings size="18" /></div>
              <template #content>
                <div id="tableSetting">
                  <div
                    v-for="(item, index) in showColumns"
                    :key="item.dataIndex"
                    class="setting"
                  >
                    <div style="margin-right: 4px; cursor: move">
                      <icon-drag-arrow />
                    </div>
                    <div>
                      <a-checkbox
                        v-model="item.checked"
                        @change="
                          handleChange($event, item as TableColumnData, index)
                        "
                      >
                      </a-checkbox>
                    </div>
                    <div class="title">
                      {{ item.title === '#' ? '序列号' : item.title }}
                    </div>
                  </div>
                </div>
              </template>
            </a-popover>
          </a-tooltip>
        </a-col>
      </a-row>
      <a-table
        row-key="id"
        :loading="loading"
        :columns="(cloneColumns as TableColumnData[])"
        :data="renderData"
        :bordered="false"
        :size="size"
        :pagination="pagination"
        @page-change="onPageChange"
        @page-size-change="onPageSizeChange"
      >
        <template #index="{ rowIndex }">
          {{ rowIndex + 1 + (pagination.current - 1) * pagination.pageSize }}
        </template>
        <template #refundStatus="{ record }">
          {{ findOrderStatus(record.refundStatus).desc }}
        </template>
        <template #okUid="{ record }">
          {{ record.mobile }} | {{ record.okUid }}
        </template>
        <template #payAmount="{ record }">
          {{ ((record.payAmount || 0) / 100).toFixed(2) }}
        </template>
        <template #refundTime="{ record }">
          {{ formatDate(record.refundTime) }}
        </template>
        <template #createTime="{ record }">
          {{ formatDate(record.createTime) }}
        </template>
        <template #operations="{ record }">
          <a-popconfirm
            content="是否确认删除?"
            :ok-loading="deleteLoading"
            @ok="onDataDelete(record)"
          >
            <a-button v-permission="['admin']" type="text" size="small">
              删除
            </a-button>
          </a-popconfirm>
          <a-dropdown trigger="hover">
            <a-button>更多</a-button>
            <template #content>
              <a-doption v-permission="['admin']" @click="onDataDetail(record)">
                详情
              </a-doption>
              <a-doption v-permission="['admin']"> 立即退款 </a-doption>
              <a-doption v-permission="['admin']"> 拒绝退款 </a-doption>
            </template>
          </a-dropdown>
        </template>
      </a-table>
    </a-card>

    <order-detail-modal ref="orderDetailModelRef" :data="orderDetailData" />
  </div>
</template>

<script lang="ts" setup>
  import { computed, ref, reactive, watch, nextTick } from 'vue';
  import { useI18n } from 'vue-i18n';
  import { Notification } from '@arco-design/web-vue';
  import useLoading from '@/hooks/loading';
  import { queryRefundList, deleteRefund } from '@/api/order';
  import { queryProviderList } from '@/api/provider';
  import { queryConsumerList } from '@/api/consumer';
  import { queryByType } from '@/api/enum';
  import { Pagination, Enum } from '@/types/global';
  import { formatDate } from '@/utils';
  import type { TableColumnData } from '@arco-design/web-vue/es/table/interface';
  import cloneDeep from 'lodash/cloneDeep';
  import Sortable from 'sortablejs';
  import OrderDetailModal from './components/order-detail-modal.vue';

  type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  type Column = TableColumnData & { checked?: true };

  const generateFormModel = () => {
    return {
      orderId: '',
      refundId: '',
      okUid: '',
      refundStatus: '',
      consumerId: '',
      providerId: '',
      createTime: [],
      refundTime: [],
    };
  };
  const { loading, setLoading } = useLoading(true);
  const { t } = useI18n();
  const renderData = ref<any[]>([]);
  const formModel = ref(generateFormModel());
  const cloneColumns = ref<Column[]>([]);
  const showColumns = ref<Column[]>([]);

  const size = ref<SizeProps>('medium');

  const basePagination: Pagination = {
    current: 1,
    pageSize: 20,
  };
  const pagination = reactive({
    ...basePagination,
  });
  const densityList = computed(() => [
    {
      name: t('searchTable.size.mini'),
      value: 'mini',
    },
    {
      name: t('searchTable.size.small'),
      value: 'small',
    },
    {
      name: t('searchTable.size.medium'),
      value: 'medium',
    },
    {
      name: t('searchTable.size.large'),
      value: 'large',
    },
  ]);
  const columns = computed<TableColumnData[]>(() => [
    {
      title: '#',
      dataIndex: 'index',
      slotName: 'index',
    },
    {
      title: '支付单号',
      dataIndex: 'orderId',
    },
    {
      title: '退款单号',
      dataIndex: 'refundId',
    },
    {
      title: '用户昵称 | ID',
      dataIndex: 'okUid',
      slotName: 'okUid',
    },
    {
      title: '商品信息',
      dataIndex: 'subject',
      slotName: 'subject',
    },
    {
      title: '实际支付',
      dataIndex: 'payAmount',
      slotName: 'payAmount',
    },
    {
      title: '退款状态',
      dataIndex: 'refundStatus',
      slotName: 'refundStatus',
    },
    {
      title: '发起退款时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
    },
    {
      title: '退款时间',
      dataIndex: 'refundTime',
      slotName: 'refundTime',
    },
    {
      title: '操作',
      dataIndex: 'operations',
      slotName: 'operations',
    },
  ]);

  // START 订单状态属性
  const orderStatusOptions = ref<Enum[]>([]);
  const fetchOrderStatusOptions = async () => {
    try {
      const { data } = await queryByType('RefundStatus');
      orderStatusOptions.value = data;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      // do nothing
    }
  };
  fetchOrderStatusOptions();

  const findOrderStatus = (status: string) => {
    const one = orderStatusOptions.value.find((opt: Enum) => {
      return opt.code === status;
    });
    return (
      one || {
        code: '',
        desc: '',
      }
    );
  };
  // END 订单状态属性

  // 第三方商户
  const consumerOptions = ref<any[]>([]);
  const fetchConsumerOptions = async () => {
    try {
      const {
        data: { records },
      } = await queryConsumerList({ current: 1, pageSize: 999 });
      consumerOptions.value = records;
    } catch (err) {
      // you can report use errorHandler or other
    }
  };
  fetchConsumerOptions();
  // 第三方商户

  // OK付商户
  const providerOptions = ref<any[]>([]);
  const fetchProviderOptions = async () => {
    try {
      const {
        data: { records },
      } = await queryProviderList({ current: 1, pageSize: 999 });
      providerOptions.value = records;
    } catch (err) {
      // you can report use errorHandler or other
    }
  };
  fetchProviderOptions();
  // OK付商户

  // START 数据加载
  const fetchData = async (params: any = { current: 1, pageSize: 20 }) => {
    setLoading(true);
    try {
      const {
        data: { records, totalRow },
      } = await queryRefundList(params);
      renderData.value = records;
      pagination.current = params.current;
      pagination.total = totalRow;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };
  const search = () => {
    fetchData({
      ...basePagination,
      ...formModel.value,
    } as unknown as any);
  };
  const onPageChange = (current: number) => {
    fetchData({ ...basePagination, current });
  };
  const onPageSizeChange = (pageSize: number) => {
    fetchData({ ...basePagination, pageSize, current: 1 });
  };
  fetchData();
  // END 数据加载

  const onExport = () => {
    Notification.info({
      title: '提示',
      content: '暂未实现',
    });
  };

  const reset = () => {
    formModel.value = generateFormModel();
  };

  const handleSelectDensity = (
    val: string | number | Record<string, any> | undefined,
    e: Event
  ) => {
    size.value = val as SizeProps;
  };

  const handleChange = (
    checked: boolean | (string | boolean | number)[],
    column: Column,
    index: number
  ) => {
    if (!checked) {
      cloneColumns.value = showColumns.value.filter(
        (item) => item.dataIndex !== column.dataIndex
      );
    } else {
      cloneColumns.value.splice(index, 0, column);
    }
  };

  const exchangeArray = <T extends Array<any>>(
    array: T,
    beforeIdx: number,
    newIdx: number,
    isDeep = false
  ): T => {
    const newArray = isDeep ? cloneDeep(array) : array;
    if (beforeIdx > -1 && newIdx > -1) {
      // 先替换后面的，然后拿到替换的结果替换前面的
      newArray.splice(
        beforeIdx,
        1,
        newArray.splice(newIdx, 1, newArray[beforeIdx]).pop()
      );
    }
    return newArray;
  };

  const popupVisibleChange = (val: boolean) => {
    if (val) {
      nextTick(() => {
        const el = document.getElementById('tableSetting') as HTMLElement;
        const sortable = new Sortable(el, {
          onEnd(e: any) {
            const { oldIndex, newIndex } = e;
            exchangeArray(cloneColumns.value, oldIndex, newIndex);
            exchangeArray(showColumns.value, oldIndex, newIndex);
          },
        });
      });
    }
  };

  // START 表格操作
  const orderDetailModelRef = ref<any>(null);
  const orderDetailData = ref();
  const onDataDetail = (data: any) => {
    orderDetailData.value = data;
    orderDetailModelRef.value?.open();
  };
  const deleteLoading = ref(false);
  const onDataDelete = async (data: any) => {
    deleteLoading.value = true;
    try {
      await deleteRefund(data.orderId);
      search();
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      deleteLoading.value = false;
    }
  };
  // END 表格操作

  watch(
    () => columns.value,
    (val) => {
      cloneColumns.value = cloneDeep(val);
      cloneColumns.value.forEach((item, index) => {
        item.checked = true;
      });
      showColumns.value = cloneDeep(cloneColumns.value);
    },
    { deep: true, immediate: true }
  );
</script>

<script lang="ts">
  export default {
    name: 'SearchTable',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 20px 20px;
  }
  :deep(.arco-table-th) {
    &:last-child {
      .arco-table-th-item-title {
        margin-left: 16px;
      }
    }
  }
  .action-icon {
    margin-left: 12px;
    cursor: pointer;
  }
  .active {
    color: #0960bd;
    background-color: #e3f4fc;
  }
  .setting {
    display: flex;
    align-items: center;
    width: 200px;
    .title {
      margin-left: 12px;
      cursor: pointer;
    }
  }
</style>
