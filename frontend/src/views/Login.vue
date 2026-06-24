<template>
  <div class="login-container">
    <div class="gradient-bg">
      <div class="gradient-orb orb-1"></div>
      <div class="gradient-orb orb-2"></div>
      <div class="gradient-orb orb-3"></div>
      <div class="grid-overlay"></div>
    </div>

    <div class="login-content">
      <div class="brand-section">
        <div class="brand-badge">
          <el-icon :size="24"><School /></el-icon>
          <span>大学生就业服务平台</span>
        </div>
        <h1 class="brand-title">
          开启你的<br/>
          <span class="gradient-text">职业新篇章</span>
        </h1>
        <p class="brand-desc">
          专业测评 · 职业规划 · 学习资源 · 精准岗位推荐
        </p>
        <div class="feature-list">
          <div class="feature-item">
            <div class="feature-icon"><el-icon><Compass /></el-icon></div>
            <span>智能职业测评</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon"><el-icon><TrendCharts /></el-icon></div>
            <span>个性化成长路径</span>
          </div>
          <div class="feature-item">
            <div class="feature-icon"><el-icon><Briefcase /></el-icon></div>
            <span>海量优质岗位</span>
          </div>
        </div>
      </div>

      <div class="login-card-wrapper">
        <div class="login-card">
          <div class="login-header">
            <h2>欢迎回来</h2>
            <p>登录账号，继续你的求职之旅</p>
          </div>

          <form @submit.prevent="handleLogin" class="login-form">
            <div class="form-group">
              <label for="username">用户名</label>
              <el-input
                id="username"
                v-model="form.username"
                placeholder="请输入用户名"
                size="large"
                :prefix-icon="User"
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
                size="large"
                :prefix-icon="Lock"
                show-password
                :rules="[{ required: true, message: '请输入密码' }]"
              />
            </div>

            <div class="form-group remember-me">
              <label class="checkbox-label">
                <input type="checkbox" v-model="rememberMe" />
                <span class="checkmark"></span>
                <span>记住我</span>
              </label>
              <a href="#" class="forgot-password">忘记密码?</a>
            </div>

            <button
              type="submit"
              class="btn btn-primary btn-block"
              :disabled="loading"
              :class="{ loading }"
            >
              <span v-if="loading" class="spinner"></span>
              <span>{{ loading ? '登录中...' : '立即登录' }}</span>
            </button>
          </form>

          <div class="login-divider">
            <span>还没有账号？</span>
          </div>

          <router-link to="/register" class="btn btn-outline btn-block">
            注册新账号
          </router-link>
        </div>

        <p class="copyright">© 2024 大学生就业服务平台 · 让求职更简单</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { School, User, Lock, Compass, TrendCharts, Briefcase } from '@element-plus/icons-vue'
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
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
  background: #0F172A;
  padding: var(--space-6);
}

.gradient-bg {
  position: fixed;
  inset: 0;
  z-index: 0;
  background:
    radial-gradient(ellipse at 20% 20%, rgba(37, 99, 235, 0.25) 0%, transparent 50%),
    radial-gradient(ellipse at 80% 80%, rgba(124, 58, 237, 0.2) 0%, transparent 50%),
    radial-gradient(ellipse at 50% 50%, rgba(15, 23, 42, 1) 0%, #020617 100%);
}

.gradient-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(60px);
  opacity: 0.5;
  animation: float-orb 20s ease-in-out infinite;
}

.orb-1 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.4) 0%, transparent 70%);
  top: -150px;
  right: -100px;
  animation-delay: 0s;
}

.orb-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(124, 58, 237, 0.35) 0%, transparent 70%);
  bottom: -100px;
  left: -80px;
  animation-delay: -7s;
}

.orb-3 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, rgba(6, 182, 212, 0.25) 0%, transparent 70%);
  top: 40%;
  right: 20%;
  animation-delay: -14s;
}

@keyframes float-orb {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(40px, -40px) scale(1.05); }
  50% { transform: translate(-30px, 20px) scale(0.95); }
  75% { transform: translate(20px, 40px) scale(1.02); }
}

