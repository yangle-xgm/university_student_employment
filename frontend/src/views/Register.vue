<template>
  <div class="register-container">
    <div class="register-wrapper">
      <div class="register-illustration">
        <div class="illustration-content">
          <span class="illustration-icon">✨</span>
          <h3>创建账号</h3>
          <p>开启您的职业发展之旅</p>
        </div>
      </div>

      <div class="register-card">
        <div class="register-header">
          <div class="logo">
            <span class="logo-icon">🎓</span>
            <span class="logo-text">大学生就业服务平台</span>
          </div>
          <h2>注册账号</h2>
          <p>请填写以下信息完成注册</p>
        </div>

        <form @submit.prevent="handleRegister" class="register-form">
          <div class="form-row">
            <div class="form-group">
              <label for="username">用户名</label>
              <el-input
                id="username"
                v-model="form.username"
                placeholder="请输入用户名"
              />
            </div>
            <div class="form-group">
              <label for="email">邮箱</label>
              <el-input
                id="email"
                v-model="form.email"
                placeholder="请输入邮箱"
              />
            </div>
          </div>

          <div class="form-row">
            <div class="form-group">
              <label for="password">密码</label>
              <el-input
                id="password"
                type="password"
                v-model="form.password"
                placeholder="请输入密码"
              />
            </div>
            <div class="form-group">
              <label for="confirmPassword">确认密码</label>
              <el-input
                id="confirmPassword"
                type="password"
                v-model="form.confirmPassword"
                placeholder="请确认密码"
              />
            </div>
          </div>

          <div class="form-group">
            <label for="phone">手机号</label>
            <el-input
              id="phone"
              v-model="form.phone"
              placeholder="请输入手机号"
            />
          </div>

          <div class="form-group agree-terms">
            <label>
              <input type="checkbox" v-model="agreeTerms" />
              <span>我已阅读并同意<a href="#">服务条款</a>和<a href="#">隐私政策</a></span>
            </label>
          </div>

          <button type="submit" class="btn btn-primary btn-block" :loading="loading">
            注册
          </button>
        </form>

        <div class="register-footer">
          <p>已有账号? <router-link to="/login">立即登录</router-link></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { register } from '@/api/auth'

const form = reactive({
  username: '',
  email: '',
  password: '',
  confirmPassword: '',
  phone: ''
})

const agreeTerms = ref(false)
const loading = ref(false)

const handleRegister = async () => {
  if (!form.username || !form.email || !form.password || !form.confirmPassword) {
    return
  }

  if (form.password !== form.confirmPassword) {
    return
  }

  if (!agreeTerms.value) {
    return
  }

  loading.value = true
  try {
    await register(form)
    window.location.href = '/login'
  } catch (error) {
    console.error('注册失败:', error)
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.register-container {
  min-height: 100vh;
  display: flex;
  justify-content: center;
  align-items: center;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  padding: 2rem;
}

.register-wrapper {
  display: flex;
  background-color: white;
  border-radius: 16px;
  box-shadow: 0 10px 40px rgba(0, 0, 0, 0.15);
  overflow: hidden;
  max-width: 900px;
  width: 100%;
}

.register-illustration {
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

.register-card {
  flex: 1;
  padding: 3rem;
}

.register-header {
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

.register-header h2 {
  color: #333;
  margin-bottom: 0.5rem;
}

.register-header p {
  color: #666;
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem;
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

.agree-terms {
  display: flex;
  align-items: flex-start;
  gap: 0.5rem;
}

.agree-terms label {
  cursor: pointer;
  font-weight: normal;
  font-size: 0.9rem;
  color: #666;
}

.agree-terms a {
  color: #4a69bd;
  text-decoration: none;
}

.agree-terms a:hover {
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

.register-footer {
  text-align: center;
  margin-top: 2rem;
}

.register-footer p {
  color: #666;
}

.register-footer a {
  color: #4a69bd;
  text-decoration: none;
}

.register-footer a:hover {
  text-decoration: underline;
}
</style>
