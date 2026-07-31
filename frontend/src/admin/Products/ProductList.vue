<template>
  <div>
    <!-- Header -->
    <div class="mt-[100px] flex justify-between items-center font-semibold">
      <h1 class="text-xl">Product List</h1>

      <router-link
        :to="{ name: 'addProduct' }"
        class="flex items-center justify-between px-3 py-3 text-white bg-orange-400 rounded"
      >
        <Icon icon="ep:plus" width="25" height="25" />
        <span class="ml-2">Add a Product</span>
      </router-link>
    </div>

    <!-- Delete Modal -->
    <transition name="fade">
      <ModalBox v-if="showModalDelete" @close="showModalDelete = false">
        <div class="px-3 py-3 text-2xl">
          <Icon
            class="mx-auto mt-4"
            icon="weui:delete-filled"
            width="70"
            height="70"
            style="color: rgb(168 85 247)"
          />
          <h2 class="mt-4 font-semibold text-center">Are you Sure!</h2>
          <p class="mt-4 text-lg text-center text-gray-500">
            Do you really want to delete this product?
          </p>

          <div class="mt-4 mb-2 text-right">
            <button
              @click="showModalDelete = false"
              class="px-4 py-2 text-sm text-gray-600 hover:underline"
            >
              Cancel
            </button>

            <button
              @click="handleDelete"
              :disabled="productStore.isLoading"
              :class="{ 'cursor-progress': productStore.isLoading }"
              class="px-4 py-2 mr-2 text-base text-white bg-red-500 rounded hover:bg-red-400"
            >
              Delete
            </button>
          </div>
        </div>
      </ModalBox>
    </transition>

    <!-- Success Modal -->
    <transition name="fade">
      <ModalBox v-if="deleteSuccess" @close="deleteSuccess = false">
        <div class="px-3 py-3 text-2xl">
          <Icon
            icon="icon-park-solid:success"
            class="mx-auto mt-4"
            width="70"
            height="70"
            style="color: rgb(168 85 247)"
          />
          <h2 class="mt-4 font-semibold text-center">Deleted successfully!</h2>
          <p class="mt-4 text-lg text-center text-gray-500">
            The product has been successfully deleted.
          </p>

          <div class="mt-4 mb-2 text-right">
            <button
              @click="deleteSuccess = false"
              class="px-4 py-2 mr-2 text-sm text-white bg-red-500 rounded hover:bg-red-400"
            >
              Close
            </button>
          </div>
        </div>
      </ModalBox>
    </transition>

    <!-- Table Section -->
    <div class="px-4 py-4 mt-6 bg-white rounded">
      <!-- Search + Count -->
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
          <span>{{ productStore.productsUI.length }}</span>
          <span class="mx-2">-</span>
          <span>Products</span>
        </div>
      </div>

      <!-- Table -->
      <div class="mt-4 overflow-x-auto sm:rounded-lg">
        <table class="w-full text-base text-left text-gray-700 rounded">
          <thead class="text-gray-900 uppercase bg-gray-100">
            <tr>
              <th class="px-6 py-3">Product name</th>
              <th class="px-6 py-3">Price</th>
              <th class="px-6 py-3">Category</th>
              <th class="px-6 py-3">Action</th>
            </tr>
          </thead>

          <!-- Data -->
          <tbody v-if="productStore.productsUI.length">
            <template v-if="filteredProducts.length">
              <tr
                v-for="product in filteredProducts"
                :key="product.id"
                class="border-b hover:bg-slate-50"
              >
                <th
                  class="flex items-center px-6 py-3 font-medium whitespace-nowrap"
                >
                  <img
                    width="45"
                    height="45"
                    :src="productStore.getPrimaryImage(product)"
                    alt=""
                  />
                  <div class="ml-3">
                    {{ product.name }}
                  </div>
                </th>

                <td class="px-6 py-3">{{ product.price }} $</td>

                <td class="px-6 py-3">
                  {{ product.category?.name }}
                </td>

                <td class="px-6 py-3">
                  <div class="flex">
                    <Icon
                      class="cursor-pointer"
                      @click="
                        router.push({
                          name: 'editProduct',
                          params: { id: product.id },
                        })
                      "
                      icon="circum:edit"
                      width="32"
                      height="32"
                      style="color: blue"
                    />

                    <Icon
                      class="ml-2 cursor-pointer"
                      @click="openDeleteModal(product.id)"
                      icon="solar:trash-bin-2-bold-duotone"
                      width="32"
                      height="32"
                      style="color: red"
                    />
                  </div>
                </td>
              </tr>
            </template>

            <!-- No search result -->
            <tr v-else>
              <td colspan="6" class="py-4 text-center text-red-500">
                No product found
              </td>
            </tr>
          </tbody>

          <!-- Skeleton -->
          <tbody v-else>
            <tr v-for="n in 3" :key="n" class="border-b">
              <td class="px-6 py-4" colspan="6">
                <div class="h-4 bg-gray-200 rounded animate-pulse"></div>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from "vue";
import { useRouter } from "vue-router";
import { Icon } from "@iconify/vue";
import ModalBox from "../../components/ModalBox.vue";
import { useProductStore } from "../../stores/product";

// Pinia store
const productStore = useProductStore();
const router = useRouter();

// State
const search = ref("");
const showModalDelete = ref(false);
const deleteSuccess = ref(false);
const deletePdId = ref(null);

const filteredProducts = computed(() => {
  return productStore.productsUI.filter((product) =>
    product.name.toLowerCase().includes(search.value.toLowerCase()),
  );
});

// Open delete modal
const openDeleteModal = (id) => {
  deletePdId.value = id;
  showModalDelete.value = true;
};

// Delete handler
const handleDelete = async () => {
  await productStore.deleteProduct(deletePdId.value);
  showModalDelete.value = false;
  deleteSuccess.value = true;
};

// Mount
onMounted(() => {
  productStore.getProducts();
});
</script>

<style scoped>
.fade-enter,
.fade-leave-to {
  opacity: 0;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 500ms ease-out;
}
</style>
