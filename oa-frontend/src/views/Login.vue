<template>
  <div class="login-wrapper">
    <div class="login-left">
      <div class="brand">
        <el-icon :size="48" color="#fff"><Monitor /></el-icon>
        <h1>OA 协同办公平台</h1>
        <p>Enterprise Collaboration System</p>
      </div>
      <div class="illustration">
        <!-- CSS Illustration Circle -->
        <div class="circle c1"></div>
        <div class="circle c2"></div>
      </div>
    </div>
    
    <div class="login-right">
      <div class="form-container fade-in">
        <h2>欢迎登录</h2>
        <p class="subtitle">通过账号密码访问您的工作台</p>
        
        <el-form :model="form" size="large" class="mt-8">
          <el-form-item>
            <el-input 
              v-model="form.username" 
              placeholder="请输入账号" 
              :prefix-icon="User"
            />
          </el-form-item>
          <el-form-item>
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码" 
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>
          
          <div class="flex-row">
            <el-checkbox v-model="remember">记住我</el-checkbox>
            <el-button link type="primary">忘记密码?</el-button>
          </div>

          <el-button type="primary" class="login-btn" @click="handleLogin" :loading="loading">
            立即登录
          </el-button>
          
          <div class="login-link">
            还没有账号？<router-link to="/register">立即注册</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { User, Lock, Monitor } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const remember = ref(true)
const form = reactive({
  username: 'admin',
  password: ''
})

const handleLogin = async () => {
    loading.value = true
    try {
        const response = await fetch('/api/auth/login', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(form)
        })
        const res = await response.json()
        
        if (res.code === 200) {
            localStorage.setItem('token', res.data.token)
            localStorage.setItem('role', res.data.role)
            localStorage.setItem('username', res.data.username)
            ElMessage.success('登录成功，欢迎回来')
            router.push('/dashboard')
        } else {
            ElMessage.error(res.message || '登录失败')
        }
    } catch (err) {
        ElMessage.error('网络请求失败')
    } finally {
        loading.value = false
    }
}
</script>

<style scoped>
.login-wrapper {
  display: flex;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
  background: #f5f7fb;
}

.login-left {
  width: 50%;
  background: linear-gradient(135deg, #005bea 0%, #00c6fb 100%);
  position: relative;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  color: #fff;
  padding: 40px;
}

.brand {
  position: relative;
  z-index: 2;
  text-align: center;
  animation: slideUp 0.8s ease-out;
}
.brand h1 { margin: 24px 0 16px; font-size: 42px; font-weight: 700; letter-spacing: 2px; text-shadow: 0 4px 10px rgba(0,0,0,0.1); }
.brand p { opacity: 0.9; font-weight: 300; font-size: 18px; letter-spacing: 1px; }

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255,255,255,0.1);
  backdrop-filter: blur(10px);
}
.c1 { width: 500px; height: 500px; top: -150px; left: -150px; }
.c2 { width: 400px; height: 400px; bottom: -50px; right: -50px; }

.login-right {
  width: 50%;
  background: #f5f7fb;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  padding: 24px;
}

.form-container {
  width: 92vw;
  max-width: 420px;
  padding: 48px;
  background: #fff;
  border-radius: 18px;
  border: 1px solid #ebeef5;
  box-shadow: 0 18px 50px rgba(15, 38, 85, 0.12);
  box-sizing: border-box;
}
/* On very large screens, give formatting */
@media (min-width: 1920px) {
  .form-container { transform: scale(1.1); }
}
@media (max-width: 1024px) {
  .login-wrapper { flex-direction: column; }
  .login-left, .login-right { width: 100%; }
  .login-left { min-height: 38vh; }
  .login-right { padding: 16px; }
}
@media (max-width: 640px) {
  .login-left { display: none; }
  .form-container { padding: 32px 24px; }
}

.form-container h2 { font-size: 32px; color: #303133; margin-bottom: 12px; font-weight: 600; }
.subtitle { color: #909399; margin-bottom: 40px; font-size: 16px; }

.mt-8 { margin-top: 32px; }
.flex-row { display: flex; justify-content: space-between; align-items: center; margin-bottom: 32px; }
.login-btn { width: 100%; font-weight: 600; height: 48px; font-size: 16px; border-radius: 8px; box-shadow: 0 4px 12px rgba(64,158,255,0.3); }
.login-btn:hover { transform: translateY(-1px); box-shadow: 0 6px 16px rgba(64,158,255,0.4); }

.login-link { margin-top: 16px; text-align: center; font-size: 14px; }
.login-link a { color: #409eff; text-decoration: none; }

:deep(.el-input__wrapper) {
  background-color: #f7f9fc;
  box-shadow: none !important;
  border: 1px solid transparent;
  padding: 8px 12px;
  border-radius: 8px;
  transition: all 0.3s;
}
:deep(.el-input__wrapper:hover), :deep(.el-input__wrapper.is-focus) {
  background-color: #fff;
  border-color: #409eff;
  box-shadow: 0 0 0 3px rgba(64,158,255,0.1) !important;
}
:deep(.el-input__inner) { height: 32px; }

@keyframes slideUp {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}
</style>
