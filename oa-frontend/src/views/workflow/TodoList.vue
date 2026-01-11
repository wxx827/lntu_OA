<template>
  <div class="fade-in">
    <!-- Header Section -->
    <div class="page-header">
      <div class="header-left">
        <h2>待办任务 <span class="subtitle">My Pending Tasks</span></h2>
        <p class="desc">高效处理您的待办事项,提升工作效率</p>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus">发起新流程</el-button>
      </div>
    </div>

    <!-- Stats Cards -->
    <el-row :gutter="16" class="stats-row">
      <el-col :span="6" v-for="stat in stats" :key="stat.label">
        <div class="stat-card card-shadow">
          <div class="stat-icon" :style="{ background: stat.color }">
            <el-icon :size="28" color="#fff"><component :is="stat.icon" /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value">{{ stat.value }}</div>
            <div class="stat-label">{{ stat.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Filter Bar -->
    <el-card shadow="never" class="filter-card">
      <el-form :inline="true">
        <el-form-item label="关键词">
          <el-input v-model="searchText" placeholder="搜索任务标题" clearable style="width: 200px">
            <template #prefix><el-icon><Search /></el-icon></template>
          </el-input>
        </el-form-item>
        <el-form-item label="优先级">
          <el-select v-model="filterPriority" placeholder="全部" clearable style="width: 120px">
            <el-option label="紧急" value="urgent" />
            <el-option label="重要" value="high" />
            <el-option label="普通" value="normal" />
          </el-select>
        </el-form-item>
        <el-form-item label="流程类型">
          <el-select v-model="filterType" placeholder="全部" clearable style="width: 140px">
            <el-option label="请假审批" value="leave" />
            <el-option label="采购审批" value="purchase" />
            <el-option label="报销审批" value="expense" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" :icon="Search">查询</el-button>
          <el-button :icon="Refresh">重置</el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <!-- Task Table -->
    <el-card shadow="never" class="table-card card-shadow">
      <el-table :data="tasks" style="width: 100%" stripe>
        <el-table-column type="selection" width="55" />
        <el-table-column prop="id" label="流程编号" width="140">
          <template #default="scope">
            <el-tag effect="plain" size="small">{{ scope.row.id }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="任务标题" min-width="250">
          <template #default="scope">
            <div class="task-title-cell">
              <el-icon :size="16" :color="getPriorityColor(scope.row.priority)">
                <component :is="getPriorityIcon(scope.row.priority)" />
              </el-icon>
              <span>{{ scope.row.title }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="initiator" label="发起人" width="120">
          <template #default="scope">
            <div class="user-cell">
              <el-avatar :size="28" :src="scope.row.avatar" />
              <span>{{ scope.row.initiator }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="type" label="流程类型" width="120">
          <template #default="scope">
            <el-tag :type="getTypeTagType(scope.row.type)" size="small">{{ scope.row.type }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="接收时间" width="180" />
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="scope">
            <el-tag :type="getPriorityTagType(scope.row.priority)" effect="dark" size="small">
              {{ scope.row.priority }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="scope">
            <el-button link type="primary" :icon="View" @click="handleView(scope.row)">详情</el-button>
            <el-button link type="success" :icon="Check" @click="handleApprove(scope.row)">通过</el-button>
            <el-button link type="danger" :icon="Close" @click="handleReject(scope.row)">驳回</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-footer">
        <el-pagination
          background
          layout="total, sizes, prev, pager, next, jumper"
          :total="100"
          :page-sizes="[10, 20, 50, 100]"
        />
      </div>
    </el-card>

    <!-- Approval Dialog -->
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" center>
      <el-form :model="approvalForm" label-width="80px">
        <el-form-item label="审批意见">
          <el-input v-model="approvalForm.comment" type="textarea" :rows="4" placeholder="请输入审批意见..." />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitApproval">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Search, Refresh, Plus, View, Check, Close, Warning, InfoFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const searchText = ref('')
const filterPriority = ref('')
const filterType = ref('')
const dialogVisible = ref(false)
const dialogTitle = ref('审批')
const approvalForm = reactive({ comment: '' })
const currentTask = ref(null)
const approvalAction = ref('pass')
const loading = ref(false)

const stats = ref([
  { label: '待处理', value: 0, color: '#409eff', icon: 'List' },
  { label: '紧急', value: 0, color: '#f56c6c', icon: 'Warning' },
  { label: '今日新增', value: 0, color: '#67c23a', icon: 'Plus' },
  { label: '即将超时', value: 0, color: '#e6a23c', icon: 'Timer' },
])

const tasks = ref([])

// 加载待办任务
const loadTasks = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/workflow/my-tasks', {
      params: { keyword: searchText.value || undefined, priority: filterPriority.value || undefined }
    })
    if (response.data.code === 200) {
      tasks.value = response.data.data.map(item => ({
        id: item.flowId || 'WF' + Date.now(),
        title: item.title,
        initiator: item.initiatorName || '未知',
        avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(item.initiatorName || 'U')}&background=409eff&color=fff`,
        type: item.flowType || '通用流程',
        createTime: item.createTime || new Date().toLocaleString(),
        priority: mapPriority(item.priority)
      }))
      // 更新统计
      stats.value[0].value = tasks.value.length
      stats.value[1].value = tasks.value.filter(t => t.priority === '紧急').length
    }
  } catch (error) {
    console.error('加载待办任务失败:', error)
    tasks.value = []
    stats.value[0].value = 0
  } finally {
    loading.value = false
  }
}

const mapPriority = (p) => {
  const map = { 'high': '紧急', 'medium': '重要', 'low': '普通' }
  return map[p] || p || '普通'
}

onMounted(() => {
  loadTasks()
})

// Helper Methods
const getPriorityColor = (p) => {
  if (p === '紧急') return '#f56c6c'
  if (p === '重要') return '#e6a23c'
  return '#909399'
}

const getTypeTagType = (t) => {
  if (t.includes('采购')) return 'success'
  if (t.includes('请假')) return 'warning'
  return 'primary'
}

const getPriorityTagType = (p) => {
  if (p === '紧急') return 'danger'
  if (p === '重要') return 'warning'
  return 'info'
}

const getPriorityIcon = (p) => p === '紧急' ? Warning : InfoFilled

// Actions
const handleView = (row) => {
  ElMessage.info(`查看任务详情: ${row.title}`)
}

const handleApprove = (row) => {
  currentTask.value = row
  approvalAction.value = 'pass'
  dialogTitle.value = '审批通过'
  approvalForm.comment = '同意'
  dialogVisible.value = true
}

const handleReject = (row) => {
  currentTask.value = row
  approvalAction.value = 'reject'
  dialogTitle.value = '驳回审批'
  approvalForm.comment = ''
  dialogVisible.value = true
}

const submitApproval = async () => {
  try {
    await axios.post(`/api/workflow/approve/${currentTask.value.id}`, {
      action: approvalAction.value,
      comment: approvalForm.comment
    })
    ElMessage.success(approvalAction.value === 'pass' ? '审批已通过' : '已驳回')
    dialogVisible.value = false
    loadTasks()
  } catch (error) {
    console.error('审批失败:', error)
    ElMessage.error('审批提交失败，请检查网络连接')
  }
}
</script>

<style scoped>
.mt-24 { margin-top: 24px; }
.ml-2 { margin-left: 8px; }
.user-info { display: flex; align-items: center; }
.pagination-box { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
