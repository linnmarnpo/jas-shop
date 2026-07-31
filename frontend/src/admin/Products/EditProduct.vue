<script setup>
import { ref, onMounted, watch } from "vue";
import { useRoute, useRouter } from "vue-router";
import { Icon } from "@iconify/vue";
import axios from "../../service/axios";
import { useCategoryStore } from "../../stores/category";
import { useProductStore } from "../../stores/product";

const categoryStore = useCategoryStore();
const productStore = useProductStore();
const route = useRoute();
const router = useRouter();

const productId = route.params.id;
const error = ref(false);
const fileInputs = ref([]);

const colors = [
  "Red",
  "White",
  "Black",
  "Blue",
  "Green",
  "Yellow",
  "Gray",
  "Pink",
  "Orange",
  "Purple",
];

const sizes = [
  { label: "Small", code: "S" },
  { label: "Medium", code: "M" },
  { label: "Large", code: "L" },
];

const addVariant = () => {
  const product = productStore.product;
  if (!product) return;

  product.productVariants ??= [];
  product.productVariants.push({
    size: sizes[0].code,
    color: colors[0],
    stockQuantity: 0,
  });
};

const removeVariant = (index) => {
  const product = productStore.product;
  if (!product?.productVariants) return;

  product.productVariants.splice(index, 1);
};

//  Sync Resources With Variant Colors
watch(
  () => productStore.product?.productVariants?.map((v) => v.color),
  (newColors = []) => {
    const product = productStore.product;
    if (!product) return;

    product.resources ??= [];

    // Add missing resources
    newColors.forEach((color) => {
      const exists = product.resources.find((r) => r.color === color);
      if (!exists) {
        product.resources.push({
          name: "",
          url: "",
          isPrimary: false,
          color,
        });
      }
    });

    // Remove unused resources
    product.resources = product.resources.filter((r) =>
      newColors.includes(r.color),
    );
  },
  { deep: true },
);

// image upload
const openFilePicker = (index) => {
  fileInputs.value[index]?.click();
};

const uploadImage = async (file, index) => {
  const product = productStore.product;
  if (!product?.name) {
    error.value = true;
    return;
  }

  const formData = new FormData();
  formData.append("image", file);
  formData.append("name", product.name);

  const res = await axios.post("/images/upload", formData);

  product.resources[index].url = axios.defaults.baseURL + res.data;
};

const onFileChange = (e, index) => {
  const file = e.target.files[0];
  if (file) uploadImage(file, index);
  e.target.value = "";
};

const onDrop = (e, index) => {
  e.preventDefault();
  const file = e.dataTransfer.files?.[0];
  if (file) uploadImage(file, index);
};

// primary image
const setPrimary = (selectedIndex) => {
  const resources = productStore.product?.resources;
  if (!resources) return;

  resources.forEach((r, i) => {
    r.isPrimary = i === selectedIndex;
  });
};

const editProduct = () => {
  productStore.editProduct(productId, {
    ...productStore.product,
    categoryId: productStore.product.category?.id,
  });
};

