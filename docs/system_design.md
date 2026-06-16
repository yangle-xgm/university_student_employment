# 大学生就业与职业成长平台 - 系统设计文档

## 1. 系统架构设计

### 1.1 架构概述
本系统采用前后端分离的微服务架构，后端基于 Spring Boot 框架构建，前端基于 Vue.js 框架构建。系统遵循 RESTful API 设计规范，实现模块化、可扩展的架构设计。

### 1.2 架构分层

```
┌─────────────────────────────────────────────────────────────────┐
│                        前端层 (Frontend)                        │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐           │
│  │ 学生端   │ │ 企业HR端 │ │ 管理端   │ │ 公共组件 │           │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘           │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTP/HTTPS
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                        网关层 (Gateway)                         │
│              Spring Cloud Gateway / Nginx                       │
│         路由转发 | 负载均衡 | 限流熔断 | 请求过滤                 │
└─────────────────────────┬───────────────────────────────────────┘
                          │ HTTP/HTTPS
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                        业务层 (Backend)                         │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐            │
│  │ 用户服务     │ │ 岗位服务     │ │ 简历服务     │            │
│  │ User Service │ │ Job Service  │ │ Resume Service│            │
│  └──────────────┘ └──────────────┘ └──────────────┘            │
│  ┌──────────────┐ ┌──────────────┐ ┌──────────────┐            │
│  │ 测评服务     │ │ 学习服务     │ │ 面试服务     │            │
│  │ Assess Service││ Learn Service│ │ Interview Svc│            │
│  └──────────────┘ └──────────────┘ └──────────────┘            │
└─────────────────────────┬───────────────────────────────────────┘
                          │ JDBC / JPA
                          ▼
┌─────────────────────────────────────────────────────────────────┐
│                        数据层 (Database)                        │
│              MySQL 8.0+  |  MinIO (文件存储)                    │
└─────────────────────────────────────────────────────────────────┘
```

### 1.3 核心组件说明

| 组件 | 职责 | 技术实现 |
| :--- | :--- | :--- |
| 用户服务 | 用户注册、登录、权限管理 | Spring Boot + Spring Security + JWT |
| 岗位服务 | 岗位发布、搜索、申请管理 | Spring Boot + Spring Data JPA |
| 简历服务 | 简历模板、编辑、优化评分 | Spring Boot + AI接口集成 |
| 测评服务 | 职业测评、报告生成 | Spring Boot + 测评算法 |
| 学习服务 | 课程管理、学习路径 | Spring Boot + MinIO |
| 面试服务 | 视频面试、模拟练习 | Spring Boot + WebRTC |

### 1.4 设计原则

| 原则 | 说明 |
| :--- | :--- |
| 单一职责 | 每个微服务只负责一个业务领域 |
| 无状态设计 | 服务不保存会话状态，便于水平扩展 |
| API优先 | 先定义API接口，再实现功能 |
| 模块化 | 功能模块解耦，便于独立开发和部署 |

---

## 2. 数据库设计

### 2.1 数据库表结构

#### 2.1.1 用户相关表

**表名: users**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 用户ID |
| username | VARCHAR(50) | NOT NULL, UNIQUE | 用户名 |
| password | VARCHAR(255) | NOT NULL | 密码(BCrypt加密) |
| email | VARCHAR(100) | NOT NULL, UNIQUE | 邮箱 |
| phone | VARCHAR(20) | NULL | 手机号 |
| real_name | VARCHAR(50) | NULL | 真实姓名 |
| avatar | VARCHAR(255) | NULL | 头像URL |
| role | ENUM | NOT NULL | 用户角色(STUDENT/HR/ADMIN) |
| status | ENUM | NOT NULL | 状态(ACTIVE/INACTIVE) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: student_profiles**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 主键ID |
| user_id | BIGINT | FOREIGN KEY, UNIQUE | 用户ID |
| school | VARCHAR(100) | NULL | 学校名称 |
| major | VARCHAR(100) | NULL | 专业 |
| grade | VARCHAR(20) | NULL | 年级 |
| graduation_date | DATE | NULL | 毕业日期 |
| education | VARCHAR(20) | NULL | 学历(BACHELOR/MASTER/PHD) |
| skills | TEXT | NULL | 技能列表(JSON) |
| resume_url | VARCHAR(255) | NULL | 简历文件URL |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: company_profiles**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 主键ID |
| user_id | BIGINT | FOREIGN KEY, UNIQUE | 用户ID |
| company_name | VARCHAR(100) | NOT NULL | 公司名称 |
| industry | VARCHAR(50) | NULL | 所属行业 |
| scale | VARCHAR(20) | NULL | 公司规模 |
| location | VARCHAR(100) | NULL | 公司地址 |
| description | TEXT | NULL | 公司简介 |
| logo_url | VARCHAR(255) | NULL | 公司logo |
| contact_name | VARCHAR(50) | NULL | 联系人 |
| contact_phone | VARCHAR(20) | NULL | 联系电话 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 2.1.2 岗位招聘相关表

