<template>
  <div class="pt-[110px] pb-16 min-h-screen flex items-start justify-center bg-white px-4">
    <div class="w-full max-w-md mx-auto my-4">
      <div class="flex items-center justify-between w-full text-2xl font-bold border-b border-black">
        <router-link
          :to="{ name: 'Register' }"
          :class="{
            'border-black border-t border-l border-r border-b-0 text-black': isRegisterRoute,
            'text-gray-400 border-b border-black': !isRegisterRoute,
          }"
          class="w-1/2 py-3 text-center transition-all"
        >
          Register
        </router-link>
        <router-link
          :to="{ name: 'Login' }"
          :class="{
            'border-black border-t border-l border-r border-b-0 text-black': isLoginRoute,
            'text-gray-400 border-b border-black': !isLoginRoute,
          }"
          class="w-1/2 py-3 text-center transition-all"
        >
          Log In
        </router-link>
      </div>
      <div class="mt-8 w-full">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed } from "vue";
import { useRoute } from "vue-router";

const route = useRoute();
const isLoginRoute = computed(() => route.name === "Login" || route.name === "LoginRegister");
const isRegisterRoute = computed(() => route.name === "Register");
</script>

<style scoped></style>

