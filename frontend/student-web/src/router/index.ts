import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(),
  routes: [
    { path: '/', name: 'home', component: HomeView, meta: { requiresAuth: true } },
    { path: '/login', name: 'login', component: () => import('../views/LoginView.vue') },
    { path: '/plan', name: 'plan', component: () => import('../views/PlanView.vue'), meta: { requiresAuth: true } },
    { path: '/checkin', name: 'checkin', component: () => import('../views/CheckinView.vue'), meta: { requiresAuth: true } },
    { path: '/stats', name: 'stats', component: () => import('../views/StatsView.vue'), meta: { requiresAuth: true } }
  ]
})

router.beforeEach((to, _from, next) => {
  const token = localStorage.getItem('token')
  if (to.meta.requiresAuth && !token) {
    next('/login')
  } else if (to.path === '/login' && token) {
    next('/')
  } else {
    next()
  }
})

export default router
