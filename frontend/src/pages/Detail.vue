<script setup>
import { computed, onMounted, ref } from "vue";
import { useRoute } from "vue-router";
import { Icon } from "@iconify/vue/dist/iconify.js";
import Toast from "../components/Toast.vue";
import { useProductStore } from "../stores/product";
import { useAuthStore } from "../stores/auth";
import { useCartStore } from "../stores/cart";

const route = useRoute();

const auth = useAuthStore();
const cartStore = useCartStore();
const productStore = useProductStore();

const selectedColor = ref(null);
const selectedSize = ref(null);
const amount = ref(1);

/* Toast */
const showToast = ref(false);
const toastMessage = ref("");

const triggerToast = (message) => {
  toastMessage.value = message;
  showToast.value = true;
  setTimeout(() => (showToast.value = false), 3000);
};

const displayImg = computed(() => {
  if (!productStore.productUI) return null;

  if (!selectedColor.value) {
    return productStore.getPrimaryImage(productStore.productUI);
  }

  const resource = productStore.productUI.resources?.find(
    (res) => res.color === selectedColor.value,
  );

  return resource?.url || productStore.getPrimaryImage(productStore.productUI);
});

const stock = computed(() => {
  if (!productStore.productUI || !selectedColor.value || !selectedSize.value) {
    return null;
  }

  const variant = productStore.productUI.productVariants?.find(
    (v) => v.color === selectedColor.value && v.size === selectedSize.value,
  );

  return variant?.stockQuantity ?? 0;
});

// Add to Cart

const addToCart = async () => {
  if (!selectedColor.value) {
    return triggerToast("Please select a color");
  }

  if (!selectedSize.value) {
    return triggerToast("Please select a size");
  }

  if (!auth.user) {
    return triggerToast("Please login first");
  }

  if (stock.value === 0) {
    return triggerToast("This product is out of stock");
  }

  if (amount.value > stock.value) {
    return triggerToast("Insufficient stock");
  }

  const variant = productStore.productUI.productVariants.find(
    (v) => v.color === selectedColor.value && v.size === selectedSize.value,
  );

  await cartStore.addItemToCart({
    productId: productStore.productUI.id,
    productVariantId: variant.id,
    quantity: amount.value,
  });

  document.querySelector('.nav-item[data-bs-toggle="offcanvas"]')?.click();
};

onMounted(() => {
  productStore.getProduct(route.query.id);
});
</script>

