<template>
  <div class="attendance-stats">
    <el-card class="month-selector">
      <el-date-picker
        v-model="selectedMonth"
        type="month"
        placeholder="选择月份"
        format="YYYY年MM月"
        value-format="YYYY-MM"
        @change="loadStats"
      />
    </el-card>

    <!-- 统计卡片 -->
    <el-row :gutter="16" class="stats-cards">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #ecf5ff; color: #409eff;">
              <el-icon :size="32"><Calendar /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">应出勤天数</div>
              <div class="stat-value">{{ stats.workDays || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #f0f9ff; color: #67c23a;">
              <el-icon :size="32"><CircleCheck /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">实际出勤</div>
              <div class="stat-value">{{ stats.actualDays || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #fef0f0; color: #f56c6c;">
              <el-icon :size="32"><Warning /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">迟到次数</div>
              <div class="stat-value">{{ stats.lateTimes || 0 }}</div>
            </div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" style="background: #fdf6ec; color: #e6a23c;">
              <el-icon :size="32"><TrendCharts /></el-icon>
            </div>
            <div class="stat-info">
              <div class="stat-label">出勤率</div>
              <div class="stat-value">{{ (stats.attendanceRate || 0).toFixed(2) }}%</div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 详细统计 -->
    <el-row :gutter="16" class="detail-stats">
      <el-col :span="12">
        <el-card header="考勤详情">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="早退次数">
              {{ stats.earlyLeaveTimes || 0 }} 次
            </el-descriptions-item>
            <el-descriptions-item label="缺勤天数">
              {{ stats.absentDays || 0 }} 天
            </el-descriptions-item>
            <el-descriptions-item label="请假天数">
              {{ stats.leaveDays || 0 }} 天
            </el-descriptions-item>
            <el-descriptions-item label="加班时长">
              {{ stats.overtimeHours || 0 }} 小时
            </el-descriptions-item>
            <el-descriptions-item label="出差天数">
              {{ stats.tripDays || 0 }} 天
            </el-descriptions-item>
            <el-descriptions-item label="工作天数">
              {{ stats.actualDays || 0 }} 天
            </el-descriptions-item>
          </el-descriptions>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card header="考勤趋势">
          <div ref="chartRef" class="chart-container"></div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 考勤状态分布 -->
    <el-card header="考勤状态分布" class="status-distribution">
      <el-row :gutter="16">
        <el-col :span="6">
          <div class="status-item">
            <el-progress type="circle" :percentage="normalRate" color="#67c23a" />
            <div class="status-label">正常</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="status-item">
            <el-progress type="circle" :percentage="lateRate" color="#f56c6c" />
            <div class="status-label">迟到</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="status-item">
            <el-progress type="circle" :percentage="leaveRate" color="#e6a23c" />
            <div class="status-label">请假</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="status-item">
            <el-progress type="circle" :percentage="absentRate" color="#909399" />
            <div class="status-label">缺勤</div>
          </div>
        </el-col>
      </el-row>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, nextTick } from 'vue'
import { Calendar, CircleCheck, Warning, TrendCharts } from '@element-plus/icons-vue'
import axios from 'axios'
import * as echarts from 'echarts'

const selectedMonth = ref('')
const stats = ref({})
const chartRef = ref(null)
let chartInstance = null
const userId = ref(1) // TODO: 从登录信息获取

const normalRate = computed(() => {
  if (!stats.value.workDays || !stats.value.actualDays) return 0
  const normalDays = stats.value.actualDays - (stats.value.lateTimes || 0) - (stats.value.earlyLeaveTimes || 0)
  return Math.round((normalDays / stats.value.workDays) * 100)
})

const lateRate = computed(() => {
  if (!stats.value.workDays || !stats.value.lateTimes) return 0
  return Math.round((stats.value.lateTimes / stats.value.workDays) * 100)
})

const leaveRate = computed(() => {
  if (!stats.value.workDays || !stats.value.leaveDays) return 0
  return Math.round((stats.value.leaveDays / stats.value.workDays) * 100)
})

const absentRate = computed(() => {
  if (!stats.value.workDays || !stats.value.absentDays) return 0
  return Math.round((stats.value.absentDays / stats.value.workDays) * 100)
})

const loadStats = async () => {
  if (!selectedMonth.value) {
    selectedMonth.value = new Date().toISOString().slice(0, 7)
  }

  try {
    const response = await axios.get(`/api/attendance/stats/${userId.value}/${selectedMonth.value}`)
    if (response.data.success) {
      stats.value = response.data.data
      await nextTick()
      initChart()
    }
  } catch (error) {
    console.error('加载统计数据失败:', error)
  }
}

const initChart = () => {
  if (!chartRef.value) return

  if (chartInstance) {
    chartInstance.dispose()
  }

  chartInstance = echarts.init(chartRef.value)

  const option = {
    tooltip: {
      trigger: 'axis'
    },
    legend: {
      data: ['出勤', '迟到', '请假', '缺勤']
    },
    xAxis: {
      type: 'category',
      data: ['第1周', '第2周', '第3周', '第4周']
    },
    yAxis: {
      type: 'value'
    },
    series: [
      {
        name: '出勤',
        type: 'bar',
        data: [5, 5, 4, 5],
        itemStyle: { color: '#67c23a' }
      },
      {
        name: '迟到',
        type: 'bar',
        data: [0, 1, 1, 0],
        itemStyle: { color: '#f56c6c' }
      },
      {
        name: '请假',
        type: 'bar',
        data: [0, 0, 1, 0],
        itemStyle: { color: '#e6a23c' }
      },
      {
        name: '缺勤',
        type: 'bar',
        data: [0, 0, 0, 0],
        itemStyle: { color: '#909399' }
      }
    ]
  }

  chartInstance.setOption(option)
}

onMounted(() => {
  loadStats()
})
</script>

<style scoped>
.attendance-stats {
  padding: 20px;
}

.month-selector {
  margin-bottom: 24px;
  text-align: center;
}

.stats-cards {
  margin-bottom: 24px;
}

.stat-card {
  height: 100%;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  color: #303133;
}

.detail-stats {
  margin-bottom: 24px;
}

.chart-container {
  width: 100%;
  height: 300px;
}

.status-distribution {
  margin-top: 24px;
}

.status-item {
  text-align: center;
  padding: 20px 0;
}

.status-label {
  margin-top: 12px;
  font-size: 14px;
  color: #606266;
}
</style>
