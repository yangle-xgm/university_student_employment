<template>
  <AppLayout title="个人资料">
    <div class="profile-page">
      <BaseCard class="profile-main-card">
        <div class="profile-header">
          <div class="avatar">
            <el-icon :size="36"><UserFilled /></el-icon>
          </div>
          <div class="profile-info">
            <h2>{{ userInfo?.username }}</h2>
            <p class="user-role">学生</p>
          </div>
          <el-button type="primary" :icon="Edit" @click="showEditModal = true">编辑资料</el-button>
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
      </BaseCard>

      <BaseCard class="profile-skills-card">
        <SectionHeader title="技能特长" />
        <div class="skills-container">
          <el-tag v-for="skill in skills" :key="skill" type="primary" effect="light" round>
            {{ skill }}
          </el-tag>
          <span v-if="skills.length === 0" class="no-skills">暂无技能标签</span>
        </div>
      </BaseCard>

      <el-dialog title="编辑个人资料" v-model="showEditModal" width="560px" destroy-on-close>
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
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { Edit, UserFilled } from '@element-plus/icons-vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
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
</script>

<style scoped>
.profile-page {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: var(--space-6);
  align-items: start;
}

.profile-main-card {
  padding: var(--space-6);
}

.profile-header {
  display: flex;
  align-items: center;
  gap: var(--space-5);
  padding-bottom: var(--space-6);
  border-bottom: 1px solid var(--color-border-light);
  margin-bottom: var(--space-6);
}

.avatar {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
  color: var(--color-text-inverse);
}

.profile-info {
  flex: 1;
}

.profile-info h2 {
  color: var(--color-text-1);
  margin-bottom: var(--space-1);
}

.user-role {
  color: var(--color-text-2);
  font-size: var(--text-sm);
}

.profile-details {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: var(--space-5);
}

.detail-section h3 {
  color: var(--color-text-1);
  margin-bottom: var(--space-4);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  padding-bottom: var(--space-2);
  border-bottom: 2px solid var(--color-primary-light);
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border-light);
  gap: var(--space-3);
}

.detail-row:last-child {
  border-bottom: none;
}

.detail-row label {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.detail-row span {
  color: var(--color-text-1);
  font-weight: var(--font-medium);
  text-align: right;
}

.profile-skills-card {
  padding: var(--space-5);
}

.skills-container {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
}

.no-skills {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.edit-form {
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
  .profile-page {
    grid-template-columns: 1fr;
  }

  .profile-details {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 640px) {
  .profile-header {
    flex-direction: column;
    align-items: flex-start;
  }

  .form-row {
    grid-template-columns: 1fr;
  }
}
</style>
