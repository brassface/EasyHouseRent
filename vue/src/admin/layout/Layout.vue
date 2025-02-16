<template>
  <div>
    <Header class="fixed-header"/>
    <div class="main-container">
      <Aside class="fixed-aside"/>
      <div class="content">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
import Aside from "../components/Aside.vue";
import request from "@/utils/request";

export default {
  name: "Layout",
  components: {
    Header,
    Aside
  },
  created() {
    let user = sessionStorage.getItem("user");
    if (user === null || user === undefined) {
      this.$router.push('/user/login');
    } else {
      user = JSON.parse(user);
      request.post("/api/user/login", user).then(res => {
        if (res.code === '0') {
          sessionStorage.setItem("user", JSON.stringify(res.data));
          user=res.data;
        } else {
          this.$message({
            type: "error",
            message: "身份验证失败，请稍后重试"
          });
        }
      }).catch(error => {
        console.error(error);
        this.$message({
          type: "error",
          message: "身份验证失败，请稍后重试"
        });
      });
      if (user.role !== 1) {
        this.$router.push('/user/home');
        this.$message.warning('用户权限无法访问');
      }
    }
  }
}
</script>

<style scoped>
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 10;
}

.main-container {
  display: flex;
  margin-top: 60px;
}

.fixed-aside {
  position: fixed;
  top: 60px;
  left: 0;
  bottom: 0;
  width: 200px;
  z-index: 5;
}

.content {
  margin-left: 180px;
  padding: 20px;
  flex: 1;
}
</style>
