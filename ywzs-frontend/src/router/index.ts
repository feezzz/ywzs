import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router'

const routes: Array<RouteRecordRaw> = [
  {
    path: '/login',
    name: 'LoginPage',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/',
    component: () => import('../views/Layout.vue'),
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: () => import('../views/Dashboard.vue')
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        children: [
          {
            path: 'user',
            name: 'UserManagement',
            component: () => import('../views/system/UserManagement.vue')
          },
          {
            path: 'role',
            name: 'RoleManagement',
            component: () => import('../views/system/RoleManagement.vue')
          },
          {
            path: 'org',
            name: 'OrgManagement',
            component: () => import('../views/system/OrgManagement.vue')
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

export default router 