**表名: jobs**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 岗位ID |
| company_id | BIGINT | FOREIGN KEY | 公司ID |
| title | VARCHAR(100) | NOT NULL | 岗位名称 |
| type | VARCHAR(20) | NOT NULL | 岗位类型(FULL_TIME/INTERNSHIP) |
| industry | VARCHAR(50) | NULL | 所属行业 |
| location | VARCHAR(100) | NOT NULL | 工作地点 |
| min_salary | DECIMAL(10,2) | NULL | 最低薪资 |
| max_salary | DECIMAL(10,2) | NULL | 最高薪资 |
| education | VARCHAR(20) | NULL | 学历要求 |
| experience | VARCHAR(20) | NULL | 经验要求 |
| requirements | TEXT | NULL | 岗位要求 |
| responsibilities | TEXT | NULL | 岗位职责 |
| benefits | TEXT | NULL | 公司福利 |
| deadline | DATE | NULL | 申请截止日期 |
| status | ENUM | NOT NULL | 状态(PUBLISHED/PAUSED/DELETED) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: applications**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 申请ID |
| job_id | BIGINT | FOREIGN KEY | 岗位ID |
| student_id | BIGINT | FOREIGN KEY | 学生ID |
| resume_id | BIGINT | FOREIGN KEY | 简历ID |
| status | ENUM | NOT NULL | 状态(PENDING/APPROVED/REJECTED/OFFER) |
| apply_time | DATETIME | NOT NULL | 申请时间 |
| feedback | TEXT | NULL | 反馈意见 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 2.1.3 简历相关表

**表名: resumes**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 简历ID |
| student_id | BIGINT | FOREIGN KEY | 学生ID |
| template_id | BIGINT | FOREIGN KEY | 模板ID |
| title | VARCHAR(100) | NOT NULL | 简历标题 |
| content | TEXT | NULL | 简历内容(JSON) |
| file_url | VARCHAR(255) | NULL | 简历文件URL |
| is_default | BOOLEAN | DEFAULT FALSE | 是否默认简历 |
| format_score | INT | NULL | 格式规范度评分 |
| content_score | INT | NULL | 内容完整度评分 |
| match_score | INT | NULL | 岗位匹配度评分 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: resume_templates**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 模板ID |
| name | VARCHAR(50) | NOT NULL | 模板名称 |
| industry | VARCHAR(50) | NOT NULL | 适用行业 |
| preview_url | VARCHAR(255) | NULL | 预览图URL |
| content | TEXT | NOT NULL | 模板内容(JSON) |
| status | ENUM | NOT NULL | 状态(ACTIVE/INACTIVE) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 2.1.4 职业测评相关表

**表名: assessments**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 测评ID |
| name | VARCHAR(100) | NOT NULL | 测评名称 |
| description | TEXT | NULL | 测评描述 |
| dimension | VARCHAR(50) | NOT NULL | 测评维度(INTEREST/PERSONALITY/ABILITY/VALUES/CAREER_ANCHOR) |
| duration | INT | NULL | 预计时长(分钟) |
| status | ENUM | NOT NULL | 状态(ACTIVE/INACTIVE) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: assessment_questions**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 题目ID |
| assessment_id | BIGINT | FOREIGN KEY | 测评ID |
| question_type | VARCHAR(20) | NOT NULL | 题目类型(SINGLE/MULTIPLE/SCALE) |
| content | TEXT | NOT NULL | 题目内容 |
| options | TEXT | NULL | 选项(JSON) |
| weight | DECIMAL(3,2) | DEFAULT 1.0 | 权重 |
| sort_order | INT | DEFAULT 0 | 排序号 |
| created_at | DATETIME | NOT NULL | 创建时间 |

