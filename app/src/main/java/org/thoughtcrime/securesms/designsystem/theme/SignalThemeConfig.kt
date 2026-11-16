package org.thoughtcrime.securesms.designsystem.theme

import androidx.compose.runtime.Immutable

/**
 * 动效偏好档位。
 */
enum class MotionPreference {
    FULL,    // 弹簧物理阻尼与平滑形变
    REDUCED, // 减弱动效，优雅淡入淡出
    NONE     // 无动画，直接切换终态
}

/**
 * 主题配置。
 */
@Immutable
data class SignalThemeConfig(
    val darkTheme: Boolean = false,
    val useDynamicColor: Boolean = true,
    val motionPreference: MotionPreference = MotionPreference.FULL,
    val highContrast: Boolean = false,
)
