<template>
  <div class="t_whole_font">
    <Navbar v-if="!isAdminRoute"></Navbar>
    <router-view v-slot="{ Component }">
      <transition name="route" mode="out-in">
        <component :is="Component" />
      </transition>
    </router-view>
    <Footer v-if="!isAdminRoute"></Footer>
  </div>
</template>

<script setup>
import Navbar from "./components/Navbar.vue";
import Footer from "./components/Footer.vue";
import { useAuthStore } from "./stores/auth";
import { useCartStore } from "./stores/cart";
import { computed, onMounted } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const auth = useAuthStore();
const isAdminRoute = computed(() => {
  return route.path.startsWith("/admin");
});
const cartStore = useCartStore();

onMounted(() => {
  auth.getUser();
  cartStore.getCarts();
});
</script>

<style>
.t_whole_font {
  font-family: 'Plus Jakarta Sans', 'Inter', -apple-system, BlinkMacSystemFont, sans-serif;
  letter-spacing: -0.011em;
}

/* route transition */
.route-enter-from {
  opacity: 0;
  transform: translateY(6px);
}

.route-enter-active {
  transition: all 0.25s cubic-bezier(0.16, 1, 0.3, 1);
}

.route-leave-to {
  opacity: 0;
  transform: translateY(-6px);
}

.route-leave-active {
  transition: all 0.2s cubic-bezier(0.7, 0, 0.84, 0);
}
</style>
