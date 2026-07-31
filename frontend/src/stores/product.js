import { defineStore } from "pinia";
import axios from "../service/axios";

export const useProductStore = defineStore("product", {
  // =====================
  // STATE
  // =====================
  state: () => ({
    products: [],
    product: null,
    isLoading: false,
  }),

  // =====================
  // GETTERS
  // =====================
  getters: {
    productsUI: (state) => {
      return state.products.map((p) => ({
        ...p,
        colors: [...new Set(p.productVariants?.map((v) => v.color))],
        sizes: [...new Set(p.productVariants?.map((v) => v.size))],
      }));
    },

    productUI: (state) => {
      if (!state.product) return null;

      return {
        ...state.product,
        colors: [
          ...new Set(state.product.productVariants?.map((v) => v.color)),
        ],
        sizes: [...new Set(state.product.productVariants?.map((v) => v.size))],
      };
    },
  },

  // =====================
  // ACTIONS
  // =====================
  actions: {
    getPrimaryImage(product) {
      const primary = product?.resources?.find((res) => res.isPrimary);
      return primary?.url;
    },

    async addProduct(product) {
      this.isLoading = true;
      try {
        const res = await axios.post("/products/create", product);
        this.getProducts();
        this.router.push({ name: "ProductList" });
      } catch (error) {
        console.log(error);
      } finally {
        this.isLoading = false;
      }
    },

    async getProducts(categoryId = null) {
      this.isLoading = true;
      try {
        const res = await axios.get("/products", {
          params: categoryId ? { categoryId } : {},
        });

        this.products = res.data.data;
      } catch (error) {
        console.log(error);
      } finally {
        this.isLoading = false;
      }
    },

    async getLatestProducts() {
      // Fetch all products first (or assume already fetched)
      this.products = [];
      await this.getProducts();

      // Sort by id descending (newest first) and keep only top `limit`
      this.products = [...this.products]
        .sort((a, b) => b.id - a.id)
        .slice(0, 8);
    },

    async getProduct(id) {
      this.isLoading = true;
      try {
        const res = await axios.get(`/products/${id}`);
        this.product = res.data.data;
      } catch (error) {
        console.log(error);
      } finally {
        this.isLoading = false;
      }
    },

    async editProduct(id, payload) {
      this.isLoading = true;
      try {
        const res = await axios.put(`/products/edit/${id}`, payload);
        this.getProducts();
        this.getProduct(id);
        this.router.back();
      } catch (error) {
        console.log(error);
      } finally {
        this.isLoading = false;
      }
    },

    async deleteProduct(id) {
      this.isLoading = true;
      try {
        const res = await axios.delete(`/products/${id}`);
        this.products = this.products.filter((p) => p.id !== id);
      } catch (error) {
        console.log(error);
      } finally {
        this.isLoading = false;
      }
    },
  },
});
