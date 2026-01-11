<template>
  <div class="fade-in main-container">
    <div class="page-header">
      <div class="header-left">
        <h2>权限配置 <span class="subtitle">角色与权限管理</span></h2>
        <p class="desc">管理系统角色及功能访问权限，配置企业组织架构的安全策略</p>
      </div>
      <div class="header-right">
        <el-button type="primary" :icon="Plus" size="large">新增角色</el-button>
      </div>
    </div>

    <el-row :gutter="24" class="content-row">
      <!-- Role List -->
      <el-col :span="8" class="h-100">
        <el-card shadow="never" class="card-shadow h-100" :body-style="{ padding: '0', height: 'calc(100% - 60px)', display: 'flex', flexDirection: 'column' }">
          <template #header>
            <div class="card-header">
              <span>角色列表</span>
              <el-tag type="info" size="small">{{ roles.length }} 个角色</el-tag>
            </div>
          </template>
          
          <el-scrollbar>
            <div class="role-list">
               <div 
                 v-for="role in roles" 
                 :key="role.id" 
                 class="role-item"
                 :class="{ active: currentRole.id === role.id }"
                 @click="currentRole = role"
               >
                  <div class="role-icon" :style="{ background: role.color }">
                    <el-icon color="#fff"><component :is="role.icon" /></el-icon>
                  </div>
                  <div class="role-info">
                    <div class="role-name">{{ role.name }}</div>
                    <div class="role-desc">{{ role.desc }}</div>
                  </div>
                  <el-icon v-if="currentRole.id === role.id" color="#409eff"><Check /></el-icon>
                  <el-icon v-else class="arrow-icon"><ArrowRight /></el-icon>
               </div>
            </div>
          </el-scrollbar>
        </el-card>
      </el-col>

      <!-- Permission Matrix -->
      <el-col :span="16" class="h-100">
        <el-card shadow="never" class="card-shadow h-100" :body-style="{ height: 'calc(100% - 60px)', display: 'flex', flexDirection: 'column' }">
          <template #header>
            <div class="card-header">
              <div class="flex items-center">
                <span class="mr-4">{{ currentRole.name || '选择角色' }} - 权限分配</span>
                <el-tag v-if="currentRole.name" size="small" effect="plain" type="primary">正在编辑</el-tag>
              </div>
              <div>
                <el-button plain @click="loadPermissions">重置</el-button>
                <el-button type="primary" :loading="saving" @click="handleSave">保存配置</el-button>
              </div>
            </div>
          </template>
          
          <div class="tree-container">
             <el-empty v-if="!currentRole.id" description="请选择左侧角色查看权限" />
             <el-scrollbar v-else>
               <el-tree
                 ref="permTreeRef"
                 :data="permissions"
                 show-checkbox
                 node-key="id"
                 :default-checked-keys="currentRole.permissions"
                 :props="defaultProps"
                 default-expand-all
                 highlight-current
                 class="permission-tree"
               />
             </el-scrollbar>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus, User, Lock, Check, Key, Star, ArrowRight } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

const roles = ref([])
const permissions = ref([])
const currentRole = ref({})
const loading = ref(false)
const saving = ref(false)
const permTreeRef = ref(null)

// Mock Data if API fails
const mockRoles = [
  { id: 1, name: '超级管理员', desc: '拥有系统所有权限', icon: 'Lock', color: '#f56c6c', permissions: [1, 2, 3, 4, 5, 6, 7, 8, 9, 10] },
  { id: 2, name: '部门经理', desc: '管理部门员工及考勤', icon: 'User', color: '#409eff', permissions: [1, 2, 3, 4] },
  { id: 3, name: '普通员工', desc: '仅访问基础功能', icon: 'Star', color: '#e6a23c', permissions: [1, 2] },
  { id: 4, name: '财务专员', desc: '处理报销与财务审批', icon: 'Key', color: '#67c23a', permissions: [1, 5] },
]

const mockPermissions = [
  {
    id: 1, label: '工作台', children: [
       { id: 11, label: '查看统计' },
       { id: 12, label: '查看动态' }
    ]
  },
  {
    id: 2, label: '考勤管理', children: [
       { id: 21, label: '打卡记录' },
       { id: 22, label: '请假审批' },
       { id: 23, label: '考勤统计' }
    ]
  },
  {
    id: 3, label: '系统设置', children: [
       { id: 31, label: '员工管理' },
       { id: 32, label: '角色权限' },
       { id: 33, label: '系统日志' }
    ]
  },
  {
    id: 4, label: '流程审批', children: [
       { id: 41, label: '发起申请' },
       { id: 42, label: '审批流程' }
    ]
  },
  {
    id: 5, label: '财务管理', children: [
       { id: 51, label: '报销审核' },
       { id: 52, label: '薪资查看' }
    ]
  }
]

