<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>我的日程 <span class="subtitle">My Calendar</span></h2>
        <p class="desc">聚合显示会议、审批、用车等日程安排</p>
      </div>
      <div class="header-right">
        <el-button-group>
          <el-button :type="viewMode === 'month' ? 'primary' : ''" @click="viewMode = 'month'">月视图</el-button>
          <el-button :type="viewMode === 'week' ? 'primary' : ''" @click="viewMode = 'week'">周视图</el-button>
          <el-button :type="viewMode === 'list' ? 'primary' : ''" @click="viewMode = 'list'">列表</el-button>
        </el-button-group>
      </div>
    </div>

    <!-- Calendar View -->
    <el-row :gutter="20" v-if="viewMode !== 'list'">
      <el-col :span="18">
        <el-card shadow="never" class="card-shadow">
          <el-calendar v-model="currentDate">
            <template #date-cell="{ data }">
              <div class="calendar-day">
                <div class="day-number">{{ data.day.split('-').slice(-1)[0] }}</div>
                <div class="events-container">
                  <div 
                    v-for="event in getEventsForDate(data.day)" 
                    :key="event.id"
                    :class="['event-dot', `event-${event.type}`]"
                    @click="showEventDetail(event)"
                  >
                    <el-tooltip :content="event.title" placement="top">
                      <span class="event-label">{{ event.title.substring(0, 6) }}...</span>
                    </el-tooltip>
                  </div>
                </div>
              </div>
            </template>
          </el-calendar>
        </el-card>
      </el-col>

      <!-- Sidebar: Today's Events -->
      <el-col :span="6">
        <el-card shadow="never" class="card-shadow">
          <template #header>
            <div class="card-header-title">今日日程</div>
          </template>
          <el-timeline>
            <el-timeline-item 
              v-for="event in todayEvents" 
              :key="event.id"
              :timestamp="event.start"
              :type="getEventTimelineType(event.type)"
              placement="top"
            >
              <div class="timeline-event">
                <div class="event-title">{{ event.title }}</div>
                <el-tag :type="getEventTagType(event.type)" size="small">{{ event.typeLabel }}</el-tag>
              </div>
            </el-timeline-item>
            <el-empty v-if="todayEvents.length === 0" description="今日暂无日程" :image-size="80" />
          </el-timeline>
        </el-card>
      </el-col>
    </el-row>

    <!-- List View -->
    <el-card shadow="never" class="card-shadow" v-else>
      <el-table :data="allEvents" style="width: 100%" :header-cell-style="{ background: '#f5f7fa' }">
        <el-table-column prop="start" label="时间" width="180" sortable />
        <el-table-column prop="title" label="事件标题" min-width="200" />
        <el-table-column prop="typeLabel" label="类型" width="120">
          <template #default="scope">
            <el-tag :type="getEventTagType(scope.row.type)" size="small">{{ scope.row.typeLabel }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="scope.row.status === 'CONFIRMED' ? 'success' : 'warning'" size="small">
              {{ scope.row.status === 'CONFIRMED' ? '已确认' : '待定' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="100">
          <template #default="scope">
            <el-button link type="primary" @click="showEventDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- Event Detail Dialog -->
    <el-dialog v-model="showDetailDialog" title="日程详情" width="500px">
      <el-descriptions :column="1" border v-if="selectedEvent">
        <el-descriptions-item label="标题">{{ selectedEvent.title }}</el-descriptions-item>
        <el-descriptions-item label="类型">
          <el-tag :type="getEventTagType(selectedEvent.type)">{{ selectedEvent.typeLabel }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="开始时间">{{ selectedEvent.start }}</el-descriptions-item>
        <el-descriptions-item label="结束时间">{{ selectedEvent.end || '无' }}</el-descriptions-item>
        <el-descriptions-item label="状态">{{ selectedEvent.status }}</el-descriptions-item>
        <el-descriptions-item label="描述">{{ selectedEvent.description || '无' }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const currentDate = ref(new Date())
const viewMode = ref('month')
const showDetailDialog = ref(false)
const selectedEvent = ref(null)
const loading = ref(false)

const allEvents = ref([])

// 获取类型标签
const getTypeLabel = (type) => {
  const map = { meeting: '会议', workflow: '审批', task: '待办', car: '用车' }
  return map[type] || '其他'
}

// 加载日程事件
const loadEvents = async () => {
  loading.value = true
  try {
    const data = await request.get('/schedule/my-events')
    allEvents.value = (data || []).map(item => ({
      id: item.id,
      title: item.title,
      start: formatDateTime(item.start),
      end: item.end ? formatDateTime(item.end) : null,
      type: item.type || 'task',
      typeLabel: getTypeLabel(item.type),
      status: item.allDay ? 'PENDING' : 'CONFIRMED',
      color: item.color,
      description: ''
    }))
  } catch (error) {
    console.error('加载日程失败:', error)
    allEvents.value = []
  } finally {
    loading.value = false
  }
}

// 格式化日期时间
const formatDateTime = (dateStr) => {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  const y = date.getFullYear()
  const m = String(date.getMonth() + 1).padStart(2, '0')
  const d = String(date.getDate()).padStart(2, '0')
  const h = String(date.getHours()).padStart(2, '0')
  const min = String(date.getMinutes()).padStart(2, '0')
  return `${y}-${m}-${d} ${h}:${min}`
}

onMounted(() => {
  loadEvents()
})

const todayEvents = computed(() => {
  const today = new Date().toISOString().split('T')[0]
  return allEvents.value.filter(e => e.start && e.start.startsWith(today))
})

const getEventsForDate = (dateStr) => {
  return allEvents.value.filter(e => e.start && e.start.startsWith(dateStr))
}

const getEventTagType = (type) => {
  const map = { meeting: 'primary', workflow: 'warning', car: 'success', task: 'info' }
  return map[type] || 'info'
}

const getEventTimelineType = (type) => {
  const map = { meeting: 'primary', workflow: 'warning', car: 'success', task: 'info' }
  return map[type] || 'info'
}

const showEventDetail = (event) => {
  selectedEvent.value = event
  showDetailDialog.value = true
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

/* Calendar Customization */
.calendar-day { min-height: 80px; padding: 4px; }
.day-number { font-weight: 600; color: #303133; margin-bottom: 4px; }
.events-container { display: flex; flex-direction: column; gap: 2px; }
.event-dot { font-size: 11px; padding: 2px 4px; border-radius: 3px; cursor: pointer; transition: all 0.2s; }
.event-dot:hover { transform: scale(1.05); }
.event-meeting { background: #ecf5ff; color: #409eff; }
.event-workflow { background: #fdf6ec; color: #e6a23c; }
.event-car { background: #f0f9ff; color: #67c23a; }
.event-task { background: #f4f4f5; color: #909399; }
.event-label { font-size: 11px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

/* Timeline */
.timeline-event { display: flex; justify-content: space-between; align-items: center; }
.event-title { font-weight: 500; color: #303133; }

.card-header-title { font-weight: 600; color: #303133; }
</style>
