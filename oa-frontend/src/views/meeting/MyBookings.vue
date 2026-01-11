<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>我的预定 <span class="subtitle">My Bookings</span></h2>
        <p class="desc">管理您的会议室预定记录</p>
      </div>
    </div>

    <!-- Booking List -->
    <el-row :gutter="24">
      <el-col :xs="24" :sm="12" :lg="8" v-for="book in bookings" :key="book.id" class="mb-24">
        <el-card class="booking-card" shadow="hover" :body-style="{ padding: '0' }">
          <div class="card-status-strip" :class="getStatusClass(book.status)"></div>
          <div class="card-content">
            <div class="card-header">
              <span class="room-name">{{ book.roomName }}</span>
              <el-tag :type="getStatusType(book.status)" size="small" effect="dark" round>
                {{ getStatusText(book.status) }}
              </el-tag>
            </div>
            
            <h3 class="topic">{{ book.topic }}</h3>
            
            <div class="meta-info">
              <div class="meta-item">
                <el-icon><Calendar /></el-icon>
                <span>{{ book.date }}</span>
              </div>
              <div class="meta-item">
                <el-icon><Clock /></el-icon>
                <span>{{ book.startTime }} - {{ book.endTime }}</span>
              </div>
            </div>

            <div class="actions">
              <el-button 
                v-if="book.status === 0" 
                type="danger" 
                plain 
                size="small" 
                class="w-100"
                @click="cancelBooking(book.id)"
              >
                取消预定
              </el-button>
              <el-button 
                v-else 
                type="info" 
                text 
                bg 
                size="small" 
                class="w-100"
                disabled
              >
                {{ book.status === 1 ? '已生效' : '已结束' }}
              </el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Empty State -->
    <el-empty v-if="bookings.length === 0" description="暂无预定记录" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Calendar, Clock } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const loading = ref(false)
const bookings = ref([])

// 加载我的预定列表
const loadMyBookings = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/meeting/book/my')
    if (response.data.code === 200) {
      bookings.value = response.data.data.map(item => ({
        id: item.bookId,
        roomName: item.roomName || item.room?.roomName || '会议室',
        topic: item.topic || '会议',
        date: item.startTime?.split(' ')[0] || new Date().toLocaleDateString(),
        startTime: item.startTime?.split(' ')[1]?.substring(0,5) || '09:00',
        endTime: item.endTime?.split(' ')[1]?.substring(0,5) || '10:00',
        status: item.status
      }))
    }
  } catch (error) {
    console.error('加载预定列表失败:', error)
    bookings.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadMyBookings()
})

const getStatusClass = (status) => {
  if (status === 1) return 'strip-success'
  if (status === 0) return 'strip-warning'
  return 'strip-danger'
}

const getStatusType = (status) => {
  if (status === 1) return 'success'
  if (status === 0) return 'warning'
  return 'info'
}

const getStatusText = (status) => {
  if (status === 1) return '已通过'
  if (status === 0) return '审核中'
  return '已结束/驳回'
}

const cancelBooking = async (id) => {
  try {
    const response = await axios.put(`/api/meeting/book/cancel/${id}`)
    if (response.data.code === 200) {
      ElMessage.success('预定已取消')
      loadMyBookings()
    } else {
      ElMessage.error(response.data.message || '取消失败')
    }
  } catch (error) {
    console.error('取消失败:', error)
    ElMessage.error('取消预定失败，请检查网络连接')
  }
}
</script>

<style scoped>
.page-header {
  margin-bottom: 24px;
}
.header-left h2 {
  font-size: 24px;
  color: #303133;
  margin: 0 0 8px 0;
}
.subtitle {
  font-size: 14px;
  color: #909399;
  margin-left: 12px;
  letter-spacing: 1px;
}
.desc { color: #606266; font-size: 14px; margin: 0; }

.booking-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  position: relative;
  transition: transform 0.2s;
}
.booking-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0,0,0,0.1);
}

.card-status-strip {
  height: 4px;
  width: 100%;
  position: absolute;
  top: 0;
  left: 0;
}
.strip-success { background: #67C23A; }
.strip-warning { background: #E6A23C; }
.strip-danger { background: #909399; }

.card-content {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.room-name {
  font-size: 14px;
  color: #606266;
  font-weight: 500;
}

.topic {
  font-size: 18px;
  color: #303133;
  margin: 0 0 16px 0;
  font-weight: 600;
  line-height: 1.4;
}

.meta-info {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-bottom: 20px;
}
.meta-item {
  display: flex;
  align-items: center;
  gap: 8px;
  color: #606266;
  font-size: 14px;
}

.actions {
  border-top: 1px solid #ebeef5;
  padding-top: 16px;
}

.w-100 { width: 100%; }
.mb-24 { margin-bottom: 24px; }
.fade-in { animation: fadeIn 0.5s ease-out; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
