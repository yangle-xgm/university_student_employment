<template>
  <AppLayout title="仪表盘">
    <div class="dashboard-page">
      <!-- 欢迎横幅 -->
      <div class="welcome-banner">
        <div class="welcome-content">
          <div class="welcome-greeting">👋 欢迎回来，{{ userInfo?.username || '同学' }}</div>
          <h1 class="welcome-title">今天是你职业成长的第 <span class="highlight">{{ streakDays }}</span> 天</h1>
          <p class="welcome-subtitle">保持学习节奏，你的目标正在逐步实现</p>
        </div>
        <div class="welcome-decoration">
          <div class="deco-circle"></div>
          <div class="deco-ring"></div>
        </div>
      </div>

      <!-- KPI 卡片区 -->
      <div class="stats-grid">
        <KpiCard
          icon="DocumentChecked"
          :value="assessmentCount"
          label="已完成测评"
          hint="查看测评记录"
          trend="+12%"
          trend-type="up"
          icon-bg="rgba(37, 99, 235, 0.1)"
          icon-color="var(--color-primary)"
          gradient="linear-gradient(135deg, rgba(37, 99, 235, 0.14) 0%, rgba(124, 58, 237, 0.08) 100%)"
          clickable
        />
        <KpiCard
          icon="Aim"
          :value="planCount"
          label="职业规划"
          hint="管理职业路径"
          trend="+1"
          trend-type="up"
          icon-bg="rgba(124, 58, 237, 0.1)"
          icon-color="var(--color-accent)"
          gradient="linear-gradient(135deg, rgba(124, 58, 237, 0.14) 0%, rgba(37, 99, 235, 0.06) 100%)"
          clickable
        />
        <KpiCard
          icon="Reading"
          :value="learningProgress + '%'"
          label="学习进度"
          hint="继续学习"
          trend="+5%"
          trend-type="up"
          icon-bg="rgba(16, 185, 129, 0.1)"
          icon-color="var(--color-success)"
          gradient="linear-gradient(135deg, rgba(16, 185, 129, 0.14) 0%, rgba(37, 99, 235, 0.06) 100%)"
          clickable
        />
        <KpiCard
          icon="Calendar"
          :value="interviewCount"
          label="面试安排"
          hint="查看日程"
          trend="本周"
          trend-type="up"
          icon-bg="rgba(245, 158, 11, 0.1)"
          icon-color="var(--color-warning)"
          gradient="linear-gradient(135deg, rgba(245, 158, 11, 0.14) 0%, rgba(239, 68, 68, 0.06) 100%)"
          clickable
        />
      </div>

      <div class="dashboard-grid">
        <!-- 最近活动 -->
        <BaseCard class="content-card">
          <SectionHeader title="最近活动" subtitle="你的近期成长足迹" />
          <div class="activity-timeline">
            <div v-for="(activity, index) in activities" :key="index" class="activity-item">
              <div class="activity-line">
                <div class="activity-dot" :style="{ backgroundColor: activity.iconBg, color: activity.iconColor }">
                  <el-icon :size="14">
                    <component :is="activity.icon" />
                  </el-icon>
                </div>
                <div v-if="index < activities.length - 1" class="activity-connector"></div>
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
              <div class="resource-icon" :style="{ background: resource.iconBg, color: resource.iconColor }">
                <el-icon :size="20">
                  <component :is="resource.icon" />
                </el-icon>
              </div>
              <div class="resource-content">
                <div class="resource-title">{{ resource.title }}</div>
                <div class="resource-category">{{ resource.category }}</div>
              </div>
              <div class="resource-progress">
                <div class="progress-ring">
                  <svg viewBox="0 0 36 36" class="circular-chart">
                    <path class="circle-bg" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
                    <path class="circle" :stroke-dasharray="resource.progress + ', 100'" d="M18 2.0845 a 15.9155 15.9155 0 0 1 0 31.831 a 15.9155 15.9155 0 0 1 0 -31.831" />
                  </svg>
                  <span class="progress-value">{{ resource.progress }}%</span>
                </div>
              </div>
            </div>
          </div>
        </BaseCard>
      </div>
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
const streakDays = ref(23)

const activities = [
  { title: '完成职业兴趣测评', time: '2小时前', icon: 'CircleCheck', iconBg: 'rgba(16, 185, 129, 0.12)', iconColor: 'var(--color-success)' },
  { title: '创建职业规划', time: '昨天', icon: 'EditPen', iconBg: 'rgba(124, 58, 237, 0.12)', iconColor: 'var(--color-accent)' },
  { title: '学习Java基础课程', time: '2天前', icon: 'Reading', iconBg: 'rgba(37, 99, 235, 0.12)', iconColor: 'var(--color-primary)' },
  { title: '预约面试', time: '3天前', icon: 'Calendar', iconBg: 'rgba(245, 158, 11, 0.12)', iconColor: 'var(--color-warning)' }
]

