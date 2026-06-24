<template>
  <AppLayout title="面试管理">
    <div class="interview-page">
      <div class="interview-stats">
        <BaseCard class="stat-card">
          <div class="stat-value">{{ upcomingCount }}</div>
          <div class="stat-label">即将到来</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ completedCount }}</div>
          <div class="stat-label">已完成</div>
        </BaseCard>
        <BaseCard class="stat-card">
          <div class="stat-value">{{ canceledCount }}</div>
          <div class="stat-label">已取消</div>
        </BaseCard>
      </div>

      <BaseCard class="filter-card">
        <el-tabs v-model="activeTab" class="filter-tabs">
          <el-tab-pane label="全部面试" name="all">
          </el-tab-pane>
          <el-tab-pane label="即将到来" name="upcoming">
          </el-tab-pane>
          <el-tab-pane label="已完成" name="completed">
          </el-tab-pane>
          <el-tab-pane label="已取消" name="canceled">
          </el-tab-pane>
        </el-tabs>
      </BaseCard>

      <div class="interview-list">
        <BaseCard v-for="interview in filteredInterviews" :key="interview.id" class="interview-card">
          <div class="interview-header">
            <div class="company-info">
              <el-icon :size="32" class="company-icon"><OfficeBuilding /></el-icon>
              <div class="company-detail">
                <h3>{{ interview.companyName }}</h3>
                <p class="position">{{ interview.position }}</p>
              </div>
            </div>
            <el-tag :type="getStatusType(interview.status)" effect="light" round>
              {{ getStatusLabel(interview.status) }}
            </el-tag>
          </div>

          <div class="interview-body">
            <div class="info-grid">
              <div class="info-item">
                <el-icon :size="18" class="info-icon"><Calendar /></el-icon>
                <div class="info-content">
                  <span class="info-label">面试时间</span>
                  <span class="info-value">{{ formatDate(interview.interviewDate) }} {{ interview.interviewTime }}</span>
                </div>
              </div>
              <div class="info-item">
                <el-icon :size="18" class="info-icon"><Location /></el-icon>
                <div class="info-content">
                  <span class="info-label">面试地点</span>
                  <span class="info-value">{{ interview.location }}</span>
                </div>
              </div>
              <div class="info-item">
                <el-icon :size="18" class="info-icon"><User /></el-icon>
                <div class="info-content">
                  <span class="info-label">面试官</span>
                  <span class="info-value">{{ interview.interviewer }}</span>
                </div>
              </div>
              <div class="info-item">
                <el-icon :size="18" class="info-icon"><Phone /></el-icon>
                <div class="info-content">
                  <span class="info-label">联系方式</span>
                  <span class="info-value">{{ interview.contactPhone }}</span>
                </div>
              </div>
            </div>

            <div class="interview-notes" v-if="interview.notes">
              <span class="notes-label">备注</span>
              <p>{{ interview.notes }}</p>
            </div>
          </div>

          <div class="interview-footer">
            <div class="action-buttons">
              <el-button type="primary" size="small" @click="viewDetails(interview)">查看详情</el-button>
              <el-button size="small" @click="editInterview(interview)">编辑</el-button>
              <el-button v-if="interview.status === 'UPCOMING'" type="danger" size="small" @click="handleCancelInterview(interview.id)">取消</el-button>
            </div>
          </div>
        </BaseCard>
      </div>

      <EmptyState
        v-if="filteredInterviews.length === 0"
        icon="Briefcase"
        title="暂无面试安排"
        description="目前还没有面试安排，快去投递简历吧"
      />

      <el-dialog title="面试详情" v-model="showDetailModal" width="600px">
        <div v-if="selectedInterview" class="detail-content">
          <div class="detail-header">
            <h2>{{ selectedInterview.companyName }}</h2>
            <p class="position">{{ selectedInterview.position }}</p>
          </div>
          <div class="detail-info">
            <div class="detail-row">
              <label>面试状态</label>
              <el-tag :type="getStatusType(selectedInterview.status)" effect="light" round class="status-badge">
                {{ getStatusLabel(selectedInterview.status) }}
              </el-tag>
            </div>
            <div class="detail-row">
              <label>面试时间</label>
              <span>{{ formatDate(selectedInterview.interviewDate) }} {{ selectedInterview.interviewTime }}</span>
            </div>
            <div class="detail-row">
              <label>面试地点</label>
              <span>{{ selectedInterview.location }}</span>
            </div>
            <div class="detail-row">
              <label>面试官</label>
              <span>{{ selectedInterview.interviewer }}</span>
            </div>
            <div class="detail-row">
              <label>联系方式</label>
              <span>{{ selectedInterview.contactPhone }}</span>
            </div>
            <div class="detail-row">
              <label>面试类型</label>
              <span>{{ getTypeLabel(selectedInterview.interviewType) }}</span>
            </div>
            <div class="detail-row notes">
              <label>备注</label>
              <p>{{ selectedInterview.notes || '无' }}</p>
            </div>
          </div>
        </div>
        <template #footer>
          <el-button @click="showDetailModal = false">关闭</el-button>
        </template>
      </el-dialog>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getInterviewsByUserId } from '@/api/interview'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
