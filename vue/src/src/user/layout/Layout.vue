<template>
  <Header/>
    <router-view style="flex: 1"/>
</template>

<script>
import Header from "../components/Header.vue";

export default {
  name: "Layout",
  components: {
    Header,
  }
}
</script>

<style scoped>

</style>