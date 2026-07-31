<template>
    <div>
        <Carousel></Carousel>
        <CatgoriesBox></CatgoriesBox>
        <TrendItem></TrendItem>
    </div>
</template>

<script>
import Footer from '../components/Footer.vue'
import TrendItem from '../components/TrendItem.vue'
import CatgoriesBox from '../components/CatgoriesBox.vue'
import Carousel from '../components/Carousel.vue'
import Navbar from '../components/Navbar.vue'

//auth
export default {
  components: {
    Footer,
    TrendItem,
    CatgoriesBox,
    Carousel, Navbar },

    data(){
        return{
            
        }
    },
    methods: {
        
    },
    async mounted(){
        
    }

}
</script>

<style>

</style>