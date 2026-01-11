<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>车辆管理 <span class="subtitle">Vehicle Management</span></h2>
        <p class="desc">企业公车信息维护与用车申请管理</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Plus" @click="showAddDialog = true">添加车辆</el-button>
      </div>
    </div>

    <!-- Stats Cards -->
    <el-row :gutter="20" class="mb-24">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-blue">
            <el-icon color="#fff"><Van /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.total }}</div>
            <div class="stat-label">车辆总数</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-green">
            <el-icon color="#fff"><CircleCheck /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.available }}</div>
            <div class="stat-label">空闲可用</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange">
            <el-icon color="#fff"><Clock /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.inUse }}</div>
            <div class="stat-label">使用中</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-red">
            <el-icon color="#fff"><Tools /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ stats.maintenance }}</div>
            <div class="stat-label">维修中</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Vehicle Grid -->
    <el-row :gutter="20">
      <el-col :span="8" v-for="car in carList" :key="car.carId">
        <el-card shadow="hover" class="car-card mb-20">
          <div class="car-header">
            <div class="license-plate">{{ car.licensePlate }}</div>
            <el-tag :type="getStatusType(car.status)" size="small">
              {{ getStatusLabel(car.status) }}
            </el-tag>
          </div>
          <div class="car-body">
            <div class="car-info-row">
              <span class="label">品牌型号:</span>
              <span class="value">{{ car.brand }} {{ car.model }}</span>
            </div>
            <div class="car-info-row">
              <span class="label">座位数:</span>
              <span class="value">{{ car.seats }} 座</span>
            </div>
            <div class="car-info-row">
              <span class="label">里程数:</span>
              <span class="value">{{ car.mileage?.toLocaleString() || 0 }} km</span>
            </div>
            <div class="car-info-row">
              <span class="label">上次保养:</span>
              <span class="value">{{ car.lastMaintenanceDate || '暂无记录' }}</span>
            </div>
          </div>
          <div class="car-footer">
            <el-button-group>
              <el-button size="small" @click="handleEdit(car)">编辑</el-button>
              <el-button size="small" @click="handleViewHistory(car)">用车记录</el-button>
              <el-button size="small" type="danger" @click="handleDelete(car)">删除</el-button>
            </el-button-group>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Add/Edit Dialog -->
    <el-dialog 
      v-model="showAddDialog" 
      :title="editingCar ? '编辑车辆' : '添加车辆'" 
      width="500px"
    >
      <el-form :model="carForm" label-width="100px">
        <el-form-item label="车牌号">
          <el-input v-model="carForm.licensePlate" placeholder="如: 京A12345" />
        </el-form-item>
        <el-form-item label="品牌">
          <el-input v-model="carForm.brand" placeholder="如: Toyota" />
        </el-form-item>
        <el-form-item label="型号">
          <el-input v-model="carForm.model" placeholder="如: Camry" />
        </el-form-item>
        <el-form-item label="座位数">
          <el-input-number v-model="carForm.seats" :min="2" :max="50" />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="carForm.status" style="width: 100%">
            <el-option label="空闲可用" value="AVAILABLE" />
            <el-option label="使用中" value="IN_USE" />
            <el-option label="维修中" value="MAINTENANCE" />
          </el-select>
        </el-form-item>
        <el-form-item label="当前里程">
          <el-input-number v-model="carForm.mileage" :min="0" :step="1000" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showAddDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSave">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { Van, CircleCheck, Clock, Tools, Plus } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import request from '@/utils/request'

const showAddDialog = ref(false)
const editingCar = ref(null)
const loading = ref(false)

const carForm = reactive({
  licensePlate: '',
  brand: '',
  model: '',
  seats: 5,
  status: 'AVAILABLE',
  mileage: 0
})

const carList = ref([])

