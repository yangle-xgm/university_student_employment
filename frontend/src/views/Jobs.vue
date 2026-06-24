<template>
  <AppLayout title="职位搜索">
    <div class="jobs-page">
      <!-- 筛选栏 -->
      <BaseCard class="filter-card">
        <div class="search-filters">
          <div class="filter-row">
            <el-input
              v-model="searchForm.keyword"
              placeholder="搜索职位关键词..."
              class="filter-input"
              clearable
              @keyup.enter="handleSearch"
            >
              <template #append>
                <el-button type="primary" @click="handleSearch">
                  <el-icon><Search /></el-icon>
                </el-button>
              </template>
            </el-input>
          </div>
          <div class="filter-row">
            <div class="filter-group">
              <label>工作地点</label>
              <el-select v-model="searchForm.location" placeholder="选择地点" class="filter-select" clearable>
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
              <el-select v-model="searchForm.industry" placeholder="选择行业" class="filter-select" clearable>
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
              <el-select v-model="searchForm.jobType" placeholder="选择类型" class="filter-select" clearable>
                <el-option label="全部" value="" />
                <el-option label="全职" value="全职" />
                <el-option label="兼职" value="兼职" />
                <el-option label="实习" value="实习" />
              </el-select>
            </div>
          </div>
        </div>
      </BaseCard>

      <!-- 统计与列表 -->
      <BaseCard class="list-card">
        <SectionHeader title="职位列表" :subtitle="`共找到 ${jobs.length} 个职位`">
          <el-button @click="resetFilters">重置筛选</el-button>
        </SectionHeader>

        <div class="jobs-list">
          <div v-for="job in jobs" :key="job.id" class="job-card" @click="handleApply(job)">
            <div class="job-header">
              <div class="job-title-section">
                <h3>{{ job.title }}</h3>
                <el-tag :type="getJobTypeType(job.jobType)" size="small" effect="light" round>
                  {{ getJobTypeLabel(job.jobType) }}
                </el-tag>
              </div>
              <div class="job-salary">{{ job.salary }}</div>
            </div>
            <div class="job-info">
              <span class="company-name">
                <el-icon><OfficeBuilding /></el-icon>
                {{ job.companyName }}
              </span>
              <span class="job-location">
                <el-icon><Location /></el-icon>
                {{ job.location }}
              </span>
              <span class="job-industry">
                <el-icon><Briefcase /></el-icon>
                {{ job.industry }}
              </span>
            </div>
            <p class="job-description">{{ job.description }}</p>
            <div class="job-tags">
              <el-tag v-for="tag in job.tags" :key="tag" type="info" size="small" effect="light" round>
                {{ tag }}
              </el-tag>
            </div>
            <div class="job-footer">
              <span class="post-date">
                <el-icon><Clock /></el-icon>
                发布时间: {{ job.postDate }}
              </span>
              <el-button type="primary" size="default" round @click.stop="handleApply(job)">
                立即申请
              </el-button>
            </div>
          </div>
        </div>

        <EmptyState
          v-if="jobs.length === 0"
          icon="Search"
          title="暂无匹配的职位"
          description="尝试调整筛选条件或关键词"
        >
          <el-button type="primary" @click="resetFilters">重置筛选条件</el-button>
        </EmptyState>
      </BaseCard>

      <!-- 职位详情弹窗 -->
      <el-dialog title="职位详情" v-model="showJobDetailModal" width="680px" destroy-on-close>
        <div v-if="selectedJob" class="job-detail">
          <div class="detail-header">
            <div class="detail-title-section">
              <h2>{{ selectedJob.title }}</h2>
              <div class="detail-meta">
                <span class="detail-company">
                  <el-icon><OfficeBuilding /></el-icon>
                  {{ selectedJob.companyName }}
                </span>
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
    </div>
  </AppLayout>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import AppLayout from '@/components/layout/AppLayout.vue'
import BaseCard from '@/components/common/BaseCard.vue'
import SectionHeader from '@/components/common/SectionHeader.vue'
import EmptyState from '@/components/common/EmptyState.vue'
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