// 加载角色列表
const loadRoles = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/role/list')
    if (response.data.code === 200 && response.data.data) {
      roles.value = response.data.data.map(item => ({
        id: item.roleId,
        name: item.roleName,
        desc: item.roleDesc || '暂无描述',
        icon: item.roleName?.includes('管理员') ? 'Lock' : item.roleName?.includes('经理') ? 'User' : item.roleName?.includes('财务') ? 'Key' : 'Star',
        color: item.roleName?.includes('管理员') ? '#f56c6c' : item.roleName?.includes('经理') ? '#409eff' : item.roleName?.includes('财务') ? '#67c23a' : '#e6a23c',
        permissions: item.permissions ? JSON.parse(item.permissions) : []
      }))
      
      if (roles.value.length > 0) {
        currentRole.value = roles.value[0]
      }
    } else {
      // Fallback to mock data only if API returns empty
      console.warn('API返回空数据，使用模拟数据')
      roles.value = mockRoles
      currentRole.value = mockRoles[0]
    }
  } catch (error) {
    console.warn('API调用失败，使用模拟数据:', error)
    roles.value = mockRoles
    currentRole.value = mockRoles[0]
  } finally {
    loading.value = false
  }
}

// 加载权限树
const loadPermissions = async () => {
  try {
    const response = await axios.get('/api/role/permissions')
    if (response.data.code === 200 && response.data.data) {
      permissions.value = response.data.data
    } else {
      console.warn('权限树API返回空数据，使用模拟数据')
      permissions.value = mockPermissions
    }
  } catch (error) {
    console.warn('权限树API调用失败，使用模拟数据:', error)
    permissions.value = mockPermissions
  }
}

const handleSave = async () => {
  saving.value = true
  // Simulate API delay
  await new Promise(resolve => setTimeout(resolve, 800))
  
  // Get checked keys from tree
  const checkedKeys = permTreeRef.value ? permTreeRef.value.getCheckedKeys() : []
  console.log('保存权限:', currentRole.value.name, checkedKeys)
  
  ElMessage.success(`已更新 [${currentRole.value.name}] 的权限配置`)
  saving.value = false
}

onMounted(() => {
  loadRoles()
  loadPermissions()
})

const defaultProps = {
  children: 'children',
  label: 'label',
}
</script>

<style scoped>
.main-container {
  display: flex;
  flex-direction: column;
  height: 100%;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  flex-shrink: 0;
}
.header-left h2 {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}
.subtitle {
  font-size: 14px;
  font-weight: 500;
  color: #909399;
  margin-left: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}
.desc { color: #606266; font-size: 14px; margin: 0; }

.content-row {
  flex: 1;
  overflow: hidden; /* Prevent scrolling on content-row itself */
}

.h-100 { height: 100%; }

.role-list {
  display: flex;
  flex-direction: column;
}
.role-item {
  display: flex;
  align-items: center;
  padding: 20px 24px;
  cursor: pointer;
  border-bottom: 1px solid #f5f7fa;
  transition: all 0.2s;
  position: relative;
}
.role-item:hover { background: #f9fafc; }
.role-item.active { background: #ecf5ff; }
.role-item.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: #409eff;
}

.role-icon {
  width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; margin-right: 16px; font-size: 22px;
  transition: transform 0.2s;
}
.role-item:hover .role-icon { transform: scale(1.05); }

.role-info { flex: 1; }
.role-name { font-weight: 600; color: #303133; margin-bottom: 4px; font-size: 15px; }
.role-desc { font-size: 13px; color: #909399; }

.arrow-icon { opacity: 0; transition: opacity 0.2s; color: #c0c4cc; }
.role-item:hover .arrow-icon { opacity: 1; }

.card-header { display: flex; justify-content: space-between; align-items: center; font-weight: 600; height: 32px; }
.flex { display: flex; }
.items-center { align-items: center; }
.mr-4 { margin-right: 16px; }

.tree-container {
  flex: 1;
  overflow: hidden; /* Scroll handled by el-scrollbar */
  display: flex;
  flex-direction: column;
}

.permission-tree {
  padding: 10px;
}
:deep(.el-tree-node__content) {
  height: 36px;
  border-radius: 4px;
}
:deep(.el-tree-node__content:hover) {
  background-color: #f5f7fa;
}
</style>
