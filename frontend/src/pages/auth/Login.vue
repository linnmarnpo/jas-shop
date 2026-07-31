<template>
  <form
    @submit.prevent="authStore.handleLogin(data)"
    :class="{ 'opacity-60': authStore.isLoading }"
    class="w-full"
  >
    <Loading v-if="authStore.isLoading"></Loading>
    
    <!-- General Error Banner -->
    <div
      v-if="authStore.generalError || authStore.errors?.[0]?.general?.[0]"
      class="p-3 mb-4 text-sm text-red-700 bg-red-100 border border-red-300 rounded"
    >
      {{ authStore.generalError || authStore.errors?.[0]?.general?.[0] }}
    </div>

    <!-- Status Banner -->
    <div
      v-if="authStore.status"
      class="p-3 mb-4 text-sm text-green-700 bg-green-100 border border-green-300 rounded"
    >
      {{ authStore.status }}
    </div>

    <div class="w-full">
      <label>Email Address</label>
      <input
        type="email"
        v-model="data.email"
        class="block w-full px-4 py-3 mt-1 border border-black"
      />
      <p v-if="authStore.errors?.[0]?.email?.[0]" class="mt-2 text-sm text-red-500">
        {{ authStore.errors[0].email[0] }}
      </p>
    </div>
    <div class="w-full mt-3">
      <label>Password</label>
      <div class="relative">
        <input
          :type="showPassword ? 'text' : 'password'"
          v-model="data.password"
          class="block w-full px-4 py-3 mt-1 border border-black"
        />
        <div
          type="button"
          @click="showPassword = !showPassword"
          class="absolute inset-y-0 flex items-center px-3 end-0 cursor-pointer"
        >
          <Icon
            :icon="showPassword ? 'mdi:eye' : 'mdi:eye-off'"
            class="text-xl"
          />
        </div>
      </div>
      <p v-if="authStore.errors?.[0]?.password?.[0]" class="mt-2 text-sm text-red-500">
        {{ authStore.errors[0].password[0] }}
      </p>
    </div>
    <div class="flex items-center mt-3">
      <input
        type="checkbox"
        v-model="authStore.rememberMe"
        class="w-5 h-5 cursor-pointer accent-gray-900"
      />
      <label class="ml-2">Remember me</label>
    </div>
    <div class="w-full mt-6">
      <button
        type="submit"
        :disabled="authStore.isLoading"
        :class="{ 'cursor-progress': authStore.isLoading }"
        class="tracking-widest mx-auto block px-4 py-3 w-full bg-gray-900 hover:bg-gray-800 text-white text-lg font-bold transition duration-200"
      >
        Log In
      </button>
    </div>
    <div class="flex items-center justify-center w-full mt-3">
      <router-link
        :to="{ name: 'ForgotPassword' }"
        class="text-sm duration-200 ease-in hover:font-semibold"
        >FORGOT YOUR PASSWORD?</router-link
      >
    </div>
  </form>
</template>

<script setup>
import Loading from "../../components/Loading.vue";
import { onMounted, ref } from "vue";
import { useAuthStore } from "../../stores/auth";
import Cookies from "js-cookie";
import { Icon } from "@iconify/vue";

const authStore = useAuthStore();
const showPassword = ref(false);
let data = ref({
  email: "",
  password: "",
});
onMounted(() => {
  authStore.clearErrors();
  const savedEmail = Cookies.get("remember_email");
  if (savedEmail) {
    data.value.email = savedEmail;
    authStore.rememberMe = true;
  }
});
</script>

<style></style>
