<template>
  <div class="pt-[110px] pb-16 min-h-[calc(100vh-200px)] flex items-center justify-center bg-white px-4">
    <div class="w-full max-w-md mx-auto">
      <form
        @submit.prevent="authStore.handleForgotPassword(email)"
        :class="{ 'opacity-60': authStore.isLoading }"
        class="w-full flex flex-col items-center justify-between"
      >
        <Loading v-if="authStore.isLoading"></Loading>
        <h4 class="text-3xl tracking-widest text-center font-bold">RESET PASSWORD</h4>

        <div class="border-b w-full border-black py-5">
          <p class="w-full text-lg tracking-wide">
            Please enter your email address below and click “Submit”, then we will send you an OTP code to reset your password.
          </p>
          <p v-if="authStore.status" class="mt-2 w-full text-sm text-green-600 font-semibold">{{ authStore.status }}</p>
        </div>

        <!-- General Error Banner -->
        <div
          v-if="authStore.generalError || authStore.errors?.[0]?.general?.[0]"
          class="w-full mt-4 p-3 text-sm text-red-700 bg-red-100 border border-red-300 rounded"
        >
          {{ authStore.generalError || authStore.errors?.[0]?.general?.[0] }}
        </div>

        <div class="w-full mt-6">
          <label class="block font-medium">Email Address</label>
          <input
            v-model="email"
            type="email"
            class="mt-2 w-full block px-4 py-3 border border-black"
          />
          <p v-if="authStore.errors?.[0]?.email?.[0]" class="text-red-500 text-sm mt-2">
            {{ authStore.errors[0].email[0] }}
          </p>
        </div>

        <div class="mt-6 w-full">
          <button
            type="submit"
            :disabled="authStore.isLoading"
            :class="{ 'cursor-progress': authStore.isLoading }"
            class="tracking-widest mx-auto block px-4 py-3 w-full bg-gray-900 hover:bg-gray-800 text-white text-xl font-bold transition duration-200"
          >
            Submit
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import Loading from "../../components/Loading.vue";
import { ref } from "vue";
import { useAuthStore } from "../../stores/auth";

const authStore = useAuthStore();
const email = ref("");
</script>

<style scoped></style>