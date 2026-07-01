# Tasks
- [ ] Task 1: 识别提交范围
  - [ ] SubTask 1.1: 执行 `git log --all --since="24 hours ago"` 获取过去 24 小时内所有提交
  - [ ] SubTask 1.2: 若提交为空，直接输出无缺陷结论并结束；否则继续后续任务
- [ ] Task 2: 获取并初步审阅 diff
  - [ ] SubTask 2.1: 对每个目标提交执行 `git show <hash>` 获取完整变更
  - [ ] SubTask 2.2: 标记涉及数据库、认证、并发、资源分配、异常处理等高风险变更
- [ ] Task 3: 追踪完整执行路径
  - [ ] SubTask 3.1: 对高风险变更定位上游调用方（控制器、定时任务、消息监听等）
  - [ ] SubTask 3.2: 分析下游影响（数据库持久化、返回值、全局状态、外部资源）
  - [ ] SubTask 3.3: 构造具体触发场景并验证其可信度
- [ ] Task 4: 修复与测试（仅在确认 Bug 时执行）
  - [ ] SubTask 4.1: 实施最小化修复，避免大范围重构
  - [ ] SubTask 4.2: 添加或更新单元/集成测试覆盖缺陷行为
  - [ ] SubTask 4.3: 运行相关测试确保修复有效且未引入回归
- [ ] Task 5: 输出最终报告
  - [ ] SubTask 5.1: 汇总发现的问题、修复位置、测试覆盖情况
  - [ ] SubTask 5.2: 若无缺陷，输出“过去 24 小时的提交中未发现关键缺陷。”

# Task Dependencies
- Task 2 依赖 Task 1
- Task 3 依赖 Task 2
- Task 4 依赖 Task 3（仅在确认 Bug 时执行）
- Task 5 依赖 Task 3 或 Task 4
