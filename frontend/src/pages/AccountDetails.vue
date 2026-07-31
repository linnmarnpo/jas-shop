<template>
  <div>
    <div class="mt-8 md:mt-[115px] flex justify-between items-center font-semibold">
      <h1 class="text-xl">Account Details</h1>
    </div>

    <div class="bg-white mt-6 px-4 py-4 rounded w-full flex items-center">
      <form class="w-full md:w-[70%] md:px-[5rem] py-2">
        <div class="w-full">
          <label>Display Name:</label>
          <input
            type="text"
            readonly
            class="mt-2 w-full px-4 py-3 border border-black"
            :value="authStore.user?.name || ''"
          />
        </div>
        <div class="w-full mt-4">
          <label>Email</label>
          <input
            type="text"
            readonly
            class="mt-2 w-full px-4 py-3 border border-black"
            :value="authStore.user?.email || ''"
          />
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from 'vue';
import { useAuthStore } from '../stores/auth';

const authStore = useAuthStore();

onMounted(async () => {
  if (!authStore.user) {
    await authStore.getUser();
  }
});
</script>

<style scoped></style>