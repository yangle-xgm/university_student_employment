<template>
  <div class="app-layout">
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
          <span v-show="!isCollapsed" class="logo-text">就业服务平台</span>
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
          <el-icon :size="20" class="nav-icon">
            <component :is="item.icon" />
          </el-icon>
          <span v-show="!isCollapsed" class="nav-text">{{ item.label }}</span>
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
          <h1 class="page-title">{{ title }}</h1>
        </div>
        <div class="header-right">
          <div class="user-info">
            <el-avatar :size="36" class="user-avatar">
              <el-icon><UserFilled /></el-icon>
            </el-avatar>
            <span class="user-name">{{ userInfo?.username || '未登录' }}</span>
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
}

.sidebar {
  position: fixed;
  left: 0;
  top: 0;
  bottom: 0;
  width: var(--sidebar-width);
  background-color: var(--color-card-bg);
  border-right: 1px solid var(--color-border);
  display: flex;
  flex-direction: column;
  z-index: 100;
  transition: width var(--t-normal) var(--ease-default);
}

.sidebar.collapsed {
  width: var(--sidebar-collapsed-width);
}

.sidebar-overlay {
  position: fixed;
  inset: 0;
  background-color: rgba(15, 23, 42, 0.45);
  z-index: 99;
}

.sidebar-header {
  padding: var(--space-4);
  border-bottom: 1px solid var(--color-border-light);
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
  width: 40px;
  height: 40px;
  border-radius: var(--radius);
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  color: var(--color-text-inverse);
  flex-shrink: 0;
}

.logo-text {
  font-size: var(--text-lg);
  font-weight: var(--font-bold);
  color: var(--color-text-1);
  white-space: nowrap;
  overflow: hidden;
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
  padding: var(--space-3) var(--space-4);
  margin-bottom: var(--space-1);
  border-radius: var(--radius);
  color: var(--color-text-2);
  text-decoration: none;
  transition: background-color var(--t-fast) var(--ease-default),
              color var(--t-fast) var(--ease-default);
}

.nav-item:hover {
  background-color: var(--color-primary-light);
  color: var(--color-primary);
}

.nav-item.active {
  background-color: var(--color-primary-light);
  color: var(--color-primary);
  font-weight: var(--font-medium);
}

.nav-icon {
  flex-shrink: 0;
}

.nav-text {
  font-size: var(--text-base);
  white-space: nowrap;
  overflow: hidden;
}

.sidebar-footer {
  padding: var(--space-3);
  border-top: 1px solid var(--color-border-light);
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
  transition: background-color var(--t-fast) var(--ease-default);
}

.logout-btn:hover {
  background-color: var(--color-danger-light);
  color: var(--color-danger);
}

.main-content {
  flex: 1;
  margin-left: var(--sidebar-width);
  min-height: 100vh;
  display: flex;
  flex-direction: column;
  transition: margin-left var(--t-normal) var(--ease-default);
}

.sidebar.collapsed ~ .main-content {
  margin-left: var(--sidebar-collapsed-width);
}

.top-header {
  position: sticky;
  top: 0;
  z-index: 50;
  display: flex;
  align-items: center;
  justify-content: space-between;
  height: var(--header-height);
  padding: 0 var(--space-6);
  background-color: var(--color-card-bg);
  border-bottom: 1px solid var(--color-border);
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
  width: 36px;
  height: 36px;
  border: 1px solid var(--color-border);
  border-radius: var(--radius);
  background-color: var(--color-card-bg);
  color: var(--color-text-2);
  cursor: pointer;
  transition: background-color var(--t-fast) var(--ease-default),
              border-color var(--t-fast) var(--ease-default);
}

.menu-toggle:hover {
  background-color: var(--color-primary-light);
  border-color: var(--color-primary-light);
  color: var(--color-primary);
}

.page-title {
  font-size: var(--text-xl);
  font-weight: var(--font-semibold);
  color: var(--color-text-1);
}

.header-right {
  display: flex;
  align-items: center;
  gap: var(--space-4);
}

.user-info {
  display: flex;
  align-items: center;
  gap: var(--space-3);
}

.user-avatar {
  background-color: var(--color-primary-light);
  color: var(--color-primary);
}

.user-name {
  font-weight: var(--font-medium);
  color: var(--color-text-1);
}

.content-body {
  flex: 1;
  padding: var(--space-6);
  max-width: var(--content-max-width);
  width: 100%;
}

/* 移动端适配 */
@media (max-width: 768px) {
  .sidebar {
    transform: translateX(-100%);
    width: var(--sidebar-width);
  }

  .sidebar.mobile-open {
    transform: translateX(0);
  }

  .main-content {
    margin-left: 0;
  }

  .sidebar.collapsed ~ .main-content {
    margin-left: 0;
  }

  .page-title {
    font-size: var(--text-lg);
  }

  .user-name {
    display: none;
  }

  .content-body {
    padding: var(--space-4);
  }
}
</style>
