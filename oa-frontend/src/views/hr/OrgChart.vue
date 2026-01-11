<template>
  <div class="org-chart">
    <el-card>
      <template #header>
        <span>组织架构图</span>
      </template>

      <div class="org-tree-container">
        <el-tree
          :data="orgTree"
          :props="treeProps"
          default-expand-all
          :expand-on-click-node="false"
          class="org-tree"
        >
          <template #default="{ node, data }">
            <div class="custom-tree-node">
              <div class="node-content">
                <el-icon class="node-icon"><OfficeBuilding /></el-icon>
                <span class="node-label">{{ data.deptName }}</span>
                <el-tag size="small" class="node-count">{{ data.employeeCount || 0 }}人</el-tag>
              </div>
              <div class="node-actions">
                <el-button link type="primary" size="small" @click="viewDepartment(data)">
                  查看详情
                </el-button>
              </div>
            </div>
          </template>
        </el-tree>
      </div>
    </el-card>

    <!-- 部门详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="部门详情" width="700px">
      <el-descriptions v-if="selectedDept" :column="2" border>
        <el-descriptions-item label="部门名称">{{ selectedDept.deptName }}</el-descriptions-item>
        <el-descriptions-item label="部门层级">第{{ selectedDept.deptLevel }}级</el-descriptions-item>
        <el-descriptions-item label="负责人">{{ selectedDept.managerName || '-' }}</el-descriptions-item>
        <el-descriptions-item label="员工人数">{{ selectedDept.employeeCount || 0 }}人</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag :type="selectedDept.status === 'ACTIVE' ? 'success' : 'info'">
            {{ selectedDept.status === 'ACTIVE' ? '启用' : '停用' }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="创建时间">
          {{ formatDateTime(selectedDept.createTime) }}
        </el-descriptions-item>
        <el-descriptions-item label="描述" :span="2">
          {{ selectedDept.description || '-' }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 部门员工列表 -->
      <div v-if="deptEmployees.length > 0" style="margin-top: 20px;">
        <h4>部门员工</h4>
        <el-table :data="deptEmployees" stripe>
          <el-table-column prop="userName" label="姓名" />
          <el-table-column prop="positionName" label="岗位" />
          <el-table-column prop="workStatus" label="状态">
            <template #default="{ row }">
              <el-tag :type="getWorkStatusType(row.workStatus)">
                {{ getWorkStatusText(row.workStatus) }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { OfficeBuilding } from '@element-plus/icons-vue'
import axios from 'axios'

const orgTree = ref([])
const showDetailDialog = ref(false)
const selectedDept = ref(null)
const deptEmployees = ref([])

const treeProps = {
  children: 'children',
  label: 'deptName'
}

const loadOrgTree = async () => {
  try {
    const response = await axios.get('/api/hr/department/tree')
    if (response.data.success) {
      orgTree.value = response.data.data
    }
  } catch (error) {
    console.error('加载组织架构失败:', error)
  }
}

const viewDepartment = async (dept) => {
  selectedDept.value = dept
  
  // 加载部门员工
  try {
    const response = await axios.get(`/api/hr/employee/dept/${dept.deptId}`)
    if (response.data.success) {
      deptEmployees.value = response.data.data
    }
  } catch (error) {
    console.error('加载部门员工失败:', error)
  }
  
  showDetailDialog.value = true
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
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

onMounted(() => {
  loadOrgTree()
})
</script>

<style scoped>
.org-chart {
  padding: 20px;
}

.org-tree-container {
  padding: 20px;
  background: #f5f7fa;
  border-radius: 4px;
}

.org-tree {
  background: transparent;
}

.custom-tree-node {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 8px 12px;
  background: #fff;
  border-radius: 4px;
  margin: 4px 0;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.node-content {
  display: flex;
  align-items: center;
  gap: 8px;
}

.node-icon {
  font-size: 18px;
  color: #409eff;
}

.node-label {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.node-count {
  margin-left: 8px;
}

.node-actions {
  opacity: 0;
  transition: opacity 0.3s;
}

.custom-tree-node:hover .node-actions {
  opacity: 1;
}
</style>
