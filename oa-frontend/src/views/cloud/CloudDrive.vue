<template>
  <div class="fade-in h-100">
    <!-- Header -->
    <div class="page-header">
      <div class="header-left">
        <h2>云文件管理 <span class="subtitle">Cloud Drive</span></h2>
        <p class="desc">企业级云盘，支持文件上传、下载与共享</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Upload" size="large" @click="handleUploadClick">上传文件</el-button>
        <el-button icon="FolderAdd" size="large" @click="showNewFolderDialog = true">新建文件夹</el-button>
        <input type="file" ref="fileInput" style="display: none" multiple @change="handleFileChange" />
      </div>
    </div>

    <el-card shadow="never" class="drive-container card-shadow">
      <el-container class="h-100">
        <!-- Sidebar: Folder Tree -->
        <el-aside width="240px" class="drive-sider">
          <div class="sider-title">
            <el-icon><Folder /></el-icon> <span>我的文件</span>
          </div>
          <el-menu
            default-active="all"
            class="drive-menu"
            @select="handleMenuSelect"
          >
            <el-menu-item index="all">
              <el-icon><CopyDocument /></el-icon>
              <span>全部文件</span>
            </el-menu-item>
            <el-menu-item index="doc">
              <el-icon><Document /></el-icon>
              <span>文档</span>
            </el-menu-item>
            <el-menu-item index="image">
              <el-icon><Picture /></el-icon>
              <span>图片</span>
            </el-menu-item>
            <el-menu-item index="video">
              <el-icon><VideoCamera /></el-icon>
              <span>视频</span>
            </el-menu-item>
            <el-menu-item index="share">
              <el-icon><Share /></el-icon>
              <span>我的共享</span>
            </el-menu-item>
          </el-menu>
          
          <div class="storage-info">
             <div class="info-row">
               <span>存储空间</span>
               <span>{{ formatSize(storageUsed) }} / 100GB</span>
             </div>
             <el-progress :percentage="storageRef" status="success" :stroke-width="10" :show-text="false" />
          </div>
        </el-aside>

        <!-- Main: File List -->
        <el-main class="drive-main">
          <!-- Breadcrumb & Toolbox -->
          <div class="file-toolbar">
            <div class="breadcrumb-area">
               <el-breadcrumb separator="/">
                 <el-breadcrumb-item><a @click="navigateToRoot">根目录</a></el-breadcrumb-item>
                 <el-breadcrumb-item v-for="(crumb, index) in crumbs" :key="crumb.id">
                    <a @click="navigateToFolder(crumb, index)">{{ crumb.name }}</a>
                 </el-breadcrumb-item>
               </el-breadcrumb>
            </div>
            <div class="view-toggles">
               <el-radio-group v-model="viewMode" size="small">
                 <el-radio-button label="list"><el-icon><Operation /></el-icon></el-radio-button>
                 <el-radio-button label="grid"><el-icon><Menu /></el-icon></el-radio-button>
               </el-radio-group>
            </div>
          </div>

          <!-- File Area -->
          <div v-loading="loading" class="file-area">
             <!-- List View -->
             <el-table 
               v-if="viewMode === 'list'" 
               :data="fileList" 
               style="width: 100%" 
               @row-click="handleRowClick"
               height="100%"
             >
               <el-table-column width="40">
                  <template #default="{ row }">
                     <el-icon :size="20" class="file-icon-sm">
                       <component :is="getFileIcon(row)" />
                     </el-icon>
                  </template>
               </el-table-column>
               <el-table-column prop="name" label="文件名" min-width="200" show-overflow-tooltip />
               <el-table-column prop="size" label="大小" width="120">
                 <template #default="{ row }">
                   {{ row.isDir ? '-' : formatSize(row.size) }}
                 </template>
               </el-table-column>
               <el-table-column prop="updateTime" label="修改时间" width="180">
                 <template #default="{ row }">
                    {{ formatDate(row.updateTime) }}
                 </template>
               </el-table-column>
               <el-table-column label="操作" width="150" align="right">
                 <template #default="{ row }">
                    <el-button link type="primary" size="small" v-if="!row.isDir" @click.stop="downloadFile(row)">下载</el-button>
                    <el-button link type="danger" size="small" @click.stop="deleteFile(row)">删除</el-button>
                 </template>
               </el-table-column>
             </el-table>

             <!-- Grid View -->
             <div v-else class="file-grid-wrapper">
                <el-row :gutter="20">
                  <el-col :span="4" v-for="file in fileList" :key="file.id">
                    <el-card shadow="hover" class="file-card" @click="handleRowClick(file)">
                       <div class="file-card-body">
                          <div class="file-icon-lg">
                             <el-icon :color="getFileColor(file)"><component :is="getFileIcon(file)" /></el-icon>
                          </div>
                          <div class="file-name" :title="file.name">{{ file.name }}</div>
                       </div>
                    </el-card>
                  </el-col>
                </el-row>
                <el-empty v-if="fileList.length === 0" description="空文件夹" />
             </div>
          </div>
        </el-main>
      </el-container>
    </el-card>
    
    <!-- New Folder Dialog -->
    <el-dialog v-model="showNewFolderDialog" title="新建文件夹" width="400px">
       <el-input v-model="newFolderName" placeholder="请输入文件夹名称" @keyup.enter="createFolder" />
       <template #footer>
          <div class="dialog-footer">
             <el-button @click="showNewFolderDialog = false">取消</el-button>
             <el-button type="primary" @click="createFolder" :loading="creating">确定</el-button>
          </div>
       </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { 
  Folder, Document, Picture, VideoCamera, Share, Upload, FolderAdd, 
  CopyDocument, Operation, Menu, Delete 
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

// State
const viewMode = ref('list') // 'list' or 'grid'
const loading = ref(false)
const fileList = ref([])
const currentPathId = ref('root')
const crumbs = ref([]) // Breadcrumbs
const fileInput = ref(null)
const storageUsed = ref(0)

// New Folder
const showNewFolderDialog = ref(false)
const newFolderName = ref('')
const creating = ref(false)

// Mock/API
const userId = 1 // TODO: Get from store

const storageRef = computed(() => {
  return Math.min((storageUsed.value / (100 * 1024 * 1024 * 1024)) * 100, 100)
})

// === API Calls ===

const loadFiles = async (parentId = 'root') => {
  loading.value = true
  try {
    const response = await axios.get('/api/disk/list', { params: { parentId, userId } })
    if (response.data.success) {
       fileList.value = response.data.data
       storageUsed.value = response.data.totalSize || 0
    } else {
       // Fallback for demo if API not ready
       console.warn('API error, using mockup if applicable')
    }
  } catch (err) {
    console.error('Fetch files failed:', err)
  } finally {
    loading.value = false
  }
}

const createFolder = async () => {
  if (!newFolderName.value.trim()) return
  creating.value = true
  try {
     const response = await axios.post('/api/disk/folder', {
       name: newFolderName.value,
       parentId: currentPathId.value,
       userId
     })
     if (response.data.success) {
       ElMessage.success('创建成功')
       showNewFolderDialog.value = false
       newFolderName.value = ''
       loadFiles(currentPathId.value)
     }
  } catch(err) {
     ElMessage.error('创建失败')
  } finally {
     creating.value = false
  }
}

const handleUploadClick = () => {
  fileInput.value.click()
}

const handleFileChange = async (e) => {
  const files = e.target.files
  if (!files.length) return
  
  const formData = new FormData()
  for (let i = 0; i < files.length; i++) {
    formData.append('files', files[i])
  }
  formData.append('parentId', currentPathId.value)
  formData.append('userId', userId)
  
  try {
    const loadingMsg = ElMessage.info({ message: '正在上传...', duration: 0 })
    const res = await axios.post('/api/disk/upload', formData, {
      headers: { 'Content-Type': 'multipart/form-data' }
    })
    loadingMsg.close()
    if (res.data.success) {
       ElMessage.success('上传成功')
       loadFiles(currentPathId.value)
    } else {
       ElMessage.error('上传失败')
    }
  } catch(err) {
     ElMessage.error('上传出错')
  }
  // Clear input
  fileInput.value.value = ''
}

const deleteFile = (row) => {
   ElMessageBox.confirm('确定要删除该文件/文件夹吗？', '警告', {
      type: 'warning'
   }).then(async () => {
      try {
         await axios.post('/api/disk/delete', { id: row.id })
         ElMessage.success('删除成功')
         loadFiles(currentPathId.value)
      } catch(e) {
         ElMessage.error('删除失败')
      }
   })
}

const downloadFile = (row) => {
   // Simulated download link
   const link = document.createElement('a')
   link.href = `/api/disk/download/${row.id}`
   link.download = row.name
   link.click()
}

// === Navigation Logic ===

const handleRowClick = (row) => {
  if (row.isDir) {
    enterFolder(row)
  } else {
    // Preview?
  }
}

const enterFolder = (folder) => {
  crumbs.value.push(folder)
  currentPathId.value = folder.id
  loadFiles(folder.id)
}

const navigateToRoot = () => {
  crumbs.value = []
  currentPathId.value = 'root'
  loadFiles('root')
}

const navigateToFolder = (crumb, index) => {
   // Keep crumbs up to index
   crumbs.value = crumbs.value.slice(0, index + 1)
   currentPathId.value = crumb.id
   loadFiles(crumb.id)
}

const handleMenuSelect = (index) => {
   // Filter logic could go here
   console.log('Menu select:', index)
}

// === Helpers ===

const formatSize = (bytes) => {
   if (bytes === 0) return '0 B'
   const k = 1024, sizes = ['B', 'KB', 'MB', 'GB']
   const i = Math.floor(Math.log(bytes) / Math.log(k))
   return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i]
}

