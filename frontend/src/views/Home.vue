<template>
  <div class="home-container">
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <el-icon :size="28" class="logo-icon"><School /></el-icon>
          <span class="logo-text">大学生就业服务平台</span>
        </div>
        <nav class="nav">
          <router-link to="/" class="nav-link">首页</router-link>
          <router-link to="/assessments" class="nav-link">职业测评</router-link>
          <router-link to="/career" class="nav-link">职业规划</router-link>
          <router-link to="/learning" class="nav-link">学习成长</router-link>
          <router-link to="/interviews" class="nav-link">面试管理</router-link>
        </nav>
        <div class="auth-buttons">
          <router-link to="/login" custom v-slot="{ navigate }">
            <el-button class="btn-header-outline" @click="navigate">登录</el-button>
          </router-link>
          <router-link to="/register" custom v-slot="{ navigate }">
            <el-button class="btn-header-fill" type="primary" @click="navigate">注册</el-button>
          </router-link>
        </div>
      </div>
    </header>

    <main class="main-content">
      <section class="hero">
        <div class="hero-content">
          <h1 class="hero-title">开启你的职业之旅</h1>
          <p class="hero-subtitle">专业的职业测评、个性化的职业规划、丰富的学习资源，助你找到理想工作</p>
          <div class="hero-buttons">
            <router-link to="/assessments" custom v-slot="{ navigate }">
              <el-button type="primary" size="large" @click="navigate">开始测评</el-button>
            </router-link>
            <router-link to="/career" custom v-slot="{ navigate }">
              <el-button size="large" @click="navigate">职业规划</el-button>
            </router-link>
          </div>
        </div>
        <div class="hero-image">
          <div class="hero-illustration">
            <el-icon :size="128"><Promotion /></el-icon>
          </div>
        </div>
      </section>

      <section class="features">
        <h2 class="section-title">平台特色</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon :size="40"><DataLine /></el-icon>
            </div>
            <h3>职业测评</h3>
            <p>科学的测评体系，帮助你了解自己的职业兴趣和能力倾向</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon :size="40"><Aim /></el-icon>
            </div>
            <h3>职业规划</h3>
            <p>个性化的职业规划方案，制定职业目标和发展路径</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon :size="40"><Reading /></el-icon>
            </div>
            <h3>学习资源</h3>
            <p>丰富的学习资料和课程，助力技能提升</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">
              <el-icon :size="40"><Briefcase /></el-icon>
            </div>
            <h3>面试管理</h3>
            <p>面试记录和提醒，助你顺利通过面试</p>
          </div>
        </div>
      </section>

      <section class="stats">
        <div class="stats-container">
          <div class="stat-item">
            <div class="stat-value">50,000+</div>
            <div class="stat-label">注册用户</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">100+</div>
            <div class="stat-label">职业测评</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">500+</div>
            <div class="stat-label">学习资源</div>
          </div>
          <div class="stat-item">
            <div class="stat-value">98%</div>
            <div class="stat-label">用户满意度</div>
          </div>
        </div>
      </section>
    </main>

    <footer class="footer">
      <div class="footer-content">
        <p>&copy; 2024 大学生就业服务平台. 保留所有权利.</p>
      </div>
    </footer>
  </div>
</template>

<script setup>
</script>

<style scoped>
.home-container {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.header {
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
  padding: var(--space-4) 0;
  box-shadow: var(--elevation-2);
}

.header-content {
  max-width: var(--content-max-width);
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 var(--space-6);
}

.logo {
  display: flex;
  align-items: center;
  gap: var(--space-2);
}

.logo-icon {
  color: var(--color-text-inverse);
}

.logo-text {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-text-inverse);
}

.nav {
  display: flex;
  gap: var(--space-6);
}

.nav-link {
  color: var(--color-text-inverse);
  text-decoration: none;
  font-weight: var(--font-medium);
  transition: opacity var(--t-fast) var(--ease-default);
}

.nav-link:hover {
  opacity: 0.8;
}

