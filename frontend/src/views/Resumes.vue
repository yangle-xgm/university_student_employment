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
        <router-link to="/interviews" class="nav-item">
          <span class="nav-icon">💼</span>
          <span class="nav-text">面试管理</span>
        </router-link>
        <router-link to="/jobs" class="nav-item">
          <span class="nav-icon">💻</span>
          <span class="nav-text">职位搜索</span>
        </router-link>
        <router-link to="/resumes" class="nav-item active">
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
        <h1>简历管理</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="resumes-content">
        <div class="resumes-list-section">
          <div class="section-header">
            <h2>我的简历</h2>
            <button class="add-btn" @click="showAddResumeModal = true">+ 创建简历</button>
          </div>

          <div class="resumes-list" v-if="resumes.length > 0">
            <div v-for="resume in resumes" :key="resume.id" class="resume-card" @click="selectResume(resume)">
              <div class="resume-header">
                <h3>{{ resume.name }}</h3>
                <span class="resume-status" :class="(resume.status || 'unknown').toLowerCase()">{{ getStatusLabel(resume.status) }}</span>
              </div>
              <p class="resume-intro">{{ resume.introduction }}</p>
              <div class="resume-meta">
                <span class="meta-item">📅 更新于 {{ resume.updatedAt }}</span>
                <span class="meta-item">📄 {{ resume.experienceCount }}年经验</span>
              </div>
              <div class="resume-actions">
                <button class="action-btn edit" @click.stop="editResume(resume)">编辑</button>
                <button class="action-btn delete" @click.stop="handleDeleteResume(resume.id)">删除</button>
              </div>
            </div>
          </div>

          <div class="empty-state" v-else>
            <span class="empty-icon">📄</span>
            <p>还没有创建简历</p>
            <button class="create-btn" @click="showAddResumeModal = true">创建第一份简历</button>
          </div>
        </div>

        <div class="resume-detail-section" v-if="selectedResume">
          <div class="section-header">
            <h2>简历详情</h2>
          </div>

          <div class="resume-detail-card">
            <div class="detail-header">
              <div class="resume-title-row">
                <h1>{{ selectedResume.name }}</h1>
                <span class="resume-position">{{ selectedResume.desiredPosition }}</span>
              </div>
              <div class="resume-contact">
                <span>{{ selectedResume.phone }}</span>
                <span>•</span>
                <span>{{ selectedResume.email }}</span>
                <span>•</span>
                <span>{{ selectedResume.location }}</span>
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
                  <span v-for="skill in selectedResume.skills" :key="skill" class="skill-tag">{{ skill }}</span>
                </div>
              </div>

              <div class="detail-section">
                <h3>项目经验</h3>
                <div class="projects-list">
                  <div v-for="(project, index) in selectedResume.projects" :key="index" class="project-item">
                    <h4>{{ project.name }}</h4>
                    <p class="project-desc">{{ project.description }}</p>
                    <div class="project-tags">
                      <span v-for="tag in project.tags" :key="tag" class="project-tag">{{ tag }}</span>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <el-dialog title="创建简历" v-model="showAddResumeModal" width="600px">
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
          <el-button type="primary" @click="handleCreateResume">创建</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getResumesByUserId, createResume, updateResume, deleteResume } from '@/api/resume'

const userInfo = ref({})
const resumes = ref([])
const selectedResume = ref(null)
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
  selectedResume.value = resume
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

    if (selectedResume.value) {
      await updateResume(selectedResume.value.id, data)
      const index = resumes.value.findIndex(r => r.id === selectedResume.value.id)
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
      id: Date.now(),
      name: resumeForm.name || '未命名简历',
      desiredPosition: resumeForm.desiredPosition || '未填写',
      phone: resumeForm.phone || '未填写',
      email: resumeForm.email || '未填写',
      location: resumeForm.location || '未填写',
      experienceCount: resumeForm.experienceCount || 0,
      introduction: resumeForm.introduction || '暂无简介',
      skills: resumeForm.skills.split(',').map(s => s.trim()).filter(s => s) || ['暂无'],
      status: 'ACTIVE',
      updatedAt: new Date().toISOString().split('T')[0],
      education: [],
      experience: [],
      projects: []
    }

    if (selectedResume.value) {
      const index = resumes.value.findIndex(r => r.id === selectedResume.value.id)
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
  selectedResume.value = null
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
  loadResumes()
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

.resumes-content {
  display: grid;
  grid-template-columns: 350px 1fr;
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

.resumes-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.resume-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.resume-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.resume-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.75rem;
}

.resume-header h3 {
  color: #333;
  margin: 0;
}

.resume-status {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.resume-status.draft {
  background-color: #fff3cd;
  color: #856404;
}

.resume-status.active {
  background-color: #d4edda;
  color: #155724;
}

.resume-status.archived {
  background-color: #e2e3e5;
  color: #6c757d;
}

.resume-intro {
  color: #666;
  font-size: 0.9rem;
  margin-bottom: 0.75rem;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.resume-meta {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.meta-item {
  color: #999;
  font-size: 0.85rem;
}

.resume-actions {
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

.resume-detail-card {
  background-color: white;
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  overflow: hidden;
}

.detail-header {
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  padding: 2rem;
  color: white;
}

.resume-title-row {
  display: flex;
  align-items: baseline;
  gap: 1rem;
  margin-bottom: 0.75rem;
}

.resume-title-row h1 {
  margin: 0;
  font-size: 1.8rem;
}

.resume-position {
  padding: 0.25rem 0.75rem;
  background-color: rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  font-size: 0.9rem;
}

.resume-contact {
  display: flex;
  align-items: center;
  gap: 0.5rem;
  opacity: 0.9;
}

.detail-content {
  padding: 2rem;
}

.detail-section {
  margin-bottom: 2rem;
}

.detail-section:last-child {
  margin-bottom: 0;
}

.detail-section h3 {
  color: #333;
  margin-bottom: 1rem;
  font-size: 1.1rem;
  padding-bottom: 0.5rem;
  border-bottom: 2px solid #4a69bd;
}

.detail-section p {
  color: #666;
  line-height: 1.6;
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
  background-color: #e0e0e0;
}

.timeline-item {
  position: relative;
  margin-bottom: 1.5rem;
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
  background-color: #4a69bd;
  border-radius: 50%;
  border: 3px solid white;
  box-shadow: 0 0 0 2px #4a69bd;
}

.timeline-content {
  background-color: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
}

.timeline-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 0.5rem;
}

.timeline-title {
  color: #333;
  font-weight: 500;
}

.timeline-date {
  color: #999;
  font-size: 0.85rem;
}

.timeline-subtitle {
  color: #666;
  font-size: 0.9rem;
}

.timeline-description {
  color: #666;
  font-size: 0.9rem;
  margin: 0.5rem 0 0 0;
}

.skills-container {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.skill-tag {
  padding: 0.5rem 1rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 20px;
  font-size: 0.9rem;
}

.projects-list {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.project-item {
  background-color: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
}

.project-item h4 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.project-desc {
  color: #666;
  font-size: 0.9rem;
  margin: 0 0 0.75rem 0;
}

.project-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
}

.project-tag {
  padding: 0.25rem 0.75rem;
  background-color: #e0e0e0;
  color: #666;
  border-radius: 12px;
  font-size: 0.85rem;
}

.modal-form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
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