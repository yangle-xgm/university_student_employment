<template>
  <AppLayout title="简历管理">
    <div class="resumes-page">
      <div class="resumes-content">
        <!-- 左侧简历列表 -->
        <BaseCard class="resumes-list-card">
          <SectionHeader title="我的简历">
            <el-button type="primary" :icon="Plus" @click="openCreateModal">创建简历</el-button>
          </SectionHeader>

          <div v-if="resumes.length > 0" class="resumes-list">
            <div
              v-for="resume in resumes"
              :key="resume.id"
              class="resume-card"
              :class="{ active: selectedResume?.id === resume.id }"
              @click="selectResume(resume)"
            >
              <div class="resume-card-glow"></div>
              <div class="resume-card-main">
                <div class="resume-avatar" :style="{ background: getAvatarGradient(resume.name) }">
                  {{ getInitials(resume.name) }}
                </div>
                <div class="resume-card-body">
                  <div class="resume-header">
                    <h3>{{ resume.name }}</h3>
                    <div class="resume-status" :class="resume.status?.toLowerCase()">
                      <span class="status-dot"></span>
                      {{ getStatusLabel(resume.status) }}
                    </div>
                  </div>
                  <p class="resume-position">{{ resume.desiredPosition }}</p>
                  <p class="resume-intro">{{ resume.introduction }}</p>
                  <div class="resume-meta">
                    <span class="meta-item">
                      <el-icon><Clock /></el-icon>
                      更新于 {{ resume.updatedAt }}
                    </span>
                    <span class="meta-item">
                      <el-icon><Briefcase /></el-icon>
                      {{ resume.experienceCount }}年经验
                    </span>
                  </div>
                </div>
              </div>
              <div class="resume-actions" @click.stop>
                <el-button size="small" type="primary" plain round :icon="Edit" @click="editResume(resume)">编辑</el-button>
                <el-button size="small" type="danger" plain round :icon="Delete" @click="handleDeleteResume(resume.id)">删除</el-button>
              </div>
            </div>
          </div>

          <EmptyState
            v-else
            icon="Document"
            title="还没有创建简历"
            description="创建你的第一份简历，开启求职之旅"
          >
            <el-button type="primary" :icon="Plus" @click="openCreateModal">创建第一份简历</el-button>
          </EmptyState>
        </BaseCard>

        <!-- 右侧简历详情 -->
        <BaseCard v-if="selectedResume" class="resume-detail-card">
          <div class="detail-header">
            <div class="detail-header-bg"></div>
            <div class="detail-header-content">
              <div class="detail-avatar" :style="{ background: getAvatarGradient(selectedResume.name) }">
                {{ getInitials(selectedResume.name) }}
              </div>
              <div class="detail-header-info">
                <div class="resume-title-row">
                  <h1>{{ selectedResume.name }}</h1>
                  <el-tag type="primary" effect="light" round class="position-tag">{{ selectedResume.desiredPosition }}</el-tag>
                </div>
                <div class="resume-contact">
                  <span><el-icon><Phone /></el-icon>{{ selectedResume.phone }}</span>
                  <span class="divider">•</span>
                  <span><el-icon><Message /></el-icon>{{ selectedResume.email }}</span>
                  <span class="divider">•</span>
                  <span><el-icon><Location /></el-icon>{{ selectedResume.location }}</span>
                  <span class="divider">•</span>
                  <span><el-icon><Briefcase /></el-icon>{{ selectedResume.experienceCount }}年经验</span>
                </div>
              </div>
            </div>
          </div>

          <div class="detail-content">
            <div class="detail-section">
              <h3>个人简介</h3>
              <p>{{ selectedResume.introduction }}</p>
            </div>

            <div class="detail-section">
              <h3>教育背景</h3>
              <div class="timeline">
                <div v-for="(edu, index) in selectedResume.education" :key="index" class="timeline-item">
                  <div class="timeline-dot"></div>
                  <div class="timeline-content">
                    <div class="timeline-header">
                      <span class="timeline-title">{{ edu.school }}</span>
                      <span class="timeline-date">{{ edu.startDate }} - {{ edu.endDate }}</span>
                    </div>
                    <span class="timeline-subtitle">{{ edu.degree }} - {{ edu.major }}</span>
                  </div>
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h3>工作经验</h3>
              <div class="timeline">
                <div v-for="(exp, index) in selectedResume.experience" :key="index" class="timeline-item">
                  <div class="timeline-dot"></div>
                  <div class="timeline-content">
                    <div class="timeline-header">
                      <span class="timeline-title">{{ exp.company }} - {{ exp.position }}</span>
                      <span class="timeline-date">{{ exp.startDate }} - {{ exp.endDate }}</span>
                    </div>
                    <p class="timeline-description">{{ exp.description }}</p>
                  </div>
                </div>
              </div>
            </div>

            <div class="detail-section">
              <h3>专业技能</h3>
              <div class="skills-container">
                <el-tag v-for="skill in selectedResume.skills" :key="skill" type="primary" effect="light" round>
                  {{ skill }}
                </el-tag>
              </div>
            </div>

            <div class="detail-section">
              <h3>项目经验</h3>
              <div class="projects-list">
                <div v-for="(project, index) in selectedResume.projects" :key="index" class="project-item">
                  <h4>{{ project.name }}</h4>
                  <p class="project-desc">{{ project.description }}</p>
                  <div class="project-tags">
                    <el-tag v-for="tag in project.tags" :key="tag" type="info" effect="light" size="small" round>
                      {{ tag }}
                    </el-tag>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </BaseCard>

        <BaseCard v-else class="resume-detail-card empty-detail">
          <EmptyState icon="Document" title="选择一份简历查看详情" />
        </BaseCard>
      </div>

      <!-- 创建/编辑简历弹窗 -->
      <el-dialog
        :title="selectedResumeForEdit ? '编辑简历' : '创建简历'"
        v-model="showAddResumeModal"
        width="640px"
        destroy-on-close
      >
        <form @submit.prevent="handleCreateResume" class="modal-form">
          <div class="form-row">
            <div class="form-group">
              <label>姓名</label>
              <el-input v-model="resumeForm.name" placeholder="请输入姓名" />
            </div>
            <div class="form-group">
              <label>期望职位</label>
              <el-input v-model="resumeForm.desiredPosition" placeholder="请输入期望职位" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>手机号</label>
              <el-input v-model="resumeForm.phone" placeholder="请输入手机号" />
            </div>
            <div class="form-group">
              <label>邮箱</label>
              <el-input v-model="resumeForm.email" placeholder="请输入邮箱" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>所在地</label>
              <el-input v-model="resumeForm.location" placeholder="请输入所在地" />
            </div>
            <div class="form-group">
              <label>工作经验</label>
              <el-input v-model="resumeForm.experienceCount" type="number" placeholder="工作年限" />
            </div>
          </div>
          <div class="form-group">
            <label>个人简介</label>
            <el-input v-model="resumeForm.introduction" type="textarea" placeholder="请介绍一下自己..." :rows="3" />
          </div>
          <div class="form-group">
            <label>专业技能</label>
            <el-input v-model="resumeForm.skills" placeholder="多个技能用逗号分隔" />
          </div>
        </form>
        <template #footer>
          <el-button @click="showAddResumeModal = false">取消</el-button>
          <el-button type="primary" @click="handleCreateResume">
            {{ selectedResumeForEdit ? '保存' : '创建' }}
          </el-button>
        </template>
      </el-dialog>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import { getResumesByUserId, createResume, updateResume, deleteResume } from '@/api/resume'
