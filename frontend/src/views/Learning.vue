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
        <router-link to="/learning" class="nav-item active">
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
        <h1>学习成长</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="learning-stats">
        <div class="stat-item">
          <div class="stat-value">{{ totalResources }}</div>
          <div class="stat-label">学习资源</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ completedResources }}</div>
          <div class="stat-label">已完成</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ learningDays }}</div>
          <div class="stat-label">连续学习</div>
        </div>
        <div class="stat-item">
          <div class="stat-value">{{ totalHours }}h</div>
          <div class="stat-label">累计时长</div>
        </div>
      </div>

      <div class="search-section">
        <el-input 
          v-model="searchKeyword" 
          placeholder="搜索学习资源..." 
          class="search-input"
          @keyup.enter="handleSearch"
        >
          <template #append>
            <el-button @click="handleSearch">搜索</el-button>
          </template>
        </el-input>
      </div>

      <div class="tabs-container">
        <el-tabs v-model="activeTab" class="resource-tabs">
          <el-tab-pane label="全部资源" name="all">
          </el-tab-pane>
          <el-tab-pane label="学习路径" name="paths">
          </el-tab-pane>
          <el-tab-pane label="我的学习" name="my">
          </el-tab-pane>
        </el-tabs>
      </div>

      <div v-if="activeTab === 'all'" class="resources-grid">
        <div v-for="resource in filteredResources" :key="resource.id" class="resource-card">
          <div class="resource-icon-wrapper" :class="getResourceIconClass(resource.type)">
            <span class="resource-icon">{{ getResourceIcon(resource.type) }}</span>
          </div>
          <div class="resource-content">
            <h3>{{ resource.title }}</h3>
            <p class="resource-desc">{{ resource.description }}</p>
            <div class="resource-meta">
              <span class="category-tag">{{ resource.category }}</span>
              <span class="duration">{{ resource.duration }}</span>
            </div>
            <div class="resource-progress" v-if="getResourceProgress(resource.id) > 0">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: getResourceProgress(resource.id) + '%' }"></div>
              </div>
              <span class="progress-text">{{ getResourceProgress(resource.id) }}%</span>
            </div>
          </div>
          <button class="learn-btn" @click="startLearning(resource)">
            {{ getResourceProgress(resource.id) > 0 ? '继续学习' : '开始学习' }}
          </button>
        </div>
      </div>

      <div v-if="activeTab === 'paths'" class="paths-list">
        <div v-for="path in learningPaths" :key="path.id" class="path-card">
          <div class="path-header">
            <div class="path-icon">🚀</div>
            <div class="path-info">
              <h3>{{ path.title }}</h3>
              <p class="path-desc">{{ path.description }}</p>
              <div class="path-meta">
                <span>{{ path.resourceCount }}个资源</span>
                <span>•</span>
                <span>{{ path.totalDuration }}</span>
              </div>
            </div>
            <div class="path-progress-circle" :style="{ '--progress': path.progress / 100 }">
              <span>{{ path.progress }}%</span>
            </div>
          </div>
          <div class="path-resources">
            <div v-for="(resource, index) in path.resources" :key="resource.id" class="path-resource-item">
              <span class="resource-index">{{ index + 1 }}</span>
              <div class="resource-info">
                <span class="resource-name">{{ resource.title }}</span>
                <span class="resource-duration">{{ resource.duration }}</span>
              </div>
              <span class="resource-status" :class="getResourceStatusClass(resource.completed)">
                {{ resource.completed ? '✓ 完成' : '○ 未完成' }}
              </span>
            </div>
          </div>
        </div>
      </div>

      <div v-if="activeTab === 'my'" class="my-learning">
        <div class="recent-section">
          <h2>最近学习</h2>
          <div class="recent-list">
            <div v-for="item in recentLearning" :key="item.id" class="recent-item">
              <div class="recent-icon">{{ getResourceIcon(item.type) }}</div>
              <div class="recent-info">
                <h4>{{ item.title }}</h4>
                <div class="recent-progress">
                  <div class="progress-bar small">
                    <div class="progress-fill" :style="{ width: item.progress + '%' }"></div>
                  </div>
                  <span>{{ item.progress }}%</span>
                </div>
              </div>
              <button class="continue-btn" @click="startLearning(item)">继续</button>
            </div>
          </div>
        </div>

        <div class="achievements-section">
          <h2>学习成就</h2>
          <div class="achievements-grid">
            <div v-for="achievement in achievements" :key="achievement.id" class="achievement-card" :class="{ unlocked: achievement.unlocked }">
              <span class="achievement-icon">{{ achievement.icon }}</span>
              <h4>{{ achievement.title }}</h4>
              <p class="achievement-desc">{{ achievement.description }}</p>
              <span class="achievement-status">{{ achievement.unlocked ? '已解锁' : '未解锁' }}</span>
            </div>
          </div>
        </div>
      </div>

      <el-dialog title="学习资源详情" v-model="showResourceModal" width="600px">
        <div v-if="selectedResource" class="resource-detail">
          <div class="detail-header">
            <div class="detail-icon" :class="getResourceIconClass(selectedResource.type)">
              {{ getResourceIcon(selectedResource.type) }}
            </div>
            <div class="detail-info">
              <h2>{{ selectedResource.title }}</h2>
              <span class="detail-category">{{ selectedResource.category }}</span>
            </div>
          </div>
          <div class="detail-content">
            <p>{{ selectedResource.description }}</p>
            <div class="detail-meta">
              <div class="meta-item">
                <span class="meta-label">时长</span>
                <span class="meta-value">{{ selectedResource.duration }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">类型</span>
                <span class="meta-value">{{ getResourceTypeLabel(selectedResource.type) }}</span>
              </div>
              <div class="meta-item">
                <span class="meta-label">难度</span>
                <span class="meta-value">{{ getDifficultyLabel(selectedResource.difficulty) }}</span>
              </div>
            </div>
          </div>
        </div>
        <template #footer>
          <el-button @click="showResourceModal = false">关闭</el-button>
          <el-button type="primary" @click="handleStartLearning">开始学习</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getLearningResources, searchResources, getLearningPaths, getLearningRecords, updateLearningProgress } from '@/api/learning'

