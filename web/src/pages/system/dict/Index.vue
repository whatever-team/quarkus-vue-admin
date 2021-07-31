<template>
  <a-card>
    <a-row :gutter="16">
      <a-col :span="12">
        <dict-list @rowClick="loadItems" />
      </a-col>
      <a-col :span="12">
        <item-list ref="item" :dict-code="dictCode"/>
      </a-col>
    </a-row>
  </a-card>

</template>

<script>
import DictList from "@/pages/system/dict/DictList";
import ItemList from "@/pages/system/dict/ItemList";
export default {
  name: "Index",
  components: {ItemList, DictList},
  data() {
    return {
      dictCode: '',
    }
  },
  methods: {
    loadItems(record) {
      this.dictCode = record.dictCode
      console.log(this.dictCode)
      // this.$refs.item.search()
    }
  }
}
</script>

<style scoped>

</style>
