<template>
  <div class="report-dashboard">
    <!-- Header -->
    <div class="dashboard-header mb-4">
      <div class="header-left">
        <h2>数据决策中心</h2>
        <span class="subtitle">实时监控企业运营核心指标</span>
      </div>
      <div class="header-right">
        <el-date-picker 
          v-model="dateRange" 
          type="daterange" 
          range-separator="至" 
          start-placeholder="开始日期" 
          end-placeholder="结束日期"
          size="default"
          class="mr-2"
        />
        <el-button type="primary" :icon="Download">导出报表</el-button>
      </div>
    </div>

    <!-- Top Statistics Cards -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="4" v-for="(card, index) in statCards" :key="index">
        <div class="glass-card stat-card" :class="'stat-' + index">
          <div class="stat-icon-wrapper">
             <el-icon :size="24"><component :is="card.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">{{ card.label }}</div>
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-trend" :class="card.trend >= 0 ? 'up' : 'down'">
              <el-icon><component :is="card.trend >= 0 ? 'Top' : 'Bottom'" /></el-icon>
              {{ Math.abs(card.trend * 100).toFixed(1) }}%
            </div>
          </div>
        </div>
      </el-col>
    </el-row>

    <!-- Main Charts Row -->
    <el-row :gutter="20" class="mb-4">
      <el-col :span="16">
        <div class="glass-card chart-card">
          <div class="card-title">
            <span>出勤与效率分析</span>
            <el-tag size="small" effect="dark">本周趋势</el-tag>
          </div>
          <div ref="attendanceChartRef" class="echart-container-lg"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="glass-card chart-card">
          <div class="card-title">
            <span>团队能力雷达</span>
          </div>
          <div ref="projectChartRef" class="echart-container-lg"></div>
        </div>
      </el-col>
    </el-row>

    <!-- Bottom Charts Row -->
    <el-row :gutter="20">
      <el-col :span="8">
        <div class="glass-card chart-card">
          <div class="card-title">部门人员分布</div>
          <div ref="departmentChartRef" class="echart-container-md"></div>
        </div>
      </el-col>
      <el-col :span="8">
        <div class="glass-card chart-card">
          <div class="card-title">业务流程漏斗</div>
          <div ref="funnelChartRef" class="echart-container-md"></div>
        </div>
      </el-col>
       <el-col :span="8">
        <div class="glass-card chart-card">
          <div class="card-title">部门绩效考核</div>
          <div ref="performanceChartRef" class="echart-container-md"></div>
        </div>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted, nextTick } from 'vue'
import { User, Tickets, FolderOpened, Money, Top, Bottom, Download, Star, TrendCharts } from '@element-plus/icons-vue'
import * as echarts from 'echarts'
import axios from 'axios'

const dateRange = ref([])
const attendanceChartRef = ref(null)
const departmentChartRef = ref(null)
const projectChartRef = ref(null)
const funnelChartRef = ref(null)
const performanceChartRef = ref(null)

let showCharts = [] // Store chart instances

const statCards = ref([
  { label: '总员工数', value: 0, icon: 'User', trend: 0 },
  { label: '活跃项目', value: 0, icon: 'FolderOpened', trend: 0 },
  { label: '待办任务', value: 0, icon: 'Tickets', trend: 0 },
  { label: '本月营收', value: '¥0', icon: 'TrendCharts', trend: 0 },
  { label: '本月支出', value: '¥0', icon: 'Money', trend: 0 },
  { label: '客户满意度', value: '0', icon: 'Star', trend: 0 },
])

const resizeCharts = () => {
  showCharts.forEach(c => c && c.resize())
}

