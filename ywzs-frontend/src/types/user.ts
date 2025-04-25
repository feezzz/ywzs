// 用户信息接口
export interface UserInfo {
  id: number
  username: string
  fullName: string
  email?: string
  phone?: string
  status: number
  remark?: string
  createdAt?: string
  lastLoginTime?: string
}

// 用户查询参数接口
export interface UserQuery {
  page: number
  size: number
  username?: string
  fullName?: string
  status?: number
}

// 用户表单接口
export interface UserForm {
  id?: number
  username: string
  password?: string
  fullName: string
  email?: string
  phone?: string
  status: number
  remark?: string
}

// 用户列表响应接口
export interface UserListResponse {
  users: UserInfo[]
  totalItems: number
} 