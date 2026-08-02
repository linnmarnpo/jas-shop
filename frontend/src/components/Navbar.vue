<template>
  <div>
    <!-- Main Navbar -->
    <div
      :class="[isActive ? 'bg-white shadow-md ' : ' backdrop-blur-xs ']"
      class="fixed top-0 left-0 right-0 z-40 text-lg transition-all duration-300 navbar"
    >
      <div
        :class="[isActive ? 'h-[54px]' : 'h-[72px]']"
        class="flex items-center justify-between px-4 mx-auto transition-all duration-300 max-w-7xl sm:px-6 lg:px-8"
      >
        <!-- MOBILE LEFT: Hamburger Menu Toggle Button -->
        <button
          @click="isMobileMenuOpen = true"
          type="button"
          class="p-2 text-gray-800 lg:hidden focus:outline-none"
          aria-label="Open Mobile Menu"
        >
          <Icon icon="lucide:menu" class="w-6 h-6" />
        </button>

        <!-- DESKTOP LEFT: Nav Links (Home, Shop, Info) -->
        <div
          class="items-center hidden space-x-8 text-sm font-bold tracking-wider text-gray-900 uppercase lg:flex"
        >
          <router-link :to="{ name: 'home' }" class="nav-link hover:text-black"
            >Home</router-link
          >
          <router-link :to="{ name: 'shop' }" class="nav-link hover:text-black"
            >Shop</router-link
          >
          <a class="nav-link hover:text-black" href="#info">Info</a>
        </div>

        <!-- CENTER: Logo Only -->
        <div class="flex items-center justify-center">
          <router-link :to="{ name: 'home' }" class="flex items-center">
            <img
              :class="[isActive ? 'h-8' : 'h-10']"
              class="object-contain w-auto transition-all duration-300 hover:scale-105"
              src="https://res.cloudinary.com/dz993lw3o/image/upload/v1709042763/logo_nmemlm.png"
              alt="Brand Logo"
            />
          </router-link>
        </div>

        <!-- DESKTOP & MOBILE RIGHT: Icons Only (Removed text beside icons) -->
        <div class="flex items-center space-x-5">
          <!-- User / Auth Icon -->
          <router-link
            v-if="auth?.user"
            :to="dashboardRoute"
            class="text-gray-800 transition hover:text-black"
            title="My Account"
          >
            <Icon icon="ep:user" width="24" height="24" />
          </router-link>
          <router-link
            v-else
            :to="{ name: 'LoginRegister' }"
            class="text-gray-800 transition hover:text-black"
            title="Log In"
          >
            <Icon icon="ep:user" width="24" height="24" />
          </router-link>

          <!-- Cart Icon with Counter Badge -->
          <button
            @click="cartStore.openCart()"
            type="button"
            class="relative p-1 text-gray-800 transition hover:text-black focus:outline-none"
            title="Shopping Cart"
          >
            <Icon icon="ep:shopping-cart" width="24" height="24" />
            <span
              v-if="cartCount > 0"
              class="absolute -top-1 -right-1 bg-red-600 text-white text-[10px] font-bold w-4 h-4 rounded-full flex items-center justify-center"
            >
              {{ cartCount }}
            </span>
          </button>
        </div>
      </div>
    </div>

    <!-- BAPE-STYLE MOBILE FULL SCREEN OVERLAY MENU -->
    <Transition name="slide-left">
      <div
        v-if="isMobileMenuOpen"
        class="fixed inset-0 z-50 flex flex-col justify-between overflow-y-auto bg-white lg:hidden"
      >
        <!-- Top Bar: Close Button 'X' -->
        <div
          class="flex items-center justify-between px-6 pt-6 pb-4 border-b border-gray-100"
        >
          <button
            @click="isMobileMenuOpen = false"
            type="button"
            class="p-2 -ml-2 text-gray-800 hover:text-black focus:outline-none"
            aria-label="Close menu"
          >
            <Icon icon="lucide:x" class="w-7 h-7" />
          </button>
          <img
            src="https://res.cloudinary.com/dz993lw3o/image/upload/v1709042763/logo_nmemlm.png"
            alt="Logo"
            class="w-auto h-7"
          />
          <div class="w-7"></div>
        </div>

        <!-- Menu Content -->
        <div class="flex-1 w-full max-w-sm px-6 py-6 mx-auto">
          <!-- Search Bar -->
          <form @submit.prevent="handleSearch" class="relative mb-8">
            <input
              v-model="searchQuery"
              type="text"
              placeholder="Search"
              class="w-full py-2 pr-8 text-sm font-medium text-gray-900 placeholder-gray-400 bg-transparent border-b border-gray-300 focus:border-black focus:outline-none"
            />
            <button
              type="submit"
              class="absolute right-0 text-gray-400 top-2 hover:text-black"
            >
              <Icon icon="lucide:search" class="w-4 h-4" />
            </button>
          </form>

          <!-- Navigation Links Stack -->
          <div
            class="text-xs font-semibold tracking-wider text-gray-900 uppercase divide-y divide-gray-100"
          >
            <router-link
              @click="isMobileMenuOpen = false"
              :to="{ name: 'home' }"
              class="flex items-center justify-between py-4 transition hover:text-amber-600"
            >
              <span>HOME</span>
              <Icon icon="lucide:chevron-right" class="w-4 h-4 text-gray-400" />
            </router-link>

            <router-link
              @click="isMobileMenuOpen = false"
              :to="{ name: 'shop' }"
              class="flex items-center justify-between py-4 transition hover:text-amber-600"
            >
              <span>SHOP</span>
              <Icon icon="lucide:chevron-right" class="w-4 h-4 text-gray-400" />
            </router-link>

            <a
              href="#info"
              @click="isMobileMenuOpen = false"
              class="flex items-center justify-between py-4 transition hover:text-amber-600"
            >
              <span>INFO</span>
              <Icon icon="lucide:chevron-right" class="w-4 h-4 text-gray-400" />
            </a>

            <!-- User Link -->
            <router-link
              v-if="auth?.user"
              @click="isMobileMenuOpen = false"
              :to="dashboardRoute"
              class="flex items-center py-4 space-x-3 text-gray-900 transition hover:text-amber-600"
            >
              <Icon icon="lucide:user" class="w-4 h-4" />
              <span>MY ACCOUNT ({{ auth.user.name }})</span>
            </router-link>
            <router-link
              v-else
              @click="isMobileMenuOpen = false"
              :to="{ name: 'LoginRegister' }"
              class="flex items-center py-4 space-x-3 text-gray-900 transition hover:text-amber-600"
            >
              <Icon icon="lucide:user" class="w-4 h-4" />
              <span>LOG IN / CREATE ACCOUNT</span>
            </router-link>
          </div>
        </div>

        <!-- Footer Section -->
        <div
          class="w-full max-w-sm px-6 py-6 mx-auto space-y-4 text-center border-t border-gray-100"
        >
          <div class="flex items-center justify-center space-x-6 text-gray-700">
            <a href="#" aria-label="Facebook"
              ><Icon icon="simple-icons:facebook" class="w-4 h-4"
            /></a>
            <a href="#" aria-label="Instagram"
              ><Icon icon="simple-icons:instagram" class="w-4 h-4"
            /></a>
            <a href="#" aria-label="X"
              ><Icon icon="simple-icons:x" class="w-4 h-4"
            /></a>
            <a href="#" aria-label="YouTube"
              ><Icon icon="simple-icons:youtube" class="w-4 h-4"
            /></a>
          </div>

          <div
            class="flex items-center justify-between text-[11px] font-medium text-gray-500 uppercase pt-2 border-t border-gray-100"
          >
            <a href="#" class="hover:text-black">FAQ</a>
            <span>USD $</span>
          </div>
        </div>
      </div>
    </Transition>

    <!-- SHOPPING CART DRAWER MODAL -->
    <Transition name="fade">
      <div
        v-if="cartStore.isOpen"
        @click="cartStore.closeCart()"
        class="fixed inset-0 z-50 bg-black/50 backdrop-blur-xs"
      ></div>
    </Transition>

    <Transition name="slide-right">
      <div
        v-if="cartStore.isOpen"
        class="fixed top-0 right-0 bottom-0 w-full sm:w-[420px] bg-white z-50 shadow-2xl flex flex-col justify-between"
      >
        <div class="flex items-center justify-between px-5 py-4 bg-gray-100">
          <h5 v-if="auth?.user" class="text-xl font-bold text-gray-900">
            {{ auth.user.name }}'s Cart
          </h5>
          <h5 v-else class="text-xl font-bold text-gray-900">Your Cart</h5>
          <button
            @click="cartStore.closeCart()"
            type="button"
            class="text-gray-500 hover:text-black focus:outline-none"
          >
            <Icon icon="lucide:x" class="w-6 h-6" />
          </button>
        </div>
        <div class="flex-1 p-4 overflow-y-auto">
          <Cart />
        </div>
      </div>
    </Transition>
  </div>
