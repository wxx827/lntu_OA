<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>通讯录 <span class="subtitle">Address Book</span></h2>
        <p class="desc">企业组织架构与员工联系方式</p>
      </div>
      <div class="header-right">
        <el-input v-model="searchText" placeholder="搜索姓名/部门..." clearable style="width: 250px">
          <template #prefix><el-icon><Search /></el-icon></template>
        </el-input>
      </div>
    </div>

    <el-row :gutter="24">
      <!-- Department Tree -->
      <el-col :span="6">
        <el-card shadow="never" class="tree-card card-shadow">
          <template #header>
            <div class="card-header">
              <el-icon><OfficeBuilding /></el-icon>
              <span>组织架构</span>
            </div>
          </template>
          <el-tree
            :data="departments"
            :props="defaultProps"
            :default-expand-all="true"
            @node-click="handleNodeClick"
            highlight-current
          >
            <template #default="{ node, data }">
              <div class="custom-tree-node">
                <el-icon :color="data.color"><component :is="data.icon" /></el-icon>
                <span>{{ node.label }}</span>
                <el-tag size="small" type="info" effect="plain">{{ data.count }}</el-tag>
              </div>
            </template>
          </el-tree>
        </el-card>
      </el-col>

      <!-- Contact List -->
      <el-col :span="18">
        <el-card shadow="never" class="card-shadow">
          <template #header>
            <div class="card-header">
              <span>{{ currentDept }} - 成员列表</span>
              <el-radio-group v-model="viewMode" size="small">
                <el-radio-button label="card"><el-icon><Grid /></el-icon></el-radio-button>
                <el-radio-button label="list"><el-icon><List /></el-icon></el-radio-button>
              </el-radio-group>
            </div>
          </template>

          <!-- Card View -->
          <el-row :gutter="16" v-if="viewMode === 'card'">
            <el-col :span="6" v-for="contact in contacts" :key="contact.id">
              <div class="contact-card">
                <div class="contact-header">
                  <el-avatar :size="64" :src="contact.avatar" />
                  <el-tag v-if="contact.isOnline" size="small" type="success" class="online-badge">在线</el-tag>
                </div>
                <div class="contact-name">{{ contact.name }}</div>
                <div class="contact-position">{{ contact.position }}</div>
                <div class="contact-dept">{{ contact.dept }}</div>
                <el-divider style="margin: 12px 0" />
                <div class="contact-info">
                  <div class="info-item">
                    <el-icon><Phone /></el-icon>
                    <span>{{ contact.phone }}</span>
                  </div>
                  <div class="info-item">
                    <el-icon><Message /></el-icon>
                    <span>{{ contact.email }}</span>
                  </div>
                </div>
                <div class="contact-actions">
                  <el-button size="small" type="primary" plain>发消息</el-button>
                  <el-button size="small" plain>查看详情</el-button>
                </div>
              </div>
            </el-col>
          </el-row>

          <!-- List View -->
          <el-table :data="contacts" v-else style="width: 100%">
            <el-table-column label="姓名" width="200">
              <template #default="scope">
                <div class="user-cell">
                  <el-avatar :size="36" :src="scope.row.avatar" />
                  <div class="user-info">
                    <div class="user-name">
                      {{ scope.row.name }}
                      <el-tag v-if="scope.row.isOnline" size="small" type="success" effect="plain">在线</el-tag>
                    </div>
                    <div class="user-position">{{ scope.row.position }}</div>
                  </div>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="dept" label="部门" width="150" />
            <el-table-column prop="phone" label="手机号" width="150">
              <template #default="scope">
                <el-icon><Phone /></el-icon> {{ scope.row.phone }}
              </template>
            </el-table-column>
            <el-table-column prop="email" label="邮箱" min-width="200">
              <template #default="scope">
                <el-icon><Message /></el-icon> {{ scope.row.email }}
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180">
              <template #default>
                <el-button link type="primary" :icon="ChatDotRound">发消息</el-button>
                <el-button link type="primary" :icon="User">详情</el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { 
  Search, OfficeBuilding, Grid, List, Phone, Message, 
  ChatDotRound, User, Briefcase, UserFilled
} from '@element-plus/icons-vue'
import axios from 'axios'

const searchText = ref('')
const viewMode = ref('card')
const currentDept = ref('全部')
const loading = ref(false)

const defaultProps = {
  children: 'children',
  label: 'label',
}

const departments = ref([
  {
    label: '总公司',
    icon: 'OfficeBuilding',
    color: '#409eff',
    count: 0,
    children: [],
  },
])

const contacts = ref([])

// 加载通讯录数据
const loadContacts = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/system/employees')
    if (response.data.code === 200) {
      contacts.value = response.data.data.map(item => ({
        id: item.empId,
        name: item.empName,
        position: item.posName || '员工',
        dept: item.depName || '未分配',
        phone: item.tel || '-',
        email: item.email || `${item.empName}@company.com`,
        avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(item.empName)}&background=409eff&color=fff`,
        isOnline: Math.random() > 0.5
      }))
      // 统计部门人数
      const deptCount = {}
      contacts.value.forEach(c => {
        deptCount[c.dept] = (deptCount[c.dept] || 0) + 1
      })
      departments.value[0].count = contacts.value.length
      departments.value[0].children = Object.keys(deptCount).map(d => ({
        label: d,
        icon: 'Briefcase',
        color: '#67c23a',
        count: deptCount[d]
      }))
    }
  } catch (error) {
    console.error('加载通讯录失败:', error)
    contacts.value = []
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadContacts()
})

// 过滤联系人
const filteredContacts = computed(() => {
  return contacts.value.filter(c => {
    const matchSearch = !searchText.value || c.name.includes(searchText.value) || c.dept.includes(searchText.value)
    const matchDept = currentDept.value === '全部' || currentDept.value === '总公司' || c.dept === currentDept.value
    return matchSearch && matchDept
  })
})

const handleNodeClick = (data) => {
  currentDept.value = data.label
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
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px 0;
}
.subtitle {
  font-size: 14px;
  font-weight: 400;
  color: #909399;
  margin-left: 12px;
  text-transform: uppercase;
}
.desc { color: #606266; font-size: 14px; margin: 0; }

.tree-card {
  height: calc(100vh - 200px);
  overflow-y: auto;
}
.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-weight: 600;
  justify-content: space-between;
}
.custom-tree-node {
  display: flex;
  align-items: center;
  gap: 8px;
  flex: 1;
  padding-right: 8px;
}

.contact-card {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  transition: all 0.3s;
  text-align: center;
}
.contact-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
  transform: translateY(-4px);
}
.contact-header {
  position: relative;
  margin-bottom: 16px;
}
.online-badge {
  position: absolute;
  top: 0;
  right: calc(50% - 40px);
}
.contact-name {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
}
.contact-position {
  font-size: 13px;
  color: #606266;
  margin-bottom: 4px;
}
.contact-dept {
  font-size: 12px;
  color: #909399;
}
.contact-info {
  margin-bottom: 16px;
}
.info-item {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  font-size: 12px;
  color: #606266;
  margin-bottom: 8px;
}
.contact-actions {
  display: flex;
  gap: 8px;
}

.user-cell {
  display: flex;
  align-items: center;
  gap: 12px;
}
.user-info {
  flex: 1;
}
.user-name {
  font-weight: 600;
  color: #303133;
  margin-bottom: 4px;
  display: flex;
  align-items: center;
  gap: 8px;
}
.user-position {
  font-size: 12px;
  color: #909399;
}
</style>
