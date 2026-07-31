<template>
  <div>
    <div class="mt-[115px] flex justify-between items-center font-semibold">
      <h1 class="text-xl">Add a Category</h1>
    </div>

    <div class="px-4 py-4 mt-6 bg-white rounded">
      <form @submit.prevent="categoryStore.createCategory(data)">
        <div class="max-w-sm mb-5">
          <label
            for="category"
            class="block mb-2 text-lg font-medium text-gray-900"
            >Category Name</label
          >
          <input
            class="block w-full px-3 py-3 text-gray-900 rounded-lg focus:outline-orange-200"
            :class="{
              'border-1 border-red-300': errors.name,
              border: !errors.name,
            }"
            v-model="data.name"
          />
          <p v-if="errors.name" class="mt-2 text-sm text-red-500">
            {{ errors.name }}
          </p>
        </div>
        <div class="w-full mb-5">
          <label
            for="category"
            class="block mb-2 text-lg font-medium text-gray-900"
            >Category Description</label
          >
          <textarea
            rows="4"
            class="block w-full px-3 py-3 text-gray-900 border rounded-lg focus:outline-orange-200 min-h-40"
            v-model="data.description"
          ></textarea>
        </div>
        <div class="mb-6">
          <label class="block mb-2 text-lg font-medium text-gray-900">
            Category Image
          </label>

          <div
            class="relative w-full max-w-sm"
            @dragover.prevent
            @drop.prevent="onDrop"
          >
            <!-- Upload Card -->
            <div
              @click="openPicker"
              class="flex flex-col items-center justify-center w-full transition border-2 border-dashed cursor-pointer h-52 rounded-xl bg-gray-50 hover:border-orange-400"
            >
              <!-- Preview -->
              <img
                v-if="data.imageUrl"
                :src="data.imageUrl"
                class="absolute inset-0 object-cover w-full h-full rounded-xl"
              />

              <!-- Overlay -->
              <div
                class="absolute inset-0 flex flex-col items-center justify-center text-white bg-black/40 rounded-xl"
                v-if="data.imageUrl"
              >
                <p class="text-sm font-semibold">Click to change</p>
              </div>

              <!-- Placeholder -->
              <div v-if="!data.imageUrl" class="text-center text-gray-400">
                <p class="text-sm font-semibold">Click or Drag image</p>
                <p class="mt-1 text-xs">PNG / JPG</p>
              </div>
            </div>

            <!-- Remove Button -->
            <button
              v-if="data.imageUrl"
              @click.stop="removeImage"
              class="absolute px-2 py-1 text-xs text-white bg-red-500 rounded top-2 right-2 hover:bg-red-600"
            >
              Remove
            </button>

            <!-- Hidden File Input -->
            <input
              ref="fileInput"
              type="file"
              accept="image/*"
              class="hidden"
              @change="onFileChange"
            />
          </div>
        </div>
        <div>
          <input
            type="submit"
            value="Submit"
            :disabled="isLoading"
            :class="{ 'cursor-progress': isLoading }"
            class="px-12 py-3 font-semibold text-white bg-orange-400 rounded"
          />
          <button
            @click="cancel()"
            class="px-12 py-3 ml-3 font-semibold text-white bg-gray-500 rounded hover:bg-gray-600"
          >
            Cancel
          </button>
        </div>
      </form>
    </div>
  </div>
</template>

<script setup>
import { ref } from "vue";
import { useRouter } from "vue-router";
import axios from "../../service/axios";
import { useCategoryStore } from "../../stores/category";

const router = useRouter();
const isLoading = ref(false);

const categoryStore = useCategoryStore();

const data = ref({
  name: "",
  description: "",
  imageUrl: "",
});

const errors = ref({});

/* image */
const fileInput = ref(null);

/* navigation */
const cancel = () => {
  router.go(-1);
};

/* file picker */
const openPicker = () => {
  fileInput.value.click();
};

const onFileChange = async (e) => {
  const file = e.target.files[0];
  if (!file) return;

  await uploadImage(file);
  e.target.value = "";
};

const onDrop = async (e) => {
  const file = e.dataTransfer.files[0];
  if (!file) return;

  await uploadImage(file);
};

/* upload image */
const uploadImage = async (file) => {
  const formData = new FormData();
  formData.append("image", file);
  if (!data.value.name) {
    return;
  }
  formData.append("name", data.value.name);
  const res = await axios.post("/images/upload", formData);

  data.value.imageUrl = axios.defaults.baseURL + res.data;
};

/* remove image */
const removeImage = () => {
  data.value.image = null;
  data.imageUrl.value = null;
  fileInput.value.value = "";
};
</script>
