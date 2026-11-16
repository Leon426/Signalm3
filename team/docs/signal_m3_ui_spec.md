# Signalm3 现代化全面 UI 重设计规范（Material 3 Beta / Expressive）

- **负责人**：ui-designer  
- **设计基准**：最新 Material 3 (Beta / Expressive 组件库) + Jetpack Compose  
- **适配系统**：Android 16+ (API 36)，全面支持 Material You 动态取色与多端自适应  
- **设计核心**：高表现力、流动感、安全感与极致现代美学的融合

---

## 1. 设计愿景与整体风格 (Design Philosophy)

传统即时通讯应用常给人刻板、严肃与冷冰冰的机械感。Signalm3 的重设计核心是**“有表现力的安全（Expressive Security）”**：
- **消解边框，由色调表达层级**：全面废弃粗线条描边与阴影卡片，使用 Material 3 的 `surfaceContainer`、`surfaceContainerHigh` 进行柔和自然的层次划分。
- **大圆角与有机多边形形变**：引入 `androidx.graphics.shapes`，将头像、气泡与操作按键注入生物般的流动变形活力。
- **动态呼吸与自然物理动效**：界面所有操作均遵循自然弹簧阻尼（Spring Physics），杜绝生硬线性平移。

---

## 2. 颜色体系与设计 Token (Color Tokens)

### 2.1 决策链路
1. **优先系统动态取色 (Dynamic Color)**：Android 12+ 上无条件跟随用户系统壁纸色调，产生千人千面的个性化感受。
2. **运行时对比度安全网 (ContrastValidator)**：内置动态色校验，若当前壁纸取色在极端对比度下文本不达标（< 4.5:1），自动整套回退至静态保底方案。
3. **静态保底种子色 (Seed Color)**：选用 Signal 经典深邃蓝改良的 **`#1A65FF`（Electric Cobalt Blue）**，生成 6 套完整调色板。

### 2.2 静态 6 套调色板 (Seed #1A65FF)

| 角色 (Token) | Light·标准 | Light·中对比 | Light·高对比 | Dark·标准 | Dark·中对比 | Dark·高对比 |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `primary` | `#0053DB` | `#00379C` | `#002674` | `#B1C5FF` | `#C8D7FF` | `#E2E9FF` |
| `onPrimary` | `#FFFFFF` | `#FFFFFF` | `#FFFFFF` | `#002C78` | `#001F58` | `#000000` |
| `primaryContainer` | `#DBE1FF` | `#0048C3` | `#003392` | `#003EA7` | `#4B79FF` | `#86A7FF` |
| `onPrimaryContainer` | `#003EA7` | `#FFFFFF` | `#FFFFFF` | `#DBE1FF` | `#000000` | `#000B2B` |
| `secondaryContainer` | `#DAE2F9` | `#4F6799` | `#324B7B` | `#3B4665` | `#8696C7` | `#BAC6FB` |
| `surface` | `#FAF8FF` | `#FAF8FF` | `#FAF8FF` | `#111318` | `#111318` | `#111318` |
| `surfaceContainerLow` | `#F4F3FA` | `#F4F3FA` | `#F1F0F7` | `#191C20` | `#1B1E22` | `#1E2025` |
| `surfaceContainer` | `#EEEDF4` | `#E8E7EE` | `#E2E1E8` | `#1D2024` | `#25282C` | `#2D3035` |
| `surfaceContainerHigh` | `#E8E7EE` | `#DDDCE3` | `#D4D3DA` | `#282A2F` | `#303237` | `#393B40` |
| `surfaceContainerHighest`| `#E2E1E9` | `#D2D1D8` | `#C6C5CC` | `#33353A` | `#3B3D42` | `#44464C` |

