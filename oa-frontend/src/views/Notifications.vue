<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>消息通知 <span class="subtitle">Notifications</span></h2>
        <p class="desc">集中查看系统通知、流程待办及重要公告</p>
      </div>
      <div class="header-right">
        <el-button plain @click="markAllRead">全部已读</el-button>
      </div>
    </div>

    <el-card shadow="never" class="card-shadow">
      <el-tabs v-model="activeTab">
        <el-tab-pane :label="'全部 (' + list.length + ')'" name="all"></el-tab-pane>
        <el-tab-pane label="系统公告" name="system"></el-tab-pane>
        <el-tab-pane label="流程审批" name="workflow"></el-tab-pane>
        <el-tab-pane label="@我" name="mention"></el-tab-pane>
      </el-tabs>

      <div class="notification-list">
        <div 
          v-for="item in filteredList" 
          :key="item.id" 
          class="notif-item"
          :class="{ 'unread': !item.read }"
          @click="readItem(item)"
        >
          <div class="notif-icon" :class="item.type">
            <el-icon color="#fff"><component :is="getIcon(item.type)" /></el-icon>
          </div>
          <div class="notif-content">
             <div class="notif-header">
               <span class="notif-title">{{ item.title }}</span>
               <el-tag v-if="!item.read" size="small" type="danger" effect="dark" class="new-tag">NEW</el-tag>
               <span class="notif-time">{{ item.time }}</span>
             </div>
             <p class="notif-desc">{{ item.desc }}</p>
          </div>
        </div>
        <el-empty v-if="filteredList.length === 0" description="暂无通知" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, watch } from 'vue'
import { Bell, Stamp, ChatDotSquare, InfoFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const activeTab = ref('all')
const loading = ref(false)

const list = ref([])

// 加载通知列表
const loadNotifications = async () => {
  loading.value = true
  try {
    const type = activeTab.value === 'all' ? null : activeTab.value
    const data = await request.get('/notification/list', { params: { type } })
    list.value = (data || []).map(item => ({
      id: item.notificationId,
      type: item.notificationType || 'system',
      title: item.title,
      desc: item.content,
      time: formatTime(item.createTime),
      read: item.isRead === 1
    }))
  } catch (error) {
    console.error('加载通知失败:', error)
    list.value = []
  } finally {
    loading.value = false
  }
}

// 格式化时间
const formatTime = (timeStr) => {
  if (!timeStr) return ''
  const date = new Date(timeStr)
  const now = new Date()
  const diff = now - date
  if (diff < 60000) return '刚刚'
  if (diff < 3600000) return Math.floor(diff / 60000) + '分钟前'
  if (diff < 86400000) return Math.floor(diff / 3600000) + '小时前'
  if (diff < 172800000) return '昨天'
  return date.toLocaleDateString()
}

onMounted(() => {
  loadNotifications()
})

// 监听标签切换，重新加载数据
watch(activeTab, () => {
  loadNotifications()
})

const filteredList = computed(() => {
  if (activeTab.value === 'all') return list.value
  return list.value.filter(item => item.type === activeTab.value)
})

const getIcon = (type) => {
  if (type === 'system') return 'Bell'
  if (type === 'workflow') return 'Stamp'
  if (type === 'mention') return 'ChatDotSquare'
  return 'InfoFilled'
}

// 标记单条已读
const readItem = async (item) => {
  if (!item.read) {
    try {
      await request.put(`/notification/read/${item.id}`)
      item.read = true
    } catch (error) {
      item.read = true // 即使API失败也在本地标记
    }
  }
}

// 标记全部已读
const markAllRead = async () => {
  try {
    await request.put('/notification/read-all')
    list.value.forEach(i => i.read = true)
    ElMessage.success('全部已读')
  } catch (error) {
    ElMessage.error('操作失败，请检查网络连接')
  }
}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  margin-bottom: 24px;
}
.header-left h2 { font-size: 24px; margin: 0 0 8px 0; color: #303133; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; font-weight: 400; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

.notification-list { margin-top: 16px; }
.notif-item {
  display: flex;
  padding: 20px;
  border-bottom: 1px solid #f0f2f5;
  cursor: pointer;
  transition: background 0.2s;
}
.notif-item:hover { background: #f9fafc; }
.notif-item.unread { background: #fff; }
.notif-item.unread .notif-title { font-weight: 600; color: #303133; }

.notif-icon {
  width: 48px; height: 48px; border-radius: 50%; display: flex; align-items: center; justify-content: center; margin-right: 16px; font-size: 24px; flex-shrink: 0;
}
.notif-icon.system { background: #e6a23c; }
.notif-icon.workflow { background: #409eff; }
.notif-icon.mention { background: #f56c6c; }

.notif-content { flex: 1; }
.notif-header { display: flex; align-items: center; margin-bottom: 8px; }
.notif-title { font-size: 16px; color: #606266; margin-right: 8px; }
.notif-time { margin-left: auto; font-size: 12px; color: #909399; }
.notif-desc { margin: 0; font-size: 14px; color: #606266; line-height: 1.5; }
.new-tag { margin-right: 8px; border-radius: 4px; height: 18px; line-height: 16px; padding: 0 4px; }
</style>
