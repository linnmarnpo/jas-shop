<template>
  <div class="px-1 py-10 mx-10 my-4 border-b border-gray-400 md:px-4">
    <div
      class="grid items-center grid-cols-1 gap-4 catgories-container sm:grid-cols-2 md:grid-cols-2 lg:grid-cols-4"
    >
      <div
        v-for="category in categoryStore.categories"
        :key="category"
        class="catgories-item"
      >
        <router-link
          :to="{
            name: 'shop',
            query: { categoryId: category.id },
          }"
          class="block text-center"
        >
          <img :src="category.imageUrl" alt="" />
          <p>{{ category.name }}</p>
        </router-link>
      </div>
    </div>
  </div>
</template>

<script setup>
import { onMounted } from "vue";
import { useCategoryStore } from "../stores/category";

const categoryStore = useCategoryStore();

onMounted(() => {
  categoryStore.getCategories();
});
</script>

<style scoped>
.catgories-container div {
  position: relative;
  display: inline-block;
  overflow: hidden;
}

.catgories-container p {
  position: absolute;
  bottom: 0;
  left: 0;
  width: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  color: white;
  text-align: center;
  padding: 10px;
  margin: 0;
}

.catgories-item a img {
  width: 100%;
  height: 28rem;
  transition: 0.7s ease-in-out;
}

.catgories-item a:hover img {
  transform: scale(1.15);
}
</style>
