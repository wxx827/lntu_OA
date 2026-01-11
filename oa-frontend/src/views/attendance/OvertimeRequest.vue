<template>
  <div class="fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-left">
        <h2>加班申请 <span class="subtitle">Overtime Request</span></h2>
        <p class="desc">提交您的加班申请，并查看审批进度</p>
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
            <el-icon color="#fff"><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.totalHours }}h</div>
            <div class="stat-label">累计加班时长</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange">
            <el-icon color="#fff"><Document /></el-icon>
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
            <el-icon color="#fff"><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.approved }}</div>
            <div class="stat-label">已批准</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-red">
            <el-icon color="#fff"><CircleClose /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.rejected }}</div>
            <div class="stat-label">被驳回</div>
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
          placeholder="搜索加班原因..." 
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
      <el-table :data="filteredRequests" style="width: 100%; flex: 1" height="100%" :header-cell-style="{ background: '#f5f7fa' }" stripe>
        <el-table-column prop="overtimeDate" label="加班日期" width="140">
          <template #default="{ row }">
            <span class="date-text font-bold">{{ formatDate(row.overtimeDate) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="时间范围" min-width="200">
          <template #default="{ row }">
            <div class="date-range">
              <span class="date-text">{{ formatTime(row.startTime) }}</span>
              <el-icon class="mx-2" color="#909399"><Right /></el-icon>
              <span class="date-text">{{ formatTime(row.endTime) }}</span>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="hours" label="时长" width="120">
          <template #default="{ row }">
            <el-tag size="small" type="warning" effect="plain">{{ row.hours }} 小时</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="reason" label="加班原因" show-overflow-tooltip min-width="200" />
        <el-table-column prop="createTime" label="申请时间" width="180">
          <template #default="{ row }">
            <span class="text-gray">{{ formatDateTime(row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120" fixed="right">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)" effect="dark">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
      </el-table>

       <div v-if="filteredRequests.length === 0 && !loading" class="flex-center" style="flex: 1; display: flex; justify-content: center; align-items: center; min-height: 300px;">
          <el-empty description="暂无加班记录" />
       </div>
    </el-card>

    <!-- 新建加班申请对话框 -->
    <el-dialog v-model="showDialog" title="新建加班申请" width="600px" destroy-on-close class="custom-dialog">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px" class="mt-4">
        <el-form-item label="加班日期" prop="overtimeDate">
          <el-date-picker
            v-model="form.overtimeDate"
            type="date"
            placeholder="选择加班日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            style="width: 100%"
          />
        </el-form-item>
        
        <el-row :gutter="20">
           <el-col :span="12">
             <el-form-item label="开始时间" prop="startTime">
               <el-date-picker
                 v-model="form.startTime"
                 type="datetime"
                 placeholder="开始时间"
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
                 placeholder="结束时间"
                 format="YYYY-MM-DD HH:mm"
                 value-format="YYYY-MM-DD HH:mm:ss"
                 style="width: 100%"
               />
             </el-form-item>
           </el-col>
        </el-row>

        <el-form-item label="预计时长" prop="hours">
          <el-input-number v-model="form.hours" :min="0.5" :step="0.5" :precision="1" />
          <span class="ml-2 text-gray">小时</span>
        </el-form-item>
        
        <el-form-item label="加班原因" prop="reason">
          <el-input
            v-model="form.reason"
            type="textarea"
            :rows="4"
            placeholder="请输入加班原因..."
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
import { Plus, Search, Refresh, Timer, Document, CircleCheck, CircleClose, Right } from '@element-plus/icons-vue'
import axios from 'axios'

const showDialog = ref(false)
const overtimeRequests = ref([])
const formRef = ref(null)
const loading = ref(false)
const submitting = ref(false)
const userId = ref(1) // TODO: Get from store

// Filters
const searchText = ref('')
const filterStatus = ref('')

const form = ref({
  userId: 1,
  overtimeDate: '',
  startTime: '',
  endTime: '',
  hours: 2,
  reason: ''
})

const rules = {
  overtimeDate: [{ required: true, message: '请选择加班日期', trigger: 'change' }],
  startTime: [{ required: true, message: '请选择开始时间', trigger: 'change' }],
  endTime: [{ required: true, message: '请选择结束时间', trigger: 'change' }],
  hours: [{ required: true, message: '请输入加班时长', trigger: 'blur' }],
  reason: [{ required: true, message: '请输入加班原因', trigger: 'blur' }]
}

// Computed stats
const stats = computed(() => {
  const totalHours = overtimeRequests.value.reduce((acc, curr) => acc + (Number(curr.hours) || 0), 0)
  const pending = overtimeRequests.value.filter(r => r.status === 'PENDING').length
  const approved = overtimeRequests.value.filter(r => r.status === 'APPROVED').length
  const rejected = overtimeRequests.value.filter(r => r.status === 'REJECTED').length
  return { totalHours: totalHours.toFixed(1), pending, approved, rejected }
})

const loadOvertimeRequests = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/attendance/overtime/my-requests', {
      params: { userId: userId.value }
    })
    if (response.data.success) {
      overtimeRequests.value = response.data.data
    }
  } catch (error) {
    console.error('加载加班申请失败:', error)
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
    const response = await axios.post('/api/attendance/overtime/apply', form.value)
    if (response.data.success) {
      ElMessage.success('加班申请提交成功')
      showDialog.value = false
      formRef.value.resetFields()
      await loadOvertimeRequests()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('提交失败')
  } finally {
     submitting.value = false
  }
}

const filteredRequests = computed(() => {
  return overtimeRequests.value.filter(item => {
    const matchText = !searchText.value || item.reason.includes(searchText.value)
    const matchStatus = !filterStatus.value || item.status === filterStatus.value
    return matchText && matchStatus
  })
})

const refreshList = () => {
  loadOvertimeRequests()
  ElMessage.success('数据已刷新')
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
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleTimeString('zh-CN', { hour12: false, hour: '2-digit', minute: '2-digit' })
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN', {
    year: 'numeric', month: '2-digit', day: '2-digit',
    hour: '2-digit', minute: '2-digit'
  })
}

onMounted(() => {
  loadOvertimeRequests()
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
.bg-red { background: linear-gradient(135deg, #f56c6c 0%, #fde2e2 100%); box-shadow: 0 4px 10px rgba(245,108,108,0.3); }

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
.date-range { display: flex; align-items: center; }
.date-text { font-family: monospace; color: #606266; }
.font-bold { font-weight: 600; color: #303133; }
.mx-2 { margin: 0 8px; }
.ml-2 { margin-left: 8px; }
.text-gray { color: #909399; }

.mt-4 { margin-top: 16px; }

/* Dialog */
.dialog-footer { display: flex; justify-content: flex-end; gap: 12px; }
</style>
