<template>
  <div class="register-container">
    <div class="register-wrapper">
      <div class="register-illustration">
        <div class="illustration-content">
          <el-icon class="illustration-icon"><Star /></el-icon>
          <h3>创建账号</h3>
          <p>开启您的职业发展之旅</p>
        </div>
      </div>

      <div class="register-card">
        <div class="register-header">
          <div class="logo">
            <el-icon class="logo-icon"><School /></el-icon>
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

          <el-button
            type="primary"
            native-type="submit"
            class="btn-block"
            :loading="loading"
          >
            注册
          </el-button>
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
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  padding: var(--space-8);
}

.register-wrapper {
  display: flex;
  background-color: var(--color-card-bg);
  border-radius: var(--radius-lg);
  box-shadow: var(--elevation-3);
  overflow: hidden;
  max-width: 900px;
  width: 100%;
}

.register-illustration {
  flex: 1;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  display: flex;
  justify-content: center;
  align-items: center;
  padding: var(--space-12);
}

.illustration-content {
  text-align: center;
  color: var(--color-text-inverse);
}

.illustration-icon {
  font-size: calc(var(--space-12) + var(--space-8));
  margin-bottom: var(--space-6);
}

.illustration-content h3 {
  font-size: var(--text-2xl);
  margin-bottom: var(--space-2);
}

.illustration-content p {
  opacity: 0.9;
}

.register-card {
  flex: 1;
  padding: var(--space-12);
}

.register-header {
  text-align: center;
  margin-bottom: var(--space-8);
}

.logo {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}

.logo-icon {
  font-size: var(--space-8);
  color: var(--color-primary);
}

.logo-text {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--color-primary);
}

.register-header h2 {
  color: var(--color-text-1);
  margin-bottom: var(--space-2);
}

.register-header p {
  color: var(--color-text-2);
}

.register-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-4);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.form-group label {
  font-weight: var(--font-medium);
  color: var(--color-text-1);
}

.agree-terms {
  display: flex;
  align-items: flex-start;
  gap: var(--space-2);
}

.agree-terms label {
  cursor: pointer;
  font-weight: normal;
  font-size: var(--text-sm);
  color: var(--color-text-2);
}

.agree-terms a {
  color: var(--color-primary);
  text-decoration: none;
}

.agree-terms a:hover {
  text-decoration: underline;
}

.btn-block {
  width: 100%;
}

.register-footer {
  text-align: center;
  margin-top: var(--space-8);
}

.register-footer p {
  color: var(--color-text-2);
}

.register-footer a {
  color: var(--color-primary);
  text-decoration: none;
}

.register-footer a:hover {
  text-decoration: underline;
}

@media (max-width: 768px) {
  .register-container {
    padding: var(--space-4);
  }

  .register-wrapper {
    flex-direction: column;
  }

  .register-illustration {
    padding: var(--space-8);
  }

  .register-card {
    padding: var(--space-8);
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
