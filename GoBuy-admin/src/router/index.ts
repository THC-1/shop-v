import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'
import MainLayout from '@/layouts/MainLayout.vue'

const routes: RouteRecordRaw[] = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/LoginView.vue'),
    meta: { title: '管理员登录' }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { title: '首页看板' }
      },
      {
        path: '/products',
        name: 'Products',
        component: () => import('@/views/ProductListView.vue'),
        meta: { title: '商品管理' }
      },
      {
        path: '/categories',
        name: 'Categories',
        component: () => import('@/views/CategoryView.vue'),
        meta: { title: '分类管理' }
      },
      {
        path: '/brands',
        name: 'Brands',
        component: () => import('@/views/BrandView.vue'),
        meta: { title: '品牌管理' }
      },
      {
        path: '/orders',
        name: 'Orders',
        component: () => import('@/views/OrderView.vue'),
        meta: { title: '订单管理' }
      },
      {
        path: '/users',
        name: 'Users',
        component: () => import('@/views/UserView.vue'),
        meta: { title: '用户管理' }
      },
      {
        path: '/roles',
        name: 'Roles',
        component: () => import('@/views/RoleView.vue'),
        meta: { title: '角色管理' }
      },
      {
        path: '/admins',
        name: 'Admins',
        component: () => import('@/views/AdminView.vue'),
        meta: { title: '管理员' }
      },
      {
        path: '/logs',
        name: 'Logs',
        component: () => import('@/views/LogView.vue'),
        meta: { title: '操作日志' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  if (to.meta.title) {
    document.title = `${to.meta.title} - GoBuy运营后台`
  }
  const adminToken = localStorage.getItem('admin-token')
  if (!adminToken && to.path !== '/login') {
    next('/login')
  } else {
    next()
  }
})

export default router