<template>
  <div>
    <div
      class="grid grid-cols-1 gap-2 font-mono text-xl select-none sm:grid-cols-2 md:grid-cols-2 md:mx-20"
    >
      <div class="mx-auto image">
        <transition name="slide" mode="out-in">
          <img
            :key="displayImg"
            :src="displayImg"
            :alt="productStore.productUI?.name"
          />
        </transition>
        <!-- <img class="coverImage" :src="productduct.backdrop" :alt="productduct.name"> -->
      </div>
      <div class="px-5 mt-3 md:ml-9 md:py-36">
        <div
          class="py-4 mt-4 leading-10 border-b-2 product-info"
          style="position: sticky; top: 4cm"
        >
          <p class="my-2 text-lg font-semibold">
            <span class="text-gray-500">Jas</span>
            <span class="ml-5 text-orange-300">{{
              productStore.productUI?.category?.name
            }}</span>
          </p>
          <h4 class="mb-2 capitalize">{{ productStore.productUI?.name }}</h4>
          <p class="text-gray-500">US ${{ productStore.productUI?.price }}</p>
          <div class="mt-4 text-base font-semibold">
            Color :
            <span class="pl-2 text-gray-600 capitalize color-name">{{
              selectedColor
            }}</span>
          </div>
          <div class="flex flex-row">
            <div
              v-for="resource in productStore.productUI?.resources"
              :key="resource"
              @click="selectedColor = resource.color"
            >
              <img
                width="45"
                class="mx-1 border-gray-500 cursor-pointer color-btn"
                :class="{
                  'border-1': selectedColor == resource.color,
                  'border-none': selectedColor !== resource.color,
                }"
                :src="resource.url"
              />
            </div>
          </div>
          <div class="mt-3 text-base font-semibold">
            Size :
            <span
              v-if="selectedSize"
              class="pl-2 text-gray-600 capitalize size-name"
              >{{ selectedSize }}</span
            >
          </div>
          <div class="flex text-xl sizes">
            <button
              @click="selectedSize = size"
              v-for="size in productStore.productUI?.sizes"
              :key="size"
              :class="{
                'bg-slate-600 text-white': selectedSize === size,
                'bg-none': selectedSize !== size,
              }"
              class="px-3 py-1 mx-1 transition duration-300 ease-in-out border size-btn hover:bg-slate-600 hover:text-white"
            >
              {{ size }}
            </button>
          </div>
          <div class="my-4 ml-1">
            <transition-group tag="div" name="list" appear>
              <div
                v-if="stock"
                class="w-1/4 px-4 py-1 text-center text-gray-700 border-2 border-green-300 rounded-full"
              >
                {{ stock }} in Stock
              </div>
              <div
                v-if="stock == 0 && selectedColor && selectedSize"
                class="w-1/3 py-1 text-center text-red-500 border-2 border-red-300 rounded-full"
              >
                Out of Stock
              </div>
            </transition-group>
          </div>
          <div class="flex flex-col gap-3 amount-addToCart md:flex-row">
            <div
              class="flex justify-around flex-1 py-2 border amount md:flex-none md:w-28"
            >
              <div class="flex items-center cursor-pointer">
                <Icon
                  @click="amount += 1"
                  icon="ic:outline-plus"
                  width="24"
                  height="24"
                />
              </div>
              <input
                v-model.number="amount"
                name="quantity"
                class="w-12 text-xl text-center text-black border-none md:w-6 focus:outline-none"
              />
              <div class="flex items-center cursor-pointer">
                <Icon
                  @click="amount == 1 ? 1 : (amount -= 1)"
                  icon="ic:outline-minus"
                  width="24"
                  height="24"
                />
              </div>
            </div>
            <button
              @click="addToCart"
              :disabled="cartStore.isLoading"
              :class="{ 'cursor-progress': cartStore.isLoading }"
              class="flex items-center justify-center flex-1 py-2 text-white bg-gray-900 cursor-pointer hover:bg-gray-800 md:flex-none md:w-1/2"
            >
              Add to Cart
            </button>
            <div
              class="flex items-center justify-center flex-1 py-1 text-center border cursor-pointer favourite md:flex-none md:w-14"
            >
              <Icon icon="mdi:heart-outline" width="30" height="30" />
              <span class="mb-1 ml-2 md:hidden"> Add to Wishlist </span>
            </div>
          </div>
        </div>
      </div>
    </div>
    <transition name="toast">
      <Toast v-if="showToast">
        {{ toastMessage }}
      </Toast>
    </transition>
  </div>
</template>

<style scoped>
/* toast transition*/
.toast-leave-to {
  opacity: 0;
  transform: translateY(-50px);
}
.toast-leave-from {
  opacity: 1;
  transform: translateY(0);
}
.toast-enter-active {
  animation: wobble 0.8s ease;
}
.toast-leave-active {
  transition: all 0.5s ease;
}

@keyframes wobble {
  0% {
    transform: translateY(-60px);
    opacity: 0;
  }
  50% {
    transform: translateY(0px);
    opacity: 1;
  }
  60% {
    transform: translateX(8px);
  }
  70% {
    transform: translateX(-8px);
  }
  80% {
    transform: translateX(4px);
  }
  90% {
    transform: translateX(-4px);
  }
  100% {
    transform: translateX(0);
  }
}
/* end */

/* slide transition */
.slide-enter-active,
.slide-leave-active {
  transition:
    transform 0.1s ease,
    opacity 0.1s ease;
}

.slide-enter-from {
  transform: translateX(40px);
  opacity: 0;
}

.slide-leave-to {
  transform: translateX(-40px);
  opacity: 0;
}

/* fade transition */
.list-enter-from,
.list-leave-to {
  opacity: 0;
  transform: scale(0.6);
}
.list-enter-to,
.list-leave-from {
  opacity: 1;
  transform: scale(1);
}
.list-enter-active {
  transition: all 0.5s ease;
}
.list-leave-active {
  transition: all 0.5s ease;
  position: absolute;
}
.list-move {
  transition: all 0.3s ease;
}
</style>
