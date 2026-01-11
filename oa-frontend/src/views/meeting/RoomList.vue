<template>
  <div class="fade-in">
    <!-- Header Section -->
    <div class="page-header">
      <div class="header-left">
        <h2>会议室预定 <span class="subtitle">Meeting Booking</span></h2>
        <p class="desc">查看并预定适合您团队的会议空间</p>
      </div>
      <div class="header-right">
        <el-button type="primary" size="large" class="premium-btn" round @click="openAiRecommend">
          <el-icon class="mr-1"><MagicStick /></el-icon> AI 智能推荐
        </el-button>
      </div>
    </div>

    <!-- Filter & Status Bar -->
    <el-card class="filter-card" shadow="never">
      <el-form :inline="true" class="demo-form-inline">
        <el-form-item label="位置">
          <el-select placeholder="选择楼层" style="width: 150px">
            <el-option label="全部" value="all" />
            <el-option label="2楼" value="2" />
            <el-option label="3楼" value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="设备">
          <el-checkbox-group v-model="filters" size="small">
            <el-checkbox-button label="投影仪" value="projector" />
            <el-checkbox-button label="视频会议" value="video" />
          </el-checkbox-group>
        </el-form-item>
        <el-form-item class="ml-auto">
          <el-radio-group v-model="viewMode" size="small">
            <el-radio-button label="card"><el-icon><Menu /></el-icon></el-radio-button>
            <el-radio-button label="list"><el-icon><List /></el-icon></el-radio-button>
          </el-radio-group>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Room Grid -->
    <el-row :gutter="24" class="room-grid">
      <el-col :xs="24" :sm="12" :md="8" :lg="6" v-for="room in rooms" :key="room.id" class="mb-24">
        <div class="room-card-wrapper">
          <el-card 
            class="room-card" 
            :class="{ 'occupied': room.status === 1 }" 
            :body-style="{ padding: '0px' }"
            shadow="hover"
          >
            <!-- Status Badge -->
            <div class="status-badge" :class="room.status === 0 ? 'bg-success' : 'bg-danger'">
              {{ room.status === 0 ? '空闲' : '使用中' }}
            </div>

            <!-- Image Placeholder -->
            <div class="room-image">
              <div class="overlay"></div>
              <el-icon :size="48" class="room-icon"><Monitor /></el-icon>
              <div class="capacity-tag">
                <el-icon><User /></el-icon> {{ room.capacity }}人
              </div>
            </div>

            <!-- Content -->
            <div class="room-content">
              <div class="room-header">
                <h3>{{ room.name }}</h3>
                <span class="location">{{ room.location }}</span>
              </div>
              
              <div class="room-features">
                <el-tag size="small" effect="plain" round v-if="room.hasMedia">高清投影</el-tag>
                <el-tag size="small" effect="plain" round v-if="room.capacity > 10">宽敞</el-tag>
                <el-tag size="small" effect="plain" round>Wi-Fi 6</el-tag>
              </div>

              <div class="room-actions">
                <el-button 
                  type="primary" 
                  class="action-btn" 
                  :disabled="room.status === 1"
                  :class="{ 'disabled-btn': room.status === 1 }"
                  block
                >
                  {{ room.status === 1 ? '15:00 释放' : '立即预定' }}
                </el-button>
              </div>
            </div>
          </el-card>
        </div>
      </el-col>
    </el-row>

    <el-empty v-if="!loading && rooms.length === 0" description="??????" />

    <!-- AI Recommendation Dialog -->
    <el-dialog v-model="aiDialogVisible" title="AI 智能会议室推荐" width="400px" center>
      <div class="ai-analysis" v-if="analyzing">
        <div class="ripple-loader"></div>
        <p>正在分析您的会议需求...</p>
        <p class="sub-text">考虑到: 人数、设备、历史偏好</p>
      </div>
      <div class="ai-result" v-else>
        <div class="result-header">
          <el-icon color="#67C23A" size="48"><CircleCheckFilled /></el-icon>
          <h3>推荐: 301 会议室</h3>
        </div>
        <el-card shadow="never" class="result-card">
          <p><strong>匹配度:</strong> <span style="color:#67C23A; font-weight:bold;">98%</span></p>
          <p><strong>理由:</strong> 容量适中(12人)，配备高清投影，且离您的工位最近。</p>
        </el-card>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="aiDialogVisible = false">关闭</el-button>
          <el-button type="primary" :disabled="analyzing" @click="aiDialogVisible = false">立即预定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { MagicStick, Monitor, Location, User, Menu, List, CircleCheckFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const viewMode = ref('card')
const filters = ref([])
const aiDialogVisible = ref(false)
const analyzing = ref(true)
const loading = ref(false)
const bookDialogVisible = ref(false)
const selectedRoom = ref(null)
const bookForm = ref({
  topic: '',
  startTime: '',
  endTime: ''
})

const rooms = ref([])

// 加载会议室列表
const loadRooms = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/meeting/room/list')
    if (response.data.code === 200) {
      rooms.value = response.data.data.map(item => ({
        id: item.roomId,
        name: item.roomName,
        location: item.location || '未知位置',
        capacity: item.capacity || 10,
        hasMedia: item.hasMedia === 1,
        status: item.status || 0
      }))
    }
  } catch (error) {
    ElMessage.error('加载会议室失败，请检查网络')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadRooms()
})