const formatDate = (str) => {
   if (!str) return '-'
   return new Date(str).toLocaleString()
}

const getFileIcon = (row) => {
  if (row.isDir) return 'Folder'
  const ext = row.name.split('.').pop().toLowerCase()
  if (['jpg','png','jpeg','gif'].includes(ext)) return 'Picture'
  if (['mp4','avi','mov'].includes(ext)) return 'VideoCamera'
  return 'Document'
}

const getFileColor = (row) => {
   if (row.isDir) return '#E6A23C'
   return '#409EFF'
}

onMounted(() => {
  loadFiles()
})
</script>

<style scoped>
.h-100 { height: 100%; display: flex; flex-direction: column; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; flex-shrink: 0; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

.drive-container { flex: 1; display: flex; border: none; padding: 0; overflow: hidden; }
/* Override Element Card Body */
:deep(.el-card__body) { padding: 0 !important; height: 100%; width: 100%; }

.drive-sider {
  border-right: 1px solid #f0f2f5;
  background-color: #fafafa;
  display: flex;
  flex-direction: column;
}
.sider-title {
  padding: 20px;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 8px;
  color: #303133;
}
.drive-menu {
  border-right: none;
  background: transparent;
  flex: 1;
}
.storage-info {
  padding: 24px;
  border-top: 1px solid #e4e7ed;
}
.info-row {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #606266;
  margin-bottom: 8px;
}

.drive-main {
  padding: 20px;
  display: flex;
  flex-direction: column;
}
.file-toolbar {
  display: flex;
  justify-content: space-between;
  margin-bottom: 16px;
  align-items: center;
}
.breadcrumb-area a { cursor: pointer; transition: color 0.2s; font-weight: 500; }
.breadcrumb-area a:hover { color: #409eff; }

.file-area {
  flex: 1;
  overflow: auto;
}

/* Grid View */
.file-grid-wrapper { padding: 4px; }
.file-card {
  cursor: pointer;
  margin-bottom: 20px;
  transition: transform 0.2s;
  border-radius: 8px;
}
.file-card:hover { transform: translateY(-4px); }
.file-card-body {
  text-align: center;
  padding: 16px;
}
.file-icon-lg { font-size: 48px; margin-bottom: 12px; }
.file-name {
  font-size: 13px;
  color: #606266;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

/* List Icons */
.file-icon-sm { color: #909399; }
</style>
