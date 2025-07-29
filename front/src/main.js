import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js'

import { setHeader } from '@/utils/axios'

const savedDomain = localStorage.getItem('domain')
const savedQrcode = localStorage.getItem('qrcode')
if (savedDomain && savedQrcode) {
  setHeader(savedDomain, savedQrcode)
}

const app = createApp(App)
app.use(router)
app.mount('#app')
