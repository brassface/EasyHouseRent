<template>
  <Header/>
  <div style="display: flex">
    <Aside/>
    <router-view style="flex: 1"/>
  </div>
</template>

<script>
import Header from "../components/Header.vue";
import Aside from "../components/Aside.vue";

export default {
  name: "Layout",
  components: {
    Header,
    Aside
  }
}
</script>

<style scoped>

</style>