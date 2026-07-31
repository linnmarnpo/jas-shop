<template>
  <div class="py-6">
    <header class="text-center mb-6">
      <h3 class="text-xl font-bold uppercase tracking-wider text-gray-900">New Arrivals</h3>
      <p class="text-xs text-gray-500 mt-1">Check out our latest collection releases.</p>
    </header>
    <div class="py-4 mx-3 trending-container">
      <div
        class="ml-4 trending-item group cursor-pointer"
        v-for="product in productStore.productsUI"
        :key="product.id"
      >
        <router-link
          :to="{
            name: 'Detail',
            params: { productName: formatProductName(product.name) },
            query: { id: product.id },
          }"
          class="block overflow-hidden rounded-lg bg-gray-50 mb-3"
        >
          <img
            :src="productStore.getPrimaryImage(product)"
            :alt="product.name"
            class="w-full h-72 object-cover object-center group-hover:scale-105 transition-transform duration-300"
          />
        </router-link>
        <div class="flex items-center justify-between px-1 text-xs md:text-sm">
          <p class="font-medium text-gray-900 truncate max-w-[160px]">{{ product.name }}</p>
          <p class="font-bold text-gray-900">${{ product.price.toFixed(2) }}</p>
        </div>
        <div class="flex justify-between px-1 mt-2 items-center">
          <div class="flex items-center space-x-1">
            <span
              v-for="color in product.colors"
              :key="color"
              class="inline-block w-3 h-3 rounded-full border border-gray-300"
              :style="{ backgroundColor: color.toLowerCase() }"
            ></span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useProductStore } from "../stores/product";

const productStore = useProductStore();

const formatProductName = (name) => {
  return name.replace(/\s+/g, "-");
};

onMounted(async () => {
  await productStore.getLatestProducts();
});
</script>

<style scoped>
.trending-container {
  display: flex;
  overflow-x: auto;
  overflow-y: hidden;
  white-space: nowrap;
  gap: 1rem;
}
.trending-container p {
  padding: 0;
  margin: 0;
}
.trending-container .trending-item {
  text-align: start;
  width: 16rem;
  flex-shrink: 0;
}

.trending-container::-webkit-scrollbar {
  height: 4px;
}
.trending-container::-webkit-scrollbar-track {
  background: #f1f1f1;
}
.trending-container::-webkit-scrollbar-thumb {
  background: #c1c1c1;
  border-radius: 4px;
}
</style>
