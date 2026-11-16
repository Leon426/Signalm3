# Signalm3 现代化 Material 3 (Beta/Expressive) 重构架构方案 (SIG-TASK-001)

- **负责人**：architect  
- **基线输入**：`team/REQUIREMENTS.md`、Signal-Android (v8.34.2) 现存代码库  
- **核心原则**：“后端与核心逻辑保持完全对齐，前端 UI 全面现代化重塑”

---

## 1. 现状剖析与重构边界

### 1.1 现状分析
Signal-Android 拥有十年演进的工业级安全代码库，其典型特征是：
1. **重型业务基础设施**：
   - 加密核心：`libsignal-client`（Rust JNI 桥接），严格的双棘轮协议与端到端加密状态机。
   - 数据存储：基于 SQLCipher 的原生加密 SQLite 数据库，使用专门的 `Cursor` / `Reader` / `SignalDatabase` 体系。
   - 传统 UI 遗留：大部分核心页面采用传统 XML 布局（`RecyclerView`, `ConversationListItem.java`, `ConversationFragment.java`）与多层 Fragment 嵌套，部分页面刚开始尝试 Compose。
2. **数据流机制**：
   - 现存 ViewModel 普遍暴露 `LiveData`、`Observable`（RxJava3）或通过后台线程轮询发布事件。

### 1.2 明确“保持对齐”的严苛边界（不可触碰红线）
为了绝对保证“后端保持对齐”、通讯保密性与 Signal 协议兼容性，以下核心底层模块**严禁改动任何业务逻辑**：
- 🚫 `lib/` 目录下的所有加解密库与协议库（`libsignal-service` 等）。
- 🚫 `org.thoughtcrime.securesms.database.*`（SQLCipher 数据库表结构、迁移脚本与底层读写）。
- 🚫 `org.thoughtcrime.securesms.net.*`（WebSocket 信令、REST API 通信、推送处理）。
- 🚫 `org.thoughtcrime.securesms.crypto.*`（本地密钥存储与安全原语）。

---

## 2. Compose M3 渐进式接入架构设计

### 2.1 架构分层拓扑

```
┌────────────────────────────────────────────────────────┐
│   Signalm3 Compose M3 现代化展现层 (全新构建)            │
│  - M3 Expressive 主题 (动态取色 + 种子保底)             │
│  - 自适应主框架 (NavigationSuiteScaffold)              │
│  - 会话列表 (ConversationListScreen)                   │
│  - 聊天详情流 (ChatThreadScreen + BerryBubble 流)      │
│  - 现代沉浸输入栏 (Expressive Toolbar + 动态麦克风形变)   │
└───────────────────────────▲────────────────────────────┘
                            │ (State / Actions UDF 契约)
┌───────────────────────────┴────────────────────────────┐
│   UI 状态适配与桥接层 (Bridge Layer)                    │
│  - LiveData / RxJava3 / Flow -> Compose StateAdapter   │
│  - 现有 ViewModel 门面封装 (ConversationListViewModel)   │
└───────────────────────────▲────────────────────────────┘
                            │ (保持不变)
┌───────────────────────────┴────────────────────────────┐
│   Signal 工业级后端基础设施 (100% 对齐保留)             │
│  - SignalDatabase (SQLCipher 加密库)                   │
│  - MessageSender / MessageReceiver 消息状态管道        │
│  - libsignal 双棘轮加密与身份验证                      │
│  - WebSocket 实时推送与网络通道                        │
└────────────────────────────────────────────────────────┘
```

### 2.2 视图接入策略（ComposeView 渐进式无缝挂载）
考虑到 Signal 原有 Activity 承载了复杂的深链跳转、通知唤起、权限申请与前后台保活：
1. **宿主保持**：保留原有的 Activity 入口（如 `MainActivity` 或各功能 Activity）。
2. **Fragment 容器挂载**：在 `ConversationListFragment` 和 `ConversationFragment` 中，将原有的 `onCreateView` 根视图替换为 `ComposeView`：
   ```kotlin
   override fun onCreateView(
       inflater: LayoutInflater,
       container: ViewGroup?,
       savedInstanceState: Bundle?
   ): View {
       return ComposeView(requireContext()).apply {
           setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
           setContent {
               SignalM3Theme {
                   ConversationListScreen(
                       viewModel = viewModel,
                       onConversationClick = { threadId -> navigateToConversation(threadId) }
                   )
               }
           }
       }
   }
   ```
这种“外壳稳定、内胆全面现代化”的架构，使得原有路由、生命周期拦截器、电话权限监听等无需重写，风险降至最低。

### 2.3 数据流桥接协议（LiveData / RxJava -> UDF State）
现存 ViewModel 暴露的非 Compose 类型，在 Compose 顶层通过统一适配器消费：
- `LiveData<T>.observeAsState()`
- `Observable<T>.subscribeAsState()`（利用 `androidx.compose.runtime:runtime-rxjava3`）
- 封装 `ConversationListUiState` 与 `ChatThreadUiState` 作为强类型数据契约，杜绝在 Composable 内部进行复杂的数据库或游标操作。

---

## 3. 技术选型与 Material 3 最新组件库规范

1. **Material 3 Beta / Expressive 体系**：
   - 依赖：`androidx.compose.material3:material3` 最新版本，全面开启 Material 3 Expressive 特性。
   - 形状形变：引入 `androidx.graphics:graphics-shapes`，实现几何变形头像与动效。
   - 自适应外壳：引入 `androidx.compose.material3.adaptive:adaptive`，在大屏与折叠屏上支持双栏列表-详情联动。
2. **隔离封装规范**：
   - 建立 `org.thoughtcrime.securesms.designsystem`，所有带有 `@OptIn(ExperimentalMaterial3ExpressiveApi::class)` 的组件必须封装为内部私有实现，对外暴露标准稳定参数的 `Signal*` 系列组件。

---

## 4. 实施阶段与下游输入

- **P0（架构与规范定稿）**：输出本文档与 `signal_m3_ui_spec.md`。
- **P1（设计系统组件库落地）**：封装 Token、Theme、气泡、输入栏与变形头像。
- **P2（核心会话列表与聊天页重构）**：对接现有 ViewModel 渲染全新 Compose M3 界面。
- **P3（联系人与设置中心重构）**：完成核心链路全闭环。
- **P4（测试与审查验证）**：无障碍、性能及加密通道安全验收。