import { Plus, Edit, Delete, Phone, Message, Location, Clock, Briefcase } from '@element-plus/icons-vue'

const userInfo = ref({})
const resumes = ref([])
const selectedResume = ref(null)
const selectedResumeForEdit = ref(null)
const userId = ref(1)

const showAddResumeModal = ref(false)

const resumeForm = reactive({
  name: '',
  desiredPosition: '',
  phone: '',
  email: '',
  location: '',
  experienceCount: 0,
  introduction: '',
  skills: ''
})

const getStatusLabel = (status) => {
  const labels = {
    'DRAFT': '草稿',
    'ACTIVE': '激活',
    'ARCHIVED': '已归档'
  }
  return labels[status] || status
}

const getStatusType = (status) => {
  const types = {
    'DRAFT': 'warning',
    'ACTIVE': 'success',
    'ARCHIVED': 'info'
  }
  return types[status] || 'info'
}

const getInitials = (name) => {
  if (!name) return '?'
  return name.slice(0, 2)
}

const getAvatarGradient = (name) => {
  const gradients = [
    'linear-gradient(135deg, #2563EB 0%, #7C3AED 100%)',
    'linear-gradient(135deg, #0891B2 0%, #2563EB 100%)',
    'linear-gradient(135deg, #059669 0%, #10B981 100%)',
    'linear-gradient(135deg, #7C3AED 0%, #DB2777 100%)',
    'linear-gradient(135deg, #EA580C 0%, #F59E0B 100%)'
  ]
  let hash = 0
  for (let i = 0; i < (name || '').length; i++) {
    hash = name.charCodeAt(i) + ((hash << 5) - hash)
  }
  return gradients[Math.abs(hash) % gradients.length]
}

