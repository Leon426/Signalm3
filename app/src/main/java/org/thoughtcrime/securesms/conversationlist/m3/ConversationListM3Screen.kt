package org.thoughtcrime.securesms.conversationlist.m3

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PinDrop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import org.thoughtcrime.securesms.designsystem.component.SignalMorphAvatar
import org.thoughtcrime.securesms.designsystem.component.SignalSearchBar
import org.thoughtcrime.securesms.designsystem.component.SignalTopAppBar
import org.thoughtcrime.securesms.designsystem.component.SignalUnreadBadge
import org.thoughtcrime.securesms.designsystem.theme.SignalDimens
import org.thoughtcrime.securesms.designsystem.theme.SignalM3

/**
 * 会话列表项数据契约（UDF 模型）。
 */
@Immutable
data class ConversationListItemUiModel(
    val threadId: Long,
    val title: String,
    val snippet: String,
    val dateLabel: String,
    val unreadCount: Int = 0,
    val isPinned: Boolean = false,
    val isOnline: Boolean = false,
    val isSecure: Boolean = true,
)

/**
 * 会话列表筛选标签。
 */
enum class ConversationFilterTab {
    ALL,
    UNREAD,
    GROUPS
}

/**
 * 全新 Material 3 会话列表主界面（spec §4.1）。
 */
@Composable
fun ConversationListM3Screen(
    conversations: List<ConversationListItemUiModel>,
    onConversationClick: (Long) -> Unit,
    onNewConversationClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf(ConversationFilterTab.ALL) }

    val filteredList = remember(conversations, searchQuery, selectedFilter) {
        conversations.filter { item ->
            val matchQuery = searchQuery.isEmpty() ||
                item.title.contains(searchQuery, ignoreCase = true) ||
                item.snippet.contains(searchQuery, ignoreCase = true)

            val matchTab = when (selectedFilter) {
                ConversationFilterTab.ALL -> true
                ConversationFilterTab.UNREAD -> item.unreadCount > 0
                ConversationFilterTab.GROUPS -> item.title.contains("群") || item.title.contains("Group")
            }
            matchQuery && matchTab
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            Column(modifier = Modifier.background(MaterialTheme.colorScheme.surface)) {
                SignalTopAppBar(
                    title = "Signal",
                    actions = {
                        IconButton(onClick = { /* 相机快捷入口 */ }) {
                            Icon(Icons.Default.CameraAlt, contentDescription = "相机")
                        }
                        IconButton(onClick = { /* 更多选项 */ }) {
                            Icon(Icons.Default.MoreVert, contentDescription = "更多")
                        }
                    },
                )
                SignalSearchBar(
                    query = searchQuery,
                    onQueryChange = { searchQuery = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
                )
                ConversationFilterRow(
                    selectedTab = selectedFilter,
                    onTabSelected = { selectedFilter = it },
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onNewConversationClick,
                shape = RoundedCornerShape(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            ) {
                Icon(Icons.Default.Edit, contentDescription = "新建聊天")
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp),
        ) {
            items(
                items = filteredList,
                key = { it.threadId },
            ) { item ->
                ConversationItemM3Row(
                    item = item,
                    onClick = { onConversationClick(item.threadId) },
                )
            }
        }
    }
}

/**
 * 顶部快捷筛选过滤条。
 */
@Composable
private fun ConversationFilterRow(
    selectedTab: ConversationFilterTab,
    onTabSelected: (ConversationFilterTab) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        FilterChip(
            label = "全部",
            selected = selectedTab == ConversationFilterTab.ALL,
            onClick = { onTabSelected(ConversationFilterTab.ALL) },
        )
        FilterChip(
            label = "未读",
            selected = selectedTab == ConversationFilterTab.UNREAD,
            onClick = { onTabSelected(ConversationFilterTab.UNREAD) },
        )
        FilterChip(
            label = "群组",
            selected = selectedTab == ConversationFilterTab.GROUPS,
            onClick = { onTabSelected(ConversationFilterTab.GROUPS) },
        )
    }
}

@Composable
private fun FilterChip(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    Surface(
        shape = CircleShape,
        color = if (selected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceContainerLow,
        contentColor = if (selected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant,
        modifier = Modifier
            .clip(CircleShape)
            .clickable(onClick = onClick),
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp),
        )
    }
}

/**
 * 单条会话项。
 */
@Composable
fun ConversationItemM3Row(
    item: ConversationListItemUiModel,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isUnread = item.unreadCount > 0

    Surface(
        color = if (item.isPinned) MaterialTheme.colorScheme.surfaceContainerHigh else Color.Transparent,
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 左侧：多边形变形头像（带在线状态）
            SignalMorphAvatar(
                name = item.title,
                size = SignalDimens.avatarSizeMd,
                isOnline = item.isOnline,
            )

            // 中间：标题与摘要
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = item.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = if (isUnread) FontWeight.Bold else FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false),
                    )
                    if (item.isSecure) {
                        Icon(
                            imageVector = Icons.Default.Lock,
                            contentDescription = "端到端加密",
                            tint = SignalM3.extendedColors.verifiedShield,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .size(14.dp),
                        )
                    }
                }

                Text(
                    text = item.snippet,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = if (isUnread) FontWeight.Medium else FontWeight.Normal,
                    color = if (isUnread) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }

            // 右侧：时间、置顶图标与未读角标
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = item.dateLabel,
                    style = MaterialTheme.typography.labelSmall,
                    color = if (isUnread) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Box(
                    modifier = Modifier
                        .height(26.dp)
                        .padding(top = 4.dp),
                    contentAlignment = Alignment.CenterEnd,
                ) {
                    if (isUnread) {
                        SignalUnreadBadge(count = item.unreadCount)
                    }
                }
            }
        }
    }
}
