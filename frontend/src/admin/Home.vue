<template>
    <div class="main w-full h-full flex bg-gray-100 select-none">
        <!-- side bar -->
        <transition name="width">
            <div v-if="showSideBar" class="w-[400px] h-screen bg-white border-r-2 transition duration-700 ease-in-out whitespace-nowrap">

                <div class="mx-4 mt-2 flex justify-between items-center">
                    <img  width="40" src="https://res.cloudinary.com/dz993lw3o/image/upload/v1709042763/logo_nmemlm.png" alt="JasMyanmar">
                    <h1 class="font-bold text-2xl text-red-600">Jas</h1>
                </div>

                <div class="nav flex px-4 items-center space-y-[10px] w-full">
                    <ul class="mt-4 w-full">
                        <li class="mb-1">
                            <router-link @click="showDropDown=false" :to="{ name: 'AdminDashboard'}" class="flex items-center py-[15px] px-[15px] text-sm md:text-base font-medium rounded-md hover:bg-gray-800 hover:text-white duration-300 ease-in">
                                <Icon icon="uil:setting" width="1.2rem" height="1.2rem" class="mr-2"/>
                                Dashboard
                            </router-link>
                        </li>
                        <li class="mb-1 group">
                            <span  @click="showDropDown=!showDropDown" :class="{'bg-gray-800 text-white': showDropDown, 'bg-white text-black': !showDropDown}" class="cursor-pointer flex items-center py-[15px] px-[15px] w-full text-sm md:text-base font-medium rounded-md hover:bg-gray-800 hover:text-white duration-300 ease-in">
                                <Icon icon="icon-park-outline:ad-product" width="1.2rem" height="1.2rem" class="mr-2"/>
                                <span>Products</span>
                                <Icon v-if="showDropDown"  class="ml-auto" icon="uim:angle-down" width="1.2rem" height="1.2rem" />
                                <Icon v-else class="ml-auto" icon="uim:angle-right" width="1.2rem" height="1.2rem" />
                            </span>
                            <transition name="height">
                                <ul v-if="showDropDown" class="block h-[175px] bg-slate-50 pl-6 mt-1 rounded-md">
                                    <li class="py-2">
                                        <router-link :to="{ name: 'ProductList'}" class=" flex items-center before:contents-[''] before:w-3 before:h-3 before:rounded-full before:border before:border-gray-500 before:mr-4 before:ml-2 hover:text-orange-400"
                                            exact-active-class="nav-exact-active"
                                        >
                                            <span>Product List</span>
                                        </router-link>
                                    </li>
                                    <li class="py-2">
                                        <router-link :to="{ name: 'addProduct'}" class=" flex items-center before:contents-[''] before:w-3 before:h-3 before:rounded-full before:border before:border-gray-500 before:mr-4 before:ml-2 hover:text-orange-400"
                                            exact-active-class="nav-exact-active"
                                        >
                                            <span>Add Product</span>
                                        </router-link>
                                    </li>
                                    <li class="py-2">
                                        <router-link :to="{ name: 'CategoryList'}" class=" flex items-center before:contents-[''] before:w-3 before:h-3 before:rounded-full before:border before:border-gray-500 before:mr-4 before:ml-2 hover:text-orange-400"
                                            exact-active-class="nav-exact-active"
                                        >
                                            <span>Category List</span>
                                        </router-link>
                                    </li>
                                    <li class="py-2">
                                        <router-link :to="{ name: 'addCategory'}" class=" flex items-center before:contents-[''] before:w-3 before:h-3 before:rounded-full before:border before:border-gray-500 before:mr-4 before:ml-2 hover:text-orange-400"
                                            exact-active-class="nav-exact-active"
                                        >
                                            <span>Add Category</span>
                                        </router-link>
                                    </li>
                                </ul>
                            </transition>
                        </li>
                        <li class="mb-1">
                            <router-link @click="showDropDown=false" :to="{ name: 'OrderList'}" class="flex items-center py-[15px] px-[15px] text-sm md:text-base font-medium rounded-md hover:bg-gray-800 hover:text-white duration-300 ease-in">
                                <Icon class="mr-2" icon="ep:shopping-cart" width="1.2rem" height="1.2rem" />
                                Orders
                            </router-link>
                        </li>
                    </ul>
                </div>
            </div>
        </transition>

        <!-- main  -->
        <div class="w-full h-screen">
            <div class="px-4 py-3 flex justify-between items-center bg-white shadow-sm fixed top-0 w-full">
                <div class="cursor-pointer">
                    <Icon v-if="showSideBar" @click="showSideBar = !showSideBar" icon="mdi:hamburger-open" width="32" height="32" />
                    <Icon v-else @click="showSideBar = !showSideBar" icon="mdi:hamburger-close" width="32" height="32" />
                </div>
                <div>
                    <input class="px-3 py-2 rounded-md focus:outline-none border" type="text" placeholder="type here to search...">
                </div>
            </div>
            <div class="side-container mx-4">
                <router-view></router-view>
            </div>
        </div>
    </div>
</template>

<script >
import { Icon } from '@iconify/vue';
export default {
    data(){
        return{
            user: {},
            active: false,
            showSideBar: true,
            showDropDown: false,
        }
    },
    components:{
        Icon
    },
    methods: {

    },
    mounted(){
        
    }
}
</script>

<style scoped>
    .side-container{
        height: 100%;
        overflow-y: auto;
    }
    .side-container::-webkit-scrollbar
    {
        width: 2px;
    }
    .side-container::-webkit-scrollbar-track
    {
        border-radius: 12px;
        background: rgb(156, 148, 148);
    }
    .side-container::-webkit-scrollbar-thumb
    {
        border-radius: 12px;
        outline: 1.5px solid rgb(68, 64, 64);
        height: 3px;
        background: rgb(68 64 60);
    }

    .main{
        overflow: hidden;
    }    
    .width-enter-from,.width-leave-to{
        width: 0;
    }

    .width-enter-to,.width-leave-from{
        width: 400px;
    }

    .width-enter-active,.width-leave-active{
        transition: all 0.5s ease-in-out;
        overflow: hidden;
    }

    .height-enter-from,.height-leave-to{
        height: 0;
    }

    .height-enter-to,.height-leave-from{
        height: 175px;
    }

    .height-enter-active,.height-leave-active{
        transition: all 0.5s ease-in-out;
        overflow: hidden;
    }

    .nav .router-link-exact-active{
        background-color: rgb(31 41 55);
        color: white;
    }
    .nav-exact-active{
        color: rgb(251 146 60);
    }
    .nav-exact-active::before {
        border: none;
        background-color: rgb(251 146 60);
    }
    

</style>