const openCreateModal = () => {
  selectedResumeForEdit.value = null
  resetForm()
  showAddResumeModal.value = true
}

const selectResume = (resume) => {
  selectedResume.value = resume
}

const editResume = (resume) => {
  resumeForm.name = resume.name
  resumeForm.desiredPosition = resume.desiredPosition
  resumeForm.phone = resume.phone
  resumeForm.email = resume.email
  resumeForm.location = resume.location
  resumeForm.experienceCount = resume.experienceCount
  resumeForm.introduction = resume.introduction
  resumeForm.skills = resume.skills.join ? resume.skills.join(',') : ''
  selectedResumeForEdit.value = resume
  showAddResumeModal.value = true
}

const handleCreateResume = async () => {
  try {
    const data = {
      name: resumeForm.name,
      desiredPosition: resumeForm.desiredPosition,
      phone: resumeForm.phone,
      email: resumeForm.email,
      location: resumeForm.location,
      experienceCount: resumeForm.experienceCount,
      introduction: resumeForm.introduction,
      skills: resumeForm.skills.split(',').map(s => s.trim()).filter(s => s)
    }

    if (selectedResumeForEdit.value) {
      await updateResume(selectedResumeForEdit.value.id, data)
      const index = resumes.value.findIndex(r => r.id === selectedResumeForEdit.value.id)
      if (index >= 0) {
        resumes.value[index] = { ...resumes.value[index], ...data, status: 'ACTIVE' }
        selectedResume.value = resumes.value[index]
      }
    } else {
      const response = await createResume(data)
      resumes.value.push(response)
      selectedResume.value = response
    }
  } catch (error) {
    console.error('保存简历失败:', error)

    const newResume = {
      id: selectedResumeForEdit.value ? selectedResumeForEdit.value.id : Date.now(),
      name: resumeForm.name || '未命名简历',
      desiredPosition: resumeForm.desiredPosition || '未填写',
      phone: resumeForm.phone || '未填写',
      email: resumeForm.email || '未填写',
      location: resumeForm.location || '未填写',
      experienceCount: resumeForm.experienceCount || 0,
      introduction: resumeForm.introduction || '暂无简介',
      skills: resumeForm.skills.split(',').map(s => s.trim()).filter(s => s).length
        ? resumeForm.skills.split(',').map(s => s.trim()).filter(s => s)
        : ['暂无'],
      status: 'ACTIVE',
      updatedAt: new Date().toISOString().split('T')[0],
      education: [],
      experience: [],
      projects: []
    }

    if (selectedResumeForEdit.value) {
      const index = resumes.value.findIndex(r => r.id === selectedResumeForEdit.value.id)
      if (index >= 0) {
        resumes.value[index] = newResume
        selectedResume.value = newResume
      }
    } else {
      resumes.value.push(newResume)
      selectedResume.value = newResume
    }
  }

  showAddResumeModal.value = false
  resetForm()
}

