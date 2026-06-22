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
        <router-link to="/jobs" class="nav-item active">
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
        <h1>职位搜索</h1>
        <div class="user-info">
          <span class="user-name">{{ userInfo?.username }}</span>
        </div>
      </header>

      <div class="search-filters">
        <div class="filter-row">
          <el-input 
            v-model="searchForm.keyword" 
            placeholder="搜索职位关键词..." 
            class="filter-input"
          >
            <template #append>
              <el-button @click="handleSearch">搜索</el-button>
            </template>
          </el-input>
        </div>
        <div class="filter-row">
          <div class="filter-group">
            <label>工作地点</label>
            <el-select v-model="searchForm.location" placeholder="选择地点" class="filter-select">
              <el-option label="全部" value="" />
              <el-option label="北京" value="北京" />
              <el-option label="上海" value="上海" />
              <el-option label="广州" value="广州" />
              <el-option label="深圳" value="深圳" />
              <el-option label="杭州" value="杭州" />
              <el-option label="成都" value="成都" />
            </el-select>
          </div>
          <div class="filter-group">
            <label>行业</label>
            <el-select v-model="searchForm.industry" placeholder="选择行业" class="filter-select">
              <el-option label="全部" value="" />
              <el-option label="互联网" value="互联网" />
              <el-option label="金融" value="金融" />
              <el-option label="教育" value="教育" />
              <el-option label="医疗" value="医疗" />
              <el-option label="制造" value="制造" />
              <el-option label="零售" value="零售" />
            </el-select>
          </div>
          <div class="filter-group">
            <label>职位类型</label>
            <el-select v-model="searchForm.jobType" placeholder="选择类型" class="filter-select">
              <el-option label="全部" value="" />
              <el-option label="全职" value="全职" />
              <el-option label="兼职" value="兼职" />
              <el-option label="实习" value="实习" />
            </el-select>
          </div>
        </div>
      </div>

      <div class="jobs-stats">
        <div class="stat-item">
          <span class="stat-value">{{ jobs.length }}</span>
          <span class="stat-label">搜索结果</span>
        </div>
      </div>

      <div class="jobs-list">
        <div v-for="job in jobs" :key="job.id" class="job-card">
          <div class="job-header">
            <div class="job-title-section">
              <h3>{{ job.title }}</h3>
              <span class="job-type" :class="(job.jobType || 'unknown').toLowerCase()">{{ getJobTypeLabel(job.jobType) }}</span>
            </div>
            <div class="job-salary">{{ job.salary }}</div>
          </div>
          <div class="job-info">
            <span class="company-name">{{ job.companyName }}</span>
            <span class="job-location">📍 {{ job.location }}</span>
            <span class="job-industry">🏢 {{ job.industry }}</span>
          </div>
          <p class="job-description">{{ job.description }}</p>
          <div class="job-tags">
            <span v-for="tag in job.tags" :key="tag" class="tag">{{ tag }}</span>
          </div>
          <div class="job-footer">
            <span class="post-date">发布时间: {{ job.postDate }}</span>
            <button class="apply-btn" @click="handleApply(job)">立即申请</button>
          </div>
        </div>
      </div>

      <div class="empty-state" v-if="jobs.length === 0">
        <span class="empty-icon">🔍</span>
        <p>暂无匹配的职位</p>
        <button class="reset-btn" @click="resetFilters">重置筛选条件</button>
      </div>

      <el-dialog title="职位详情" v-model="showJobDetailModal" width="600px">
        <div v-if="selectedJob" class="job-detail">
          <div class="detail-header">
            <div class="detail-title-section">
              <h2>{{ selectedJob.title }}</h2>
              <div class="detail-meta">
                <span class="detail-company">{{ selectedJob.companyName }}</span>
                <span class="detail-salary">{{ selectedJob.salary }}</span>
              </div>
            </div>
          </div>
          <div class="detail-content">
            <div class="detail-section">
              <h3>职位描述</h3>
              <p>{{ selectedJob.description }}</p>
            </div>
            <div class="detail-section">
              <h3>岗位要求</h3>
              <ul>
                <li v-for="(requirement, index) in selectedJob.requirements" :key="index">{{ requirement }}</li>
              </ul>
            </div>
            <div class="detail-section">
              <h3>公司信息</h3>
              <div class="company-info">
                <div class="info-row">
                  <span class="info-label">公司名称</span>
                  <span class="info-value">{{ selectedJob.companyName }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">所在城市</span>
                  <span class="info-value">{{ selectedJob.location }}</span>
                </div>
                <div class="info-row">
                  <span class="info-label">所属行业</span>
                  <span class="info-value">{{ selectedJob.industry }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
        <template #footer>
          <el-button @click="showJobDetailModal = false">关闭</el-button>
          <el-button type="primary" @click="handleApply(selectedJob)">申请职位</el-button>
        </template>
      </el-dialog>
    </main>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { getAllJobs, searchJobs } from '@/api/job'

const userInfo = ref({})
const jobs = ref([])
const selectedJob = ref(null)
const showJobDetailModal = ref(false)

const searchForm = reactive({
  keyword: '',
  location: '',
  industry: '',
  jobType: ''
})

const getJobTypeLabel = (type) => {
  const labels = {
    'FULL_TIME': '全职',
    'PART_TIME': '兼职',
    'INTERNSHIP': '实习'
  }
  return labels[type] || type
}

const handleSearch = async () => {
  try {
    const data = await searchJobs(searchForm.keyword, searchForm.location, searchForm.industry)
    jobs.value = data
  } catch (error) {
    console.error('搜索职位失败:', error)
    jobs.value = []
  }
}

const handleApply = (job) => {
  selectedJob.value = job
  showJobDetailModal.value = true
}

const resetFilters = () => {
  searchForm.keyword = ''
  searchForm.location = ''
  searchForm.industry = ''
  searchForm.jobType = ''
  loadJobs()
}

const loadJobs = async () => {
  try {
    const data = await getAllJobs()
    jobs.value = data
  } catch (error) {
    console.error('加载职位列表失败:', error)
    jobs.value = [
      { id: 1, title: 'Java开发工程师', companyName: '字节跳动', location: '北京', industry: '互联网', salary: '15-25K', jobType: 'FULL_TIME', description: '负责公司核心业务系统的开发与维护，要求扎实的Java基础和Spring Boot框架经验。', requirements: ['本科及以上学历', '3年以上Java开发经验', '熟悉Spring Boot/Spring Cloud', '良好的沟通能力'], tags: ['Java', 'Spring Boot', 'MySQL'], postDate: '2024-01-15' },
      { id: 2, title: '前端开发工程师', companyName: '阿里巴巴', location: '杭州', industry: '互联网', salary: '12-20K', jobType: 'FULL_TIME', description: '负责电商平台前端页面开发，使用Vue/React等主流框架。', requirements: ['本科及以上学历', '2年以上前端开发经验', '精通Vue/React', '熟悉Webpack/Vite'], tags: ['Vue', 'React', 'TypeScript'], postDate: '2024-01-14' },
      { id: 3, title: '产品经理', companyName: '腾讯', location: '深圳', industry: '互联网', salary: '18-30K', jobType: 'FULL_TIME', description: '负责产品规划和需求分析，推动产品迭代优化。', requirements: ['本科及以上学历', '2年以上产品经理经验', '有互联网产品经验优先', '良好的数据分析能力'], tags: ['产品', '需求分析', '项目管理'], postDate: '2024-01-13' },
      { id: 4, title: '数据分析实习生', companyName: '美团', location: '北京', industry: '互联网', salary: '10-15K', jobType: 'INTERNSHIP', description: '协助团队进行数据收集、清洗和分析工作。', requirements: ['本科及以上学历', '统计学/数学相关专业', '熟练使用SQL', '会Python优先'], tags: ['数据分析', 'SQL', 'Excel'], postDate: '2024-01-12' },
      { id: 5, title: '测试工程师', companyName: '京东', location: '北京', industry: '互联网', salary: '10-18K', jobType: 'FULL_TIME', description: '负责软件产品的测试工作，确保产品质量。', requirements: ['本科及以上学历', '2年以上测试经验', '熟悉测试流程', '会自动化测试优先'], tags: ['测试', '自动化', 'Linux'], postDate: '2024-01-11' },
      { id: 6, title: 'UI设计师', companyName: '网易', location: '杭州', industry: '互联网', salary: '12-22K', jobType: 'FULL_TIME', description: '负责产品界面设计和用户体验优化。', requirements: ['本科及以上学历', '3年以上UI设计经验', '精通Figma/PS', '有设计系统经验优先'], tags: ['UI设计', 'Figma', '用户体验'], postDate: '2024-01-10' }
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
  }
  loadJobs()
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

.search-filters {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  margin-bottom: 2rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
}

.filter-row {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.filter-row:last-child {
  margin-bottom: 0;
}

.filter-input {
  flex: 1;
  max-width: 400px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: 0.5rem;
}

.filter-group label {
  font-weight: 500;
  color: #333;
  font-size: 0.9rem;
}

.filter-select {
  width: 150px;
}

.jobs-stats {
  margin-bottom: 1.5rem;
}

.stat-item {
  display: flex;
  align-items: baseline;
  gap: 0.5rem;
}

.stat-value {
  font-size: 1.5rem;
  font-weight: bold;
  color: #4a69bd;
}

.stat-label {
  color: #666;
}

.jobs-list {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.job-card {
  background-color: white;
  border-radius: 12px;
  padding: 1.5rem;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.05);
  cursor: pointer;
  transition: transform 0.3s, box-shadow 0.3s;
}

.job-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 1rem;
}

.job-title-section {
  display: flex;
  align-items: center;
  gap: 1rem;
}

.job-title-section h3 {
  color: #333;
  margin: 0;
  font-size: 1.2rem;
}

.job-type {
  padding: 0.25rem 0.75rem;
  border-radius: 20px;
  font-size: 0.85rem;
  font-weight: 500;
}

.job-type.full_time {
  background-color: #d4edda;
  color: #155724;
}

.job-type.part_time {
  background-color: #fff3cd;
  color: #856404;
}

.job-type.internship {
  background-color: #cce5ff;
  color: #004085;
}

.job-salary {
  font-size: 1.3rem;
  font-weight: bold;
  color: #e74c3c;
}

.job-info {
  display: flex;
  gap: 1.5rem;
  margin-bottom: 1rem;
}

.company-name {
  color: #333;
  font-weight: 500;
}

.job-location, .job-industry {
  color: #666;
  font-size: 0.9rem;
}

.job-description {
  color: #666;
  line-height: 1.6;
  margin-bottom: 1rem;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 0.5rem;
  margin-bottom: 1rem;
}

.tag {
  padding: 0.25rem 0.75rem;
  background-color: #f0f4ff;
  color: #4a69bd;
  border-radius: 20px;
  font-size: 0.85rem;
}

.job-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 1rem;
  border-top: 1px solid #f0f0f0;
}

.post-date {
  color: #999;
  font-size: 0.85rem;
}

.apply-btn {
  padding: 0.5rem 1.5rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.apply-btn:hover {
  background-color: #3d5a9e;
}

.empty-state {
  text-align: center;
  padding: 4rem;
  background-color: white;
  border-radius: 12px;
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

.reset-btn {
  padding: 0.75rem 2rem;
  background-color: #4a69bd;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
}

.job-detail {
  padding: 0.5rem;
}

.detail-header {
  margin-bottom: 1.5rem;
}

.detail-title-section h2 {
  color: #333;
  margin: 0 0 0.5rem 0;
}

.detail-meta {
  display: flex;
  gap: 1.5rem;
}

.detail-company {
  color: #666;
}

.detail-salary {
  color: #e74c3c;
  font-weight: bold;
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: 1.5rem;
}

.detail-section h3 {
  color: #333;
  margin-bottom: 1rem;
  font-size: 1rem;
}

.detail-section p {
  color: #666;
  line-height: 1.6;
}

.detail-section ul {
  list-style: none;
  padding: 0;
}

.detail-section li {
  padding: 0.5rem 0;
  color: #666;
  border-bottom: 1px solid #f0f0f0;
}

.company-info {
  background-color: #f8f9fa;
  padding: 1rem;
  border-radius: 8px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.5rem 0;
  border-bottom: 1px solid #e0e0e0;
}

.info-label {
  color: #999;
}

.info-value {
  color: #333;
  font-weight: 500;
}
</style>