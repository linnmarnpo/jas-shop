<template>
  <div class="pt-[110px] pb-16 min-h-[calc(100vh-200px)] flex items-center justify-center bg-white px-4">
    <div class="w-full max-w-md mx-auto">
      <Loading v-if="authStore.isLoading"></Loading>
      <form
        @submit.prevent="authStore.handleResetPassword(data)"
        :class="{ 'opacity-60': authStore.isLoading }"
        class="w-full flex flex-col items-center justify-between"
      >
        <h4 class="text-3xl tracking-widest text-center font-bold">UPDATE YOUR PASSWORD</h4>

        <p class="py-5 w-full text-lg tracking-wide border-b border-black">
          Please enter your new password and confirm it below, then click “Submit” to update your password.
        </p>

        <!-- General Error Banner -->
        <div
          v-if="authStore.generalError || authStore.errors?.[0]?.general?.[0]"
          class="w-full mt-4 p-3 text-sm text-red-700 bg-red-100 border border-red-300 rounded"
        >
          {{ authStore.generalError || authStore.errors?.[0]?.general?.[0] }}
        </div>

        <div class="w-full mt-6">
          <label class="block font-medium">New Password</label>
          <input
            v-model="data.password"
            type="password"
            class="mt-2 w-full block px-4 py-3 border border-black"
          />
          <p v-if="authStore.errors?.[0]?.password?.[0]" class="text-red-500 text-sm mt-2">
            {{ authStore.errors[0].password[0] }}
          </p>
        </div>

        <div class="w-full mt-4">
          <label class="block font-medium">Confirm Password</label>
          <input
            v-model="data.password_confirmation"
            type="password"
            class="mt-2 w-full block px-4 py-3 border border-black"
          />
          <p v-if="authStore.errors?.[0]?.password_confirmation?.[0]" class="text-red-500 text-sm mt-2">
            {{ authStore.errors[0].password_confirmation[0] }}
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
import { useRoute } from "vue-router";

const route = useRoute();
const authStore = useAuthStore();

const data = ref({
  password: "",
  password_confirmation: "",
  email: route.query.email,
  token: route.params.token,
});
</script>

<style scoped></style>