<script setup>
import { computed, onMounted, ref } from "vue";
import { Icon } from "@iconify/vue";
import gsap from "gsap";
import { useProductStore } from "../stores/product";
import { useCategoryStore } from "../stores/category";
import { useRoute } from "vue-router";

const productStore = useProductStore();
const categoryStore = useCategoryStore();
const route = useRoute();

const categoryId = route.query.categoryId || null;
const catNav = ref("All");

if (categoryId) {
  const category = categoryStore.categories.find(
    (category) => category.id === Number(categoryId),
  );
  if (category) {
    catNav.value = category.name;
  } else {
    catNav.value = "All";
  }
}

const showSorting = ref(false);

const formatProductName = (name) => {
  return name.replace(/\s+/g, "-");
};

const beforeEnter = (el) => {
  el.style.opacity = 0;
  el.style.transform = "translateY(100px)";
};

const enter = (el, done) => {
  gsap.to(el, {
    opacity: 1,
    y: 0,
    duration: 0.8,
    delay: el.dataset.index * 0.1,
    onComplete: done,
  });
};

onMounted(() => {
  productStore.getProducts(categoryId);
  categoryStore.getCategories();
});
</script>
<template>
  <div>
    <!-- discount -->
    <div class="flex items-center justify-center w-full px-4 bg-gray-100 h-80">
      <h4 class="text-base text-center text-gray-700 md:text-xl">
        Get 40% discount on purchases over 70,000 MMK for only this month! Save
        big on your order and shop now for amazing deals.
      </h4>
    </div>

    <!-- Products -->
    <div class="mx-5 my-3 text-sm text-gray-700 md:text-lg">
      <!-- menu -->
      <div class="py-3 border-b border-b-black">
        <div class="flex items-center justify-between">
          <div>
            <span>Shop |</span>
            <span
              class="px-2 font-semibold"
              @click="showSorting = !showSorting"
            >
              {{ catNav }}</span
            >
          </div>
          <div>
            <span
              ><span class="text-xl font-semibold text-black">{{
                productStore.productsUI.length
              }}</span>
              items |
            </span>
            <span
              >Sortby:
              <span class="font-semibold" @click="showSorting = !showSorting"
                >sort</span
              ></span
            >
          </div>
        </div>
        <div
          v-if="showSorting"
          class="flex items-center justify-between mt-2 text-xs md:text-base"
        >
          <div class="flex items-center">
            <div class="cursor-pointer">↑↓</div>
            <div>
              <span
                class="ml-3 cursor-pointer"
                @click="(productStore.getProducts(null), (catNav = 'All'))"
                >All</span
              >
              <span
                class="ml-3 cursor-pointer"
                v-for="category in categoryStore.categories"
                :key="category"
                @click="
                  (productStore.getProducts(category.id),
                  (catNav = category.name))
                "
              >
                {{ category.name }}
              </span>
            </div>
          </div>
          <div class="text-gray-500">
            <span class="cursor-pointer hover:text-gray-900">Newest</span>
            <span class="mx-2 cursor-pointer hover:text-gray-900"
              >Low Price</span
            >
            <span class="cursor-pointer hover:text-gray-900">High Price</span>
          </div>
        </div>
      </div>

      <!-- products -->
      <div>
        <template v-if="productStore.productsUI.length">
          <transition-group
            appear=""
            tag="ul"
            @before-enter="beforeEnter"
            @enter="enter"
            class="items-center mx-4 py-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 border-b-2 border-gray-400 min-h-[30rem]"
          >
            <li
              v-for="(product, index) in productStore.productsUI"
              :key="index"
              :data-index="index + 1"
            >
              <router-link
                :to="{
                  name: 'Detail',
                  params: { productName: formatProductName(product.name) },
                  query: { id: product.id },
                }"
              >
                <div>
                  <img :src="productStore.getPrimaryImage(product)" />
                </div>
                <div class="flex justify-between px-2 pt-2 text-xs md:text-sm">
                  <p class="font-medium text-gray-900 truncate max-w-[150px]">{{ product.name }}</p>
                  <p class="font-bold text-gray-900">${{ product.price.toFixed(2) }}</p>
                </div>
                <div class="flex justify-between px-2 mt-1.5 items-center">
                  <div class="color flex items-center space-x-1">
                    <template v-for="color in product.colors" :key="color">
                      <span
                        class="inline-block w-3 h-3 rounded-full border border-gray-300"
                        :style="{ backgroundColor: color.toLowerCase() }"
                      ></span>
                    </template>
                  </div>
                </div>
              </router-link>
            </li>
          </transition-group>
        </template>

        <template v-else>
          <div
            class="items-center mx-4 py-10 grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4 border-b-2 border-gray-400 min-h-[30rem]"
          >
            <div v-for="n in 8" :key="n">
              <div
                class="flex items-center justify-center w-[400px] h-[20rem] bg-gray-300 rounded animate-pulse my-4"
              >
                <svg
                  class="w-10 h-10 text-gray-200 dark:text-gray-600"
                  aria-hidden="true"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="currentColor"
                  viewBox="0 0 20 18"
                >
                  <path
                    d="M18 0H2a2 2 0 0 0-2 2v14a2 2 0 0 0 2 2h16a2 2 0 0 0 2-2V2a2 2 0 0 0-2-2Zm-5.5 4a1.5 1.5 0 1 1 0 3 1.5 1.5 0 0 1 0-3Zm4.376 10.481A1 1 0 0 1 16 15H4a1 1 0 0 1-.895-1.447l3.5-7A1 1 0 0 1 7.468 6a.965.965 0 0 1 .9.5l2.775 4.757 1.546-1.887a1 1 0 0 1 1.618.1l2.541 4a1 1 0 0 1 .028 1.011Z"
                  />
                </svg>
              </div>
              <div class="flex justify-between px-3 py-2">
                <div
                  class="h-2.5 bg-gray-200 rounded-full animate-pulse w-24"
                ></div>
                <div
                  class="h-2.5 bg-gray-200 rounded-full animate-pulse w-24"
                ></div>
              </div>
              <div class="flex justify-between px-3 mt-2">
                <div>
                  <Icon icon="ic:round-color-lens" width="30" height="30" />
                </div>
                <div class="mt-1 color">
                  <span
                    v-for="n in 3"
                    :key="n"
                    class="inline-block w-4 h-4 ml-1 bg-gray-200 rounded-full animate-pulse"
                  ></span>
                </div>
              </div>
            </div>
          </div>
        </template>
      </div>
    </div>
  </div>
</template>
