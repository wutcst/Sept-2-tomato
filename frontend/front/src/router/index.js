import Vue from 'vue'
import VueRouter from 'vue-router'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    redirect: '/login-main'
  },
  {
    path: '/login-main',
    name: 'login-main',
    component: () => import('../views/login/loginMain.vue')
  },
  {
    path: '/forget-password',
    name: 'forgetPassword',
    component: () => import('../views/login/forgetPassword.vue')
  },
  {
    path: '/regist-main',
    name: 'regist-main',
    component: () => import('../views/login/registerMain.vue')
  },
  {
    path: '/home-main',
    name: 'home-main',
    component: () => import('../views/home/homeMain.vue'),
    children: [
      {
        path: '/',
        name: 'homeUse',
        component: () => import('../views/home/index/homeUse.vue')
      },

    ]
  },
]

const router = new VueRouter({
  routes
})



export default router
