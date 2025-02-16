<template>
  <div
      style="height: 70px; width: 100%; background: linear-gradient(90deg, #8a33cf, #8f193f); line-height: 50px; border-bottom: 1px solid #ccc; display: flex; align-items: center; justify-content: space-between;">
    <div style="width: 300px; padding-left: 30px; font-weight: bold; color: white;">
      <p style="color: white; text-decoration: none;">基于Java Web的房源信息系统</p>
    </div>
    <el-dropdown>
      <span class="el-dropdown-link" style="color:white;margin-right: 50px">{{ user.name }}
        <i class="el-icon-arrow-down el-icon--right"></i>
      </span>
      <template #dropdown>
        <el-dropdown-menu>
          <el-dropdown-item @click="this.$router.push('/user/login')">退出登录</el-dropdown-item>
        </el-dropdown-menu>
      </template>
    </el-dropdown>
  </div>
</template>

<script>
export default {
  name: "Header",
  data() {
    return {
      user: {},
    }
  },
  created() {
    let str = sessionStorage.getItem("user") || "{}";
    this.user = JSON.parse(str);
  },
}
</script>

<style scoped>
.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.el-icon-arrow-down {
  font-size: 12px;
}
</style>