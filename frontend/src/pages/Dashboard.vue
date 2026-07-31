<template>
  <div class="my-[4rem] md:my-[6rem] px-4 py-2 w-full min-h-screen flex flex-col md:flex-row items-start">
    <div class="w-full md:w-[18%] pt-4 md:pt-[5rem] flex flex-col items-center border-b md:border-b-0 md:border-r-2 pb-4 md:pb-0">
      <div class="w-full text-center font-semibold mb-2">Hello, {{ auth.user?.name || 'User' }}</div>
      <div class="user-nav w-full md:w-[60%] flex items-center flex-col">
        <router-link
          :to="{ name: 'orders' }"
          class="mt-3 py-[15px] w-full text-center text-sm md:text-base font-medium rounded-md hover:bg-gray-800 hover:text-white duration-300 ease-in"
        >
          Orders
        </router-link>
        <router-link
          :to="{ name: 'editAccount' }"
          class="mt-3 py-[15px] w-full text-center text-sm md:text-base font-medium rounded-md hover:bg-gray-800 hover:text-white duration-300 ease-in"
        >
          Account Details
        </router-link>
        <button
          @click="auth.handleLogout()"
          class="mt-3 py-[15px] w-full text-center text-sm md:text-base font-medium rounded-md hover:bg-gray-800 hover:text-white duration-300 ease-in"
        >
          Log Out
        </button>
      </div>
    </div>

    <div class="w-full md:w-3/4">
      <div class="mt-4 md:mt-[6rem] mx-2 md:mx-5">
        <router-view></router-view>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useAuthStore } from "../stores/auth";

const auth = useAuthStore();
</script>

<style scoped>
.router-link-exact-active {
  background-color: rgb(31 41 55);
  color: white;
}
</style>
