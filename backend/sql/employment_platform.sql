/*
 Navicat Premium Data Transfer

 Source Server         : SpringdemoTest
 Source Server Type    : MySQL
 Source Server Version : 80017
 Source Host           : localhost:3306
 Source Schema         : employment_platform

 Target Server Type    : MySQL
 Target Server Version : 80017
 File Encoding         : 65001

 Date: 24/06/2026 11:48:10
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for applications
-- ----------------------------
DROP TABLE IF EXISTS `applications`;
CREATE TABLE `applications`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `job_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `resume_id` bigint(20) NOT NULL,
  `status` enum('PENDING','APPROVED','REJECTED','OFFER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PENDING',
  `apply_time` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `feedback` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `job_id`(`job_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `resume_id`(`resume_id`) USING BTREE,
  CONSTRAINT `applications_ibfk_1` FOREIGN KEY (`job_id`) REFERENCES `jobs` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `applications_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `applications_ibfk_3` FOREIGN KEY (`resume_id`) REFERENCES `resumes` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of applications
-- ----------------------------
INSERT INTO `applications` VALUES (1, 1, 1, 1, 'PENDING', '2026-06-16 10:23:16', NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for assessment_questions
-- ----------------------------
DROP TABLE IF EXISTS `assessment_questions`;
CREATE TABLE `assessment_questions`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `assessment_id` bigint(20) NOT NULL,
  `question_type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `options` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `weight` decimal(3, 2) NULL DEFAULT 1.00,
  `sort_order` int(11) NULL DEFAULT 0,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `assessment_id`(`assessment_id`) USING BTREE,
  CONSTRAINT `assessment_questions_ibfk_1` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 51 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of assessment_questions
-- ----------------------------
INSERT INTO `assessment_questions` VALUES (1, 1, 'SINGLE', '您更喜欢哪种工作环境？', '[\"安静独立的办公室\", \"热闹的团队环境\", \"户外工作场所\", \"创意工作室\"]', 1.00, 0, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (2, 1, 'SINGLE', '您更倾向于从事哪种类型的工作？', '[\"数据分析\", \"创意设计\", \"人际交往\", \"技术研发\"]', 1.00, 1, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (3, 1, 'SINGLE', '您对哪类活动最感兴趣？', '[\"阅读写作\", \"运动健身\", \"音乐艺术\", \"科学实验\"]', 1.00, 2, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (4, 1, 'SINGLE', '您更享受哪种工作方式？', '[\"独立完成任务\", \"团队协作\", \"领导他人\", \"跟随指导\"]', 1.00, 3, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (5, 1, 'SCALE', '我喜欢学习新知识和技能', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 4, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (6, 1, 'SCALE', '我对自己的职业方向有清晰的规划', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 5, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (7, 1, 'SINGLE', '您认为工作中最重要的是？', '[\"成就感\", \"人际关系\", \"薪资待遇\", \"个人成长\"]', 1.00, 6, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (8, 1, 'SCALE', '我经常主动了解行业动态', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 7, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (9, 1, 'SINGLE', '您更愿意在哪个行业发展？', '[\"IT/互联网\", \"金融\", \"教育\", \"医疗\"]', 1.00, 8, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (10, 1, 'SCALE', '我愿意为职业发展投入额外时间', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 9, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (11, 2, 'SCALE', '我是一个善于社交的人', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 0, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (12, 2, 'SCALE', '我做事比较谨慎', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 1, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (13, 2, 'SCALE', '我容易感到焦虑或紧张', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 2, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (14, 2, 'SCALE', '我喜欢尝试新事物', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 3, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (15, 2, 'SCALE', '我在团队中通常扮演领导角色', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 4, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (16, 2, 'SCALE', '我能够很好地控制自己的情绪', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 5, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (17, 2, 'SCALE', '我更喜欢按计划行事而非随机应变', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 6, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (18, 2, 'SCALE', '我善于倾听他人的意见', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 7, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (19, 2, 'SCALE', '面对压力我仍能保持冷静', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 8, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (20, 2, 'SCALE', '我乐于帮助他人解决问题', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 9, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (21, 3, 'SCALE', '我的逻辑思维能力很强', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 0, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (22, 3, 'SCALE', '我能够清晰表达自己的想法', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 1, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (23, 3, 'SCALE', '我善于分析和解决复杂问题', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 2, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (24, 3, 'SCALE', '我的时间管理能力很好', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 3, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (25, 3, 'SCALE', '我有较强的学习能力', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 4, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (26, 3, 'SCALE', '我能够高效完成多任务处理', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 5, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (27, 3, 'SCALE', '我的创新能力较强', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 6, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (28, 3, 'SCALE', '我善于与他人合作', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 7, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (29, 3, 'SCALE', '我的计算机操作能力很强', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 8, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (30, 3, 'SCALE', '我能够快速适应新环境', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 9, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (31, 4, 'SINGLE', '您选择工作时最看重的是？', '[\"薪资待遇\", \"发展空间\", \"工作氛围\", \"稳定性\"]', 1.00, 0, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (32, 4, 'SCALE', '工作与生活平衡对我很重要', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 1, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (33, 4, 'SCALE', '我认为工作应该有意义和价值', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 2, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (34, 4, 'SINGLE', '您更认可以下哪种说法？', '[\"工作是为了更好的生活\", \"生活是为了更好的工作\", \"工作与生活同等重要\", \"只要能赚钱就行\"]', 1.00, 3, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (35, 4, 'SCALE', '我重视团队合作和集体荣誉', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 4, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (36, 4, 'SCALE', '创新和挑战对我来说很重要', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 5, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (37, 4, 'SINGLE', '您对加班的态度是？', '[\"坚决不接受\", \"偶尔可以接受\", \"可以接受合理加班\", \"为了事业可以接受\"]', 1.00, 6, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (38, 4, 'SCALE', '我认为企业社会责任很重要', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 7, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (39, 4, 'SCALE', '我希望能帮助他人通过工作', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 8, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (40, 4, 'SINGLE', '您理想的公司规模是？', '[\"初创公司\", \"中小型企业\", \"大型企业\", \"无所谓\"]', 1.00, 9, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (41, 5, 'SINGLE', '您希望在工作中获得什么？', '[\"专业成长\", \"职位晋升\", \"工作自由\", \"稳定收入\"]', 1.00, 0, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (42, 5, 'SCALE', '我渴望成为某个领域的专家', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 1, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (43, 5, 'SCALE', '我希望未来能管理一个团队', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 2, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (44, 5, 'SINGLE', '您更倾向于哪种职业发展路径？', '[\"技术专家路线\", \"管理路线\", \"创业路线\", \"自由职业\"]', 1.00, 3, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (45, 5, 'SCALE', '我追求工作与生活的平衡', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 4, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (46, 5, 'SCALE', '我渴望自主创业', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 5, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (47, 5, 'SINGLE', '您认为5年后的自己应该？', '[\"成为技术专家\", \"担任管理岗位\", \"拥有自己的事业\", \"稳定工作即可\"]', 1.00, 6, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (48, 5, 'SCALE', '我重视工作带来的社会地位', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 7, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (49, 5, 'SCALE', '我希望工作能提供持续学习机会', '[\"非常不符合\", \"不太符合\", \"一般\", \"比较符合\", \"非常符合\"]', 1.00, 8, '2026-06-16 10:23:16');
INSERT INTO `assessment_questions` VALUES (50, 5, 'SINGLE', '您如何看待工作中的挑战？', '[\"主动寻求挑战\", \"接受合理挑战\", \"尽量避免挑战\", \"无所谓\"]', 1.00, 9, '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for assessment_records
-- ----------------------------
DROP TABLE IF EXISTS `assessment_records`;
CREATE TABLE `assessment_records`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `assessment_id` bigint(20) NOT NULL,
  `answers` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `score` decimal(5, 2) NULL DEFAULT NULL,
  `status` enum('IN_PROGRESS','COMPLETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'IN_PROGRESS',
  `completed_at` datetime(0) NULL DEFAULT NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `assessment_id`(`assessment_id`) USING BTREE,
  CONSTRAINT `assessment_records_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `assessment_records_ibfk_2` FOREIGN KEY (`assessment_id`) REFERENCES `assessments` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assessment_reports
-- ----------------------------
DROP TABLE IF EXISTS `assessment_reports`;
CREATE TABLE `assessment_reports`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `record_id` bigint(20) NOT NULL,
  `student_id` bigint(20) NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `recommendations` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `generated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `record_id`(`record_id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  CONSTRAINT `assessment_reports_ibfk_1` FOREIGN KEY (`record_id`) REFERENCES `assessment_records` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `assessment_reports_ibfk_2` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for assessments
-- ----------------------------
DROP TABLE IF EXISTS `assessments`;
CREATE TABLE `assessments`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `dimension` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `duration` int(11) NULL DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of assessments
-- ----------------------------
INSERT INTO `assessments` VALUES (1, '职业兴趣测评', '了解您的职业兴趣倾向', 'INTEREST', 15, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `assessments` VALUES (2, '性格特质测评', '分析您的性格特点', 'PERSONALITY', 20, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `assessments` VALUES (3, '能力倾向测评', '评估您的能力优势', 'ABILITY', 25, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `assessments` VALUES (4, '价值观测评', '探索您的职业价值观', 'VALUES', 10, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `assessments` VALUES (5, '职业锚点测评', '确定您的职业定位', 'CAREER_ANCHOR', 15, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for career_milestones
-- ----------------------------
DROP TABLE IF EXISTS `career_milestones`;
CREATE TABLE `career_milestones`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `plan_id` bigint(20) NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `target_date` date NULL DEFAULT NULL,
  `progress` decimal(5, 2) NULL DEFAULT 0.00,
  `completed` tinyint(1) NULL DEFAULT 0,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `plan_id`(`plan_id`) USING BTREE,
  CONSTRAINT `career_milestones_ibfk_1` FOREIGN KEY (`plan_id`) REFERENCES `career_plans` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of career_milestones
-- ----------------------------
INSERT INTO `career_milestones` VALUES (1, 1, '完成毕业论文', '按时完成本科毕业论文', '2026-06-30', 0.00, 0, '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `career_milestones` VALUES (2, 1, '拿到心仪offer', '拿到互联网大厂offer', '2026-12-31', 0.00, 0, '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `career_milestones` VALUES (3, 1, '晋升高级工程师', '工作3年内晋升为高级工程师', '2029-12-31', 0.00, 0, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for career_plans
-- ----------------------------
DROP TABLE IF EXISTS `career_plans`;
CREATE TABLE `career_plans`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `goal_short` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `goal_medium` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `goal_long` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `current_stage` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `progress` decimal(5, 2) NULL DEFAULT 0.00,
  `status` enum('ACTIVE','COMPLETED','ARCHIVED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  CONSTRAINT `career_plans_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of career_plans
-- ----------------------------
INSERT INTO `career_plans` VALUES (1, 1, '我的职业规划', '毕业后进入互联网公司担任Java开发工程师', '3年内成长为高级开发工程师', '5年内成为技术主管', NULL, 0.00, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for company_profiles
-- ----------------------------
DROP TABLE IF EXISTS `company_profiles`;
CREATE TABLE `company_profiles`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `company_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `scale` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `logo_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `contact_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `contact_phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `company_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of company_profiles
-- ----------------------------
INSERT INTO `company_profiles` VALUES (1, 3, '腾讯科技', '互联网', '10000+', '深圳', '腾讯是一家知名互联网公司', NULL, NULL, NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for interview_evaluations
-- ----------------------------
DROP TABLE IF EXISTS `interview_evaluations`;
CREATE TABLE `interview_evaluations`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `interview_id` bigint(20) NOT NULL,
  `evaluator_id` bigint(20) NOT NULL,
  `score` int(11) NULL DEFAULT NULL,
  `communication_score` int(11) NULL DEFAULT NULL,
  `technical_score` int(11) NULL DEFAULT NULL,
  `attitude_score` int(11) NULL DEFAULT NULL,
  `comments` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `interview_id`(`interview_id`) USING BTREE,
  INDEX `evaluator_id`(`evaluator_id`) USING BTREE,
  CONSTRAINT `interview_evaluations_ibfk_1` FOREIGN KEY (`interview_id`) REFERENCES `interviews` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `interview_evaluations_ibfk_2` FOREIGN KEY (`evaluator_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interview_evaluations
-- ----------------------------
INSERT INTO `interview_evaluations` VALUES (1, 1, 3, 85, 88, 82, 88, '表现优秀，技术扎实，沟通能力强', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for interview_questions
-- ----------------------------
DROP TABLE IF EXISTS `interview_questions`;
CREATE TABLE `interview_questions`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `answer_hint` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `difficulty` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interview_questions
-- ----------------------------
INSERT INTO `interview_questions` VALUES (1, '自我介绍', '请做一个简单的自我介绍', '介绍基本信息、专业背景、核心优势', 'BEGINNER', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `interview_questions` VALUES (2, '职业规划', '谈谈你的职业规划', '短期目标、长期目标、如何实现', 'INTERMEDIATE', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `interview_questions` VALUES (3, '项目经验', '介绍一个你参与过的项目', 'STAR法则：情境、任务、行动、结果', 'INTERMEDIATE', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `interview_questions` VALUES (4, '技术问题', '什么是RESTful API？', 'REST的基本概念和设计原则', 'ADVANCED', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `interview_questions` VALUES (5, '抗压能力', '你如何处理工作压力？', '举例说明处理压力的方法', 'INTERMEDIATE', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for interviews
-- ----------------------------
DROP TABLE IF EXISTS `interviews`;
CREATE TABLE `interviews`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NULL DEFAULT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `scheduled_time` datetime(0) NULL DEFAULT NULL,
  `status` enum('SCHEDULED','IN_PROGRESS','COMPLETED','CANCELLED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'SCHEDULED',
  `meeting_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `recording_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `application_id`(`application_id`) USING BTREE,
  CONSTRAINT `interviews_ibfk_1` FOREIGN KEY (`application_id`) REFERENCES `applications` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of interviews
-- ----------------------------
INSERT INTO `interviews` VALUES (1, 1, 'REAL', '2026-07-01 10:00:00', 'SCHEDULED', NULL, NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for jobs
-- ----------------------------
DROP TABLE IF EXISTS `jobs`;
CREATE TABLE `jobs`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `location` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `min_salary` decimal(10, 2) NULL DEFAULT NULL,
  `max_salary` decimal(10, 2) NULL DEFAULT NULL,
  `education` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `experience` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `requirements` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `responsibilities` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `benefits` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `deadline` date NULL DEFAULT NULL,
  `status` enum('PUBLISHED','PAUSED','DELETED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'PUBLISHED',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `company_id`(`company_id`) USING BTREE,
  CONSTRAINT `jobs_ibfk_1` FOREIGN KEY (`company_id`) REFERENCES `company_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of jobs
-- ----------------------------
INSERT INTO `jobs` VALUES (1, 1, 'Java开发工程师', 'FULL_TIME', '互联网', '深圳', 15000.00, 25000.00, 'BACHELOR', '1-3年', '本科及以上学历，Java基础扎实', '负责后端系统开发', '五险一金，年终奖', '2026-12-31', 'PUBLISHED', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for learning_path_resources
-- ----------------------------
DROP TABLE IF EXISTS `learning_path_resources`;
CREATE TABLE `learning_path_resources`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `path_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `sort_order` int(11) NULL DEFAULT 0,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `path_id`(`path_id`) USING BTREE,
  INDEX `resource_id`(`resource_id`) USING BTREE,
  CONSTRAINT `learning_path_resources_ibfk_1` FOREIGN KEY (`path_id`) REFERENCES `learning_paths` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `learning_path_resources_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `learning_resources` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 3 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of learning_path_resources
-- ----------------------------
INSERT INTO `learning_path_resources` VALUES (1, 1, 1, 1, '2026-06-16 10:23:16');
INSERT INTO `learning_path_resources` VALUES (2, 1, 2, 2, '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for learning_paths
-- ----------------------------
DROP TABLE IF EXISTS `learning_paths`;
CREATE TABLE `learning_paths`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `career_goal` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `estimated_duration` int(11) NULL DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of learning_paths
-- ----------------------------
INSERT INTO `learning_paths` VALUES (1, 'Java后端开发工程师成长路径', '从零基础到Java工程师的完整学习路径', 'JAVA_ENGINEER', 200, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_paths` VALUES (2, '产品经理入门到精通', '系统学习产品经理必备技能', 'PRODUCT_MANAGER', 150, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_paths` VALUES (3, '数据分析专家之路', '掌握数据分析核心技能', 'DATA_ANALYST', 180, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for learning_records
-- ----------------------------
DROP TABLE IF EXISTS `learning_records`;
CREATE TABLE `learning_records`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `resource_id` bigint(20) NOT NULL,
  `progress` decimal(5, 2) NULL DEFAULT 0.00,
  `completed` tinyint(1) NULL DEFAULT 0,
  `last_watch_time` datetime(0) NULL DEFAULT NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `resource_id`(`resource_id`) USING BTREE,
  CONSTRAINT `learning_records_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT,
  CONSTRAINT `learning_records_ibfk_2` FOREIGN KEY (`resource_id`) REFERENCES `learning_resources` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of learning_records
-- ----------------------------
INSERT INTO `learning_records` VALUES (1, 1, 1, 50.00, 0, NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_records` VALUES (2, 1, 3, 100.00, 1, NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for learning_resources
-- ----------------------------
DROP TABLE IF EXISTS `learning_resources`;
CREATE TABLE `learning_resources`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `title` varchar(200) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `type` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `category` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `difficulty` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `duration` int(11) NULL DEFAULT NULL,
  `url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `thumbnail_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `description` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `view_count` int(11) NULL DEFAULT 0,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of learning_resources
-- ----------------------------
INSERT INTO `learning_resources` VALUES (1, 'Java基础入门', 'VIDEO', '编程开发', 'BEGINNER', 120, 'https://example.com/java-basic', NULL, 'Java编程语言基础课程', 0, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_resources` VALUES (2, 'Spring Boot实战', 'VIDEO', '编程开发', 'INTERMEDIATE', 180, 'https://example.com/spring-boot', NULL, 'Spring Boot框架实战教程', 0, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_resources` VALUES (3, '简历写作技巧', 'EBOOK', '职业发展', 'BEGINNER', 60, 'https://example.com/resume-guide', NULL, '专业简历写作指南', 0, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_resources` VALUES (4, '面试技巧大全', 'EBOOK', '职业发展', 'INTERMEDIATE', 90, 'https://example.com/interview-guide', NULL, '面试准备与技巧', 0, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `learning_resources` VALUES (5, '产品经理案例分析', 'CASE', '产品运营', 'ADVANCED', 45, 'https://example.com/product-case', NULL, '知名产品案例深度分析', 0, 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for resume_templates
-- ----------------------------
DROP TABLE IF EXISTS `resume_templates`;
CREATE TABLE `resume_templates`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `industry` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `preview_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 11 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resume_templates
-- ----------------------------
INSERT INTO `resume_templates` VALUES (1, 'IT技术岗模板', 'IT', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"专业技能\", \"项目经验\", \"实习经历\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (2, '金融行业模板', '金融', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"证书资质\", \"工作经历\", \"项目经验\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (3, '市场营销模板', '市场营销', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"工作经历\", \"营销案例\", \"技能证书\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (4, '人力资源模板', '人力资源', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"工作经历\", \"HR项目\", \"专业技能\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (5, '制造业模板', '制造业', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"工作经历\", \"项目经验\", \"技能特长\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (6, '教育行业模板', '教育', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"教学经历\", \"获奖情况\", \"资格证书\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (7, '医疗健康模板', '医疗', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"执业资格\", \"工作经历\", \"专业技能\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (8, '物流运输模板', '物流', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"工作经历\", \"项目经验\", \"职业证书\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (9, '房地产模板', '房地产', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"工作经历\", \"业绩成果\", \"专业技能\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `resume_templates` VALUES (10, '文化传媒模板', '文化传媒', NULL, '{\"sections\": [\"个人信息\", \"教育背景\", \"工作经历\", \"作品集\", \"专业技能\"]}', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for resumes
-- ----------------------------
DROP TABLE IF EXISTS `resumes`;
CREATE TABLE `resumes`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `student_id` bigint(20) NOT NULL,
  `template_id` bigint(20) NULL DEFAULT NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `content` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `file_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `is_default` tinyint(1) NULL DEFAULT 0,
  `format_score` int(11) NULL DEFAULT NULL,
  `content_score` int(11) NULL DEFAULT NULL,
  `match_score` int(11) NULL DEFAULT NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `student_id`(`student_id`) USING BTREE,
  INDEX `fk_resume_template`(`template_id`) USING BTREE,
  CONSTRAINT `fk_resume_template` FOREIGN KEY (`template_id`) REFERENCES `resume_templates` (`id`) ON DELETE RESTRICT ON UPDATE RESTRICT,
  CONSTRAINT `resumes_ibfk_1` FOREIGN KEY (`student_id`) REFERENCES `student_profiles` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of resumes
-- ----------------------------
INSERT INTO `resumes` VALUES (1, 1, 1, '个人简历', '{\"personalInfo\": {\"name\": \"张三\", \"phone\": \"13800138001\", \"email\": \"student1@example.com\"}, \"education\": [{\"school\": \"北京大学\", \"major\": \"计算机科学与技术\", \"degree\": \"本科\"}]}', NULL, 0, NULL, NULL, NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for student_profiles
-- ----------------------------
DROP TABLE IF EXISTS `student_profiles`;
CREATE TABLE `student_profiles`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) NOT NULL,
  `school` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `major` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `grade` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `graduation_date` date NULL DEFAULT NULL,
  `education` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `skills` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `resume_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id`) USING BTREE,
  CONSTRAINT `student_profiles_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE ON UPDATE RESTRICT
) ENGINE = InnoDB AUTO_INCREMENT = 2 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of student_profiles
-- ----------------------------
INSERT INTO `student_profiles` VALUES (1, 2, '北京大学', '计算机科学与技术', '大四', NULL, 'BACHELOR', '[\"Java\", \"Python\", \"Spring Boot\"]', NULL, '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Table structure for users
-- ----------------------------
DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`  (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `real_name` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL DEFAULT NULL,
  `role` enum('STUDENT','HR','ADMIN') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `status` enum('ACTIVE','INACTIVE') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0),
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username`) USING BTREE,
  UNIQUE INDEX `email`(`email`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of users
-- ----------------------------
INSERT INTO `users` VALUES (1, 'admin', '$2a$10$9mKobCPxSsNSQHw30CYOYeOd34gQp04sOtBHurKdkax5z1jgIFupe', 'admin@example.com', '13800138000', '管理员', NULL, 'ADMIN', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');
INSERT INTO `users` VALUES (2, 'student1', '$2a$10$9mKobCPxSsNSQHw30CYOYeOd34gQp04sOtBHurKdkax5z1jgIFupe', 'student1@example.com', '13800138001', '张三', NULL, 'STUDENT', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-17 18:30:28');
INSERT INTO `users` VALUES (3, 'hr1', '$2a$10$9mKobCPxSsNSQHw30CYOYeOd34gQp04sOtBHurKdkax5z1jgIFupe', 'hr1@example.com', '13800138002', '李四', NULL, 'HR', 'ACTIVE', '2026-06-16 10:23:16', '2026-06-16 10:23:16');

-- ----------------------------
-- Alter table: company_profiles - 添加审核字段
-- ----------------------------
ALTER TABLE `company_profiles`
  ADD COLUMN `verified` tinyint(1) DEFAULT 0 AFTER `contact_phone`,
  ADD COLUMN `verified_at` datetime NULL AFTER `verified`,
  ADD COLUMN `verified_by` bigint(20) NULL AFTER `verified_at`,
  ADD CONSTRAINT `fk_company_verified_by` FOREIGN KEY (`verified_by`) REFERENCES `users` (`id`) ON DELETE SET NULL;

-- 移除 user_id 的 UNIQUE 约束以支持多HR协作
ALTER TABLE `company_profiles` DROP INDEX `user_id`;

-- ----------------------------
-- Alter table: student_profiles - 添加新字段
-- ----------------------------
ALTER TABLE `student_profiles`
  ADD COLUMN `bio` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `skills`,
  ADD COLUMN `avatar_url` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `bio`,
  ADD COLUMN `target_job` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `avatar_url`,
  ADD COLUMN `expected_salary` decimal(10,2) NULL AFTER `target_job`,
  ADD COLUMN `job_status` varchar(20) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL AFTER `expected_salary`;

-- ----------------------------
-- Table structure for company_hr_members
-- ----------------------------
DROP TABLE IF EXISTS `company_hr_members`;
CREATE TABLE `company_hr_members` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `company_id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role` enum('OWNER','ADMIN','MEMBER') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL DEFAULT 'MEMBER',
  `invited_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `accepted` tinyint(1) DEFAULT 0,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uq_company_user` (`company_id`, `user_id`),
  INDEX `idx_user_id` (`user_id`),
  CONSTRAINT `fk_chm_company` FOREIGN KEY (`company_id`) REFERENCES `company_profiles` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_chm_user` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- 迁移现有 company_profiles.user_id 到 company_hr_members
INSERT INTO `company_hr_members` (`company_id`, `user_id`, `role`, `accepted`)
SELECT `id`, `user_id`, 'OWNER', 1 FROM `company_profiles` WHERE `user_id` IS NOT NULL;

-- ----------------------------
-- Table structure for audit_logs
-- ----------------------------
DROP TABLE IF EXISTS `audit_logs`;
CREATE TABLE `audit_logs` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `actor_user_id` bigint(20) NULL,
  `actor_role` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `action` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NOT NULL,
  `target_type` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `target_id` bigint(20) NULL,
  `details` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `ip` varchar(45) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `user_agent` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_actor_user_id` (`actor_user_id`),
  CONSTRAINT `fk_audit_user` FOREIGN KEY (`actor_user_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for job_offers
-- ----------------------------
DROP TABLE IF EXISTS `job_offers`;
CREATE TABLE `job_offers` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `application_id` bigint(20) NOT NULL,
  `hr_id` bigint(20) NOT NULL,
  `salary` decimal(10,2) NULL,
  `title` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `status` enum('SENT','ACCEPTED','REJECTED','REVOKED') CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci DEFAULT 'SENT',
  `sent_at` datetime NULL,
  `responded_at` datetime NULL,
  `notes` text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci NULL,
  `created_at` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE,
  INDEX `idx_application_id` (`application_id`),
  INDEX `idx_hr_id` (`hr_id`),
  CONSTRAINT `fk_offer_app` FOREIGN KEY (`application_id`) REFERENCES `applications` (`id`) ON DELETE CASCADE,
  CONSTRAINT `fk_offer_hr` FOREIGN KEY (`hr_id`) REFERENCES `users` (`id`) ON DELETE SET NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_unicode_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
