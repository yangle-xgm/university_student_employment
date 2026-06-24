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
              <div class="resume-header">
                <h3>{{ resume.name }}</h3>
                <el-tag
                  :type="getStatusType(resume.status)"
                  size="small"
                  effect="light"
                  round
                >
                  {{ getStatusLabel(resume.status) }}
                </el-tag>
              </div>
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
            <div class="resume-title-row">
              <h1>{{ selectedResume.name }}</h1>
              <el-tag type="primary" effect="light" round>{{ selectedResume.desiredPosition }}</el-tag>
            </div>
            <div class="resume-contact">
              <span><el-icon><Phone /></el-icon>{{ selectedResume.phone }}</span>
              <span class="divider">•</span>
              <span><el-icon><Message /></el-icon>{{ selectedResume.email }}</span>
              <span class="divider">•</span>
              <span><el-icon><Location /></el-icon>{{ selectedResume.location }}</span>
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
  background-color: var(--color-bg);
  border-radius: var(--radius);
  padding: var(--space-4);
  cursor: pointer;
  transition: background-color var(--t-fast) var(--ease-default),
              box-shadow var(--t-normal) var(--ease-default);
  border: 2px solid transparent;
}

.resume-card:hover {
  background-color: var(--color-card-bg);
  box-shadow: var(--elevation-2);
}

.resume-card.active {
  background-color: var(--color-card-bg);
  border-color: var(--color-primary);
  box-shadow: var(--elevation-2);
}

.resume-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: var(--space-2);
  gap: var(--space-2);
}

.resume-header h3 {
  color: var(--color-text-1);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  margin: 0;
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
  margin-bottom: var(--space-3);
  flex-wrap: wrap;
}

.meta-item {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.resume-actions {
  display: flex;
  gap: var(--space-2);
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
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  padding: var(--space-8) var(--space-6);
  color: var(--color-text-inverse);
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
}

.resume-contact {
  display: flex;
  align-items: center;
  gap: var(--space-2);
  flex-wrap: wrap;
  opacity: 0.9;
}

.resume-contact span {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
}

.resume-contact .divider {
  opacity: 0.6;
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
  font-weight: var(--font-semibold);
  padding-bottom: var(--space-2);
  border-bottom: 2px solid var(--color-primary-light);
}

.detail-section p {
  color: var(--color-text-2);
  line-height: 1.7;
}

.timeline {
  position: relative;
  padding-left: 20px;
}

.timeline::before {
  content: '';
  position: absolute;
  left: 5px;
  top: 0;
  bottom: 0;
  width: 2px;
  background-color: var(--color-border);
}

.timeline-item {
  position: relative;
  margin-bottom: var(--space-4);
}

.timeline-item:last-child {
  margin-bottom: 0;
}

.timeline-dot {
  position: absolute;
  left: -17px;
  top: 3px;
  width: 10px;
  height: 10px;
  background-color: var(--color-primary);
  border-radius: 50%;
  border: 3px solid var(--color-card-bg);
  box-shadow: 0 0 0 2px var(--color-primary);
}

.timeline-content {
  background-color: var(--color-bg);
  padding: var(--space-4);
  border-radius: var(--radius);
}

.timeline-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: var(--space-1);
  gap: var(--space-2);
  flex-wrap: wrap;
}

.timeline-title {
  color: var(--color-text-1);
  font-weight: var(--font-medium);
}

.timeline-date {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.timeline-subtitle {
  color: var(--color-text-2);
  font-size: var(--text-sm);
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

.projects-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.project-item {
  background-color: var(--color-bg);
  padding: var(--space-4);
  border-radius: var(--radius);
}

.project-item h4 {
  color: var(--color-text-1);
  margin: 0 0 var(--space-2) 0;
  font-weight: var(--font-semibold);
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
}

@media (max-width: 640px) {
  .form-row {
    grid-template-columns: 1fr;
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
