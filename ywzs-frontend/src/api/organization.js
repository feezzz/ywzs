import axios from 'axios'

// 获取组织树结构
export function getOrgTree() {
  return axios({
    url: '/org/tree',
    method: 'get'
  })
}

// 获取组织详情
export function getOrgDetail(id) {
  return axios({
    url: '/org/detail/' + id,
    method: 'get'
  })
}

// 添加组织
export function addOrg(data) {
  return axios({
    url: '/org/add',
    method: 'post',
    data
  })
}

// 更新组织
export function updateOrg(data) {
  return axios({
    url: '/org/update',
    method: 'put',
    data
  })
}

// 删除组织
export function deleteOrg(id) {
  return axios({
    url: '/org/delete/' + id,
    method: 'delete'
  })
} 