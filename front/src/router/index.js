import { createRouter, createWebHistory } from 'vue-router'
import ImageListView from '../components/ImageList.vue'
import MainActions from '../components/MainActions.vue'

const routes = [
  {
    path: '/',
    name: 'HomeView',
    component: MainActions
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