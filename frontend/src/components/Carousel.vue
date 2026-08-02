<template>
  <div class="relative w-full overflow-hidden bg-black group">
    <!-- Carousel Images Slider -->
    <div class="relative w-full h-[45vh] sm:h-[60vh] md:h-[70vh] lg:h-[85vh] min-h-[250px] max-h-[900px]">
      <transition-group name="fade" tag="div" class="w-full h-full">
        <div
          v-for="(slide, index) in slides"
          :key="slide.id"
          v-show="currentIndex === index"
          class="absolute inset-0 w-full h-full"
        >
          <img
            :src="slide.image"
            :alt="slide.title"
            class="w-full h-full object-cover object-center"
          />
          <!-- Optional Dark Overlay for contrast -->
          <div class="absolute inset-0 bg-black/20"></div>
        </div>
      </transition-group>
    </div>

    <!-- Indicator Dots (Bottom Center) -->
    <div class="absolute bottom-3 sm:bottom-5 left-1/2 -translate-x-1/2 z-20 flex space-x-1.5 sm:space-x-2.5">
      <button
        v-for="(slide, index) in slides"
        :key="slide.id"
        @click="goToSlide(index)"
        type="button"
        :class="[
          'w-2.5 h-2.5 sm:w-3.5 sm:h-3.5 rounded-full transition-all duration-300 focus:outline-none',
          currentIndex === index ? 'bg-white scale-125 shadow-md' : 'bg-white/50 hover:bg-white/80'
        ]"
        :aria-label="'Go to slide ' + (index + 1)"
      ></button>
    </div>

    <!-- Previous Arrow Button -->
    <button
      @click="prevSlide"
      type="button"
      class="absolute left-2 sm:left-4 top-1/2 -translate-y-1/2 z-20 p-1.5 sm:p-2.5 text-white bg-black/40 hover:bg-black/80 rounded-full transition-all duration-200 focus:outline-none backdrop-blur-xs"
      aria-label="Previous slide"
    >
      <Icon icon="uil:angle-left" class="w-5 h-5 sm:w-8 sm:h-8" />
    </button>

    <!-- Next Arrow Button -->
    <button
      @click="nextSlide"
      type="button"
      class="absolute right-2 sm:right-4 top-1/2 -translate-y-1/2 z-20 p-1.5 sm:p-2.5 text-white bg-black/40 hover:bg-black/80 rounded-full transition-all duration-200 focus:outline-none backdrop-blur-xs"
      aria-label="Next slide"
    >
      <Icon icon="uil:angle-right" class="w-5 h-5 sm:w-8 sm:h-8" />
    </button>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from "vue";
import { Icon } from "@iconify/vue";

const slides = ref([
  {
    id: 1,
    image: "https://int.bape.com/cdn/shop/files/MBA_26_Lookbook_BANNER_Bape_online_2160x1080_2bf621e1-aa6e-4a1e-83d4-2e821f4527cb.jpg?v=1772693743&width=1600",
    title: "New Lookbook Collection"
  },
  {
    id: 2,
    image: "https://res.cloudinary.com/dz993lw3o/image/upload/v1714644494/bape_men_fllx6a.jpg",
    title: "Men's Streetwear"
  },
  {
    id: 3,
    image: "https://us.bape.com/cdn/shop/files/FLIP_STA_FEB25_LOOKBOOK_BANNER_BAPE_ONLINE_HP_DESKTOP_2160x1080_554f86a9-cee8-4f22-9ce2-57acac108340.jpg?v=1739559468&width=1600",
    title: "Flip STA Footwear"
  }
]);

const currentIndex = ref(0);
let timer = null;

const nextSlide = () => {
  currentIndex.value = (currentIndex.value + 1) % slides.value.length;
};

const prevSlide = () => {
  currentIndex.value = (currentIndex.value - 1 + slides.value.length) % slides.value.length;
};

const goToSlide = (index) => {
  currentIndex.value = index;
};

const startAutoSlide = () => {
  stopAutoSlide();
  timer = setInterval(nextSlide, 4500);
};

const stopAutoSlide = () => {
  if (timer) clearInterval(timer);
};

onMounted(() => {
  startAutoSlide();
});

onUnmounted(() => {
  stopAutoSlide();
});
</script>

<style scoped>
.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.8s ease-in-out;
}

.fade-enter-from,
.fade-leave-to {
  opacity: 0;
}
</style>
