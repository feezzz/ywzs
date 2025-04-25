import { createStore } from 'vuex'
import { login, logout, getUserInfo } from '../api/auth'

export default createStore({
  state: {
    token: localStorage.getItem('token') || '',
    userInfo: JSON.parse(localStorage.getItem('userInfo')) || {},
    collapsed: false // 侧边栏是否折叠
  },
  mutations: {
    // 设置token
    SET_TOKEN(state, token) {
      state.token = token
      localStorage.setItem('token', token)
    },
    // 设置用户信息
    SET_USER_INFO(state, userInfo) {
      state.userInfo = userInfo
      localStorage.setItem('userInfo', JSON.stringify(userInfo))
    },
    // 清除用户信息
    CLEAR_USER_INFO(state) {
      state.token = ''
      state.userInfo = {}
      localStorage.removeItem('token')
      localStorage.removeItem('userInfo')
    },
    // 切换侧边栏
    TOGGLE_SIDEBAR(state) {
      state.collapsed = !state.collapsed
    }
  },
  actions: {
    // 登录
    async login({ commit }, credentials) {
      try {
        const response = await login(credentials)
        const { token, userInfo } = response.data
        commit('SET_TOKEN', token)
        commit('SET_USER_INFO', userInfo)
        return Promise.resolve()
      } catch (error) {
        return Promise.reject(error)
      }
    },
    // 退出登录
    async logout({ commit }) {
      try {
        await logout()
        commit('CLEAR_USER_INFO')
        return Promise.resolve()
      } catch (error) {
        return Promise.reject(error)
      }
    },
    // 获取用户信息
    async getUserInfo({ commit }) {
      try {
        const response = await getUserInfo()
        const { userInfo } = response.data
        commit('SET_USER_INFO', userInfo)
        return Promise.resolve(userInfo)
      } catch (error) {
        return Promise.reject(error)
      }
    }
  },
  getters: {
    token: state => state.token,
    userInfo: state => state.userInfo,
    collapsed: state => state.collapsed,
    roles: state => state.userInfo.roles || []
  }
}) 