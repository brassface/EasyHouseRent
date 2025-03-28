<template>
  <div>
    <Header class="fixed-header"/>
    <router-view style="flex: 1;margin-top: 60px"/>
    <div class="back-to-top-container">
      <button class="back-to-top-button" @click="scrollToTop">
        <span class="arrow">&#8593;</span>
        <span class="text">回到顶部</span>
      </button>
    </div>
  </div>
</template>

<script>
import Header from "../components/Header.vue";

export default {
  name: "Layout",
  components: {
    Header,
  },
  created() {
    if (sessionStorage.getItem('user') === null) {
      this.$router.push("/user/login/");
    } else {
      let user = JSON.parse(sessionStorage.getItem('user'));
      if (user.image === null || user.phone === null || user.address === null) {
        this.$message.warning('请到个人中心完善信息');
      }
    }
  },
  methods: {
    scrollToTop() {
      window.scrollTo({ top: 0, behavior: 'smooth' });
    },
  },
}
</script>

<style scoped>
.fixed-header {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  z-index: 1000;
}

.back-to-top-container {
  position: fixed;
  bottom: 30px;
  left: 50%;
  transform: translateX(-50%) translateX(400px);
  z-index: 1000;
}

.back-to-top-button {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #6c757d;
  border: none;
  border-radius: 50%;
  width: 60px;
  height: 60px;
  color: white;
  font-size: 18px;
  cursor: pointer;
  transition: background-color 0.3s, transform 0.3s;
}

.back-to-top-button:hover {
  background-color: #5a6268;
  transform: scale(1.2);
}

.arrow {
  font-size: 24px;
}

.text {
  font-size: 10px;
  margin-top: 5px;
}
</style>
