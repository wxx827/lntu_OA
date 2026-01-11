<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>我的领用 <span class="subtitle">My Applications</span></h2>
        <p class="desc">物资领用申请进度追踪与记录</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Plus" @click="handleApply">申请领用</el-button>
      </div>
    </div>

    <!-- 1. Stats Cards -->
    <el-row :gutter="20" class="mb-24">
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-blue">
            <el-icon color="#fff"><Box /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">24</div>
            <div class="stat-label">本年度领用 (件)</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange">
            <el-icon color="#fff"><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">2</div>
            <div class="stat-label">审批中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="8">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-red">
            <el-icon color="#fff"><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">0</div>
            <div class="stat-label">已驳回</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. Main Content -->
    <el-card shadow="never" class="card-shadow">
      <!-- Toolbar -->
      <div class="table-toolbar">
        <el-input 
          v-model="searchText" 
          placeholder="搜索物资名称..." 
          prefix-icon="Search"
          style="width: 240px" 
          clearable
        />
        <el-select v-model="filterStatus" placeholder="状态" style="width: 140px; margin-left: 12px" clearable>
          <el-option label="全部状态" value="" />
          <el-option label="审批中" value="Pending" />
          <el-option label="已通过" value="Approved" />
          <el-option label="已驳回" value="Rejected" />
        </el-select>
        <el-date-picker
          v-model="dateRange"
          type="daterange"
          range-separator="至"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
          style="width: 240px; margin-left: 12px"
        />
        <div class="ml-auto">
          <el-button icon="Refresh" circle />
        </div>
      </div>

      <!-- Table -->
      <el-table :data="filteredList" style="width: 100%" :header-cell-style="{ background: '#f5f7fa' }">
        <el-table-column prop="id" label="单号" width="140">
           <template #default="scope">
             <span class="mono-font">{{ scope.row.id }}</span>
           </template>
        </el-table-column>
        <el-table-column prop="itemName" label="物资名称" min-width="180">
          <template #default="scope">
            <span class="item-name">{{ scope.row.itemName }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="quantity" label="数量" width="100">
           <template #default="scope">
             <el-tag size="small" effect="plain">{{ scope.row.quantity }} {{ scope.row.unit }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column prop="applyTime" label="申请时间" width="170" sortable />
        <el-table-column prop="status" label="状态" width="120">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)" effect="light" round>
              {{ getStatusLabel(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="用途备注" min-width="200" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="scope">
            <el-button link type="primary" v-if="scope.row.status === 'Pending'">撤回</el-button>
            <el-button link type="primary" v-else>详情</el-button>
          </template>
        </el-table-column>
      </el-table>

       <div class="pagination-footer">
        <el-pagination background layout="total, prev, pager, next" :total="filteredList.length" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Box, Timer, Warning, Search, Plus, Refresh } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const searchText = ref('')
const filterStatus = ref('')
const dateRange = ref([])
const loading = ref(false)

// 申请列表数据
const applyList = ref([])

// 加载我的申请列表
const loadMyApply = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/material/apply/my')
    if (response.data.code === 200) {
      applyList.value = (response.data.data || []).map(item => ({
        id: item.applyId || 'MAT' + Date.now(),
        itemName: item.matName || '物资',
        quantity: item.count,
        unit: '件',
        applyTime: item.applyDate || new Date().toLocaleString(),
        status: mapStatus(item.state),
        reason: item.reason || ''
      }))
    }
  } catch (error) {
    console.error('加载申请列表失败:', error)
    applyList.value = []
  } finally {
    loading.value = false
  }
}

// 状态映射
const mapStatus = (state) => {
  const map = { 0: 'Pending', 1: 'Approved', 2: 'Rejected' }
  return map[state] || 'Pending'
}

onMounted(() => {
  loadMyApply()
})

const filteredList = computed(() => {
  return applyList.value.filter(item => {
    const matchText = !searchText.value || item.itemName.includes(searchText.value) || item.id.includes(searchText.value)
    const matchStatus = !filterStatus.value || item.status === filterStatus.value || (item.status === 'Completed' && filterStatus.value === 'Approved')
    
    return matchText && matchStatus
  })
})

const getStatusType = (status) => {
  const map = {
    'Pending': 'warning',
    'Approved': 'success',
    'Completed': 'info',
    'Rejected': 'danger'
  }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = {
    'Pending': '审批中',
    'Approved': '已通过',
    'Completed': '已领用',
    'Rejected': '已驳回'
  }
  return map[status] || status
}

const handleApply = () => {
  router.push('/dashboard/material/list')
}

const handleRefresh = () => {
  loadMyApply()
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

/* Stats Cards */
.stat-card { border: none; }
:deep(.el-card__body) { display: flex; align-items: center; padding: 20px !important; }
.stat-icon { width: 48px; height: 48px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 16px; font-size: 24px; }
.bg-blue { background: linear-gradient(135deg, #409eff 0%, #79bbff 100%); box-shadow: 0 4px 10px rgba(64,158,255,0.3); }
.bg-orange { background: linear-gradient(135deg, #e6a23c 0%, #f3d19e 100%); box-shadow: 0 4px 10px rgba(230,162,60,0.3); }
.bg-red { background: linear-gradient(135deg, #f56c6c 0%, #fab6b6 100%); box-shadow: 0 4px 10px rgba(245,108,108,0.3); }

.stat-value { font-size: 24px; font-weight: 700; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }

/* Table Toolbar */
.table-toolbar { display: flex; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.ml-auto { margin-left: auto; }
.mono-font { font-family: 'Roboto Mono', monospace; color: #606266; }
.item-name { font-weight: 500; color: #303133; }

/* Pagination */
.pagination-footer { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
