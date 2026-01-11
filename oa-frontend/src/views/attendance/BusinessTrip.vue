<template>
  <div class="fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-left">
        <h2>出差申请 <span class="subtitle">Business Trip</span></h2>
        <p class="desc">管理差旅计划，提交出差申请和预算审批</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Plus" size="large" @click="showDialog = true">新建申请</el-button>
      </div>
    </div>

    <!-- Stats Cards -->
    <el-row :gutter="20" class="mb-24">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-blue">
            <el-icon color="#fff"><Place /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.activeTrips }}</div>
            <div class="stat-label">进行中行程</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange">
            <el-icon color="#fff"><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.pending }}</div>
            <div class="stat-label">待审批</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-green">
            <el-icon color="#fff"><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalDays }}</div>
            <div class="stat-label">年度出差天数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-purple">
            <el-icon color="#fff"><Money /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">¥{{ stats.totalCost }}</div>
            <div class="stat-label">累计预算</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Main Content -->
    <el-card shadow="never" class="card-shadow main-card">
      <!-- Toolbar -->
      <div class="table-toolbar">
        <el-input 
          v-model="searchText" 
          placeholder="搜索目的地/事由..." 
          prefix-icon="Search"
          style="width: 240px" 
          clearable
        />
        <el-select v-model="filterStatus" placeholder="状态" style="width: 140px; margin-left: 12px" clearable>
          <el-option label="全部状态" value="" />
          <el-option label="待审批" value="PENDING" />
          <el-option label="已批准" value="APPROVED" />
          <el-option label="已拒绝" value="REJECTED" />
          <el-option label="已取消" value="CANCELLED" />
        </el-select>
        <div class="ml-auto">
          <el-button icon="Refresh" circle @click="refreshList" />
        </div>
      </div>

      <!-- Table -->
      <el-table :data="filteredTrips" style="width: 100%; flex: 1" height="100%" :header-cell-style="{ background: '#f5f7fa' }" stripe>
        <el-table-column label="目的地" width="180">
          <template #default="{ row }">
             <div class="flex items-center">
                <el-icon class="mr-2" color="#409eff"><Place /></el-icon>
                <span class="font-medium">{{ row.destination }}</span>
             </div>
          </template>
        </el-table-column>
        <el-table-column label="行程时间" min-width="240">
          <template #default="{ row }">
            <div class="date-range">
              <span class="date-text">{{ formatDate(row.startTime) }}</span>
              <el-icon class="mx-2" color="#909399"><Right /></el-icon>
              <span class="date-text">{{ formatDate(row.endTime) }}</span>
              <el-tag size="small" type="info" class="ml-2">{{ row.days }}天</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="transportType" label="交通" width="100">
          <template #default="{ row }">
            <el-tag size="small" effect="plain">{{ getTransportText(row.transportType) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="estimatedCost" label="预算" width="120">
          <template #default="{ row }">
            <span class="cost-text">¥ {{ row.estimatedCost?.toLocaleString() }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="purpose" label="事由" show-overflow-tooltip min-width="200" />
        <el-table-column prop="status" label="状态" width="120" fixed="right">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="dark">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>
      
      
       <div v-if="filteredTrips.length === 0 && !loading" class="flex-center" style="flex: 1; display: flex; justify-content: center; align-items: center; min-height: 300px;">
          <el-empty description="暂无出差记录" />
       </div>
    </el-card>

    <!-- 新建出差申请对话框 -->
    <el-dialog v-model="showDialog" title="新建出差申请" width="650px" destroy-on-close class="custom-dialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="mt-4">
        <el-row :gutter="20">
          <el-col :span="12">
            <el-form-item label="目的地" prop="destination">
               <el-input v-model="form.destination" placeholder="城市/地点" prefix-icon="Place" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
             <el-form-item label="交通方式" prop="transportType">
              <el-select v-model="form.transportType" placeholder="请选择" style="width: 100%">
                <el-option label="飞机" value="FLIGHT" />
                <el-option label="火车" value="TRAIN" />
                <el-option label="汽车" value="CAR" />
                <el-option label="其他" value="OTHER" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="20">
           <el-col :span="12">
             <el-form-item label="开始时间" prop="startTime">
               <el-date-picker
                 v-model="form.startTime"
                 type="datetime"
                 placeholder="出发时间"
                 format="YYYY-MM-DD HH:mm"
                 value-format="YYYY-MM-DD HH:mm:ss"
                 style="width: 100%"
               />
             </el-form-item>
           </el-col>
           <el-col :span="12">
             <el-form-item label="结束时间" prop="endTime">
               <el-date-picker
                 v-model="form.endTime"
                 type="datetime"
                 placeholder="返回时间"
                 format="YYYY-MM-DD HH:mm"
                 value-format="YYYY-MM-DD HH:mm:ss"
                 style="width: 100%"
               />
             </el-form-item>
           </el-col>
        </el-row>
        
        <el-row :gutter="20">
          <el-col :span="12">
             <el-form-item label="出差天数" prop="days">
                <el-input-number v-model="form.days" :min="0.5" :step="0.5" :precision="1" style="width: 100%" />
             </el-form-item>
          </el-col>
          <el-col :span="12">
             <el-form-item label="预算费用" prop="estimatedCost">
                <el-input-number v-model="form.estimatedCost" :min="0" :step="100" :precision="2" style="width: 100%" />
             </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="出差事由" prop="purpose">
          <el-input
            v-model="form.purpose"
            type="textarea"
            :rows="3"
            placeholder="请输入详细的出差事由和计划..."
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="showDialog = false">取消</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="submitting">提交申请</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Search, Refresh, Place, Timer, Calendar, Money, Right } from '@element-plus/icons-vue'
import axios from 'axios'

const showDialog = ref(false)
const businessTrips = ref([])
const formRef = ref(null)
const loading = ref(false)
const submitting = ref(false)
const userId = ref(1) // TODO: Get from store

// Filters
const searchText = ref('')
const filterStatus = ref('')

const form = ref({
  userId: 1,
  destination: '',
  startTime: '',
  endTime: '',
  days: 1,
  transportType: '',
  estimatedCost: 0,
  purpose: ''
})

const rules = {
  destination: [{ required: true, message: '请输入目的地', trigger: 'blur' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  days: [{ required: true, message: '请输入出差天数', trigger: 'blur' }],
  transportType: [{ required: true, message: '请选择交通方式', trigger: 'change' }],
  estimatedCost: [{ required: true, message: '请输入预算费用', trigger: 'blur' }],
  purpose: [{ required: true, message: '请输入出差事由', trigger: 'blur' }]
}

// Stats
const stats = computed(() => {
  const now = new Date()
  const activeTrips = businessTrips.value.filter(t => {
      const start = new Date(t.startTime)
      const end = new Date(t.endTime)
      return t.status === 'APPROVED' && now >= start && now <= end
  }).length
  
  const pending = businessTrips.value.filter(t => t.status === 'PENDING').length
  const totalDays = businessTrips.value.reduce((acc, curr) => acc + (curr.status === 'APPROVED' ? Number(curr.days) : 0), 0)
  const totalCost = businessTrips.value.reduce((acc, curr) => acc + (curr.status === 'APPROVED' ? Number(curr.estimatedCost) : 0), 0)

  return { activeTrips, pending, totalDays, totalCost: totalCost.toLocaleString() }
})

const loadBusinessTrips = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/attendance/trip/my-requests', {
      params: { userId: userId.value }
    })
    if (response.data.success) {
      businessTrips.value = response.data.data
    }
  } catch (error) {
    console.error('加载出差申请失败:', error)
  } finally {
    loading.value = false
  }
}

