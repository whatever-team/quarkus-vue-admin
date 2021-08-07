<template>
  <a-card>

    <a-button type="primary" v-auth:permission="'permission-test-btn'" class="mgr12">
      v-auth:permission="'permission-test-btn'" (有权限)
    </a-button>

    <a-button type="primary" v-auth:permission="'nothing'" class="mgr12">
      v-auth:permission="'nothing'" (没有权限)
    </a-button>

    <a-button type="primary" v-auth="'permission-test-btn'" class="mgr12">
      v-auth="'permission-test-btn'" (有权限，精简写法)
    </a-button>

    <a-button type="primary" v-auth="'nothing'">
      v-auth="'nothing'" (没有权限，精简写法)
    </a-button>

  </a-card>
</template>

<script>

export default {
  name: "Permission",
  data() {
    return {

    }
  },
  methods: {

  }
}
</script>

<style scoped>

</style>