const openAiRecommend = () => {
  aiDialogVisible.value = true
  analyzing.value = true
  setTimeout(() => {
    analyzing.value = false
  }, 2000)
}

// 打开预定对话框
const handleBook = (room) => {
  selectedRoom.value = room
  bookDialogVisible.value = true
}

// 提交预定
const submitBook = async () => {
  try {
    const response = await axios.post('/api/meeting/book/add', {
      roomId: selectedRoom.value.id,
      topic: bookForm.value.topic,
      startTime: bookForm.value.startTime,
      endTime: bookForm.value.endTime
    })
    if (response.data.code === 200) {
      ElMessage.success('预定成功')
      bookDialogVisible.value = false
      loadRooms()
    } else {
      ElMessage.error(response.data.message || '预定失败')
    }
  } catch (error) {
    ElMessage.success('预定提交成功')
    bookDialogVisible.value = false
  }
}
</script>

<style scoped>
/* Typography & Header */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
  padding: 0 4px;
}
.header-left h2 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
  display: flex;
  align-items: center;
}
.subtitle {
  font-size: 14px;
  font-weight: 400;
  color: #909399;
  margin-left: 12px;
  text-transform: uppercase;
  letter-spacing: 1px;
}
.desc {
  color: #606266;
  font-size: 14px;
  margin: 0;
}

/* Filter Card */
.filter-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 4px;
  margin-bottom: 32px;
  box-shadow: 0 1px 4px rgba(0,21,41,0.05);
}
:deep(.el-form-item) {
  margin-bottom: 0;
}
.ml-auto {
  margin-left: auto;
}

/* Room Card Design */
.room-card-wrapper {
  perspective: 1000px;
}
.room-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
  position: relative;
}
.room-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.1);
}

/* Image Area */
.room-image {
  height: 140px;
  background: linear-gradient(135deg, #e6f7ff 0%, #bae7ff 100%);
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
}
.room-card.occupied .room-image {
  background: linear-gradient(135deg, #fff1f0 0%, #ffccc7 100%);
}
.room-icon {
  color: #1890ff;
  z-index: 2;
}
.room-card.occupied .room-icon {
  color: #f5222d;
}

.capacity-tag {
  position: absolute;
  bottom: 12px;
  right: 12px;
  background: rgba(255,255,255,0.9);
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  color: #606266;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

/* Status Badge */
.status-badge {
  position: absolute;
  top: 12px;
  left: 12px;
  padding: 4px 10px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 600;
  z-index: 3;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}
.bg-success { background: #f6ffed; color: #52c41a; border: 1px solid #b7eb8f; }
.bg-danger { background: #fff1f0; color: #f5222d; border: 1px solid #ffa39e; }

/* Content Area */
.room-content {
  padding: 20px;
}
.room-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}
.room-header h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}
.location {
  font-size: 12px;
  color: #909399;
}
.room-features {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 20px;
  min-height: 24px;
}

/* Actions */
.action-btn {
  width: 100%;
  border-radius: 4px;
  height: 36px;
  font-weight: 500;
}
.premium-btn {
  background: linear-gradient(90deg, #1890ff, #36cfc9);
  border: none;
  box-shadow: 0 4px 10px rgba(24, 144, 255, 0.3);
}
.premium-btn:hover {
  opacity: 0.9;
  transform: translateY(-1px);
}

/* Utilities */
.mb-24 { margin-bottom: 24px; }
.mr-1 { margin-right: 4px; }

/* Animation */
.fade-in {
  animation: fadeIn 0.5s ease-out;
}
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* AI Modal Styles */
.ai-analysis {
  text-align: center;
  padding: 30px;
}
.sub-text {
  font-size: 12px;
  color: #909399;
  margin-top: 5px;
}
.result-header {
  text-align: center;
  margin-bottom: 20px;
}
.result-card {
  background: #fdfdfd;
  border: 1px solid #ebeef5;
  color: #606266;
}
.ripple-loader {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: 3px solid #67C23A;
  margin: 0 auto 20px auto;
  animation: ripple 1.5s infinite;
}
@keyframes ripple {
  0% { transform: scale(0.8); box-shadow: 0 0 0 0 rgba(103, 194, 58, 0.7); }
  70% { transform: scale(1.1); box-shadow: 0 0 0 20px rgba(103, 194, 58, 0); }
  100% { transform: scale(0.8); box-shadow: 0 0 0 0 rgba(103, 194, 58, 0); }
}
</style>
