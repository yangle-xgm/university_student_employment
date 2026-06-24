<template>
  <div class="app-layout">
    <!-- 背景装饰 -->
    <div class="ambient-bg">
      <div class="ambient-orb orb-1"></div>
      <div class="ambient-orb orb-2"></div>
      <div class="ambient-grid"></div>
    </div>

    <!-- 移动端遮罩 -->
    <div
      v-if="isMobile && !sidebarCollapsed"
      class="sidebar-overlay"
      @click="sidebarCollapsed = true"
    />

    <!-- 侧边栏 -->
    <aside
      class="sidebar"
      :class="{
        collapsed: sidebarCollapsed && !isMobile,
        'mobile-open': !sidebarCollapsed && isMobile
      }"
      :aria-hidden="isMobile && sidebarCollapsed ? 'true' : 'false'"
    >
      <div class="sidebar-header">
        <div class="logo">
          <div class="logo-badge">
            <el-icon :size="22"><School /></el-icon>
          </div>
          <span v-show="!isCollapsed" class="logo-text">就业服务</span>
        </div>
      </div>

      <nav class="sidebar-nav" aria-label="主导航">
        <router-link
          v-for="item in navItems"
          :key="item.path"
          :to="item.path"
          class="nav-item"
          :class="{ active: route.path === item.path }"
          :aria-current="route.path === item.path ? 'page' : undefined"
        >
          <div class="nav-icon-wrapper">
            <el-icon :size="20" class="nav-icon">
              <component :is="item.icon" />
            </el-icon>
          </div>
          <span v-show="!isCollapsed" class="nav-text">{{ item.label }}</span>
          <div v-if="route.path === item.path && !isCollapsed" class="active-indicator"></div>
        </router-link>
      </nav>

      <div class="sidebar-footer">
        <button class="logout-btn" @click="handleLogout">
          <el-icon :size="18" class="logout-icon"><SwitchButton /></el-icon>
          <span v-show="!isCollapsed">退出登录</span>
        </button>
      </div>
    </aside>

    <!-- 主内容区 -->
    <main class="main-content">
      <header class="top-header">
        <div class="header-left">
          <button
            class="menu-toggle"
            aria-label="切换侧边栏"
            @click="toggleSidebar"
          >
            <el-icon :size="20"><Fold v-if="!sidebarCollapsed" /><Expand v-else /></el-icon>
          </button>
          <div class="page-title-group">
            <h1 class="page-title">{{ title }}</h1>
            <p class="page-subtitle">大学生就业服务平台</p>
          </div>
        </div>
        <div class="header-right">
          <div class="user-info">
            <div class="user-badge">
              <el-avatar :size="40" class="user-avatar">
                <el-icon><UserFilled /></el-icon>
              </el-avatar>
              <div v-if="!isCollapsed" class="user-meta">
                <span class="user-name">{{ userInfo?.username || '未登录' }}</span>
                <span class="user-role">学生用户</span>
              </div>
            </div>
          </div>
        </div>
      </header>

      <div class="content-body">
        <slot />
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'

const route = useRoute()

defineProps({
  title: { type: String, default: '' }
})

const sidebarCollapsed = ref(false)
const isMobile = ref(false)

const isCollapsed = computed(() => sidebarCollapsed.value || isMobile.value)

const navItems = [
  { path: '/dashboard', label: '仪表盘', icon: 'DataLine' },
  { path: '/profile', label: '个人资料', icon: 'User' },
  { path: '/assessments', label: '职业测评', icon: 'DocumentChecked' },
  { path: '/career', label: '职业规划', icon: 'Aim' },
  { path: '/learning', label: '学习成长', icon: 'Reading' },
  { path: '/interviews', label: '面试管理', icon: 'Calendar' },
  { path: '/jobs', label: '职位搜索', icon: 'Briefcase' },
  { path: '/resumes', label: '简历管理', icon: 'Document' }
]

const userInfo = ref({})

const toggleSidebar = () => {
  sidebarCollapsed.value = !sidebarCollapsed.value
}

const checkMobile = () => {
  isMobile.value = window.innerWidth <= 768
  if (isMobile.value) {
    sidebarCollapsed.value = true
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
  }
  checkMobile()
  window.addEventListener('resize', checkMobile)
})

onUnmounted(() => {
  window.removeEventListener('resize', checkMobile)
})
</script>

<style scoped>
.app-layout {
  display: flex;
  min-height: 100vh;
  background-color: var(--color-bg);
  position: relative;
  overflow-x: hidden;
}

/* 背景装饰 */
.ambient-bg {
  position: fixed;
  inset: 0;
  pointer-events: none;
  z-index: 0;
  overflow: hidden;
}

.ambient-orb {
  position: absolute;
  border-radius: 50%;
  filter: blur(80px);
  opacity: 0.4;
  animation: float 20s ease-in-out infinite;
}

.orb-1 {
  width: 600px;
  height: 600px;
  background: radial-gradient(circle, rgba(37, 99, 235, 0.15) 0%, transparent 70%);
  top: -200px;
  right: -200px;
}

.orb-2 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, rgba(124, 58, 237, 0.12) 0%, transparent 70%);
  bottom: -150px;
  left: 10%;
  animation-delay: -10s;
}

.ambient-grid {
  position: absolute;
  inset: 0;
  background-image:
    linear-gradient(rgba(15, 23, 42, 0.03) 1px, transparent 1px),
    linear-gradient(90deg, rgba(15, 23, 42, 0.03) 1px, transparent 1px);
  background-size: 60px 60px;
  mask-image: linear-gradient(to bottom, transparent 0%, black 20%, black 80%, transparent 100%);
}

@keyframes float {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(30px, -30px) scale(1.05); }
  66% { transform: translate(-20px, 20px) scale(0.95); }
}

