import request from '@/utils/request'
import type { UserQuery, UserForm, UserListResponse } from '@/types/user'
import type { AxiosResponse } from 'axios'

// 获取用户列表
export function getUsers(params: UserQuery): Promise<UserListResponse> {
  return request.get<UserListResponse, AxiosResponse<UserListResponse>>('/api/users', { params })
    .then(response => response.data)
}

// 创建用户
export function createUser(data: UserForm) {
  return request.post('/api/users', data)
}

// 更新用户
export function updateUser(id: number, data: UserForm) {
  return request.put(`/api/users/${id}`, data)
}

// 删除用户
export function deleteUser(id: number) {
  return request.delete(`/api/users/${id}`)
}

// 更新用户状态
export function updateUserStatus(id: number, status: number) {
  return request.patch(`/api/users/${id}/status`, { status })
}

// 重置用户密码
export function resetUserPassword(id: number, password: string) {
  return request.patch(`/api/users/${id}/password`, { password })
} 