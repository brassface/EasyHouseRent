<template>
  <div id="app">
    <component :is="layoutComponent">
      <router-view />
    </component>
  </div>
</template>

<script>
import AdminLayout from './admin/layout/Layout.vue';
import UserLayout from './user/layout/Layout.vue';

export default {
  name: 'App',
  components: {
    AdminLayout,
    UserLayout
  },
  computed: {
    layoutComponent() {
      if (this.$route.path.endsWith('/login/') || this.$route.path.endsWith('/register/')) {
        return 'div';
      } else {
        if (this.$route.path.startsWith('/admin')) {
          return AdminLayout;
        } else {
          return UserLayout;
        }
      }
    }
  }
};
</script>

<style>
</style>
