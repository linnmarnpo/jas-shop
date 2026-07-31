// users for card

import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
    const user = ref(
        {
            id: '',
            name: '',
            email: '',
            phone: '',
            password: '',
            balance: 0,
            isAdmin: false,
        }
    );

    // Function to load user data from localStorage
    const loadUser = () => {
        const storedUser = localStorage.getItem("user");
        user.value = storedUser ? JSON.parse(storedUser) : null;
    };

    // Function to set user (called after login)
    const setUser = (newUser) => {
        user.value = newUser;
        if (newUser) {
            localStorage.setItem("user", JSON.stringify(newUser));
        } else {
            localStorage.removeItem("user");
        }
    };

    const logout = () => {
        user.value = {
            name: '',
            email: '',
            phone: '',
            password: '',
            balance: 0
        };
        localStorage.removeItem("user");
    };

    return { user, loadUser, setUser, logout };
});
