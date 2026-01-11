<template>
  <div class="fade-in">
    <!-- Page Header -->
    <div class="page-header">
      <div class="header-left">
        <h2>报销申请 <span class="subtitle">Expense Claim</span></h2>
        <p class="desc">提交费用报销申请，快速审批流转</p>
      </div>
      <div class="header-right">
        <el-button type="primary" icon="Document" @click="$router.push('/dashboard/finance/my-expenses')">我的报销</el-button>
      </div>
    </div>

    <!-- Stats Cards - 更紧凑的设计 -->
    <el-row :gutter="16" class="mb-20">
      <el-col :span="6" v-for="stat in statCards" :key="stat.label">
        <div class="stat-card-new" :class="stat.class">
          <div class="stat-icon-new">
            <el-icon :size="22"><component :is="stat.icon" /></el-icon>
          </div>
          <div class="stat-content">
            <div class="stat-value-new">{{ stat.value }}</div>
            <div class="stat-label-new">{{ stat.label }}</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Main Form Card - 更美观的表单 -->
    <el-card shadow="never" class="card-shadow form-card">
      <template #header>
        <div class="card-header">
          <div class="header-title">
            <el-icon class="header-icon"><EditPen /></el-icon>
            <span>新建报销申请</span>
          </div>
          <el-tag type="info" effect="plain" size="small">请如实填写报销信息</el-tag>
        </div>
      </template>

      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="expense-form">
        <div class="form-section">
          <div class="section-label">基本信息</div>
          <el-row :gutter="24">
            <el-col :span="8">
              <el-form-item label="费用类型" prop="expenseTypeId">
                <el-select v-model="form.expenseTypeId" placeholder="请选择费用类型" style="width: 100%">
                  <el-option
                    v-for="type in expenseTypes"
                    :key="type.typeId"
                    :label="type.typeName"
                    :value="type.typeId"
                  />
                </el-select>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="报销总金额" prop="totalAmount">
                <el-input-number v-model="form.totalAmount" :min="0" :precision="2" :step="100" style="width: 100%">
                  <template #prefix>¥</template>
                </el-input-number>
              </el-form-item>
            </el-col>
            <el-col :span="8">
              <el-form-item label="关联项目">
                <el-select v-model="form.projectId" placeholder="选择项目(可选)" style="width: 100%" clearable>
                  <el-option label="2026年度系统研发" value="p1" />
                  <el-option label="市场推广Q1" value="p2" />
                </el-select>
              </el-form-item>
            </el-col>
          </el-row>
          
          <el-form-item label="报销说明" prop="description">
            <el-input
              v-model="form.description"
              type="textarea"
              :rows="3"
              placeholder="请详细说明报销事由，如：出差目的、招待对象等..."
            />
          </el-form-item>
        </div>

        <el-divider>
          <el-icon><Tickets /></el-icon>
          <span style="margin-left: 8px;">发票明细</span>
        </el-divider>

        <div class="invoices-section">
          <div v-for="(invoice, index) in invoices" :key="index" class="invoice-item-new">
            <div class="invoice-header-new">
              <span class="invoice-num">发票 {{ index + 1 }}</span>
              <el-button v-if="invoices.length > 1" link type="danger" icon="Delete" @click="removeInvoice(index)">删除</el-button>
            </div>
            <el-row :gutter="16">
              <el-col :span="6">
                <el-form-item label="发票号码">
                  <el-input v-model="invoice.invoiceNo" placeholder="发票号码" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="发票类型">
                  <el-select v-model="invoice.invoiceType" style="width: 100%">
                    <el-option label="增值税专用发票" value="VAT_SPECIAL" />
                    <el-option label="增值税普通发票" value="VAT_ORDINARY" />
                    <el-option label="收据" value="RECEIPT" />
                    <el-option label="其他" value="OTHER" />
                  </el-select>
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="发票金额">
                  <el-input-number v-model="invoice.amount" :min="0" :precision="2" style="width: 100%" />
                </el-form-item>
              </el-col>
              <el-col :span="6">
                <el-form-item label="开票日期">
                  <el-date-picker v-model="invoice.invoiceDate" type="date" placeholder="选择日期" value-format="YYYY-MM-DD" style="width: 100%" />
                </el-form-item>
              </el-col>
            </el-row>
          </div>

          <el-button type="primary" plain icon="Plus" @click="addInvoice" class="add-invoice-btn">
            添加发票
          </el-button>
        </div>

        <div class="form-actions">
          <el-button size="large" @click="resetForm">重置</el-button>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting" icon="Promotion">
            提交申请
          </el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, Money, Timer, CircleCheck, CircleClose, Tickets, EditPen } from '@element-plus/icons-vue'
import axios from 'axios'

const formRef = ref(null)
const expenseTypes = ref([])
const submitting = ref(false)

const form = ref({
  userId: 1,
  expenseTypeId: null,
  totalAmount: 0,
  description: '',
  projectId: ''
})

const invoices = ref([{
  invoiceNo: '',
  invoiceType: 'VAT_ORDINARY',
  amount: 0,
  invoiceDate: ''
}])

const rules = {
  expenseTypeId: [{ required: true, message: '请选择费用类型', trigger: 'change' }],
  totalAmount: [{ required: true, message: '请输入总金额', trigger: 'blur' }],
  description: [{ required: true, message: '请输入报销说明', trigger: 'blur' }]
}

