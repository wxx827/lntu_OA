<template>
  <div class="my-expenses">
    <el-card>
      <template #header>
        <span>我的报销记录</span>
      </template>

      <el-table :data="expenses" stripe>
        <el-table-column prop="claimNo" label="报销单号" width="180" />
        <el-table-column prop="expenseTypeName" label="费用类型" width="120" />
        <el-table-column prop="totalAmount" label="金额(元)" width="120">
          <template #default="{ row }">
            ¥{{ row.totalAmount.toFixed(2) }}
          </template>
        </el-table-column>
        <el-table-column prop="claimDate" label="申请日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.claimDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="审批状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="paymentStatus" label="支付状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getPaymentStatusType(row.paymentStatus)">
              {{ getPaymentStatusText(row.paymentStatus) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="说明" show-overflow-tooltip />
        <el-table-column label="操作" width="120" fixed="right">
          <template #default="{ row }">
            <el-button link type="primary" @click="viewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <!-- 详情对话框 -->
    <el-dialog v-model="showDetailDialog" title="报销详情" width="700px">
      <el-descriptions v-if="currentExpense" :column="2" border>
        <el-descriptions-item label="报销单号">{{ currentExpense.claimNo }}</el-descriptions-item>
        <el-descriptions-item label="费用类型">{{ currentExpense.expenseTypeName }}</el-descriptions-item>
        <el-descriptions-item label="总金额">¥{{ currentExpense.totalAmount.toFixed(2) }}</el-descriptions-item>
        <el-descriptions-item label="申请日期">{{ formatDate(currentExpense.claimDate) }}</el-descriptions-item>
        <el-descriptions-item label="审批状态">
          <el-tag :type="getStatusType(currentExpense.status)">
            {{ getStatusText(currentExpense.status) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="支付状态">
          <el-tag :type="getPaymentStatusType(currentExpense.paymentStatus)">
            {{ getPaymentStatusText(currentExpense.paymentStatus) }}
          </el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="报销说明" :span="2">
          {{ currentExpense.description }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentExpense.approverName" label="审批人">
          {{ currentExpense.approverName }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentExpense.approveTime" label="审批时间">
          {{ formatDateTime(currentExpense.approveTime) }}
        </el-descriptions-item>
        <el-descriptions-item v-if="currentExpense.approveRemark" label="审批意见" :span="2">
          {{ currentExpense.approveRemark }}
        </el-descriptions-item>
      </el-descriptions>

      <!-- 发票列表 -->
      <div v-if="currentExpense && currentExpense.invoices && currentExpense.invoices.length > 0" style="margin-top: 20px;">
        <h4>发票信息</h4>
        <el-table :data="currentExpense.invoices" stripe>
          <el-table-column prop="invoiceNo" label="发票号码" />
          <el-table-column prop="invoiceType" label="类型">
            <template #default="{ row }">
              {{ getInvoiceTypeText(row.invoiceType) }}
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额">
            <template #default="{ row }">
              ¥{{ row.amount.toFixed(2) }}
            </template>
          </el-table-column>
          <el-table-column prop="companyName" label="开票单位" />
        </el-table>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import axios from 'axios'

const expenses = ref([])
const showDetailDialog = ref(false)
const currentExpense = ref(null)
const userId = ref(1) // TODO: 从登录信息获取

const loadExpenses = async () => {
  try {
    const response = await axios.get('/api/finance/expense/my-claims', {
      params: { userId: userId.value }
    })
    if (response.data.success) {
      expenses.value = response.data.data
    }
  } catch (error) {
    console.error('加载报销记录失败:', error)
  }
}

const viewDetail = async (row) => {
  try {
    const response = await axios.get(`/api/finance/expense/detail/${row.claimId}`)
    if (response.data.success) {
      currentExpense.value = response.data.data
      showDetailDialog.value = true
    }
  } catch (error) {
    console.error('加载详情失败:', error)
  }
}

const formatDate = (date) => {
  if (!date) return '-'
  return new Date(date).toLocaleDateString('zh-CN')
}

const formatDateTime = (dateTime) => {
  if (!dateTime) return '-'
  return new Date(dateTime).toLocaleString('zh-CN')
}

const getStatusType = (status) => {
  const map = {
    'PENDING': 'warning',
    'APPROVED': 'success',
    'REJECTED': 'danger',
    'CANCELLED': 'info'
  }
  return map[status] || 'info'
}

const getStatusText = (status) => {
  const map = {
    'PENDING': '待审批',
    'APPROVED': '已批准',
    'REJECTED': '已拒绝',
    'CANCELLED': '已取消'
  }
  return map[status] || status
}

const getPaymentStatusType = (status) => {
  const map = {
    'UNPAID': 'warning',
    'PAID': 'success',
    'PROCESSING': 'primary'
  }
  return map[status] || 'info'
}

const getPaymentStatusText = (status) => {
  const map = {
    'UNPAID': '未支付',
    'PAID': '已支付',
    'PROCESSING': '支付中'
  }
  return map[status] || status
}

const getInvoiceTypeText = (type) => {
  const map = {
    'VAT_SPECIAL': '增值税专用发票',
    'VAT_ORDINARY': '增值税普通发票',
    'RECEIPT': '收据',
    'OTHER': '其他'
  }
  return map[type] || type
}

onMounted(() => {
  loadExpenses()
})
</script>

<style scoped>
.my-expenses {
  padding: 20px;
}
</style>