const userInfo = ref({})
const activeTab = ref('all')
const searchKeyword = ref('')
const userId = ref(1)

const resources = ref([])
const learningPaths = ref([])
const learningRecords = ref([])
const recentLearning = ref([])
const achievements = ref([
  { id: 1, icon: '🌟', title: '初学者', description: '完成第一个学习资源', unlocked: true },
  { id: 2, icon: '🔥', title: '学习达人', description: '连续学习7天', unlocked: true },
  { id: 3, icon: '📚', title: '知识渊博', description: '完成10个学习资源', unlocked: false },
  { id: 4, icon: '🏆', title: '全栈大师', description: '完成全栈开发路径', unlocked: false }
])

const showResourceModal = ref(false)
const selectedResource = ref(null)

const totalResources = computed(() => resources.value.length)
const completedResources = computed(() => resources.value.filter(r => getResourceProgress(r.id) === 100).length)
const learningDays = ref(7)
const totalHours = ref(15)

const filteredResources = computed(() => {
  if (!searchKeyword.value) return resources.value
  const keyword = searchKeyword.value.toLowerCase()
  return resources.value.filter(r => 
    r.title.toLowerCase().includes(keyword) || 
    r.description.toLowerCase().includes(keyword) ||
    r.category.toLowerCase().includes(keyword)
  )
})

const getResourceProgress = (resourceId) => {
  const record = learningRecords.value.find(r => r.resourceId === resourceId)
  return record ? record.progress : 0
}

const getResourceIcon = (type) => {
  const icons = {
    'VIDEO': '🎬',
    'COURSE': '📖',
    'DOCUMENT': '📄',
    'ARTICLE': '📝'
  }
  return icons[type] || '📚'
}

const getResourceIconClass = (type) => {
  return type ? type.toLowerCase() : 'unknown'
}

const getResourceTypeLabel = (type) => {
  const labels = {
    'VIDEO': '视频课程',
    'COURSE': '系列课程',
    'DOCUMENT': '文档资料',
    'ARTICLE': '文章教程'
  }
  return labels[type] || type
}

const getDifficultyLabel = (difficulty) => {
  const labels = {
    'BEGINNER': '入门',
    'INTERMEDIATE': '中级',
    'ADVANCED': '高级'
  }
  return labels[difficulty] || difficulty
}

const getResourceStatusClass = (completed) => {
  return completed ? 'completed' : 'pending'
}

const handleSearch = async () => {
  if (!searchKeyword.value) {
    await loadResources()
  } else {
    try {
      const data = await searchResources(searchKeyword.value)
      resources.value = data
    } catch (error) {
      console.error('搜索资源失败:', error)
    }
  }
}

const startLearning = (resource) => {
  selectedResource.value = resource
  showResourceModal.value = true
}

const handleStartLearning = async () => {
  if (!selectedResource.value) return
  
  try {
    const progress = getResourceProgress(selectedResource.value.id)
    const newProgress = Math.min(100, progress + 10)
    await updateLearningProgress(userId.value, selectedResource.value.id, newProgress)
    
    const recordIndex = learningRecords.value.findIndex(r => r.resourceId === selectedResource.value.id)
    if (recordIndex >= 0) {
      learningRecords.value[recordIndex].progress = newProgress
    } else {
      learningRecords.value.push({
        studentId: userId.value,
        resourceId: selectedResource.value.id,
        progress: newProgress
      })
    }
  } catch (error) {
    console.error('更新学习进度失败:', error)
  }
  
  showResourceModal.value = false
}