const handleDeleteResume = async (resumeId) => {
  try {
    await deleteResume(resumeId)
    resumes.value = resumes.value.filter(r => r.id !== resumeId)
    if (selectedResume.value && selectedResume.value.id === resumeId) {
      selectedResume.value = null
    }
  } catch (error) {
    console.error('删除简历失败:', error)
    resumes.value = resumes.value.filter(r => r.id !== resumeId)
    if (selectedResume.value && selectedResume.value.id === resumeId) {
      selectedResume.value = null
    }
  }
}

const resetForm = () => {
  resumeForm.name = ''
  resumeForm.desiredPosition = ''
  resumeForm.phone = ''
  resumeForm.email = ''
  resumeForm.location = ''
  resumeForm.experienceCount = 0
  resumeForm.introduction = ''
  resumeForm.skills = ''
  selectedResumeForEdit.value = null
}

const loadResumes = async () => {
  try {
    const data = await getResumesByUserId(userId.value)
    resumes.value = data
    if (resumes.value.length > 0) {
      selectedResume.value = resumes.value[0]
    }
  } catch (error) {
    console.error('加载简历列表失败:', error)
    resumes.value = [
      {
        id: 1,
        name: '张三',
        desiredPosition: 'Java开发工程师',
        phone: '13800138000',
        email: 'zhangsan@example.com',
        location: '北京',
        experienceCount: 3,
        introduction: '3年Java开发经验，熟悉Spring Boot、Spring Cloud等框架，有大型分布式系统开发经验。',
        skills: ['Java', 'Spring Boot', 'MySQL', 'Redis', 'Docker'],
        status: 'ACTIVE',
        updatedAt: '2024-01-15',
        education: [
          { school: '北京大学', degree: '本科', major: '计算机科学', startDate: '2017-09', endDate: '2021-06' }
        ],
        experience: [
          { company: '字节跳动', position: 'Java开发工程师', startDate: '2021-07', endDate: '至今', description: '负责核心业务系统开发，参与系统架构设计和优化。' }
        ],
        projects: [
          { name: '推荐系统重构', description: '负责推荐系统的架构重构，提升推荐准确率30%。', tags: ['Java', 'Redis', 'Kafka'] }
        ]
      }
    ]
    selectedResume.value = resumes.value[0]
  }
}

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    userId.value = userInfo.value.id || userInfo.value.userId || 1
  }
  loadResumes()
})
</script>

<style scoped>
.resumes-page {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.resumes-content {
  display: grid;
  grid-template-columns: 380px 1fr;
  gap: var(--space-6);
  align-items: start;
}

.resumes-list-card {
  padding: var(--space-5);
}

.resumes-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.resume-card {
  position: relative;
  background: var(--gradient-surface);
  border-radius: var(--radius-md);
  padding: var(--space-4);
  cursor: pointer;
  transition: transform var(--t-normal) var(--ease-smooth),
              box-shadow var(--t-normal) var(--ease-smooth),
              border-color var(--t-fast) var(--ease-default);
  border: 1px solid var(--color-border-light);
  overflow: hidden;
}

.resume-card-glow {
  position: absolute;
  top: 0;
  right: 0;
  width: 160px;
  height: 160px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.08) 0%, transparent 70%);
  border-radius: 50%;
  transform: translate(40%, -40%);
  opacity: 0;
  transition: opacity var(--t-normal) var(--ease-default);
  pointer-events: none;
}

.resume-card:hover {
  transform: translateY(-3px);
  box-shadow: var(--elevation-3);
  border-color: rgba(37, 99, 235, 0.12);
}

