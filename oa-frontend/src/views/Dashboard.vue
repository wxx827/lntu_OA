<template>
  <div class="app-layout">
    <el-container class="h-100">
        <el-aside width="280px" class="layout-sider">
        <div class="logo-box">
          <img src="https://element-plus.org/images/element-plus-logo.svg" alt="logo" class="logo-img" />
          <span class="logo-text">企业协同办公</span>
        </div>
        
        <el-menu
          :default-active="$route.path"
          class="sider-menu"
          background-color="#ffffff"
          text-color="#303133"
          active-text-color="#409eff"
          router
        >
          <!-- 1. 工作台 -->
          <el-menu-item index="/dashboard">
            <el-icon><Odometer /></el-icon>
            <span>工作台</span>
          </el-menu-item>

          <!-- 2. 考勤管理 -->
          <el-sub-menu index="attendance">
            <template #title>
              <el-icon><Clock /></el-icon>
              <span>考勤管理</span>
            </template>
            <el-menu-item index="/dashboard/attendance/clock">考勤打卡</el-menu-item>
            <el-menu-item index="/dashboard/attendance/records">考勤记录</el-menu-item>
            <el-menu-item index="/dashboard/attendance/leave">请假申请</el-menu-item>
            <el-menu-item index="/dashboard/attendance/overtime">加班申请</el-menu-item>
            <el-menu-item index="/dashboard/attendance/trip">出差申请</el-menu-item>
            <el-menu-item index="/dashboard/attendance/stats">考勤统计</el-menu-item>
          </el-sub-menu>

          <!-- 3. 流程审批 -->
          <el-sub-menu index="workflow">
            <template #title>
              <el-icon><Tickets /></el-icon>
              <span>流程审批</span>
            </template>
            <el-menu-item index="/dashboard/workflow/todo">待办任务</el-menu-item>
            <el-menu-item index="/dashboard/workflow/done">已办历史</el-menu-item>
            <el-menu-item index="/dashboard/workflow/apply">发起申请</el-menu-item>
            <el-menu-item index="/dashboard/workflow/my">我的申请</el-menu-item>
          </el-sub-menu>

          <!-- 4. 会议协作 -->
          <el-sub-menu index="meeting">
            <template #title>
              <el-icon><Calendar /></el-icon>
              <span>会议协作</span>
            </template>
            <el-menu-item index="/dashboard/meeting/rooms">会议室预定</el-menu-item>
            <el-menu-item index="/dashboard/meeting/my">我的会议</el-menu-item>
          </el-sub-menu>

          <!-- 5. 在线消息 -->
          <el-menu-item index="/dashboard/message/email">
            <el-icon><Message /></el-icon>
            <span>在线消息</span>
          </el-menu-item>

          <!-- 6. 云文件 -->
          <el-menu-item index="/dashboard/cloud/drive">
             <el-icon><Folder /></el-icon>
             <span>云文件</span>
          </el-menu-item>

          <!-- 7. 资产物资 -->
          <el-sub-menu index="material">
            <template #title>
              <el-icon><Box /></el-icon>
              <span>资产物资</span>
            </template>
            <el-menu-item index="/dashboard/material/list">库存中心</el-menu-item>
            <el-menu-item index="/dashboard/material/my">申请记录</el-menu-item>
          </el-sub-menu>



          <!-- 7. 行政管理 -->
          <el-sub-menu index="administrative">
            <template #title>
              <el-icon><Van /></el-icon>
              <span>行政管理</span>
            </template>
            <el-menu-item index="/dashboard/administrative/cars">车辆管理</el-menu-item>
          </el-sub-menu>



          <!-- 9. 人力资源 -->
          <el-sub-menu index="hr">
            <template #title>
              <el-icon><User /></el-icon>
              <span>人力资源</span>
            </template>
            <el-menu-item index="/dashboard/hr/departments">部门管理</el-menu-item>
            <el-menu-item index="/dashboard/hr/org-chart">组织架构</el-menu-item>
            <el-menu-item index="/dashboard/hr/employees">员工档案</el-menu-item>
          </el-sub-menu>

          <!-- 10. 财务管理 -->
          <el-sub-menu index="finance">
            <template #title>
              <el-icon><Money /></el-icon>
              <span>财务管理</span>
            </template>
            <el-menu-item index="/dashboard/finance/claim">报销申请</el-menu-item>
            <el-menu-item index="/dashboard/finance/my-expenses">我的报销</el-menu-item>
          </el-sub-menu>

          <!-- 11. 通讯录 -->
          <el-menu-item index="/dashboard/contacts">
            <el-icon><UserFilled /></el-icon>
            <span>通讯录</span>
          </el-menu-item>

          <!-- 12. 系统设置 -->
          <el-sub-menu index="system">
            <template #title>
              <el-icon><Setting /></el-icon>
              <span>系统设置</span>
            </template>
            <el-menu-item index="/dashboard/system/employees">员工管理</el-menu-item>
            <el-menu-item index="/dashboard/system/roles">权限配置</el-menu-item>
            <el-menu-item index="/dashboard/system/announcements">公告管理</el-menu-item>
          </el-sub-menu>

          <!-- 13. 数据报表 -->
          <el-menu-item index="/dashboard/report/dashboard">
            <el-icon><DataAnalysis /></el-icon>
            <span>数据报表</span>
          </el-menu-item>
        </el-menu>
      </el-aside>
      
      <el-container>
        <!-- Header -->
        <el-header class="layout-header">
          <div class="header-left">
            <el-breadcrumb separator="/">
              <el-breadcrumb-item :to="{ path: '/dashboard' }">首页</el-breadcrumb-item>
              <el-breadcrumb-item>当前页面</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          
          <div class="header-right">
            <el-tooltip content="消息通知">
              <div class="action-item" @click="$router.push('/dashboard/notifications')">
                <el-badge :value="5" class="item">
                  <el-icon :size="20"><Bell /></el-icon>
                </el-badge>
              </div>
            </el-tooltip>
            
            <el-dropdown trigger="click">
              <div class="action-item user-action">
                <el-avatar :size="32" :src="`https://ui-avatars.com/api/?name=${currentUser.username}&background=409eff&color=fff`" />
                <span class="username">{{ currentUser.username }}</span>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/dashboard/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </el-header>
        
        <!-- Main Content -->
        <el-main class="layout-main">
          <router-view v-if="$route.path !== '/dashboard'" v-slot="{ Component }">
            <component :is="Component" class="fade-in" />
          </router-view>
          
          <!-- Home Dashboard -->
          <div v-else class="dashboard-home fade-in">
            <!-- 1. Welcome Banner -->
            <div class="welcome-banner card-shadow mb-24">
              <div class="banner-left">
                <h2>早安，{{ currentUser.username }}，祝你开心每一天！</h2>
                <p class="weather">今天是个好天气，适合推进重要项目。</p>
              </div>
              <div class="banner-right">
                <div class="stat-box">
                  <div class="num">{{ statistics.pendingTasks }}</div>
                  <div class="txt">待办事项</div>
                </div>
                <div class="stat-box">
                  <div class="num">{{ statistics.teamPresent }}/{{ statistics.teamTotal }}</div>
                  <div class="txt">团队在岗</div>
                </div>
                <div class="stat-box">
                  <div class="num">{{ statistics.systemVisits?.toLocaleString() }}</div>
                  <div class="txt">项目访问</div>
                </div>
              </div>
            </div>

            <el-row :gutter="24">
              <!-- Left Column (Main) -->
              <el-col :span="16">
                <!-- Project Progress Cards -->
                <el-row :gutter="16" class="mb-24">
                  <el-col :span="8" v-for="proj in projects" :key="proj.name">
                    <el-card shadow="hover" class="project-card" :body-style="{ padding: '16px' }">
                      <div class="card-header-flex">
                        <el-icon :size="24" :class="proj.iconClass"><component :is="proj.icon" /></el-icon>
                        <el-tag size="small" :type="proj.statusType">{{ proj.status }}</el-tag>
                      </div>
                      <h4>{{ proj.name }}</h4>
                      <p class="card-desc">{{ proj.desc }}</p>
                      <div class="progress-bar">
                        <span>进度</span>
                        <el-progress :percentage="proj.progress" :color="proj.color" :stroke-width="6" />
                      </div>
                      <div class="card-footer">
                        <el-avatar-group size="small">
                          <el-avatar v-for="(user, i) in proj.users" :key="i" :src="user" />
                        </el-avatar-group>
                        <span class="date">{{ proj.date }}</span>
                      </div>
                    </el-card>
                  </el-col>
                </el-row>

                <!-- Recent News / Dynamic -->
                <el-card class="card-shadow">
                  <template #header>
                    <div class="card-header-row">
                      <span>最新动态</span>
                      <el-button type="primary" size="small" icon="Plus" @click="openActivityDialog">发布动态</el-button>
                    </div>
                  </template>
                  <el-timeline>
                    <el-timeline-item 
                      v-for="(activity, index) in activities.slice(0, 3)" 
                      :key="index"
                      :timestamp="activity.timestamp" 
                      placement="top" 
                      :type="activity.type">
                      <el-card shadow="never" class="news-card">
                        <h4>{{ activity.title }}</h4>
                        <p>{{ activity.description }}</p>
                      </el-card>
                    </el-timeline-item>
                  </el-timeline>
                  <el-empty v-if="activities.length === 0" description="暂无动态" :image-size="60" />
                </el-card>
              </el-col>

              <!-- Right Column (Side) -->
              <el-col :span="8">
                <!-- Accessibility / Quick Nav -->
                <el-card class="card-shadow mb-24" header="快捷导航">
                  <div class="quick-nav-grid">
                    <div class="nav-item" @click="$router.push('/dashboard/workflow/apply')">
                      <span class="nav-icon" style="color: #409eff"><el-icon><EditPen /></el-icon></span>
                      <span class="nav-text">发起审批</span>
                    </div>
                    <div class="nav-item" @click="$router.push('/dashboard/meeting/rooms')">
                      <span class="nav-icon" style="color: #67c23a"><el-icon><Calendar /></el-icon></span>
                      <span class="nav-text">预定会议</span>
                    </div>

                    <div class="nav-item" @click="$router.push('/dashboard/system/employees')">
                      <span class="nav-icon" style="color: #f56c6c"><el-icon><User /></el-icon></span>
                      <span class="nav-text">员工录入</span>
                    </div>
                    <div class="nav-item" @click="$router.push('/dashboard/report/dashboard')">
                      <span class="nav-icon" style="color: #722ed1"><el-icon><DataAnalysis /></el-icon></span>
                      <span class="nav-text">数据报表</span>
                    </div>
                    <div class="nav-item">
                      <span class="nav-icon" style="color: #13c2c2"><el-icon><Setting /></el-icon></span>
                      <span class="nav-text">个性设置</span>
                    </div>
                  </div>
                </el-card>

                <!-- Announcement / Notice -->
                <el-card class="card-shadow mb-24">
                  <template #header>
                    <div class="card-header-row">
                      <span>公告通知</span>
                      <el-button type="primary" size="small" icon="Plus" @click="openAnnouncementDialog">发布</el-button>
                    </div>
                  </template>
                  <div class="notice-list">
                    <div class="notice-item" v-for="ann in displayedAnnouncements" :key="ann.id" @click="viewAnnouncement(ann)">
                      <el-tag size="small" effect="plain" :type="ann.type === 'important' ? 'danger' : ann.type === 'urgent' ? 'warning' : 'info'">
                        {{ ann.type === 'important' ? '重要' : ann.type === 'urgent' ? '紧急' : '通知' }}
                      </el-tag>
                      <span class="notice-text">{{ ann.title }}</span>
                    </div>
                    <el-empty v-if="announcements.length === 0" description="暂无公告" :image-size="60" />
                  </div>
                  <div class="more-btn" v-if="announcements.length > 3">
                    <el-button link type="primary" @click="showAllAnnouncements = !showAllAnnouncements">
                      {{ showAllAnnouncements ? '收起' : `查看更多 (${announcements.length - 3})` }}
                    </el-button>
                  </div>
                </el-card>

                <!-- Team Members -->
                <el-card class="card-shadow" header="团队成员 (在线)">
                  <div class="team-grid">
                    <div class="team-member" v-for="u in team" :key="u.name">
                      <div class="avatar-wrapper">
                         <el-avatar :src="u.avatar" />
                         <span class="status-dot" :class="u.status"></span>
                      </div>
                      <span class="member-name">{{ u.name }}</span>
                    </div>
                  </div>
                </el-card>
              </el-col>
            </el-row>
          </div>
        </el-main>
      </el-container>
    </el-container>
    
    <!-- 发布动态对话框 -->
    <el-dialog v-model="activityDialogVisible" title="发布动态" width="500px">
      <el-form :model="activityForm" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="activityForm.title" placeholder="请输入动态标题" />
        </el-form-item>
        <el-form-item label="类型">
          <el-select v-model="activityForm.type" style="width: 100%">
            <el-option label="普通" value="primary" />
            <el-option label="成功" value="success" />
            <el-option label="警告" value="warning" />
            <el-option label="重要" value="danger" />
          </el-select>
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="activityForm.description" type="textarea" :rows="4" placeholder="请输入动态内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="activityDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitActivity" :loading="submitting">发布</el-button>
      </template>
    </el-dialog>

    <!-- 发布公告对话框 -->
    <el-dialog v-model="announcementDialogVisible" title="发布公告" width="600px">
      <el-form :model="announcementForm" label-width="80px">
        <el-form-item label="标题" required>
          <el-input v-model="announcementForm.title" placeholder="请输入公告标题" maxlength="100" show-word-limit />
        </el-form-item>
        <el-form-item label="类型">
          <el-radio-group v-model="announcementForm.type">
            <el-radio label="normal">普通</el-radio>
            <el-radio label="important">重要</el-radio>
            <el-radio label="urgent">紧急</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="内容" required>
          <el-input v-model="announcementForm.content" type="textarea" :rows="6" placeholder="请输入公告内容" maxlength="2000" show-word-limit />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="announcementDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitAnnouncement" :loading="submitting">发布</el-button>
      </template>
    </el-dialog>

    <!-- 查看公告详情 -->
    <el-dialog v-model="viewAnnouncementVisible" title="公告详情" width="550px">
      <div v-if="currentAnnouncement" class="announcement-detail">
        <h3>{{ currentAnnouncement.title }}</h3>
        <div class="ann-meta">
          <el-tag size="small" :type="currentAnnouncement.type === 'important' ? 'danger' : 'info'">
            {{ currentAnnouncement.type === 'important' ? '重要' : '通知' }}
          </el-tag>
          <span>{{ currentAnnouncement.publisher }} · {{ currentAnnouncement.publishTime }}</span>
        </div>
        <div class="ann-content">{{ currentAnnouncement.content }}</div>
      </div>
    </el-dialog>
    
    <AIChatBot />
  </div>
