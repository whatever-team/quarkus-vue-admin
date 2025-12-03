<template>
  <div class="list-wrap">
    <a-row class="list-row" :gutter="24">
      <a-col
        v-for="item in orderList"
        :key="item.id"
        :xs="12"
        :sm="12"
        :md="12"
        :lg="6"
        :xl="6"
        :xxl="6"
        class="list-col"
        style="min-height: 162px"
      >
        <CardWrap
          :loading="loading"
          :title="item.orderId"
          :description="item.subject"
          :default-value="true"
          action-type="item.actionType"
          :expires="false"
          open-txt="open-txt"
          close-txt="close-txt"
          expires-text="expires-text"
          :tag-text="item.statusText"
          expires-tag-text="expires"
          :icon="item.icon"
        >
          <template #skeleton>
            <a-skeleton :animation="true">
              <a-skeleton-line :widths="['100%', '40%', '100%']" :rows="3" />
              <a-skeleton-line :widths="['40%']" :rows="1" />
            </a-skeleton>
          </template>
        </CardWrap>
      </a-col>
    </a-row>
    <a-pagination :total="200" show-total />
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { queryOrderList } from '@/api/order';
  import useLoading from '@/hooks/loading';
  import CardWrap from './card-wrap.vue';

  const { loading, setLoading } = useLoading(true);
  const orderList = ref<any[]>(new Array(7).fill({}));
  const fetchData = async () => {
    try {
      const { data } = await queryOrderList({});
      orderList.value = data;
    } catch (err) {
      // you can report use errorHandler or other
    } finally {
      setLoading(false);
    }
  };
  fetchData();
</script>

<style scoped lang="less"></style>