const loadData = async () => {
  try {
    // 1. Overview
    const overviewRes = await axios.get('http://localhost:8080/oa-backend/api/report/overview')
    if (overviewRes.data.success) {
      const d = overviewRes.data.data
      statCards.value = [
        { label: '总员工数', value: d.totalEmployees, icon: 'User', trend: d.employeeGrowth },
        { label: '活跃项目', value: d.activeProjects, icon: 'FolderOpened', trend: d.projectGrowth },
        { label: '待办任务', value: d.pendingTasks, icon: 'Tickets', trend: d.taskGrowth },
        { label: '本月营收', value: `¥${(d.revenue/10000).toFixed(1)}w`, icon: 'TrendCharts', trend: d.revenueGrowth },
        { label: '本月支出', value: `¥${(d.monthlyExpenses/10000).toFixed(1)}w`, icon: 'Money', trend: d.expenseGrowth },
        { label: '客户满意度', value: d.clientSatisfaction, icon: 'Star', trend: d.satisfactionGrowth },
      ]
    }

    // 2. Attendance/Efficiency (Line + Bar)
    const attRes = await axios.get('http://localhost:8080/oa-backend/api/report/chart/attendance')
    if (attRes.data.success) {
        const d = attRes.data.data
        const chart = echarts.init(attendanceChartRef.value)
        showCharts.push(chart)
        chart.setOption({
            tooltip: { trigger: 'axis' },
            legend: { top: 0 },
            grid: { left: '3%', right: '3%', bottom: '5%', containLabel: true },
            xAxis: { type: 'category', data: d.xAxis },
            yAxis: [
                { type: 'value', name: '人数', min: 0, max: 160 },
                { type: 'value', name: '效率%', min: 0, max: 100 }
            ],
            series: d.series.map(s => {
                if(s.type === 'line') s.yAxisIndex = 1
                return s
            }),
            color: ['#409eff', '#e6a23c', '#f56c6c', '#67c23a']
        })
    }

    // 3. Project Radar
    const projRes = await axios.get('http://localhost:8080/oa-backend/api/report/chart/project-status')
    if (projRes.data.success) {
        const d = projRes.data.data
        const chart = echarts.init(projectChartRef.value)
        showCharts.push(chart)
        chart.setOption({
            tooltip: {},
            legend: { bottom: 0 },
            radar: {
                indicator: d.indicator,
                shape: 'circle',
                splitArea: { areaStyle: { color: ['rgba(255,255,255,0.1)', 'rgba(255,255,255,0.2)'], shadowColor: 'rgba(0, 0, 0, 0.2)', shadowBlur: 10 } }
            },
            series: [{
                type: 'radar',
                data: [
                    { value: d.teamA, name: 'Team A (研发一组)', areaStyle: { opacity: 0.3 } },
                    { value: d.teamB, name: 'Team B (研发二组)', areaStyle: { opacity: 0.3 } }
                ]
            }],
            color: ['#5470c6', '#91cc75']
        })
    }
    
    // 4. Department Pie
    const deptRes = await axios.get('http://localhost:8080/oa-backend/api/report/chart/department')
    if (deptRes.data.success) {
        const d = deptRes.data.data
        const chart = echarts.init(departmentChartRef.value)
        showCharts.push(chart)
        chart.setOption({
            tooltip: { trigger: 'item' },
            legend: { orient: 'vertical', left: 'left', bottom: 'bottom' },
            series: [
                {
                    name: '部门分布',
                    type: 'pie',
                    radius: ['40%', '70%'],
                    center: ['60%', '50%'],
                    itemStyle: { borderRadius: 8, borderColor: '#fff', borderWidth: 2 },
                    data: d
                }
            ]
        })
    }
    
    // 5. Funnel
    const funnelRes = await axios.get('http://localhost:8080/oa-backend/api/report/chart/funnel')
    if (funnelRes.data.success) {
        const d = funnelRes.data.data
        const chart = echarts.init(funnelChartRef.value)
        showCharts.push(chart)
        chart.setOption({
            tooltip: { trigger: 'item' },
            series: [
                {
                    name: 'Funnel',
                    type: 'funnel',
                    left: '10%',
                    top: 20,
                    bottom: 20,
                    width: '80%',
                    sort: 'descending',
                    gap: 2,
                    label: { show: true, position: 'inside' },
                    itemStyle: { borderColor: '#fff', borderWidth: 1 },
                    data: d
                }
            ],
            color: ['#5470c6', '#91cc75', '#fac858', '#ee6666', '#73c0de']
        })
    }
    
    // 6. Performance Bar Race (simplified as horiz bar)
    const perfRes = await axios.get('http://localhost:8080/oa-backend/api/report/chart/performance')
    if (perfRes.data.success) {
        const d = perfRes.data.data
        const chart = echarts.init(performanceChartRef.value)
        showCharts.push(chart)
        chart.setOption({
             tooltip: { trigger: 'axis', axisPointer: { type: 'shadow' } },
             legend: {},
             grid: { left: '3%', right: '4%', bottom: '3%', containLabel: true },
             xAxis: { type: 'value', boundaryGap: [0, 0.01] },
             yAxis: { type: 'category', data: d.yAxis },
             series: d.series.map(s => ({ ...s, type: 'bar' })),
             color: ['#fac858', '#ee6666']
        })
    }

  } catch (err) {
    console.error(err)
  }
}