import EmptyState from '@/components/common/EmptyState.vue'
import { OfficeBuilding, Calendar, Location, User, Phone, Briefcase } from '@element-plus/icons-vue'

const userInfo = ref({})
const activeTab = ref('all')
const userId = ref(1)

const interviews = ref([])
const showDetailModal = ref(false)
const selectedInterview = ref(null)

const upcomingCount = computed(() => interviews.value.filter(i => i.status === 'UPCOMING').length)
const completedCount = computed(() => interviews.value.filter(i => i.status === 'COMPLETED').length)
const canceledCount = computed(() => interviews.value.filter(i => i.status === 'CANCELED').length)

const filteredInterviews = computed(() => {
  if (activeTab.value === 'all') return interviews.value
  if (activeTab.value === 'upcoming') return interviews.value.filter(i => i.status === 'UPCOMING')
  if (activeTab.value === 'completed') return interviews.value.filter(i => i.status === 'COMPLETED')
  if (activeTab.value === 'canceled') return interviews.value.filter(i => i.status === 'CANCELED')
  return interviews.value
})

const getStatusLabel = (status) => {
  const labels = {
    'UPCOMING': '即将到来',
    'COMPLETED': '已完成',
    'CANCELED': '已取消',
    'RESCHEDULED': '已改期'
  }
  return labels[status] || status
}

const getStatusClass = (status) => {
  return status ? status.toLowerCase() : 'unknown'
}

const getStatusType = (status) => {
  const types = {
    'UPCOMING': 'warning',
    'COMPLETED': 'success',
    'CANCELED': 'danger',
    'RESCHEDULED': 'info'
  }
  return types[status] || 'info'
}

const getTypeLabel = (type) => {
  const labels = {
    'ON_SITE': '现场面试',
    'VIDEO': '视频面试',
    'PHONE': '电话面试'
  }
  return labels[type] || type
}

const formatDate = (dateStr) => {
  const date = new Date(dateStr)
  return `${date.getFullYear()}年${date.getMonth() + 1}月${date.getDate()}日`
}

const viewDetails = (interview) => {
  selectedInterview.value = interview
  showDetailModal.value = true
}

const editInterview = (interview) => {
  selectedInterview.value = interview
  showDetailModal.value = true
}

const handleCancelInterview = async (interviewId) => {
  const interview = interviews.value.find(i => i.id === interviewId)
  if (interview) {
    try {
      interview.status = 'CANCELED'
    } catch (error) {
      console.error('取消面试失败:', error)
      interview.status = 'CANCELED'
    }
  }
}

