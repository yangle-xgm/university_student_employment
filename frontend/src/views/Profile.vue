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
        <router-link to="/profile" class="nav-item active">
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
        <h1>个人资料</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="profile-content">
        <div class="profile-card">
          <div class="profile-header">
            <div class="avatar">
              <span class="avatar-icon">👤</span>
            </div>
            <div class="profile-info">
              <h2>{{ userInfo?.username }}</h2>
              <p class="user-role">学生</p>
            </div>
            <button class="edit-btn" @click="showEditModal = true">
              编辑资料
            </button>
          </div>

          <div class="profile-details">
            <div class="detail-section">
              <h3>基本信息</h3>
              <div class="detail-row">
                <label>用户名</label>
                <span>{{ profile?.username || userInfo?.username }}</span>
              </div>
              <div class="detail-row">
                <label>邮箱</label>
                <span>{{ profile?.email || userInfo?.email }}</span>
              </div>
              <div class="detail-row">
                <label>手机号</label>
                <span>{{ profile?.phone || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <label>注册时间</label>
                <span>{{ profile?.createdAt || userInfo?.createdAt || '未知' }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h3>教育背景</h3>
              <div class="detail-row">
                <label>学校</label>
                <span>{{ profile?.school || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <label>专业</label>
                <span>{{ profile?.major || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <label>学历</label>
                <span>{{ profile?.education || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <label>毕业时间</label>
                <span>{{ profile?.graduationDate || '未填写' }}</span>
              </div>
            </div>

            <div class="detail-section">
              <h3>求职意向</h3>
              <div class="detail-row">
                <label>期望职位</label>
                <span>{{ profile?.desiredPosition || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <label>期望地点</label>
                <span>{{ profile?.desiredLocation || '未填写' }}</span>
              </div>
              <div class="detail-row">
                <label>期望薪资</label>
                <span>{{ profile?.desiredSalary || '未填写' }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="profile-card">
          <h3>技能特长</h3>
          <div class="skills-container">
            <div v-for="skill in skills" :key="skill" class="skill-tag">
              {{ skill }}
            </div>
          </div>
        </div>
      </div>

      <el-dialog title="编辑个人资料" v-model="showEditModal" width="500px">
        <form @submit.prevent="handleSave" class="edit-form">
          <div class="form-row">
            <div class="form-group">
              <label>用户名</label>
              <el-input v-model="editForm.username" />
            </div>
            <div class="form-group">
              <label>邮箱</label>
              <el-input v-model="editForm.email" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>手机号</label>
              <el-input v-model="editForm.phone" />
            </div>
            <div class="form-group">
              <label>学历</label>
              <el-input v-model="editForm.education" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>学校</label>
              <el-input v-model="editForm.school" />
            </div>
            <div class="form-group">
              <label>专业</label>
              <el-input v-model="editForm.major" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>期望职位</label>
              <el-input v-model="editForm.desiredPosition" />
            </div>
            <div class="form-group">
              <label>期望地点</label>
              <el-input v-model="editForm.desiredLocation" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>期望薪资</label>
              <el-input v-model="editForm.desiredSalary" />
            </div>
            <div class="form-group">
              <label>毕业时间</label>
              <el-input v-model="editForm.graduationDate" type="date" />
            </div>
          </div>
          <div class="form-group">
            <label>技能特长</label>
            <el-input v-model="editForm.skills" placeholder="多个技能用逗号分隔" />
          </div>
        </form>
        <template #footer>
          <el-button @click="showEditModal = false">取消</el-button>
          <el-button type="primary" @click="handleSave">保存</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getStudentInfo, updateStudentProfile } from '@/api/student'

const userInfo = ref({})
const profile = ref({})
const skills = ref([])
const showEditModal = ref(false)
const loading = ref(false)

const editForm = reactive({
  username: '',
  email: '',
  phone: '',
  education: '',
  school: '',
  major: '',
  desiredPosition: '',
  desiredLocation: '',
  desiredSalary: '',
  graduationDate: '',
  skills: ''
})

const userId = ref(1)

onMounted(async () => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
    const storedUserId = userInfo.value.id || userInfo.value.userId
    if (storedUserId) {
      userId.value = storedUserId
    }
  }
  
  await loadProfile()
})

const loadProfile = async () => {
  try {
    const data = await getStudentInfo(userId.value)
    profile.value = data
    
    if (data.skills) {
      skills.value = data.skills.split(',').map(s => s.trim())
    }
  } catch (error) {
    console.error('加载个人资料失败:', error)
  }
}

const handleSave = async () => {
  loading.value = true
  try {
    const data = {
      username: editForm.username,
      email: editForm.email,
      phone: editForm.phone,
      education: editForm.education,
      school: editForm.school,
      major: editForm.major,
      desiredPosition: editForm.desiredPosition,
      desiredLocation: editForm.desiredLocation,
      desiredSalary: editForm.desiredSalary,
      graduationDate: editForm.graduationDate,
      skills: editForm.skills
    }
    
    await updateStudentProfile(userId.value, data)
    
    profile.value = { ...profile.value, ...data }
    if (editForm.skills) {
      skills.value = editForm.skills.split(',').map(s => s.trim())
    }
    
    showEditModal.value = false
  } catch (error) {
    console.error('保存个人资料失败:', error)
  } finally {
    loading.value = false
  }
}

const handleLogout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('userInfo')
  window.location.href = '/login'
}
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

.profile-content {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 1.5rem;
}

.profile-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: 1.5rem;
  padding-bottom: 1.5rem;
  border-bottom: 1px solid #eee;
  margin-bottom: 1.5rem;
}

.avatar {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.avatar-icon {
  font-size: 2.5rem;
}

.profile-info h2 {
  color: #333;
  margin-bottom: 0.25rem;
}

.user-role {
  color: #666;
  font-size: 0.9rem;
}

.edit-btn {
  margin-left: auto;
  padding: 0.5rem 1.5rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.edit-btn:hover {
  background-color: #3d5a9e;
}

.profile-details {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.detail-section h3 {
  color: #333;
  margin-bottom: 1rem;
  font-size: 1rem;
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: 0.75rem 0;
  border-bottom: 1px solid #f0f0f0;
}

.detail-row label {
  color: #666;
  font-size: 0.9rem;
}

.detail-row span {
  color: #333;
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
  font-size: 0.85rem;
}

.edit-form {
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
}</style>