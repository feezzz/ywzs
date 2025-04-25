<template>
  <div class="dashboard-container">
    <el-row :gutter="20">
      <!-- 统计卡片 -->
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">今日值班人数</div>
          <div class="stat-value">{{ stats.dutyToday }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">本周值班人次</div>
          <div class="stat-value">{{ stats.dutyWeek }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">本月交接班次数</div>
          <div class="stat-value">{{ stats.handoverMonth }}</div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-title">系统用户数</div>
          <div class="stat-value">{{ stats.userCount }}</div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <!-- 值班日历 -->
      <el-col :span="16">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>值班日历</span>
            </div>
          </template>
          <div class="calendar-container">
            <el-calendar v-model="currentDate">
              <template #dateCell="{ data }">
                <div class="calendar-day">
                  <div>{{ data.day.split('-').slice(2).join('-') }}</div>
                  <div class="duty-person" v-if="getDutyPerson(data)">
                    {{ getDutyPerson(data) }}
                  </div>
                </div>
              </template>
            </el-calendar>
          </div>
        </el-card>
      </el-col>

      <!-- 值班通知 -->
      <el-col :span="8">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>值班通知</span>
            </div>
          </template>
          <div v-if="notices.length > 0">
            <div v-for="(notice, index) in notices" :key="index" class="notice-item">
              <div class="notice-title">{{ notice.title }}</div>
              <div class="notice-content">{{ notice.content }}</div>
              <div class="notice-time">{{ notice.time }}</div>
            </div>
          </div>
          <el-empty v-else description="暂无通知"></el-empty>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="mt-20">
      <!-- 最近交接班记录 -->
      <el-col :span="24">
        <el-card class="box-card">
          <template #header>
            <div class="card-header">
              <span>最近交接班记录</span>
              <el-button class="more-btn" type="text">查看更多</el-button>
            </div>
          </template>
          <el-table :data="recentHandovers" style="width: 100%">
            <el-table-column prop="date" label="日期" width="180" />
            <el-table-column prop="handoverPerson" label="交班人" width="120" />
            <el-table-column prop="receivePerson" label="接班人" width="120" />
            <el-table-column prop="content" label="交接内容" />
            <el-table-column prop="status" label="状态" width="100">
              <template #default="scope">
                <el-tag :type="scope.row.status === '已完成' ? 'success' : 'warning'">
                  {{ scope.row.status }}
                </el-tag>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { reactive, ref, onMounted } from 'vue'
import { getDashboardStats, getDutyCalendar, getDutyNotices, getRecentHandovers } from '../api/duty'
import { ElMessage } from 'element-plus'

export default {
  name: 'MainDashboard',
  setup() {
    // 当前日期
    const currentDate = ref(new Date())

    // 统计数据
    const stats = reactive({
      dutyToday: 0,
      dutyWeek: 0,
      handoverMonth: 0,
      userCount: 0
    })

    // 值班通知
    const notices = reactive([])

    // 最近交接班记录
    const recentHandovers = reactive([])

    // 值班人员数据
    const dutyPersons = reactive({})

    // 获取仪表盘统计数据
    const fetchStats = async () => {
      try {
        const response = await getDashboardStats()
        Object.assign(stats, response.data)
      } catch (error) {
        ElMessage.error('获取统计数据失败')
      }
    }

    // 获取值班日历数据
    const fetchDutyCalendar = async () => {
      try {
        const date = new Date()
        const response = await getDutyCalendar(date.getFullYear(), date.getMonth() + 1)
        Object.assign(dutyPersons, response.data)
      } catch (error) {
        ElMessage.error('获取值班日历数据失败')
      }
    }

    // 获取值班通知
    const fetchNotices = async () => {
      try {
        const response = await getDutyNotices()
        notices.splice(0, notices.length, ...response.data)
      } catch (error) {
        ElMessage.error('获取值班通知失败')
      }
    }

    // 获取最近交接班记录
    const fetchRecentHandovers = async () => {
      try {
        const response = await getRecentHandovers()
        recentHandovers.splice(0, recentHandovers.length, ...response.data)
      } catch (error) {
        ElMessage.error('获取交接班记录失败')
      }
    }

    // 获取值班人员
    const getDutyPerson = (data) => {
      const dateStr = data.day
      return dutyPersons[dateStr]
    }

    // 组件挂载时获取数据
    onMounted(() => {
      fetchStats()
      fetchDutyCalendar()
      fetchNotices()
      fetchRecentHandovers()
    })

    return {
      currentDate,
      stats,
      notices,
      recentHandovers,
      getDutyPerson
    }
  }
}
</script>

<style scoped>
.dashboard-container {
  padding: 10px;
}
.mt-20 {
  margin-top: 20px;
}
.stat-card {
  text-align: center;
  color: #303133;
}
.stat-title {
  font-size: 14px;
  color: #606266;
}
.stat-value {
  font-size: 28px;
  font-weight: bold;
  margin-top: 10px;
}
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.more-btn {
  padding: 0;
  font-size: 14px;
}
.calendar-container {
  height: 400px;
}
.calendar-day {
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  padding: 5px;
}
.duty-person {
  font-size: 12px;
  color: #409EFF;
  margin-top: 5px;
}
.notice-item {
  margin-bottom: 15px;
  padding-bottom: 15px;
  border-bottom: 1px solid #ebeef5;
}
.notice-item:last-child {
  border-bottom: none;
  margin-bottom: 0;
  padding-bottom: 0;
}
.notice-title {
  font-weight: bold;
  margin-bottom: 5px;
}
.notice-content {
  font-size: 14px;
  color: #606266;
  margin-bottom: 5px;
}
.notice-time {
  font-size: 12px;
  color: #909399;
}
</style> 