# Signalm3 任务追踪看板 (TASKS.md)

| 任务编号 | 任务名称 | 负责人 | 状态 | 依赖 | 交付物 / 说明 |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **SIG-TASK-001** | Signalm3 Compose M3 界面重构与底层对齐架构方案 | architect | **已完成** | 无 | `team/docs/signal_m3_architecture_plan.md`（验收通过） |
| **SIG-TASK-002** | Material 3 最新组件库全套 UI/UX 视觉与交互规范设计 | ui-designer | **已完成** | SIG-TASK-001 | `team/docs/signal_m3_ui_spec.md`（验收通过） |
| **SIG-TASK-003** | Material 3 Expressive 设计系统组件库实现 | android-dev | **已完成** | SIG-TASK-002 | `designsystem` 包下全套 Token、Theme、Shape、Motion 及核心组件 |
| **SIG-TASK-004** | 会话列表与主框架 Compose M3 重构落地 | android-dev | **已完成** | SIG-TASK-003 | `ConversationListM3Screen.kt` 会话列表、多边形头像、未读过滤与搜索 |
| **SIG-TASK-005** | 聊天详情流与输入工具栏 Compose M3 重构落地 | android-dev | **已完成** | SIG-TASK-003 | `ChatThreadM3Screen.kt` 不对称气泡、悬浮动态输入栏、阅后即焚状态 |
| **SIG-TASK-006** | 联系人与设置页面 Compose M3 重构落地 | android-dev | **已完成** | SIG-TASK-003 | `ContactsM3Screen.kt`（吸顶分组）与 `SettingsM3Screen.kt`（大圆角卡片） |
| **SIG-TASK-007** | 自动化测试用例与无障碍验证 | qa-tester | **已完成** | SIG-TASK-004, 005, 006 | 详见 `team/docs/signal_m3_qa_report.md` 及 2 套自动化单测 |
| **SIG-TASK-008** | 架构隔离、内存泄漏与端到端加密通道安全审查 | code-reviewer | **已完成** | SIG-TASK-004, 005, 006 | 详见 `team/docs/signal_m3_code_review_report.md`（复审通过） |
