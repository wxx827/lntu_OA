<template>
  <div class="ai-chatbot">
    <!-- Chat Window -->
    <transition name="slide-up">
      <div v-if="isOpen" class="chat-window">
        <!-- Header -->
        <div class="chat-header">
          <div class="header-info">
            <el-avatar :size="32" src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" class="ai-avatar" />
            <div>
              <h3>OA 智能助手</h3>
              <span class="status">在线 | Online</span>
            </div>
          </div>
          <el-icon class="close-btn" @click="isOpen = false"><Close /></el-icon>
        </div>

        <!-- Messages Area -->
        <div class="messages" ref="messagesRef">
          <div v-for="(msg, index) in messages" :key="index" class="message-row" :class="msg.role">
            <div class="message-bubble">
              <p>{{ msg.text }}</p>
              <div v-if="msg.actions" class="actions-list">
                <el-button 
                  v-for="action in msg.actions" 
                  :key="action.label" 
                  size="small" 
                  round 
                  class="action-btn"
                  @click="handleAction(action.handler)"
                >
                  {{ action.label }}
                </el-button>
              </div>
            </div>
          </div>
          <div v-if="isTyping" class="message-row ai">
            <div class="message-bubble typing">
              <span></span><span></span><span></span>
            </div>
          </div>
        </div>

        <!-- Input Area -->
        <div class="input-area">
          <el-input
            v-model="inputText"
            placeholder="输入您的问题..."
            @keyup.enter="sendMessage"
          >
            <template #append>
              <el-button @click="sendMessage">
                <el-icon><Position /></el-icon>
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </transition>

    <!-- Floating Button -->
    <div class="floating-btn" @click="toggleChat" :class="{ 'hide': isOpen }">
      <div class="pulse-ring"></div>
      <el-icon :size="24"><Service /></el-icon>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick } from 'vue'
import { Close, Position, Service } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const isOpen = ref(false)
const inputText = ref('')
const isTyping = ref(false)
const messagesRef = ref(null)

const messages = ref([
  { 
    role: 'ai', 
    text: '您好！我是您的智能办公助手。您可以让我帮您预定会议室、查询库存或分析报表。',
    actions: [
      { label: '我要订会议室', handler: 'bookMeeting' },
      { label: '查看物资库存', handler: 'checkStock' }
    ]
  }
])

const toggleChat = () => {
  isOpen.value = !isOpen
}

const scrollToBottom = () => {
  nextTick(() => {
    if (messagesRef.value) {
      messagesRef.value.scrollTop = messagesRef.value.scrollHeight
    }
  })
}

const handleAction = (handler) => {
  if (handler === 'bookMeeting') {
    sendMessage('我要预定会议室')
  } else if (handler === 'checkStock') {
    sendMessage('现在的物资库存怎么样？')
  }
}

const sendMessage = (textPayload) => {
  const text = typeof textPayload === 'string' ? textPayload : inputText.value
  if (!text.trim()) return

  // User Message
  messages.value.push({ role: 'user', text })
  inputText.value = ''
  scrollToBottom()

  // AI Response Simulation
  isTyping.value = true
  setTimeout(() => {
    isTyping.value = false
    let responseText = ''
    let actions = []

    if (text.includes('预定') || text.includes('会议')) {
      responseText = '收到。已为您自动筛选出 3 个可用会议室。根据您的团队规模(10人)，推荐使用 [301 会议室]。是否立即跳转？'
      actions = [{ label: '前往预定', handler: 'goBook' }]
    } else if (text.includes('库存') || text.includes('物资')) {
      responseText = '根据 AI 预测，[黑色签字笔] 将在 3 天后耗尽。建议您尽快提交采购申请。'
      actions = [{ label: '查看库存详情', handler: 'goMaterial' }]
    } else {
      responseText = '抱歉，我还在学习中。您能问我一些关于 OA 系统的问题吗？'
    }

    messages.value.push({ role: 'ai', text: responseText, actions })
    scrollToBottom()

    // Handle Auto Navigation Mock
    if (actions.length > 0 && actions[0].handler === 'goBook') {
      setTimeout(() => router.push('/dashboard/meeting/rooms'), 1500)
    } else if (actions.length > 0 && actions[0].handler === 'goMaterial') {
      setTimeout(() => router.push('/dashboard/material/list'), 1500)
    }
  }, 1200)
}
</script>

