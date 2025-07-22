import { createRouter, createWebHistory } from 'vue-router'
import ImageListView from '../components/ImageList.vue'
import MainView from '../components/Main.vue'

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
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router