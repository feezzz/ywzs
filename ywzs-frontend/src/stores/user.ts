import { defineStore } from 'pinia'
import { ref } from 'vue'
import request from '@/utils/request'

interface LoginForm {
  username: string
  password: string
}

interface UserInfo {
  id?: number
  username?: string
  fullName?: string
  email?: string
  phone?: string
}

export const useUserStore = defineStore('user', () => {
  const token = ref('')
  const userInfo = ref<UserInfo>({})

  async function login(loginForm: LoginForm) {
    try {
      const response = await request.post('/auth/login', loginForm)
      const { token: newToken, userInfo: user } = response.data.data
      token.value = newToken
      userInfo.value = user
    } catch (error) {
      clearUser()
      throw error
    }
  }

  function setToken(value: string) {
    token.value = value
  }

  function setUserInfo(info: UserInfo) {
    userInfo.value = info
  }

  function clearUser() {
    token.value = ''
    userInfo.value = {}
  }

  function logout() {
    clearUser()
  }

  return {
    token,
    userInfo,
    login,
    setToken,
    setUserInfo,
    clearUser,
    logout
  }
}) 