**表名: assessment_records**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录ID |
| student_id | BIGINT | FOREIGN KEY | 学生ID |
| assessment_id | BIGINT | FOREIGN KEY | 测评ID |
| answers | TEXT | NOT NULL | 答案(JSON) |
| score | DECIMAL(5,2) | NULL | 得分 |
| status | ENUM | NOT NULL | 状态(IN_PROGRESS/COMPLETED) |
| completed_at | DATETIME | NULL | 完成时间 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: assessment_reports**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 报告ID |
| record_id | BIGINT | FOREIGN KEY | 测评记录ID |
| student_id | BIGINT | FOREIGN KEY | 学生ID |
| content | TEXT | NOT NULL | 报告内容(JSON) |
| recommendations | TEXT | NULL | 职业建议 |
| generated_at | DATETIME | NOT NULL | 生成时间 |

#### 2.1.5 学习成长相关表

**表名: learning_resources**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 资源ID |
| title | VARCHAR(200) | NOT NULL | 资源标题 |
| type | VARCHAR(20) | NOT NULL | 资源类型(VIDEO/EBOOK/CASE) |
| category | VARCHAR(50) | NULL | 分类 |
| difficulty | VARCHAR(20) | NULL | 难度(BEGINNER/INTERMEDIATE/ADVANCED) |
| duration | INT | NULL | 时长(分钟) |
| url | VARCHAR(255) | NOT NULL | 资源URL |
| thumbnail_url | VARCHAR(255) | NULL | 封面图URL |
| description | TEXT | NULL | 描述 |
| view_count | INT | DEFAULT 0 | 浏览次数 |
| status | ENUM | NOT NULL | 状态(ACTIVE/INACTIVE) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: learning_paths**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 路径ID |
| title | VARCHAR(100) | NOT NULL | 路径名称 |
| description | TEXT | NULL | 路径描述 |
| career_goal | VARCHAR(50) | NULL | 职业目标 |
| estimated_duration | INT | NULL | 预计时长(小时) |
| status | ENUM | NOT NULL | 状态(ACTIVE/INACTIVE) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: learning_path_resources**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 主键ID |
| path_id | BIGINT | FOREIGN KEY | 路径ID |
| resource_id | BIGINT | FOREIGN KEY | 资源ID |
| sort_order | INT | DEFAULT 0 | 排序号 |
| created_at | DATETIME | NOT NULL | 创建时间 |

**表名: learning_records**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 记录ID |
| student_id | BIGINT | FOREIGN KEY | 学生ID |
| resource_id | BIGINT | FOREIGN KEY | 资源ID |
| progress | DECIMAL(5,2) | DEFAULT 0 | 学习进度(0-100) |
| completed | BOOLEAN | DEFAULT FALSE | 是否完成 |
| last_watch_time | DATETIME | NULL | 最后观看时间 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

#### 2.1.6 面试相关表

**表名: interviews**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 面试ID |
| application_id | BIGINT | FOREIGN KEY | 申请ID |
| type | VARCHAR(20) | NOT NULL | 面试类型(REAL/SIMULATION) |
| scheduled_time | DATETIME | NOT NULL | 预约时间 |
| status | ENUM | NOT NULL | 状态(SCHEDULED/IN_PROGRESS/COMPLETED/CANCELLED) |
| meeting_url | VARCHAR(255) | NULL | 会议链接 |
| recording_url | VARCHAR(255) | NULL | 录像URL |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: interview_questions**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 题目ID |
| category | VARCHAR(50) | NULL | 题目分类 |
| content | TEXT | NOT NULL | 题目内容 |
| answer_hint | TEXT | NULL | 回答要点 |
| difficulty | VARCHAR(20) | NULL | 难度 |
| status | ENUM | NOT NULL | 状态(ACTIVE/INACTIVE) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: interview_evaluations**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 评价ID |
| interview_id | BIGINT | FOREIGN KEY | 面试ID |
| evaluator_id | BIGINT | FOREIGN KEY | 评价人ID |
| score | INT | NULL | 综合评分 |
| communication_score | INT | NULL | 沟通能力评分 |
| technical_score | INT | NULL | 技术能力评分 |
| attitude_score | INT | NULL | 态度评分 |
| comments | TEXT | NULL | 评价意见 |
| created_at | DATETIME | NOT NULL | 创建时间 |

#### 2.1.7 职业规划相关表

