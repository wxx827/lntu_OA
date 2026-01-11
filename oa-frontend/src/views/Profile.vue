<template>
  <div class="fade-in">
    <el-row :gutter="24">
      <!-- Left: Profile Card -->
      <el-col :span="8">
        <el-card shadow="never" class="profile-card card-shadow text-center">
          <div class="avatar-section">
            <el-avatar :size="100" :src="`https://ui-avatars.com/api/?name=${currentUser.username}&background=409eff&color=fff&size=128`" class="main-avatar" />
            <div class="role-badge">{{ currentUser.role === 'Admin' ? '超级管理员' : '员工' }}</div>
          </div>
          <h2 class="user-name">{{ currentUser.username }}</h2>
          <p class="user-bio">技术部 / CTO</p>
          
          <div class="user-stats">
            <div class="stat-item">
              <div class="num">156</div>
              <div class="label">登录次数</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="num">12</div>
              <div class="label">发起流程</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="num">98%</div>
              <div class="label">考勤率</div>
            </div>
          </div>

          <el-divider />
          
          <div class="contact-info">
             <p><el-icon><Phone /></el-icon> 138****8888</p>
             <p><el-icon><Message /></el-icon> admin@company.com</p>
             <p><el-icon><Location /></el-icon> 总部大楼 505 室</p>
          </div>
        </el-card>
      </el-col>

      <!-- Right: Tabs (Edit & Activity) -->
      <el-col :span="16">
        <el-card shadow="never" class="card-shadow">
          <el-tabs v-model="activeTab">
            <el-tab-pane label="基本资料" name="info">
               <el-form label-position="top" class="mt-4">
                 <el-row :gutter="20">
                   <el-col :span="12">
                     <el-form-item label="用户名">
                       <el-input v-model="form.username" disabled />
                     </el-form-item>
                   </el-col>
                   <el-col :span="12">
                     <el-form-item label="真实姓名">
                       <el-input v-model="form.realName" />
                     </el-form-item>
                   </el-col>
                 </el-row>
                 <el-row :gutter="20">
                   <el-col :span="12">
                     <el-form-item label="手机号码">
                       <el-input v-model="form.phone" />
                     </el-form-item>
                   </el-col>
                   <el-col :span="12">
                     <el-form-item label="电子邮箱">
                       <el-input v-model="form.email" />
                     </el-form-item>
                   </el-col>
                 </el-row>
                 <el-form-item label="个人简介">
                   <el-input type="textarea" v-model="form.bio" :rows="3" />
                 </el-form-item>
                 <el-form-item>
                   <el-button type="primary">保存修改</el-button>
                 </el-form-item>
               </el-form>
            </el-tab-pane>

            <el-tab-pane label="安全设置" name="security">
              <el-form label-width="100px" class="mt-4" style="max-width: 500px">
                <el-form-item label="旧密码">
                  <el-input type="password" show-password />
                </el-form-item>
                <el-form-item label="新密码">
                  <el-input type="password" show-password />
                </el-form-item>
                <el-form-item label="确认密码">
                  <el-input type="password" show-password />
                </el-form-item>
                <el-form-item>
                  <el-button type="danger">修改密码</el-button>
                </el-form-item>
              </el-form>
            </el-tab-pane>

            <el-tab-pane label="操作日志" name="log">
              <el-timeline class="mt-4">
                <el-timeline-item timestamp="2026-01-05 15:30" placement="top">
                  <el-card shadow="never" class="log-card">
                    <h4>登录系统</h4>
                    <p>IP: 192.168.1.100 (公司内网)</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="2026-01-05 14:20" placement="top" color="#0bbd87">
                  <el-card shadow="never" class="log-card">
                    <h4>审批通过 - 采购申请 WF20260101</h4>
                    <p>操作人: {{ currentUser.username }}</p>
                  </el-card>
                </el-timeline-item>
                <el-timeline-item timestamp="2026-01-04 09:00" placement="top">
                   <el-card shadow="never" class="log-card">
                    <h4>更新个人资料</h4>
                   </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-tab-pane>
          </el-tabs>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { Phone, Message, Location } from '@element-plus/icons-vue'

// Current user info from localStorage
const currentUser = ref({
  username: localStorage.getItem('username') || 'User',
  role: localStorage.getItem('role') || 'Employee'
})

const activeTab = ref('info')

const form = reactive({
  username: currentUser.value.username,
  realName: '管理员',
  phone: '13800138000',
  email: 'admin@company.com',
  bio: '负责全公司技术架构与信息安全管理。'
})
</script>

<style scoped>
.text-center { text-align: center; }
.profile-card { padding-bottom: 20px; }
.avatar-section { position: relative; display: inline-block; margin-top: 20px; margin-bottom: 16px; }
.main-avatar { border: 4px solid #fff; box-shadow: 0 4px 12px rgba(0,0,0,0.1); }
.role-badge {
  position: absolute;
  bottom: 0;
  left: 50%;
  transform: translateX(-50%);
  background: #E6A23C;
  color: #fff;
  font-size: 12px;
  padding: 2px 10px;
  border-radius: 10px;
  white-space: nowrap;
}
.user-name { font-size: 24px; margin: 0 0 4px 0; color: #303133; }
.user-bio { color: #909399; font-size: 14px; margin-bottom: 24px; }

.user-stats {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 24px;
}
.stat-item { padding: 0 16px; }
.stat-divider { width: 1px; height: 24px; background: #e0e0e0; }
.stat-item .num { font-size: 20px; font-weight: 600; color: #303133; }
.stat-item .label { font-size: 12px; color: #909399; margin-top: 4px; }

.contact-info p {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #606266;
  font-size: 14px;
  margin: 12px 0;
}

.mt-4 { margin-top: 16px; }
.log-card { border: none; background: #f9fafc; }
.log-card h4 { margin: 0 0 4px 0; font-size: 14px; color: #303133; }
.log-card p { margin: 0; font-size: 12px; color: #909399; }
</style>