const handleSubmit = async () => {
  if (!formRef.value) return
  const valid = await formRef.value.validate()
  if (!valid) return

  submitting.value = true
  try {
    const response = await axios.post('/api/attendance/trip/apply', form.value)
    if (response.data.success) {
      ElMessage.success('出差申请提交成功')
      showDialog.value = false
      formRef.value.resetFields()
      await loadBusinessTrips()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('提交失败')
  } finally {
    submitting.value = false
  }
}

const filteredTrips = computed(() => {
  return businessTrips.value.filter(item => {
    const matchText = !searchText.value || 
                      item.destination.includes(searchText.value) || 
                      item.purpose.includes(searchText.value)
    
    const matchStatus = !filterStatus.value || item.status === filterStatus.value
    return matchText && matchStatus
  })
})

const refreshList = () => {
  loadBusinessTrips()
  ElMessage.success('数据已刷新')
}

const getTransportText = (type) => {
  const map = {
    'FLIGHT': '飞机', 'TRAIN': '火车', 'CAR': '汽车', 'OTHER': '其他'
  }
  return map[type] || type
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'warning', 'APPROVED': 'success', 'REJECTED': 'danger', 'CANCELLED': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待审批', 'APPROVED': '已批准', 'REJECTED': '已拒绝', 'CANCELLED': '已取消'
  }
  return map[status] || status
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN', { month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' })
}

onMounted(() => {
  loadBusinessTrips()
})
</script>

<style scoped>
/* Layout Container */
.fade-in {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-shrink: 0; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

/* Stats Cards */
.mb-24 { margin-bottom: 24px; flex-shrink: 0; }
.stat-card { border: none; transition: transform 0.3s; }
.stat-card:hover { transform: translateY(-5px); }
:deep(.stat-card .el-card__body) { display: flex; align-items: center; padding: 20px !important; }
.stat-icon { width: 48px; height: 48px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 16px; font-size: 24px; }
.bg-blue { background: linear-gradient(135deg, #409eff 0%, #79bbff 100%); box-shadow: 0 4px 10px rgba(64,158,255,0.3); }
.bg-green { background: linear-gradient(135deg, #67c23a 0%, #95d475 100%); box-shadow: 0 4px 10px rgba(103,194,58,0.3); }
.bg-orange { background: linear-gradient(135deg, #e6a23c 0%, #f3d19e 100%); box-shadow: 0 4px 10px rgba(230,162,60,0.3); }
.bg-purple { background: linear-gradient(135deg, #722ed1 0%, #9254de 100%); box-shadow: 0 4px 10px rgba(114,46,209,0.3); }

.stat-value { font-size: 24px; font-weight: 700; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }

/* Main Card (Flex Fill) */
.main-card {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
  border: none;
}
:deep(.main-card .el-card__body) {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 24px;
  overflow: hidden;
}

/* Toolbar */
.table-toolbar { display: flex; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; flex-shrink: 0; }
.ml-auto { margin-left: auto; }

/* Table Styling */
.flex { display: flex; }
.items-center { align-items: center; }
.mr-2 { margin-right: 8px; }
.font-medium { font-weight: 500; }
.date-range { display: flex; align-items: center; }
.date-text { font-family: monospace; color: #606266; }
.mx-2 { margin: 0 8px; }
.ml-2 { margin-left: 8px; }
.cost-text { font-family: 'DIN Alternate', monospace; font-weight: 600; color: #606266; }

.mt-4 { margin-top: 16px; }

/* Dialog */
.dialog-footer { display: flex; justify-content: flex-end; gap: 12px; }
</style>