const loadInterviews = async () => {
  try {
    const data = await getInterviewsByUserId(userId.value)
    interviews.value = data
  } catch (error) {
    console.error('加载面试列表失败:', error)
    interviews.value = [
      { id: 1, companyName: '阿里巴巴', position: 'Java开发工程师', interviewDate: '2024-02-15', interviewTime: '10:00', location: '杭州市西湖区文三路', interviewer: '张经理', contactPhone: '138****8888', interviewType: 'ON_SITE', status: 'UPCOMING', notes: '请准备项目经历介绍' },
      { id: 2, companyName: '腾讯', position: '前端开发工程师', interviewDate: '2024-02-10', interviewTime: '14:00', location: '深圳市南山区科技园', interviewer: '李总监', contactPhone: '139****9999', interviewType: 'VIDEO', status: 'COMPLETED', notes: '已完成一面，等待二面通知' },
      { id: 3, companyName: '字节跳动', position: '后端开发工程师', interviewDate: '2024-02-20', interviewTime: '09:30', location: '北京市海淀区中关村', interviewer: '王技术', contactPhone: '137****7777', interviewType: 'ON_SITE', status: 'UPCOMING', notes: '' },
      { id: 4, companyName: '美团', position: '测试开发工程师', interviewDate: '2024-02-08', interviewTime: '15:00', location: '北京市朝阳区望京', interviewer: '陈主管', contactPhone: '136****6666', interviewType: 'PHONE', status: 'CANCELED', notes: '因个人原因取消' }
    ]
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
  loadInterviews()
})
</script>

<style scoped>
.interview-page {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.interview-stats {
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
}

.stat-label {
  color: var(--color-text-2);
  margin-top: var(--space-1);
  font-size: var(--text-sm);
}

.filter-card {
  padding: var(--space-3) var(--space-5);
}

.filter-tabs :deep(.el-tabs__header) {
  margin-bottom: 0;
}

.interview-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.interview-card {
  padding: 0;
  overflow: hidden;
}

.interview-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding: var(--space-6);
  border-bottom: 1px solid var(--color-border-light);
}

.company-info {
  display: flex;
  gap: var(--space-4);
  align-items: center;
}

.company-icon {
  color: var(--color-primary);
}

.company-detail h3 {
  color: var(--color-text-1);
  margin: 0 0 var(--space-1) 0;
  font-size: var(--text-lg);
}

.position {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin: 0;
}

.interview-body {
  padding: var(--space-6);
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: var(--space-4);
}

.info-item {
  display: flex;
  gap: var(--space-3);
  align-items: flex-start;
}

.info-icon {
  color: var(--color-text-3);
  margin-top: var(--space-1);
}

.info-content {
  display: flex;
  flex-direction: column;
}

.info-label {
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

.info-value {
  color: var(--color-text-1);
  font-size: var(--text-base);
}

.interview-notes {
  margin-top: var(--space-4);
  padding: var(--space-4);
  background-color: var(--color-bg);
  border-radius: var(--radius);
}

.notes-label {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  margin-bottom: var(--space-2);
  display: block;
}

.interview-notes p {
  color: var(--color-text-1);
  margin: 0;
}

.interview-footer {
  padding: var(--space-4) var(--space-6);
  background-color: var(--color-bg);
}

.action-buttons {
  display: flex;
  gap: var(--space-3);
}

.detail-content {
  padding: var(--space-2);
}

.detail-header h2 {
  color: var(--color-text-1);
  margin: 0 0 var(--space-1) 0;
}

.detail-header .position {
  color: var(--color-primary);
}

.detail-info {
  margin-top: var(--space-6);
}

.detail-row {
  display: flex;
  justify-content: space-between;
  padding: var(--space-3) 0;
  border-bottom: 1px solid var(--color-border-light);
}

.detail-row label {
  color: var(--color-text-2);
}

.detail-row span {
  color: var(--color-text-1);
  font-weight: var(--font-medium);
}

.status-badge {
  font-size: var(--text-sm);
}

.detail-row.notes {
  flex-direction: column;
  gap: var(--space-2);
}

.detail-row.notes p {
  color: var(--color-text-1);
  margin: 0;
  line-height: 1.6;
}

@media (max-width: 768px) {
  .interview-stats {
    grid-template-columns: 1fr;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
