import { defineStore } from "pinia";
import axios from "../service/axios";

export const useCartStore = defineStore("cart", {

    state:() => ({
        carts: [],
        isLoading: false
    }),

    getters: {
        totalAmount: (state) => {
            return state.carts.reduce((sum, item) => {
                return sum + item.price * item.quantity
            }, 0)
        }
    },

    actions: {
        clearCarts() {
            this.carts = [];
        },
        async getCarts() {
            try {
                const res = await axios.get('/carts');
                this.carts = res.data;
                console.log(this.carts)
            } catch (error) {
                console.log(error);
            }
        },
        async addItemToCart(data) {
            this.isLoading = true;
            try {
                await axios.post('/carts/create', data);
                this.getCarts();
            } catch (error) {
                console.log(error)
            }
            finally {
                this.isLoading = false;
            }
        },
        async deleteCart(id) {
            this.isLoading = true;
            try {
                await axios.delete(`/carts/delete/${id}`);
                this.carts = this.carts.filter(cart => cart.id !== id);
            } catch (error) {
                console.log(error)
            }
            finally {
                this.isLoading = false;
            }
        }
    }
})