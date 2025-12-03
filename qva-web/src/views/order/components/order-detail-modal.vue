<script setup lang="ts">
  import { ref, toRefs, defineProps, defineExpose, watch } from 'vue';
  import { isEmptyObject } from '@/utils/is';
  import { formatDate } from '@/utils';

  const props = defineProps({
    data: Object,
  });

  const data = ref<any>({});
  const convertData = () => {
    const d: any = props.data;
    if (isEmptyObject(d)) {
      data.value = {};
    } else {
      data.value.user = [
        {
          label: '用户名',
          value: d.mobile,
        },
        {
          label: '用户OKID',
          value: d.okUid,
        },
      ];
      data.value.order = [
        {
          label: '订单号',
          value: d.orderId,
        },
        {
          label: '创建时间',
          value: formatDate(d.createTime),
        },
        {
          label: '订单状态',
          value: d.statusText,
        },
        {
          label: '总价',
          value: (d.totalAmount / 100).toFixed(2),
        },
        {
          label: '实际支付',
          value: (d.payAmount / 100).toFixed(2),
        },
        {
          label: '支付时间',
          value: formatDate(d.payTime),
        },
        {
          label: '第三方商户',
          value: d.consumerId,
        },
        {
          label: 'OK付商户',
          value: d.providerId,
        },
      ];
      data.value.product = [
        {
          label: '商品信息',
          value: d.subject,
        },
        {
          label: '商品描述',
          value: d.body,
        },
      ];
      data.value.refund = [
        {
          label: '退款单号',
          value: d.subject,
        },
        {
          label: '退款金额',
          value: d.body,
        },
      ];
    }
  };
  watch(
    () => props.data,
    (val) => {
      convertData();
    },
    { deep: false, immediate: false }
  );

  const visible = ref(false);
  const handleOk = () => {
    visible.value = false;
  };
  const handleCancel = () => {
    visible.value = false;
  };

  const open = () => {
    visible.value = true;
  };

  const close = () => {
    handleCancel();
  };

  defineExpose({
    open,
    close,
  });
</script>

<template>
  <div>
    <a-modal
      v-model:visible="visible"
      width="auto"
      draggable
      hide-cancel
      @ok="handleOk"
      @cancel="handleCancel"
    >
      <template #title> 订单详情 </template>
      <div>
        <a-space direction="vertical" size="large">
          <a-descriptions
            :data="data.user"
            title="用户信息"
            bordered
            align="right"
          />
          <a-descriptions
            :data="data.product"
            title="商品信息"
            bordered
            align="right"
          />
          <a-descriptions
            :data="data.order"
            title="订单信息"
            bordered
            align="right"
          />
          <a-descriptions
            v-if="
              data.order?.status === 'REFUNDING' ||
              data.order?.status === 'REFUNDED'
            "
            :data="data.refund"
            title="退款信息"
            bordered
            align="right"
          />
        </a-space>
      </div>
    </a-modal>
  </div>
</template>

<style scoped lang="less"></style>