</template>

<script setup>
import { 
  Odometer, Calendar, Box, Tickets, UserFilled, Setting, Bell, 
  Top, Bottom, EditPen, Notebook, DataAnalysis, User, Platform, Van, Clock, Money, Message, Folder, Plus
} from '@element-plus/icons-vue'
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import AIChatBot from '../components/AIChatBot.vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()

// Current user info from localStorage
const currentUser = ref({
  username: localStorage.getItem('username') || 'User',
  role: localStorage.getItem('role') || 'Employee'
})

// Dashboard statistics
const statistics = ref({
  pendingTasks: 0,
  teamPresent: 0,
  teamTotal: 0,
  systemVisits: 0
})

// Activities/News
const activities = ref([])

// Announcements
const announcements = ref([])

// Team members
const team = ref([])

// Projects
const projects = ref([])

const iconMap = { Platform, Box, DataAnalysis }

// Dialog states
const activityDialogVisible = ref(false)
const announcementDialogVisible = ref(false)
const viewAnnouncementVisible = ref(false)
const submitting = ref(false)
const currentAnnouncement = ref(null)
const showAllAnnouncements = ref(false)

// Computed - 显示的公告列表
const displayedAnnouncements = computed(() => {
  return showAllAnnouncements.value ? announcements.value : announcements.value.slice(0, 3)
})

