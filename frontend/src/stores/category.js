import { defineStore } from "pinia";
import axios from "../service/axios";

export const useCategoryStore = defineStore("category", {
  state: () => ({
    categories: [],
    isLoading: false,
  }),

  actions: {
    async getCategories() {
      this.isLoading = true;
      try {
        let res = await axios.get("/categories");
        if (res) {
          this.categories = res.data.data;
        }
      } catch (e) {
        console.log(e);
      } finally {
        this.isLoading = false;
      }
    },

    async createCategory(data) {
      this.isLoading = true;
      try {
        await axios.post("/categories/create", data);
        this.getCategories();
        this.router.push({ name: "CategoryList" });
      } catch (error) {
      } finally {
        this.isLoading = false;
      }
    },

    async deleteCategory(id) {
      this.isLoading = true;
      try {
        await axios.delete("categories/delete/" + id);
      } catch (error) {
        console.log(error);
      } finally {
        this.isLoading = false;
      }
    },
  },
});
