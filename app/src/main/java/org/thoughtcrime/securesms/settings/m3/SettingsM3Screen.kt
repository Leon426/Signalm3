package org.thoughtcrime.securesms.settings.m3

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ColorLens
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import org.thoughtcrime.securesms.designsystem.component.SignalMorphAvatar
import org.thoughtcrime.securesms.designsystem.component.SignalSectionCard
import org.thoughtcrime.securesms.designsystem.theme.SignalDimens

/**
 * 全新 Material 3 设置中心（spec §4.4，现代化 28dp 分组大卡片）。
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsM3Screen(
    profileName: String,
    profilePhone: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var dynamicColorEnabled by remember { mutableStateOf(true) }
    var darkThemeEnabled by remember { mutableStateOf(false) }
    var fullMotionEnabled by remember { mutableStateOf(true) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("设置", style = MaterialTheme.typography.titleLarge) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "返回")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                ),
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
        ) {
            // 个人资料卡片
            SignalSectionCard {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SignalMorphAvatar(
                        name = profileName,
                        size = SignalDimens.avatarSizeLg,
                        isOnline = true,
                    )
                    Column(modifier = Modifier.padding(start = 16.dp)) {
                        Text(
                            text = profileName,
                            style = MaterialTheme.typography.headlineMedium,
                        )
                        Text(
                            text = profilePhone,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 4.dp),
                        )
                    }
                }
            }

            // 外观与主题
            SignalSectionCard(title = "外观与表现力") {
                SettingSwitchRow(
                    icon = Icons.Default.ColorLens,
                    title = "系统动态取色 (Material You)",
                    subtitle = "优先跟随系统壁纸色调生成调色板",
                    checked = dynamicColorEnabled,
                    onCheckedChange = { dynamicColorEnabled = it },
                )
                SettingSwitchRow(
                    icon = Icons.Default.DarkMode,
                    title = "深色模式",
                    subtitle = "纯净深邃的夜间视觉体系",
                    checked = darkThemeEnabled,
                    onCheckedChange = { darkThemeEnabled = it },
                )
                SettingSwitchRow(
                    icon = Icons.Default.Speed,
                    title = "完整物理弹簧动效",
                    subtitle = "启用高表现力 Spring 空间物理动效与形变",
                    checked = fullMotionEnabled,
                    onCheckedChange = { fullMotionEnabled = it },
                )
            }

            // 隐私与安全
            SignalSectionCard(title = "隐私与安全") {
                SettingNavRow(
                    icon = Icons.Default.Lock,
                    title = "应用锁定与屏幕安全",
                    subtitle = "生物指纹识别与防截屏保护",
                    onClick = { /* 应用锁 */ },
                )
                SettingNavRow(
                    icon = Icons.Default.Security,
                    title = "端到端安全号码对比",
                    subtitle = "验证与通讯录联系人的加密指纹",
                    onClick = { /* 安全号码 */ },
                )
            }

            // 通知与存储
            SignalSectionCard(title = "通用") {
                SettingNavRow(
                    icon = Icons.Default.Notifications,
                    title = "通知与免打扰",
                    subtitle = "应用内提示音、震动与频道管理",
                    onClick = { /* 通知 */ },
                )
                SettingNavRow(
                    icon = Icons.Default.Storage,
                    title = "数据与存储空间",
                    subtitle = "管理已缓存的媒体与阅后即焚清理",
                    onClick = { /* 存储 */ },
                )
            }
        }
    }
}

@Composable
private fun SettingSwitchRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCheckedChange(!checked) }
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}

@Composable
private fun SettingNavRow(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp),
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(horizontal = 16.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium)
            Text(text = subtitle, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