**表名: career_plans**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 规划ID |
| student_id | BIGINT | FOREIGN KEY | 学生ID |
| title | VARCHAR(100) | NOT NULL | 规划名称 |
| goal_short | TEXT | NULL | 短期目标(1年内) |
| goal_medium | TEXT | NULL | 中期目标(3年内) |
| goal_long | TEXT | NULL | 长期目标(5年以上) |
| current_stage | VARCHAR(50) | NULL | 当前阶段 |
| progress | DECIMAL(5,2) | DEFAULT 0 | 整体进度 |
| status | ENUM | NOT NULL | 状态(ACTIVE/COMPLETED/ARCHIVED) |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

**表名: career_milestones**

| 字段名 | 类型 | 约束 | 说明 |
| :--- | :--- | :--- | :--- |
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT | 里程碑ID |
| plan_id | BIGINT | FOREIGN KEY | 规划ID |
| title | VARCHAR(100) | NOT NULL | 里程碑名称 |
| description | TEXT | NULL | 里程碑描述 |
| target_date | DATE | NULL | 目标日期 |
| progress | DECIMAL(5,2) | DEFAULT 0 | 进度(0-100) |
| completed | BOOLEAN | DEFAULT FALSE | 是否完成 |
| created_at | DATETIME | NOT NULL | 创建时间 |
| updated_at | DATETIME | NOT NULL | 更新时间 |

### 2.2 数据库ER图

```
用户相关关系:
┌──────────┐       ┌────────────────┐       ┌──────────────────┐
│  users   │───────│student_profiles│       │company_profiles  │
└──────────┘       └────────────────┘       └──────────────────┘

岗位招聘关系:
┌──────────┐       ┌──────────┐       ┌──────────────┐
│companies │───────│   jobs   │───────│ applications │
└──────────┘       └──────────┘       └──────────────┘
                                           │
                                           ▼
                                    ┌────────────┐
                                    │  resumes   │
                                    └────────────┘

测评关系:
┌──────────────┐       ┌────────────────────┐       ┌──────────────────┐
│ assessments  │───────│assessment_questions│       │assessment_records│
└──────────────┘       └────────────────────┘       └──────────────────┘
                                                           │
                                                           ▼
                                                  ┌──────────────────┐
                                                  │assessment_reports│
                                                  └──────────────────┘

学习关系:
┌──────────────────┐       ┌──────────────────┐       ┌────────────────┐
│learning_resources│───────│learning_path_res│───────│learning_paths  │
└──────────────────┘       └──────────────────┘       └────────────────┘
       │
       ▼
┌────────────────┐
│learning_records│
└────────────────┘

面试关系:
┌────────────┐       ┌──────────────────┐       ┌────────────────────┐
│ interviews │───────│interview_evaluations│   │interview_questions │
└────────────┘       └──────────────────┘       └────────────────────┘

职业规划关系:
┌──────────────┐       ┌─────────────────┐
│career_plans  │───────│career_milestones│
└──────────────┘       └─────────────────┘
```

---

## 3. API接口设计

### 3.1 接口规范

| 规范项 | 说明 |
| :--- | :--- |
| 协议 | HTTP/HTTPS |
| 基础路径 | `/api/v1` |
| 数据格式 | JSON |
| 字符编码 | UTF-8 |
| 认证方式 | JWT Token (Header: Authorization: Bearer {token}) |
| 统一响应格式 | `{"code": 0, "message": "success", "data": {}}` |

### 3.2 统一响应结构

```json
{
  "code": 0,
  "message": "success",
  "data": {},
  "timestamp": 1672531200000
}
```

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| code | INT | 状态码 (0成功, 非0失败) |
| message | STRING | 响应消息 |
| data | OBJECT | 响应数据 |
| timestamp | LONG | 响应时间戳 |

### 3.3 接口列表

#### 3.3.1 用户认证接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/auth/login` | POST | `AuthController.java` | 用户登录 |
| `/api/v1/auth/register` | POST | `AuthController.java` | 用户注册 |
| `/api/v1/auth/logout` | POST | `AuthController.java` | 用户登出 |
| `/api/v1/auth/refresh-token` | POST | `AuthController.java` | 刷新Token |
| `/api/v1/auth/me` | GET | `AuthController.java` | 获取当前用户信息 |