.auth-buttons {
  display: flex;
  gap: var(--space-4);
}

.btn-header-outline {
  background-color: transparent;
  color: var(--color-text-inverse);
  border: 2px solid var(--color-text-inverse);
}

.btn-header-outline:hover {
  background-color: color-mix(in srgb, var(--color-text-inverse) 10%, transparent);
  color: var(--color-text-inverse);
}

.btn-header-fill {
  background-color: var(--color-text-inverse);
  color: var(--color-primary);
  border-color: var(--color-text-inverse);
}

.btn-header-fill:hover {
  background-color: var(--color-border-light);
  color: var(--color-primary-hover);
  border-color: var(--color-border-light);
}

.main-content {
  flex: 1;
}

.hero {
  display: flex;
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: var(--space-12) var(--space-6);
  align-items: center;
  gap: var(--space-12);
}

.hero-content {
  flex: 1;
}

.hero-title {
  font-size: var(--text-3xl);
  color: var(--color-text-1);
  margin-bottom: var(--space-4);
}

.hero-subtitle {
  font-size: var(--text-xl);
  color: var(--color-text-2);
  margin-bottom: var(--space-6);
  line-height: 1.6;
}

.hero-buttons {
  display: flex;
  gap: var(--space-4);
}

.hero-image {
  flex: 1;
  display: flex;
  justify-content: center;
}

.hero-illustration {
  color: var(--color-primary);
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.features {
  padding: var(--space-12) var(--space-6);
  background-color: var(--color-bg);
}

.section-title {
  text-align: center;
  font-size: var(--text-2xl);
  color: var(--color-text-1);
  margin-bottom: var(--space-8);
}

.features-grid {
  max-width: var(--content-max-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-6);
}

.feature-card {
  background-color: var(--color-card-bg);
  padding: var(--space-6);
  border-radius: var(--radius-md);
  box-shadow: var(--elevation-1);
  text-align: center;
  transition: transform var(--t-normal) var(--ease-default);
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  color: var(--color-primary);
  margin-bottom: var(--space-4);
}

.feature-card h3 {
  color: var(--color-text-1);
  margin-bottom: var(--space-2);
}

.feature-card p {
  color: var(--color-text-2);
  font-size: var(--text-sm);
  line-height: 1.5;
}

.stats {
  padding: var(--space-12) var(--space-6);
  background: linear-gradient(135deg, var(--color-primary) 0%, var(--color-accent) 100%);
}

.stats-container {
  max-width: var(--content-max-width);
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: var(--space-6);
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: var(--text-3xl);
  font-weight: var(--font-bold);
  color: var(--color-text-inverse);
  margin-bottom: var(--space-2);
}

.stat-label {
  font-size: var(--text-md);
  color: var(--color-text-inverse);
}

.footer {
  background-color: var(--color-text-1);
  color: var(--color-text-inverse);
  padding: var(--space-6) 0;
  text-align: center;
}

.footer-content {
  max-width: var(--content-max-width);
  margin: 0 auto;
  padding: 0 var(--space-6);
}

@media (max-width: 1024px) {
  .hero {
    flex-direction: column;
    text-align: center;
    gap: var(--space-8);
  }

  .hero-buttons {
    justify-content: center;
  }

  .features-grid {
    grid-template-columns: repeat(2, 1fr);
  }

  .stats-container {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 768px) {
  .header-content {
    flex-direction: column;
    gap: var(--space-4);
  }

  .nav {
    flex-wrap: wrap;
    justify-content: center;
    gap: var(--space-4);
  }

  .auth-buttons {
    width: 100%;
    justify-content: center;
  }

  .hero {
    padding: var(--space-8) var(--space-4);
  }

  .hero-title {
    font-size: var(--text-2xl);
  }

  .hero-subtitle {
    font-size: var(--text-lg);
  }

  .features,
  .stats {
    padding: var(--space-8) var(--space-4);
  }

  .features-grid,
  .stats-container {
    grid-template-columns: 1fr;
  }
}
</style>
