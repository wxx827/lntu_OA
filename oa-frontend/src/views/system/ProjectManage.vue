<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>项目管理 <span class="subtitle">Project Management</span></h2>
        <p class="desc">管理工作台展示的项目任务</p>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus" @click="handleAdd">新增项目</el-button>
      </div>
    </div>

    <!-- Project Cards -->
    <el-row :gutter="20">
      <el-col :xs="24" :sm="12" :lg="8" v-for="project in projects" :key="project.projectId" class="mb-20">
        <el-card shadow="hover" class="project-card">
          <div class="card-header">
            <el-icon :size="32" :style="{ color: project.color }">
              <component :is="getIconComponent(project.icon)" />
            </el-icon>
            <el-tag :type="getStatusType(project.status)" size="small">{{ project.status }}</el-tag>
          </div>
          
          <h3 class="project-name">{{ project.name }}</h3>
          <p class="project-desc">{{ project.description }}</p>
          
          <div class="progress-section">
            <span class="progress-label">进度</span>
            <el-progress :percentage="project.progress" :color="project.color" :stroke-width="8" />
          </div>
          
          <div class="card-footer">
            <span class="due-date">
              <el-icon><Calendar /></el-icon>
              {{ formatDate(project.dueDate) }}
            </span>
            <div class="actions">
              <el-button link type="primary" :icon="Edit" @click="handleEdit(project)">编辑</el-button>
              <el-button link type="danger" :icon="Delete" @click="handleDelete(project)">删除</el-button>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- Empty State -->
    <el-empty v-if="projects.length === 0 && !loading" description="暂无项目，点击上方按钮新增">
      <el-button type="primary" @click="handleAdd">新增项目</el-button>
    </el-empty>

    <!-- Add/Edit Dialog -->
    <el-dialog v-model="dialogVisible" :title="isEdit ? '编辑项目' : '新增项目'" width="500px">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="项目名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入项目名称" />
        </el-form-item>
        <el-form-item label="项目描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入项目描述" />
        </el-form-item>
        <el-form-item label="进度" prop="progress">
          <el-slider v-model="form.progress" :min="0" :max="100" show-input />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="已计划" value="已计划" />
            <el-option label="进行中" value="进行中" />
            <el-option label="开发中" value="开发中" />
            <el-option label="已完成" value="已完成" />
          </el-select>
        </el-form-item>
        <el-form-item label="颜色" prop="color">
          <el-color-picker v-model="form.color" />
        </el-form-item>
        <el-form-item label="图标" prop="icon">
          <el-select v-model="form.icon" placeholder="请选择图标">
            <el-option label="平台" value="Platform" />
            <el-option label="盒子" value="Box" />
            <el-option label="数据分析" value="DataAnalysis" />
            <el-option label="文档" value="Document" />
            <el-option label="设置" value="Setting" />
          </el-select>
        </el-form-item>
        <el-form-item label="截止日期" prop="dueDate">
          <el-date-picker v-model="form.dueDate" type="date" placeholder="选择日期" style="width: 100%" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, shallowRef } from 'vue'
import { Plus, Edit, Delete, Calendar, Platform, Box, DataAnalysis, Document, Setting } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

const projects = ref([])
const loading = ref(false)
const dialogVisible = ref(false)
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)

const form = ref({
  projectId: '',
  name: '',
  description: '',
  progress: 0,
  status: '已计划',
  color: '#409eff',
  icon: 'Platform',
  dueDate: null
})

const rules = {
  name: [{ required: true, message: '请输入项目名称', trigger: 'blur' }],
  status: [{ required: true, message: '请选择状态', trigger: 'change' }]
}

const iconMap = {
  Platform,
  Box,
  DataAnalysis,
  Document,
  Setting
}

const getIconComponent = (iconName) => {
  return iconMap[iconName] || Platform
}

const getStatusType = (status) => {
  if (status === '进行中') return ''
  if (status === '开发中') return 'warning'
  if (status === '已计划') return 'info'
  if (status === '已完成') return 'success'
  return ''
}

const formatDate = (date) => {
  if (!date) return '-'
  const d = new Date(date)
  return d.toLocaleDateString('zh-CN')
}

const loadProjects = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/project/list')
    if (response.data.code === 200) {
      projects.value = response.data.data
    }
  } catch (error) {
    console.error('加载项目失败:', error)
    ElMessage.error('加载项目失败')
  } finally {
    loading.value = false
  }
}

const handleAdd = () => {
  isEdit.value = false
  form.value = {
    projectId: '',
    name: '',
    description: '',
    progress: 0,
    status: '已计划',
    color: '#409eff',
    icon: 'Platform',
    dueDate: null
  }
  dialogVisible.value = true
}

const handleEdit = (project) => {
  isEdit.value = true
  form.value = { ...project }
  dialogVisible.value = true
}

const handleDelete = async (project) => {
  try {
    await ElMessageBox.confirm(`确定要删除项目 "${project.name}" 吗？`, '确认删除', {
      type: 'warning'
    })
    await axios.delete(`/api/project/delete/${project.projectId}`)
    ElMessage.success('删除成功')
    loadProjects()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('删除失败')
    }
  }
}

const handleSubmit = async () => {
  try {
    await formRef.value.validate()
    submitting.value = true
    
    if (isEdit.value) {
      await axios.put(`/api/project/update/${form.value.projectId}`, form.value)
      ElMessage.success('更新成功')
    } else {
      await axios.post('/api/project/add', form.value)
      ElMessage.success('创建成功')
    }
    
    dialogVisible.value = false
    loadProjects()
  } catch (error) {
    if (error !== false) {
      ElMessage.error('操作失败')
    }
  } finally {
    submitting.value = false
  }
}

onMounted(() => {
  loadProjects()
})
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.header-left h2 { font-size: 24px; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; }
.desc { color: #606266; font-size: 14px; margin: 0; }

.mb-20 { margin-bottom: 20px; }

.project-card { border-radius: 12px; }
.card-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.project-name { font-size: 18px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.project-desc { font-size: 14px; color: #606266; margin: 0 0 16px 0; line-height: 1.5; }

.progress-section { margin-bottom: 16px; }
.progress-label { font-size: 12px; color: #909399; margin-bottom: 8px; display: block; }

.card-footer { display: flex; justify-content: space-between; align-items: center; border-top: 1px solid #ebeef5; padding-top: 12px; }
.due-date { font-size: 13px; color: #909399; display: flex; align-items: center; gap: 4px; }
.actions { display: flex; gap: 8px; }

.fade-in { animation: fadeIn 0.5s ease-out; }
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
