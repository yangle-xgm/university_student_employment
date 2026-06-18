<template>
  <div class="home-container">
    <header class="header">
      <div class="header-content">
        <div class="logo">
          <span class="logo-icon">🎓</span>
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
          <router-link to="/login" class="btn btn-outline">登录</router-link>
          <router-link to="/register" class="btn btn-primary">注册</router-link>
        </div>
      </div>
    </header>

    <main class="main-content">
      <section class="hero">
        <div class="hero-content">
          <h1 class="hero-title">开启你的职业之旅</h1>
          <p class="hero-subtitle">专业的职业测评、个性化的职业规划、丰富的学习资源，助你找到理想工作</p>
          <div class="hero-buttons">
            <router-link to="/assessments" class="btn btn-primary btn-large">开始测评</router-link>
            <router-link to="/career" class="btn btn-outline btn-large">职业规划</router-link>
          </div>
        </div>
        <div class="hero-image">
          <div class="hero-illustration">
            <span class="icon">🚀</span>
          </div>
        </div>
      </section>

      <section class="features">
        <h2 class="section-title">平台特色</h2>
        <div class="features-grid">
          <div class="feature-card">
            <div class="feature-icon">📊</div>
            <h3>职业测评</h3>
            <p>科学的测评体系，帮助你了解自己的职业兴趣和能力倾向</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🎯</div>
            <h3>职业规划</h3>
            <p>个性化的职业规划方案，制定职业目标和发展路径</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📚</div>
            <h3>学习资源</h3>
            <p>丰富的学习资料和课程，助力技能提升</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💼</div>
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
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
  padding: 1rem 0;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 2rem;
}

.logo {
  display: flex;
  align-items: center;
  gap: 0.5rem;
}

.logo-icon {
  font-size: 1.8rem;
}

.logo-text {
  font-size: 1.4rem;
  font-weight: bold;
  color: white;
}

.nav {
  display: flex;
  gap: 2rem;
}

.nav-link {
  color: white;
  text-decoration: none;
  font-weight: 500;
  transition: opacity 0.3s;
}

.nav-link:hover {
  opacity: 0.8;
}

.auth-buttons {
  display: flex;
  gap: 1rem;
}

.btn {
  padding: 0.5rem 1.5rem;
  border-radius: 8px;
  text-decoration: none;
  font-weight: 500;
  transition: all 0.3s;
  border: none;
  cursor: pointer;
}

.btn-primary {
  background-color: white;
  color: #4a69bd;
}

.btn-primary:hover {
  background-color: #f0f0f0;
}

.btn-outline {
  background-color: transparent;
  color: white;
  border: 2px solid white;
}

.btn-outline:hover {
  background-color: rgba(255, 255, 255, 0.1);
}

.btn-large {
  padding: 0.8rem 2rem;
  font-size: 1.1rem;
}

.main-content {
  flex: 1;
}

.hero {
  display: flex;
  max-width: 1200px;
  margin: 0 auto;
  padding: 4rem 2rem;
  align-items: center;
  gap: 4rem;
}

.hero-content {
  flex: 1;
}

.hero-title {
  font-size: 2.5rem;
  color: #333;
  margin-bottom: 1rem;
}

.hero-subtitle {
  font-size: 1.2rem;
  color: #666;
  margin-bottom: 2rem;
  line-height: 1.6;
}

.hero-buttons {
  display: flex;
  gap: 1rem;
}

.hero-image {
  flex: 1;
  display: flex;
  justify-content: center;
}

.hero-illustration {
  font-size: 8rem;
  animation: float 3s ease-in-out infinite;
}

@keyframes float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}

.features {
  padding: 4rem 2rem;
  background-color: #f8f9fa;
}

.section-title {
  text-align: center;
  font-size: 2rem;
  color: #333;
  margin-bottom: 3rem;
}

.features-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
}

.feature-card {
  background-color: white;
  padding: 2rem;
  border-radius: 12px;
  box-shadow: 0 4px 6px rgba(0, 0, 0, 0.05);
  text-align: center;
  transition: transform 0.3s;
}

.feature-card:hover {
  transform: translateY(-5px);
}

.feature-icon {
  font-size: 2.5rem;
  margin-bottom: 1rem;
}

.feature-card h3 {
  color: #333;
  margin-bottom: 0.5rem;
}

.feature-card p {
  color: #666;
  font-size: 0.9rem;
  line-height: 1.5;
}

.stats {
  padding: 4rem 2rem;
  background: linear-gradient(135deg, #4a69bd 0%, #6a89cc 100%);
}

.stats-container {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 2rem;
}

.stat-item {
  text-align: center;
}

.stat-value {
  font-size: 2.5rem;
  font-weight: bold;
  color: white;
}

.stat-label {
  color: rgba(255, 255, 255, 0.8);
  margin-top: 0.5rem;
}

.footer {
  background-color: #333;
  padding: 2rem;
  text-align: center;
}

.footer-content p {
  color: #999;
}
</style>
