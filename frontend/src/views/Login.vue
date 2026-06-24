<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-card">
        <div class="login-header">
          <div class="logo">
            <div class="logo-icon">
              <el-icon :size="32"><School /></el-icon>
            </div>
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
          <div class="illustration-icon">
            <el-icon :size="80"><Promotion /></el-icon>
          </div>
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
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  padding: var(--space-6);
}

.login-wrapper {
  display: flex;
  background-color: var(--color-card-bg);
  border-radius: var(--radius-lg);
  box-shadow: var(--elevation-3);
  overflow: hidden;
  max-width: 900px;
  width: 100%;
}

.login-card {
  flex: 1;
  padding: var(--space-10);
}

.login-header {
  text-align: center;
  margin-bottom: var(--space-6);
}

.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-2);
  margin-bottom: var(--space-3);
}

.logo-icon {
  color: var(--color-primary);
}

.logo-text {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
}

.login-header h2 {
  color: var(--color-text-1);
  margin-bottom: var(--space-2);
}

.login-header p {
  color: var(--color-text-2);
}

.login-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.form-group label {
  font-weight: var(--font-medium);
  color: var(--color-text-2);
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.remember-me label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
}

.forgot-password {
  color: var(--color-primary);
  text-decoration: none;
}

.forgot-password:hover {
  text-decoration: underline;
}

.btn {
  padding: var(--space-3) var(--space-5);
  border-radius: var(--radius);
  font-weight: var(--font-medium);
  transition: all var(--t-normal);
  border: none;
  cursor: pointer;
}

.btn-primary {
  background-color: var(--color-primary);
  color: var(--color-text-inverse);
}

.btn-primary:hover {
  background-color: var(--color-primary-hover);
}

.btn-block {
  width: 100%;
}

.login-footer {
  text-align: center;
  margin-top: var(--space-6);
}

.login-footer p {
  color: var(--color-text-2);
}

.login-footer a {
  color: var(--color-primary);
  text-decoration: none;
}

.login-footer a:hover {
  text-decoration: underline;
}

.login-illustration {
  flex: 1;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: var(--space-10);
}

.illustration-content {
  text-align: center;
  color: var(--color-text-inverse);
}

.illustration-icon {
  color: var(--color-text-inverse);
  margin-bottom: var(--space-5);
}

.illustration-content h3 {
  font-size: var(--text-xl);
  margin-bottom: var(--space-2);
}

.illustration-content p {
  opacity: 0.9;
}

@media (max-width: 768px) {
  .login-wrapper {
    flex-direction: column;
  }

  .login-illustration {
    order: -1;
    padding: var(--space-6);
  }

  .login-card {
    padding: var(--space-6);
  }
}
</style>