.grid-overlay {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(255, 255, 255, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(255, 255, 255, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: radial-gradient(circle at 50% 50%, black 0%, transparent 70%);
  -webkit-mask-image: radial-gradient(circle at 50% 50%, black 0%, transparent 70%);
}

.login-content {
  position: relative;
  z-index: 1;
  display: grid;
  grid-template-columns: 1fr 440px;
  gap: var(--space-16);
  max-width: 1200px;
  width: 100%;
  align-items: center;
  animation: fade-in-up 0.8s var(--ease-smooth) forwards;
}

@keyframes fade-in-up {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.brand-section {
  color: var(--color-text-inverse);
}

.brand-badge {
  display: inline-flex;
  align-items: center;
  gap: var(--space-2);
  padding: var(--space-2) var(--space-4);
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: var(--radius-full);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  margin-bottom: var(--space-8);
  backdrop-filter: blur(10px);
}

.brand-title {
  font-size: var(--text-4xl);
  font-weight: var(--font-extrabold);
  line-height: 1.1;
  letter-spacing: -0.03em;
  margin-bottom: var(--space-5);
}

.gradient-text {
  background: linear-gradient(135deg, #60A5FA 0%, #A78BFA 50%, #22D3EE 100%);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.brand-desc {
  font-size: var(--text-lg);
  color: rgba(255, 255, 255, 0.7);
  margin-bottom: var(--space-10);
  line-height: 1.6;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.feature-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  font-size: var(--text-base);
  color: rgba(255, 255, 255, 0.85);
}

.feature-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius);
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  display: flex;
  align-items: center;
  justify-content: center;
  color: #60A5FA;
}

.login-card-wrapper {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.login-card {
  background: rgba(255, 255, 255, 0.06);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border: 1px solid rgba(255, 255, 255, 0.12);
  border-radius: var(--radius-xl);
  padding: var(--space-10);
  box-shadow: 0 24px 80px rgba(0, 0, 0, 0.3);
  animation: fade-in-up 0.8s var(--ease-smooth) 0.15s forwards;
  opacity: 0;
}

.login-header {
  text-align: center;
  margin-bottom: var(--space-8);
}

.login-header h2 {
  color: var(--color-text-inverse);
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  margin-bottom: var(--space-2);
  letter-spacing: -0.02em;
}

.login-header p {
  color: rgba(255, 255, 255, 0.6);
  font-size: var(--text-sm);
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
  color: rgba(255, 255, 255, 0.8);
  font-size: var(--text-sm);
}

.login-form :deep(.el-input__wrapper) {
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.12);
  box-shadow: none;
  border-radius: var(--radius);
  padding: 4px 16px;
  transition: all var(--t-normal) var(--ease-default);
}

.login-form :deep(.el-input__wrapper:hover) {
  background: rgba(255, 255, 255, 0.12);
  border-color: rgba(255, 255, 255, 0.2);
}

.login-form :deep(.el-input__wrapper.is-focus) {
  background: rgba(255, 255, 255, 0.1);
  border-color: rgba(96, 165, 250, 0.5);
  box-shadow: 0 0 0 3px rgba(96, 165, 250, 0.15);
}

.login-form :deep(.el-input__inner) {
  color: var(--color-text-inverse);
  font-size: var(--text-base);
  height: 44px;
}

.login-form :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.4);
}

.login-form :deep(.el-input__icon) {
  color: rgba(255, 255, 255, 0.5);
}

.remember-me {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-direction: row;
}

.checkbox-label {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  cursor: pointer;
  color: rgba(255, 255, 255, 0.7);
  font-size: var(--text-sm);
}

.checkbox-label input {
  position: absolute;
  opacity: 0;
  width: 0;
  height: 0;
}

.checkmark {
  width: 18px;
  height: 18px;
  border-radius: 5px;
  background: rgba(255, 255, 255, 0.08);
  border: 1px solid rgba(255, 255, 255, 0.2);
  display: flex;
  align-items: center;
  justify-content: center;
  transition: all var(--t-fast) var(--ease-default);
}

.checkbox-label input:checked + .checkmark {
  background: var(--color-primary);
  border-color: var(--color-primary);
}

.checkbox-label input:checked + .checkmark::after {
  content: '';
  width: 5px;
  height: 9px;
  border: solid white;
  border-width: 0 2px 2px 0;
  transform: rotate(45deg);
  margin-bottom: 2px;
}

.forgot-password {
  color: #60A5FA;
  text-decoration: none;
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  transition: color var(--t-fast);
}

.forgot-password:hover {
  color: #93C5FD;
  text-decoration: underline;
}

.btn {
  padding: var(--space-3) var(--space-5);
  border-radius: var(--radius);
  font-weight: var(--font-semibold);
  transition: all var(--t-normal) var(--ease-smooth);
  border: none;
  cursor: pointer;
  text-align: center;
  text-decoration: none;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  gap: var(--space-2);
  font-size: var(--text-base);
}

.btn-primary {
  background: var(--gradient-primary);
  color: var(--color-text-inverse);
  box-shadow: 0 8px 24px rgba(37, 99, 235, 0.35);
}

.btn-primary:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(37, 99, 235, 0.45);
}

.btn-primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.btn-outline {
  background: transparent;
  color: rgba(255, 255, 255, 0.9);
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.btn-outline:hover {
  background: rgba(255, 255, 255, 0.08);
  border-color: rgba(255, 255, 255, 0.3);
}

.btn-block {
  width: 100%;
  height: 48px;
}

.btn.loading {
  position: relative;
}

.spinner {
  width: 18px;
  height: 18px;
  border: 2px solid rgba(255, 255, 255, 0.3);
  border-top-color: white;
  border-radius: 50%;
  animation: spin 0.8s linear infinite;
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.login-divider {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin: var(--space-5) 0;
  color: rgba(255, 255, 255, 0.4);
  font-size: var(--text-sm);
}

.login-divider::before,
.login-divider::after {
  content: '';
  flex: 1;
  height: 1px;
  background: rgba(255, 255, 255, 0.1);
}

.copyright {
  text-align: center;
  color: rgba(255, 255, 255, 0.4);
  font-size: var(--text-xs);
}

@media (max-width: 1024px) {
  .login-content {
    grid-template-columns: 1fr;
    gap: var(--space-8);
    max-width: 480px;
  }

  .brand-section {
    text-align: center;
  }

  .brand-badge {
    margin-bottom: var(--space-5);
  }

  .brand-title {
    font-size: var(--text-3xl);
  }

  .brand-desc {
    margin-bottom: var(--space-6);
  }

  .feature-list {
    align-items: center;
  }

  .feature-item {
    justify-content: center;
  }
}

@media (max-width: 640px) {
  .login-container {
    padding: var(--space-4);
  }

  .login-card {
    padding: var(--space-6);
  }

  .brand-title {
    font-size: var(--text-2xl);
  }

  .brand-desc {
    font-size: var(--text-base);
  }
}
</style>
