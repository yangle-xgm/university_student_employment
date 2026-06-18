<template>
  <div class="dashboard-container">
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="logo">
          <span class="logo-icon">🎓</span>
          <span class="logo-text">就业服务平台</span>
        </div>
      </div>
      <nav class="sidebar-nav">
        <router-link to="/dashboard" class="nav-item active">
          <span class="nav-icon">📊</span>
          <span class="nav-text">仪表盘</span>
        </router-link>
        <router-link to="/profile" class="nav-item">
          <span class="nav-icon">👤</span>
          <span class="nav-text">个人资料</span>
        </router-link>
        <router-link to="/assessments" class="nav-item">
          <span class="nav-icon">📝</span>
          <span class="nav-text">职业测评</span>
        </router-link>
        <router-link to="/career" class="nav-item">
          <span class="nav-icon">🎯</span>
          <span class="nav-text">职业规划</span>
        </router-link>
        <router-link to="/learning" class="nav-item">
          <span class="nav-icon">📚</span>
          <span class="nav-text">学习成长</span>
        </router-link>
        <router-link to="/interviews" class="nav-item">
          <span class="nav-icon">💼</span>
          <span class="nav-text">面试管理</span>
        </router-link>
        <router-link to="/jobs" class="nav-item">
          <span class="nav-icon">💻</span>
          <span class="nav-text">职位搜索</span>
        </router-link>
        <router-link to="/resumes" class="nav-item">
          <span class="nav-icon">📄</span>
          <span class="nav-text">简历管理</span>
        </router-link>
      </nav>
      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <span>退出登录</span>
        </button>
      </div>
    </aside>

    <main class="main-content">
      <header class="top-header">
        <h1>欢迎回来，{{ userInfo?.username }}</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="stats-grid">
        <div class="stat-card">
          <div class="stat-icon">📊</div>
          <div class="stat-content">
            <div class="stat-value">{{ assessmentCount }}</div>
            <div class="stat-label">已完成测评</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">🎯</div>
          <div class="stat-content">
            <div class="stat-value">{{ planCount }}</div>
            <div class="stat-label">职业规划</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">📚</div>
          <div class="stat-content">
            <div class="stat-value">{{ learningProgress }}%</div>
            <div class="stat-label">学习进度</div>
          </div>
        </div>
        <div class="stat-card">
          <div class="stat-icon">💼</div>
          <div class="stat-content">
            <div class="stat-value">{{ interviewCount }}</div>
            <div class="stat-label">面试安排</div>
          </div>
        </div>
      </div>

      <div class="content-section">
        <div class="section-header">
          <h2>最近活动</h2>
        </div>
        <div class="activity-list">
          <div class="activity-item">
            <span class="activity-icon">✅</span>
            <div class="activity-content">
              <div class="activity-title">完成职业兴趣测评</div>
              <div class="activity-time">2小时前</div>
            </div>
          </div>
          <div class="activity-item">
            <span class="activity-icon">📝</span>
            <div class="activity-content">
              <div class="activity-title">创建职业规划</div>
              <div class="activity-time">昨天</div>
            </div>
          </div>
          <div class="activity-item">
            <span class="activity-icon">📚</span>
            <div class="activity-content">
              <div class="activity-title">学习Java基础课程</div>
              <div class="activity-time">2天前</div>
            </div>
          </div>
          <div class="activity-item">
            <span class="activity-icon">💼</span>
            <div class="activity-content">
              <div class="activity-title">预约面试</div>
              <div class="activity-time">3天前</div>
            </div>
          </div>
        </div>
      </div>

      <div class="content-section">
        <div class="section-header">
          <h2>推荐学习资源</h2>
        </div>
        <div class="resource-list">
          <div class="resource-card">
            <div class="resource-icon">📖</div>
            <div class="resource-content">
              <div class="resource-title">Java编程入门</div>
              <div class="resource-category">编程基础</div>
            </div>
            <div class="resource-progress">
              <div class="progress-bar">
                <div class="progress-fill" style="width: 65%"></div>
              </div>
              <span class="progress-text">65%</span>
            </div>
          </div>
          <div class="resource-card">
            <div class="resource-icon">💻</div>
            <div class="resource-content">
              <div class="resource-title">前端开发实战</div>
              <div class="resource-category">Web开发</div>
            </div>
            <div class="resource-progress">
              <div class="progress-bar">
                <div class="progress-fill" style="width: 40%"></div>
              </div>
              <span class="progress-text">40%</span>
            </div>
          </div>
          <div class="resource-card">
            <div class="resource-icon">📊</div>
            <div class="resource-content">
              <div class="resource-title">数据结构与算法</div>
              <div class="resource-category">计算机基础</div>
            </div>
            <div class="resource-progress">
              <div class="progress-bar">
                <div class="progress-fill" style="width: 20%"></div>
              </div>
              <span class="progress-text">20%</span>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const userInfo = ref({})
