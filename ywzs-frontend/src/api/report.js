import axios from 'axios'

// 获取报表列表
export function getReportList(params) {
  return axios({
    url: '/report/list',
    method: 'get',
    params
  })
}

// 生成月度值班报表
export function generateMonthlyReport(params) {
  return axios({
    url: '/report/monthly',
    method: 'get',
    params
  })
}

// 生成季度值班报表
export function generateQuarterlyReport(params) {
  return axios({
    url: '/report/quarterly',
    method: 'get',
    params
  })
}

// 生成年度值班报表
export function generateAnnualReport(params) {
  return axios({
    url: '/report/annual',
    method: 'get',
    params
  })
}

// 导出报表
export function exportReport(params) {
  return axios({
    url: '/report/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
} 