<template>
  <div class="app-wrapper">
    <!-- 侧边栏 -->
    <div class="sidebar-container" :class="{ 'is-collapsed': isCollapsed }">
      <div class="logo">
        <span>运维值守系统</span>
      </div>
      <el-menu
        :default-active="activeMenu"
        class="sidebar-menu"
        :collapse="isCollapsed"
        background-color="#304156"
        text-color="#bfcbd9"
        active-text-color="#409EFF"
        router
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <span>首页</span>
        </el-menu-item>

        <!-- 系统管理 -->
        <el-sub-menu index="/system">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/system/user">人员管理</el-menu-item>
          <el-menu-item index="/system/role">角色管理</el-menu-item>
          <el-menu-item index="/system/org">组织管理</el-menu-item>
        </el-sub-menu>

        <!-- 值班管理 -->
        <el-sub-menu index="/duty">
          <template #title>
            <el-icon><Calendar /></el-icon>
            <span>值班管理</span>
          </template>
          <el-menu-item index="/duty/handover">交接班管理</el-menu-item>
          <el-menu-item index="/duty/schedule">排班管理</el-menu-item>
          <el-menu-item index="/duty/dutylist">值班管理</el-menu-item>
        </el-sub-menu>

        <!-- 报表管理 -->
        <el-sub-menu index="/report">
          <template #title>
            <el-icon><Document /></el-icon>
            <span>报表管理</span>
          </template>
          <el-menu-item index="/report/list">报表管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </div>

    <!-- 主要内容区域 -->
    <div class="main-container">
      <!-- 顶部导航栏 -->
      <div class="navbar">
        <div class="hamburger" @click="toggleSidebar">
          <el-icon v-if="isCollapsed"><Expand /></el-icon>
          <el-icon v-else><Fold /></el-icon>
        </div>
        <div class="breadcrumb">
          <!-- 面包屑导航 -->
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="currentRoute.meta && currentRoute.meta.title">{{ currentRoute.meta.title }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="right-menu">
          <el-dropdown trigger="click">
            <div class="avatar-wrapper">
              <span>{{ userInfo.fullName || '管理员' }}</span>
              <el-icon class="el-icon--right"><arrow-down /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>

      <!-- 内容 -->
      <div class="app-main">
        <router-view />
      </div>
    </div>
  </div>
</template>

<script lang="ts">
import { computed, defineComponent } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { useAppStore } from '@/stores/app'
import { HomeFilled, Setting, Calendar, Document, ArrowDown, Expand, Fold } from '@element-plus/icons-vue'
import { ElMessageBox } from 'element-plus'

export default defineComponent({
  name: 'MainLayout',
  components: {
    HomeFilled,
    Setting,
    Calendar,
    Document,
    ArrowDown,
    Expand,
    Fold
  },
  setup() {
    const userStore = useUserStore()
    const appStore = useAppStore()
    const route = useRoute()
    const router = useRouter()

    // 当前路由
    const currentRoute = computed(() => route)

    // 活跃菜单
    const activeMenu = computed(() => {
      const { meta, path } = route
      if (meta.activeMenu) {
        return meta.activeMenu as string
      }
      return path
    })

    // 用户信息
    const userInfo = computed(() => userStore.userInfo)

    // 侧边栏折叠状态
    const isCollapsed = computed(() => appStore.sidebarCollapsed)

    // 切换侧边栏
    const toggleSidebar = () => {
      appStore.toggleSidebar()
    }

    // 退出登录
    const handleLogout = () => {
      ElMessageBox.confirm('确定要退出登录吗?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        userStore.logout()
        router.push('/login')
      }).catch(() => {
        // 用户取消退出操作，不做任何处理
        return
      })
    }

    return {
      currentRoute,
      activeMenu,
      userInfo,
      isCollapsed,
      toggleSidebar,
      handleLogout
    }
  }
})
</script>

<style scoped>
.app-wrapper {
  position: relative;
  height: 100%;
  width: 100%;
}

.sidebar-container {
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  width: 210px;
  background-color: #304156;
  transition: width 0.3s;
  z-index: 1001;
  overflow: hidden;
}

.sidebar-container.is-collapsed {
  width: 64px;
}

.logo {
  height: 50px;
  line-height: 50px;
  text-align: center;
  font-size: 20px;
  font-weight: bold;
  color: #fff;
  background-color: #2b2f3a;
  overflow: hidden;
}

.sidebar-menu {
  border-right: none;
}

.main-container {
  margin-left: 210px;
  min-height: 100%;
  position: relative;
  transition: margin-left 0.3s;
}

.sidebar-container.is-collapsed + .main-container {
  margin-left: 64px;
}

.navbar {
  height: 50px;
  overflow: hidden;
  position: relative;
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,.08);
  display: flex;
  align-items: center;
  padding: 0 15px;
}

.hamburger {
  cursor: pointer;
  margin-right: 15px;
}

.breadcrumb {
  flex: 1;
}

.right-menu {
  margin-left: auto;
}

.avatar-wrapper {
  cursor: pointer;
  display: flex;
  align-items: center;
}

.app-main {
  padding: 20px;
  min-height: calc(100vh - 50px);
  position: relative;
  overflow: hidden;
  background-color: #f0f2f5;
}
</style> 