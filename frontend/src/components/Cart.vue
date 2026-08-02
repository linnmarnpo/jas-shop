<template>
  <div class="flex flex-col h-full">
    <transition name="switch">
      <div v-if="cartStore.carts.length" class="flex-1 px-4 overflow-y-auto">
        <div
          v-for="cart in cartStore.carts"
          :key="cart.id"
          class="flex py-2 border-b"
          :class="{ 'opacity-50 cursor-progress': cartStore.isLoading }"
        >
          <div class="mr-5">
            <img width="70" :src="cart.imageUrl" alt="product" />
          </div>

          <div class="flex flex-col w-full mt-3">
            <div class="flex items-center justify-between px-2">
              <div>
                <h2 class="text-lg font-semibold max-w-60">
                  {{ cart.productName }}
                </h2>
                <span>{{ cart.productVariant.color }}, </span>
                <span>{{ cart.productVariant.size }}</span>
              </div>

              <Icon
                @click="cartStore.deleteCart(cart.id)"
                class="cursor-pointer"
                icon="ion:close-outline"
                width="23"
                height="23"
              />
            </div>

            <div class="flex justify-between w-full px-2 mt-1">
              <p>$ {{ cart.price }} × {{ cart.quantity }}</p>
              <p class="text-xl text-gray-500">
                $ {{ (cart.price * cart.quantity).toFixed(2) }}
              </p>
            </div>
          </div>
        </div>
      </div>

      <div v-else class="flex flex-col items-center justify-center flex-1">
        <div
          class="w-[8rem] h-[8rem] rounded-full bg-gray-200 flex items-center justify-center"
        >
          <Icon icon="ep:shopping-cart" width="55" height="55" />
        </div>
        <div class="mt-4 text-center">
          <p>No products in your cart</p>
        </div>
        <div class="mt-8">
          <router-link
            :to="{ name: 'shop' }"
            @click="cartStore.closeCart()"
            class="px-8 py-3 border border-black"
          >
            Continue shopping
          </router-link>
        </div>
      </div>
    </transition>

    <div
      v-if="cartStore.carts.length"
      class="sticky bottom-0 p-4 bg-white border-t"
    >
      <div class="flex justify-between text-lg font-semibold">
        <div>Total:</div>
        <div>$ {{ cartStore.totalAmount.toFixed(2) }}</div>
      </div>
      <router-link
        :to="{ name: 'checkout' }"
        @click="cartStore.closeCart()"
        class="block py-3 mt-3 text-center text-white bg-gray-900 hover:bg-gray-800"
      >
        Checkout
      </router-link>
    </div>
  </div>
</template>

<script setup>
import { Icon } from "@iconify/vue";
import { useCartStore } from "../stores/cart";

const cartStore = useCartStore();
</script>

<style scoped>
/* switch style */
.switch-enter-from,
.switch-leave-to {
  opacity: 0;
  transform: translateY(40px);
}

.switch-enter-to,
.switch-leave-from {
  opacity: 1;
  transform: translateY(0);
}

.switch-enter-active {
  transition: all 0.5s ease;
}

.switch-enter-active {
  transition: all 0.5s ease;
}
</style>
