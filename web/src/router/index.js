import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const routes = [
  {
    path: '/',
    redirect: '/login',
    component: HomeView
  },
  {
    path: '/login',
    name: 'login-view',
    component: () => import('../views/login.vue')
  },
  {
    path: '/main',
    name: 'main-view',
    component: () => import('../views/mainView.vue')
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
