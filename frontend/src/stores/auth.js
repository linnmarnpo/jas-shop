import { defineStore } from "pinia";
import axios from "../service/axios";
import { useCartStore } from "./cart";
import { isTokenValid, saveToken } from "./jwt-helper";
import Cookies from "js-cookie";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    rememberMe: false,
    confirmPasswordError: null,
    authErrors: {},
    generalError: null,
    authStatus: null,
    isLoading: false,
    emailToVerify: null,
    user: null,
    role: null,
  }),
  getters: {
    errors: (state) => state.authErrors,
    status: (state) => state.authStatus,
  },
  actions: {
    clearErrors() {
      this.authErrors = {};
      this.generalError = null;
      this.confirmPasswordError = null;
    },

    extractErrors(error) {
      if (error.response?.data?.errors) {
        this.authErrors = error.response.data.errors;
        if (Array.isArray(error.response.data.errors) && error.response.data.errors[0]?.general) {
          this.generalError = error.response.data.errors[0].general[0];
        } else if (error.response?.data?.message) {
          this.generalError = error.response.data.message;
        }
      } else if (error.response?.data?.message) {
        this.generalError = error.response.data.message;
        this.authErrors = [{ general: [error.response.data.message] }];
      } else {
        this.generalError = error.message || "An unexpected error occurred.";
        this.authErrors = [{ general: [this.generalError] }];
      }
    },

    async handleLogin(data) {
      this.isLoading = true;
      this.clearErrors();
      try {
        let res = await axios.post("/auth/login", data);
        if (res) {
          saveToken(res.data.data.token);
          if (this.rememberMe) {
            Cookies.set("remember_email", data.email, { expires: 7 });
          } else {
            Cookies.remove("remember_email");
          }
          await this.getUser();
          const cartStore = useCartStore();
          await cartStore.getCarts();
          this.router.push("/");
        }
      } catch (error) {
        this.extractErrors(error);
      } finally {
        this.isLoading = false;
      }
    },

    async getUser() {
      try {
        let res = await axios.get("/user/profile");
        if (res) {
          this.user = res.data.data;
          const authority = res.data.data.authorities?.[0]?.authority;
          if (authority === "ROLE_ADMIN") {
            this.role = "ADMIN";
          } else {
            this.role = "USER";
          }
        }
      } catch (error) {
        console.log(error);
      }
    },

    async handleLogout() {
      this.user = null;
      localStorage.removeItem("authToken");
      const cartStore = useCartStore();
      cartStore.clearCarts();
      this.router.push({ name: "LoginRegister" });
    },

    async handleRegister(payload) {
      this.isLoading = true;
      this.clearErrors();

      try {
        await axios.post("/auth/register", payload);

        this.emailToVerify = payload.email;
        sessionStorage.setItem("emailToVerify", payload.email);
        this.router.push({ name: "VerifyEmail" });
      } catch (error) {
        this.extractErrors(error);
      } finally {
        this.isLoading = false;
      }
    },

    async verifyEmail(data) {
      this.isLoading = true;
      this.clearErrors();
      try {
        const res = await axios.post("/auth/verify", data);
        this.authStatus = res.data.message || "Email verified successfully";
        sessionStorage.removeItem("emailToVerify");
        this.router.push({ name: "LoginRegister" });
      } catch (error) {
        this.extractErrors(error);
      } finally {
        this.isLoading = false;
      }
    },

    async handleForgotPassword(email) {
      this.isLoading = true;
      this.clearErrors();
      try {
        const res = await axios.post("/forgot-password", {
          email: email,
        });
        this.authStatus = res.data.message || res.data.status || "Password reset email sent successfully.";
      } catch (error) {
        this.extractErrors(error);
      } finally {
        this.isLoading = false;
      }
    },

    async handleResetPassword(data) {
      this.isLoading = true;
      this.clearErrors();
      try {
        const res = await axios.post("/reset-password", data);
        this.authStatus = res.data.message || "Password reset successfully. Please log in.";
        this.router.push({ name: "LoginRegister" });
      } catch (error) {
        this.extractErrors(error);
      } finally {
        this.isLoading = false;
      }
    },
  },
});