const assessmentCount = ref(3)
const planCount = ref(1)
const learningProgress = ref(45)
const interviewCount = ref(2)

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  window.location.href = '/login'
}
</script>

<style scoped>
.dashboard-container {
  display: flex;
  min-height: 100vh;
  background-color: #f5f7fa;
}

.sidebar {
  width: 250px;
  background: linear-gradient(180deg, #4a69bd 0%, #3d5a9e 100%);
  color: white;
  display: flex;
  flex-direction: column;
}

.sidebar-header {
  padding: 1.5rem;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo-icon {
  font-size: 1.5rem;
}

.logo-text {
  font-size: 1.1rem;
  font-weight: bold;
}

.sidebar-nav {
  flex: 1;
  padding: 1rem;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  padding: 0.75rem 1rem;
  margin-bottom: 0.25rem;
  border-radius: 8px;
  color: white;
  text-decoration: none;
  transition: background-color 0.3s;
}

.nav-item:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.nav-item.active {
  background-color: rgba(255, 255, 255, 0.2);
}

.nav-icon {
  font-size: 1.2rem;
}

.nav-text {
  font-size: 0.95rem;
}

.sidebar-footer {
  padding: 1rem;
  border-top: 1px solid rgba(255, 255, 255, 0.1);
}

.logout-btn {
  width: 100%;
  padding: 0.75rem;
  background-color: rgba(255, 255, 255, 0.1);
  border: none;
  border-radius: 8px;
  color: white;
  cursor: pointer;
  transition: background-color 0.3s;
}

.logout-btn:hover {
  background-color: rgba(255, 255, 255, 0.2);
}

.main-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
}

.top-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 2rem;
}

.top-header h1 {
  color: #333;
  font-size: 1.5rem;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.user-name {
  font-weight: 500;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-card {
  background-color: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  align-items: center;
  gap: 1rem;
}

.stat-icon {
  font-size: 2rem;
}

.stat-content {
  flex: 1;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: #333;
}

.stat-label {
  color: #666;
  font-size: 0.9rem;
}

.content-section {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.section-header {
  margin-bottom: 1.5rem;
}

.section-header h2 {
  color: #333;
  font-size: 1.2rem;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.activity-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.activity-icon {
  font-size: 1.5rem;
}

.activity-content {
  flex: 1;
}

.activity-title {
  color: #333;
  font-weight: 500;
}

.activity-time {
  color: #999;
  font-size: 0.85rem;
}

.resource-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.resource-card {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.resource-icon {
  font-size: 1.5rem;
}

.resource-content {
  flex: 1;
}

.resource-title {
  color: #333;
  font-weight: 500;
}

.resource-category {
  color: #666;
  font-size: 0.85rem;
}

.resource-progress {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.progress-bar {
  width: 80px;
  height: 6px;
  background-color: #e0e0e0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background-color: #4a69bd;
  border-radius: 3px;
}

.progress-text {
  font-size: 0.85rem;
  color: #4a69bd;
  font-weight: 500;
}
</style>
