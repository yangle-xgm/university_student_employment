<template>
  <AppLayout title="学习成长">
    <div class="learning-page">
      <div class="learning-stats">
        <BaseCard class="stat-card">
          <div class="stat-value">{{ totalResources }}</div>
          <div class="stat-label">学习资源</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ completedResources }}</div>
          <div class="stat-label">已完成</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ learningDays }}</div>
          <div class="stat-label">连续学习</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ totalHours }}h</div>
          <div class="stat-label">累计时长</div>
        </BaseCard>
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
        <BaseCard v-for="resource in filteredResources" :key="resource.id" class="resource-card">
          <div class="resource-icon-wrapper" :class="getResourceIconClass(resource.type)">
            <el-icon :size="28">
              <component :is="getResourceIcon(resource.type)" />
            </el-icon>
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
          <el-button type="primary" class="learn-btn" @click="startLearning(resource)">
            {{ getResourceProgress(resource.id) > 0 ? '继续学习' : '开始学习' }}
          </el-button>
        </BaseCard>
      </div>

      <div v-if="activeTab === 'paths'" class="paths-list">
        <BaseCard v-for="path in learningPaths" :key="path.id" class="path-card">
          <div class="path-header">
            <div class="path-icon">
              <el-icon :size="40"><Promotion /></el-icon>
            </div>
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
                <el-icon :size="14">
                  <component :is="resource.completed ? CircleCheck : CircleClose" />
                </el-icon>
                {{ resource.completed ? '完成' : '未完成' }}
              </span>
            </div>
          </div>
        </BaseCard>
      </div>

      <div v-if="activeTab === 'my'" class="my-learning">
        <BaseCard class="recent-section">
          <SectionHeader title="最近学习" />
          <div v-if="recentLearning.length > 0" class="recent-list">
            <div v-for="item in recentLearning" :key="item.id" class="recent-item">
              <div class="recent-icon" :class="getResourceIconClass(item.type)">
                <el-icon :size="24">
                  <component :is="getResourceIcon(item.type)" />
                </el-icon>
              </div>
              <div class="recent-info">
                <h4>{{ item.title }}</h4>
                <div class="recent-progress">
                  <div class="progress-bar small">
                    <div class="progress-fill" :style="{ width: item.progress + '%' }"></div>
                  </div>
                  <span>{{ item.progress }}%</span>
                </div>
              </div>
              <el-button type="primary" size="small" plain @click="startLearning(item)">继续</el-button>
            </div>
          </div>
          <EmptyState
            v-else
            icon="Reading"
            title="暂无最近学习"
            description="开始学习资源，记录你的成长轨迹"
          />
        </BaseCard>

        <BaseCard class="achievements-section">
          <SectionHeader title="学习成就" />
          <div class="achievements-grid">
            <div v-for="achievement in achievements" :key="achievement.id" class="achievement-card" :class="{ unlocked: achievement.unlocked }">
              <el-icon :size="32" class="achievement-icon">
                <component :is="achievement.icon" />
              </el-icon>
              <h4>{{ achievement.title }}</h4>
              <p class="achievement-desc">{{ achievement.description }}</p>
              <span class="achievement-status">{{ achievement.unlocked ? '已解锁' : '未解锁' }}</span>
            </div>
          </div>
        </BaseCard>
      </div>

      <el-dialog title="学习资源详情" v-model="showResourceModal" width="600px">
        <div v-if="selectedResource" class="resource-detail">
          <div class="detail-header">
            <div class="detail-icon" :class="getResourceIconClass(selectedResource.type)">
              <el-icon :size="32">
                <component :is="getResourceIcon(selectedResource.type)" />
              </el-icon>
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
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { VideoPlay, Reading, Document, EditPen, Promotion, Star, Medal, Trophy, CircleCheck, CircleClose } from '@element-plus/icons-vue'
import { getLearningResources, searchResources, getLearningPaths, getLearningRecords, updateLearningProgress } from '@/api/learning'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
import EmptyState from '@/components/common/EmptyState.vue'

const userInfo = ref({})
const activeTab = ref('all')
const searchKeyword = ref('')
const userId = ref(1)

