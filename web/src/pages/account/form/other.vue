<template>
  <div>
    <a-list item-layout="horizontal" :data-source="data">
      <a-list-item slot="renderItem" slot-scope="item">
        <a slot="actions">{{item.action}}</a>
        <a-list-item-meta :description="item.description">
          <span slot="title">{{ item.title }}</span>
        </a-list-item-meta>
      </a-list-item>
    </a-list>
  </div>
</template>

<script>
const data = [
  {
    title: '密保手机',
    description: '已绑定手机 : 130****0000',
    action: '修改'
  },
  {
    title: '密保问题',
    description: '未设置密保问题，密保问题可有效保护账户安全',
    action: '设置'
  },
  {
    title: '备用邮箱',
    description: '已绑定邮箱 : ant***sign.com',
    action: '修改'
  },
  {
    title: 'MFA 设备',
    description: '未绑定 MFA 设备，绑定后，可以进行二次确认',
    action: '绑定'
  },
];
export default {
  name: "other",
  data() {
    return {
      data,
    };
  },
}
</script>

<style scoped>

</style>
