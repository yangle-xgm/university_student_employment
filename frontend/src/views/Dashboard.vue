<template>
  <AppLayout title="仪表盘">
    <div class="dashboard-page">
      <!-- KPI 卡片区 -->
      <div class="stats-grid">
        <KpiCard
          icon="DocumentChecked"
          :value="assessmentCount"
          label="已完成测评"
          hint="查看测评记录"
          :icon-bg="'var(--color-primary-light)'"
          :icon-color="'var(--color-primary)'"
          clickable
        />
        <KpiCard
          icon="Aim"
          :value="planCount"
          label="职业规划"
          hint="管理职业路径"
          :icon-bg="'var(--color-accent-light)'"
          :icon-color="'var(--color-accent)'"
          clickable
        />
        <KpiCard
          icon="Reading"
          :value="learningProgress + '%'"
          label="学习进度"
          hint="继续学习"
          :icon-bg="'var(--color-success-light)'"
          :icon-color="'var(--color-success)'"
          clickable
        />
        <KpiCard
          icon="Calendar"
          :value="interviewCount"
          label="面试安排"
          hint="查看日程"
          :icon-bg="'var(--color-warning-light)'"
          :icon-color="'var(--color-warning)'"
          clickable
        />
      </div>

      <!-- 最近活动 -->
      <BaseCard class="content-card">
        <SectionHeader title="最近活动" />
        <div class="activity-list">
          <div v-for="(activity, index) in activities" :key="index" class="activity-item">
            <div class="activity-icon" :style="{ backgroundColor: activity.iconBg, color: activity.iconColor }">
              <el-icon :size="18">
                <component :is="activity.icon" />
              </el-icon>
            </div>
            <div class="activity-content">
              <div class="activity-title">{{ activity.title }}</div>
              <div class="activity-time">{{ activity.time }}</div>
            </div>
          </div>
        </div>
      </BaseCard>

      <!-- 推荐学习资源 -->
      <BaseCard class="content-card">
        <SectionHeader title="推荐学习资源" subtitle="根据你的职业规划推荐" />
        <div class="resource-list">
          <div v-for="resource in resources" :key="resource.title" class="resource-card">
            <div class="resource-icon" :style="{ backgroundColor: resource.iconBg, color: resource.iconColor }">
              <el-icon :size="20">
                <component :is="resource.icon" />
              </el-icon>
            </div>
            <div class="resource-content">
              <div class="resource-title">{{ resource.title }}</div>
              <div class="resource-category">{{ resource.category }}</div>
            </div>
            <div class="resource-progress">
              <div class="progress-bar">
                <div class="progress-fill" :style="{ width: resource.progress + '%' }"></div>
              </div>
              <span class="progress-text">{{ resource.progress }}%</span>
            </div>
          </div>
        </div>
      </BaseCard>
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import KpiCard from '@/components/common/KpiCard.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'

const userInfo = ref({})
const assessmentCount = ref(3)
const planCount = ref(1)
const learningProgress = ref(45)
const interviewCount = ref(2)

const activities = [
  { title: '完成职业兴趣测评', time: '2小时前', icon: 'CircleCheck', iconBg: 'var(--color-success-light)', iconColor: 'var(--color-success)' },
  { title: '创建职业规划', time: '昨天', icon: 'EditPen', iconBg: 'var(--color-accent-light)', iconColor: 'var(--color-accent)' },
  { title: '学习Java基础课程', time: '2天前', icon: 'Reading', iconBg: 'var(--color-primary-light)', iconColor: 'var(--color-primary)' },
  { title: '预约面试', time: '3天前', icon: 'Calendar', iconBg: 'var(--color-warning-light)', iconColor: 'var(--color-warning)' }
]

const resources = [
  { title: 'Java编程入门', category: '编程基础', progress: 65, icon: 'Notebook', iconBg: 'var(--color-primary-light)', iconColor: 'var(--color-primary)' },
  { title: '前端开发实战', category: 'Web开发', progress: 40, icon: 'Monitor', iconBg: 'var(--color-accent-light)', iconColor: 'var(--color-accent)' },
  { title: '数据结构与算法', category: '计算机基础', progress: 20, icon: 'DataAnalysis', iconBg: 'var(--color-success-light)', iconColor: 'var(--color-success)' }
]

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
})
</script>

<style scoped>
.dashboard-page {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-5);
}

.content-card {
  padding: var(--space-5);
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.activity-item {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-4);
  background-color: var(--color-bg);
  border-radius: var(--radius);
}

.activity-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: var(--radius);
  flex-shrink: 0;
}

.activity-content {
  flex: 1;
}

.activity-title {
  font-weight: var(--font-medium);
  color: var(--color-text-1);
}

.activity-time {
  font-size: var(--text-sm);
  color: var(--color-text-3);
  margin-top: var(--space-1);
}

.resource-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-3);
}

.resource-card {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  padding: var(--space-4);
  background-color: var(--color-bg);
  border-radius: var(--radius);
}

.resource-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: var(--radius);
  flex-shrink: 0;
}

.resource-content {
  flex: 1;
}

.resource-title {
  font-weight: var(--font-medium);
  color: var(--color-text-1);
}

.resource-category {
  font-size: var(--text-sm);
  color: var(--color-text-2);
  margin-top: var(--space-1);
}

.resource-progress {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.progress-bar {
  width: 120px;
  height: 8px;
  background-color: var(--color-border);
  border-radius: var(--radius-full);
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(90deg, var(--color-primary) 0%, var(--color-accent) 100%);
  border-radius: var(--radius-full);
}

.progress-text {
  font-size: var(--text-sm);
  font-weight: var(--font-semibold);
  color: var(--color-primary);
  min-width: 40px;
  text-align: right;
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }
}
</style>