// Form data
const activityForm = reactive({
  title: '',
  description: '',
  type: 'primary'
})

const announcementForm = reactive({
  title: '',
  content: '',
  type: 'normal'
})

// Fetch dashboard statistics
const loadStatistics = async () => {
  try {
    const response = await axios.get('/api/dashboard/statistics')
    if (response.data.success) {
      statistics.value = response.data.data
    }
  } catch (error) {
    console.error('Failed to load dashboard statistics:', error)
  }
}

// Fetch recent activities
const loadActivities = async () => {
  try {
    const response = await axios.get('/api/dashboard/activities')
    if (response.data.success) {
      activities.value = response.data.data
    }
  } catch (error) {
    console.error('Failed to load activities:', error)
  }
}

// Fetch announcements
const loadAnnouncements = async () => {
  try {
    const response = await axios.get('/api/dashboard/announcements')
    if (response.data.success) {
      announcements.value = response.data.data
    }
  } catch (error) {
    console.error('Failed to load announcements:', error)
  }
}

// Fetch project progress
const loadProjects = async () => {
  try {
    const response = await axios.get('/api/dashboard/projects')
    if (response.data.success) {
      projects.value = response.data.data.map(item => ({
        ...item,
        icon: iconMap[item.icon] || Platform
      }))
    }
  } catch (error) {
    console.error('Failed to load projects:', error)
    ElMessage.error('加载项目失败')
  }
}

