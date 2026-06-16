CREATE DATABASE IF NOT EXISTS employment_platform DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE employment_platform;

DROP TABLE IF EXISTS career_milestones;
DROP TABLE IF EXISTS career_plans;
DROP TABLE IF EXISTS interview_evaluations;
DROP TABLE IF EXISTS interview_questions;
DROP TABLE IF EXISTS interviews;
DROP TABLE IF EXISTS learning_records;
DROP TABLE IF EXISTS learning_path_resources;
DROP TABLE IF EXISTS learning_paths;
DROP TABLE IF EXISTS learning_resources;
DROP TABLE IF EXISTS assessment_reports;
DROP TABLE IF EXISTS assessment_records;
DROP TABLE IF EXISTS assessment_questions;
DROP TABLE IF EXISTS assessments;
DROP TABLE IF EXISTS resume_templates;
DROP TABLE IF EXISTS resumes;
DROP TABLE IF EXISTS applications;
DROP TABLE IF EXISTS jobs;
DROP TABLE IF EXISTS company_profiles;
DROP TABLE IF EXISTS student_profiles;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    phone VARCHAR(20),
    real_name VARCHAR(50),
    avatar VARCHAR(255),
    role ENUM('STUDENT', 'HR', 'ADMIN') NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE student_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    school VARCHAR(100),
    major VARCHAR(100),
    grade VARCHAR(20),
    graduation_date DATE,
    education VARCHAR(20),
    skills TEXT,
    resume_url VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE company_profiles (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id BIGINT NOT NULL UNIQUE,
    company_name VARCHAR(100) NOT NULL,
    industry VARCHAR(50),
    scale VARCHAR(20),
    location VARCHAR(100),
    description TEXT,
    logo_url VARCHAR(255),
    contact_name VARCHAR(50),
    contact_phone VARCHAR(20),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE jobs (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    industry VARCHAR(50),
    location VARCHAR(100) NOT NULL,
    min_salary DECIMAL(10,2),
    max_salary DECIMAL(10,2),
    education VARCHAR(20),
    experience VARCHAR(20),
    requirements TEXT,
    responsibilities TEXT,
    benefits TEXT,
    deadline DATE,
    status ENUM('PUBLISHED', 'PAUSED', 'DELETED') NOT NULL DEFAULT 'PUBLISHED',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (company_id) REFERENCES company_profiles(id) ON DELETE CASCADE
);

CREATE TABLE resumes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    template_id BIGINT,
    title VARCHAR(100) NOT NULL,
    content TEXT,
    file_url VARCHAR(255),
    is_default BOOLEAN DEFAULT FALSE,
    format_score INT,
    content_score INT,
    match_score INT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

CREATE TABLE resume_templates (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL,
    industry VARCHAR(50) NOT NULL,
    preview_url VARCHAR(255),
    content TEXT NOT NULL,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE applications (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    job_id BIGINT NOT NULL,
    student_id BIGINT NOT NULL,
    resume_id BIGINT NOT NULL,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'OFFER') NOT NULL DEFAULT 'PENDING',
    apply_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    feedback TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (job_id) REFERENCES jobs(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (resume_id) REFERENCES resumes(id) ON DELETE CASCADE
);

CREATE TABLE assessments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    description TEXT,
    dimension VARCHAR(50) NOT NULL,
    duration INT,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE assessment_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    assessment_id BIGINT NOT NULL,
    question_type VARCHAR(20) NOT NULL,
    content TEXT NOT NULL,
    options TEXT,
    weight DECIMAL(3,2) DEFAULT 1.0,
    sort_order INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

CREATE TABLE assessment_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    assessment_id BIGINT NOT NULL,
    answers TEXT NOT NULL,
    score DECIMAL(5,2),
    status ENUM('IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'IN_PROGRESS',
    completed_at DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (assessment_id) REFERENCES assessments(id) ON DELETE CASCADE
);

CREATE TABLE assessment_reports (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    record_id BIGINT NOT NULL UNIQUE,
    student_id BIGINT NOT NULL,
    content TEXT NOT NULL,
    recommendations TEXT,
    generated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (record_id) REFERENCES assessment_records(id) ON DELETE CASCADE,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

CREATE TABLE learning_resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(200) NOT NULL,
    type VARCHAR(20) NOT NULL,
    category VARCHAR(50),
    difficulty VARCHAR(20),
    duration INT,
    url VARCHAR(255) NOT NULL,
    thumbnail_url VARCHAR(255),
    description TEXT,
    view_count INT DEFAULT 0,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE learning_paths (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    career_goal VARCHAR(50),
    estimated_duration INT,
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE learning_path_resources (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    path_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    sort_order INT DEFAULT 0,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (path_id) REFERENCES learning_paths(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resources(id) ON DELETE CASCADE
);

CREATE TABLE learning_records (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    resource_id BIGINT NOT NULL,
    progress DECIMAL(5,2) DEFAULT 0,
    completed BOOLEAN DEFAULT FALSE,
    last_watch_time DATETIME,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE,
    FOREIGN KEY (resource_id) REFERENCES learning_resources(id) ON DELETE CASCADE
);

CREATE TABLE interviews (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    application_id BIGINT,
    type VARCHAR(20) NOT NULL,
    scheduled_time DATETIME NOT NULL,
    status ENUM('SCHEDULED', 'IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'SCHEDULED',
    meeting_url VARCHAR(255),
    recording_url VARCHAR(255),
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (application_id) REFERENCES applications(id) ON DELETE CASCADE
);

CREATE TABLE interview_questions (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    category VARCHAR(50),
    content TEXT NOT NULL,
    answer_hint TEXT,
    difficulty VARCHAR(20),
    status ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE interview_evaluations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    interview_id BIGINT NOT NULL,
    evaluator_id BIGINT NOT NULL,
    score INT,
    communication_score INT,
    technical_score INT,
    attitude_score INT,
    comments TEXT,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (interview_id) REFERENCES interviews(id) ON DELETE CASCADE,
    FOREIGN KEY (evaluator_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE career_plans (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    student_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    goal_short TEXT,
    goal_medium TEXT,
    goal_long TEXT,
    current_stage VARCHAR(50),
    progress DECIMAL(5,2) DEFAULT 0,
    status ENUM('ACTIVE', 'COMPLETED', 'ARCHIVED') NOT NULL DEFAULT 'ACTIVE',
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (student_id) REFERENCES student_profiles(id) ON DELETE CASCADE
);

CREATE TABLE career_milestones (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plan_id BIGINT NOT NULL,
    title VARCHAR(100) NOT NULL,
    description TEXT,
    target_date DATE,
    progress DECIMAL(5,2) DEFAULT 0,
    completed BOOLEAN DEFAULT FALSE,
    created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (plan_id) REFERENCES career_plans(id) ON DELETE CASCADE
);

ALTER TABLE resumes ADD CONSTRAINT fk_resume_template FOREIGN KEY (template_id) REFERENCES resume_templates(id);

INSERT INTO users (username, password, email, phone, real_name, role, status) VALUES
('admin', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'admin@example.com', '13800138000', '管理员', 'ADMIN', 'ACTIVE'),
('student1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'student1@example.com', '13800138001', '张三', 'STUDENT', 'ACTIVE'),
('hr1', '$2a$10$N9qo8uLOickgx2ZMRZoMye.IjzqAKL9xL5jvMFVdNJHvGCgTq/VEq', 'hr1@example.com', '13800138002', '李四', 'HR', 'ACTIVE');

INSERT INTO student_profiles (user_id, school, major, grade, education, skills) VALUES
((SELECT id FROM users WHERE username = 'student1'), '北京大学', '计算机科学与技术', '大四', 'BACHELOR', '["Java", "Python", "Spring Boot"]');

INSERT INTO company_profiles (user_id, company_name, industry, scale, location, description) VALUES
((SELECT id FROM users WHERE username = 'hr1'), '腾讯科技', '互联网', '10000+', '深圳', '腾讯是一家知名互联网公司');

INSERT INTO jobs (company_id, title, type, industry, location, min_salary, max_salary, education, experience, requirements, responsibilities, benefits, deadline) VALUES
((SELECT id FROM company_profiles WHERE company_name = '腾讯科技'), 'Java开发工程师', 'FULL_TIME', '互联网', '深圳', 15000.00, 25000.00, 'BACHELOR', '1-3年', '本科及以上学历，Java基础扎实', '负责后端系统开发', '五险一金，年终奖', '2026-12-31');

INSERT INTO resume_templates (name, industry, content) VALUES
('IT技术岗模板', 'IT', '{"sections": ["个人信息", "教育背景", "专业技能", "项目经验", "实习经历"]}'),
('金融行业模板', '金融', '{"sections": ["个人信息", "教育背景", "证书资质", "工作经历", "项目经验"]}'),
('市场营销模板', '市场营销', '{"sections": ["个人信息", "教育背景", "工作经历", "营销案例", "技能证书"]}'),
('人力资源模板', '人力资源', '{"sections": ["个人信息", "教育背景", "工作经历", "HR项目", "专业技能"]}'),
('制造业模板', '制造业', '{"sections": ["个人信息", "教育背景", "工作经历", "项目经验", "技能特长"]}'),
('教育行业模板', '教育', '{"sections": ["个人信息", "教育背景", "教学经历", "获奖情况", "资格证书"]}'),
('医疗健康模板', '医疗', '{"sections": ["个人信息", "教育背景", "执业资格", "工作经历", "专业技能"]}'),
('物流运输模板', '物流', '{"sections": ["个人信息", "教育背景", "工作经历", "项目经验", "职业证书"]}'),
('房地产模板', '房地产', '{"sections": ["个人信息", "教育背景", "工作经历", "业绩成果", "专业技能"]}'),
('文化传媒模板', '文化传媒', '{"sections": ["个人信息", "教育背景", "工作经历", "作品集", "专业技能"]}');

INSERT INTO assessments (name, description, dimension, duration) VALUES
('职业兴趣测评', '了解您的职业兴趣倾向', 'INTEREST', 15),
('性格特质测评', '分析您的性格特点', 'PERSONALITY', 20),
('能力倾向测评', '评估您的能力优势', 'ABILITY', 25),
('价值观测评', '探索您的职业价值观', 'VALUES', 10),
('职业锚点测评', '确定您的职业定位', 'CAREER_ANCHOR', 15);

INSERT INTO assessment_questions (assessment_id, question_type, content, options) VALUES
((SELECT id FROM assessments WHERE dimension = 'INTEREST'), 'SINGLE', '您更喜欢哪种工作环境？', '["安静独立的办公室", "热闹的团队环境", "户外工作场所", "创意工作室"]'),
((SELECT id FROM assessments WHERE dimension = 'INTEREST'), 'SINGLE', '您更倾向于从事哪种类型的工作？', '["数据分析", "创意设计", "人际交往", "技术研发"]'),
((SELECT id FROM assessments WHERE dimension = 'PERSONALITY'), 'SCALE', '我是一个善于社交的人', '["非常不符合", "不太符合", "一般", "比较符合", "非常符合"]'),
((SELECT id FROM assessments WHERE dimension = 'PERSONALITY'), 'SCALE', '我做事比较谨慎', '["非常不符合", "不太符合", "一般", "比较符合", "非常符合"]'),
((SELECT id FROM assessments WHERE dimension = 'ABILITY'), 'SINGLE', '您认为自己最擅长的能力是？', '["逻辑思维", "语言表达", "空间想象", "动手能力"]'),
((SELECT id FROM assessments WHERE dimension = 'VALUES'), 'SINGLE', '您选择工作时最看重的是？', '["薪资待遇", "发展空间", "工作氛围", "稳定性"]'),
((SELECT id FROM assessments WHERE dimension = 'CAREER_ANCHOR'), 'SINGLE', '您希望在工作中获得什么？', '["专业成长", "职位晋升", "工作自由", "稳定收入"]');

INSERT INTO learning_resources (title, type, category, difficulty, duration, url, description) VALUES
('Java基础入门', 'VIDEO', '编程开发', 'BEGINNER', 120, 'https://example.com/java-basic', 'Java编程语言基础课程'),
('Spring Boot实战', 'VIDEO', '编程开发', 'INTERMEDIATE', 180, 'https://example.com/spring-boot', 'Spring Boot框架实战教程'),
('简历写作技巧', 'EBOOK', '职业发展', 'BEGINNER', 60, 'https://example.com/resume-guide', '专业简历写作指南'),
('面试技巧大全', 'EBOOK', '职业发展', 'INTERMEDIATE', 90, 'https://example.com/interview-guide', '面试准备与技巧'),
('产品经理案例分析', 'CASE', '产品运营', 'ADVANCED', 45, 'https://example.com/product-case', '知名产品案例深度分析');

INSERT INTO learning_paths (title, description, career_goal, estimated_duration) VALUES
('Java后端开发工程师成长路径', '从零基础到Java工程师的完整学习路径', 'JAVA_ENGINEER', 200),
('产品经理入门到精通', '系统学习产品经理必备技能', 'PRODUCT_MANAGER', 150),
('数据分析专家之路', '掌握数据分析核心技能', 'DATA_ANALYST', 180);

INSERT INTO interview_questions (category, content, answer_hint, difficulty) VALUES
('自我介绍', '请做一个简单的自我介绍', '介绍基本信息、专业背景、核心优势', 'BEGINNER'),
('职业规划', '谈谈你的职业规划', '短期目标、长期目标、如何实现', 'INTERMEDIATE'),
('项目经验', '介绍一个你参与过的项目', 'STAR法则：情境、任务、行动、结果', 'INTERMEDIATE'),
('技术问题', '什么是RESTful API？', 'REST的基本概念和设计原则', 'ADVANCED'),
('抗压能力', '你如何处理工作压力？', '举例说明处理压力的方法', 'INTERMEDIATE');

INSERT INTO resumes (student_id, template_id, title, content) VALUES
((SELECT id FROM student_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'student1')), 1, '个人简历', '{"personalInfo": {"name": "张三", "phone": "13800138001", "email": "student1@example.com"}, "education": [{"school": "北京大学", "major": "计算机科学与技术", "degree": "本科"}]}');

INSERT INTO applications (job_id, student_id, resume_id) VALUES
((SELECT id FROM jobs WHERE title = 'Java开发工程师'), (SELECT id FROM student_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'student1')), (SELECT id FROM resumes WHERE title = '个人简历'));

INSERT INTO career_plans (student_id, title, goal_short, goal_medium, goal_long) VALUES
((SELECT id FROM student_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'student1')), '我的职业规划', '毕业后进入互联网公司担任Java开发工程师', '3年内成长为高级开发工程师', '5年内成为技术主管');

INSERT INTO career_milestones (plan_id, title, description, target_date) VALUES
((SELECT id FROM career_plans WHERE title = '我的职业规划'), '完成毕业论文', '按时完成本科毕业论文', '2026-06-30'),
((SELECT id FROM career_plans WHERE title = '我的职业规划'), '拿到心仪offer', '拿到互联网大厂offer', '2026-12-31'),
((SELECT id FROM career_plans WHERE title = '我的职业规划'), '晋升高级工程师', '工作3年内晋升为高级工程师', '2029-12-31');

INSERT INTO learning_records (student_id, resource_id, progress, completed) VALUES
((SELECT id FROM student_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'student1')), 1, 50.00, FALSE),
((SELECT id FROM student_profiles WHERE user_id = (SELECT id FROM users WHERE username = 'student1')), 3, 100.00, TRUE);

INSERT INTO interviews (application_id, type, scheduled_time) VALUES
((SELECT id FROM applications WHERE job_id = (SELECT id FROM jobs WHERE title = 'Java开发工程师')), 'REAL', '2026-07-01 10:00:00');

INSERT INTO interview_evaluations (interview_id, evaluator_id, score, communication_score, technical_score, attitude_score, comments) VALUES
((SELECT id FROM interviews WHERE type = 'REAL'), (SELECT id FROM users WHERE username = 'hr1'), 85, 88, 82, 88, '表现优秀，技术扎实，沟通能力强');

INSERT INTO learning_path_resources (path_id, resource_id, sort_order) VALUES
((SELECT id FROM learning_paths WHERE title = 'Java后端开发工程师成长路径'), 1, 1),
((SELECT id FROM learning_paths WHERE title = 'Java后端开发工程师成长路径'), 2, 2);

COMMIT;