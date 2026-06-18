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
        <button class="back-btn" @click="goBack">← 返回</button>
        <h1>{{ assessment.name }}</h1>
        <div class="progress-info">
          <span>进度: {{ currentIndex + 1 }}/{{ questions.length }}</span>
        </div>
      </header>

      <div class="assessment-detail">
        <div class="assessment-info">
          <div class="info-row">
            <span class="info-label">测评维度</span>
            <span class="info-value">{{ getDimensionLabel(assessment.dimension) }}</span>
          </div>
          <div class="info-row">
            <span class="info-label">预计时长</span>
            <span class="info-value">{{ assessment.duration }}分钟</span>
          </div>
          <div class="info-row">
            <span class="info-label">题目数量</span>
            <span class="info-value">{{ questions.length }}题</span>
          </div>
        </div>

        <div class="question-area" v-if="!isFinished && !loading && questions.length > 0">
          <div class="question-card">
            <div class="question-header">
              <span class="question-number">第 {{ currentIndex + 1 }} 题</span>
              <span class="question-type">{{ currentQuestion ? getQuestionTypeLabel(currentQuestion.questionType) : '' }}</span>
            </div>
            <h3 class="question-content">{{ currentQuestion ? currentQuestion.content : '' }}</h3>
            
            <div class="options-list" v-if="currentQuestion && currentQuestion.optionsArray">
              <label 
                v-for="(option, index) in currentQuestion.optionsArray" 
                :key="index" 
                class="option-item"
                :class="{ selected: answers[currentQuestion.id] === String(index + 1) }"
              >
                <input 
                  type="radio" 
                  :name="'question-' + currentQuestion.id" 
                  :value="index + 1"
                  @change="selectAnswer(currentQuestion.id, String(index + 1))"
                />
                <span class="option-label">{{ getOptionLabel(index) }}</span>
                <span class="option-text">{{ option }}</span>
              </label>
            </div>
          </div>

          <div class="navigation-buttons">
            <button 
              class="nav-btn prev-btn" 
              :disabled="currentIndex === 0"
              @click="prevQuestion"
            >
              上一题
            </button>
            <button 
              v-if="currentIndex < questions.length - 1"
              class="nav-btn next-btn"
              @click="nextQuestion"
            >
              下一题
            </button>
            <button 
              v-else
              class="nav-btn submit-btn"
              @click="submitAnswers"
            >
              提交测评
            </button>
          </div>
        </div>

        <div class="loading-state" v-if="loading">
          <div class="loading-spinner"></div>
          <p>加载中...</p>
        </div>

        <div class="result-area" v-else-if="isFinished">
          <div class="result-card">
            <div class="result-header">
              <span class="result-icon">🎉</span>
              <h2>测评完成!</h2>
            </div>
            <div class="result-score">
              <div class="score-circle">
                <span class="score-value">{{ finalScore }}</span>
                <span class="score-unit">分</span>
              </div>
              <div class="score-level">{{ scoreLevel }}</div>
            </div>
            <div class="result-content">
              <h3>测评报告</h3>
              <p>{{ reportContent }}</p>
              <div class="recommendations">
                <h4>发展建议</h4>
                <ul>
                  <li v-for="(item, index) in recommendations" :key="index">{{ item }}</li>
                </ul>
              </div>
            </div>
            <button class="back-btn-large" @click="goBack">返回测评列表</button>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getAssessmentById, getQuestionsByAssessmentId, submitAnswers as submitAssessmentAnswers, getReportById } from '@/api/assessment'

const route = useRoute()
const assessmentId = ref(Number(route.params.id))

const assessment = ref({
  id: 1,
  name: '职业兴趣测评',
  description: '了解您的职业兴趣方向',
  dimension: 'INTEREST',
  duration: 20
})

const questions = ref([])
const currentIndex = ref(0)
const answers = ref({})
const isFinished = ref(false)
const loading = ref(false)
const finalScore = ref(0)
const scoreLevel = ref('')
const reportContent = ref('')
const recommendations = ref([])
const userId = ref(1)

const currentQuestion = computed(() => questions.value[currentIndex.value])

const getDimensionLabel = (dimension) => {
  const labels = { 'INTEREST': '职业兴趣', 'PERSONALITY': '性格特质', 'ABILITY': '能力倾向', 'VALUE': '职业价值观' }
  return labels[dimension] || dimension
}

const getQuestionTypeLabel = (type) => {
  return type === 'SINGLE' ? '单选题' : '多选题'
}

