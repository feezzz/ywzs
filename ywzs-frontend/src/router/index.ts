import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'LoginPage',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    meta: { requiresAuth: true },
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue'),
        meta: { requiresAuth: true }
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        meta: { requiresAuth: true },
        children: [
          {
            path: 'user',
            name: 'UserManagement',
            component: () => import('../views/system/UserManagement.vue'),
            meta: { requiresAuth: true }
          },
          {
            path: 'role',
            name: 'RoleManagement',
            component: () => import('../views/system/RoleManagement.vue'),
            meta: { requiresAuth: true }
          },
          {
            path: 'org',
            name: 'OrgManagement',
            component: () => import('../views/system/OrgManagement.vue'),
            meta: { requiresAuth: true }
          }
        ]
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  const requiresAuth = to.matched.some(record => record.meta.requiresAuth)

  if (requiresAuth && !token) {
    // 需要认证但未登录，重定向到登录页
    next({ name: 'LoginPage', query: { redirect: to.fullPath } })
  } else if (to.path === '/login' && token) {
    // 已登录但访问登录页，重定向到首页
    next({ path: '/' })
  } else {
    // 其他情况正常放行
    next()
  }
})

export default router 