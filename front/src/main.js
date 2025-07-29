import './assets/main.css'

import { createApp } from 'vue'
import App from './App.vue'
import router from './router/index.js'

import { setHeader } from '@/utils/axios'

const domain = localStorage.getItem('groupName')
const code = localStorage.getItem('qrCode')
if (domain && code) {
  setHeader(domain, code)
}

const app = createApp(App)
app.use(router)
app.mount('#app')
