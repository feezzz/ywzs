import axios from 'axios'

// 获取角色列表
export function getRoleList(params) {
  return axios({
    url: '/role/list',
    method: 'get',
    params
  })
}

// 获取所有角色（用于下拉选择）
export function getAllRoles() {
  return axios({
    url: '/role/all',
    method: 'get'
  })
}

// 添加角色
export function addRole(data) {
  return axios({
    url: '/role/add',
    method: 'post',
    data
  })
}

// 更新角色
export function updateRole(data) {
  return axios({
    url: '/role/update',
    method: 'put',
    data
  })
}

// 删除角色
export function deleteRole(id) {
  return axios({
    url: '/role/delete/' + id,
    method: 'delete'
  })
}

// 获取角色权限
export function getRolePermissions(roleId) {
  return axios({
    url: '/role/permissions/' + roleId,
    method: 'get'
  })
}

// 更新角色权限
export function updateRolePermissions(data) {
  return axios({
    url: '/role/permissions/update',
    method: 'post',
    data
  })
} 