const getOptionLabel = (index) => {
  return ['A', 'B', 'C', 'D', 'E', 'F'][index] || String(index + 1)
}

const selectAnswer = (questionId, value) => {
  answers.value[questionId] = value
}

const prevQuestion = () => {
  if (currentIndex.value > 0) {
    currentIndex.value--
  }
}

const nextQuestion = () => {
  if (currentIndex.value < questions.value.length - 1) {
    currentIndex.value++
  }
}

const submitAnswers = async () => {
  const totalQuestions = questions.value.length
  const answeredCount = Object.keys(answers.value).length
  
  if (answeredCount < totalQuestions) {
    alert('请完成所有题目后再提交')
    return
  }

  loading.value = true
  
  try {
    const answerList = Object.keys(answers.value).map(questionId => ({
      questionId: Number(questionId),
      answer: answers.value[questionId]
    }))
    
    const response = await submitAssessmentAnswers(userId.value, { assessmentId: assessmentId.value, answers: answerList })
    
    const reportId = response.reportId
    const report = await getReportById(reportId)
    
    const content = JSON.parse(report.content)
    finalScore.value = content.score || 0
    scoreLevel.value = content.level || '未知'
    reportContent.value = content.description || `您在职业兴趣维度的得分为${finalScore.value}分，属于${scoreLevel.value}水平。`
    recommendations.value = content.recommendations || [
      '建议您继续探索自己感兴趣的领域',
      '可以参加相关行业的实习或实践活动',
      '多与行业人士交流，了解真实工作内容',
      '制定职业发展计划，逐步实现目标'
    ]
  } catch (error) {
    console.error('提交测评失败:', error)
    
    let score = 0
    const values = Object.values(answers.value)
    values.forEach(v => {
      score += parseInt(v) * 20
    })
    finalScore.value = Math.round(score / totalQuestions)
    
    if (finalScore.value >= 80) scoreLevel.value = '优秀'
    else if (finalScore.value >= 60) scoreLevel.value = '良好'
    else if (finalScore.value >= 40) scoreLevel.value = '中等'
    else scoreLevel.value = '需提升'

    reportContent.value = `您在职业兴趣维度的得分为${finalScore.value}分，属于${scoreLevel.value}水平。这表明您${scoreLevel.value === '优秀' ? '在职业兴趣方面有明确的方向和强烈的驱动力。' : scoreLevel.value === '良好' ? '有一定的职业兴趣认知，但仍有探索空间。' : '需要进一步探索和了解自己的职业兴趣。'}`

    recommendations.value = [
      '建议您继续探索自己感兴趣的领域',
      '可以参加相关行业的实习或实践活动',
      '多与行业人士交流，了解真实工作内容',
      '制定职业发展计划，逐步实现目标'
    ]
  } finally {
    loading.value = false
    isFinished.value = true
  }
}

const goBack = () => {
  window.location.href = '/assessments'
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  window.location.href = '/login'
}

const loadAssessment = async () => {
  loading.value = true
  try {
    const user = localStorage.getItem('userInfo')
    if (user) {
      const userInfo = JSON.parse(user)
      userId.value = userInfo.id || userInfo.userId || 1
    }
    
    const assessmentData = await getAssessmentById(assessmentId.value)
    assessment.value = assessmentData
    
    const questionsData = await getQuestionsByAssessmentId(assessmentId.value)
    questions.value = questionsData.map(q => ({
      ...q,
      optionsArray: q.options.split(',')
    }))
  } catch (error) {
    console.error('加载测评失败:', error)
    assessment.value = {
      id: 1,
      name: '职业兴趣测评',
      description: '了解您的职业兴趣方向',
      dimension: 'INTEREST',
      duration: 20
    }
    questions.value = [
      { id: 1, content: '您更喜欢哪种工作环境?', questionType: 'SINGLE', options: '安静独立的办公室,团队协作的开放式空间,创意自由的工作室,户外或现场工作', optionsArray: ['安静独立的办公室', '团队协作的开放式空间', '创意自由的工作室', '户外或现场工作'] },
      { id: 2, content: '您更倾向于从事哪类工作?', questionType: 'SINGLE', options: '数据分析和研究,创意设计和艺术,与人沟通和协调,动手操作和实践', optionsArray: ['数据分析和研究', '创意设计和艺术', '与人沟通和协调', '动手操作和实践'] },
      { id: 3, content: '您在团队中通常扮演什么角色?', questionType: 'SINGLE', options: '决策者和领导者,创意提供者,执行者和协调者,分析者和评估者', optionsArray: ['决策者和领导者', '创意提供者', '执行者和协调者', '分析者和评估者'] },
      { id: 4, content: '您最喜欢的学习方式是?', questionType: 'SINGLE', options: '阅读和研究,观察和模仿,实践和体验,讨论和交流', optionsArray: ['阅读和研究', '观察和模仿', '实践和体验', '讨论和交流'] },
      { id: 5, content: '您理想的工作状态是?', questionType: 'SINGLE', options: '专注深入,灵活多变,稳定有序,充满挑战', optionsArray: ['专注深入', '灵活多变', '稳定有序', '充满挑战'] }
    ]
  } finally {
    loading.value = false
  }
}

