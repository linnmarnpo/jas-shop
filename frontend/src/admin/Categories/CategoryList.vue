<script setup>
import { ref, computed, onMounted } from "vue";
import { Icon } from "@iconify/vue";
import { useCategoryStore } from "../../stores/category";

const categoryStore = useCategoryStore();

const search = ref("");

onMounted(() => {
  categoryStore.getCategories();
});

const filteredCategories = computed(() =>
  categoryStore.categories.filter((category) =>
    category.name.toLowerCase().includes(search.value.toLowerCase()),
  ),
);
</script>

<template>
  <div>
    <div class="mt-[100px] flex justify-between items-center font-semibold">
      <h1 class="text-xl">Product Category List</h1>
      <router-link
        :to="{ name: 'addCategory' }"
        class="flex items-center justify-between px-3 py-3 text-white bg-orange-400 rounded"
      >
        <Icon icon="ep:plus" width="25" height="25" />
        <span class="ml-2">Add Category</span>
      </router-link>
    </div>
    <div class="px-4 py-4 mt-6 bg-white rounded">
      <div class="flex justify-between">
        <div class="flex items-center border px-3 py-2.5 rounded">
          <Icon icon="icon-park-twotone:search" width="20" height="20" />
          <input
            type="text"
            v-model="search"
            placeholder="Search..."
            class="ml-3 focus:outline-none"
          />
        </div>
        <div class="flex items-center px-3 py-2 text-white bg-gray-800 rounded">
          <span>{{ categoryStore.categories.length }}</span>
          <span class="mx-2">-</span>
          <span>Categories</span>
        </div>
      </div>

      <div class="relative mt-4 overflow-x-auto sm:rounded-lg">
        <table
          class="w-full text-base text-left text-gray-700 rounded rtl:text-right"
        >
          <thead class="text-gray-900 uppercase bg-gray-100">
            <tr>
              <th scope="col" class="px-6 py-3">Category name</th>
              <th scope="col" class="px-6 py-3">Description</th>
              <th scope="col" class="px-6 py-3">Image</th>
              <th scope="col" class="px-6 py-3">Action</th>
            </tr>
          </thead>
          <tbody v-if="categoryStore.categories.length">
            <template v-if="filteredCategories.length">
              <tr
                v-for="category in filteredCategories"
                :key="category"
                class="border-b hover:bg-slate-50"
              >
                <th scope="row" class="px-6 py-4 font-medium whitespace-nowrap">
                  {{ category.name }}
                </th>
                <td class="px-6 py-4">{{ category.description }}</td>
                <td class="px-6 py-4">
                  <img width="45" height="45" :src="category.imageUrl" alt="" />
                </td>
                <td class="flex px-6 py-4">
                  <span href="#" class="cursor-pointer">
                    <Icon
                      icon="circum:edit"
                      width="32"
                      height="32"
                      style="color: blue"
                    />
                  </span>
                  <span
                    href="#"
                    class="ml-3 cursor-pointer"
                    @click="deleteCategories(category.id)"
                  >
                    <Icon
                      icon="solar:trash-bin-2-bold-duotone"
                      width="32"
                      height="32"
                      style="color: red"
                    />
                  </span>
                </td>
              </tr>
            </template>

            <tr v-else class="border-b hover:bg-slate-50">
              <td class="py-4 text-base text-center" colspan="4">
                <span class="text-red-500">No category found</span>
              </td>
            </tr>
          </tbody>
          <tbody v-else>
            <tr v-for="n in 3" :key="n" class="border-b">
              <td class="px-6 py-4">
                <div
                  class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"
                ></div>
              </td>
              <td class="px-6 py-4">
                <div
                  class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"
                ></div>
              </td>
              <td class="px-6 py-4">
                <div
                  class="h-2.5 bg-gray-200 rounded-full w-24 animate-pulse"
                ></div>
              </td>
              <td class="px-6 py-4">
                <div class="flex space-x-3">
                  <div
                    class="w-8 h-8 bg-gray-200 rounded-full animate-pulse"
                  ></div>
                  <div
                    class="w-8 h-8 bg-gray-200 rounded-full animate-pulse"
                  ></div>
                </div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<style></style>