const resources = [
  { title: 'Java编程入门', category: '编程基础', progress: 65, icon: 'Notebook', iconBg: 'linear-gradient(135deg, rgba(37, 99, 235, 0.12) 0%, rgba(124, 58, 237, 0.08) 100%)', iconColor: 'var(--color-primary)' },
  { title: '前端开发实战', category: 'Web开发', progress: 40, icon: 'Monitor', iconBg: 'linear-gradient(135deg, rgba(124, 58, 237, 0.12) 0%, rgba(37, 99, 235, 0.08) 100%)', iconColor: 'var(--color-accent)' },
  { title: '数据结构与算法', category: '计算机基础', progress: 20, icon: 'DataAnalysis', iconBg: 'linear-gradient(135deg, rgba(16, 185, 129, 0.12) 0%, rgba(37, 99, 235, 0.08) 100%)', iconColor: 'var(--color-success)' }
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

.welcome-banner {
  position: relative;
  background: var(--gradient-primary);
  border-radius: var(--radius-lg);
  padding: var(--space-8) var(--space-6);
  color: var(--color-text-inverse);
  overflow: hidden;
  box-shadow: var(--elevation-3);
}

.welcome-content {
  position: relative;
  z-index: 1;
  max-width: 600px;
}

.welcome-greeting {
  font-size: var(--text-sm);
  opacity: 0.9;
  margin-bottom: var(--space-2);
  font-weight: var(--font-medium);
}

.welcome-title {
  font-size: var(--text-2xl);
  font-weight: var(--font-extrabold);
  letter-spacing: -0.03em;
  line-height: 1.2;
  margin-bottom: var(--space-2);
}

.welcome-title .highlight {
  color: rgba(255, 255, 255, 0.7);
  text-decoration: underline;
  text-decoration-thickness: 4px;
  text-underline-offset: 4px;
}

.welcome-subtitle {
  font-size: var(--text-base);
  opacity: 0.85;
}

.welcome-decoration {
  position: absolute;
  right: var(--space-8);
  top: 50%;
  transform: translateY(-50%);
  z-index: 0;
}

.deco-circle {
  width: 140px;
  height: 140px;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.deco-ring {
  position: absolute;
  top: -20px;
  left: -20px;
  right: -20px;
  bottom: -20px;
  border: 2px solid rgba(255, 255, 255, 0.15);
  border-radius: 50%;
  animation: pulse-ring 3s ease-in-out infinite;
}

@keyframes pulse-ring {
  0%, 100% { transform: scale(1); opacity: 0.6; }
  50% { transform: scale(1.1); opacity: 0.3; }
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-5);
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: var(--space-6);
}

.content-card {
  padding: var(--space-5);
}

.activity-timeline {
  display: flex;
  flex-direction: column;
  gap: 0;
}

.activity-item {
  display: flex;
  gap: var(--space-4);
  padding: var(--space-3) 0;
}

.activity-line {
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 28px;
  flex-shrink: 0;
}

.activity-dot {
  width: 28px;
  height: 28px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.activity-connector {
  width: 2px;
  flex: 1;
  min-height: 30px;
  background: linear-gradient(to bottom, var(--color-border) 0%, transparent 100%);
  margin: var(--space-2) 0;
}

.activity-content {
  flex: 1;
  padding-bottom: var(--space-4);
}

.activity-title {
  font-weight: var(--font-semibold);
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
  background: var(--color-bg);
  border-radius: var(--radius);
  border: 1px solid var(--color-border-light);
  transition: all var(--t-fast) var(--ease-default);
}

.resource-card:hover {
  border-color: rgba(37, 99, 235, 0.15);
  background: var(--color-card-bg);
  box-shadow: var(--elevation-2);
}

.resource-icon {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 48px;
  height: 48px;
  border-radius: var(--radius);
  flex-shrink: 0;
}

.resource-content {
  flex: 1;
}

.resource-title {
  font-weight: var(--font-semibold);
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
  justify-content: center;
}

.progress-ring {
  position: relative;
  width: 48px;
  height: 48px;
}

.circular-chart {
  display: block;
  width: 100%;
  height: 100%;
}

.circle-bg {
  fill: none;
  stroke: var(--color-border);
  stroke-width: 3;
}

.circle {
  fill: none;
  stroke: url(#dashboardGradient);
  stroke-width: 3;
  stroke-linecap: round;
  transform: rotate(-90deg);
  transform-origin: 50% 50%;
  transition: stroke-dasharray var(--t-slow) var(--ease-smooth);
}

.progress-value {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  font-size: var(--text-xs);
  font-weight: var(--font-bold);
  color: var(--color-primary);
}

@media (max-width: 1024px) {
  .stats-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .dashboard-grid {
    grid-template-columns: 1fr;
  }

  .welcome-decoration {
    display: none;
  }
}

@media (max-width: 640px) {
  .stats-grid {
    grid-template-columns: 1fr;
  }

  .welcome-banner {
    padding: var(--space-6);
  }

  .welcome-title {
    font-size: var(--text-xl);
  }
}
</style>
