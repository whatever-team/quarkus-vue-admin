<template>
  <div class="container">
    <Breadcrumb :items="['menu.system', 'menu.system.users']" />
    <a-card class="general-card" title="用户列表">
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
                <a-form-item field="username" label="用户名">
                  <a-input
                    v-model="formModel.username"
                    placeholder="请输入用户名"
                    :allow-clear="true"
                    @press-enter="search"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="username" label="昵称">
                  <a-input
                    v-model="formModel.nickName"
                    placeholder="请输入昵称"
                    :allow-clear="true"
                    @press-enter="search"
                  />
                </a-form-item>
              </a-col>
              <a-col :span="8">
                <a-form-item field="createTime" label="创建时间">
                  <a-range-picker
                    v-model="formModel.createTime"
                    style="width: 100%"
                    @change="search"
                  />
                </a-form-item>
              </a-col>
            </a-row>
          </a-form>

        </a-col>
        <a-divider style="height: 34px" direction="vertical" />
        <a-col :flex="'86px'" style="text-align: right">
          <a-space :size="18">
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
                <icon-plus />
              </template>
              新增
            </a-button>
            <a-button type="primary" status="danger" @click="onExport">
              <template #icon>
                <icon-delete />
              </template>
              删除
            </a-button>
            <a-button type="primary" @click="onExport">
              <template #icon>
                <icon-import />
              </template>
              导入
            </a-button>
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
                    :key="index"
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
        :column-resizable="true"
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
        <template #createTime="{ record }">
          {{ formatDate(record.createTime) }}
        </template>
        <template #enabled="{ record }">
          <span v-if="record.enabled === 'offline'" class="circle"></span>
          <span v-else class="circle pass"></span>
        </template>
        <template #operations="{ record }">
          <a-button type="text" size="small">
            编辑
          </a-button>
          <a-popconfirm
            content="是否确认删除?"
            :ok-loading="deleteLoading"
            @ok="onDataDelete(record)"
          >
            <a-button type="text" size="small">
              删除
            </a-button>
          </a-popconfirm>
          <a-dropdown trigger="hover">
            <a-button>更多</a-button>
            <template #content>
              <a-doption @click="onDataDetail(record)">
                重置密码
              </a-doption>
              <a-doption @click="onDataDetail(record)">
                分配角色
              </a-doption>
            </template>
          </a-dropdown>
        </template>
      </a-table>
    </a-card>

    <order-detail-modal ref="orderDetailModelRef" :data="orderDetailData" />
  </div>
</template>

<script lang="ts" setup>
import { computed, nextTick, onMounted, reactive, ref, watch } from "vue";
import { useI18n } from "vue-i18n";
import { Notification } from "@arco-design/web-vue";
import useLoading from "@/hooks/loading";
import { fetchDelete, fetchQuery } from "@/api/system/users";
import { Pagination, SortType } from "@/types/global";
import { formatDate } from "@/utils";
import type { TableColumnData } from "@arco-design/web-vue/es/table/interface";
import cloneDeep from "lodash/cloneDeep";
import Sortable from "sortablejs";
import OrderDetailModal from "../components/order-detail-modal.vue";

type SizeProps = 'mini' | 'small' | 'medium' | 'large';
  type Column = TableColumnData & { checked?: true };

  const generateFormModel = () => {
    return {
      username: '',
      nickName: '',
      createTime: [],
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
    total: 0,
    sortType: 'Descending',
    sortField: 'id',
    showJumper: true,
    showMore: true,
    showTotal: true,
    showPageSize: true,
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
      title: '编号',
      dataIndex: 'id',
    },
    {
      title: '用户名',
      dataIndex: 'username',
    },
    {
      title: '昵称',
      dataIndex: 'nickName',
    },
    {
      title: '手机号',
      dataIndex: 'phone',
    },
    {
      title: '状态',
      dataIndex: 'enabled',
      slotName: 'enabled',
      width: 90,
    },
    {
      title: '创建时间',
      dataIndex: 'createTime',
      slotName: 'createTime',
      width: 180,
    },
    {
      title: '操作',
      dataIndex: 'operations',
      slotName: 'operations',
      fixed: 'right',
      width: 220,
    },
  ]);

  // START 数据加载
  const fetchData = async (params: any) => {
    setLoading(true);
    try {
      const { data: response } = await fetchQuery(params);
      renderData.value = response.result.data;
      pagination.current = params.current;
      pagination.total = response.result.page.totalCount;
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
  onMounted(() => {
    search();
  });
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
      await fetchDelete(data.id);
      search();
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      deleteLoading.value = false;
    }
  };
  const onApplyRefund = () => {
    Notification.info({
      title: '提示',
      content: '暂未实现',
    });
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
