<template>
  <AppLayout title="职业测评">
    <div class="assessment-page">
      <div class="assessment-stats">
        <BaseCard class="stat-card">
          <div class="stat-value">{{ assessments.length }}</div>
          <div class="stat-label">可用测评</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ completedCount }}</div>
          <div class="stat-label">已完成</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ inProgressCount }}</div>
          <div class="stat-label">进行中</div>
        </BaseCard>
      </div>

      <SectionHeader title="测评列表" />

      <div class="assessment-grid">
        <BaseCard
          v-for="assessment in assessments"
          :key="assessment.id"
          class="assessment-card"
          @click="goToDetail(assessment.id)"
        >
          <div class="assessment-icon" :class="`icon-${assessment.dimension.toLowerCase()}`">
            <el-icon :size="28">
              <component :is="getIconName(assessment.dimension)" />
            </el-icon>
          </div>
          <div class="assessment-content">
            <h3>{{ assessment.name }}</h3>
            <p class="assessment-dimension">{{ getDimensionLabel(assessment.dimension) }}</p>
            <p class="assessment-desc">{{ assessment.description }}</p>
          </div>
          <div class="assessment-info">
            <span class="duration">{{ assessment.duration }}分钟</span>
            <el-button type="primary" @click.stop="startAssessment(assessment.id)">开始测评</el-button>
          </div>
        </BaseCard>
      </div>

      <SectionHeader title="我的测评报告" />

      <div v-if="reports.length > 0" class="report-list">
        <BaseCard v-for="report in reports" :key="report.id" class="report-item">
          <div class="report-info">
            <h4>{{ getReportTitle(report) }}</h4>
            <p class="report-date">{{ report.generatedAt }}</p>
          </div>
          <div class="report-score">
            <span class="score-value">{{ getScore(report.content) }}分</span>
            <el-tag type="primary">{{ getLevel(report.content) }}</el-tag>
          </div>
          <el-button type="primary" plain @click="viewReport(report.id)">查看报告</el-button>
        </BaseCard>
      </div>
      <EmptyState
        v-else
        icon="Document"
        title="暂无测评报告"
        description="完成测评后将在此生成您的专属报告"
      />
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import { getAssessmentList, getReportsByStudentId } from '@/api/assessment'

const assessments = ref([])
const reports = ref([])
const loading = ref(false)

const userId = ref(1)

const completedCount = computed(() => reports.value.length)
const inProgressCount = ref(1)

onMounted(async () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    const userInfo = JSON.parse(user)
    const storedUserId = userInfo.id || userInfo.userId
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

const getIconName = (dimension) => {
  const icons = {
    'INTEREST': 'Brush',
    'PERSONALITY': 'User',
    'ABILITY': 'Lightning',
    'VALUE': 'Coin'
  }
  return icons[dimension] || 'Document'
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
</script>

<style scoped>
.assessment-page {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.assessment-stats {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-6);
}

.stat-card {
  text-align: center;
}

.stat-value {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-primary);
  margin-bottom: var(--space-1);
}

.stat-label {
  color: var(--color-text-2);
}

.assessment-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-6);
}

.assessment-card {
  display: flex;
  align-items: flex-start;
  gap: var(--space-5);
  cursor: pointer;
}

.assessment-icon {
  flex-shrink: 0;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 56px;
  height: 56px;
  border-radius: var(--radius-md);
  background-color: var(--color-primary-light);
  color: var(--color-primary);
}

.assessment-icon.icon-personality {
  background-color: var(--color-accent-light);
  color: var(--color-accent);
}

.assessment-icon.icon-ability {
  background-color: var(--color-warning-light);
  color: var(--color-warning);
}

.assessment-icon.icon-value {
  background-color: var(--color-success-light);
  color: var(--color-success);
}

.assessment-content {
  flex: 1;
  min-width: 0;
}

.assessment-content h3 {
  color: var(--color-text-1);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin-bottom: var(--space-2);
}

.assessment-dimension {
  color: var(--color-primary);
  font-size: var(--text-sm);
  margin-bottom: var(--space-2);
}

.assessment-desc {
  color: var(--color-text-2);
  font-size: var(--text-base);
  line-height: 1.5;
}

.assessment-info {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: space-between;
  gap: var(--space-3);
  flex-shrink: 0;
}

.duration {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.report-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.report-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-4);
}

.report-info {
  flex: 1;
  min-width: 0;
}

.report-info h4 {
  color: var(--color-text-1);
  font-size: var(--text-md);
  font-weight: var(--font-semibold);
  margin-bottom: var(--space-1);
}

.report-date {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.report-score {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  flex-shrink: 0;
}

.score-value {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-primary);
}

@media (max-width: 768px) {
  .assessment-stats {
    grid-template-columns: 1fr;
  }

  .assessment-grid {
    grid-template-columns: 1fr;
  }

  .assessment-card {
    flex-direction: column;
  }

  .assessment-info {
    align-items: flex-start;
    width: 100%;
  }

  .report-item {
    flex-direction: column;
    align-items: flex-start;
  }

  .report-score {
    width: 100%;
    justify-content: space-between;
  }
}
</style>
