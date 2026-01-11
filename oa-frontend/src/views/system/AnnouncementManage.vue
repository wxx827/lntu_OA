<template>
  <div class="announcement-manage">
    <!-- 页面头部 -->
    <div class="page-header">
      <h2>公告管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        发布公告
      </el-button>
    </div>

    <!-- 数据表格 -->
    <el-card shadow="hover">
      <el-table :data="announcements" v-loading="loading" stripe>
        <el-table-column type="index" label="#" width="60" />
        
        <el-table-column prop="title" label="标题" min-width="200">
          <template #default="{ row }">
            <el-tag v-if="row.isTop === 1" type="danger" size="small" class="mr-2">置顶</el-tag>
            {{ row.title }}
          </template>
        </el-table-column>
        
        <el-table-column prop="type" label="类型" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="row.type === 'important' ? 'danger' : row.type === 'urgent' ? 'warning' : 'info'"
              size="small">
              {{ getTypeLabel(row.type) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag 
              :type="row.status === 1 ? 'success' : row.status === 0 ? 'info' : 'danger'"
              size="small">
              {{ getStatusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        
        <el-table-column prop="publisherName" label="发布人" width="120" />
        
        <el-table-column prop="publishTime" label="发布时间" width="180">
          <template #default="{ row }">
            {{ formatTime(row.publishTime) }}
          </template>
        </el-table-column>
        
        <el-table-column prop="views" label="浏览量" width="100" />
        
        <el-table-column label="操作" width="280" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" size="small" @click="handleView(row)">
              <el-icon><View /></el-icon> 查看
            </el-button>
            <el-button link type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon> 编辑
            </el-button>
            <el-button 
              link 
              :type="row.isTop === 1 ? 'warning' : 'success'" 
              size="small" 
              @click="handleToggleTop(row)">
              <el-icon><Top /></el-icon> {{ row.isTop === 1 ? '取消置顶' : '置顶' }}
            </el-button>
            <el-button link type="danger" size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon> 删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 编辑/新增对话框 -->
    <el-dialog 
      v-model="dialogVisible" 
      :title="dialogTitle"
      width="800px"
      @close="handleDialogClose">
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="公告标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入公告标题" maxlength="200" show-word-limit />
        </el-form-item>
        
        <el-form-item label="公告类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="normal">普通</el-radio>
            <el-radio label="important">重要</el-radio>
            <el-radio label="urgent">紧急</el-radio>
          </el-radio-group>
        </el-form-item>
        
        <el-form-item label="公告内容" prop="content">
          <el-input 
            v-model="form.content" 
            type="textarea" 
            :rows="8"
            placeholder="请输入公告内容"
            maxlength="2000"
            show-word-limit />
        </el-form-item>
        
        <el-form-item label="是否置顶">
          <el-switch v-model="form.isTop" :active-value="1" :inactive-value="0" />
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitting">发布</el-button>
      </template>
    </el-dialog>

    <!-- 查看详情对话框 -->
    <el-dialog v-model="viewDialogVisible" title="公告详情" width="700px">
      <div class="announcement-detail" v-if="currentAnnouncement">
        <h3>{{ currentAnnouncement.title }}</h3>
        <div class="meta-info">
          <span>发布人: {{ currentAnnouncement.publisherName }}</span>
          <span>发布时间: {{ formatTime(currentAnnouncement.publishTime) }}</span>
          <span>浏览量: {{ currentAnnouncement.views }}</span>
        </div>
        <div class="content">
          {{ currentAnnouncement.content }}
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, View, Top } from '@element-plus/icons-vue'
import axios from 'axios'

const loading = ref(false)
const announcements = ref([])
const dialogVisible = ref(false)
const viewDialogVisible = ref(false)
const dialogTitle = ref('发布公告')
const submitting = ref(false)
const formRef = ref(null)
const currentAnnouncement = ref(null)

const form = ref({
  id: null,
  title: '',
  content: '',
  type: 'normal',
  isTop: 0
})

const rules = {
  title: [{ required: true, message: '请输入公告标题', trigger: 'blur' }],
  content: [{ required: true, message: '请输入公告内容', trigger: 'blur' }],
  type: [{ required: true, message: '请选择公告类型', trigger: 'change' }]
}

// 加载公告列表
const loadAnnouncements = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/announcement/list')
    if (response.data.success) {
      announcements.value = response.data.data
    }
  } catch (error) {
    ElMessage.error('加载公告列表失败')
    console.error(error)
  } finally {
    loading.value = false
  }
}

// 新增公告
const handleAdd = () => {
  dialogTitle.value = '发布公告'
  form.value = {
    id: null,
    title: '',
    content: '',
    type: 'normal',
    isTop: 0
  }
  dialogVisible.value = true
}

// 编辑公告
const handleEdit = (row) => {
  dialogTitle.value = '编辑公告'
  form.value = { ...row }
  dialogVisible.value = true
}

// 查看详情
const handleView = async (row) => {
  try {
    const response = await axios.get(`/api/announcement/${row.id}`)
    if (response.data.success) {
      currentAnnouncement.value = response.data.data
      viewDialogVisible.value = true
    }
  } catch (error) {
    ElMessage.error('加载公告详情失败')
  }
}

// 提交表单
const handleSubmit = async () => {
  if (!formRef.value) return
  
  await formRef.value.validate(async (valid) => {
    if (valid) {
      submitting.value = true
      try {
        const response = await axios.post('/api/announcement/save', form.value)
        if (response.data.success) {
          ElMessage.success(form.value.id ? '更新成功' : '发布成功')
          dialogVisible.value = false
          loadAnnouncements()
        } else {
          ElMessage.error(response.data.message || '操作失败')
        }
      } catch (error) {
        ElMessage.error('操作失败')
        console.error(error)
      } finally {
        submitting.value = false
      }
    }
  })
}

// 置顶/取消置顶
const handleToggleTop = async (row) => {
  try {
    const response = await axios.put(`/api/announcement/toggle-top/${row.id}`)
    if (response.data.success) {
      ElMessage.success(row.isTop === 1 ? '已取消置顶' : '已置顶')
      loadAnnouncements()
    }
  } catch (error) {
    ElMessage.error('操作失败')
  }
}

// 删除公告
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除这条公告吗?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  }).then(async () => {
    try {
      const response = await axios.delete(`/api/announcement/${row.id}`)
      if (response.data.success) {
        ElMessage.success('删除成功')
        loadAnnouncements()
      }
    } catch (error) {
      ElMessage.error('删除失败')
    }
  }).catch(() => {})
}

// 关闭对话框
const handleDialogClose = () => {
  formRef.value?.resetFields()
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return '-'
  return new Date(time).toLocaleString('zh-CN')
}

// 获取类型标签
const getTypeLabel = (type) => {
  const map = {
    'important': '重要',
    'urgent': '紧急',
    'normal': '普通'
  }
  return map[type] || '普通'
}

// 获取状态标签
const getStatusLabel = (status) => {
  const map = {
    0: '草稿',
    1: '已发布',
    2: '已撤回'
  }
  return map[status] || '未知'
}

onMounted(() => {
  loadAnnouncements()
})
</script>

<style scoped>
.announcement-manage {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-header h2 {
  margin: 0;
  font-size: 24px;
  color: #303133;
}

.mr-2 {
  margin-right: 8px;
}

.announcement-detail h3 {
  margin: 0 0 16px 0;
  font-size: 20px;
  color: #303133;
}

.meta-info {
  display: flex;
  gap: 24px;
  padding: 12px 0;
  border-bottom: 1px solid #ebeef5;
  margin-bottom: 16px;
  font-size: 13px;
  color: #909399;
}

.content {
  line-height: 1.8;
  color: #606266;
  white-space: pre-wrap;
  min-height: 200px;
}
</style>
