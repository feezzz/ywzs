import { defineStore } from 'pinia'

interface UserState {
  user: any | null
  isAuthenticated: boolean
}

export const useUserStore = defineStore('user', {
  state: (): UserState => ({
    user: null,
    isAuthenticated: false
  }),

  actions: {
    setUser(user: any) {
      this.user = user
      this.isAuthenticated = true
    },

    clearUser() {
      this.user = null
      this.isAuthenticated = false
    },

    async checkAuth() {
      try {
        const response = await fetch('/api/auth/current-user', {
          credentials: 'include'
        })
        if (response.ok) {
          const user = await response.json()
          this.setUser(user)
          return true
        } else {
          this.clearUser()
          return false
        }
      } catch (error) {
        console.error('检查认证状态失败：', error)
        this.clearUser()
        return false
      }
    },

    async logout() {
      try {
        await fetch('/api/auth/logout', {
          method: 'POST',
          credentials: 'include'
        })
        this.clearUser()
      } catch (error) {
        console.error('退出登录失败：', error)
      }
    }
  }
}) 