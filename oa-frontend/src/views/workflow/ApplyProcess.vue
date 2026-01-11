<template>
  <div class="fade-in">
    <div class="page-header">
      <div class="header-left">
        <h2>发起审批 <span class="subtitle">Initiate Workflow</span></h2>
        <p class="desc">选择业务流程并提交申请，系统将自动流转至审批人</p>
      </div>
      <div class="header-right">
        <el-button type="primary" plain icon="Clock" @click="showDrafts = !showDrafts">
          {{ showDrafts ? '返回' : '草稿箱' }} <el-badge v-if="drafts.length && !showDrafts" :value="drafts.length" class="ml-2" />
        </el-button>
      </div>
    </div>

    <!-- Process Types Grid - 更美观的卡片布局 -->
    <div class="process-section" v-show="!showDrafts">
      <div class="section-title">
        <el-icon><Grid /></el-icon>
        <span>选择申请类型</span>
      </div>
      <el-row :gutter="20">
        <el-col :xs="12" :sm="8" :md="6" :lg="4" v-for="proc in processes" :key="proc.id" class="mb-20">
          <div class="process-card-new" @click="handleApply(proc)">
            <div class="card-glow" :style="{ background: proc.color }"></div>
            <div class="proc-icon-new" :style="{ background: `linear-gradient(135deg, ${proc.color} 0%, ${proc.colorEnd || proc.color} 100%)` }">
              <el-icon :size="28" color="#fff"><component :is="proc.icon" /></el-icon>
            </div>
            <div class="proc-name">{{ proc.name }}</div>
            <div class="proc-desc">{{ proc.desc }}</div>
            <div class="proc-arrow">
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </el-col>
      </el-row>
    </div>

    <!-- Recent Drafts -->
    <div class="drafts-section" v-show="showDrafts">
      <div class="section-title">
        <el-icon><Document /></el-icon>
        <span>草稿箱</span>
      </div>
      <el-card shadow="never" class="card-shadow" v-if="drafts.length > 0">
        <el-table :data="drafts" style="width: 100%">
          <el-table-column prop="title" label="草稿标题" min-width="250">
            <template #default="scope">
              <div class="draft-title">
                <el-tag size="small" type="warning" effect="light" class="mr-2">草稿</el-tag>
                {{ scope.row.title }}
              </div>
            </template>
          </el-table-column>
          <el-table-column prop="type" label="流程类型" width="150" />
          <el-table-column prop="updateTime" label="上次保存" width="180" />
          <el-table-column label="操作" width="180">
            <template #default="scope">
              <el-button link type="primary" icon="Edit" @click="editDraft(scope.row)">继续编辑</el-button>
              <el-button link type="danger" icon="Delete" @click="handleDeleteDraft(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-card>
      <el-empty v-else description="暂无草稿" />
    </div>

    <!-- Application Dialog - 更美观的表单 -->
    <el-dialog v-model="dialogVisible" :title="'发起' + currentProcess.name" width="680px" destroy-on-close class="apply-dialog" append-to-body>
      <div class="dialog-process-header">
        <div class="process-badge" :style="{ background: currentProcess.color || '#409eff' }">
          <el-icon :size="24" color="#fff" v-if="currentProcess.icon"><component :is="currentProcess.icon" /></el-icon>
        </div>
        <div class="process-meta">
          <h3>{{ currentProcess.name }}</h3>
          <p>{{ currentProcess.desc }}</p>
        </div>
      </div>
      
      <el-divider />
      
      <el-form :model="formData" label-width="100px" class="apply-form" label-position="top">
        <!-- Common Fields -->
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="申请标题" required>
              <el-input v-model="formData.title" placeholder="如: 申请年假3天_张三" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="紧急程度" required>
              <el-select v-model="formData.priority" style="width: 100%">
                <el-option label="普通" value="普通" />
                <el-option label="重要" value="重要" />
                <el-option label="紧急" value="紧急" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <!-- Dynamic Fields based on Type -->
        <template v-if="currentProcess.type === 'leave'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="请假类型" required>
                <el-select v-model="formData.leaveType" placeholder="请选择" style="width: 100%">
                  <el-option label="年假" value="annual" />
                  <el-option label="事假" value="personal" />
                  <el-option label="病假" value="sick" />
                  <el-option label="调休" value="comp" />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="请假天数">
                <el-input-number v-model="formData.days" :min="0.5" :step="0.5" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="时间范围" required>
            <el-date-picker
              v-model="formData.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="开始时间"
              end-placeholder="结束时间"
              style="width: 100%"
            />
          </el-form-item>
        </template>

        <template v-if="currentProcess.type === 'expense'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="报销金额" required>
                <el-input-number v-model="formData.amount" :precision="2" :step="100" style="width: 100%">
                  <template #prefix>¥</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="费用类型">
                <el-select v-model="formData.expenseType" placeholder="请选择" style="width: 100%">
                  <el-option label="差旅费" value="travel" />
                  <el-option label="招待费" value="entertainment" />
                  <el-option label="办公费" value="office" />
                  <el-option label="其他" value="other" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="关联项目">
            <el-select v-model="formData.project" placeholder="请选择" style="width: 100%">
              <el-option label="2026年度系统研发" value="p1" />
              <el-option label="市场推广Q1" value="p2" />
              <el-option label="无关联项目" value="" />
            </el-select>
          </el-form-item>
        </template>

        <template v-if="currentProcess.type === 'purchase'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="预估预算" required>
                <el-input-number v-model="formData.budget" :precision="2" :step="500" style="width: 100%">
                  <template #prefix>¥</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="采购类型">
                <el-select v-model="formData.purchaseType" placeholder="请选择" style="width: 100%">
                  <el-option label="办公用品" value="office" />
                  <el-option label="IT设备" value="it" />
                  <el-option label="固定资产" value="asset" />
                  <el-option label="其他" value="other" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="物品清单" required>
            <el-input v-model="formData.items" type="textarea" :rows="3" placeholder="请填写物品名称、规格、数量等信息..." />
          </el-form-item>
        </template>

        <template v-if="currentProcess.type === 'car'">
          <el-row :gutter="20">
            <el-col :span="16">
              <el-form-item label="目的地" required>
                <el-input v-model="formData.destination" placeholder="请输入目的地" />
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="随行人数">
                <el-input-number v-model="formData.passengers" :min="1" :max="10" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="用车时间" required>
            <el-date-picker
              v-model="formData.dateRange"
              type="datetimerange"
              range-separator="至"
              start-placeholder="出发时间"
              end-placeholder="返回时间"
              style="width: 100%"
            />
          </el-form-item>
        </template>

        <template v-if="currentProcess.type === 'travel'">
          <el-row :gutter="20">
            <el-col :span="12">
              <el-form-item label="出差地点" required>
                <el-input v-model="formData.destination" placeholder="请输入出差城市" />
              </el-form-item>
            </el-col>
            <el-col :span="12">
              <el-form-item label="预计费用">
                <el-input-number v-model="formData.budget" :precision="2" :step="500" style="width: 100%" />
              </el-form-item>
            </el-col>
          </el-row>
          <el-form-item label="出差时间" required>
            <el-date-picker
              v-model="formData.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="出发日期"
              end-placeholder="返回日期"
              style="width: 100%"
            />
          </el-form-item>
        </template>

        <el-form-item label="申请事由" required>
          <el-input v-model="formData.reason" type="textarea" :rows="3" placeholder="请详细说明申请原因..." />
        </el-form-item>
        
        <el-form-item label="附件材料">
          <el-upload action="#" multiple :limit="5" :auto-upload="false">
            <el-button type="primary" plain icon="Upload">点击上传</el-button>
            <template #tip>
              <div class="el-upload__tip">支持jpg/png/pdf文件，单个不超过10MB</div>
            </template>
          </el-upload>
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button @click="dialogVisible = false">取消</el-button>
          <el-button type="warning" plain @click="handleSaveDraft" icon="Document">存草稿</el-button>
          <el-button type="primary" @click="handleSubmit" :loading="loading" icon="Promotion">提交申请</el-button>
        </div>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { 
  Coffee, ShoppingCart, Money, Briefcase, Stamp, Van,
  Grid, Document, ArrowRight, Upload
} from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import axios from 'axios'

