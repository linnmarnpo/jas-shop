<template>
  <footer id="info" class="bg-gray-900 text-white mt-16 border-t border-gray-800">
    <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-12">
      <div class="grid grid-cols-1 md:grid-cols-4 gap-8">
        
        <!-- Column 1: Brand Info -->
        <div class="space-y-4">
          <div class="flex items-center space-x-2">
            <img
              width="40"
              src="https://res.cloudinary.com/dz993lw3o/image/upload/v1709042763/logo_nmemlm.png"
              alt="JAS Logo"
              class="h-9 w-auto"
            />
            <span class="font-extrabold text-xl tracking-wider uppercase text-white">JAS</span>
          </div>
          <p class="text-xs text-gray-400 leading-relaxed">
            Premium streetwear & curated fashion collections. Quality craftsmanship designed for modern everyday style.
          </p>
        </div>

        <!-- Column 2: Quick Links -->
        <div>
          <h4 class="text-xs font-bold uppercase tracking-widest text-gray-300 mb-4">Shop</h4>
          <ul class="space-y-2 text-xs text-gray-400 font-medium">
            <li><router-link :to="{ name: 'shop' }" class="hover:text-white transition">Topwear</router-link></li>
            <li><router-link :to="{ name: 'shop' }" class="hover:text-white transition">T-Shirt</router-link></li>
            <li><router-link :to="{ name: 'shop' }" class="hover:text-white transition">Pants</router-link></li>
            <li><router-link :to="{ name: 'shop' }" class="hover:text-white transition">Sneakers</router-link></li>
          </ul>
        </div>

        <!-- Column 3: Customer Care & Socials -->
        <div>
          <h4 class="text-xs font-bold uppercase tracking-widest text-gray-300 mb-4">Follow Us</h4>
          <p class="text-xs text-gray-400 mb-4 leading-relaxed">
            Stay up to date with our latest drops by following our social channels.
          </p>
          <div class="flex items-center space-x-4 text-gray-400">
            <a href="#" aria-label="Facebook" class="hover:text-white transition"><Icon icon="simple-icons:facebook" class="w-4 h-4" /></a>
            <a href="#" aria-label="Instagram" class="hover:text-white transition"><Icon icon="simple-icons:instagram" class="w-4 h-4" /></a>
            <a href="#" aria-label="X" class="hover:text-white transition"><Icon icon="simple-icons:x" class="w-4 h-4" /></a>
            <a href="#" aria-label="YouTube" class="hover:text-white transition"><Icon icon="simple-icons:youtube" class="w-4 h-4" /></a>
          </div>
        </div>

        <!-- Column 4: Feedback & Review Input -->
        <div>
          <h4 class="text-xs font-bold uppercase tracking-widest text-gray-300 mb-4">Feedback</h4>
          <p class="text-xs text-gray-400 mb-3">Leave your thoughts to help us improve your shopping experience.</p>
          <form @submit.prevent="submitReview" class="relative">
            <input
              v-model="reviewText"
              type="text"
              placeholder="Write your review..."
              class="w-full py-2.5 px-3 bg-gray-800 border border-gray-700 rounded-lg text-xs text-white placeholder-gray-500 focus:outline-none focus:border-white transition"
            />
            <button
              type="submit"
              class="absolute right-1.5 top-1.5 bg-white text-gray-900 px-3 py-1 rounded text-[11px] font-bold hover:bg-gray-200 transition"
            >
              Send
            </button>
          </form>
          <p v-if="reviewSubmitted" class="text-[11px] text-emerald-400 mt-2">Thank you for your feedback!</p>
        </div>
      </div>

      <!-- Bottom Bar -->
      <div class="mt-12 pt-6 border-t border-gray-800 flex flex-col sm:flex-row items-center justify-between text-[11px] text-gray-500">
        <p>© 2026 JAS Store. All rights reserved.</p>
        <div class="flex space-x-4 mt-2 sm:mt-0">
          <a href="#" class="hover:text-gray-400">Privacy Policy</a>
          <a href="#" class="hover:text-gray-400">Terms of Service</a>
          <a href="#" class="hover:text-gray-400">Shipping Policy</a>
        </div>
      </div>
    </div>
  </footer>
</template>

<script setup>
import { ref } from "vue";
import { Icon } from "@iconify/vue";

const reviewText = ref("");
const reviewSubmitted = ref(false);

const submitReview = () => {
  if (reviewText.value.trim()) {
    reviewSubmitted.value = true;
    reviewText.value = "";
    setTimeout(() => {
      reviewSubmitted.value = false;
    }, 4000);
  }
};
</script>

<style scoped></style>