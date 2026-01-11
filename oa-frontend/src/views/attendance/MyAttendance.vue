<template>
  <div class="my-attendance">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>我的考勤记录</span>
          <el-date-picker
            v-model="dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            @change="loadRecords"
          />
        </div>
      </template>

      <!-- 考勤日历视图 -->
      <el-calendar v-model="currentDate">
        <template #date-cell="{ data }">
          <div class="calendar-cell" @click="showDetail(data.day)">
            <div class="date-num">{{ data.day.split('-').slice(2).join('-') }}</div>
            <div v-if="getAttendanceByDate(data.day)" class="attendance-badge">
              <el-tag 
                :type="getStatusType(getAttendanceByDate(data.day).status)" 
                size="small"
              >
                {{ getStatusText(getAttendanceByDate(data.day).status) }}
              </el-tag>
            </div>
          </div>
        </template>
      </el-calendar>

      <!-- 考勤记录表格 -->
      <div class="records-table" style="margin-top: 24px;">
        <el-table :data="attendanceRecords" stripe>
          <el-table-column prop="workDate" label="日期" width="120">
            <template #default="{ row }">
              {{ formatDate(row.workDate) }}
            </template>
          </el-table-column>
          <el-table-column prop="clockInTime" label="签到时间" width="150">
            <template #default="{ row }">
              {{ formatTime(row.clockInTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="clockOutTime" label="签退时间" width="150">
            <template #default="{ row }">
              {{ formatTime(row.clockOutTime) }}
            </template>
          </el-table-column>
          <el-table-column prop="workHours" label="工作时长" width="100">
            <template #default="{ row }">
              {{ row.workHours || '-' }} 小时
            </template>
          </el-table-column>
          <el-table-column prop="status" label="状态" width="100">
            <template #default="{ row }">
              <el-tag :type="getStatusType(row.status)">
                {{ getStatusText(row.status) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="clockInLocation" label="签到地点" show-overflow-tooltip />
          <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        </el-table>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const currentDate = ref(new Date())
const dateRange = ref([])
const attendanceRecords = ref([])
const userId = ref(1) // TODO: 从登录信息获取

const loadRecords = async () => {
  if (!dateRange.value || dateRange.value.length !== 2) {
    // 默认加载本月数据
    const now = new Date()
    const startDate = new Date(now.getFullYear(), now.getMonth(), 1)
    const endDate = new Date(now.getFullYear(), now.getMonth() + 1, 0)
    dateRange.value = [startDate, endDate]
  }

  try {
    const response = await axios.get('/api/attendance/my-records', {
      params: {
        userId: userId.value,
        startDate: formatDateParam(dateRange.value[0]),
        endDate: formatDateParam(dateRange.value[1])
      }
    })
    if (response.data.success) {
      attendanceRecords.value = response.data.data
    }
  } catch (error) {
    console.error('加载考勤记录失败:', error)
  }
}

const getAttendanceByDate = (dateStr) => {
  return attendanceRecords.value.find(record => {
    const recordDate = formatDate(record.workDate)
    return recordDate === dateStr
  })
}

const showDetail = (date) => {
  const attendance = getAttendanceByDate(date)
  if (attendance) {
    console.log('考勤详情:', attendance)
  }
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return d.toISOString().split('T')[0]
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleTimeString('zh-CN', { hour12: false })
}

const formatDateParam = (date) => {
  const d = new Date(date)
  return d.toISOString().split('T')[0]
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
  loadRecords()
})
</script>

<style scoped>
.my-attendance {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.calendar-cell {
  height: 100%;
  padding: 4px;
  cursor: pointer;
}

.calendar-cell:hover {
  background-color: #f5f7fa;
}

.date-num {
  font-size: 14px;
  color: #606266;
}

.attendance-badge {
  margin-top: 4px;
}
</style>
