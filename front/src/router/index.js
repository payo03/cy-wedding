import { createRouter, createWebHistory } from 'vue-router'
import ImageListView from '../components/ImageList.vue'
import MainView from '../components/MainView.vue'

import { setHeader } from '@/utils/axios'

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
    path: '/qr/:domain/:qrcode',
    name: 'qr',
    beforeEnter: (to, from, next) => {
      // 1. domain, qrcode 값을 추출
      const domain = to.params.domain;
      const qrcode = to.params.qrcode;

      // 2. localStorage에 저장
      setHeader(domain, qrcode);

      // 3. 메인 페이지로 리다이렉트
      next({ name: 'MainView' });
    },
  },
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router