onMounted(() => {
  categoryStore.getCategories();
  productStore.getProduct(productId);
});
</script>
<template>
  <div>
    <div class="mt-[115px] flex justify-between items-center font-semibold">
      <h1 class="text-2xl text-gray-800">Edit Product</h1>
    </div>
    <div class="px-4 py-4 mt-6 bg-white rounded shadow-sm">
      <form v-if="productStore.product" @submit.prevent="editProduct">
        <div class="grid w-full grid-cols-3 gap-6">
          <div>
            <label
              for="category"
              class="block mb-2 text-sm font-medium text-gray-700"
              >Product Name</label
            >
            <input
              v-model="productStore.product.name"
              class="block w-full px-3 py-3 text-gray-900 placeholder-gray-400 border rounded-lg focus:outline-orange-200"
              placeholder="Enter product name"
            />
            <p v-if="error" class="mt-2 text-sm text-red-500">
              Please enter a product name
            </p>
          </div>
          <div>
            <label
              for="price"
              class="block mb-2 text-sm font-medium text-gray-700"
              >Price</label
            >
            <input
              class="block w-full px-3 py-3 text-gray-900 placeholder-gray-400 border rounded-lg focus:outline-orange-200"
              v-model="productStore.product.price"
              placeholder="Enter price"
            />
          </div>
          <div>
            <label
              for="category"
              class="block mb-2 text-sm font-medium text-gray-700"
              >Category</label
            >
            <select
              name="category"
              class="block w-full px-3 py-3 text-gray-900 border rounded-lg focus:outline-orange-200"
              v-model="productStore.product.category.id"
            >
              <option
                v-for="category in categoryStore.categories"
                :key="category"
                :value="category.id"
                class="py-2 text-gray-900.bg-white hover:bg-orange-50"
              >
                {{ category.name }}
              </option>
            </select>
          </div>
        </div>
        <!-- for product variants -->
        <h3 class="mt-5 font-semibold text-gray-700 text-md">Variants</h3>
        <div
          v-for="(variant, index) in productStore.product.productVariants || []"
          :key="index"
          class="grid items-center w-full grid-cols-4 gap-4 mt-3"
        >
          <div class="relative">
            <select
              name="size"
              class="block w-full px-3 py-3 text-gray-900 border rounded-lg focus:outline-orange-200"
              v-model="variant.size"
            >
              <option
                v-for="size in sizes"
                :key="size.code"
                :value="size.code"
                class="py-2 text-gray-900 bg-white hover:bg-orange-50"
              >
                {{ size.label }}
              </option>
            </select>
          </div>
          <div>
            <select
              name="size"
              class="block w-full px-3 py-3 text-gray-900 border rounded-lg focus:outline-orange-200"
              v-model="variant.color"
            >
              <option
                v-for="color in colors"
                :key="color"
                :value="color"
                class="py-2 text-gray-900 bg-white hover:bg-orange-50"
              >
                {{ color }}
              </option>
            </select>
          </div>
          <div>
            <input
              v-model="variant.stockQuantity"
              class="block w-full px-3 py-3 text-gray-900 placeholder-gray-400 border rounded-lg focus:outline-orange-200"
              placeholder="Stock quantity"
            />
          </div>
          <button
            v-if="(productStore.product.productVariants || []).length > 1"
            type="button"
            @click="removeVariant(index)"
            class="flex items-center justify-center w-8 h-8 mt-6 text-gray-600 border border-orange-300 rounded-full cursor-pointer hover:bg-orange-400 hover:text-white hover:border-orange-400"
          >
            <Icon icon="mdi:delete-outline" width="16" />
          </button>
        </div>
        <button
          @click="addVariant"
          type="button"
          class="flex items-center justify-center w-8 h-8 mt-2 text-gray-600 border border-orange-300 rounded-full cursor-pointer hover:bg-orange-400 hover:text-white hover:border-orange-400"
        >
          <Icon icon="mdi:plus" width="16" />
        </button>

        <!-- for resourcesImg -->
        <h3 class="mt-5 font-semibold text-gray-700 text-md">
          Product resources
        </h3>
        <div
          v-for="(resource, index) in productStore.product.resources || []"
          :key="index"
          class="grid items-center w-full grid-cols-5 gap-4 mt-2"
        >
          <!-- Name -->
          <div>
            <input
              v-model="resource.name"
              class="block w-full px-3 py-3 text-gray-900 placeholder-gray-400 border rounded-lg focus:outline-orange-200"
              placeholder="Name"
            />
          </div>

          <!-- Color -->
          <div>
            <select
              v-model="resource.color"
              class="block w-full px-3 py-3 text-gray-900 border rounded-lg cursor-not-allowed focus:outline-orange-200"
              disabled
            >
              <option disabled value="">Select your image color</option>
              <option v-for="color in colors" :key="color" :value="color">
                {{ color }}
              </option>
            </select>
          </div>

          <!-- Image / Upload -->
          <div class="flex flex-col items-center">
            <!-- Preview -->
            <div
              v-if="productStore.product.resources[index]?.url"
              class="w-[100px] h-[100px] rounded-xl overflow-hidden cursor-pointer bg-white shadow-md ring-1 ring-gray-200 transition hover:shadow-xl hover:-translate-y-1 hover:ring-orange-300"
            >
              <img
                :src="productStore.product.resources[index].url"
                class="object-cover w-full h-full"
              />
            </div>

            <!-- Upload -->
            <div
              v-else
              @click="openFilePicker(index)"
              @dragover.prevent
              @drop="onDrop($event, index)"
              class="w-[100px] h-[100px] border-2 border-dashed rounded flex flex-col items-center justify-center cursor-pointer text-green-500 hover:border-orange-300 hover:text-orange-500"
            >
              <Icon icon="mdi:plus" width="16" />
              <span class="text-sm">Add image</span>
              <input
                type="file"
                accept="image/*"
                class="hidden"
                :ref="(el) => (fileInputs[index] = el)"
                @change="onFileChange($event, index)"
              />
            </div>

            <!-- Primary image checkbox -->
            <div class="flex items-center gap-1">
              <input
                type="checkbox"
                class="checkbox"
                v-model="resource.isPrimary"
                @change="setPrimary(index)"
              />
              <label class="text-base label-text">is Primary Image</label>
            </div>
          </div>
        </div>
        <div class="mt-3">
          <input
            type="submit"
            value="Update"
            :disabled="productStore.isLoading"
            :class="{ 'cursor-progress': productStore.isLoading }"
            class="px-12 py-3 font-semibold text-white bg-orange-400 rounded cursor-pointer hover:bg-orange-500 focus:outline-none focus:ring-2 focus:ring-orange-300"
          />
          <button
            @click="router.back()"
            type="button"
            class="px-12 py-3 ml-3 font-semibold text-white bg-gray-500 rounded hover:bg-gray-600 focus:outline-none focus:ring-2 focus:ring-gray-300"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  </div>
</template>
