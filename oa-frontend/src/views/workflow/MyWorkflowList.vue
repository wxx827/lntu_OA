<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>我的申请 <span class="subtitle">My Applications</span></h2>
        <p class="desc">查看已提交的流程申请及审批进度</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Plus" @click="$router.push('/dashboard/workflow/apply')">发起新申请</el-button>
      </div>
    </div>

    <!-- Stats Cards -->
    <el-row :gutter="20" class="mb-24">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-blue"><el-icon color="#fff"><Document /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">全部申请</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange"><el-icon color="#fff"><Clock /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.pending }}</div>
            <div class="stat-label">审批中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-green"><el-icon color="#fff"><Check /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.approved }}</div>
            <div class="stat-label">已通过</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-red"><el-icon color="#fff"><Close /></el-icon></div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.rejected }}</div>
            <div class="stat-label">已驳回</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Filter & Table -->
    <el-card shadow="never" class="card-shadow">
      <div class="table-toolbar">
        <el-input v-model="searchText" placeholder="搜索申请标题..." prefix-icon="Search" style="width: 240px" clearable />
        <el-select v-model="filterStatus" placeholder="状态" style="width: 120px; margin-left: 12px" clearable>
          <el-option label="全部" value="" />
          <el-option label="审批中" value="0" />
          <el-option label="已通过" value="1" />
          <el-option label="已驳回" value="2" />
        </el-select>
        <el-select v-model="filterType" placeholder="类型" style="width: 140px; margin-left: 12px" clearable>
          <el-option label="全部类型" value="" />
          <el-option label="请假申请" value="leave" />
          <el-option label="物资采购" value="purchase" />
          <el-option label="费用报销" value="expense" />
          <el-option label="出差申请" value="travel" />
          <el-option label="用车申请" value="car" />
        </el-select>
        <el-button icon="Refresh" style="margin-left: 12px" @click="loadData">刷新</el-button>
      </div>

      <el-table :data="filteredList" style="width: 100%" v-loading="loading" :header-cell-style="{ background: '#f5f7fa' }">
        <el-table-column prop="flowId" label="流程编号" width="150">
          <template #default="scope">
            <span class="mono-font">{{ scope.row.flowId }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="title" label="申请标题" min-width="200" show-overflow-tooltip />
        <el-table-column prop="flowType" label="流程类型" width="120">
          <template #default="scope">
            <el-tag size="small" :type="getTypeTag(scope.row.flowType)">{{ getTypeName(scope.row.flowType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="priority" label="优先级" width="100">
          <template #default="scope">
            <el-tag size="small" :type="getPriorityTag(scope.row.priority)" effect="dark">{{ getPriorityName(scope.row.priority) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="提交时间" width="170" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-tag :type="getStatusTag(scope.row.status)" effect="light" round>{{ getStatusName(scope.row.status) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button link type="primary" @click="viewDetail(scope.row)">详情</el-button>
            <el-button link type="danger" v-if="scope.row.status === 0" @click="handleCancel(scope.row)">撤回</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="pagination-footer">
        <el-pagination background layout="total, prev, pager, next" :total="workflowList.length" />
      </div>
    </el-card>

    <!-- Detail Dialog -->
    <el-dialog v-model="detailVisible" title="申请详情" width="600px">
      <el-descriptions :column="2" border v-if="currentItem">
        <el-descriptions-item label="流程编号">{{ currentItem.flowId }}</el-descriptions-item>
        <el-descriptions-item label="流程类型">{{ getTypeName(currentItem.flowType) }}</el-descriptions-item>
        <el-descriptions-item label="申请标题" :span="2">{{ currentItem.title }}</el-descriptions-item>
        <el-descriptions-item label="优先级">{{ getPriorityName(currentItem.priority) }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="getStatusTag(currentItem.status)">{{ getStatusName(currentItem.status) }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="提交时间">{{ currentItem.createTime }}</el-descriptions-item>
        <el-descriptions-item label="更新时间">{{ currentItem.updateTime }}</el-descriptions-item>
      </el-descriptions>
      <template #footer>
        <el-button @click="detailVisible = false">关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Document, Clock, Check, Close, Search, Refresh, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const searchText = ref('')
const filterStatus = ref('')
const filterType = ref('')
const loading = ref(false)
const detailVisible = ref(false)
const currentItem = ref(null)
const workflowList = ref([])

const stats = computed(() => {
  const list = workflowList.value
  return {
    total: list.length,
    pending: list.filter(i => i.status === 0).length,
    approved: list.filter(i => i.status === 1).length,
    rejected: list.filter(i => i.status === 2).length
  }
})

const filteredList = computed(() => {
  return workflowList.value.filter(item => {
    const matchText = !searchText.value || item.title.includes(searchText.value) || item.flowId.includes(searchText.value)
    const matchStatus = filterStatus.value === '' || item.status === parseInt(filterStatus.value)
    const matchType = !filterType.value || item.flowType === filterType.value
    return matchText && matchStatus && matchType
  })
})

const loadData = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/workflow/my-initiated')
    if (response.data.code === 200) {
      workflowList.value = response.data.data || []
    }
  } catch (error) {
    console.error('加载失败:', error)
    workflowList.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => { loadData() })

const getTypeName = (type) => {
  const map = { leave: '请假申请', purchase: '物资采购', expense: '费用报销', travel: '出差申请', car: '用车申请', seal: '用印申请' }
  return map[type] || type || '通用流程'
}
const getTypeTag = (type) => {
  const map = { leave: 'success', purchase: '', expense: 'warning', travel: 'danger', car: 'info' }
  return map[type] || ''
}
const getPriorityName = (p) => {
  const map = { high: '紧急', medium: '重要', low: '普通' }
  return map[p] || p || '普通'
}
const getPriorityTag = (p) => {
  const map = { high: 'danger', medium: 'warning', low: 'info' }
  return map[p] || 'info'
}
const getStatusName = (s) => {
  const map = { 0: '审批中', 1: '已通过', 2: '已驳回', '-1': '草稿' }
  return map[s] || '未知'
}
const getStatusTag = (s) => {
  const map = { 0: 'warning', 1: 'success', 2: 'danger', '-1': 'info' }
  return map[s] || 'info'
}

const viewDetail = (row) => {
  currentItem.value = row
  detailVisible.value = true
}

const handleCancel = async (row) => {
  try {
    await ElMessageBox.confirm('确定要撤回此申请吗？', '提示', { type: 'warning' })
    await axios.delete(`/api/workflow/draft/${row.flowId}`)
    ElMessage.success('已撤回')
    loadData()
  } catch (e) {
    if (e !== 'cancel') ElMessage.error('撤回失败')
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; }
.desc { color: #606266; font-size: 14px; margin: 0; }
.mb-24 { margin-bottom: 24px; }
.stat-card { border: none; }
.stat-card :deep(.el-card__body) { display: flex; align-items: center; padding: 20px; }
.stat-icon { width: 48px; height: 48px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 16px; font-size: 24px; }
.bg-blue { background: linear-gradient(135deg, #409eff, #79bbff); }
.bg-orange { background: linear-gradient(135deg, #e6a23c, #f3d19e); }
.bg-green { background: linear-gradient(135deg, #67c23a, #95d475); }
.bg-red { background: linear-gradient(135deg, #f56c6c, #fab6b6); }
.stat-value { font-size: 24px; font-weight: 700; color: #303133; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }
.table-toolbar { display: flex; align-items: center; margin-bottom: 20px; }
.mono-font { font-family: monospace; color: #606266; }
.pagination-footer { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
