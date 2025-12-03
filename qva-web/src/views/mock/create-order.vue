<template>
  <div class="container">
    <Breadcrumb :items="['模拟', '模拟下单']" />
    <a-form ref="formRef" layout="vertical" :model="formData">
      <a-space direction="vertical" :size="16">
        <a-card class="general-card">
          <template #title> 下单参数 </template>
          <a-row :gutter="80">
            <a-col :span="8">
              <a-form-item label="第三方商户" field="partner">
                <a-select v-model="formData.partner" placeholder="请选择">
                  <a-option value="mock">MOCK</a-option>
                  <a-option value="eleme">饿了么</a-option>
                </a-select>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="订单标题" field="subject">
                <a-input
                  v-model="formData.subject"
                  placeholder="请输入订单标题"
                >
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="订单描述" field="subject">
                <a-input v-model="formData.body" placeholder="请输入订单描述">
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="订单号" field="transactionId">
                <a-input v-model="formData.transactionId" placeholder="请输入">
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="支付金额" field="payAmount">
                <a-input v-model="formData.payAmount" placeholder="请输入">
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="异步回调地址" field="notifyUrl">
                <a-input v-model="formData.notifyUrl" placeholder="请输入">
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="同步返回地址" field="redirectUrl">
                <a-input v-model="formData.redirectUrl" placeholder="请输入">
                </a-input>
              </a-form-item>
            </a-col>
            <a-col :span="8">
              <a-form-item label="uid(openid)" field="redirectUrl">
                <a-input v-model="formData.uid" placeholder="uid就是openid">
                </a-input>
              </a-form-item>
            </a-col>
          </a-row>
        </a-card>
        <a-card class="general-card">
          <template #title> 返回结果 </template>
          <a-row :gutter="80">
            <a-col :span="24">
              <a-textarea
                v-model="resultData"
                auto-size
                placeholder="这里会自动填充"
              />
            </a-col>
          </a-row>
        </a-card>
        <a-card class="general-card">
          <template #title> 验证结果 </template>
          <a-row :gutter="80">
            <a-col :span="24">
              <a-textarea
                v-model="directPaymentResult"
                placeholder="这里会自动填充"
              />
            </a-col>
            <a-col :span="24">
              <a-textarea
                v-model="directPaymentCurl"
                auto-size
                placeholder="这里会自动填充"
              />
            </a-col>
          </a-row>
        </a-card>
      </a-space>

      <div class="actions">
        <a-space>
          <a-button> 重置 </a-button>
          <a-button type="primary" :loading="loading" @click="onSubmitClick">
            提交
          </a-button>
          <a-button type="outline" :loading="loading" @click="onPreviewResult">
            验证结果
          </a-button>
        </a-space>
      </div>
    </a-form>
  </div>
</template>

<script lang="ts" setup>
  import { ref } from 'vue';
  import { FormInstance } from '@arco-design/web-vue/es/form';
  import useLoading from '@/hooks/loading';
  import dayjs from 'dayjs';
  import { pay } from '@/api/proxy';
  import { Notification } from '@arco-design/web-vue';

  const generateFormModel = () => {
    return {
      partner: 'eleme',
      subject: '潘多拉-繁花似锦手链ZT0275',
      body: '潘多拉-繁花似锦手链ZT0275',
      transactionId: dayjs().valueOf().toString(),
      payAmount: '100',
      notifyUrl: '',
      redirectUrl: 'http://localhost:7777/version',
      uid: 'f722844542ce6d62229da275f3019cb6',
    };
  };
  const formData = ref(generateFormModel());
  const formRef = ref<FormInstance>();

  setInterval(() => {
    formData.value.transactionId = dayjs().valueOf().toString();
  }, 1000);

  const { loading, setLoading } = useLoading();
  const resultData = ref<string>('');
  const onSubmitClick = async () => {
    const res = await formRef.value?.validate();
    if (!res) {
      setLoading(true);
      try {
        const resp = await pay(formData.value.partner, formData.value);
        resultData.value = JSON.stringify(resp);
      } catch (err) {
        // you can report use errorHandler or other
      } finally {
        setLoading(false);
      }
    }
  };

  const directPaymentResult = ref<any>('');
  const directPaymentCurl = ref<string>('');
  const onPreviewResult = async () => {
    if (resultData.value) {
      try {
        const d = JSON.parse(resultData.value);
        // const resp = await directPayment(d.url, d.formData);
        // directPaymentResult.value = JSON.stringify(resp);

        const formBody = [];
        const { formData } = d;
        // eslint-disable-next-line
        for (const property in formData) {
          const encodedKey = encodeURIComponent(property);
          const encodedValue = encodeURIComponent(formData[property]);
          formBody.push(`${encodedKey}=${encodedValue}`);
        }
        const formBodyText: string = formBody.join('&');
        directPaymentCurl.value = `curl -X POST -H "Content-Type: application/x-www-form-urlencoded" -d '${formBodyText}' ${d.url}`;
        Notification.success({
          title: '提示',
          content: '已生成curl，复制到请求工具中调用',
        });
      } catch (err) {
        // you can report use errorHandler or other
      }
    } else {
      Notification.info({
        title: '提示',
        content: '请先提交',
      });
    }
  };
</script>

<script lang="ts">
  export default {
    name: 'CreateOrder',
  };
</script>

<style scoped lang="less">
  .container {
    padding: 0 20px 40px 20px;
    overflow: hidden;
  }

  .actions {
    position: fixed;
    left: 0;
    right: 0;
    bottom: 0;
    height: 60px;
    padding: 14px 20px 14px 0;
    background: var(--color-bg-2);
    text-align: right;
  }
</style>
