<template>
  <form
    @submit.prevent="authStore.handleRegister(form)"
    :class="{ 'opacity-60 pointer-events-none': authStore.isLoading }"
    class="w-full"
  >
    <Loading v-if="authStore.isLoading" />

    <!-- General Error Banner -->
    <div
      v-if="authStore.generalError || authStore.errors?.[0]?.general?.[0]"
      class="p-3 mb-4 text-sm text-red-700 bg-red-100 border border-red-300 rounded"
    >
      {{ authStore.generalError || authStore.errors?.[0]?.general?.[0] }}
    </div>

    <!-- Name -->
    <div class="w-full">
      <label>Your Name</label>
      <input
        type="text"
        v-model="form.name"
        class="block w-full px-4 py-3 mt-1 border border-black"
      />
      <p
        v-if="authStore.errors?.[0]?.name?.[0]"
        class="mt-2 text-sm text-red-500"
      >
        {{ authStore.errors[0].name[0] }}
      </p>
    </div>

    <!-- Email -->
    <div class="w-full mt-3">
      <label>Email Address</label>
      <input
        type="email"
        v-model="form.email"
        class="block w-full px-4 py-3 mt-1 border border-black"
      />
      <p
        v-if="authStore.errors?.[0]?.email?.[0]"
        class="mt-2 text-sm text-red-500"
      >
        {{ authStore.errors[0].email[0] }}
      </p>
    </div>

    <!-- Password -->
    <div class="w-full mt-3">
      <label>Password</label>
      <input
        type="password"
        v-model="form.password"
        class="block w-full px-4 py-3 mt-1 border border-black"
      />
      <p
        v-if="authStore.errors?.[0]?.password?.[0]"
        class="mt-2 text-sm text-red-500"
      >
        {{ authStore.errors[0].password[0] }}
      </p>
    </div>

    <!-- Confirm Password -->
    <div class="w-full mt-3">
      <label>Confirm Password</label>
      <input
        type="password"
        v-model="form.password_confirmation"
        class="block w-full px-4 py-3 mt-1 border border-black"
      />
      <p
        v-if="authStore.confirmPasswordError || authStore.errors?.[0]?.password_confirmation?.[0]"
        class="mt-2 text-sm text-red-500"
      >
        {{ authStore.confirmPasswordError || authStore.errors[0].password_confirmation[0] }}
      </p>
    </div>

    <!-- Submit -->
    <div class="w-full mt-6">
      <button
        type="submit"
        :disabled="authStore.isLoading"
        class="mx-auto block px-4 py-3 w-full bg-gray-900 hover:bg-gray-800 text-white text-xl font-bold tracking-widest transition duration-200"
      >
        Sign Up
      </button>
    </div>
  </form>
</template>

<script setup>
import { onMounted, reactive, ref } from "vue";
import { useAuthStore } from "../../stores/auth";
import Loading from "../../components/Loading.vue";

const authStore = useAuthStore();
const error = ref("");
const form = reactive({
  name: "",
  email: "",
  password: "",
  password_confirmation: "",
});

onMounted(() => {
  authStore.clearErrors();
});
</script>
