<template>
  <div class="fade-in h-100">
    <el-container class="h-100 mail-layout">
      <!-- Sidebar: Mailboxes -->
      <el-aside width="220px" class="mail-sider">
        <div class="compose-btn-wrapper">
          <el-button type="primary" icon="EditPen" class="w-100" size="large" @click="showCompose = true">写信</el-button>
        </div>
        <el-menu
          :default-active="activeBox"
          class="mail-menu"
          @select="handleBoxSelect"
        >
          <el-menu-item index="inbox">
            <el-icon><Message /></el-icon>
            <span>收件箱</span>
            <el-tag v-if="unreadCount > 0" type="danger" size="small" round class="unread-tag">{{ unreadCount }}</el-tag>
          </el-menu-item>
          <el-menu-item index="outbox">
            <el-icon><Position /></el-icon>
            <span>发件箱</span>
          </el-menu-item>
          <el-menu-item index="drafts">
            <el-icon><Edit /></el-icon>
            <span>草稿箱</span>
          </el-menu-item>
          <el-menu-item index="trash">
            <el-icon><Delete /></el-icon>
            <span>已删除</span>
          </el-menu-item>
        </el-menu>
      </el-aside>

      <!-- Main: List & Detail -->
      <el-main class="mail-main">
        <div class="mail-header">
           <h3>{{ getBoxTitle(activeBox) }}</h3>
           <div class="header-actions">
              <el-input v-model="searchText" placeholder="搜索邮件" prefix-icon="Search" style="width: 200px" />
              <el-button icon="Refresh" circle @click="loadMessages" />
           </div>
        </div>

        <div v-loading="loading" class="mail-content">
           <el-table :data="messageList" style="width: 100%" @row-click="handleRowClick" :row-class-name="tableRowClassName">
              <el-table-column width="40" align="center">
                 <template #default="{ row }">
                    <span v-if="!row.isRead" class="dot"></span>
                 </template>
              </el-table-column>
              <el-table-column prop="sender" label="发件人" width="180" v-if="activeBox === 'inbox'" />
              <el-table-column prop="receiver" label="收件人" width="180" v-else />
              <el-table-column prop="subject" label="主题" min-width="300" show-overflow-tooltip>
                 <template #default="{ row }">
                    <span class="subject-text">{{ row.subject }}</span>
                    <span class="preview-text"> - {{ row.preview }}</span>
                 </template>
              </el-table-column>
              <el-table-column prop="time" label="时间" width="160" align="right" />
           </el-table>
           <el-empty v-if="messageList.length === 0" description="暂无邮件" />
        </div>
      </el-main>
    </el-container>

    <!-- Compose Dialog -->
    <el-dialog v-model="showCompose" title="写信" width="800px" :close-on-click-modal="false">
       <el-form :model="composeForm" label-width="80px">
          <el-form-item label="收件人">
             <el-select
                v-model="composeForm.toUserIds"
                multiple
                filterable
                placeholder="请选择同事"
                style="width: 100%"
                remote
                :remote-method="searchUsers"
                :loading="userLoading"
             >
                <el-option
                  v-for="item in userOptions"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value"
                />
             </el-select>
          </el-form-item>
          <el-form-item label="主题">
             <el-input v-model="composeForm.subject" placeholder="邮件主题" />
          </el-form-item>
          <el-form-item label="正文">
             <el-input
               v-model="composeForm.content"
               type="textarea"
               :rows="12"
               placeholder="请输入邮件内容..."
             />
          </el-form-item>
          <el-form-item>
             <el-checkbox v-model="composeForm.isUrgent">紧急邮件</el-checkbox>
          </el-form-item>
       </el-form>
       <template #footer>
          <div class="dialog-footer">
             <el-button @click="showCompose = false">取消</el-button>
             <el-button type="info" @click="saveDraft">存草稿</el-button>
             <el-button type="primary" @click="sendMail" :loading="sending">发送</el-button>
          </div>
       </template>
    </el-dialog>

    <!-- Read Dialog -->
    <el-dialog v-model="showRead" :title="currentMail?.subject" width="800px">
       <div class="mail-reader" v-if="currentMail">
          <div class="reader-meta">
             <div class="meta-row"><strong>发件人：</strong>{{ currentMail.sender }}</div>
             <div class="meta-row"><strong>时间：</strong>{{ currentMail.time }}</div>
             <div class="meta-row"><strong>收件人：</strong>{{ currentMail.receiver }}</div>
          </div>
          <div class="reader-content">
             {{ currentMail.content }}
          </div>
       </div>
       <template #footer>
          <el-button @click="showRead = false">关闭</el-button>
          <el-button type="primary" icon="Reply">回复</el-button>
       </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { EditPen, Message, Position, Edit, Delete, Search, Refresh, Reply } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

// State
const activeBox = ref('inbox')
const searchText = ref('')
const loading = ref(false)
const messageList = ref([])
const unreadCount = ref(0)
const userId = 1 // TODO