/* 侧边栏 */
.sidebar {
  position: fixed;
  left: 16px;
  top: 16px;
  bottom: 16px;
  width: var(--sidebar-width);
  background: var(--glass-bg);
  backdrop-filter: var(--blur-backdrop);
  -webkit-backdrop-filter: var(--blur-backdrop);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-lg);
  box-shadow: var(--glass-shadow);
  display: flex;
  flex-direction: column;
  z-index: 100;
  transition: width var(--t-slow) var(--ease-smooth),
              left var(--t-normal) var(--ease-default);
}

.sidebar.collapsed {
  width: var(--sidebar-collapsed-width);
}

.sidebar-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.45);
  backdrop-filter: blur(4px);
  z-index: 99;
}

.sidebar-header {
  padding: var(--space-5);
  border-bottom: 1px solid var(--glass-border);
}

.logo {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  height: 48px;
}

.logo-badge {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 44px;
  height: 44px;
  border-radius: var(--radius);
  background: var(--gradient-primary);
  color: var(--color-text-inverse);
  flex-shrink: 0;
  box-shadow: var(--elevation-2);
}

.logo-text {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
  white-space: nowrap;
  overflow: hidden;
  letter-spacing: -0.02em;
}

.sidebar-nav {
  flex: 1;
  padding: var(--space-3);
  overflow-y: auto;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-3);
  margin-bottom: var(--space-1);
  border-radius: var(--radius);
  color: var(--color-text-2);
  text-decoration: none;
  position: relative;
  transition: all var(--t-fast) var(--ease-default);
}

.nav-item:hover {
  background-color: rgba(37, 99, 235, 0.06);
  color: var(--color-primary);
}

.nav-item.active {
  background: var(--gradient-primary-soft);
  color: var(--color-primary);
  font-weight: var(--font-medium);
}

.nav-icon-wrapper {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: var(--radius-sm);
  transition: all var(--t-fast) var(--ease-default);
}

.nav-item:hover .nav-icon-wrapper,
.nav-item.active .nav-icon-wrapper {
  background-color: rgba(37, 99, 235, 0.1);
}

.nav-icon {
  flex-shrink: 0;
}

.nav-text {
  font-size: var(--text-base);
  white-space: nowrap;
  overflow: hidden;
  flex: 1;
}

.active-indicator {
  width: 4px;
  height: 24px;
  border-radius: var(--radius-full);
  background: var(--gradient-primary);
  flex-shrink: 0;
}

.sidebar-footer {
  padding: var(--space-3);
  border-top: 1px solid var(--glass-border);
}

.logout-btn {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  width: 100%;
  padding: var(--space-3) var(--space-4);
  background-color: transparent;
  border: none;
  border-radius: var(--radius);
  color: var(--color-text-2);
  font-size: var(--text-base);
  cursor: pointer;
  transition: all var(--t-fast) var(--ease-default);
}

.logout-btn:hover {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.main-content {
  flex: 1;
  margin-left: calc(var(--sidebar-width) + 32px);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
  z-index: 1;
  transition: margin-left var(--t-slow) var(--ease-smooth);
}

.sidebar.collapsed ~ .main-content {
  margin-left: calc(var(--sidebar-collapsed-width) + 32px);
}

.top-header {
  position: sticky;
  top: 16px;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--header-height);
  margin: 16px 16px 0 0;
  padding: 0 var(--space-6);
  background: var(--glass-bg);
  backdrop-filter: var(--blur-backdrop);
  -webkit-backdrop-filter: var(--blur-backdrop);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-lg);
  box-shadow: var(--glass-shadow);
}

.header-left {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.menu-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background-color: var(--color-card-bg);
  color: var(--color-text-2);
  cursor: pointer;
  transition: all var(--t-fast) var(--ease-default);
}

.menu-toggle:hover {
  background-color: var(--color-primary-light);
  border-color: var(--color-primary-light);
  color: var(--color-primary);
}

.page-title-group {
  display: flex;
  flex-direction: column;
}

.page-title {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
  letter-spacing: -0.02em;
  line-height: 1.2;
}

.page-subtitle {
  font-size: var(--text-xs);
  color: var(--color-text-3);
  font-weight: var(--font-medium);
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.user-badge {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  padding: var(--space-1) var(--space-2) var(--space-1) var(--space-1);
  background-color: var(--color-card-bg);
  border: 1px solid var(--color-border);
  border-radius: var(--radius-full);
}

.user-avatar {
  background: var(--gradient-primary);
  color: var(--color-text-inverse);
}

.user-meta {
  display: flex;
  flex-direction: column;
  padding-right: var(--space-2);
}

.user-name {
  font-weight: var(--font-semibold);
  color: var(--color-text-1);
  font-size: var(--text-sm);
  line-height: 1.2;
}

.user-role {
  font-size: var(--text-xs);
  color: var(--color-text-3);
  line-height: 1.2;
}

.content-body {
  flex: 1;
  padding: var(--space-6) 16px var(--space-6) 0;
  max-width: var(--content-max-width);
  width: 100%;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(calc(-100% - 32px));
    width: var(--sidebar-width);
    left: 0;
    top: 0;
    bottom: 0;
    border-radius: 0 var(--radius-lg) var(--radius-lg) 0;
  }

  .sidebar.mobile-open {
    transform: translateX(0);
  }

  .main-content {
    margin-left: 0;
    margin-right: 0;
  }

  .sidebar.collapsed ~ .main-content {
    margin-left: 0;
  }

  .top-header {
    margin: 0;
    border-radius: 0;
    border-left: none;
    border-right: none;
    border-top: none;
  }

  .content-body {
    padding: var(--space-4);
  }

  .page-subtitle {
    display: none;
  }
}
</style>