const loadResources = async () => {
  try {
    const data = await getLearningResources()
    resources.value = data
  } catch (error) {
    console.error('加载学习资源失败:', error)
    resources.value = [
      { id: 1, title: 'Java编程入门', description: '从零开始学习Java编程语言，掌握基本语法和面向对象编程', category: '编程基础', type: 'VIDEO', duration: '4小时', difficulty: 'BEGINNER' },
      { id: 2, title: '数据结构与算法', description: '深入学习常用数据结构和算法，提升编程能力', category: '计算机基础', type: 'COURSE', duration: '8小时', difficulty: 'INTERMEDIATE' },
      { id: 3, title: 'Vue.js实战教程', description: '学习Vue.js框架，构建现代化前端应用', category: 'Web开发', type: 'VIDEO', duration: '6小时', difficulty: 'INTERMEDIATE' },
      { id: 4, title: 'Spring Boot实战', description: '掌握Spring Boot框架，快速构建企业级后端服务', category: '后端开发', type: 'COURSE', duration: '10小时', difficulty: 'ADVANCED' },
      { id: 5, title: 'SQL数据库基础', description: '学习SQL语言，掌握数据库基本操作', category: '数据库', type: 'DOCUMENT', duration: '2小时', difficulty: 'BEGINNER' },
      { id: 6, title: '前端工程化实践', description: '学习现代前端工程化工具和最佳实践', category: 'Web开发', type: 'ARTICLE', duration: '3小时', difficulty: 'INTERMEDIATE' }
    ]
  }
}

const loadPaths = async () => {
  try {
    const data = await getLearningPaths()
    learningPaths.value = data
  } catch (error) {
    console.error('加载学习路径失败:', error)
    learningPaths.value = [
      { 
        id: 1, 
        title: '全栈开发工程师成长路径', 
        description: '从零基础到全栈开发工程师的完整学习路径',
        resourceCount: 12,
        totalDuration: '48小时',
        progress: 42,
        resources: [
          { id: 1, title: 'HTML/CSS基础', duration: '4小时', completed: true },
          { id: 2, title: 'JavaScript基础', duration: '6小时', completed: true },
          { id: 3, title: 'Vue.js框架', duration: '8小时', completed: false },
          { id: 4, title: 'Node.js后端', duration: '8小时', completed: false }
        ]
      },
      { 
        id: 2, 
        title: 'Java后端开发进阶', 
        description: '深入学习Java后端开发技术栈',
        resourceCount: 8,
        totalDuration: '32小时',
        progress: 25,
        resources: [
          { id: 1, title: 'Java高级特性', duration: '6小时', completed: true },
          { id: 2, title: 'Spring Framework', duration: '8小时', completed: false },
          { id: 3, title: '数据库优化', duration: '4小时', completed: false }
        ]
      }
    ]
  }
}

const loadRecords = async () => {
  try {
    const data = await getLearningRecords(userId.value)
    learningRecords.value = data
    
    recentLearning.value = resources.value
      .map(r => ({
        ...r,
        progress: getResourceProgress(r.id)
      }))
      .filter(r => r.progress > 0)
      .slice(0, 3)
  } catch (error) {
    console.error('加载学习记录失败:', error)
    recentLearning.value = [
      { id: 1, title: 'Java编程入门', type: 'VIDEO', progress: 65 },
      { id: 2, title: '数据结构与算法', type: 'COURSE', progress: 30 },
      { id: 3, title: 'SQL数据库基础', type: 'DOCUMENT', progress: 100 }
    ]
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  window.location.href = '/login'
}

onMounted(async () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    userId.value = userInfo.value.id || userInfo.value.userId || 1
  }
  
  await loadResources()
  await loadPaths()
  await loadRecords()
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

.learning-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 1.5rem;
  margin-bottom: 2rem;
}

.stat-item {
  background-color: white;
  padding: 1.5rem;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  text-align: center;
}

.stat-value {
  font-size: 1.8rem;
  font-weight: bold;
  color: #4a69bd;
}

.stat-label {
  color: #666;
  margin-top: 0.25rem;
}

.search-section {
  margin-bottom: 1.5rem;
}

.search-input {
  max-width: 400px;
}

.tabs-container {
  margin-bottom: 1.5rem;
}

.resource-tabs {
  background-color: white;
  padding: 0.5rem;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.resources-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1.5rem;
}

.resource-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  gap: 1rem;
  transition: transform 0.3s, box-shadow 0.3s;
}

.resource-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.resource-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.resource-icon-wrapper.video {
  background-color: #ffe4e6;
}

.resource-icon-wrapper.course {
  background-color: #e6f7ff;
}

