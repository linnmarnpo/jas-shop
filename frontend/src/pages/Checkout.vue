<template>
  <div class="flex items-center w-full text-lg">
    <!-- LEFT SIDE : BILLING + PAYMENT -->
    <form
      class="w-1/2 bg-white h-screen mx-2 py-[5rem] px-[4rem] overflow-y-auto"
    >
      <div>
        <h1 class="text-3xl">Billing Details</h1>
      </div>

      <!-- Address -->
      <div class="mt-[2rem]">
        <label>Address:</label>
        <textarea
          v-model="form.address"
          class="block border border-black w-full mt-2 px-3 py-3 max-h-[200px]"
        ></textarea>
      </div>

      <!-- Order Note -->
      <div class="mt-[2rem]">
        <label>Order Note (Optional):</label>
        <textarea
          v-model="form.note"
          class="block border border-black w-full mt-2 px-3 py-3 max-h-[200px]"
        ></textarea>
      </div>

      <!-- PAYMENT METHOD -->
      <div class="mt-[2rem] px-3 py-2 border rounded-md">
        <!-- Cash on Delivery -->
        <div
          @click="showCart = false"
          class="flex items-center px-4 py-3 cursor-pointer"
        >
          <div
            class="flex items-center justify-center w-4 h-4 border border-black rounded-full"
          >
            <span
              :class="{ 'bg-gray-800': !showCart }"
              class="block w-2 h-2 rounded-full"
            ></span>
          </div>
          <div class="text-base ml-[1rem]">Cash on Delivery</div>
        </div>

        <!-- Jas Pay -->
        <div
          @click="showCart = true"
          class="flex items-center px-4 py-3 cursor-pointer"
        >
          <div
            class="flex items-center justify-center w-4 h-4 border border-black rounded-full"
          >
            <span
              :class="{ 'bg-gray-800': showCart }"
              class="block w-2 h-2 rounded-full"
            ></span>
          </div>
          <div class="text-base ml-[1rem]">Jas Pay</div>
        </div>
      </div>

      <!-- Jas Pay Login Card -->
      <div
        v-if="showCart"
        class="mt-5 rounded-[10px] bg-gray-900 p-7 text-gray-400"
      >
        <!-- <div class="flex items-center gap-x-4">
          <div class="p-3 bg-gray-700 rounded-full">
            <Icon
              class="w-5 h-5 text-indigo-400 fill-current"
              icon="mdi-light:credit-card"
              width="30"
              height="30"
            />
          </div>

          <div>
            <div class="text-sm">PHONE NUMBER</div>
            <input
              v-model="form.phone"
              type="text"
              class="block px-4 py-3 text-sm text-white bg-gray-900 border-b border-gray-200"
            />
          </div>

          <div>
            <div class="text-sm">PASSWORD</div>
            <input
              v-model="loginData.password"
              type="text"
              class="block px-4 py-3 text-sm text-white bg-gray-900 border-b border-gray-200"
            />
          </div>

          <div v-if="!cardLoginSuccess">
            <button
              @click="cardLogin"
              type="button"
              class="px-10 py-3 text-white bg-red-500 rounded-lg hover:bg-red-600"
            >
              Login
            </button>
          </div>

          <Icon
            v-else
            icon="mdi:check-circle"
            class="text-4xl text-green-500"
          />
        </div> -->
      </div>
    </form>

    <!-- RIGHT SIDE : ORDER SUMMARY -->
    <div class="w-1/2 h-screen bg-gray-50">
      <div
        class="px-[3rem] py-[3rem] w-full h-full flex flex-col items-center text-lg text-gray-700"
      >
        <!-- CART ITEMS -->
        <div
          v-for="cart in cartStore.carts"
          :key="cart.id"
          :class="{ 'opacity-50': cartStore.isLoading }"
          class="flex py-3 w-[90%] justify-around bg-white rounded-lg mt-2 shadow-md"
        >
          <img width="80" :src="cart.imageUrl" />

          <div class="flex flex-col justify-between">
            <h2 class="max-w-60 min-w-60">{{ cart.productName }}</h2>
            <span>
              Amount:
              <span class="px-3 py-[0.5] bg-gray-200 rounded-full">
                {{ cart.quantity }}
              </span>
            </span>
          </div>

          <div class="flex flex-col items-center justify-between">
            <div class="flex">
              <p>Size: {{ cart.productVariant.size }},</p>
              <p class="ml-4">{{ cart.productVariant.color }}</p>
            </div>
            <p>$ {{ (cart.price * cart.quantity).toFixed(2) }}</p>
          </div>
        </div>

        <!-- TOTAL SECTION -->
        <div class="px-[4rem] py-3 w-[90%] bg-white rounded-lg mt-2 shadow-sm">
          <div class="flex justify-between py-3 border-b">
            <div>Subtotal</div>
            <div>$ {{ cartStore.totalAmount.toFixed(2) }}</div>
          </div>

          <div class="flex justify-between py-3 border-b">
            <div>Discount</div>
            <div :class="{ 'font-semibold text-green-500': isDiscount }">
              $ {{ discount }}
            </div>
          </div>

          <div class="flex justify-between py-3">
            <div>Total Cost</div>
            <div>$ {{ (cartStore.totalAmount - 5).toFixed(2) }}</div>
          </div>

          <!-- PLACE ORDER -->
          <div class="mt-[2rem]">
            <button
              @click="addOrder()"
              class="block w-full px-10 py-3 mx-auto text-white bg-gray-900 hover:bg-gray-800"
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
