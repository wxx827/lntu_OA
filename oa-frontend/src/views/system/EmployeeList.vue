<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>员工管理 <span class="subtitle">Employee Management</span></h2>
        <p class="desc">管理企业员工信息、部门分配及系统权限</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Plus">新增员工</el-button>
        <el-button icon="Download">导出名单</el-button>
      </div>
    </div>

    <!-- 1. Stats Cards -->
    <el-row :gutter="20" class="mb-24">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-blue">
            <el-icon color="#fff"><UserFilled /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">员工总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-green">
            <el-icon color="#fff"><Check /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.active }}</div>
            <div class="stat-label">在职 (Active)</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange">
            <el-icon color="#fff"><Coffee /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.onLeave }}</div>
            <div class="stat-label">休假中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-purple">
            <el-icon color="#fff"><TrendCharts /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.newThisMonth > 0 ? '+' + stats.newThisMonth : stats.newThisMonth }}</div>
            <div class="stat-label">本月入职</div>
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
          placeholder="搜索姓名/手机号..." 
          prefix-icon="Search"
          style="width: 240px" 
          clearable
        />
        <el-select v-model="filterDepartment" placeholder="所属部门" style="width: 140px; margin-left: 12px" clearable>
          <el-option label="全部部门" value="" />
          <el-option label="技术部" value="技术部" />
          <el-option label="市场部" value="市场部" />
          <el-option label="人事部" value="人事部" />
          <el-option label="行政部" value="行政部" />
        </el-select>
        <el-select v-model="filterRole" placeholder="系统角色" style="width: 140px; margin-left: 12px" clearable>
          <el-option label="全部角色" value="" />
          <el-option label="管理员" value="Admin" />
          <el-option label="普通用户" value="User" />
        </el-select>
        <div class="ml-auto">
          <el-button icon="Refresh" circle @click="refreshList" />
        </div>
      </div>

      <!-- Table -->
      <el-table :data="filteredEmployees" style="width: 100%" :header-cell-style="{ background: '#f5f7fa' }">
        <el-table-column label="员工信息" min-width="240">
          <template #default="scope">
            <div class="user-cell">
              <el-avatar :size="36" :src="scope.row.avatar" />
              <div class="user-info">
                <span class="name">{{ scope.row.name }}</span>
                <span class="email">{{ scope.row.email }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="department" label="部门" width="120">
           <template #default="scope">
             <el-tag size="small" effect="plain">{{ scope.row.department }}</el-tag>
           </template>
        </el-table-column>
        <el-table-column prop="position" label="职位" width="140" />
        <el-table-column prop="phone" label="联系电话" width="140" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="scope">
            <el-badge is-dot :type="scope.row.status === 'Active' ? 'success' : 'info'" class="status-dot">
              {{ scope.row.status === 'Active' ? '在职' : '离职' }}
            </el-badge>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" width="120">
          <template #default="scope">
             <el-tag :type="scope.row.role === 'Admin' ? 'danger' : 'info'" size="small" round>
               {{ scope.row.role }}
             </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="scope">
            <el-button link type="primary" size="small">编辑</el-button>
            <el-button link type="warning" size="small">重置密码</el-button>
          </template>
        </el-table-column>
      </el-table>

       <div class="pagination-footer">
        <el-pagination background layout="total, prev, pager, next" :total="filteredEmployees.length" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { UserFilled, Check, Coffee, TrendCharts, Search, Plus, Refresh, Download } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const searchText = ref('')
const filterDepartment = ref('')
const filterRole = ref('')
const loading = ref(false)

const employees = ref([])
const stats = ref({
  total: 0,
  active: 0,
  onLeave: 0,
  newThisMonth: 0
})

// 加载员工统计
const loadStats = async () => {
  try {
    const response = await axios.get('/api/system/employees/stats')
    if (response.data.code === 200) {
      stats.value = response.data.data
    } else {
      // Fallback: Calculate from employee list
      stats.value.total = employees.value.length
      stats.value.active = employees.value.filter(e => e.status === 'Active').length
    }
  } catch (error) {
    // Fallback: Calculate from loaded employees
    stats.value.total = employees.value.length
    stats.value.active = employees.value.filter(e => e.status === 'Active').length
    stats.value.onLeave = 0
    stats.value.newThisMonth = 0
  }
}

// 加载员工列表
const loadEmployees = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/system/employees', {
      params: { keyword: searchText.value || undefined }
    })
    if (response.data.code === 200) {
      employees.value = response.data.data.map(item => ({
        id: item.empId,
        name: item.empName,
        email: item.email || `${item.empName}@oa.com`,
        department: item.depName || item.department?.depName || '未分配',
        position: item.posName || item.position?.posName || '员工',
        role: item.role || 'User',
        phone: item.tel || '-',
        status: 'Active',
        avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(item.empName)}&background=409eff&color=fff`
      }))
    }
  } catch (error) {
    console.error('加载员工列表失败:', error)
    employees.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadEmployees()
  loadStats()
})

const filteredEmployees = computed(() => {
  return employees.value.filter(item => {
    const matchText = !searchText.value || 
                      item.name.includes(searchText.value) || 
                      item.phone.includes(searchText.value)
    
    const matchDept = !filterDepartment.value || item.department === filterDepartment.value
    const matchRole = !filterRole.value || item.role === filterRole.value

    return matchText && matchDept && matchRole
  })
})

const refreshList = () => {
  loadEmployees()
  loadStats()
  ElMessage.success('数据已刷新')
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
.bg-green { background: linear-gradient(135deg, #67c23a 0%, #95d475 100%); box-shadow: 0 4px 10px rgba(103,194,58,0.3); }
.bg-orange { background: linear-gradient(135deg, #e6a23c 0%, #f3d19e 100%); box-shadow: 0 4px 10px rgba(230,162,60,0.3); }
.bg-purple { background: linear-gradient(135deg, #722ed1 0%, #9254de 100%); box-shadow: 0 4px 10px rgba(114,46,209,0.3); }

.stat-value { font-size: 24px; font-weight: 700; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }

/* Table Toolbar */
.table-toolbar { display: flex; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.ml-auto { margin-left: auto; }
.user-cell { display: flex; align-items: center; gap: 12px; }
.user-info { display: flex; flex-direction: column; }
.name { font-weight: 500; color: #303133; }
.email { font-size: 12px; color: #909399; }
.status-dot { margin-right: 8px; }

/* Pagination */
.pagination-footer { margin-top: 20px; display: flex; justify-content: flex-end; }
</style>
