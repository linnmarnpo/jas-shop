<template>
    <div class="tt-wrapper">
      <div class="tt">
        <slot></slot>
      </div>
    </div>
</template>
  
<style scoped>
.tt-wrapper {
    position: fixed;
    width: 100%;
    top: 100px;
}
.tt {
    padding: 20px;
    text-align: center;
    color: white;
    background: #ff0062;
    border-radius: 10px;
    box-shadow: 1px 3px 5px rgba(0,0,0,0.2);
    max-width: 400px;
    margin: 0 auto;
}
</style>