<style scoped>
.ai-chatbot {
  position: fixed;
  bottom: 30px;
  right: 30px;
  z-index: 9999;
  font-family: 'Inter', sans-serif;
}

/* Floating Button */
.floating-btn {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  background: linear-gradient(135deg, #6366f1, #a855f7);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  cursor: pointer;
  box-shadow: 0 4px 15px rgba(99, 102, 241, 0.4);
  transition: transform 0.3s cubic-bezier(0.175, 0.885, 0.32, 1.275);
  position: relative;
}
.floating-btn:hover {
  transform: scale(1.1);
}
.floating-btn.hide {
  transform: scale(0);
  opacity: 0;
  pointer-events: none;
}

.pulse-ring {
  position: absolute;
  width: 100%;
  height: 100%;
  border-radius: 50%;
  border: 2px solid #a855f7;
  animation: pulse 2s infinite;
}
@keyframes pulse {
  0% { transform: scale(1); opacity: 0.8; }
  100% { transform: scale(1.5); opacity: 0; }
}

/* Chat Window */
.chat-window {
  width: 350px;
  height: 500px;
  background: #fff;
  border-radius: 8px;
  border: 1px solid #ebeef5;
  box-shadow: 0 4px 20px rgba(0,0,0,0.15);
  display: flex;
  flex-direction: column;
  overflow: hidden;
  position: absolute;
  bottom: 0;
  right: 0;
  transform-origin: bottom right;
}

/* Header */
.chat-header {
  background: #fafafa;
  padding: 16px;
  border-bottom: 1px solid #ebeef5;
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-info {
  display: flex;
  align-items: center;
  gap: 12px;
}
.header-info h3 {
  margin: 0;
  font-size: 16px;
  color: #303133;
}
.status {
  font-size: 12px;
  color: #67C23A;
}
.close-btn {
  color: #909399;
  cursor: pointer;
  transition: color 0.2s;
}
.close-btn:hover { color: #303133; }

/* Messages */
.messages {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  background: #fff;
}
.message-row {
  display: flex;
  margin-bottom: 16px;
}
.message-row.user { justify-content: flex-end; }
.message-row.ai { justify-content: flex-start; }

.message-bubble {
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 8px;
  font-size: 14px;
  line-height: 1.5;
  position: relative;
  box-shadow: 0 1px 2px rgba(0,0,0,0.05);
}
.ai .message-bubble {
  background: #f4f4f5;
  color: #606266;
  border-bottom-left-radius: 2px;
}
.user .message-bubble {
  background: #409eff;
  color: #fff;
  border-bottom-right-radius: 2px;
}

/* Typing Animation */
.typing span {
  display: inline-block;
  width: 6px;
  height: 6px;
  background: #909399;
  border-radius: 50%;
  margin: 0 2px;
  animation: bounce 1.4s infinite ease-in-out;
}
.typing span:nth-child(1) { animation-delay: -0.32s; }
.typing span:nth-child(2) { animation-delay: -0.16s; }
@keyframes bounce {
  0%, 80%, 100% { transform: scale(0); }
  40% { transform: scale(1); }
}

/* Actions in Chat */
.actions-list {
  margin-top: 10px;
  display: flex;
  gap: 8px;
  flex-wrap: wrap;
}
.action-btn {
  background: #fff;
  border: 1px solid #c6e2ff;
  color: #409eff;
}
.action-btn:hover {
  background: #ecf5ff;
  color: #409eff;
  border-color: #b3d8ff;
}

/* Input Area */
.input-area {
  padding: 16px;
  border-top: 1px solid #ebeef5;
  background: #fff;
}
:deep(.el-input__wrapper) {
  background: #f5f7fa;
  box-shadow: none;
  border: 1px solid transparent;
}
:deep(.el-input-group__append) {
  background: #f5f7fa;
  border: none;
  color: #606266;
}

/* Transition */
.slide-up-enter-active, .slide-up-leave-active {
  transition: all 0.3s cubic-bezier(0.175, 0.885, 0.32, 1);
}
.slide-up-enter-from, .slide-up-leave-to {
  opacity: 0;
  transform: translateY(20px) scale(0.95);
}
</style>
