<template>
    <div class="fixed w-full h-full top-0 left-0 flex items-center justify-center z-10" v-if="open">
      <div class="absolute w-full h-full bg-gray-900 opacity-50" @click="close"></div>
  
      <div class="absolute max-h-full w-[35rem]">
        <div class="container bg-white overflow-hidden rounded-lg w-full">
            <slot></slot>
        </div>
      </div>
    </div>
  </template>
  
  <script>
    export default {
      data() {
        return {
          open: true,
        };
      },
      props: {
        
      },
      methods: {
        close() {
          this.open = false;
          this.$emit("close");
        },
      },
      mounted() {

      },
    };
  </script>
  