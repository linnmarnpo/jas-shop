<template>
    <span class="close_btn" style="top: 0.5cm; right: 0.5cm" title="close"></span>
</template>

<script>
export default {

}
</script>

<style scoped>
.close_btn
{
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    position: absolute;
    width: 100%;
    height: 100%;
    border-radius: 50%;
    transition: 0.5s ease-in-out;
}
.close_btn:hover{
    background-color: #D5D8DC ;
}
.close_btn::before,
.close_btn::after
{
    background-color: black;
    content: '';
    display: flex;
    height: 2px;
    transform-origin: center ;
    transition: all 0.2s ease-in;
    width: 50%;
    z-index: 0;
}
.close_btn::before
{
    transform: rotate(45deg);
}
.close_btn::after
{
    position: absolute;
    transform: rotate(-45deg);
}
.close_btn:hover::before
{
    transform: rotate(0deg);
}
.close_btn:hover::after
{
    transform: rotate(0deg);
}
</style>