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
        <router-link to="/interviews" class="nav-item active">
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
        <h1>面试管理</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="interview-stats">
        <div class="stat-item">
          <div class="stat-value">{{ upcomingCount }}</div>
          <div class="stat-label">即将到来</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ completedCount }}</div>
          <div class="stat-label">已完成</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ canceledCount }}</div>
          <div class="stat-label">已取消</div>
        </div>
      </div>

      <div class="filter-section">
        <el-tabs v-model="activeTab" class="filter-tabs">
          <el-tab-pane label="全部面试" name="all">
          </el-tab-pane>
          <el-tab-pane label="即将到来" name="upcoming">
          </el-tab-pane>
          <el-tab-pane label="已完成" name="completed">
          </el-tab-pane>
          <el-tab-pane label="已取消" name="canceled">
          </el-tab-pane>
        </el-tabs>
      </div>

      <div class="interview-list">
        <div v-for="interview in filteredInterviews" :key="interview.id" class="interview-card">
          <div class="interview-header">
            <div class="company-info">
              <span class="company-icon">🏢</span>
              <div class="company-detail">
                <h3>{{ interview.companyName }}</h3>
                <p class="position">{{ interview.position }}</p>
              </div>
            </div>
            <span class="interview-status" :class="getStatusClass(interview.status)">
              {{ getStatusLabel(interview.status) }}
            </span>
          </div>
          
          <div class="interview-body">
            <div class="info-grid">
              <div class="info-item">
                <span class="info-icon">📅</span>
                <div class="info-content">
                  <span class="info-label">面试时间</span>
                  <span class="info-value">{{ formatDate(interview.interviewDate) }} {{ interview.interviewTime }}</span>
                </div>
              </div>
              <div class="info-item">
                <span class="info-icon">📍</span>
                <div class="info-content">
                  <span class="info-label">面试地点</span>
                  <span class="info-value">{{ interview.location }}</span>
                </div>
              </div>
              <div class="info-item">
                <span class="info-icon">👤</span>
                <div class="info-content">
                  <span class="info-label">面试官</span>
                  <span class="info-value">{{ interview.interviewer }}</span>
                </div>
              </div>
              <div class="info-item">
                <span class="info-icon">📞</span>
                <div class="info-content">
                  <span class="info-label">联系方式</span>
                  <span class="info-value">{{ interview.contactPhone }}</span>
                </div>
              </div>
            </div>
            
            <div class="interview-notes" v-if="interview.notes">
              <span class="notes-label">备注</span>
              <p>{{ interview.notes }}</p>
            </div>
          </div>

          <div class="interview-footer">
            <div class="action-buttons">
              <button class="action-btn primary" @click="viewDetails(interview)">查看详情</button>
              <button class="action-btn secondary" @click="editInterview(interview)">编辑</button>
              <button v-if="interview.status === 'UPCOMING'" class="action-btn danger" @click="handleCancelInterview(interview.id)">取消</button>
            </div>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="filteredInterviews.length === 0">
        <span class="empty-icon">💼</span>
        <p>暂无面试安排</p>
      </div>

      <el-dialog title="面试详情" v-model="showDetailModal" width="600px">
        <div v-if="selectedInterview" class="detail-content">
          <div class="detail-header">
            <h2>{{ selectedInterview.companyName }}</h2>
            <p class="position">{{ selectedInterview.position }}</p>
          </div>
          <div class="detail-info">
            <div class="detail-row">
              <label>面试状态</label>
              <span :class="getStatusClass(selectedInterview.status)" class="status-badge">
                {{ getStatusLabel(selectedInterview.status) }}
              </span>
            </div>
            <div class="detail-row">
              <label>面试时间</label>
              <span>{{ formatDate(selectedInterview.interviewDate) }} {{ selectedInterview.interviewTime }}</span>
            </div>
            <div class="detail-row">
              <label>面试地点</label>
              <span>{{ selectedInterview.location }}</span>
            </div>
            <div class="detail-row">
              <label>面试官</label>
              <span>{{ selectedInterview.interviewer }}</span>
            </div>
            <div class="detail-row">
              <label>联系方式</label>
              <span>{{ selectedInterview.contactPhone }}</span>
            </div>
            <div class="detail-row">
              <label>面试类型</label>
              <span>{{ getTypeLabel(selectedInterview.interviewType) }}</span>
            </div>
            <div class="detail-row notes">
              <label>备注</label>
              <p>{{ selectedInterview.notes || '无' }}</p>
            </div>
          </div>
        </div>
        <template #footer>
          <el-button @click="showDetailModal = false">关闭</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getInterviewsByUserId } from '@/api/interview'

const userInfo = ref({})
const activeTab = ref('all')
const userId = ref(1)

const interviews = ref([])
const showDetailModal = ref(false)
const selectedInterview = ref(null)

const upcomingCount = computed(() => interviews.value.filter(i => i.status === 'UPCOMING').length)
const completedCount = computed(() => interviews.value.filter(i => i.status === 'COMPLETED').length)
const canceledCount = computed(() => interviews.value.filter(i => i.status === 'CANCELED').length)

const filteredInterviews = computed(() => {
  if (activeTab.value === 'all') return interviews.value
  if (activeTab.value === 'upcoming') return interviews.value.filter(i => i.status === 'UPCOMING')
  if (activeTab.value === 'completed') return interviews.value.filter(i => i.status === 'COMPLETED')
  if (activeTab.value === 'canceled') return interviews.value.filter(i => i.status === 'CANCELED')
  return interviews.value
})

