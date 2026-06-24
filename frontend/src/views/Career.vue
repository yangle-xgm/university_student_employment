<template>
  <AppLayout title="职业规划">
    <div class="career-page">
      <div class="plans-section">
        <SectionHeader title="我的职业规划">
          <el-button type="primary" @click="showAddPlanModal = true">+ 创建规划</el-button>
        </SectionHeader>

        <div class="plans-list" v-if="plans.length > 0">
          <BaseCard
            v-for="plan in plans"
            :key="plan.id"
            class="plan-card"
            @click="selectPlan(plan)"
          >
            <div class="plan-header">
              <h3>{{ plan.title }}</h3>
              <el-tag :type="getStatusType(plan.status)">{{ getStatusLabel(plan.status) }}</el-tag>
            </div>
            <p class="plan-description">{{ plan.description }}</p>
            <div class="plan-meta">
              <span class="meta-item">
                <el-icon><Aim /></el-icon>
                {{ plan.careerGoal }}
              </span>
              <span class="meta-item">
                <el-icon><Calendar /></el-icon>
                {{ plan.targetDate }}
              </span>
            </div>
            <div class="plan-progress">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: getPlanProgress(plan) + '%' }"></div>
              </div>
              <span class="progress-text">{{ getPlanProgress(plan) }}%</span>
            </div>
          </BaseCard>
        </div>

        <EmptyState
          v-else
          icon="Aim"
          title="还没有创建职业规划"
          description="开始规划你的职业路径，让目标更清晰"
        >
          <el-button type="primary" @click="showAddPlanModal = true">创建第一个规划</el-button>
        </EmptyState>
      </div>

      <div class="milestones-section" v-if="selectedPlan">
        <SectionHeader :title="selectedPlan.title + ' - 里程碑'">
          <el-button type="primary" @click="showAddMilestoneModal = true">+ 添加里程碑</el-button>
        </SectionHeader>

        <div class="milestones-list">
          <BaseCard v-for="(milestone, index) in milestones" :key="milestone.id" class="milestone-card">
            <div class="milestone-number">{{ index + 1 }}</div>
            <div class="milestone-content">
              <div class="milestone-header">
                <h4>{{ milestone.title }}</h4>
                <span class="milestone-due">截止: {{ milestone.dueDate }}</span>
              </div>
              <p class="milestone-description">{{ milestone.description }}</p>
              <div class="milestone-progress-container">
                <div class="milestone-progress-bar">
                  <div class="milestone-progress-fill" :style="{ width: milestone.progress + '%' }"></div>
                </div>
                <span class="milestone-progress-text">{{ milestone.progress }}%</span>
              </div>
              <div class="milestone-actions">
                <el-button type="primary" plain size="small" @click="handleUpdateMilestoneProgress(milestone)">更新进度</el-button>
                <el-button type="danger" plain size="small" @click="handleDeleteMilestone(milestone.id)">删除</el-button>
              </div>
            </div>
          </BaseCard>
        </div>
      </div>
    </div>

    <el-dialog title="创建职业规划" v-model="showAddPlanModal" width="500px">
      <form @submit.prevent="handleCreatePlan" class="modal-form">
        <div class="form-group">
          <label>规划标题</label>
          <el-input v-model="planForm.title" placeholder="请输入规划标题" />
        </div>
        <div class="form-group">
          <label>职业目标</label>
          <el-input v-model="planForm.careerGoal" placeholder="例如：成为高级软件工程师" />
        </div>
        <div class="form-group">
          <label>目标日期</label>
          <el-input v-model="planForm.targetDate" type="date" />
        </div>
        <div class="form-group">
          <label>规划描述</label>
          <el-input v-model="planForm.description" type="textarea" placeholder="请描述您的职业规划..." :rows="3" />
        </div>
      </form>
      <template #footer>
        <el-button @click="showAddPlanModal = false">取消</el-button>
        <el-button type="primary" @click="handleCreatePlan">创建</el-button>
      </template>
    </el-dialog>

    <el-dialog title="添加里程碑" v-model="showAddMilestoneModal" width="500px">
      <form @submit.prevent="handleAddMilestone" class="modal-form">
        <div class="form-group">
          <label>里程碑标题</label>
          <el-input v-model="milestoneForm.title" placeholder="请输入里程碑标题" />
        </div>
        <div class="form-group">
          <label>截止日期</label>
          <el-input v-model="milestoneForm.dueDate" type="date" />
        </div>
        <div class="form-group">
          <label>里程碑描述</label>
          <el-input v-model="milestoneForm.description" type="textarea" placeholder="请描述这个里程碑..." :rows="3" />
        </div>
      </form>
      <template #footer>
        <el-button @click="showAddMilestoneModal = false">取消</el-button>
        <el-button type="primary" @click="handleAddMilestone">添加</el-button>
      </template>
    </el-dialog>
  </AppLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getPlansByStudentId, createPlan, getMilestones, addMilestone, updateMilestoneProgress, deleteMilestone, deletePlan } from '@/api/career'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