const resources = ref([])
const learningPaths = ref([])
const learningRecords = ref([])
const recentLearning = ref([])
const achievements = ref([
  { id: 1, icon: Star, title: '初学者', description: '完成第一个学习资源', unlocked: true },
  { id: 2, icon: Medal, title: '学习达人', description: '连续学习7天', unlocked: true },
  { id: 3, icon: Reading, title: '知识渊博', description: '完成10个学习资源', unlocked: false },
  { id: 4, icon: Trophy, title: '全栈大师', description: '完成全栈开发路径', unlocked: false }
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
    'VIDEO': VideoPlay,
    'COURSE': Reading,
    'DOCUMENT': Document,
    'ARTICLE': EditPen
  }
  return icons[type] || Reading
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
.learning-page {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.learning-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-5);
}

.stat-card {
  text-align: center;
  padding: var(--space-5);
}

.stat-value {
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  color: var(--color-primary);
  line-height: 1.2;
}

.stat-label {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin-top: var(--space-2);
}

.search-section {
  margin-bottom: var(--space-2);
}

.search-input {
  max-width: 400px;
}

.tabs-container {
  margin-bottom: var(--space-2);
}

.resource-tabs {
  background-color: var(--color-card-bg);
  padding: var(--space-2);
  border-radius: var(--radius);
  box-shadow: var(--elevation-1);
}

.resources-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-5);
}

.resource-card {
  display: flex;
  gap: var(--space-4);
  align-items: flex-start;
  transition: transform var(--t-normal) var(--ease-default),
              box-shadow var(--t-normal) var(--ease-default);
}

.resource-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--elevation-2);
}

.resource-icon-wrapper {
  width: 56px;
  height: 56px;
  border-radius: var(--radius-md);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.resource-icon-wrapper.video {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.resource-icon-wrapper.course {
  background-color: var(--color-info-light);
  color: var(--color-info);
}

.resource-icon-wrapper.document {
  background-color: var(--color-success-light);
  color: var(--color-success);
}

.resource-icon-wrapper.article {
  background-color: var(--color-warning-light);
  color: var(--color-warning);
}

.resource-icon-wrapper.unknown {
  background-color: var(--color-primary-light);
  color: var(--color-primary);
}

.resource-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  min-width: 0;
}

.resource-content h3 {
  color: var(--color-text-1);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  margin: 0 0 var(--space-2) 0;
}

.resource-desc {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin: 0 0 var(--space-3) 0;
  flex: 1;
  line-height: 1.5;
}

.resource-meta {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}

.category-tag {
  padding: var(--space-1) var(--space-3);
  background-color: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
}

.duration {
  color: var(--color-text-3);
  font-size: var(--text-xs);
}

.resource-progress {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}

.progress-bar {
  flex: 1;
  height: 6px;
  background-color: var(--color-border-light);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.progress-bar.small {
  width: 120px;
  flex: none;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: var(--radius-full);
  transition: width var(--t-normal) var(--ease-default);
}

.progress-text {
  color: var(--color-primary);
  font-weight: var(--font-semibold);
  font-size: var(--text-xs);
  min-width: 32px;
  text-align: right;
}

.learn-btn {
  align-self: flex-start;
}

.paths-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.path-card {
  overflow: hidden;
}

.path-header {
  display: flex;
  gap: var(--space-5);
  padding: var(--space-5);
  border-bottom: 1px solid var(--color-border-light);
  align-items: center;
}

.path-icon {
  color: var(--color-primary);
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: var(--radius-md);
  background-color: var(--color-primary-light);
  flex-shrink: 0;
}

.path-info {
  flex: 1;
  min-width: 0;
}

.path-info h3 {
  color: var(--color-text-1);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin: 0 0 var(--space-2) 0;
}

.path-desc {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin: 0 0 var(--space-2) 0;
  line-height: 1.5;
}

.path-meta {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  color: var(--color-text-3);
  font-size: var(--text-xs);
}

.path-progress-circle {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  background: conic-gradient(var(--color-primary) calc(var(--progress) * 360deg), var(--color-border-light) 0);
  display: flex;
  justify-content: center;
  align-items: center;
  position: relative;
  flex-shrink: 0;
}

.path-progress-circle::before {
  content: '';
  position: absolute;
  width: 60px;
  height: 60px;
  background-color: var(--color-card-bg);
  border-radius: 50%;
}

.path-progress-circle span {
  position: relative;
  z-index: 1;
  color: var(--color-primary);
  font-weight: var(--font-bold);
  font-size: var(--text-sm);
}

.path-resources {
  padding: var(--space-4) var(--space-5);
}

.path-resource-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border-light);
}

.path-resource-item:last-child {
  border-bottom: none;
}

.resource-index {
  width: 28px;
  height: 28px;
  background-color: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
}

