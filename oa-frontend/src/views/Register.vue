<template>
  <div class="login-wrapper">
    <div class="login-left">
      <div class="brand">
        <el-icon :size="48" color="#fff"><Monitor /></el-icon>
        <h1>OA 协同办公平台</h1>
        <p>Enterprise Collaboration System</p>
      </div>
      <div class="illustration">
        <div class="circle c1"></div>
        <div class="circle c2"></div>
      </div>
    </div>
    
    <div class="login-right">
      <div class="form-container fade-in">
        <h2>注册账号</h2>
        <p class="subtitle">创建一个新账号以访问工作台</p>
        
        <el-form :model="form" ref="formRef" :rules="rules" size="large" class="mt-8">
          <el-form-item prop="username">
            <el-input 
              v-model="form.username" 
              placeholder="请输入用户名/账号" 
              :prefix-icon="User"
            />
          </el-form-item>
          
          <el-form-item prop="password">
            <el-input 
              v-model="form.password" 
              type="password" 
              placeholder="请输入密码" 
              :prefix-icon="Lock"
              show-password
            />
          </el-form-item>

          <el-form-item prop="role">
            <el-select v-model="form.role" placeholder="请选择角色" style="width: 100%">
              <el-option label="普通员工" value="EMPLOYEE" />
              <el-option label="实习生" value="INTERN" />
              <el-option label="管理员" value="ADMIN" />
            </el-select>
          </el-form-item>

          <el-form-item prop="email">
            <el-input 
              v-model="form.email" 
              placeholder="请输入邮箱" 
              :prefix-icon="Message"
            />
          </el-form-item>

          <el-form-item prop="tel">
            <el-input 
              v-model="form.tel" 
              placeholder="请输入电话" 
              :prefix-icon="Iphone"
            />
          </el-form-item>

          <el-button type="primary" class="login-btn" @click="handleRegister" :loading="loading">
            立即注册
          </el-button>
          
          <div class="login-link">
            已有账号？<router-link to="/login">去登录</router-link>
          </div>
        </el-form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { User, Lock, Monitor, Message, Iphone } from '@element-plus/icons-vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'

const router = useRouter()
const loading = ref(false)
const formRef = ref(null)

const form = reactive({
  username: '',
  password: '',
  role: 'EMPLOYEE',
  email: '',
  tel: '',
  sex: 'Male' // Default
})

const rules = {
  username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }],
  role: [{ required: true, message: '请选择角色', trigger: 'change' }]
}

const handleRegister = async () => {
    if (!formRef.value) return
    await formRef.value.validate(async (valid) => {
        if (valid) {
            loading.value = true
            try {
                // Call backend API
                const response = await fetch('/api/auth/register', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(form)
                })
                const res = await response.json()
                
                if (res.code === 200) {
                    ElMessage.success('注册成功，请登录')
                    router.push('/login')
                } else {
                    ElMessage.error(res.message || '注册失败')
                }
            } catch (err) {
                ElMessage.error('网络请求失败')
            } finally {
                loading.value = false
            }
        }
    })
}
</script>

<style scoped>
/* Reuse styles from Login.vue */
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

.form-container h2 { font-size: 32px; color: #303133; margin-bottom: 12px; font-weight: 600; }
.subtitle { color: #909399; margin-bottom: 40px; font-size: 16px; }

.mt-8 { margin-top: 32px; }
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
</style>
