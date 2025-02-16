<template>
  <div style="width: 100%;height: 100vh; display: flex;justify-content: center;align-items: center; background: linear-gradient(340deg, #68b4cc, #c69f3d);">
    <div style="width: 320px; height: 400px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.7); display: flex; flex-direction: column; justify-content: center; align-items: center; border-radius: 15px; background-color: #5926e6;">
      <h1 style="text-align: center; margin-top: 20px; font-size: 24px; color: #1c8f35; font-family: Arial; font-weight: bold;">欢迎使用</h1>
      <h2 style="text-align: center; margin-top: 20px; font-size: 18px; color: #5aa5fc; font-family: Arial;">EasyHouseRent</h2>
    </div>
    <div style="width: 300px; height: 400px; box-shadow: 0 5px 15px rgba(0, 0, 0, 0.7); display: flex; justify-content: center; align-items: center; border-radius: 15px; background-color: #fa0b8e;">
      <el-form :model="form" size="normal" style="width: 260px;" ref="form" :rules="rules">
        <div style="width: 100%; height: 40px; margin-top: 10px; line-height: 40px; text-align: center;">
          <span style="color: #0a2979; font-weight: bold; font-size: 28px;">登录</span>
        </div>
        <el-form-item style="margin-top: 30px;" prop="username">
          <el-input v-model="form.username" prefix-icon="el-icon-user-solid" placeholder="请输入用户名" style="border-radius: 10px; border: 1px solid #30bcde;"></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="form.password" prefix-icon="el-icon-lock" placeholder="请输入密码" show-password style="border-radius: 10px; border: 1px solid #919aeb;"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; height: 45px; border-radius: 10px; background: linear-gradient(26deg, #7a86f5, #da5b4d); color: white;" @click="login">登录</el-button>
        </el-form-item>
        <el-form-item>
          <el-button style="width: 100%; height: 45px; border-radius: 10px; background: linear-gradient(26deg, #fbbd4c, #ff6f61); color: white;" @click="toRegister">注册</el-button>
        </el-form-item>
        </el-form>
    </div>
  </div>
</template>

<script>
import request from "../../utils/request";

export default {
  name: "Login",
  data(){
    return{
      form:{},
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
        ],
      }
    }
  },
  created() {
    sessionStorage.removeItem("user");
  },
  mounted() {
    window.addEventListener("keydown", this.handleKeyPress);
  },
  beforeDestroy() {
    window.removeEventListener("keydown", this.handleKeyPress);
    document.body.style.overflow = "";
  },
  methods:{
    toRegister() {
      this.$router.push("/user/register")
    },
    login() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          request.post("/api/user/login", this.form).then(res => {
            if (res.code === '0') {
              this.$message({
                type: "success",
                message: "登录成功"
              });
              sessionStorage.setItem("user", JSON.stringify(res.data));
              this.$router.push("/user/");
            } else {
              this.$message({
                type: "error",
                message: res.msg
              });
            }
          }).catch(error => {
            console.error(error);
            this.$message({
              type: "error",
              message: "登录失败，请稍后重试"
            });
          });
        } else {
          console.log('error submit!!');
          return false;
        }
      });
    },
    handleKeyPress(event) {
      if (event.key === "Enter") {
        this.login();
      }
    },
  }
}
</script>

<style scoped>
</style>