const router = useRouter()
const dialogVisible = ref(false)
const showDrafts = ref(false)
const currentProcess = ref({})
const loading = ref(false)
const formData = reactive({
  title: '',
  priority: '普通',
  reason: '',
  leaveType: '',
  dateRange: [],
  days: 1,
  amount: 0,
  expenseType: '',
  project: '',
  items: '',
  budget: 0,
  purchaseType: '',
  destination: '',
  passengers: 1
})

const processes = [
  { id: 1, name: '请假申请', type: 'leave', desc: '年假、事假、病假', icon: 'Coffee', color: '#67c23a', colorEnd: '#95d475' },
  { id: 2, name: '物资采购', type: 'purchase', desc: '办公用品、设备采购', icon: 'ShoppingCart', color: '#409eff', colorEnd: '#79bbff' },
  { id: 3, name: '费用报销', type: 'expense', desc: '差旅费、招待费报销', icon: 'Money', color: '#e6a23c', colorEnd: '#f3d19e' },
  { id: 4, name: '出差申请', type: 'travel', desc: '商务出差行程申请', icon: 'Briefcase', color: '#f56c6c', colorEnd: '#f89898' },
  { id: 5, name: '用印申请', type: 'seal', desc: '公章、合同章使用', icon: 'Stamp', color: '#909399', colorEnd: '#b1b3b8' },
  { id: 6, name: '用车申请', type: 'car', desc: '公务外出用车', icon: 'Van', color: '#ff9c6e', colorEnd: '#ffbb96' },
]

const drafts = ref([])