.resource-info {
  flex: 1;
  min-width: 0;
}

.resource-name {
  color: var(--color-text-1);
  font-size: var(--text-sm);
  display: block;
}

.resource-duration {
  color: var(--color-text-3);
  font-size: var(--text-xs);
}

.resource-status {
  display: flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
}

.resource-status.completed {
  color: var(--color-success);
}

.resource-status.pending {
  color: var(--color-text-3);
}

.my-learning {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-6);
}

.recent-section,
.achievements-section {
  display: flex;
  flex-direction: column;
}

.recent-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.recent-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3);
  background-color: var(--color-bg);
  border-radius: var(--radius);
}

.recent-icon {
  width: 40px;
  height: 40px;
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.recent-icon.video {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.recent-icon.course {
  background-color: var(--color-info-light);
  color: var(--color-info);
}

.recent-icon.document {
  background-color: var(--color-success-light);
  color: var(--color-success);
}

.recent-icon.article {
  background-color: var(--color-warning-light);
  color: var(--color-warning);
}

.recent-info {
  flex: 1;
  min-width: 0;
}

.recent-info h4 {
  color: var(--color-text-1);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  margin: 0 0 var(--space-2) 0;
}

.recent-progress {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.recent-progress span {
  color: var(--color-primary);
  font-size: var(--text-xs);
  font-weight: var(--font-semibold);
  min-width: 32px;
  text-align: right;
}

.achievements-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-3);
}

.achievement-card {
  padding: var(--space-4);
  background-color: var(--color-bg);
  border-radius: var(--radius);
  text-align: center;
  opacity: 0.5;
  transition: opacity var(--t-normal) var(--ease-default),
              transform var(--t-normal) var(--ease-default);
}

.achievement-card.unlocked {
  opacity: 1;
}

.achievement-card.unlocked:hover {
  transform: translateY(-2px);
}

.achievement-icon {
  color: var(--color-warning);
  display: flex;
  justify-content: center;
  margin-bottom: var(--space-2);
}

.achievement-card h4 {
  color: var(--color-text-1);
  margin: 0 0 var(--space-1) 0;
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
}

.achievement-desc {
  color: var(--color-text-2);
  font-size: var(--text-xs);
  margin: 0 0 var(--space-2) 0;
  line-height: 1.5;
}

.achievement-status {
  font-size: var(--text-xs);
  color: var(--color-text-3);
}

.resource-detail {
  padding: var(--space-2);
}

.detail-header {
  display: flex;
  gap: var(--space-5);
  margin-bottom: var(--space-5);
  align-items: center;
}

.detail-icon {
  width: 64px;
  height: 64px;
  border-radius: var(--radius-md);
  display: flex;
  justify-content: center;
  align-items: center;
  flex-shrink: 0;
}

.detail-icon.video {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.detail-icon.course {
  background-color: var(--color-info-light);
  color: var(--color-info);
}

.detail-icon.document {
  background-color: var(--color-success-light);
  color: var(--color-success);
}

.detail-icon.article {
  background-color: var(--color-warning-light);
  color: var(--color-warning);
}

.detail-info h2 {
  color: var(--color-text-1);
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  margin: 0 0 var(--space-2) 0;
}

.detail-category {
  padding: var(--space-1) var(--space-3);
  background-color: var(--color-primary-light);
  color: var(--color-primary);
  border-radius: var(--radius-full);
  font-size: var(--text-xs);
}

.detail-content p {
  color: var(--color-text-2);
  line-height: 1.6;
  margin-bottom: var(--space-5);
}

.detail-meta {
  display: flex;
  gap: var(--space-8);
}

.detail-meta .meta-item {
  display: flex;
  flex-direction: column;
  gap: var(--space-1);
}

.meta-label {
  color: var(--color-text-3);
  font-size: var(--text-xs);
}

.meta-value {
  color: var(--color-text-1);
  font-weight: var(--font-medium);
  font-size: var(--text-sm);
}

@media (max-width: 1024px) {
  .learning-stats {
    grid-template-columns: repeat(2, 1fr);
  }

  .resources-grid {
    grid-template-columns: 1fr;
  }

  .my-learning {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .learning-stats {
    grid-template-columns: 1fr;
  }

  .resource-card {
    flex-direction: column;
  }

  .path-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .path-progress-circle {
    align-self: flex-end;
  }

  .detail-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .detail-meta {
    gap: var(--space-4);
  }
}
</style>
