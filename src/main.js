import { createApp } from 'vue'
import App from './App.vue'
import router from './router'
import './assets/global.css'
import {createPinia} from 'pinia'
import { createPersistedState } from 'pinia-persistedstate-plugin'
const pinia = createPinia()
pinia.use(createPersistedState())

const app = createApp(App)
app.use(router)
app.use(pinia)
app.mount('#app')
