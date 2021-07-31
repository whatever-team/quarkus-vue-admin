<template>
  <exception-page home-route="/dashboard/workplace" :style="`min-height: ${minHeight}`" type="dev" />
</template>

<script>
import ExceptionPage from '@/components/exception/ExceptionPage'
import {mapState} from 'vuex'
export default {
  name: 'Dev',
  components: {ExceptionPage},
  computed: {
    ...mapState('setting', ['pageMinHeight']),
    minHeight() {
      return this.pageMinHeight ? this.pageMinHeight + 'px' : '100vh'
    }
  }
}
</script>

<style scoped lang="less">
</style>