onMounted(async () => {
  await nextTick()
  loadData()
  window.addEventListener('resize', resizeCharts)
})

onUnmounted(() => {
  window.removeEventListener('resize', resizeCharts)
  showCharts.forEach(c => c && c.dispose())
})
</script>

<style scoped>
.report-dashboard {
  background: #f0f2f5;
  min-height: 100vh;
  padding: 20px;
}
.mb-4 { margin-bottom: 24px; }
.mr-2 { margin-right: 12px; }

/* Dashboard Header */
.dashboard-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}
.header-left h2 { margin: 0; font-size: 24px; color: #1f2f3d; }
.header-left .subtitle { color: #909399; font-size: 14px; }
.header-right { display: flex; align-items: center; }

/* Glass Cards */
.glass-card {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(10px);
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  border: 1px solid rgba(255, 255, 255, 0.3);
  transition: transform 0.3s;
}
.glass-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
}

/* Stat Cards */
.stat-card {
  padding: 20px;
  display: flex;
  align-items: center;
}

.stat-icon-wrapper {
  width: 48px;
  height: 48px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 12px;
  color: #fff;
  font-weight: bold;
}

.stat-0 .stat-icon-wrapper { background: linear-gradient(135deg, #FF6B6B, #FF8787); box-shadow: 0 4px 10px rgba(255, 107, 107, 0.3); }
.stat-1 .stat-icon-wrapper { background: linear-gradient(135deg, #4D96FF, #6BCB77); box-shadow: 0 4px 10px rgba(77, 150, 255, 0.3); }
.stat-2 .stat-icon-wrapper { background: linear-gradient(135deg, #FFD93D, #F6C000); box-shadow: 0 4px 10px rgba(255, 217, 61, 0.3); }
.stat-3 .stat-icon-wrapper { background: linear-gradient(135deg, #6BCB77, #4D96FF); box-shadow: 0 4px 10px rgba(107, 203, 119, 0.3); }
.stat-4 .stat-icon-wrapper { background: linear-gradient(135deg, #FFC75F, #FF9671); box-shadow: 0 4px 10px rgba(255, 199, 95, 0.3); }
.stat-5 .stat-icon-wrapper { background: linear-gradient(135deg, #845EC2, #D65DB1); box-shadow: 0 4px 10px rgba(132, 94, 194, 0.3); }

.stat-info { flex: 1; }
.stat-label { font-size: 12px; color: #909399; margin-bottom: 4px; }
.stat-value { font-size: 20px; font-weight: 700; color: #2c3e50; line-height: 1.2; }
.stat-trend { font-size: 12px; margin-top: 4px; display: flex; align-items: center; font-weight: 500; }
.stat-trend.up { color: #f56c6c; }
.stat-trend.down { color: #67c23a; }

/* Chart Cards */
.chart-card { padding: 20px; height: 100%; }
.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #2c3e50;
  margin-bottom: 16px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.echart-container-lg { height: 380px; width: 100%; }
.echart-container-md { height: 300px; width: 100%; }
</style>
