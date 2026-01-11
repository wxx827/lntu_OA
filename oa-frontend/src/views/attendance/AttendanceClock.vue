<template>
  <div class="attendance-clock">
    <el-card class="clock-card">
      <template #header>
        <div class="card-header">
          <span>考勤打卡</span>
          <el-tag :type="todayStatus.type">{{ todayStatus.text }}</el-tag>
        </div>
      </template>

      <!-- 当前时间显示 -->
      <div class="time-display">
        <div class="current-time">{{ currentTime }}</div>
        <div class="current-date">{{ currentDate }}</div>
      </div>

      <!-- 打卡按钮 -->
      <div class="clock-buttons">
        <el-button 
          type="primary" 
          size="large" 
          :disabled="!canClockIn"
          @click="handleClockIn"
          class="clock-btn"
        >
          <el-icon><Clock /></el-icon>
          签到
        </el-button>
        <el-button 
          type="success" 
          size="large" 
          :disabled="!canClockOut"
          @click="handleClockOut"
          class="clock-btn"
        >
          <el-icon><CircleCheck /></el-icon>
          签退
        </el-button>
      </div>

      <!-- 今日考勤状态 -->
      <div class="today-status" v-if="todayAttendance">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="签到时间">
            {{ formatTime(todayAttendance.clockInTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="签退时间">
            {{ formatTime(todayAttendance.clockOutTime) }}
          </el-descriptions-item>
          <el-descriptions-item label="工作时长">
            {{ todayAttendance.workHours || '-' }} 小时
          </el-descriptions-item>
          <el-descriptions-item label="状态">
            <el-tag :type="getStatusType(todayAttendance.status)">
              {{ getStatusText(todayAttendance.status) }}
            </el-tag>
          </el-descriptions-item>
        </el-descriptions>
      </div>
    </el-card>

    <!-- 本月考勤统计 -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="出勤天数" :value="monthStats.actualDays || 0" />
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="迟到次数" :value="monthStats.lateTimes || 0">
            <template #suffix>次</template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="请假天数" :value="monthStats.leaveDays || 0">
            <template #suffix>天</template>
          </el-statistic>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover">
          <el-statistic title="出勤率" :value="monthStats.attendanceRate || 0" :precision="2">
            <template #suffix>%</template>
          </el-statistic>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Clock, CircleCheck } from '@element-plus/icons-vue'
import axios from 'axios'

const currentTime = ref('')
const currentDate = ref('')
const todayAttendance = ref(null)
const monthStats = ref({})
const userId = ref(1) // TODO: 从登录信息获取

let timer = null

const canClockIn = computed(() => {
  return !todayAttendance.value || !todayAttendance.value.clockInTime
})

const canClockOut = computed(() => {
  return todayAttendance.value && todayAttendance.value.clockInTime && !todayAttendance.value.clockOutTime
})

const todayStatus = computed(() => {
  if (!todayAttendance.value) {
    return { type: 'info', text: '未打卡' }
  }
  if (!todayAttendance.value.clockOutTime) {
    return { type: 'warning', text: '工作中' }
  }
  if (todayAttendance.value.status === 'LATE') {
    return { type: 'danger', text: '迟到' }
  }
  if (todayAttendance.value.status === 'EARLY_LEAVE') {
    return { type: 'warning', text: '早退' }
  }
  return { type: 'success', text: '正常' }
})

const updateTime = () => {
  const now = new Date()
  currentTime.value = now.toLocaleTimeString('zh-CN', { hour12: false })
  currentDate.value = now.toLocaleDateString('zh-CN', { 
    year: 'numeric', 
    month: 'long', 
    day: 'numeric',
    weekday: 'long'
  })
}

const loadTodayAttendance = async () => {
  try {
    const response = await axios.get('/api/attendance/today', {
      params: { userId: userId.value }
    })
    if (response.data.success) {
      todayAttendance.value = response.data.data
    }
  } catch (error) {
    console.error('加载今日考勤失败:', error)
  }
}

const loadMonthStats = async () => {
  try {
    const yearMonth = new Date().toISOString().slice(0, 7)
    const response = await axios.get(`/api/attendance/stats/${userId.value}/${yearMonth}`)
    if (response.data.success) {
      monthStats.value = response.data.data
    }
  } catch (error) {
    console.error('加载月度统计失败:', error)
  }
}

const handleClockIn = async () => {
  try {
    const response = await axios.post('/api/attendance/clock-in', null, {
      params: {
        userId: userId.value,
        location: '北京市海淀区' // TODO: 获取真实位置
      }
    })
    if (response.data.success) {
      ElMessage.success('签到成功')
      await loadTodayAttendance()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('签到失败')
  }
}

const handleClockOut = async () => {
  try {
    const response = await axios.post('/api/attendance/clock-out', null, {
      params: {
        userId: userId.value,
        location: '北京市海淀区' // TODO: 获取真实位置
      }
    })
    if (response.data.success) {
      ElMessage.success('签退成功')
      await loadTodayAttendance()
      await loadMonthStats()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('签退失败')
  }
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleTimeString('zh-CN', { hour12: false })
}

const getStatusType = (status) => {
  const map = {
    'NORMAL': 'success',
    'LATE': 'danger',
    'EARLY_LEAVE': 'warning',
    'ABSENT': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'NORMAL': '正常',
    'LATE': '迟到',
    'EARLY_LEAVE': '早退',
    'ABSENT': '缺勤'
  }
  return map[status] || '未知'
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 1000)
  loadTodayAttendance()
  loadMonthStats()
})

onUnmounted(() => {
  if (timer) {
    clearInterval(timer)
  }
})
</script>

<style scoped>
.attendance-clock {
  padding: 20px;
}

.clock-card {
  margin-bottom: 24px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.time-display {
  text-align: center;
  padding: 40px 0;
}

.current-time {
  font-size: 48px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.current-date {
  font-size: 18px;
  color: #909399;
}

.clock-buttons {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin: 32px 0;
}

.clock-btn {
  width: 150px;
  height: 50px;
  font-size: 16px;
}

.today-status {
  margin-top: 24px;
}

.stats-row {
  margin-top: 16px;
}
</style>