### 2.3 隐私通讯专属扩展语义色 (`ExtendedColors`)
- `verifiedShield`（端到端身份安全验证通过）：固定色相翡翠绿 `#00875A`（外圈 2dp 柔光）。
- `disappearingTimer`（阅后即焚计时/燃烧）：固定琥珀金 `#E57800`。
- `bubbleOutgoing`：映射 `primaryContainer`；文字 `onPrimaryContainer`（严禁半透明弱化文字）。
- `bubbleIncoming`：映射 `surfaceContainerHigh`；文字 `onSurface`。

---

## 3. 形状与动效系统 (Expressive Shapes & Motion)

### 3.1 形状与多边形平滑过渡 (`graphics-shapes`)
- **联系人与自身头像**：
  - 常态：9 顶点圆角多边形（Circle）。
  - 在线/正在输入/通话中：平滑 Morph 为 **`Cookie9Sided`（9 瓣花形曲奇）**，过渡时间 400ms，配合快弹簧。
- **消息气泡不对称圆角**：
  - 发送方出站气泡：右下角圆角收窄为 `6dp`，其余角为 `24dp`。同组连续气泡的右上角同步收窄，形成一体化气泡组。
  - 接收方入站气泡：左下角圆角收窄为 `6dp`，其余角为 `24dp`。
- **底部输入框**：全圆角胶囊 Pill 形态（`shape = CircleShape`）。

### 3.2 动效设计（双轨制保障无障碍）
- **FULL 模式（默认，物理弹簧阻尼）**：
  - 发送消息：气泡从输入框位置以空间弹簧（Spatial Spring, stiffness 800, damping 0.7）弹入消息流。
  - 图标变形：输入框右侧按钮在空文本时为“麦克风”，一旦键入文字，矢量路径平滑 Morph 变形为“纸飞机发送图标”，伴随轻微弹性缩放。
- **REDUCED 模式（系统减弱动画或省电状态）**：
  - 移除所有位移弹簧与形变旋转，降级为 150ms 优雅淡入淡出（Crossfade），不依赖位移传达任何关键信息。

---

## 4. 核心页面重设计与组件规格

### 4.1 会话列表页 (ConversationListScreen)
1. **顶栏与搜索**：
   - 采用 Expressive `LargeFlexibleTopAppBar`，展开态展示大字重强调标题“Signalm3”，滚动时平滑折叠为小标题。
   - 顶部集成全圆角胶囊搜索栏 `BerrySearchBar`，点击时以容器展开过渡展开为全屏搜索视图。
2. **顶部快速过滤筛选**：
   - 采用连接式 `BerryButtonGroup`（全部 / 未读 / 群聊 / 标星置顶）。
3. **会话列表项 (`ConversationListItem`)**：
   - 高度 76dp，左侧 52dp 变形头像（带在线点与安全锁徽标）。
   - 主标题：加粗字重，未读消息时采用 `titleMediumEmphasized`。
   - 副标题：消息摘要展示端到端锁/阅后即焚图标，未读时显示高亮。
   - 右侧：时间戳 + 紧凑式未读角标 `BerryUnreadBadge`（大字体模式下自适应缩放）。
   - **交互手势**：左右滑动操作（`SwipeToDismissBox`），右滑置顶（`secondaryContainer`），左滑静音/归档（`errorContainer`），并全量绑定无障碍 TalkBack Action。
4. **新建会话 FAB**：
   - 右下角 Expressive 大圆角方形 FAB（按压时几何形变反馈），支持长按/点击展开快速菜单（发起单聊、新建群聊、扫码绑定设备）。

### 4.2 聊天详情流与沉浸式输入区 (ChatThreadScreen)
1. **现代悬浮顶栏**：
   - 紧凑布局：返回按钮、对方形变头像、昵称及在线状态。
   - 安全验证勋章：点击直接弹出端到端安全号码（Safety Number）对比面板。
