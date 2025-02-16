<template>
  <div style="height: 70px; width: 100%; background: linear-gradient(90deg, #8a33cf, #8f193f); line-height: 50px; border-bottom: 1px solid #ccc; display: flex; align-items: center; justify-content: space-between;">
    <div style="width: 300px; padding-left: 30px; font-weight: bold; color: white;">
      <router-link to="/user/" style="color: white; text-decoration: none;">基于Java Web的房源信息系统</router-link>
    </div>
    <el-menu
        style="flex-grow: 1; background: transparent; border: none; padding: 20px;"
        :default-active="activeMenu"
        class="el-menu-horizontal-demo"
        router
        background-color="inherit"
        text-color="#fff"
        active-text-color="#ffd04b"
        mode="horizontal"
        theme="dark">
      <el-menu-item index="/user/home">首页</el-menu-item>
      <el-menu-item index="/user/house_rent">房源信息</el-menu-item>
      <el-menu-item index="/user/house_need">租房需求信息</el-menu-item>
      <el-menu-item index="/user/house_talk">租友社区信息</el-menu-item>
      <el-menu-item index="/user/person">个人中心</el-menu-item>
      <el-menu-item index="/admin" v-if="user.role == 1">跳转后台</el-menu-item>
    </el-menu>
  </div>
</template>

<script>
export default {
  data() {
    return {
      user: JSON.parse(sessionStorage.getItem('user')) || {},
      activeMenu: this.$route.path
    };
  },
  watch: {
    '$route.path'(newPath) {
      this.activeMenu = newPath;
    }
  }
};
</script>

<style scoped>
/* 可根据需求调整样式 */
.el-menu-item.is-active {
  border-bottom: 2px solid #ffd04b; /* 自定义下划线的样式 */
}
</style>