</template>

<script setup>
import { computed, onMounted, onUnmounted, ref } from "vue";
import { useRouter } from "vue-router";
import { Icon } from "@iconify/vue";
import Cart from "./Cart.vue";
import { useAuthStore } from "../stores/auth";
import { useCartStore } from "../stores/cart";

const router = useRouter();
const auth = useAuthStore();
const cartStore = useCartStore();

const isActive = ref(false);
const isMobileMenuOpen = ref(false);
const isCartOpen = ref(false);
const searchQuery = ref("");

const cartCount = computed(() => {
  return (
    cartStore.carts?.reduce((sum, item) => sum + (item.quantity || 1), 0) || 0
  );
});

const dashboardRoute = computed(() => {
  return auth.role === "ADMIN"
    ? { name: "AdminHome" }
    : { name: "userDashboard" };
});

const handleScroll = () => {
  isActive.value = window.scrollY > 0;
};

const handleSearch = () => {
  if (searchQuery.value.trim()) {
    isMobileMenuOpen.value = false;
    router.push({ name: "shop", query: { search: searchQuery.value.trim() } });
  }
};

onMounted(() => {
  window.addEventListener("scroll", handleScroll);
});

onUnmounted(() => {
  window.removeEventListener("scroll", handleScroll);
});
</script>

<style scoped>
a {
  text-decoration: none;
  color: #333;
}

.navbar {
  transition: all 0.3s ease-in-out;
}

.nav-link {
  font-weight: bold;
  position: relative;
}

.nav-link:hover {
  color: #000;
}

.nav-link::before {
  content: "";
  position: absolute;
  bottom: -4px;
  left: 0;
  width: 0;
  height: 2px;
  background-color: black;
  visibility: hidden;
  transition: 0.3s ease-in-out;
}

.nav-link:hover::before,
.router-link-exact-active::before {
  width: 100%;
  visibility: visible;
}

/* Slide Transitions */
.slide-left-enter-active,
.slide-left-leave-active {
  transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.slide-left-enter-from,
.slide-left-leave-to {
  transform: translateX(-100%);
}

.slide-right-enter-active,
.slide-right-leave-active {
  transition: transform 0.3s cubic-bezier(0.16, 1, 0.3, 1);
}
.slide-right-enter-from,
.slide-right-leave-to {
  transform: translateX(100%);
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.2s ease;
}
.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