onMounted(() => {
  loadAssessment()
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

.back-btn {
  padding: 0.5rem 1rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.back-btn:hover {
  background-color: #e0e8ff;
}

.top-header h1 {
  color: #333;
  font-size: 1.5rem;
}

.progress-info {
  color: #666;
}

.assessment-detail {
  max-width: 800px;
  margin: 0 auto;
}

.assessment-info {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.info-label {
  color: #666;
}

.info-value {
  color: #333;
  font-weight: 500;
}

.loading-state {
  text-align: center;
  padding: 4rem;
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f0f4ff;
  border-top: 4px solid #4a69bd;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto 1rem;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.question-card {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.question-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 1.5rem;
}

.question-number {
  color: #4a69bd;
  font-weight: 500;
}

.question-type {
  padding: 0.25rem 0.75rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 20px;
  font-size: 0.85rem;
}

.question-content {
  font-size: 1.2rem;
  color: #333;
  margin-bottom: 2rem;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 1rem;
  padding: 1rem;
  border: 2px solid #e0e0e0;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.option-item:hover {
  border-color: #4a69bd;
}

.option-item.selected {
  border-color: #4a69bd;
  background-color: #f0f4ff;
}

.option-item input {
  width: 20px;
  height: 20px;
}

.option-label {
  width: 24px;
  height: 24px;
  display: flex;
  justify-content: center;
  align-items: center;
  background-color: #e0e0e0;
  border-radius: 50%;
  color: #666;
  font-weight: 500;
}

.option-item.selected .option-label {
  background-color: #4a69bd;
  color: white;
}

.option-text {
  flex: 1;
  color: #333;
}

.navigation-buttons {
  display: flex;
  justify-content: flex-end;
  gap: 1rem;
  margin-top: 2rem;
}

.nav-btn {
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.prev-btn {
  background-color: #f0f0f0;
  color: #666;
}

.prev-btn:hover:not(:disabled) {
  background-color: #e0e0e0;
}

.prev-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.next-btn {
  background-color: #4a69bd;
  color: white;
}

.next-btn:hover {
  background-color: #3d5a9e;
}

.submit-btn {
  background-color: #28a745;
  color: white;
}

.submit-btn:hover {
  background-color: #218838;
}

.result-card {
  background-color: white;
  border-radius: 12px;
  padding: 2rem;
  text-align: center;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.result-header {
  margin-bottom: 2rem;
}

.result-icon {
  font-size: 3rem;
  display: block;
  margin-bottom: 1rem;
}

.result-header h2 {
  color: #333;
}

.result-score {
  margin-bottom: 2rem;
}

.score-circle {
  width: 150px;
  height: 150px;
  border-radius: 50%;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin: 0 auto 1rem;
}

.score-value {
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
}

.score-unit {
  color: rgba(255, 255, 255, 0.8);
}

.score-level {
  font-size: 1.2rem;
  color: #4a69bd;
  font-weight: 500;
}

.result-content {
  text-align: left;
  padding: 1.5rem;
  background-color: #f8f9fa;
  border-radius: 8px;
  margin-bottom: 2rem;
}

.result-content h3 {
  color: #333;
  margin-bottom: 1rem;
}

.result-content p {
  color: #666;
  line-height: 1.6;
}

.recommendations {
  margin-top: 1.5rem;
}

.recommendations h4 {
  color: #333;
  margin-bottom: 1rem;
}

.recommendations ul {
  list-style: none;
  padding: 0;
}

.recommendations li {
  padding: 0.5rem 0;
  color: #666;
  border-bottom: 1px solid #e0e0e0;
}

.back-btn-large {
  padding: 0.75rem 2rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.back-btn-large:hover {
  background-color: #3d5a9e;
}
</style>