#### 3.3.2 用户管理接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/users` | GET | `UserController.java` | 获取用户列表(管理员) |
| `/api/v1/users/{id}` | GET | `UserController.java` | 获取用户详情 |
| `/api/v1/users/{id}` | PUT | `UserController.java` | 更新用户信息 |
| `/api/v1/users/{id}` | DELETE | `UserController.java` | 删除用户(管理员) |
| `/api/v1/users/profile` | PUT | `UserController.java` | 更新个人资料 |
| `/api/v1/students/profile` | PUT | `StudentController.java` | 更新学生资料 |
| `/api/v1/companies/profile` | PUT | `CompanyController.java` | 更新企业资料 |

#### 3.3.3 岗位招聘接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/jobs` | GET | `JobController.java` | 获取岗位列表 |
| `/api/v1/jobs/{id}` | GET | `JobController.java` | 获取岗位详情 |
| `/api/v1/jobs` | POST | `JobController.java` | 发布岗位(HR) |
| `/api/v1/jobs/{id}` | PUT | `JobController.java` | 更新岗位(HR) |
| `/api/v1/jobs/{id}` | DELETE | `JobController.java` | 删除岗位(HR) |
| `/api/v1/jobs/{id}/apply` | POST | `ApplicationController.java` | 申请岗位 |
| `/api/v1/applications` | GET | `ApplicationController.java` | 获取申请列表 |
| `/api/v1/applications/{id}` | GET | `ApplicationController.java` | 获取申请详情 |
| `/api/v1/applications/{id}` | PUT | `ApplicationController.java` | 更新申请状态(HR) |

#### 3.3.4 简历管理接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/resumes` | GET | `ResumeController.java` | 获取简历列表 |
| `/api/v1/resumes/{id}` | GET | `ResumeController.java` | 获取简历详情 |
| `/api/v1/resumes` | POST | `ResumeController.java` | 创建简历 |
| `/api/v1/resumes/{id}` | PUT | `ResumeController.java` | 更新简历 |
| `/api/v1/resumes/{id}` | DELETE | `ResumeController.java` | 删除简历 |
| `/api/v1/resumes/{id}/optimize` | POST | `ResumeController.java` | AI优化建议 |
| `/api/v1/resumes/{id}/score` | POST | `ResumeController.java` | 简历评分 |
| `/api/v1/resume-templates` | GET | `ResumeController.java` | 获取模板列表 |
| `/api/v1/resume-templates/{id}` | GET | `ResumeController.java` | 获取模板详情 |

#### 3.3.5 职业测评接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/assessments` | GET | `AssessmentController.java` | 获取测评列表 |
| `/api/v1/assessments/{id}` | GET | `AssessmentController.java` | 获取测评详情 |
| `/api/v1/assessments/{id}/start` | POST | `AssessmentController.java` | 开始测评 |
| `/api/v1/assessments/{id}/submit` | POST | `AssessmentController.java` | 提交测评 |
| `/api/v1/assessment-records` | GET | `AssessmentController.java` | 获取测评记录 |
| `/api/v1/reports/{id}` | GET | `AssessmentController.java` | 获取测评报告 |

#### 3.3.6 学习成长接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/resources` | GET | `LearningController.java` | 获取学习资源列表 |
| `/api/v1/resources/{id}` | GET | `LearningController.java` | 获取资源详情 |
| `/api/v1/resources/{id}/watch` | POST | `LearningController.java` | 记录学习进度 |
| `/api/v1/learning-paths` | GET | `LearningController.java` | 获取学习路径列表 |
| `/api/v1/learning-paths/{id}` | GET | `LearningController.java` | 获取学习路径详情 |
| `/api/v1/learning-records` | GET | `LearningController.java` | 获取学习记录 |

#### 3.3.7 线上面试接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/interviews` | GET | `InterviewController.java` | 获取面试列表 |
| `/api/v1/interviews/{id}` | GET | `InterviewController.java` | 获取面试详情 |
| `/api/v1/interviews` | POST | `InterviewController.java` | 创建面试 |
| `/api/v1/interviews/{id}` | PUT | `InterviewController.java` | 更新面试 |
| `/api/v1/interviews/{id}` | DELETE | `InterviewController.java` | 删除面试 |
| `/api/v1/interviews/{id}/join` | POST | `InterviewController.java` | 加入面试 |
| `/api/v1/interview-questions` | GET | `InterviewController.java` | 获取面试题库 |
| `/api/v1/interviews/{id}/evaluate` | POST | `InterviewController.java` | 提交面试评价 |

