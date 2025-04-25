import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useAppStore = defineStore('app', () => {
  // 侧边栏折叠状态
  const sidebarCollapsed = ref(false)

  // 切换侧边栏状态
  function toggleSidebar() {
    sidebarCollapsed.value = !sidebarCollapsed.value
  }

  // 设置侧边栏状态
  function setSidebarCollapsed(collapsed: boolean) {
    sidebarCollapsed.value = collapsed
  }

  return {
    sidebarCollapsed,
    toggleSidebar,
    setSidebarCollapsed
  }
}) 