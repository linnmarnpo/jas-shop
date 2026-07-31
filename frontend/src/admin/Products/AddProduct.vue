<script setup>
import { onMounted, ref, watch } from "vue";
import { Icon } from "@iconify/vue";
import axios from "../../service/axios";
import { useCategoryStore } from "../../stores/category";
import { useProductStore } from "../../stores/product";

const error = ref(null);

const colors = ref([
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
]);
const sizes = ref([
  { label: "Small", code: "S" },
  { label: "Medium", code: "M" },
  { label: "Large", code: "L" },
]);

const categoryStore = useCategoryStore();
const productStore = useProductStore();

const productName = ref("");
const categoryId = ref("");
const price = ref(0);

const variants = ref([
  { size: sizes.value[0].code, color: colors.value[0], stockQuantity: 0 },
]);
const resources = ref([
  { name: "", url: "", isPrimary: false, color: colors.value[0] },
]);

const setPrimary = (selectedIndex) => {
  resources.value.forEach((res, i) => {
    res.isPrimary = i === selectedIndex;
  });
};

const addVariant = () => {
  variants.value.push({
    size: sizes.value[0].code,
    color: colors.value[0],
    stockQuantity: 0,
  });
};
const removeVariant = (index) => {
  variants.value.splice(index, 1);
  resources.value.splice(index, 1);
  preview.value.splice(index, 1);
};
const addProduct = async () => {
  await productStore.addProduct({
    name: productName.value,
    categoryId: categoryId.value,
    price: price.value,
    productVariants: variants.value,
    resources: resources.value,
  });
};

const fileInputs = ref([]);

/* upload image */
const openFilePicker = (index) => {
  fileInputs.value[index]?.click();
};
const onFileChange = async (e, index) => {
  const file = e.target.files[0];
  if (!file) return;

  await uploadImage(file, index);
  e.target.value = ""; // Reset the input
};

const onDrop = async (e, index) => {
  e.preventDefault();
  const files = e.dataTransfer?.files;
  if (!files || !files.length) return;

  const file = files[0];

  await uploadImage(file, index);
};

const uploadImage = async (file, index) => {
  const formData = new FormData();
  formData.append("image", file);
  if (!productName.value) {
    error.value = true;
    return;
  }
  formData.append("name", productName.value);
  const res = await axios.post("/images/upload", formData);
  resources.value[index].url = axios.defaults.baseURL + res.data;
};

onMounted(() => {
  categoryStore.getCategories();
});

watch(
  () => variants.value.map((v) => v.color),
  (newColors) => {
    // ADD resource groups that is based on new colors
    newColors.forEach((color) => {
      const exists = resources.value.find((r) => r.color === color);

      if (!exists) {
        resources.value.push({
          name: "",
          url: "",
          isPrimary: false,
          color: color,
        });
      }
    });

    // REMOVE unused resource based on unique color
    resources.value = resources.value.filter((resource) =>
      newColors.includes(resource.color),
    );
  },
  { immediate: true },
);
</script>
<template>
  <div>
    <div class="mt-[115px] flex justify-between items-center font-semibold">
      <h1 class="text-2xl text-gray-800">Add a Product</h1>
    </div>
    <div class="px-4 py-4 mt-6 bg-white rounded shadow-sm">
      <form @submit.prevent="addProduct">
        <div class="grid w-full grid-cols-3 gap-6">
          <div>
            <label
              for="category"
              class="block mb-2 text-sm font-medium text-gray-700"
              >Product Name</label
            >
            <input
              v-model="productName"
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
              v-model="price"
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
              v-model="categoryId"
            >
              <option
                v-for="category in categoryStore.categories"
                :key="category"
                :value="category.id"
                class="py-2 text-gray-900 bg-white hover:bg-orange-50"
              >
                {{ category.name }}
              </option>
            </select>
          </div>
        </div>

        <!-- for product variants -->
        <h3 class="mt-5 font-semibold text-gray-700 text-md">Variants</h3>
        <div
          v-for="(variant, index) in variants"
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
            v-if="variants.length > 1"
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
          v-for="(resource, index) in resources"
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
              v-if="resources[index].url"
              class="w-[100px] h-[100px] rounded-xl overflow-hidden cursor-pointer bg-white shadow-md ring-1 ring-gray-200 transition hover:shadow-xl hover:-translate-y-1 hover:ring-orange-300"
            >
              <img
                :src="resources[index].url"
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
            value="Submit"
            :disabled="productStore.isLoading"
            :class="{ 'cursor-progress': productStore.isLoading }"
            class="px-12 py-3 font-semibold text-white bg-orange-400 rounded cursor-pointer hover:bg-orange-500 focus:outline-none focus:ring-2 focus:ring-orange-300"
          />
          <button
            @click="cancel()"
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