#### 3.3.8 职业规划接口

| API路径 | HTTP方法 | Controller文件 | 功能描述 |
| :--- | :--- | :--- | :--- |
| `/api/v1/career-plans` | GET | `CareerController.java` | 获取职业规划列表 |
| `/api/v1/career-plans/{id}` | GET | `CareerController.java` | 获取职业规划详情 |
| `/api/v1/career-plans` | POST | `CareerController.java` | 创建职业规划 |
| `/api/v1/career-plans/{id}` | PUT | `CareerController.java` | 更新职业规划 |
| `/api/v1/career-plans/{id}` | DELETE | `CareerController.java` | 删除职业规划 |
| `/api/v1/career-plans/{id}/milestones` | POST | `CareerController.java` | 添加里程碑 |
| `/api/v1/career-plans/{id}/milestones/{mid}` | PUT | `CareerController.java` | 更新里程碑 |
| `/api/v1/career-plans/{id}/milestones/{mid}` | DELETE | `CareerController.java` | 删除里程碑 |

---

## 4. 安全与权限设计

### 4.1 角色定义

| 角色 | 标识 | 权限范围 |
| :--- | :--- | :--- |
| 学生 | STUDENT | 岗位搜索、简历管理、测评、学习、面试 |
| 企业HR | HR | 岗位发布、简历筛选、面试管理 |
| 平台管理员 | ADMIN | 系统管理、用户管理、内容管理 |

### 4.2 权限控制策略

| 模块 | STUDENT | HR | ADMIN |
| :--- | :---: | :---: | :---: |
| 用户管理 | 查看自己 | 查看企业信息 | 全部权限 |
| 岗位管理 | 搜索、申请 | 发布、管理 | 全部权限 |
| 简历管理 | 创建、管理自己的 | 查看申请人简历 | 全部权限 |
| 测评管理 | 参与测评 | 无 | 全部权限 |
| 学习管理 | 学习资源 | 无 | 全部权限 |
| 面试管理 | 参与面试 | 创建、管理 | 全部权限 |
| 职业规划 | 创建、管理自己的 | 无 | 全部权限 |

### 4.3 安全措施

| 措施 | 说明 |
| :--- | :--- |
| JWT认证 | 无状态认证，Token有效期2小时 |
| 密码加密 | BCrypt算法加密存储 |
| 权限校验 | Spring Security + 自定义注解 |
| 请求限流 | 防止恶意请求 |
| SQL注入防护 | JPA预编译语句 |
| XSS防护 | 输入输出过滤 |

---

## 5. 部署架构

### 5.1 服务器架构

```
                    ┌─────────────────────────────────┐
                    │         负载均衡层              │
                    │         Nginx / LVS            │
                    └───────────────┬───────────────┘
                                    │
        ┌───────────────────────────┼───────────────────────────┐
        │                           │                           │
        ▼                           ▼                           ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│   Web Server  │         │   Web Server  │         │   Web Server  │
│   (Nginx)     │         │   (Nginx)     │         │   (Nginx)     │
└───────┬───────┘         └───────┬───────┘         └───────┬───────┘
        │                         │                         │
        ▼                         ▼                         ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│   App Server  │         │   App Server  │         │   App Server  │
│  (SpringBoot) │         │  (SpringBoot) │         │  (SpringBoot) │
└───────────────┘         └───────────────┘         └───────────────┘
        │                         │                         │
        └─────────────────────────┼─────────────────────────┘
                                  │
                                  ▼
                    ┌─────────────────────────────────┐
                    │        数据库集群               │
                    │     MySQL Master-Slave         │
                    └─────────────────────────────────┘
                                  │
                    ┌─────────────┴─────────────┐
                    ▼                           ▼
              ┌───────────┐             ┌───────────┐
              │   MinIO   │             │  Redis    │
              │ (文件存储) │             │ (缓存)    │
              └───────────┘             └───────────┘
```

### 5.2 环境配置

| 环境 | 描述 | 配置 |
| :--- | :--- | :--- |
| 开发环境 | 本地开发 | H2内存数据库 |
| 测试环境 | 测试验证 | MySQL + Docker |
| 生产环境 | 线上运行 | MySQL集群 + Redis + MinIO |

---

**文档版本**: V1.0  
**创建日期**: 2026-06-16  
**作者**: 开发团队