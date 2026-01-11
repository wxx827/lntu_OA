<template>
  <div class="fade-in">
    <!-- Header Section -->
    <div class="page-header">
      <div class="header-left">
        <h2>物资管理 <span class="subtitle">Material Inventory</span></h2>
        <p class="desc">实时监控库存状态，智能预警补货</p>
      </div>
      <div class="header-right">
        <el-input 
          v-model="searchQuery" 
          placeholder="搜索物资名称/编号..." 
          class="search-input"
          :prefix-icon="Search"
        />
        <el-button type="success" class="ml-12" circle>
          <el-icon><Plus /></el-icon>
        </el-button>
      </div>
    </div>

    <!-- Stats Row -->
    <el-row :gutter="20" class="mb-32">
      <el-col :span="6">
        <div class="stat-card gradient-blue">
          <div class="stat-icon"><el-icon><Box /></el-icon></div>
          <div class="stat-info">
            <span class="label">总物资数</span>
            <span class="value">{{ materialStats.total }}</span>
          </div>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="stat-card gradient-orange">
          <div class="stat-icon"><el-icon><Warning /></el-icon></div>
          <div class="stat-info">
            <span class="label">库存预警</span>
            <span class="value">{{ materialStats.lowStock }}</span>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Data Table -->
    <el-card class="table-card" shadow="never">
      <el-table 
        :data="materials" 
        style="width: 100%" 
        :row-class-name="tableRowClassName"
      >
        <el-table-column prop="id" label="编号" width="100" />
        <el-table-column label="物资名称" min-width="180">
          <template #default="scope">
            <div class="item-name">
              <span class="name">{{ scope.row.name }}</span>
              <el-tag size="small" type="info" class="ml-2">{{ scope.row.spec }}</el-tag>
            </div>
          </template>
        </el-table-column>
        <el-table-column prop="price" label="单价" width="120">
          <template #default="scope">
            ¥{{ scope.row.price.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column label="库存状态" width="250">
          <template #default="scope">
            <div class="stock-wrapper">
              <el-progress 
                :percentage="calculatePercentage(scope.row.stock, scope.row.maxStock)" 
                :status="getStockStatus(scope.row.stock)"
                :format="() => scope.row.stock"
                :stroke-width="8"
              />
              <span class="stock-label" :class="{'low-stock': scope.row.stock < 20}">
                {{ scope.row.stock < 20 ? '库存不足' : '充足' }}
              </span>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" align="right">
          <template #default="scope">
            <el-button 
              size="small" 
              type="primary" 
              plain
              @click="handleApply(scope.row)"
              :disabled="scope.row.stock <= 0"
            >
              领用申请
            </el-button>
          </template>
        </el-table-column>
      <template #empty>
        <el-empty v-if="!loading" description="??????" />
      </template>
      </el-table>

      
    </el-card>

    <!-- Dialog -->
    <el-dialog v-model="dialogVisible" title="物资领用申请" width="30%">
      <el-form :model="applyForm" label-width="80px">
        <el-form-item label="物资">
          <el-input v-model="applyForm.itemName" disabled />
        </el-form-item>
        <el-form-item label="数量">
          <el-input-number v-model="applyForm.count" :min="1" :max="applyForm.maxCount" />
        </el-form-item>
        <el-form-item label="用途">
          <el-input v-model="applyForm.reason" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="primary" @click="submitApply">提交申请</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { Box, Warning, Search, Plus } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const searchQuery = ref('')
const dialogVisible = ref(false)
const loading = ref(false)
const applyForm = ref({ matId: '', itemName: '', count: 1, maxCount: 100, reason: '' })

const materials = ref([])

// 加载物资列表
const loadMaterials = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/material/list')
    if (response.data.code === 200) {
      materials.value = response.data.data.map(item => ({
        id: item.matId,
        name: item.matName,
        spec: item.spec || '-',
        price: item.price || 0,
        stock: item.stock || 0,
        maxStock: Math.max(item.stock * 2, 100)
      }))
    }
  } catch (error) {
    ElMessage.error('加载物资失败，请检查网络')
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadMaterials()
})

// 过滤物资
const filteredMaterials = computed(() => {
  if (!searchQuery.value) return materials.value
  return materials.value.filter(m => 
    m.name.includes(searchQuery.value) || m.id.includes(searchQuery.value)
  )
})

// 物资统计
const materialStats = computed(() => ({
  total: materials.value.length,
  lowStock: materials.value.filter(m => m.stock < 20).length
}))

const calculatePercentage = (current, max) => Math.min(100, Math.round((current / max) * 100))

const getStockStatus = (stock) => {
  if (stock < 10) return 'exception'
  if (stock < 30) return 'warning'
  return 'success'
}

const handleApply = (row) => {
  applyForm.value = {
    matId: row.id,
    itemName: row.name,
    count: 1,
    maxCount: row.stock,
    reason: ''
  }
  dialogVisible.value = true
}

const submitApply = async () => {
  try {
    const response = await axios.post('/api/material/apply', {
      matId: applyForm.value.matId,
      count: applyForm.value.count,
      reason: applyForm.value.reason
    })
    if (response.data.code === 200) {
      ElMessage.success('申请提交成功，请等待审批')
      dialogVisible.value = false
      // 跳转到我的领用页面
      router.push('/dashboard/material/my')
    } else {
      ElMessage.error(response.data.message || '申请失败')
    }
  } catch (error) {
    ElMessage.error('申请提交失败，请检查网络连接')
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

.header-left h2 {
  font-size: 24px;
  color: #303133;
  margin: 0 0 8px 0;
}

.subtitle {
  font-size: 14px;
  color: #909399;
  margin-left: 12px;
  letter-spacing: 1px;
}
.desc {
  color: #606266;
  font-size: 14px;
  margin: 0;
}

.search-input {
  width: 240px;
}
:deep(.el-input__wrapper) {
  background-color: #fff;
}

/* Stat Cards */
.stat-card {
  padding: 20px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  gap: 16px;
  position: relative;
  overflow: hidden;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
  background: #fff;
  border: 1px solid #ebeef5;
}
.gradient-blue .stat-icon { background: #e6f7ff; color: #1890ff; }
.gradient-orange .stat-icon { background: #fff1f0; color: #f5222d; }

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
}
.stat-info {
  display: flex;
  flex-direction: column;
}
.stat-info .label {
  font-size: 12px;
  color: #909399;
}
.stat-info .value {
  font-size: 24px;
  font-weight: bold;
  color: #303133;
}

/* Table */
.table-card {
  border: 1px solid #ebeef5;
  border-radius: 8px;
  background-color: #fff !important;
}
:deep(.el-table) {
  --el-table-header-bg-color: #f5f7fa;
  color: #606266;
}
:deep(.el-table th) {
  color: #303133;
  font-weight: 600;
}

.item-name {
  display: flex;
  align-items: center;
}
.name {
  font-weight: 500;
  color: #303133;
}
.ml-2 { margin-left: 8px; }

.stock-wrapper {
  display: flex;
  align-items: center;
  gap: 12px;
}
:deep(.el-progress) {
  width: 140px;
}
.stock-label {
  font-size: 12px;
  color: #67C23A;
}
.stock-label.low-stock {
  color: #F56C6C;
}

/* Utils */
.ml-12 { margin-left: 12px; }
.mb-32 { margin-bottom: 32px; }
.fade-in { animation: fadeIn 0.5s ease-out; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