import EmptyState from '@/components/common/EmptyState.vue'

const userInfo = ref({})
const plans = ref([])
const selectedPlan = ref(null)
const milestones = ref([])
const userId = ref(1)

const showAddPlanModal = ref(false)
const showAddMilestoneModal = ref(false)

const planForm = reactive({
  title: '',
  careerGoal: '',
  targetDate: '',
  description: ''
})

const milestoneForm = reactive({
  title: '',
  dueDate: '',
  description: ''
})

const getStatusLabel = (status) => {
  const labels = {
    'IN_PROGRESS': '进行中',
    'COMPLETED': '已完成',
    'PLANNED': '规划中'
  }
  return labels[status] || status
}

const getStatusType = (status) => {
  const types = {
    'IN_PROGRESS': 'warning',
    'COMPLETED': 'success',
    'PLANNED': 'info'
  }
  return types[status] || 'info'
}

const getPlanProgress = (plan) => {
  const planMilestones = milestones.value.filter(m => m.planId === plan.id)
  if (planMilestones.length === 0) return 0
  const total = planMilestones.reduce((sum, m) => sum + m.progress, 0)
  return Math.round(total / planMilestones.length)
}

const selectPlan = async (plan) => {
  selectedPlan.value = plan
  await loadMilestones(plan.id)
}

const loadPlans = async () => {
  try {
    const data = await getPlansByStudentId(userId.value)
    plans.value = data
    if (plans.value.length > 0) {
      selectedPlan.value = plans.value[0]
      await loadMilestones(selectedPlan.value.id)
    }
  } catch (error) {
    console.error('加载职业规划失败:', error)
    plans.value = [
      { id: 1, title: '三年职业发展规划', careerGoal: '高级软件工程师', targetDate: '2027-06-30', description: '通过系统学习和项目实践，在三年内成长为高级软件工程师', status: 'IN_PROGRESS' }
    ]
    selectedPlan.value = plans.value[0]
    milestones.value = [
      { id: 1, planId: 1, title: '掌握Java高级特性', dueDate: '2024-12-31', description: '深入学习Java并发编程、JVM调优、设计模式', progress: 80 },
      { id: 2, planId: 1, title: '完成三个企业级项目', dueDate: '2025-06-30', description: '参与并主导至少三个企业级项目开发', progress: 50 },
      { id: 3, planId: 1, title: '获取技术认证', dueDate: '2026-06-30', description: '获取PMP或相关技术认证', progress: 10 }
    ]
  }
}

const loadMilestones = async (planId) => {
  try {
    const data = await getMilestones(planId)
    milestones.value = data
  } catch (error) {
    console.error('加载里程碑失败:', error)
    milestones.value = milestones.value.filter(m => m.planId === planId)
  }
}

const handleCreatePlan = async () => {
  try {
    const response = await createPlan(userId.value, {
      title: planForm.title,
      careerGoal: planForm.careerGoal,
      targetDate: planForm.targetDate,
      description: planForm.description
    })

    plans.value.push(response)
    selectedPlan.value = response
    milestones.value = []
  } catch (error) {
    console.error('创建职业规划失败:', error)
    const newPlan = {
      id: Date.now(),
      title: planForm.title,
      careerGoal: planForm.careerGoal,
      targetDate: planForm.targetDate,
      description: planForm.description,
      status: 'PLANNED'
    }
    plans.value.push(newPlan)
    selectedPlan.value = newPlan
    milestones.value = []
  }

  showAddPlanModal.value = false
  planForm.title = ''
  planForm.careerGoal = ''
  planForm.targetDate = ''
  planForm.description = ''
}

