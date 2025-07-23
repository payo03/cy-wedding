import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js'

import { setQrcodeToken } from '@/utils/axios'

const savedQrcode = localStorage.getItem('qrcode')
if (savedQrcode) {
  setQrcodeToken(savedQrcode)
}

const app = createApp(App)
app.use(router)
app.mount('#app')
