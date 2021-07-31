import {listByTypeCode} from "@/services/dict";

export const DictMixin = {
    data() {
        return {

        }
    },
    computed: {

    },
    mounted() {

    },
    methods: {
        dictItemAction(dictCode, itemCode) {
            const key = this.genDictKey(dictCode)
            const row = this[key] && this[key].filter(item => item.itemCode === itemCode + '')
            return row && row[0] && row[0].itemName
        },
        dictAction(dictCode) {
            const key = this.genDictKey(dictCode)
            return this[key]
        },
        async loadDict(dictCode) {
            if (!(dictCode instanceof Array)) {
                dictCode = [dictCode]
            }
            for (const item of dictCode) {
                const response = await listByTypeCode(item)
                if (response.data.code === 'SUCCESS') {
                    const key = this.genDictKey(item)
                    this[key] = response.data.result.data
                }
            }
        },
        genDictKey(dictCode) {
            return `${dictCode}Options`
        }
    }
}