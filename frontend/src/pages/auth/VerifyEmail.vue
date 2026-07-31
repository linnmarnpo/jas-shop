<template>
  <div class="pt-[110px] pb-16 min-h-[calc(100vh-200px)] flex items-center justify-center bg-white px-4">
    <div class="w-full max-w-md mx-auto">
      <form
        @submit.prevent="authStore.verifyEmail(data)"
        :class="{ 'opacity-60': authStore.isLoading }"
        class="flex flex-col items-center justify-between w-full"
      >
        <Loading v-if="authStore.isLoading"></Loading>
        <h4 class="text-3xl tracking-widest text-center font-bold">VERIFY YOUR EMAIL</h4>

        <div class="w-full py-5 border-b border-black">
          <p class="w-full text-lg tracking-wide">Please enter your 6 digit code below and click “Submit”.</p>
          <p v-if="authStore.status" class="w-full mt-2 text-sm text-green-600 font-semibold">{{ authStore.status }}</p>
        </div>

        <!-- General Error Banner -->
        <div
          v-if="authStore.generalError || authStore.errors?.[0]?.general?.[0]"
          class="w-full mt-4 p-3 text-sm text-red-700 bg-red-100 border border-red-300 rounded"
        >
          {{ authStore.generalError || authStore.errors?.[0]?.general?.[0] }}
        </div>

        <div class="w-full mt-6">
          <label class="block font-medium">6 Digit Code</label>
          <input
            v-model="data.code"
            type="text"
            placeholder="Enter verification code"
            class="block w-full px-4 py-3 mt-2 border border-black"
          />
          <p v-if="authStore.errors?.[0]?.code?.[0]" class="mt-2 text-sm text-red-500">
            {{ authStore.errors[0].code[0] }}
          </p>
        </div>

        <div class="w-full mt-6">
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
import { ref, onMounted } from "vue";
import { useAuthStore } from "../../stores/auth";
import { useRouter } from "vue-router";

const authStore = useAuthStore();
const router = useRouter();

const data = ref({
  userName: "",
  code: "",
});

onMounted(() => {
  const savedEmail = sessionStorage.getItem("emailToVerify");
  if (!authStore.emailToVerify && savedEmail) {
    authStore.emailToVerify = savedEmail;
  }
  if (!authStore.emailToVerify) {
    router.replace("/");
    return;
  } else {
    data.value.userName = authStore.emailToVerify;
  }
});
</script>

<style scoped></style>