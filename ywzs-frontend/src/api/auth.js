import axios from 'axios'
import { ElMessage } from 'element-plus'

const api = axios.create({
  // Vue CLI 环境变量通过 process.env 访问，且变量需以 VUE_APP_ 开头
  baseURL: process.env.VUE_APP_API_URL || 'http://localhost:8080/api', 
  timeout: 5000
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    const token = localStorage.getItem('token')
    if (token) {
      config.headers['Authorization'] = `Bearer ${token}`
    }
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => response.data,
  error => {
    if (error.response) {
      switch (error.response.status) {
        case 401:
          // token过期或无效，清除用户信息并跳转到登录页
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          window.location.href = '/login'
          break
        case 403:
          ElMessage.error('没有权限访问该资源')
          break
        default:
          ElMessage.error(error.response.data.message || '服务器错误')
      }
    }
    return Promise.reject(error)
  }
)

export const login = (credentials) => {
  return api.post('/auth/login', credentials)
}

export const logout = () => {
  return api.post('/auth/logout')
}

export const getUserInfo = () => {
  return api.get('/auth/user-info')
}

export default api 