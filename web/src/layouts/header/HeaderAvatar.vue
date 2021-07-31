<template>
  <a-dropdown>
    <div class="header-avatar" style="cursor: pointer">
      <a-avatar class="avatar" size="small" shape="circle" :src="previewAction(user.avatar)"/>
      <span class="name">{{user.username}}</span>
    </div>
    <a-menu :class="['avatar-menu']" slot="overlay">
      <a-menu-item>
        <router-link to="/account/center">
          <a-icon type="user" />
          <span> 个人中心</span>
        </router-link>
      </a-menu-item>
      <a-menu-item @click="showPassword">
        <a-icon type="safety-certificate" />
        <span>修改密码</span>
        <password ref="password"/>
      </a-menu-item>
      <a-menu-divider />
      <a-menu-item @click="logout">
        <a-icon style="margin-right: 8px;" type="poweroff" />
        <span>退出登录</span>
      </a-menu-item>
    </a-menu>
  </a-dropdown>
</template>

<script>
import {mapGetters} from 'vuex'
import {logout} from '@/services/user'
import Password from "@/pages/account/password";
import {OssMixin} from "@/mixin/OssMixin";

export default {
  name: 'HeaderAvatar',
  components: {Password},
  mixins: [OssMixin],
  computed: {
    ...mapGetters('account', ['user']),
  },
  methods: {
    logout() {
      logout()
      this.$router.push('/login')
    },
    showPassword() {
      this.$refs.password.show()
    }
  }
}
</script>

<style lang="less">
  .header-avatar{
    display: inline-flex;
    .avatar, .name{
      align-self: center;
    }
    .avatar{
      margin-right: 8px;
    }
    .name{
      font-weight: 500;
    }
  }
  .avatar-menu{
    width: 150px;
  }

</style>
