import api from './auth'

// 获取仪表盘统计数据
export const getDashboardStats = () => {
  return api.get('/duty/dashboard/stats')
}

// 获取值班日历数据
export const getDutyCalendar = (year, month) => {
  return api.get('/duty/calendar', { params: { year, month } })
}

// 获取值班通知
export const getDutyNotices = () => {
  return api.get('/duty/notices')
}

// 获取最近交接班记录
export const getRecentHandovers = () => {
  return api.get('/duty/handovers/recent')
}

// 创建交接班记录
export const createHandover = (data) => {
  return api.post('/duty/handovers', data)
}

// 获取排班表
export const getDutySchedule = (params) => {
  return api.get('/duty/schedule', { params })
}

// 更新排班
export const updateDutySchedule = (data) => {
  return api.put('/duty/schedule', data)
}

// 获取交接班记录列表
export function getHandoverList(params) {
  return api.get('/duty/handover/list', { params })
}

// 添加交接班记录
export function addHandover(data) {
  return api.post('/duty/handover/add', data)
}

// 获取交接班详情
export function getHandoverDetail(id) {
  return api.get('/duty/handover/detail/' + id)
}

// 获取排班计划列表
export function getScheduleList(params) {
  return api.get('/duty/schedule/list', { params })
}

// 添加排班计划
export function addSchedule(data) {
  return api.post('/duty/schedule/add', data)
}

// 更新排班计划
export function updateSchedule(data) {
  return api.put('/duty/schedule/update', data)
}

// 删除排班计划
export function deleteSchedule(id) {
  return api.delete('/duty/schedule/delete/' + id)
}

// 获取值班记录列表
export function getDutyList(params) {
  return api.get('/duty/list', { params })
}

// 签到
export function checkIn(data) {
  return api.post('/duty/checkin', data)
}

// 签退
export function checkOut(data) {
  return api.post('/duty/checkout', data)
} 