const handleAddMilestone = async () => {
  if (!selectedPlan.value) return

  try {
    const response = await addMilestone(selectedPlan.value.id, {
      title: milestoneForm.title,
      dueDate: milestoneForm.dueDate,
      description: milestoneForm.description,
      progress: 0
    })
    milestones.value.push(response)
  } catch (error) {
    console.error('添加里程碑失败:', error)
    const newMilestone = {
      id: Date.now(),
      planId: selectedPlan.value.id,
      title: milestoneForm.title,
      dueDate: milestoneForm.dueDate,
      description: milestoneForm.description,
      progress: 0
    }
    milestones.value.push(newMilestone)
  }

  showAddMilestoneModal.value = false
  milestoneForm.title = ''
  milestoneForm.dueDate = ''
  milestoneForm.description = ''
}

const handleUpdateMilestoneProgress = async (milestone) => {
  const input = window.prompt('请输入新的进度 (0-100):', milestone.progress)
  if (input === null) return

  const progress = parseInt(input, 10)
  if (isNaN(progress) || progress < 0 || progress > 100) {
    alert('请输入 0 到 100 之间的数字')
    return
  }

  try {
    await updateMilestoneProgress(milestone.id, progress)
    milestone.progress = progress
  } catch (error) {
    console.error('更新里程碑进度失败:', error)
    milestone.progress = progress
  }
}

const handleDeleteMilestone = async (milestoneId) => {
  if (!window.confirm('确定要删除这个里程碑吗？')) return

  try {
    await deleteMilestone(milestoneId)
    milestones.value = milestones.value.filter(m => m.id !== milestoneId)
  } catch (error) {
    console.error('删除里程碑失败:', error)
    milestones.value = milestones.value.filter(m => m.id !== milestoneId)
  }
}

onMounted(async () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    userId.value = userInfo.value.id || userInfo.value.userId || 1
  }

  await loadPlans()
})
</script>

<style scoped>
.career-page {
  display: grid;
  grid-template-columns: 380px 1fr;
  gap: var(--space-6);
  align-items: start;
}

.plans-section,
.milestones-section {
  min-width: 0;
}

.plans-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.plan-card {
  cursor: pointer;
  transition: transform var(--t-normal) var(--ease-default),
              box-shadow var(--t-normal) var(--ease-default);
}

.plan-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--elevation-2);
}

.plan-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
}

.plan-header h3 {
  color: var(--color-text-1);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin: 0;
}

.plan-description {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  line-height: 1.5;
  margin: 0 0 var(--space-4) 0;
}

.plan-meta {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-4);
  margin-bottom: var(--space-4);
}

.meta-item {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  color: var(--color-text-2);
  font-size: var(--text-sm);
}

.meta-item .el-icon {
  color: var(--color-primary);
}

.plan-progress {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.progress-bar {
  flex: 1;
  height: 6px;
  background-color: var(--color-border-light);
  border-radius: var(--radius-full);
  overflow: hidden;
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
  font-size: var(--text-sm);
  min-width: 36px;
  text-align: right;
}

.milestones-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.milestone-card {
  display: flex;
  gap: var(--space-4);
  align-items: flex-start;
}

.milestone-number {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  color: var(--color-text-inverse);
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: var(--font-bold);
  font-size: var(--text-sm);
  flex-shrink: 0;
}

.milestone-content {
  flex: 1;
  min-width: 0;
}

.milestone-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: var(--space-3);
  margin-bottom: var(--space-2);
}

.milestone-header h4 {
  color: var(--color-text-1);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  margin: 0;
}

.milestone-due {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.milestone-description {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  line-height: 1.5;
  margin: 0 0 var(--space-4) 0;
}

.milestone-progress-container {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-4);
}

.milestone-progress-bar {
  flex: 1;
  height: 6px;
  background-color: var(--color-border-light);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.milestone-progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: var(--radius-full);
  transition: width var(--t-normal) var(--ease-default);
}

.milestone-progress-text {
  color: var(--color-primary);
  font-weight: var(--font-semibold);
  font-size: var(--text-sm);
  min-width: 36px;
  text-align: right;
}

.milestone-actions {
  display: flex;
  gap: var(--space-3);
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.form-group label {
  font-weight: var(--font-medium);
  color: var(--color-text-2);
  font-size: var(--text-sm);
}

@media (max-width: 1024px) {
  .career-page {
    grid-template-columns: 1fr;
  }
}
</style>
