<template>
 <a-card>
   <a-row :gutter="32">
     <a-col :md="24" :lg="7">
       <a-card class="account">
         <a-upload
             name="file"
             list-type="picture-card"
             class="avatar-uploader"
             :headers="headers"
             :show-upload-list="false"
             :action="uploadAction('avatar')"
             :before-upload="beforeUpload"
             @change="handleChange"
         >
           <img class="avatar-image scale" v-if="user.avatar" :src="previewAction(user.avatar)" alt="avatar" />
           <div v-else>
             <a-icon :type="loading ? 'loading' : 'plus'" />
             <div class="ant-upload-text">
               Upload
             </div>
           </div>
         </a-upload>
         <a-list item-layout="horizontal">
           <a-list-item>
             <a-list-item-meta
                 :description="user.phone"
             >
               <a slot="title" href="https://www.antdv.com/">手机号</a>
               <a-icon type="mobile" slot="avatar"/>
             </a-list-item-meta>
           </a-list-item>
           <a-list-item>
             <a-list-item-meta
                 :description="user.username"
             >
               <a slot="title" href="https://www.antdv.com/">用户名</a>
               <a-icon type="user" slot="avatar" />
             </a-list-item-meta>
           </a-list-item>
           <a-list-item>
             <a-list-item-meta
                 :description="user.roleNames.join(',')"
             >
               <a slot="title" href="https://www.antdv.com/">分配角色</a>
               <a-icon type="contacts" slot="avatar" />
             </a-list-item-meta>
           </a-list-item>
           <a-list-item>
             <a-list-item-meta
                 :description="user.deptNames.join(',')"
             >
               <a slot="title" href="https://www.antdv.com/">所在部门</a>
               <a-icon type="deployment-unit" slot="avatar" />
             </a-list-item-meta>
           </a-list-item>
           <a-list-item>
             <a-list-item-meta>
               <div slot="description">
                 <a @click="passwordVisible = true;">修改密码</a> <a>开发文档</a> <a>项目源码</a>
               </div>
               <a slot="title" href="https://www.antdv.com/">安全设置</a>
               <a-icon type="security-scan" slot="avatar" />
             </a-list-item-meta>
           </a-list-item>

         </a-list>
       </a-card>
     </a-col>
     <a-col :md="24" :lg="17">
       <a-tabs default-active-key="1">
         <a-tab-pane key="1" tab="基本信息" force-render>
           <basic/>
         </a-tab-pane>
         <a-tab-pane key="2" tab="其他设置">
           <other/>
         </a-tab-pane>
       </a-tabs>
     </a-col>
   </a-row>
   <password :visible="passwordVisible" @close="handlePasswordClose"/>
 </a-card>
</template>

<script>
import {update} from "@/services/user";
import {mapGetters} from "vuex";
import Basic from "./form/basic";
import Password from "./password";
import Other from "./form/other";
import {OssMixin} from "@/mixin/OssMixin";

export default {
  name: "center",
  components: {Other, Basic, Password},
  computed: {
    ...mapGetters('account', ['user']),
  },
  mixins: [OssMixin],
  data() {
    return {
      passwordVisible: false
    };
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
          // 更新用户头像
          const updateResponse = await update({avatar: response.result.data.path})
          if (updateResponse.data.code === 'SUCCESS') {
            this.user.avatar = response.result.data.path
          } else {
            this.$message.error(updateResponse.data.message)
            return false
          }
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
    handlePasswordClose() {
      this.passwordVisible = false
    }
  }
}
</script>

<style>
.account i.anticon{
  font-size: 26px;
}
.avatar-uploader > .ant-upload , .avatar-image{
  width: 128px;
  height: 128px;
}
img.scale {
  object-fit: scale-down;
}
.ant-upload-select-picture-card i {
  font-size: 32px;
  color: #999;
}

.ant-upload-select-picture-card .ant-upload-text {
  margin-top: 8px;
  color: #666;
}
</style>
