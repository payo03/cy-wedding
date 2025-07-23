import { createRouter, createWebHistory } from 'vue-router'
import ImageListView from '../components/ImageList.vue'
import MainView from '../components/Main.vue'

import { setQrcodeToken } from '@/utils/axios'

const routes = [
  {
    path: '/',
    name: 'MainView',
    component: MainView
  },
  {
    path: '/image/list',
    name: 'ImageList',
    component: ImageListView
  },
  {
    path: '/qr/:qrcode',
    name: 'qr',
    beforeEnter: (to, from, next) => {
      // 1. qrcode 값을 추출
      const qrcode = to.params.qrcode;

      // 2. localStorage에 저장
      if (qrcode) {
        setQrcodeToken(qrcode)
      }

      // 3. 메인 페이지로 리다이렉트
      next('/');
    },
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router