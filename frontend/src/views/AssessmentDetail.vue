<template>
  <AppLayout :title="assessment.name || '测评详情'">
    <div class="assessment-detail">
      <SectionHeader title="测评详情">
        <el-button @click="goBack">
          <el-icon class="btn-icon"><ArrowLeft /></el-icon>
          返回
        </el-button>
      </SectionHeader>

      <BaseCard class="assessment-info">
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
      </BaseCard>

      <div class="progress-info">
        <span>进度: {{ currentIndex + 1 }}/{{ questions.length }}</span>
      </div>

      <div class="question-area" v-if="!isFinished && !loading && questions.length > 0">
        <BaseCard class="question-card">
          <div class="question-header">
            <span class="question-number">第 {{ currentIndex + 1 }} 题</span>
            <el-tag type="primary">{{ currentQuestion ? getQuestionTypeLabel(currentQuestion.questionType) : '' }}</el-tag>
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
        </BaseCard>

        <div class="navigation-buttons">
          <el-button
            :disabled="currentIndex === 0"
            @click="prevQuestion"
          >
            上一题
          </el-button>
          <el-button
            v-if="currentIndex < questions.length - 1"
            type="primary"
            @click="nextQuestion"
          >
            下一题
          </el-button>
          <el-button
            v-else
            type="success"
            @click="submitAnswers"
          >
            提交测评
          </el-button>
        </div>
      </div>

      <div class="loading-state" v-if="loading">
        <div class="loading-spinner"></div>
        <p>加载中...</p>
      </div>

      <div class="result-area" v-else-if="isFinished">
        <BaseCard class="result-card">
          <div class="result-header">
            <el-icon :size="48" class="result-icon"><Trophy /></el-icon>
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
          <el-button type="primary" size="large" @click="goBack">返回测评列表</el-button>
        </BaseCard>
      </div>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
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
.assessment-detail {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.assessment-info .info-row {
  display: flex;
  justify-content: space-between;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border-light);
}

.assessment-info .info-row:last-child {
  border-bottom: none;
}

.info-label {
  color: var(--color-text-2);
}

.info-value {
  color: var(--color-text-1);
  font-weight: var(--font-medium);
}

.progress-info {
  color: var(--color-text-2);
  font-size: var(--text-sm);
}

.question-area {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.question-card {
  padding: var(--space-6);
}

.question-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-4);
}

.question-number {
  color: var(--color-primary);
  font-weight: var(--font-semibold);
}

.question-content {
  color: var(--color-text-1);
  font-size: var(--text-lg);
  margin-bottom: var(--space-6);
  line-height: 1.5;
}

.options-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.option-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-4);
  border: 2px solid var(--color-border);
  border-radius: var(--radius);
  cursor: pointer;
  transition: all var(--t-normal) var(--ease-default);
}

.option-item:hover {
  border-color: var(--color-primary);
  background-color: var(--color-primary-light);
}

.option-item.selected {
  border-color: var(--color-primary);
  background-color: var(--color-primary-light);
}

.option-item input[type="radio"] {
  display: none;
}

.option-label {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  background-color: var(--color-bg);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-semibold);
  color: var(--color-text-2);
  flex-shrink: 0;
}

.option-item.selected .option-label {
  background-color: var(--color-primary);
  color: var(--color-text-inverse);
}

.option-text {
  color: var(--color-text-1);
  flex: 1;
}

.navigation-buttons {
  display: flex;
  justify-content: space-between;
  gap: var(--space-3);
}

.loading-state {
  text-align: center;
  padding: calc(var(--space-8) * 2);
}

.loading-state p {
  color: var(--color-text-2);
}

.loading-spinner {
  width: 40px;
  height: 40px;
  border: 4px solid var(--color-primary-light);
  border-top: 4px solid var(--color-primary);
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin: 0 auto var(--space-4);
}

@keyframes spin {
  to { transform: rotate(360deg); }
}

.result-area {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.result-card {
  padding: var(--space-8);
  text-align: center;
}

.result-header {
  margin-bottom: var(--space-6);
}

.result-icon {
  color: var(--color-warning);
  margin-bottom: var(--space-3);
}

.result-header h2 {
  color: var(--color-text-1);
  margin: 0;
}

.result-score {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-6);
}

.score-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: var(--color-text-inverse);
}

.score-value {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
}

.score-unit {
  font-size: var(--text-sm);
}

.score-level {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-primary);
}

.result-content {
  text-align: left;
  margin-bottom: var(--space-6);
}

.result-content h3 {
  color: var(--color-text-1);
  margin-bottom: var(--space-3);
  font-size: var(--text-lg);
}

.result-content p {
  color: var(--color-text-2);
  line-height: 1.7;
  margin-bottom: var(--space-5);
}

.recommendations h4 {
  color: var(--color-text-1);
  margin-bottom: var(--space-3);
  font-size: var(--text-base);
}

.recommendations ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.recommendations li {
  padding: var(--space-2) 0;
  color: var(--color-text-2);
  padding-left: var(--space-5);
  position: relative;
}

.recommendations li::before {
  content: '✓';
  position: absolute;
  left: 0;
  color: var(--color-success);
  font-weight: var(--font-bold);
}

.btn-icon {
  margin-right: var(--space-1);
}

@media (max-width: 640px) {
  .navigation-buttons {
    flex-direction: column-reverse;
  }

  .navigation-buttons .el-button {
    width: 100%;
  }

  .question-card {
    padding: var(--space-4);
  }

  .result-card {
    padding: var(--space-5);
  }
}
</style>
