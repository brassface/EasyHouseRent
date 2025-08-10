<template>
  <el-menu
      style="width: 200px; height: 100vh; background: linear-gradient(90deg, #dad8c3, #dad8c3);"
      :unique-opened="true"
      default-active="/admin/"
      class="el-menu-vertical-demo"
      router
      background-color="inherit"
      text-color="#333"
      active-text-color="#ff6600">
    <el-menu-item index="/admin/user">用户管理</el-menu-item>
    <el-menu-item index="/admin/house_rent">房源信息管理</el-menu-item>
    <el-menu-item index="/admin/house_need">租房需求管理</el-menu-item>
    <el-menu-item index="/admin/house_talk">租友帖子管理</el-menu-item>
    <el-menu-item index="/admin/statics">统计</el-menu-item>
    <el-menu-item index="/user">跳转前台</el-menu-item>
  </el-menu>

</template>

<script>
    export default {
        name: "Aside",
    }
</script>

<style scoped>

</style>