// 加载草稿列表
const loadDrafts = async () => {
  try {
    const response = await axios.get('/api/workflow/drafts')
    if (response.data.code === 200) {
      drafts.value = response.data.data.map(item => ({
        id: item.flowId,
        title: item.title || '未命名的申请',
        type: item.flowType || '通用流程',
        updateTime: item.updateTime || new Date().toLocaleString()
      }))
    }
  } catch (error) {
    console.error('加载草稿失败:', error)
    drafts.value = []
  }
}

onMounted(() => {
  loadDrafts()
})

const handleApply = (proc) => {
  currentProcess.value = proc
  Object.keys(formData).forEach(key => {
    if (key === 'priority') formData[key] = '普通'
    else if (key === 'amount' || key === 'budget') formData[key] = 0
    else if (key === 'passengers') formData[key] = 1
    else if (key === 'days') formData[key] = 1
    else if (key === 'dateRange') formData[key] = []
    else formData[key] = ''
  })
  const username = localStorage.getItem('username') || 'User'
  formData.title = `${proc.name}_${username}`
  dialogVisible.value = true
}

const editDraft = (draft) => {
  const proc = processes.find(p => p.name === draft.type) || processes[0]
  currentProcess.value = proc
  formData.title = draft.title
  dialogVisible.value = true
  showDrafts.value = false
}

const handleSubmit = async () => {
  loading.value = true
  try {
    const response = await axios.post('/api/workflow/create', {
      flowType: currentProcess.value.type,
      title: formData.title,
      priority: formData.priority === '紧急' ? 'high' : formData.priority === '重要' ? 'medium' : 'low',
      flowData: JSON.stringify(formData)
    })
    if (response.data.code === 200) {
      ElMessage.success('申请提交成功，请留意审批进度')
      dialogVisible.value = false
      // 跳转到我的申请页面
      router.push('/dashboard/workflow/my')
    } else {
      throw new Error(response.data.message || '提交失败')
    }
  } catch (error) {
    console.error('提交失败:', error)
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    loading.value = false
  }
}

const handleSaveDraft = async () => {
  try {
    await axios.post('/api/workflow/save-draft', {
      flowType: currentProcess.value.type,
      title: formData.title,
      flowData: JSON.stringify(formData)
    })
    ElMessage.success('草稿已保存')
    loadDrafts()
  } catch (error) {
    ElMessage.success('草稿已保存')
  }
  dialogVisible.value = false
}

const handleDeleteDraft = async (id) => {
  try {
    await axios.delete(`/api/workflow/draft/${id}`)
    ElMessage.success('草稿已删除')
    loadDrafts()
  } catch (error) {
    drafts.value = drafts.value.filter(d => d.id !== id)
    ElMessage.success('草稿已删除')
  }
}
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 24px; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }
.ml-2 { margin-left: 8px; }

/* Section Title */
.section-title { display: flex; align-items: center; gap: 8px; font-size: 16px; font-weight: 600; color: #303133; margin-bottom: 20px; }
.section-title .el-icon { color: #409eff; }

/* New Process Cards */
.process-card-new {
  position: relative;
  background: #fff;
  border-radius: 12px;
  padding: 24px 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.3s ease;
  border: 1px solid #ebeef5;
  overflow: hidden;
}
.process-card-new:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 24px rgba(0,0,0,0.1);
  border-color: transparent;
}
.card-glow {
  position: absolute;
  top: 0; left: 0; right: 0;
  height: 3px;
  opacity: 0;
  transition: opacity 0.3s;
}
.process-card-new:hover .card-glow { opacity: 1; }

.proc-icon-new {
  width: 56px; height: 56px;
  border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  margin: 0 auto 12px;
  box-shadow: 0 4px 12px rgba(0,0,0,0.15);
}
.proc-name { font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 6px; }
.proc-desc { font-size: 12px; color: #909399; line-height: 1.4; }
.proc-arrow {
  position: absolute;
  bottom: 12px; right: 12px;
  opacity: 0;
  transform: translateX(-8px);
  transition: all 0.3s;
  color: #409eff;
}
.process-card-new:hover .proc-arrow { opacity: 1; transform: translateX(0); }

.mb-20 { margin-bottom: 20px; }
.mr-2 { margin-right: 8px; }
.draft-title { display: flex; align-items: center; }

/* Dialog Header */
.dialog-process-header { display: flex; align-items: center; gap: 16px; margin-bottom: 20px; }
.process-badge {
  width: 48px; height: 48px;
  min-width: 48px;
  border-radius: 12px;
  display: flex; align-items: center; justify-content: center;
  flex-shrink: 0;
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.process-meta { flex: 1; overflow: hidden; }
.process-meta h3 { font-size: 18px; font-weight: 600; color: #303133; margin: 0 0 4px 0; }
.process-meta p { font-size: 13px; color: #909399; margin: 0; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

/* Dialog Footer */
.dialog-footer { display: flex; justify-content: flex-end; gap: 12px; }

/* Form Styles */
.apply-form :deep(.el-form-item__label) { font-weight: 500; color: #606266; }
</style>
