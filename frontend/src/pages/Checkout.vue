<template>
  <div class="min-h-screen pt-16 flex flex-col lg:flex-row w-full bg-gray-50 text-gray-800">
    <!-- LEFT SIDE : BILLING + PAYMENT -->
    <form
      class="w-full lg:w-1/2 bg-white px-4 sm:px-8 lg:px-12 py-8 sm:py-12 flex flex-col justify-between"
      @submit.prevent="addOrder"
    >
      <div>
        <h1 class="text-2xl sm:text-3xl font-bold text-gray-900 mb-6">Billing Details</h1>

        <!-- Address -->
        <div class="mb-6">
          <label class="block text-sm font-semibold text-gray-700 mb-2">Delivery Address <span class="text-red-500">*</span></label>
          <textarea
            v-model="form.address"
            required
            placeholder="Enter your street address, apartment, city..."
            class="block border border-gray-300 focus:border-black rounded-md w-full px-4 py-3 text-sm focus:outline-none min-h-[100px]"
          ></textarea>
        </div>

        <!-- Order Note -->
        <div class="mb-6">
          <label class="block text-sm font-semibold text-gray-700 mb-2">Order Note (Optional)</label>
          <textarea
            v-model="form.note"
            placeholder="Special instructions for delivery..."
            class="block border border-gray-300 focus:border-black rounded-md w-full px-4 py-3 text-sm focus:outline-none min-h-[80px]"
          ></textarea>
        </div>

        <!-- PAYMENT METHOD -->
        <div class="mb-6">
          <label class="block text-sm font-semibold text-gray-700 mb-2">Payment Method</label>
          <div class="border border-gray-200 rounded-md divide-y divide-gray-100 bg-gray-50/50">
            <!-- Cash on Delivery -->
            <div
              @click="showCart = false"
              class="flex items-center px-4 py-3.5 cursor-pointer hover:bg-white transition"
            >
              <div
                class="flex items-center justify-center w-4 h-4 border border-gray-800 rounded-full"
              >
                <span
                  :class="{ 'bg-gray-900': !showCart }"
                  class="block w-2 h-2 rounded-full"
                ></span>
              </div>
              <div class="text-sm font-medium ml-3">Cash on Delivery</div>
            </div>

            <!-- Jas Pay -->
            <div
              @click="showCart = true"
              class="flex items-center px-4 py-3.5 cursor-pointer hover:bg-white transition"
            >
              <div
                class="flex items-center justify-center w-4 h-4 border border-gray-800 rounded-full"
              >
                <span
                  :class="{ 'bg-gray-900': showCart }"
                  class="block w-2 h-2 rounded-full"
                ></span>
              </div>
              <div class="text-sm font-medium ml-3">Jas Pay</div>
            </div>
          </div>
        </div>
      </div>

      <!-- PLACE ORDER BUTTON FOR MOBILE (hidden on desktop if summary takes care of it, or keep main submit) -->
      <div class="mt-4 lg:hidden">
        <button
          type="submit"
          class="w-full py-3.5 px-6 text-white font-semibold bg-gray-900 hover:bg-gray-800 rounded-md transition"
        >
          Place Order (${{ Math.max(0, cartStore.totalAmount - discount).toFixed(2) }})
        </button>
      </div>
    </form>

    <!-- RIGHT SIDE : ORDER SUMMARY -->
    <div class="w-full lg:w-1/2 bg-gray-50 px-4 sm:px-8 lg:px-12 py-8 sm:py-12 flex flex-col items-center">
      <div class="w-full max-w-lg">
        <h2 class="text-xl sm:text-2xl font-bold text-gray-900 mb-6">Order Summary</h2>

        <!-- CART ITEMS -->
        <div class="space-y-3 mb-6 max-h-[400px] overflow-y-auto pr-1">
          <div
            v-for="cart in cartStore.carts"
            :key="cart.id"
            :class="{ 'opacity-50': cartStore.isLoading }"
            class="flex items-center justify-between p-3.5 bg-white rounded-lg border border-gray-200 shadow-xs"
          >
            <div class="flex items-center space-x-3.5">
              <img class="w-16 h-16 object-cover rounded-md border border-gray-100" :src="cart.imageUrl" />

              <div>
                <h3 class="text-sm font-semibold text-gray-900 line-clamp-1 max-w-[160px] sm:max-w-[220px]">
                  {{ cart.productName }}
                </h3>
                <p class="text-xs text-gray-500 mt-0.5">
                  Size: {{ cart.productVariant?.size }} | Color: {{ cart.productVariant?.color }}
                </p>
                <span class="inline-block text-xs text-gray-600 bg-gray-100 px-2 py-0.5 rounded-full mt-1">
                  Qty: {{ cart.quantity }}
                </span>
              </div>
            </div>

            <div class="text-right">
              <p class="text-sm font-bold text-gray-900">$ {{ (cart.price * cart.quantity).toFixed(2) }}</p>
            </div>
          </div>
        </div>

        <!-- TOTAL SECTION -->
        <div class="p-6 bg-white rounded-lg border border-gray-200 shadow-xs space-y-3">
          <div class="flex justify-between text-sm text-gray-600">
            <div>Subtotal</div>
            <div class="font-medium text-gray-900">$ {{ cartStore.totalAmount.toFixed(2) }}</div>
          </div>

          <div class="flex justify-between text-sm text-gray-600">
            <div>Discount</div>
            <div :class="{ 'font-semibold text-green-600': isDiscount }" class="font-medium">
              -$ {{ discount.toFixed(2) }}
            </div>
          </div>

          <div class="flex justify-between text-base font-bold text-gray-900 pt-3 border-t border-gray-200">
            <div>Total Cost</div>
            <div>$ {{ Math.max(0, cartStore.totalAmount - discount).toFixed(2) }}</div>
          </div>

          <!-- PLACE ORDER (DESKTOP) -->
          <div class="pt-4 hidden lg:block">
            <button
              @click="addOrder()"
              type="button"
              class="w-full py-3.5 px-6 text-white font-semibold bg-gray-900 hover:bg-gray-800 rounded-md transition"
            >
              Place Order
            </button>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, ref, watch } from "vue";
import { useCartStore } from "../stores/cart";
import axios from "../service/axios";
import { useRouter } from "vue-router";

const router = useRouter();
const cartStore = useCartStore();
const showCart = ref(false);

const form = reactive({
  address: "",
  note: "",
});

const isDiscount = computed(() => {
  return cartStore.totalAmount > 100;
});

const discount = computed(() => {
  return isDiscount.value ? 5 : 0;
});

const addOrder = async () => {
  try {
    const order = {
      address: form.address,
      note: form.note,
      discount: discount.value,
      paymentMethod: "Cash on Delivery",
    };
    await axios.post("/orders/create", order);
    cartStore.clearCarts();
    router.push({ name: "userDashboard" });
  } catch (error) {
    console.log(error);
  }
};
</script>

<style></style>