.resource-icon-wrapper.document {
  background-color: #f6ffed;
}

.resource-icon-wrapper.article {
  background-color: #fff7e6;
}

.resource-icon {
  font-size: 1.8rem;
}

.resource-content {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.resource-content h3 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.resource-desc {
  color: #666;
  font-size: 0.9rem;
  margin: 0 0 0.75rem 0;
  flex: 1;
}

.resource-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 0.75rem;
}

.category-tag {
  padding: 0.25rem 0.75rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 20px;
  font-size: 0.85rem;
}

.duration {
  color: #999;
  font-size: 0.85rem;
}

.resource-progress {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.progress-bar {
  flex: 1;
  height: 6px;
  background-color: #e0e0e0;
  border-radius: 3px;
  overflow: hidden;
}

.progress-bar.small {
  width: 120px;
}

.progress-fill {
  height: 100%;
  background-color: #4a69bd;
  border-radius: 3px;
  transition: width 0.3s;
}

.progress-text {
  color: #4a69bd;
  font-weight: 500;
  font-size: 0.85rem;
}

.learn-btn {
  padding: 0.5rem 1.5rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  align-self: flex-start;
}

.learn-btn:hover {
  background-color: #3d5a9e;
}

.paths-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.path-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.path-header {
  display: flex;
  gap: 1.5rem;
  padding: 1.5rem;
  border-bottom: 1px solid #f0f0f0;
}

.path-icon {
  font-size: 2.5rem;
}

.path-info {
  flex: 1;
}

.path-info h3 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.path-desc {
  color: #666;
  font-size: 0.9rem;
  margin: 0 0 0.5rem 0;
}

.path-meta {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  color: #999;
  font-size: 0.85rem;
}

.path-progress-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: conic-gradient(#4a69bd calc(var(--progress) * 360deg), #e0e0e0 0);
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
}

.path-progress-circle::before {
  content: '';
  position: absolute;
  width: 60px;
  height: 60px;
  background-color: white;
  border-radius: 50%;
}

.path-progress-circle span {
  position: relative;
  z-index: 1;
  color: #4a69bd;
  font-weight: bold;
}

.path-resources {
  padding: 1rem 1.5rem;
}

.path-resource-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.path-resource-item:last-child {
  border-bottom: none;
}

.resource-index {
  width: 28px;
  height: 28px;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.85rem;
  font-weight: 500;
}

.resource-info {
  flex: 1;
}

.resource-name {
  color: #333;
  font-size: 0.95rem;
}

.resource-duration {
  color: #999;
  font-size: 0.85rem;
}

.resource-status {
  font-size: 0.85rem;
  font-weight: 500;
}

.resource-status.completed {
  color: #28a745;
}

.resource-status.pending {
  color: #999;
}

.my-learning {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.recent-section, .achievements-section {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.recent-section h2, .achievements-section h2 {
  color: #333;
  margin-bottom: 1.5rem;
  font-size: 1.2rem;
}

.recent-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.recent-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
}

.recent-icon {
  font-size: 1.5rem;
}

.recent-info {
  flex: 1;
}

.recent-info h4 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.recent-progress {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.continue-btn {
  padding: 0.375rem 1rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 0.85rem;
  cursor: pointer;
}

.continue-btn:hover {
  background-color: #3d5a9e;
}

.achievements-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 1rem;
}

.achievement-card {
  padding: 1rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  text-align: center;
  opacity: 0.5;
}

.achievement-card.unlocked {
  opacity: 1;
}

.achievement-icon {
  font-size: 2rem;
  display: block;
  margin-bottom: 0.5rem;
}

.achievement-card h4 {
  color: #333;
  margin: 0 0 0.25rem 0;
  font-size: 0.9rem;
}

.achievement-desc {
  color: #666;
  font-size: 0.8rem;
  margin: 0 0 0.5rem 0;
}

.achievement-status {
  font-size: 0.8rem;
  color: #999;
}

.resource-detail {
  padding: 0.5rem;
}

.detail-header {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1.5rem;
}

.detail-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 2rem;
}

.detail-icon.video { background-color: #ffe4e6; }
.detail-icon.course { background-color: #e6f7ff; }
.detail-icon.document { background-color: #f6ffed; }
.detail-icon.article { background-color: #fff7e6; }

.detail-info h2 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.detail-category {
  padding: 0.25rem 0.75rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 20px;
  font-size: 0.85rem;
}

.detail-content p {
  color: #666;
  line-height: 1.6;
  margin-bottom: 1.5rem;
}

.detail-meta {
  display: flex;
  gap: 2rem;
}

.meta-item {
  display: flex;
  flex-direction: column;
}

.meta-label {
  color: #999;
  font-size: 0.85rem;
}

.meta-value {
  color: #333;
  font-weight: 500;
}
</style>