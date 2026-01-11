<template>
  <div class="department-list">
    <el-card>
      <template #header>
        <div class="card-header">
          <span>部门管理</span>
          <el-button type="primary" @click="showAddDialog">
            <el-icon><Plus /></el-icon>
            新增部门
          </el-button>
        </div>
      </template>

      <!-- 部门树形表格 -->
      <el-table
        :data="departments"
        row-key="deptId"
        :tree-props="{ children: 'children', hasChildren: 'hasChildren' }"
        stripe
      >
        <el-table-column prop="deptName" label="部门名称" width="250" />
        <el-table-column prop="managerName" label="负责人" width="120" />
        <el-table-column prop="employeeCount" label="人数" width="80" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 'ACTIVE' ? 'success' : 'info'">
              {{ row.status === 'ACTIVE' ? '启用' : '停用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="描述" show-overflow-tooltip />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="handleEdit(row)">编辑</el-button>
            <el-button link type="primary" @click="handleAddChild(row)">添加子部门</el-button>
            <el-button link type="danger" @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 新增/编辑部门对话框 -->
    <el-dialog v-model="showDialog" :title="dialogTitle" width="600px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="部门名称" prop="deptName">
          <el-input v-model="form.deptName" placeholder="请输入部门名称" />
        </el-form-item>
        <el-form-item label="上级部门" prop="parentId">
          <el-tree-select
            v-model="form.parentId"
            :data="departmentTreeOptions"
            :props="{ label: 'deptName', value: 'deptId' }"
            placeholder="请选择上级部门（不选则为顶级部门）"
            clearable
            check-strictly
          />
        </el-form-item>
        <el-form-item label="负责人" prop="managerId">
          <el-input-number v-model="form.managerId" placeholder="负责人ID" />
        </el-form-item>
        <el-form-item label="排序" prop="sortOrder">
          <el-input-number v-model="form.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="ACTIVE">启用</el-radio>
            <el-radio label="INACTIVE">停用</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input
            v-model="form.description"
            type="textarea"
            :rows="3"
            placeholder="请输入部门描述"
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
import axios from 'axios'

const showDialog = ref(false)
const dialogTitle = ref('新增部门')
const departments = ref([])
const departmentTreeOptions = ref([])
const formRef = ref(null)
const isEdit = ref(false)

const form = ref({
  deptId: null,
  deptName: '',
  parentId: null,
  managerId: null,
  sortOrder: 0,
  status: 'ACTIVE',
  description: ''
})

const rules = {
  deptName: [{ required: true, message: '请输入部门名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const loadDepartments = async () => {
  try {
    const response = await axios.get('/api/hr/department/tree')
    if (response.data.success) {
      departments.value = response.data.data
      departmentTreeOptions.value = response.data.data
    }
  } catch (error) {
    console.error('加载部门列表失败:', error)
  }
}

const showAddDialog = () => {
  isEdit.value = false
  dialogTitle.value = '新增部门'
  form.value = {
    deptId: null,
    deptName: '',
    parentId: null,
    managerId: null,
    sortOrder: 0,
    status: 'ACTIVE',
    description: ''
  }
  showDialog.value = true
}

const handleAddChild = (row) => {
  isEdit.value = false
  dialogTitle.value = '新增子部门'
  form.value = {
    deptId: null,
    deptName: '',
    parentId: row.deptId,
    managerId: null,
    sortOrder: 0,
    status: 'ACTIVE',
    description: ''
  }
  showDialog.value = true
}

const handleEdit = (row) => {
  isEdit.value = true
  dialogTitle.value = '编辑部门'
  form.value = { ...row }
  showDialog.value = true
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate()
  if (!valid) return

  try {
    const url = isEdit.value ? '/api/hr/department/update' : '/api/hr/department/add'
    const method = isEdit.value ? 'put' : 'post'
    
    const response = await axios[method](url, form.value)
    
    if (response.data.success) {
      ElMessage.success(response.data.message)
      showDialog.value = false
      await loadDepartments()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

const handleDelete = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该部门吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    const response = await axios.delete(`/api/hr/department/${row.deptId}`)
    
    if (response.data.success) {
      ElMessage.success('删除成功')
      await loadDepartments()
    } else {
      ElMessage.error(response.data.message)
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

onMounted(() => {
  loadDepartments()
})
</script>

<style scoped>
.department-list {
  padding: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
</style>
