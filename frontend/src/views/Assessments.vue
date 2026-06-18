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
        <router-link to="/dashboard" class="nav-item">
          <span class="nav-icon">📊</span>
          <span class="nav-text">仪表盘</span>
        </router-link>
        <router-link to="/profile" class="nav-item">
          <span class="nav-icon">👤</span>
          <span class="nav-text">个人资料</span>
        </router-link>
        <router-link to="/assessments" class="nav-item active">
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
        <h1>职业测评</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="assessment-stats">
        <div class="stat-item">
          <div class="stat-value">{{ assessments.length }}</div>
          <div class="stat-label">可用测评</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ completedCount }}</div>
          <div class="stat-label">已完成</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ inProgressCount }}</div>
          <div class="stat-label">进行中</div>
        </div>
      </div>

      <div class="assessment-list">
        <h2>测评列表</h2>
        <div class="assessment-grid">
          <div v-for="assessment in assessments" :key="assessment.id" class="assessment-card" @click="goToDetail(assessment.id)">
            <div class="assessment-icon">{{ getIcon(assessment.dimension) }}</div>
            <div class="assessment-content">
              <h3>{{ assessment.name }}</h3>
              <p class="assessment-dimension">{{ getDimensionLabel(assessment.dimension) }}</p>
              <p class="assessment-desc">{{ assessment.description }}</p>
            </div>
            <div class="assessment-info">
              <span class="duration">{{ assessment.duration }}分钟</span>
              <button class="start-btn" @click.stop="startAssessment(assessment.id)">开始测评</button>
            </div>
          </div>
        </div>
      </div>

      <div class="recent-reports">
        <h2>我的测评报告</h2>
        <div class="report-list">
          <div v-for="report in reports" :key="report.id" class="report-item">
            <div class="report-info">
              <h4>{{ getReportTitle(report) }}</h4>
              <p class="report-date">{{ report.generatedAt }}</p>
            </div>
            <div class="report-score">
              <span class="score-value">{{ getScore(report.content) }}分</span>
              <span class="score-level">{{ getLevel(report.content) }}</span>
            </div>
            <button class="view-btn" @click="viewReport(report.id)">查看报告</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getAssessmentList, getReportsByStudentId } from '@/api/assessment'

const userInfo = ref({})
const assessments = ref([])
const reports = ref([])
const loading = ref(false)

const userId = ref(1)

const completedCount = computed(() => reports.value.length)
const inProgressCount = ref(1)

onMounted(async () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    const storedUserId = userInfo.value.id || userInfo.value.userId
    if (storedUserId) {
      userId.value = storedUserId
    }
  }
  
  await loadAssessments()
  await loadReports()
})

const loadAssessments = async () => {
  try {
    const data = await getAssessmentList()
    assessments.value = data
  } catch (error) {
    console.error('加载测评列表失败:', error)
    assessments.value = [
      { id: 1, name: '职业兴趣测评', description: '了解您的职业兴趣方向，帮助您找到适合的职业领域', dimension: 'INTEREST', duration: 20 },
      { id: 2, name: '性格特质测评', description: '探索您的性格特点，发现您的职业优势', dimension: 'PERSONALITY', duration: 15 },
      { id: 3, name: '能力倾向测评', description: '评估您的职业能力倾向，指导职业发展', dimension: 'ABILITY', duration: 25 },
      { id: 4, name: '职业价值观测评', description: '了解您的职业价值观，找到匹配的工作环境', dimension: 'VALUE', duration: 10 }
    ]
  }
}

const loadReports = async () => {
  try {
    const data = await getReportsByStudentId(userId.value)
    reports.value = data
  } catch (error) {
    console.error('加载测评报告失败:', error)
    reports.value = [
      { id: 1, assessmentId: 1, content: '{"score":85,"level":"优秀"}', generatedAt: '2024-01-15' },
      { id: 2, assessmentId: 2, content: '{"score":72,"level":"良好"}', generatedAt: '2024-01-10' }
    ]
  }
}

const getIcon = (dimension) => {
  const icons = {
    'INTEREST': '🎨',
    'PERSONALITY': '🧠',
    'ABILITY': '⚡',
    'VALUE': '💎'
  }
  return icons[dimension] || '📊'
}

const getDimensionLabel = (dimension) => {
  const labels = {
    'INTEREST': '职业兴趣',
    'PERSONALITY': '性格特质',
    'ABILITY': '能力倾向',
    'VALUE': '职业价值观'
  }
  return labels[dimension] || dimension
}

const getReportTitle = (report) => {
  const assessment = assessments.value.find(a => a.id === report.assessmentId)
  if (assessment) {
    return `${assessment.name}报告`
  }
  return `测评报告 ${report.id}`
}

const getScore = (content) => {
  try {
    const data = JSON.parse(content)
    return data.score || 0
  } catch {
    return 0
  }
}

const getLevel = (content) => {
  try {
    const data = JSON.parse(content)
    return data.level || '未知'
  } catch {
    return '未知'
  }
}

const goToDetail = (id) => {
  window.location.href = `/assessment/${id}`
}

const startAssessment = (id) => {
  window.location.href = `/assessment/${id}`
}

const viewReport = (reportId) => {
  window.location.href = `/assessment/report/${reportId}`
}

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

.assessment-stats {
  display: flex;
  gap: 2rem;
  margin-bottom: 2rem;
}

.stat-item {
  background-color: white;
  padding: 1.5rem 2rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.stat-value {
  font-size: 2rem;
  font-weight: bold;
  color: #4a69bd;
}

.stat-label {
  color: #666;
  margin-top: 0.25rem;
}

.assessment-list h2,
.recent-reports h2 {
  color: #333;
  margin-bottom: 1.5rem;
}

.assessment-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.assessment-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  gap: 1.5rem;
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.assessment-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.assessment-icon {
  font-size: 3rem;
}

.assessment-content {
  flex: 1;
}

.assessment-content h3 {
  color: #333;
  margin-bottom: 0.5rem;
}

.assessment-dimension {
  color: #4a69bd;
  font-size: 0.85rem;
  margin-bottom: 0.5rem;
}

.assessment-desc {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
}

.assessment-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
}

.duration {
  color: #999;
  font-size: 0.85rem;
}

.start-btn {
  padding: 0.5rem 1.5rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.start-btn:hover {
  background-color: #3d5a9e;
}

.report-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.report-item {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.report-info h4 {
  color: #333;
  margin-bottom: 0.25rem;
}

.report-date {
  color: #999;
  font-size: 0.85rem;
}

.report-score {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.score-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4a69bd;
}

.score-level {
  padding: 0.25rem 0.75rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 20px;
  font-size: 0.85rem;
}

.view-btn {
  padding: 0.5rem 1.5rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.view-btn:hover {
  background-color: #e0e8ff;
}
</style>