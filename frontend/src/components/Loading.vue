<template>
    <div class="colorful"></div>
</template>

<script>
export default {

}
</script>

<style scoped>
.colorful {
   position: absolute;
   top: 50%;
   left: 50%;
   width: 15.8px;
   height: 15.8px;
   border-radius: 50%;
   background: radial-gradient(farthest-side,#474bff 94%,#0000 ),
          radial-gradient(farthest-side,#ff6761 94%,#0000 ),
          #474bff;
   background-position: center;
   background-repeat: no-repeat;
   animation: colorful-jaigj7 1.5s infinite;
}

@keyframes colorful-jaigj7 {
   0% {
      background-size: 0    0   ,0    0;
   }

   30%, 50% {
      background-size: 0    0   ,120% 120%;
   }

   80%, 100% {
      background-size: 120% 120%,120% 120%;
   }
}
</style>