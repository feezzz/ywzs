import axios from 'axios'

// 登录
export function login(data) {
  return axios({
    url: '/user/login',
    method: 'post',
    data
  })
}

// 获取用户信息
export function getUserInfo() {
  return axios({
    url: '/user/info',
    method: 'get'
  })
}

// 退出登录
export function logout() {
  return axios({
    url: '/user/logout',
    method: 'post'
  })
}

// 获取用户列表
export function getUserList(params) {
  return axios({
    url: '/user/list',
    method: 'get',
    params
  })
}

// 添加用户
export function addUser(data) {
  return axios({
    url: '/user/add',
    method: 'post',
    data
  })
}

// 更新用户
export function updateUser(data) {
  return axios({
    url: '/user/update',
    method: 'put',
    data
  })
}

// 删除用户
export function deleteUser(id) {
  return axios({
    url: '/user/delete/' + id,
    method: 'delete'
  })
}

// 重置密码
export function resetPassword(id) {
  return axios({
    url: '/user/reset-password/' + id,
    method: 'post'
  })
} 