const getStatusLabel = (status) => {
  const labels = {
    'UPCOMING': '即将到来',
    'COMPLETED': '已完成',
    'CANCELED': '已取消',
    'RESCHEDULED': '已改期'
  }
  return labels[status] || status
}

const getStatusClass = (status) => {
  return status.toLowerCase()
}

const getTypeLabel = (type) => {
  const labels = {
    'ON_SITE': '现场面试',
    'VIDEO': '视频面试',
    'PHONE': '电话面试'
  }
  return labels[type] || type
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

const viewDetails = (interview) => {
  selectedInterview.value = interview
  showDetailModal.value = true
}

const editInterview = (interview) => {
  selectedInterview.value = interview
  showDetailModal.value = true
}

const handleCancelInterview = async (interviewId) => {
  const interview = interviews.value.find(i => i.id === interviewId)
  if (interview) {
    try {
      interview.status = 'CANCELED'
    } catch (error) {
      console.error('取消面试失败:', error)
      interview.status = 'CANCELED'
    }
  }
}

const loadInterviews = async () => {
  try {
    const data = await getInterviewsByUserId(userId.value)
    interviews.value = data
  } catch (error) {
    console.error('加载面试列表失败:', error)
    interviews.value = [
      { id: 1, companyName: '阿里巴巴', position: 'Java开发工程师', interviewDate: '2024-02-15', interviewTime: '10:00', location: '杭州市西湖区文三路', interviewer: '张经理', contactPhone: '138****8888', interviewType: 'ON_SITE', status: 'UPCOMING', notes: '请准备项目经历介绍' },
      { id: 2, companyName: '腾讯', position: '前端开发工程师', interviewDate: '2024-02-10', interviewTime: '14:00', location: '深圳市南山区科技园', interviewer: '李总监', contactPhone: '139****9999', interviewType: 'VIDEO', status: 'COMPLETED', notes: '已完成一面，等待二面通知' },
      { id: 3, companyName: '字节跳动', position: '后端开发工程师', interviewDate: '2024-02-20', interviewTime: '09:30', location: '北京市海淀区中关村', interviewer: '王技术', contactPhone: '137****7777', interviewType: 'ON_SITE', status: 'UPCOMING', notes: '' },
      { id: 4, companyName: '美团', position: '测试开发工程师', interviewDate: '2024-02-08', interviewTime: '15:00', location: '北京市朝阳区望京', interviewer: '陈主管', contactPhone: '136****6666', interviewType: 'PHONE', status: 'CANCELED', notes: '因个人原因取消' }
    ]
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  window.location.href = '/login'
}

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    userId.value = userInfo.value.id || userInfo.value.userId || 1
  }
  loadInterviews()
})
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

.interview-stats {
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
  flex: 1;
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

.filter-section {
  margin-bottom: 1.5rem;
}

.filter-tabs {
  background-color: white;
  padding: 0.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.interview-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.interview-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.interview-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.company-info {
  display: flex;
  gap: 1rem;
}

.company-icon {
  font-size: 2rem;
}

.company-detail h3 {
  color: #333;
  margin: 0 0 0.25rem 0;
}

.position {
  color: #666;
  font-size: 0.9rem;
  margin: 0;
}

.interview-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.interview-status.upcoming {
  background-color: #fff3cd;
  color: #856404;
}

.interview-status.completed {
  background-color: #d4edda;
  color: #155724;
}

.interview-status.canceled {
  background-color: #f8d7da;
  color: #721c24;
}

.interview-body {
  padding: 1.5rem;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.info-item {
  display: flex;
  gap: 0.75rem;
}

.info-icon {
  font-size: 1.2rem;
}

.info-content {
  display: flex;
  flex-direction: column;
}

.info-label {
  color: #999;
  font-size: 0.85rem;
}

.info-value {
  color: #333;
  font-size: 0.95rem;
}

.interview-notes {
  margin-top: 1rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.notes-label {
  color: #666;
  font-size: 0.85rem;
  margin-bottom: 0.5rem;
  display: block;
}

.interview-notes p {
  color: #333;
  margin: 0;
}

.interview-footer {
  padding: 1rem 1.5rem;
  background-color: #f8f9fa;
}

.action-buttons {
  display: flex;
  gap: 0.75rem;
}

.action-btn {
  padding: 0.5rem 1.5rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  font-size: 0.9rem;
  transition: all 0.3s;
}

.action-btn.primary {
  background-color: #4a69bd;
  color: white;
}

.action-btn.primary:hover {
  background-color: #3d5a9e;
}

.action-btn.secondary {
  background-color: #f0f4ff;
  color: #4a69bd;
}

.action-btn.secondary:hover {
  background-color: #e0e8ff;
}

.action-btn.danger {
  background-color: #f8d7da;
  color: #721c24;
}

.action-btn.danger:hover {
  background-color: #f5c6cb;
}

.empty-state {
  background-color: white;
  border-radius: 12px;
  padding: 3rem;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.empty-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 1rem;
}

.empty-state p {
  color: #666;
}

.detail-content {
  padding: 0.5rem;
}

.detail-header h2 {
  color: #333;
  margin: 0 0 0.25rem 0;
}

.detail-header .position {
  color: #4a69bd;
}

.detail-info {
  margin-top: 1.5rem;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail-row label {
  color: #666;
}

.detail-row span {
  color: #333;
  font-weight: 500;
}

.status-badge {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
}

.detail-row.notes {
  flex-direction: column;
  gap: 0.5rem;
}

.detail-row.notes p {
  color: #333;
  margin: 0;
  line-height: 1.6;
}
</style>