<template>
  <div class="employee-profile">
    <el-card>
      <template #header>
        <span>员工档案管理</span>
      </template>

      <!-- 员工列表 -->
      <el-table :data="employees" stripe>
        <el-table-column prop="userName" label="姓名" width="120" />
        <el-table-column prop="deptName" label="部门" width="150" />
        <el-table-column prop="positionName" label="岗位" width="150" />
        <el-table-column prop="education" label="学历" width="100">
          <template #default="{ row }">
            {{ getEducationText(row.education) }}
          </template>
        </el-table-column>
        <el-table-column prop="joinDate" label="入职日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.joinDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="workStatus" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getWorkStatusType(row.workStatus)">
              {{ getWorkStatusText(row.workStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewProfile(row)">查看详情</el-button>
            <el-button link type="primary" @click="editProfile(row)">编辑</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 员工档案详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="员工档案详情" width="800px">
      <el-tabs v-model="activeTab">
        <el-tab-pane label="基本信息" name="basic">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="姓名">{{ currentProfile.userName }}</el-descriptions-item>
            <el-descriptions-item label="性别">{{ currentProfile.gender === 'MALE' ? '男' : '女' }}</el-descriptions-item>
            <el-descriptions-item label="身份证号">{{ currentProfile.idCard }}</el-descriptions-item>
            <el-descriptions-item label="出生日期">{{ formatDate(currentProfile.birthDate) }}</el-descriptions-item>
            <el-descriptions-item label="部门">{{ currentProfile.deptName }}</el-descriptions-item>
            <el-descriptions-item label="岗位">{{ currentProfile.positionName }}</el-descriptions-item>
            <el-descriptions-item label="学历">{{ getEducationText(currentProfile.education) }}</el-descriptions-item>
            <el-descriptions-item label="专业">{{ currentProfile.major }}</el-descriptions-item>
            <el-descriptions-item label="毕业院校" :span="2">{{ currentProfile.school }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        
        <el-tab-pane label="工作信息" name="work">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="入职日期">{{ formatDate(currentProfile.joinDate) }}</el-descriptions-item>
            <el-descriptions-item label="转正日期">{{ formatDate(currentProfile.regularDate) }}</el-descriptions-item>
            <el-descriptions-item label="工作状态">
              <el-tag :type="getWorkStatusType(currentProfile.workStatus)">
                {{ getWorkStatusText(currentProfile.workStatus) }}
              </el-tag>
            </el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
        
        <el-tab-pane label="联系信息" name="contact">
          <el-descriptions :column="2" border>
            <el-descriptions-item label="紧急联系人">{{ currentProfile.emergencyContact }}</el-descriptions-item>
            <el-descriptions-item label="紧急联系电话">{{ currentProfile.emergencyPhone }}</el-descriptions-item>
            <el-descriptions-item label="家庭住址" :span="2">{{ currentProfile.address }}</el-descriptions-item>
            <el-descriptions-item label="开户银行">{{ currentProfile.bankName }}</el-descriptions-item>
            <el-descriptions-item label="银行账号">{{ currentProfile.bankAccount }}</el-descriptions-item>
          </el-descriptions>
        </el-tab-pane>
      </el-tabs>
    </el-dialog>

    <!-- 编辑员工档案对话框 -->
    <el-dialog v-model="showEditDialog" title="编辑员工档案" width="700px">
      <el-form :model="editForm" :rules="editRules" ref="editFormRef" label-width="120px">
        <el-form-item label="部门" prop="deptId">
          <el-select v-model="editForm.deptId" placeholder="请选择部门">
            <el-option
              v-for="dept in departments"
              :key="dept.deptId"
              :label="dept.deptName"
              :value="dept.deptId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="岗位" prop="positionId">
          <el-select v-model="editForm.positionId" placeholder="请选择岗位">
            <el-option
              v-for="pos in positions"
              :key="pos.positionId"
              :label="pos.positionName"
              :value="pos.positionId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="工作状态" prop="workStatus">
          <el-select v-model="editForm.workStatus">
            <el-option label="试用期" value="PROBATION" />
            <el-option label="正式" value="REGULAR" />
            <el-option label="离职" value="RESIGNED" />
          </el-select>
        </el-form-item>
        <el-form-item label="转正日期" prop="regularDate">
          <el-date-picker
            v-model="editForm.regularDate"
            type="date"
            placeholder="选择转正日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const employees = ref([])
const departments = ref([])
const positions = ref([])
const showDetailDialog = ref(false)
const showEditDialog = ref(false)
const currentProfile = ref({})
const activeTab = ref('basic')
const editFormRef = ref(null)

const editForm = ref({
  profileId: null,
  userId: null,
  deptId: null,
  positionId: null,
  workStatus: '',
  regularDate: null
})

const editRules = {
  deptId: [{ required: true, message: '请选择部门', trigger: 'change' }],
  positionId: [{ required: true, message: '请选择岗位', trigger: 'change' }],
  workStatus: [{ required: true, message: '请选择工作状态', trigger: 'change' }]
}

const loadEmployees = async () => {
  try {
    const response = await axios.get('/api/hr/employee/profile/list')
    if (response.data.success) {
      employees.value = response.data.data
    }
  } catch (error) {
    console.error('加载员工列表失败:', error)
  }
}

const loadDepartments = async () => {
  try {
    const response = await axios.get('/api/hr/department/list')
    if (response.data.success) {
      departments.value = response.data.data
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
  }
}

const loadPositions = async () => {
  try {
    const response = await axios.get('/api/hr/position/list')
    if (response.data.success) {
      positions.value = response.data.data
    }
  } catch (error) {
    console.error('加载岗位列表失败:', error)
  }
}

const viewProfile = (row) => {
  currentProfile.value = row
  activeTab.value = 'basic'
  showDetailDialog.value = true
}

const editProfile = (row) => {
  editForm.value = {
    profileId: row.profileId,
    userId: row.userId,
    deptId: row.deptId,
    positionId: row.positionId,
    workStatus: row.workStatus,
    regularDate: row.regularDate
  }
  showEditDialog.value = true
}

const handleUpdate = async () => {
  const valid = await editFormRef.value.validate()
  if (!valid) return

  try {
    const response = await axios.put('/api/hr/employee/profile/update', editForm.value)
    if (response.data.success) {
      ElMessage.success('更新成功')
      showEditDialog.value = false
      await loadEmployees()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('更新失败')
  }
}

const getEducationText = (education) => {
  const map = {
    'HIGH_SCHOOL': '高中',
    'COLLEGE': '大专',
    'BACHELOR': '本科',
    'MASTER': '硕士',
    'DOCTOR': '博士'
  }
  return map[education] || education
}

const getWorkStatusType = (status) => {
  const map = {
    'PROBATION': 'warning',
    'REGULAR': 'success',
    'RESIGNED': 'info'
  }
  return map[status] || 'info'
}

const getWorkStatusText = (status) => {
  const map = {
    'PROBATION': '试用期',
    'REGULAR': '正式',
    'RESIGNED': '离职'
  }
  return map[status] || status
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

onMounted(() => {
  loadEmployees()
  loadDepartments()
  loadPositions()
})
</script>

<style scoped>
.employee-profile {
  padding: 20px;
}
</style>
