<template>
  <el-menu
    active-text-color="#409EFF"
    background-color="#304156"
    text-color="#bfcbd9"
    router
    :default-active="activePath"
    class="sidebar-menu"
  >
    <div class="logo">
      <img src="@/assets/logo.png" alt="OA Logo" v-if="false" />
      <span>OA System</span>
    </div>

    <!-- 工作台 -->
    <el-menu-item index="/dashboard">
      <el-icon><DataBoard /></el-icon>
      <span>工作台</span>
    </el-menu-item>

    <!-- 考勤管理 -->
    <el-sub-menu index="/attendance">
      <template #title>
        <el-icon><Clock /></el-icon>
        <span>考勤管理</span>
      </template>
      <el-menu-item index="/dashboard/attendance/clock">考勤打卡</el-menu-item>
      <el-menu-item index="/dashboard/attendance/records">考勤记录</el-menu-item>
      <el-menu-item index="/dashboard/attendance/leave">请假申请</el-menu-item>
      <el-menu-item index="/dashboard/attendance/overtime">加班申请</el-menu-item>
      <el-menu-item index="/dashboard/attendance/trip">出差申请</el-menu-item>
      <el-menu-item index="/dashboard/attendance/stats">考勤统计</el-menu-item>
    </el-sub-menu>

    <!-- 工作流 -->
    <el-sub-menu index="/workflow">
      <template #title>
        <el-icon><CopyDocument /></el-icon>
        <span>流程审批</span>
      </template>
      <el-menu-item index="/dashboard/workflow/todo">我的待办</el-menu-item>
      <el-menu-item index="/dashboard/workflow/apply">发起申请</el-menu-item>
      <el-menu-item index="/dashboard/workflow/done">历史记录</el-menu-item>
    </el-sub-menu>
    
    <!-- 会议管理 -->
    <el-sub-menu index="/meeting">
      <template #title>
        <el-icon><Monitor /></el-icon>
        <span>会议协作</span>
      </template>
      <el-menu-item index="/dashboard/meeting/rooms">会议室列表</el-menu-item>
      <el-menu-item index="/dashboard/meeting/my">我的会议</el-menu-item>
    </el-sub-menu>

    <!-- 物资管理 -->
    <el-sub-menu index="/material">
      <template #title>
        <el-icon><Box /></el-icon>
        <span>资产物资</span>
      </template>
      <el-menu-item index="/dashboard/material/list">物资列表</el-menu-item>
      <el-menu-item index="/dashboard/material/my">我的申请</el-menu-item>
    </el-sub-menu>



    <!-- 行政管理 -->
    <el-sub-menu index="/administrative">
      <template #title>
        <el-icon><Van /></el-icon>
        <span>行政管理</span>
      </template>
      <el-menu-item index="/dashboard/administrative/cars">车辆管理</el-menu-item>
    </el-sub-menu>

    <!-- 日程管理 -->
    <el-menu-item index="/dashboard/schedule/calendar">
      <el-icon><Calendar /></el-icon>
      <span>日程管理</span>
    </el-menu-item>

    <!-- 人力资源 -->
    <el-sub-menu index="/hr">
      <template #title>
        <el-icon><UserFilled /></el-icon>
        <span>人力资源</span>
      </template>
      <el-menu-item index="/dashboard/hr/departments">部门管理</el-menu-item>
      <el-menu-item index="/dashboard/hr/employees">员工档案</el-menu-item>
      <el-menu-item index="/dashboard/hr/org-chart">组织架构</el-menu-item>
    </el-sub-menu>

    <!-- 财务管理 -->
    <el-sub-menu index="/finance">
      <template #title>
        <el-icon><Money /></el-icon>
        <span>财务管理</span>
      </template>
      <el-menu-item index="/dashboard/finance/claim">费用报销</el-menu-item>
      <el-menu-item index="/dashboard/finance/my-expenses">我的报销</el-menu-item>
    </el-sub-menu>

    <!-- 通讯录 -->
    <el-menu-item index="/dashboard/contacts">
      <el-icon><User /></el-icon>
      <span>通讯录</span>
    </el-menu-item>

    <!-- 系统管理 -->
    <el-sub-menu index="/system">
      <template #title>
        <el-icon><Setting /></el-icon>
        <span>系统设置</span>
      </template>
      <el-menu-item index="/dashboard/system/employees">员工管理</el-menu-item>
      <el-menu-item index="/dashboard/system/roles">角色权限</el-menu-item>
      <el-menu-item index="/dashboard/system/announcements">公告管理</el-menu-item>
    </el-sub-menu>
  </el-menu>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute } from 'vue-router'
import { 
  DataBoard, CopyDocument, Monitor, Box, 
  User, Setting, Calendar, Van, Clock, UserFilled, Money
} from '@element-plus/icons-vue'

const route = useRoute()
const activePath = computed(() => route.path)
</script>

<style scoped>
.sidebar-menu {
  height: 100vh;
  border-right: none;
}
.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
  font-size: 20px;
  font-weight: bold;
  background-color: #2b3648;
}
</style>