// Fetch team status
const loadTeamStatus = async () => {
  try {
    const response = await axios.get('/api/dashboard/team-status')
    if (response.data.success) {
      team.value = response.data.data
    }
  } catch (error) {
    console.error('Failed to load team status:', error)
    ElMessage.error('加载团队状态失败')
  }
}

const handleLogout = () => {
  router.push('/login')
}

// Dialog methods
const openActivityDialog = () => {
  activityForm.title = ''
  activityForm.description = ''
  activityForm.type = 'primary'
  activityDialogVisible.value = true
}

const openAnnouncementDialog = () => {
  announcementForm.title = ''
  announcementForm.content = ''
  announcementForm.type = 'normal'
  announcementDialogVisible.value = true
}

const viewAnnouncement = (ann) => {
  currentAnnouncement.value = ann
  viewAnnouncementVisible.value = true
}

const submitActivity = async () => {
  if (!activityForm.title || !activityForm.description) {
    ElMessage.warning('请填写完整信息')
    return
  }
  submitting.value = true
  try {
    const response = await axios.post('/api/dashboard/activity', {
      title: activityForm.title,
      description: activityForm.description,
      type: activityForm.type,
      userName: currentUser.value.username
    })
    if (response.data.success) {
      ElMessage.success('动态发布成功')
      activityDialogVisible.value = false
      loadActivities()
    } else {
      throw new Error(response.data.message)
    }
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const submitAnnouncement = async () => {
  if (!announcementForm.title || !announcementForm.content) {
    ElMessage.warning('请填写完整信息')
    return
  }
  submitting.value = true
  try {
    const response = await axios.post('/api/announcement/save', {
      title: announcementForm.title,
      content: announcementForm.content,
      type: announcementForm.type,
      status: 1,
      publisherName: currentUser.value.username
    })
    if (response.data.success) {
      ElMessage.success('公告发布成功')
      announcementDialogVisible.value = false
      loadAnnouncements()
    } else {
      throw new Error(response.data.message)
    }
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadStatistics()
  loadActivities()
  loadAnnouncements()
  loadTeamStatus()
  loadProjects()
})
</script>

<style scoped>
.h-100 { height: 100vh; }
.layout-sider {
  background-color: #fff;
  color: #303133;
  display: flex;
  flex-direction: column;
  box-shadow: 2px 0 8px 0 rgba(29,35,41,0.05);
  z-index: 10;
}
.logo-box {
  height: 64px;
  display: flex;
  align-items: center;
  padding-left: 20px;
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
}
.logo-img { height: 32px; margin-right: 12px; }
.logo-text { font-size: 18px; font-weight: 600; white-space: nowrap; color: #303133; }

.sider-menu { border-right: none; }
:deep(.el-menu-item.is-active) {
  background-color: #e6f7ff !important;
  border-right: 3px solid #1890ff;
}

.layout-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0,21,41,0.08);
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 24px;
  z-index: 10;
}

.header-right { display: flex; align-items: center; gap: 24px; }
.action-item { cursor: pointer; display: flex; align-items: center; gap: 8px; color: #5a5e66; transition: color 0.3s; }
.action-item:hover { color: #409eff; }
.username { font-weight: 500; }

.layout-main {
  background-color: #f0f2f5;
  padding: 16px;
  overflow-x: hidden;
}

/* Dashboard Home Styles */
.welcome-banner {
  background: #fff;
  padding: 24px 32px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-radius: 4px;
}
.banner-left h2 { margin: 0 0 8px 0; font-size: 20px; color: #303133; }
.weather { color: #909399; font-size: 14px; margin: 0; }
.banner-right { display: flex; gap: 48px; }
.stat-box { text-align: right; }
.stat-box .num { font-size: 24px; font-weight: 600; color: #303133; }
.stat-box .txt { font-size: 12px; color: #909399; margin-top: 4px; }

/* Project Cards */
.project-card { height: 100%; transition: all 0.3s; }
.project-card:hover { transform: translateY(-5px); box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.card-header-flex { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 12px; }
.project-card h4 { margin: 0 0 8px 0; font-size: 16px; color: #303133; }
.card-desc { font-size: 12px; color: #909399; margin-bottom: 24px; height: 36px; overflow: hidden; line-height: 1.5; }
.progress-bar { display: flex; align-items: center; gap: 8px; font-size: 12px; color: #606266; margin-bottom: 16px; }
.progress-bar .el-progress { flex: 1; }
.card-footer { display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #f5f7fa; padding-top: 12px; }
.date { font-size: 12px; color: #c0c4cc; }

.text-blue { color: #409eff; }
.text-orange { color: #e6a23c; }
.text-red { color: #f56c6c; }

/* Quick Nav */
.quick-nav-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 16px; padding: 8px; }
.nav-item { 
  display: flex; flex-direction: column; align-items: center; justify-content: center; 
  padding: 16px; cursor: pointer; border-radius: 4px; transition: background 0.2s; 
}
.nav-item:hover { background: #f5f7fa; }
.nav-icon { font-size: 28px; margin-bottom: 8px; }
.nav-text { font-size: 12px; color: #606266; }

/* Notice */
.card-header-row { display: flex; justify-content: space-between; align-items: center; }
.notice-list { display: flex; flex-direction: column; gap: 12px; }
.notice-item { display: flex; align-items: center; gap: 8px; }
.notice-text { font-size: 13px; color: #606266; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

/* Team */
.team-grid { display: flex; flex-wrap: wrap; gap: 16px; }
.team-member { text-align: center; width: 60px; }
.avatar-wrapper { position: relative; display: inline-block; margin-bottom: 4px; }
.status-dot {
  position: absolute; bottom: 0; right: 0; width: 10px; height: 10px; border-radius: 50%; border: 2px solid #fff;
}
.status-dot.online { background: #67c23a; }
.status-dot.busy { background: #f56c6c; }
.status-dot.offline { background: #909399; }
.member-name { font-size: 12px; color: #606266; display: block; }

/* News */
.news-card { background: #f9fafc; border: none; cursor: pointer; transition: background 0.2s; }
.news-card:hover { background: #f0f2f5; }
.news-card h4 { margin: 0 0 4px 0; font-size: 14px; color: #303133; }
.news-card p { margin: 0; font-size: 12px; color: #909399; }

.echart-container { width: 100%; height: 280px; }
.mb-24 { margin-bottom: 24px; }

/* Announcement Detail */
.announcement-detail h3 { margin: 0 0 12px 0; font-size: 18px; color: #303133; }
.ann-meta { display: flex; align-items: center; gap: 12px; font-size: 13px; color: #909399; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid #ebeef5; }
.ann-content { line-height: 1.8; color: #606266; white-space: pre-wrap; }

/* Notice item hover */
.notice-item { cursor: pointer; padding: 6px 0; border-radius: 4px; transition: background 0.2s; }
.notice-item:hover { background: #f5f7fa; }

/* More button */
.more-btn { text-align: center; margin-top: 12px; padding-top: 12px; border-top: 1px dashed #ebeef5; }
</style>
