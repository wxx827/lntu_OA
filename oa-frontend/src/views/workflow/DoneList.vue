<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>已办历史 <span class="subtitle">Workflow History</span></h2>
        <p class="desc">查看您处理过的所有审批记录及流程归档</p>
      </div>
    </div>

    <!-- 1. Stats Cards -->
    <el-row :gutter="20" class="mb-24">
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-blue">
            <el-icon color="#fff"><Finished /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ doneStats.total }}</div>
            <div class="stat-label">累计处理</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-green">
            <el-icon color="#fff"><Check /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ doneStats.passed }}</div>
            <div class="stat-label">审批通过</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-red">
            <el-icon color="#fff"><Close /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ doneStats.rejected }}</div>
            <div class="stat-label">驳回记录</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card shadow="hover" class="stat-card">
          <div class="stat-icon bg-orange">
            <el-icon color="#fff"><Timer /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">-</div>
            <div class="stat-label">平均耗时</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 2. Main Content Card -->
    <el-card shadow="never" class="card-shadow">
      <el-tabs v-model="activeTab" class="custom-tabs">
        <!-- Tab 1: Processed Tasks (Table View) -->
        <el-tab-pane label="我处理的" name="processed">
          <!-- Toolbar -->
          <div class="table-toolbar">
            <el-input 
              v-model="searchText" 
              placeholder="搜索流程标题/编号..." 
              prefix-icon="Search"
              style="width: 240px" 
              clearable
            />
            <el-select v-model="filterType" placeholder="流程类型" style="width: 140px; margin-left: 12px" clearable>
              <el-option label="全部类型" value="" />
              <el-option label="采购流程" value="采购流程" />
              <el-option label="请假流程" value="请假流程" />
              <el-option label="报销流程" value="报销流程" />
            </el-select>
            <el-date-picker
              v-model="dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 240px; margin-left: 12px"
            />
            <div class="ml-auto">
              <el-button type="primary" plain icon="Download">导出报表</el-button>
            </div>
          </div>

          <!-- Table -->
          <el-table :data="filteredHistory" style="width: 100%" :header-cell-style="{ background: '#f5f7fa' }" row-key="id">
            <el-table-column prop="id" label="流程编号" width="130">
               <template #default="scope">
                 <span class="mono-font">{{ scope.row.id }}</span>
               </template>
            </el-table-column>
            <el-table-column prop="title" label="任务标题" min-width="220">
              <template #default="scope">
                <span class="task-title">{{ scope.row.title }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="initiator" label="发起人" width="140">
               <template #default="scope">
                <el-avatar :size="24" :src="scope.row.avatar" style="vertical-align: middle; margin-right: 8px" />
                <span>{{ scope.row.initiator }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="type" label="类型" width="120">
              <template #default="scope">
                <el-tag size="small" type="info" effect="plain">{{ scope.row.type }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="endTime" label="处理时间" width="170" sortable />
            <el-table-column prop="result" label="结果" width="100">
              <template #default="scope">
                <el-tag :type="getResultType(scope.row.result)" effect="light" round>
                  {{ getResultLabel(scope.row.result) }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="100" fixed="right">
              <template #default>
                <el-button link type="primary">查看详情</el-button>
              </template>
            </el-table-column>
          </el-table>

           <div class="pagination-footer">
            <el-pagination background layout="total, prev, pager, next" :total="128" />
          </div>
        </el-tab-pane>
        
        <!-- Tab 2: My Initiated (Timeline View) -->
        <el-tab-pane label="我发起的" name="initiated">
          <div class="timeline-wrapper">
             <el-timeline>
                <el-timeline-item timestamp="2026-01-05" placement="top" type="primary">
                  <el-card shadow="hover" class="timeline-card">
                    <h4>IT设备采购申请_Admin <el-tag size="small" type="success" class="float-right">已完成</el-tag></h4>
                    <p>提交于 09:30，经由 张三、李四 审批通过。</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="2026-01-02" placement="top" color="#e6a23c">
                  <el-card shadow="hover" class="timeline-card">
                    <h4>部门团建经费申请 <el-tag size="small" type="warning" class="float-right">审批中</el-tag></h4>
                    <p>提交于 14:00，当前节点：财务总监审批。</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="2025-12-28" placement="top" color="#909399">
                   <p style="color:#909399; font-size:12px;">2025年记录已归档...</p>
                </el-timeline-item>
             </el-timeline>
          </div>
        </el-tab-pane>
      </el-tabs>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { Finished, Check, Close, Timer, Search, Download } from '@element-plus/icons-vue'
import axios from 'axios'

const activeTab = ref('processed')
const searchText = ref('')
const filterType = ref('')
const dateRange = ref([])
const loading = ref(false)

const historyTasks = ref([])

// 加载已办列表
const loadDoneList = async () => {
  loading.value = true
  try {
    const response = await axios.get('/api/workflow/done-list')
    if (response.data.code === 200) {
      historyTasks.value = response.data.data.map(item => ({
        id: item.flowId || 'WF' + Date.now(),
        title: item.title,
        initiator: item.initiatorName || '未知',
        avatar: `https://ui-avatars.com/api/?name=${encodeURIComponent(item.initiatorName || 'U')}&background=409eff&color=fff`,
        type: item.flowType || '通用流程',
        endTime: item.updateTime || new Date().toLocaleString(),
        result: mapResult(item.status)
      }))
    }
  } catch (error) {
    console.error('加载已办列表失败:', error)
    historyTasks.value = []
  } finally {
    loading.value = false
  }
}

const mapResult = (status) => {
  if (status === 1 || status === 'approved') return 'Pass'
  if (status === 2 || status === 'rejected') return 'Reject'
  return 'Pass'
}

const filteredHistory = computed(() => {
  if (!historyTasks.value) return []
  
  return historyTasks.value.filter(item => {
    const matchText = !searchText.value || 
                      (item.title && item.title.includes(searchText.value)) || 
                      (item.id && item.id.includes(searchText.value))
    const matchType = !filterType.value || item.type === filterType.value
    return matchText && matchType
  })
})

const getResultType = (res) => res === 'Pass' ? 'success' : 'danger'
const getResultLabel = (res) => res === 'Pass' ? '已通过' : '已驳回'

// 统计
const doneStats = computed(() => ({
  total: historyTasks.value.length,
  passed: historyTasks.value.filter(t => t.result === 'Pass').length,
  rejected: historyTasks.value.filter(t => t.result === 'Reject').length
}))

onMounted(() => {
  loadDoneList()
})
</script>

<style scoped>
.page-header { margin-bottom: 24px; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

/* Stats Cards */
.stat-card { border: none; }
:deep(.el-card__body) { display: flex; align-items: center; padding: 20px !important; }
.stat-icon { width: 48px; height: 48px; border-radius: 8px; display: flex; align-items: center; justify-content: center; margin-right: 16px; font-size: 24px; }
.bg-blue { background: linear-gradient(135deg, #409eff 0%, #79bbff 100%); box-shadow: 0 4px 10px rgba(64,158,255,0.3); }
.bg-green { background: linear-gradient(135deg, #67c23a 0%, #95d475 100%); box-shadow: 0 4px 10px rgba(103,194,58,0.3); }
.bg-red { background: linear-gradient(135deg, #f56c6c 0%, #fab6b6 100%); box-shadow: 0 4px 10px rgba(245,108,108,0.3); }
.bg-orange { background: linear-gradient(135deg, #e6a23c 0%, #f3d19e 100%); box-shadow: 0 4px 10px rgba(230,162,60,0.3); }

.stat-value { font-size: 24px; font-weight: 700; color: #303133; line-height: 1.2; }
.stat-label { font-size: 12px; color: #909399; margin-top: 4px; }

/* Table Toolbar */
.table-toolbar { display: flex; align-items: center; margin-bottom: 20px; flex-wrap: wrap; gap: 12px; }
.ml-auto { margin-left: auto; }
.mono-font { font-family: 'Roboto Mono', monospace; color: #606266; }
.task-title { font-weight: 500; color: #303133; }

/* Pagination */
.pagination-footer { margin-top: 20px; display: flex; justify-content: flex-end; }

/* Timeline */
.timeline-wrapper { padding: 20px 40px; max-width: 800px; }
.timeline-card h4 { margin: 0 0 8px 0; font-size: 16px; color: #303133; }
.timeline-card p { margin: 0; color: #606266; font-size: 14px; }
.float-right { float: right; }
</style>