// Stats data
const stats = ref({ totalAmount: '¥0', pending: 0, approved: 0, rejected: 0 })

const statCards = computed(() => [
  { label: '本月报销', value: stats.value.totalAmount, icon: 'Money', class: 'stat-blue' },
  { label: '待审批', value: stats.value.pending, icon: 'Timer', class: 'stat-orange' },
  { label: '已批准', value: stats.value.approved, icon: 'CircleCheck', class: 'stat-green' },
  { label: '已拒绝', value: stats.value.rejected, icon: 'CircleClose', class: 'stat-red' }
])

const loadExpenseTypes = async () => {
  try {
    const response = await axios.get('/api/finance/expense-types')
    if (response.data.success) {
      expenseTypes.value = response.data.data
    } else {
      expenseTypes.value = [
        { typeId: 1, typeName: '差旅费' },
        { typeId: 2, typeName: '招待费' },
        { typeId: 3, typeName: '办公费' },
        { typeId: 4, typeName: '其他' }
      ]
    }
  } catch (error) {
    console.error('加载费用类型失败:', error)
    expenseTypes.value = [
      { typeId: 1, typeName: '差旅费' },
      { typeId: 2, typeName: '招待费' },
      { typeId: 3, typeName: '办公费' }
    ]
  }
}

const loadStats = async () => {
  try {
    const response = await axios.get('/api/finance/expense/stats')
    if (response.data.success) {
      stats.value = response.data.data
    }
  } catch (error) {
    console.error('加载统计失败:', error)
  }
}

const addInvoice = () => {
  invoices.value.push({
    invoiceNo: '',
    invoiceType: 'VAT_ORDINARY',
    amount: 0,
    invoiceDate: ''
  })
}

const removeInvoice = (index) => {
  if (invoices.value.length > 1) {
    invoices.value.splice(index, 1)
  } else {
    ElMessage.warning('至少保留一张发票')
  }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate()
  if (!valid) return

  submitting.value = true
  try {
    const response = await axios.post('/api/finance/expense/submit', {
      claim: form.value,
      invoices: invoices.value
    })
    
    if (response.data.success) {
      ElMessage.success('报销申请提交成功')
      resetForm()
    } else {
      ElMessage.error(response.data.message || '提交失败')
    }
  } catch (error) {
    ElMessage.error('提交失败，请稍后重试')
  } finally {
    submitting.value = false
  }
}

const resetForm = () => {
  formRef.value?.resetFields()
  invoices.value = [{
    invoiceNo: '',
    invoiceType: 'VAT_ORDINARY',
    amount: 0,
    invoiceDate: ''
  }]
}

onMounted(() => {
  loadExpenseTypes()
  loadStats()
})
</script>

<style scoped>
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.header-left h2 { font-size: 24px; font-weight: 600; color: #303133; margin: 0 0 8px 0; }
.subtitle { font-size: 14px; color: #909399; margin-left: 12px; text-transform: uppercase; }
.desc { color: #606266; font-size: 14px; margin: 0; }

/* Stats Cards - New Design */
.mb-20 { margin-bottom: 20px; }
.stat-card-new {
  display: flex; align-items: center; gap: 14px;
  background: #fff; border-radius: 10px; padding: 16px 18px;
  border: 1px solid #ebeef5; transition: all 0.3s;
}
.stat-card-new:hover { transform: translateY(-3px); box-shadow: 0 6px 16px rgba(0,0,0,0.08); }
.stat-icon-new { width: 44px; height: 44px; border-radius: 10px; display: flex; align-items: center; justify-content: center; }
.stat-blue .stat-icon-new { background: linear-gradient(135deg, #409eff, #79bbff); color: #fff; }
.stat-orange .stat-icon-new { background: linear-gradient(135deg, #e6a23c, #f3d19e); color: #fff; }
.stat-green .stat-icon-new { background: linear-gradient(135deg, #67c23a, #95d475); color: #fff; }
.stat-red .stat-icon-new { background: linear-gradient(135deg, #f56c6c, #f89898); color: #fff; }
.stat-value-new { font-size: 20px; font-weight: 700; color: #303133; }
.stat-label-new { font-size: 12px; color: #909399; margin-top: 2px; }

/* Form Card */
.form-card { border-radius: 12px; }
.card-header { display: flex; justify-content: space-between; align-items: center; }
.header-title { display: flex; align-items: center; gap: 8px; font-weight: 600; font-size: 16px; color: #303133; }
.header-icon { color: #409eff; }

/* Form Sections */
.form-section { margin-bottom: 8px; }
.section-label { font-size: 13px; font-weight: 600; color: #606266; margin-bottom: 16px; padding-left: 8px; border-left: 3px solid #409eff; }
.expense-form :deep(.el-form-item__label) { font-weight: 500; color: #606266; }

/* Invoice Items */
.invoices-section { background: #fafafa; border-radius: 8px; padding: 16px; }
.invoice-item-new { background: #fff; border-radius: 8px; padding: 16px; margin-bottom: 12px; border: 1px solid #ebeef5; }
.invoice-header-new { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.invoice-num { font-weight: 600; color: #409eff; font-size: 14px; }
.add-invoice-btn { width: 100%; border-style: dashed; }

/* Form Actions */
.form-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px; padding-top: 20px; border-top: 1px solid #ebeef5; }
</style>