// 加载车辆列表
const loadCarList = async () => {
  loading.value = true
  try {
    const data = await request.get('/car/list')
    carList.value = (data || []).map(item => ({
      carId: item.carId,
      licensePlate: item.carLicence,
      brand: item.brand,
      model: item.carType,
      seats: item.seats || 5,
      status: mapStatus(item.state),
      mileage: item.mileage || 0,
      remark: item.remark
    }))
  } catch (error) {
    console.error('加载车辆列表失败:', error)
    carList.value = []
  } finally {
    loading.value = false
  }
}

const mapStatus = (state) => {
  const map = { 0: 'AVAILABLE', 1: 'IN_USE', 2: 'MAINTENANCE' }
  return map[state] || 'AVAILABLE'
}

onMounted(() => {
  loadCarList()
})

const stats = computed(() => ({
  total: carList.value.length,
  available: carList.value.filter(c => c.status === 'AVAILABLE').length,
  inUse: carList.value.filter(c => c.status === 'IN_USE').length,
  maintenance: carList.value.filter(c => c.status === 'MAINTENANCE').length,
}))

const getStatusType = (status) => {
  const map = { AVAILABLE: 'success', IN_USE: 'warning', MAINTENANCE: 'danger' }
  return map[status] || 'info'
}

const getStatusLabel = (status) => {
  const map = { AVAILABLE: '空闲', IN_USE: '使用中', MAINTENANCE: '维修中' }
  return map[status] || status
}

const handleEdit = (car) => {
  editingCar.value = car
  Object.assign(carForm, car)
  showAddDialog.value = true
}

const handleViewHistory = (car) => {
  ElMessage.info(`查看 ${car.licensePlate} 的用车记录`)
}

const handleDelete = async (car) => {
  ElMessageBox.confirm(`确定要删除车辆 ${car.licensePlate} 吗？`, '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning',
  }).then(async () => {
    try {
      await request.delete(`/car/delete/${car.carId}`)
      ElMessage.success('删除成功')
      loadCarList()
    } catch (error) {
      ElMessage.error('删除失败，请检查网络连接')
    }
  })
}

// 将前端status映射到后端state
const mapStatusToState = (status) => {
  const map = { 'AVAILABLE': 0, 'IN_USE': 1, 'MAINTENANCE': 2 }
  return map[status] ?? 0
}

const handleSave = async () => {
  const submitData = {
    carLicence: carForm.licensePlate,
    brand: carForm.brand,
    carType: carForm.model,
    seats: carForm.seats,
    state: mapStatusToState(carForm.status),
    mileage: carForm.mileage
  }
  try {
    if (editingCar.value) {
      await request.put(`/car/update/${editingCar.value.carId}`, submitData)
      ElMessage.success('更新成功')
    } else {
      await request.post('/car/add', submitData)
      ElMessage.success('添加成功')
    }
    loadCarList()
    showAddDialog.value = false
    editingCar.value = null
    Object.keys(carForm).forEach(key => carForm[key] = key === 'seats' ? 5 : key === 'status' ? 'AVAILABLE' : key === 'mileage' ? 0 : '')
  } catch (error) {
    ElMessage.error(editingCar.value ? '更新失败' : '添加失败')
  }
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
.bg-red { background: linear-gradient(135deg, #f56c6c 0%, #fab6b6 100%); box-shadow: 0 4px 10px rgba(245,108,108,0.3); }
.stat-value { font-size: 24px; font-weight: 700; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }

/* Car Cards */
.car-card { border-radius: 8px; transition: all 0.3s; }
.car-card:hover { transform: translateY(-4px); box-shadow: 0 8px 16px rgba(0,0,0,0.1); }
.car-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; padding-bottom: 12px; border-bottom: 1px solid #e4e7ed; }
.license-plate { font-size: 18px; font-weight: 700; color: #303133; font-family: 'Courier New', monospace; }
.car-body { margin-bottom: 16px; }
.car-info-row { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 14px; }
.car-info-row .label { color: #909399; }
.car-info-row .value { color: #303133; font-weight: 500; }
.car-footer { display: flex; justify-content: center; }
</style>
