import axios from 'axios'
import { ElMessage } from 'element-plus'
import { useUserStore } from '@/stores/user'

// 创建axios实例
const request = axios.create({
  baseURL: '/api', // API 基础URL
  timeout: 5000 // 请求超时时间
})

// 请求拦截器
request.interceptors.request.use(
  config => {
    const userStore = useUserStore()
    if (userStore.token) {
      config.headers['Authorization'] = `Bearer ${userStore.token}`
    }
    console.log('Request:', {
      url: config.url,
      method: config.method,
      headers: config.headers,
      data: config.data
    })
    return config
  },
  error => {
    console.error('Request Error:', error)
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  response => {
    console.log('Response:', {
      status: response.status,
      data: response.data,
      headers: response.headers
    })
    const res = response.data
    // 如果是文件流，直接返回
    if (response.config.responseType === 'blob') {
      return res
    }
    return res
  },
  error => {
    console.error('Response Error:', {
      status: error.response?.status,
      data: error.response?.data,
      message: error.message,
      config: {
        url: error.config?.url,
        method: error.config?.method,
        data: error.config?.data
      }
    })
    const message = error.response?.data?.message || error.message || '请求失败'
    ElMessage.error(message)
    
    // 如果是401，说明token过期或无效，需要重新登录
    if (error.response?.status === 401) {
      const userStore = useUserStore()
      userStore.logout()
    }
    
    return Promise.reject(error)
  }
)

export default request 