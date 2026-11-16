package org.thoughtcrime.securesms.conversation.m3

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoneAll
import androidx.compose.material.icons.filled.HourglassBottom
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Videocam
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.thoughtcrime.securesms.designsystem.component.SignalBubble
import org.thoughtcrime.securesms.designsystem.component.SignalFloatingInputBar
import org.thoughtcrime.securesms.designsystem.component.SignalMorphAvatar
import org.thoughtcrime.securesms.designsystem.shape.BubblePosition
import org.thoughtcrime.securesms.designsystem.theme.SignalM3

/**
 * 消息项数据契约。
 */
@Immutable
data class ChatMessageUiModel(
    val id: Long,
    val text: String,
    val isOutgoing: Boolean,
    val timeLabel: String,
    val isDelivered: Boolean = true,
    val isRead: Boolean = false,
    val isDisappearing: Boolean = false,
    val disappearingSecondsRemaining: Int? = null,
    val replyToText: String? = null,
)

/**
 * 全新 Material 3 聊天详情页面（spec §4.2）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatThreadM3Screen(
    recipientName: String,
    isOnline: Boolean,
    messages: List<ChatMessageUiModel>,
    onBackClick: () -> Unit,
    onSendMessage: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var inputText by remember { mutableStateOf("") }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        SignalMorphAvatar(
                            name = recipientName,
                            size = 38.dp,
                            isOnline = isOnline,
                        )
                        Column(modifier = Modifier.padding(start = 12.dp)) {
                            Text(
                                text = recipientName,
                                style = MaterialTheme.typography.titleMedium,
                                maxLines = 1,
                            )
                            Text(
                                text = if (isOnline) "在线" else "加密端到端会话",
                                style = MaterialTheme.typography.labelSmall,
                                color = if (isOnline) SignalM3.extendedColors.presenceOnline else MaterialTheme.colorScheme.onSurfaceVariant,
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "返回",
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* 呼叫 */ }) {
                        Icon(Icons.Default.Phone, contentDescription = "语音呼叫")
                    }
                    IconButton(onClick = { /* 视频 */ }) {
                        Icon(Icons.Default.Videocam, contentDescription = "视频通话")
                    }
                    IconButton(onClick = { /* 更多 */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "更多")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                ),
            )
        },
        bottomBar = {
            SignalFloatingInputBar(
                text = inputText,
                onTextChange = { inputText = it },
                onSend = { text ->
                    onSendMessage(text)
                },
                onAttachClick = { /* 附件底板 */ },
                onVoiceClick = { /* 语音录音 */ },
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 12.dp),
            reverseLayout = true,
            contentPadding = PaddingValues(vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(
                items = messages,
                key = { it.id },
            ) { msg ->
                ChatMessageBubbleRow(message = msg)
            }
        }
    }
}

/**
 * 单条消息气泡行（含不对称圆角、阅后即焚状态与双勾已读标记）。
 */
@Composable
private fun ChatMessageBubbleRow(
    message: ChatMessageUiModel,
    modifier: Modifier = Modifier,
) {
    val alignment = if (message.isOutgoing) Alignment.End else Alignment.Start

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = alignment,
    ) {
        SignalBubble(
            isOutgoing = message.isOutgoing,
            position = BubblePosition.SINGLE,
        ) {
            Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp)) {
                // 引用回复内容
                if (message.replyToText != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 6.dp)
                            .background(
                                color = MaterialTheme.colorScheme.surface.copy(alpha = 0.2f),
                                shape = MaterialTheme.shapes.small,
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                    ) {
                        Text(
                            text = message.replyToText,
                            style = MaterialTheme.typography.bodyMedium,
                            color = if (message.isOutgoing) SignalM3.extendedColors.onBubbleOutgoing else SignalM3.extendedColors.onBubbleIncoming,
                            maxLines = 2,
                        )
                    }
                }

                // 消息主体文本
                Text(
                    text = message.text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (message.isOutgoing) SignalM3.extendedColors.onBubbleOutgoing else SignalM3.extendedColors.onBubbleIncoming,
                )

                // 底部状态栏（时间、阅后即焚砂漏、发送状态）
                Row(
                    modifier = Modifier.padding(top = 4.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    // 阅后即焚倒计时
                    if (message.isDisappearing) {
                        Icon(
                            imageVector = Icons.Default.HourglassBottom,
                            contentDescription = "阅后即焚",
                            tint = SignalM3.extendedColors.disappearingTimer,
                            modifier = Modifier.size(12.dp),
                        )
                        if (message.disappearingSecondsRemaining != null) {
                            Text(
                                text = "${message.disappearingSecondsRemaining}s",
                                fontSize = 10.sp,
                                color = SignalM3.extendedColors.disappearingTimer,
                            )
                        }
                    }

                    // 时间戳
                    Text(
                        text = message.timeLabel,
                        fontSize = 11.sp,
                        color = if (message.isOutgoing) {
                            SignalM3.extendedColors.onBubbleOutgoing.copy(alpha = 0.8f)
                        } else {
                            SignalM3.extendedColors.onBubbleIncoming.copy(alpha = 0.7f)
                        },
                    )

                    // 发送/已读双勾状态（仅针对发出的消息）
                    if (message.isOutgoing) {
                        if (message.isRead) {
                            Icon(
                                imageVector = Icons.Default.DoneAll,
                                contentDescription = "已读",
                                tint = if (message.isOutgoing) SignalM3.extendedColors.onBubbleOutgoing else Color.Unspecified,
                                modifier = Modifier.size(14.dp),
                            )
                        } else if (message.isDelivered) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "已送达",
                                tint = if (message.isOutgoing) SignalM3.extendedColors.onBubbleOutgoing.copy(alpha = 0.8f) else Color.Unspecified,
                                modifier = Modifier.size(14.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}
