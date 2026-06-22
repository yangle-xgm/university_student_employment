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
        <router-link to="/career" class="nav-item active">
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
        <h1>职业规划</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="career-content">
        <div class="plans-section">
          <div class="section-header">
            <h2>我的职业规划</h2>
            <button class="add-btn" @click="showAddPlanModal = true">+ 创建规划</button>
          </div>
          
          <div class="plans-list" v-if="plans.length > 0">
            <div v-for="plan in plans" :key="plan.id" class="plan-card" @click="selectPlan(plan)">
              <div class="plan-header">
                <h3>{{ plan.title }}</h3>
                <span class="plan-status" :class="plan.status.toLowerCase()">{{ getStatusLabel(plan.status) }}</span>
              </div>
              <p class="plan-description">{{ plan.description }}</p>
              <div class="plan-meta">
                <span class="meta-item">🎯 {{ plan.careerGoal }}</span>
                <span class="meta-item">📅 {{ plan.targetDate }}</span>
              </div>
              <div class="plan-progress">
                <div class="progress-bar">
                  <div class="progress-fill" :style="{ width: getPlanProgress(plan) + '%' }"></div>
                </div>
                <span class="progress-text">{{ getPlanProgress(plan) }}%</span>
              </div>
            </div>
          </div>
          
          <div class="empty-state" v-else>
            <span class="empty-icon">🎯</span>
            <p>还没有创建职业规划</p>
            <button class="create-btn" @click="showAddPlanModal = true">创建第一个规划</button>
          </div>
        </div>

        <div class="milestones-section" v-if="selectedPlan">
          <div class="section-header">
            <h2>{{ selectedPlan.title }} - 里程碑</h2>
            <button class="add-btn" @click="showAddMilestoneModal = true">+ 添加里程碑</button>
          </div>
          
          <div class="milestones-list">
            <div v-for="(milestone, index) in milestones" :key="milestone.id" class="milestone-card">
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
                  <button class="action-btn edit" @click="handleUpdateMilestoneProgress(milestone)">更新进度</button>
                  <button class="action-btn delete" @click="handleDeleteMilestone(milestone.id)">删除</button>
                </div>
              </div>
            </div>
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
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getPlansByStudentId, createPlan, getMilestones, addMilestone, updateMilestoneProgress, deleteMilestone, deletePlan } from '@/api/career'

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
  const newProgress = Math.min(100, milestone.progress + 10)
  try {
    await updateMilestoneProgress(milestone.id, newProgress)
    milestone.progress = newProgress
  } catch (error) {
    console.error('更新里程碑进度失败:', error)
    milestone.progress = newProgress
  }
}

const handleDeleteMilestone = async (milestoneId) => {
  try {
    await deleteMilestone(milestoneId)
    milestones.value = milestones.value.filter(m => m.id !== milestoneId)
  } catch (error) {
    console.error('删除里程碑失败:', error)
    milestones.value = milestones.value.filter(m => m.id !== milestoneId)
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
  loadPlans()
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

.career-content {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 2rem;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 1.5rem;
}

.section-header h2 {
  color: #333;
  font-size: 1.2rem;
}

.add-btn {
  padding: 0.5rem 1.5rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.add-btn:hover {
  background-color: #3d5a9e;
}

.plans-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.plan-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.plan-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.plan-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.plan-header h3 {
  color: #333;
  margin: 0;
}

.plan-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.plan-status.in_progress {
  background-color: #fff3cd;
  color: #856404;
}

.plan-status.completed {
  background-color: #d4edda;
  color: #155724;
}

.plan-status.planned {
  background-color: #cce5ff;
  color: #004085;
}

.plan-description {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}

.plan-meta {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.meta-item {
  color: #666;
  font-size: 0.85rem;
}

.plan-progress {
  display: flex;
  align-items: center;
  gap: 0.75rem;
}

.progress-bar {
  flex: 1;
  height: 8px;
  background-color: #e0e0e0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, #4a69bd 0%, #6a89cc 100%);
  border-radius: 4px;
  transition: width 0.3s;
}

.progress-text {
  color: #4a69bd;
  font-weight: 500;
  font-size: 0.9rem;
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
  margin-bottom: 1.5rem;
}

.create-btn {
  padding: 0.75rem 2rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.create-btn:hover {
  background-color: #3d5a9e;
}

.milestones-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.milestone-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  display: flex;
  gap: 1rem;
}

.milestone-number {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: white;
  font-weight: bold;
  flex-shrink: 0;
}

.milestone-content {
  flex: 1;
}

.milestone-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.5rem;
}

.milestone-header h4 {
  color: #333;
  margin: 0;
}

.milestone-due {
  color: #999;
  font-size: 0.85rem;
}

.milestone-description {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 1rem;
}

.milestone-progress-container {
  display: flex;
  align-items: center;
  gap: 0.75rem;
  margin-bottom: 1rem;
}

.milestone-progress-bar {
  flex: 1;
  height: 6px;
  background-color: #e0e0e0;
  border-radius: 3px;
  overflow: hidden;
}

.milestone-progress-fill {
  height: 100%;
  background-color: #4a69bd;
  border-radius: 3px;
  transition: width 0.3s;
}

.milestone-progress-text {
  color: #4a69bd;
  font-weight: 500;
  font-size: 0.85rem;
}

.milestone-actions {
  display: flex;
  gap: 0.5rem;
}

.action-btn {
  padding: 0.375rem 1rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 0.85rem;
  transition: background-color 0.3s;
}

.action-btn.edit {
  background-color: #f0f4ff;
  color: #4a69bd;
}

.action-btn.edit:hover {
  background-color: #e0e8ff;
}

.action-btn.delete {
  background-color: #f8d7da;
  color: #721c24;
}

.action-btn.delete:hover {
  background-color: #f5c6cb;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.form-group label {
  font-weight: 500;
  color: #333;
}
</style>