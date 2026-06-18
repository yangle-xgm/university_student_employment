import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/dashboard',
    name: 'Dashboard',
    component: () => import('../views/Dashboard.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: () => import('../views/Profile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/assessments',
    name: 'Assessments',
    component: () => import('../views/Assessments.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/assessment/:id',
    name: 'AssessmentDetail',
    component: () => import('../views/AssessmentDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/career',
    name: 'Career',
    component: () => import('../views/Career.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/interviews',
    name: 'Interviews',
    component: () => import('../views/Interviews.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/learning',
    name: 'Learning',
    component: () => import('../views/Learning.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/jobs',
    name: 'Jobs',
    component: () => import('../views/Jobs.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/resumes',
    name: 'Resumes',
    component: () => import('../views/Resumes.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const isAuthenticated = localStorage.getItem('token')
  if (to.meta.requiresAuth && !isAuthenticated) {
    next('/login')
  } else {
    next()
  }
})

export default router
