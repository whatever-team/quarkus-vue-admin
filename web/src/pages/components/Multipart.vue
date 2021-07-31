<template>
  <a-card>
    <a-alert message="不定期清理文件，请知悉" type="info" show-icon /> <br>
    <a-upload
        name="file"
        list-type="picture-card"
        class="avatar-uploader"
        :headers="headers"
        :show-upload-list="false"
        :action="uploadAction('test')"
        :before-upload="beforeUpload"
        @change="handleChange"
    >
      <img class="avatar-image scale" v-if="picture !== ''" :src="previewAction(picture)" alt="picture" />
      <div v-else>
        <a-icon :type="loading ? 'loading' : 'plus'" />
        <div class="ant-upload-text">
          Upload
        </div>
      </div>
    </a-upload>

    <a-button type="primary" icon="download" @click="handleDownload">
      Download
    </a-button>
  </a-card>
</template>

<script>
import {OssMixin} from "@/mixin/OssMixin";

export default {
  name: "Multipart",
  mixins: [OssMixin],
  data() {
    return {
      loading: false,
      picture: ''
    }
  },
  methods: {
    handleChange(info) {
      if (info.file.status === 'uploading') {
        this.loading = true;
        return;
      }
      if (info.file.status === 'done') {
        this.loading = false;
        this.handleUpload(info, async (response) => {
          this.picture = response.result.data.path
          return true
        }, (response) => {
          this.$message.error(response.data.message)
          return false
        })
      }
    },
    beforeUpload(file) {
      const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
      if (!isJpgOrPng) {
        this.$message.error('You can only upload JPG file!');
      }
      const isLt2M = file.size / 1024 / 1024 < 2;
      if (!isLt2M) {
        this.$message.error('Image must smaller than 2MB!');
      }
      return isJpgOrPng && isLt2M;
    },
    handleDownload() {
      if (this.picture === '') {
        this.$message.error('请先上传，再测试下载')
        return
      }
      const url = this.previewAction(this.picture)
      window.open(url)
    }
  }
}
</script>

<style scoped>

</style>