const getJobTypeType = (type) => {
  const types = {
    'FULL_TIME': 'success',
    'PART_TIME': 'warning',
    'INTERNSHIP': 'info'
  }
  return types[type] || 'info'
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

onMounted(() => {
  const user = localStorage.getItem('userInfo')
  if (user) {
    userInfo.value = JSON.parse(user)
  }
  loadJobs()
})
</script>

<style scoped>
.jobs-page {
  display: flex;
  flex-direction: column;
  gap: var(--space-6);
}

.filter-card {
  padding: var(--space-5);
}

.search-filters {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.filter-row {
  display: flex;
  gap: var(--space-4);
  flex-wrap: wrap;
  align-items: flex-end;
}

.filter-input {
  width: 100%;
  max-width: 480px;
}

.filter-group {
  display: flex;
  flex-direction: column;
  gap: var(--space-2);
}

.filter-group label {
  font-size: var(--text-sm);
  font-weight: var(--font-medium);
  color: var(--color-text-2);
}

.filter-select {
  width: 180px;
}

.list-card {
  padding: var(--space-5);
}

.jobs-list {
  display: flex;
  flex-direction: column;
  gap: var(--space-4);
}

.job-card {
  background-color: var(--color-bg);
  border-radius: var(--radius-md);
  padding: var(--space-5);
  cursor: pointer;
  transition: transform var(--t-normal) var(--ease-default),
              box-shadow var(--t-normal) var(--ease-default);
}

.job-card:hover {
  transform: translateY(-2px);
  box-shadow: var(--elevation-2);
}

.job-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: var(--space-3);
  gap: var(--space-4);
}

.job-title-section {
  display: flex;
  align-items: center;
  gap: var(--space-3);
  flex-wrap: wrap;
}

.job-title-section h3 {
  color: var(--color-text-1);
  font-size: var(--text-lg);
  font-weight: var(--font-semibold);
  margin: 0;
}

.job-salary {
  font-size: var(--text-xl);
  font-weight: var(--font-bold);
  color: var(--color-danger);
  white-space: nowrap;
}

.job-info {
  display: flex;
  gap: var(--space-5);
  margin-bottom: var(--space-3);
  flex-wrap: wrap;
}

.job-info span {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  color: var(--color-text-2);
  font-size: var(--text-sm);
}

.job-description {
  color: var(--color-text-2);
  line-height: 1.6;
  margin-bottom: var(--space-3);
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.job-tags {
  display: flex;
  flex-wrap: wrap;
  gap: var(--space-2);
  margin-bottom: var(--space-4);
}

.job-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: var(--space-3);
  border-top: 1px solid var(--color-border);
  gap: var(--space-3);
}

.post-date {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  color: var(--color-text-3);
  font-size: var(--text-sm);
}

/* 弹窗详情 */
.job-detail {
  padding: var(--space-2);
}

.detail-header {
  margin-bottom: var(--space-5);
}

.detail-title-section h2 {
  color: var(--color-text-1);
  margin: 0 0 var(--space-3) 0;
  font-size: var(--text-xl);
}

.detail-meta {
  display: flex;
  align-items: center;
  gap: var(--space-4);
  flex-wrap: wrap;
}

.detail-company {
  display: inline-flex;
  align-items: center;
  gap: var(--space-1);
  color: var(--color-text-2);
}

.detail-salary {
  color: var(--color-danger);
  font-weight: var(--font-bold);
  font-size: var(--text-lg);
}

.detail-content {
  display: flex;
  flex-direction: column;
  gap: var(--space-5);
}

.detail-section h3 {
  color: var(--color-text-1);
  margin-bottom: var(--space-3);
  font-size: var(--text-base);
  font-weight: var(--font-semibold);
  padding-bottom: var(--space-2);
  border-bottom: 2px solid var(--color-primary-light);
}

.detail-section p {
  color: var(--color-text-2);
  line-height: 1.7;
}

.detail-section ul {
  list-style: none;
  padding: 0;
  margin: 0;
}

.detail-section li {
  padding: var(--space-2) 0;
  color: var(--color-text-2);
  border-bottom: 1px solid var(--color-border-light);
}

.detail-section li:last-child {
  border-bottom: none;
}

.company-info {
  background-color: var(--color-bg);
  padding: var(--space-4);
  border-radius: var(--radius);
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: var(--space-2) 0;
  border-bottom: 1px solid var(--color-border-light);
}

.info-row:last-child {
  border-bottom: none;
}

.info-label {
  color: var(--color-text-3);
}

.info-value {
  color: var(--color-text-1);
  font-weight: var(--font-medium);
}

@media (max-width: 640px) {
  .filter-row {
    flex-direction: column;
    align-items: stretch;
  }

  .filter-select {
    width: 100%;
  }

  .job-header {
    flex-direction: column;
  }

  .job-info {
    gap: var(--space-3);
  }

  .job-footer {
    flex-direction: column;
    align-items: flex-start;
  }
}
</style>