2. **消息流 (`LazyColumn reverseLayout = true`)**：
   - **文本气泡**：自适应宽度（最大不超过屏幕 78% 且上限 480dp），气泡内边距水平 16dp，垂直 10dp。
   - **语音消息**：集成自适应波形指示器（`BerryWaveformProgress`），随播放进度动态填充 `primary` 色，附带总时长与播放暂停切换。
   - **阅后即焚消息**：气泡右上角展示动态燃烧微动效砂漏与剩余时间倒计时。
   - **引用回复**：气泡内部顶置轻量透明背景块，左侧带 3dp `primary` 垂直强调线。
3. **沉浸式水平浮动输入栏 (`SignalFloatingInputBar`)**：
   - 采用悬浮式 `HorizontalFloatingToolbar` 设计，悬浮于屏幕底部（自动响应 `imePadding` 键盘贴合）。
   - 左侧：圆形附件按钮 `+`（点击弹起 4 列 Expressive 附件底板：相册、相机、文件、联系人、位置）。
   - 中间：自适应高度文本框（1~5 行自动弹性扩展，内部填充 `surfaceContainerHighest`）。
   - 右侧：表情/贴纸按钮，以及由**麦克风 ⇄ 发送图标**动态变形的圆形主操作钮。

### 4.3 联系人列表页 (ContactsScreen)
- **吸顶分组**：按字母顺序排列，分组头（A, B, C...）采用吸顶悬浮效果（`stickyHeader`），高 32dp。
- **快速滑动索引条**：右侧边缘提供 48dp 触摸热区的字母快速跳转轨道，滑动时屏幕中心弹出大号平滑多边形字母气泡（72×72dp）。

### 4.4 个人中心与设置中心 (SettingsScreen)
- **现代化分组卡片 (`BerrySectionCard`)**：
  - 彻底抛弃传统的平铺列表，每个大功能区采用 28dp 大圆角的 `surfaceContainer` 分组卡片包裹。
  - 开关项全面采用最新的 Material 3 Expressive `Switch`（带内置勾选图标，开启与关闭具备弹性形变）。
- **外观与主题设置**：
  - 动态取色开关：开启时展示当前系统提取的色相环预览。
  - 主题切换：跟随系统 / 浅色 / 深色。
  - 动效强度：自动（根据设备性能自适应）/ 完整物理动效 / 减弱动效。

---

## 5. 多设备与全尺寸自适应响应式规范

| 设备类型 | 屏幕宽度 | 导航与页面布局策略 |
| :--- | :--- | :--- |
| **手机纵屏 (Compact)** | `< 600dp` | 底部短导航栏（`ShortNavigationBar`），列表到详情为全屏平滑转场，详情页自动隐藏底部栏 |
| **折叠屏展开 / 小平板 (Medium)** | `600–839dp` | 左侧收起态宽导航轨（`WideNavigationRail` 宽 96dp），双栏列表与聊天详情自适应并排 |
| **大平板 / 桌面端 (Expanded)** | `≥ 840dp` | 左侧展开态导航轨（宽 240dp），采用 `NavigableListDetailPaneScaffold` 呈现标准三栏布局 |

---

## 6. 交付给开发的核心组件清单 (`Signal*` 组件树)

所有组件严格收拢在 `designsystem`，公共接口完全无任何实验性注解侵入：
1. **基础控件**：`SignalButton`, `SignalIconButton`, `SignalFab`, `SignalSwitch`, `SignalChip`
2. **顶栏与搜索**：`SignalTopAppBar`, `SignalSearchBar`, `SignalNavigationSuite`
3. **展示与消息**：
   - `SignalMorphAvatar`（圆形 ↔ Cookie9Sided 变形头像）
   - `SignalBubble`（不对称连续圆角气泡）
   - `SignalWaveformProgress`（语音波形播放器）
   - `SignalFloatingInputBar`（沉浸式悬浮输入工具栏与麦克风变形）
   - `SignalSwipeBox`（左右弹性滑动操作容器）
   - `SignalSectionCard`（设置分组大圆角卡片）