.resume-card:hover .resume-card-glow {
  opacity: 1;
}

.resume-card.active {
  background: #FFFFFF;
  border-color: var(--color-primary);
  box-shadow: var(--elevation-2), 0 0 0 1px rgba(37, 99, 235, 0.08);
}

.resume-card.active::before {
  content: '';
  position: absolute;
  left: 0;
  top: 0;
  bottom: 0;
  width: 3px;
  background: var(--gradient-primary);
}

.resume-card-main {
  display: flex;
  gap: var(--space-4);
  margin-bottom: var(--space-4);
}

.resume-avatar {
  width: 52px;
  height: 52px;
  border-radius: var(--radius);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-inverse);
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  flex-shrink: 0;
  box-shadow: var(--elevation-2);
}

.resume-card-body {
  flex: 1;
  min-width: 0;
}

.resume-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-1);
  gap: var(--space-2);
}

.resume-header h3 {
  color: var(--color-text-1);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  margin: 0;
}

.resume-status {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  font-size: var(--text-xs);
  font-weight: var(--font-medium);
  padding: 2px 8px;
  border-radius: var(--radius-full);
  background: var(--color-bg);
  color: var(--color-text-2);
}

.resume-status .status-dot {
  width: 6px;
  height: 6px;
  border-radius: 50%;
  background: currentColor;
}

.resume-status.active {
  background: var(--color-success-light);
  color: var(--color-success);
}

.resume-status.draft {
  background: var(--color-warning-light);
  color: var(--color-warning);
}

.resume-status.archived {
  background: var(--color-border-light);
  color: var(--color-text-3);
}

.resume-position {
  color: var(--color-primary);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  margin-bottom: var(--space-2);
}

.resume-intro {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin-bottom: var(--space-3);
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.resume-meta {
  display: flex;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.meta-item {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  color: var(--color-text-3);
  font-size: var(--text-xs);
}

.resume-actions {
  display: flex;
  gap: var(--space-2);
  padding-left: 68px;
}

.resume-detail-card {
  padding: 0;
  overflow: hidden;
}

.empty-detail {
  display: flex;
  align-items: center;
  justify-content: center;
  min-height: 500px;
}

.detail-header {
  position: relative;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  padding: var(--space-8) var(--space-6);
  color: var(--color-text-inverse);
  overflow: hidden;
}

.detail-header-bg {
  position: absolute;
  top: -50%;
  right: -10%;
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.12) 0%, transparent 60%);
  border-radius: 50%;
  pointer-events: none;
}

.detail-header-content {
  position: relative;
  display: flex;
  align-items: center;
  gap: var(--space-5);
  z-index: 1;
}

.detail-avatar {
  width: 84px;
  height: 84px;
  border-radius: var(--radius-lg);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-inverse);
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  flex-shrink: 0;
  box-shadow: 0 12px 32px rgba(15, 23, 42, 0.2);
  border: 3px solid rgba(255, 255, 255, 0.2);
}

.detail-header-info {
  flex: 1;
  min-width: 0;
}

.resume-title-row {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  margin-bottom: var(--space-3);
  flex-wrap: wrap;
}

.resume-title-row h1 {
  margin: 0;
  font-size: var(--text-2xl);
  font-weight: var(--font-bold);
  letter-spacing: -0.02em;
}

.position-tag {
  font-weight: var(--font-medium);
  background: rgba(255, 255, 255, 0.18);
  border-color: rgba(255, 255, 255, 0.25);
  color: var(--color-text-inverse);
}

.resume-contact {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-wrap: wrap;
  opacity: 0.92;
  font-size: var(--text-sm);
}

.resume-contact span {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
}

.resume-contact .divider {
  opacity: 0.5;
}

.detail-content {
  padding: var(--space-6);
}

.detail-section {
  margin-bottom: var(--space-8);
}

.detail-section:last-child {
  margin-bottom: 0;
}