// Compose
const showCompose = ref(false)
const sending = ref(false)
const userLoading = ref(false)
const userOptions = ref([]) // { label: 'Name', value: id }
const composeForm = reactive({
  toUserIds: [],
  subject: '',
  content: '',
  isUrgent: false
})

// Read
const showRead = ref(false)
const currentMail = ref(null)

// Methods

const getBoxTitle = (box) => {
   const map = {
      'inbox': '收件箱',
      'outbox': '发件箱',
      'drafts': '草稿箱',
      'trash': '已删除'
   }
   return map[box]
}

const handleBoxSelect = (index) => {
   activeBox.value = index
   loadMessages()
}

const loadMessages = async () => {
   loading.value = true
   try {
      const res = await axios.get('/api/message/list', {
         params: { box: activeBox.value, userId }
      })
      if (res.data.success) {
         messageList.value = res.data.data
         if (activeBox.value === 'inbox') {
            unreadCount.value = messageList.value.filter(m => !m.isRead).length
         }
      } else {
         // Mock data if API fails to avoid empty screen
         messageList.value = []
         console.warn('API not ready, empty list')
      }
   } catch(e) {
      console.error(e)
   } finally {
      loading.value = false
   }
}

const tableRowClassName = ({ row }) => {
   return row.isRead ? 'read-row' : 'unread-row'
}

const handleRowClick = async (row) => {
   currentMail.value = { ...row, content: '正在加载内容...' }
   showRead.value = true
   
   // Fetch full content
   try {
      const res = await axios.get(`/api/message/detail/${row.id}`)
      if (res.data.success) {
         currentMail.value = res.data.data
         
         // Mark read locally
         if (!row.isRead && activeBox.value === 'inbox') {
             row.isRead = true
             unreadCount.value = Math.max(0, unreadCount.value - 1)
         }
      }
   } catch(e) {
      currentMail.value.content = '加载失败'
   }
}

// Compose Logic
const searchUsers = async (query) => {
   if (!query) return
   userLoading.value = true
   try {
      const res = await axios.get('/api/system/user/search', { params: { keyword: query } })
      if (res.data.success) {
         userOptions.value = res.data.data.map(u => ({ label: u.username, value: u.id }))
      }
   } finally {
      userLoading.value = false
   }
}

const sendMail = async () => {
   if (!composeForm.toUserIds.length || !composeForm.subject) {
      ElMessage.warning('请填写收件人和主题')
      return
   }
   sending.value = true
   try {
      const res = await axios.post('/api/message/send', {
         senderId: userId,
         ...composeForm
      })
      if (res.data.success) {
         ElMessage.success('发送成功')
         showCompose.value = false
         // Reset form
         composeForm.subject = ''
         composeForm.content = ''
         composeForm.toUserIds = []
         
         if (activeBox.value === 'outbox') loadMessages()
      } else {
         ElMessage.error('发送失败')
      }
   } catch(e) {
      ElMessage.error('发送出错')
   } finally {
      sending.value = false
   }
}

const saveDraft = () => {
   ElMessage.info('已保存到草稿箱')
   showCompose.value = false
}

onMounted(() => {
   // Pre-load some users for demo
   userOptions.value = [
      { label: '张三', value: 2 },
      { label: '李四', value: 3 },
      { label: 'Admin', value: 1 }
   ]
   loadMessages()
})
</script>

<style scoped>
.h-100 { height: 100%; }
.w-100 { width: 100%; }

.mail-layout {
   background: #fff;
   border-radius: 4px;
   box-shadow: 0 1px 4px rgba(0,0,0,0.1);
   overflow: hidden;
}

.mail-sider {
   border-right: 1px solid #e6e6e6;
   background-color: #f9f9fa;
   display: flex;
   flex-direction: column;
}
.compose-btn-wrapper {
   padding: 20px;
}
.mail-menu {
   border-right: none;
   background: transparent;
   flex: 1;
}
.unread-tag { margin-left: auto; }

.mail-main {
   padding: 0;
   display: flex;
   flex-direction: column;
   background: #fff;
}
.mail-header {
   padding: 16px 24px;
   border-bottom: 1px solid #eee;
   display: flex;
   justify-content: space-between;
   align-items: center;
}
.mail-header h3 { margin: 0; font-size: 18px; color: #333; }
.header-actions { display: flex; gap: 12px; }

.mail-content { flex: 1; padding: 0; overflow: auto; }

.dot {
   width: 8px; height: 8px; background: #f56c6c; border-radius: 50%; display: inline-block;
}
.subject-text { font-weight: 500; color: #303133; }
.preview-text { color: #909399; font-size: 13px; }

:deep(.unread-row) { font-weight: 600; background-color: #fff; }
:deep(.read-row) { color: #606266; background-color: #fafafa; }

/* Reader */
.mail-reader { padding: 20px; }
.reader-meta { background: #f5f7fa; padding: 16px; border-radius: 4px; margin-bottom: 20px; }
.meta-row { margin-bottom: 8px; font-size: 14px; }
.reader-content { white-space: pre-wrap; line-height: 1.6; font-size: 15px; color: #333; }
</style>
