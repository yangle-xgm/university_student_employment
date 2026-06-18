<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <span class="logo-icon">🎓</span>
            <span class="logo-text">大学生就业服务平台</span>
          </div>
          <h2>欢迎登录</h2>
          <p>请输入您的账号信息</p>
        </div>

        <form @submit.prevent="handleLogin" class="login-form">
          <div class="form-group">
            <label for="username">用户名</label>
            <el-input
              id="username"
              v-model="form.username"
              placeholder="请输入用户名"
              :rules="[{ required: true, message: '请输入用户名' }]"
            />
          </div>

          <div class="form-group">
            <label for="password">密码</label>
            <el-input
              id="password"
              type="password"
              v-model="form.password"
              placeholder="请输入密码"
              :rules="[{ required: true, message: '请输入密码' }]"
            />
          </div>

          <div class="form-group remember-me">
            <label>
              <input type="checkbox" v-model="rememberMe" />
              <span>记住我</span>
            </label>
            <a href="#" class="forgot-password">忘记密码?</a>
          </div>

          <button type="submit" class="btn btn-primary btn-block" :loading="loading">
            登录
          </button>
        </form>

        <div class="login-footer">
          <p>还没有账号? <router-link to="/register">立即注册</router-link></p>
        </div>
      </div>

      <div class="login-illustration">
        <div class="illustration-content">
          <span class="illustration-icon">🚀</span>
          <h3>开启职业之旅</h3>
          <p>专业测评、职业规划、学习资源</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { login } from '@/api/auth'

const form = reactive({
  username: '',
  password: ''
})

const rememberMe = ref(false)
const loading = ref(false)

const handleLogin = async () => {
  if (!form.username || !form.password) {
    return
  }

  loading.value = true
  try {
    const response = await login(form)
    localStorage.setItem('token', response.token)
    localStorage.setItem('userInfo', JSON.stringify(response))
    window.location.href = '/dashboard'
  } catch (error) {
    console.error('登录失败:', error)
    const errorMsg = error?.response?.data?.message || error?.message || '登录失败，请检查用户名和密码'
    ElMessage.error(errorMsg)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  padding: 2rem;
}

.login-wrapper {
  display: flex;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  max-width: 900px;
  width: 100%;
}

.login-card {
  flex: 1;
  padding: 3rem;
}

.login-header {
  text-align: center;
  margin-bottom: 2rem;
}

.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.logo-icon {
  font-size: 2rem;
}

.logo-text {
  font-size: 1.2rem;
  font-weight: bold;
  color: #4a69bd;
}

.login-header h2 {
  color: #333;
  margin-bottom: 0.5rem;
}

.login-header p {
  color: #666;
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 500;
  color: #333;
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remember-me label {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  cursor: pointer;
}

.forgot-password {
  color: #4a69bd;
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.btn {
  padding: 0.8rem 1.5rem;
  border-radius: 8px;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
}

.btn-primary {
  background-color: #4a69bd;
  color: white;
}

.btn-primary:hover {
  background-color: #3d5a9e;
}

.btn-block {
  width: 100%;
}

.login-footer {
  text-align: center;
  margin-top: 2rem;
}

.login-footer p {
  color: #666;
}

.login-footer a {
  color: #4a69bd;
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}

.login-illustration {
  flex: 1;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 3rem;
}

.illustration-content {
  text-align: center;
  color: white;
}

.illustration-icon {
  font-size: 5rem;
  margin-bottom: 1.5rem;
}

.illustration-content h3 {
  font-size: 1.5rem;
  margin-bottom: 0.5rem;
}

.illustration-content p {
  opacity: 0.9;
}
</style>
