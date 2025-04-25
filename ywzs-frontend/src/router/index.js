import { createRouter, createWebHistory } from 'vue-router'

// 路由懒加载
const Login = () => import('../views/Login.vue')
const MainLayout = () => import('../views/Layout.vue')
const Dashboard = () => import('../views/Dashboard.vue')
const UserManagement = () => import('../views/system/UserManagement.vue')
const RoleManagement = () => import('../views/system/RoleManagement.vue')
const OrgManagement = () => import('../views/system/OrgManagement.vue')
const ShiftHandover = () => import('../views/duty/ShiftHandover.vue')
const ScheduleManagement = () => import('../views/duty/ScheduleManagement.vue')
const DutyManagement = () => import('../views/duty/DutyManagement.vue')
const ReportManagement = () => import('../views/report/ReportManagement.vue')

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: Login,
    meta: { title: '登录', requireAuth: false }
  },
  {
    path: '/',
    component: MainLayout,
    redirect: '/dashboard',
    children: [
      {
        path: 'dashboard',
        name: 'Dashboard',
        component: Dashboard,
        meta: { title: '首页', icon: 'el-icon-s-home', requireAuth: true }
      },
      {
        path: 'system',
        name: 'System',
        redirect: '/system/user',
        meta: { title: '系统管理', icon: 'el-icon-setting', requireAuth: true },
        children: [
          {
            path: 'user',
            name: 'UserManagement',
            component: UserManagement,
            meta: { title: '人员管理', requireAuth: true }
          },
          {
            path: 'role',
            name: 'RoleManagement',
            component: RoleManagement,
            meta: { title: '角色管理', requireAuth: true }
          },
          {
            path: 'org',
            name: 'OrgManagement',
            component: OrgManagement,
            meta: { title: '组织管理', requireAuth: true }
          }
        ]
      },
      {
        path: 'duty',
        name: 'Duty',
        redirect: '/duty/handover',
        meta: { title: '值班管理', icon: 'el-icon-alarm-clock', requireAuth: true },
        children: [
          {
            path: 'handover',
            name: 'ShiftHandover',
            component: ShiftHandover,
            meta: { title: '交接班管理', requireAuth: true }
          },
          {
            path: 'schedule',
            name: 'ScheduleManagement',
            component: ScheduleManagement,
            meta: { title: '排班管理', requireAuth: true }
          },
          {
            path: 'dutylist',
            name: 'DutyManagement',
            component: DutyManagement,
            meta: { title: '值班管理', requireAuth: true }
          }
        ]
      },
      {
        path: 'report',
        name: 'Report',
        redirect: '/report/list',
        meta: { title: '报表管理', icon: 'el-icon-document', requireAuth: true },
        children: [
          {
            path: 'list',
            name: 'ReportManagement',
            component: ReportManagement,
            meta: { title: '报表管理', requireAuth: true }
          }
        ]
      }
    ]
  },
  // 404页面
  {
    path: '/:pathMatch(.*)*',
    redirect: '/dashboard'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 导航守卫
router.beforeEach((to, from, next) => {
  // 设置页面标题
  document.title = to.meta.title ? `${to.meta.title} - 运维值守系统` : '运维值守系统'
  
  // 判断该路由是否需要登录权限
  if (to.meta.requireAuth) {
    // 判断是否已登录
    const token = localStorage.getItem('token')
    if (token) {
      next()
    } else {
      next({
        path: '/login',
        query: { redirect: to.fullPath } // 将跳转的路由path作为参数，登录成功后跳转到该路由
      })
    }
  } else {
    next()
  }
})

export default router 