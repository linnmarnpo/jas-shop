import { createApp, markRaw } from 'vue'
import './style.css'
import App from './App.vue'
import { createPinia } from 'pinia'

import router from './router'
import { setupAuthGuard } from './router/guard'
import api from './service/axios'

const pinia = createPinia()
pinia.use(({ store }) => {
    store.router = markRaw(router)
})

const app = createApp(App)

app.config.globalProperties.$axios = api

app.use(pinia)
app.use(router)

setupAuthGuard(router)
app.mount('#app')