.detail-section h3 {
  color: var(--color-text-1);
  margin-bottom: var(--space-4);
  font-size: var(--text-base);
  font-weight: var(--font-bold);
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.detail-section h3::before {
  content: '';
  width: 4px;
  height: 18px;
  background: var(--gradient-primary);
  border-radius: var(--radius-full);
}

.detail-section h3::after {
  content: '';
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, var(--color-border) 0%, transparent 100%);
  margin-left: var(--space-2);
}

.detail-section p {
  color: var(--color-text-2);
  line-height: 1.7;
}

.timeline {
  position: relative;
  padding-left: 24px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 6px;
  top: 8px;
  bottom: 8px;
  width: 2px;
  background: linear-gradient(to bottom, var(--color-primary) 0%, var(--color-border) 100%);
  border-radius: var(--radius-full);
}

.timeline-item {
  position: relative;
  margin-bottom: var(--space-4);
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-item:last-child .timeline-content {
  margin-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -22px;
  top: 6px;
  width: 14px;
  height: 14px;
  background: var(--color-card-bg);
  border-radius: 50%;
  border: 3px solid var(--color-primary);
  box-shadow: 0 0 0 4px rgba(37, 99, 235, 0.08);
}

.timeline-content {
  background: var(--gradient-surface);
  padding: var(--space-4);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border-light);
  box-shadow: var(--elevation-1);
  transition: transform var(--t-normal) var(--ease-smooth),
              box-shadow var(--t-normal) var(--ease-smooth);
}

.timeline-content:hover {
  transform: translateX(4px);
  box-shadow: var(--elevation-2);
}

.timeline-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: var(--space-2);
  gap: var(--space-2);
  flex-wrap: wrap;
}

.timeline-title {
  color: var(--color-text-1);
  font-weight: var(--font-semibold);
  font-size: var(--text-base);
}

.timeline-date {
  color: var(--color-primary);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  background: var(--color-primary-light);
  padding: 2px 8px;
  border-radius: var(--radius-full);
}

.timeline-subtitle {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
}

.timeline-description {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin: var(--space-2) 0 0 0;
  line-height: 1.6;
}

.skills-container {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.skills-container :deep(.el-tag) {
  background: var(--gradient-primary-soft);
  border-color: rgba(37, 99, 235, 0.12);
  color: var(--color-primary);
  font-weight: var(--font-medium);
  transition: transform var(--t-fast) var(--ease-default),
              box-shadow var(--t-fast) var(--ease-default);
}

.skills-container :deep(.el-tag:hover) {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(37, 99, 235, 0.12);
}

.projects-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.project-item {
  background: var(--gradient-surface);
  padding: var(--space-5);
  border-radius: var(--radius-md);
  border: 1px solid var(--color-border-light);
  box-shadow: var(--elevation-1);
  transition: transform var(--t-normal) var(--ease-smooth),
              box-shadow var(--t-normal) var(--ease-smooth);
}

.project-item:hover {
  transform: translateY(-2px);
  box-shadow: var(--elevation-2);
}

.project-item h4 {
  color: var(--color-text-1);
  margin: 0 0 var(--space-2) 0;
  font-weight: var(--font-semibold);
  font-size: var(--text-base);
}

.project-desc {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin: 0 0 var(--space-3) 0;
  line-height: 1.6;
}

.project-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
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
  .resumes-content {
    grid-template-columns: 1fr;
  }

  .resume-detail-card {
    order: -1;
  }

  .resume-actions {
    padding-left: 0;
  }

  .resume-card-main {
    margin-bottom: var(--space-3);
  }
}

@media (max-width: 640px) {
  .form-row {
    grid-template-columns: 1fr;
  }

  .detail-header-content {
    flex-direction: column;
    align-items: flex-start;
    gap: var(--space-4);
  }

  .resume-title-row h1 {
    font-size: var(--text-xl);
  }

  .detail-header {
    padding: var(--space-6) var(--space-4);
  }

  .detail-content {
    padding: var(--space-4);
  }
}
</style>
