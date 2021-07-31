import {OSS} from "@/services/api";
import Cookie from 'js-cookie'

export const OssMixin = {
    data() {
        return {
            headers: {

            }
        }
    },
    mounted() {
        this.headers = {
            Authorization : Cookie.get('Authorization')
        }
        console.log(this.headers)
    },
    methods: {
        uploadAction(type) {
            return OSS + '/upload/' + type??'default'
        },
        previewAction(path) {
            if (path === undefined) {
                return ''
            }
            if (path.indexOf('http') > -1) {
                return path
            }
            return OSS + '/preview/' + path
        },
        handleUpload(info, success, fail) {
            const response = info.file.response
            if (response.code === 'SUCCESS') {
                if (success(response)) {
                    this.$message.success(response.message)
                }
            } else {
                if (fail(response)) {
                    this.$message.error(response.message)
                }
            }
        }
    }
}