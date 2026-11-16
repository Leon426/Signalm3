package org.thoughtcrime.securesms.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

/**
 * Signalm3 隐私通讯专属扩展语义色（spec §2.3）。
 */
@Immutable
data class SignalExtendedColors(
    val verifiedShield: Color,
    val onVerifiedShield: Color,
    val disappearingTimer: Color,
    val onDisappearingTimer: Color,
    val bubbleOutgoing: Color,
    val onBubbleOutgoing: Color,
    val bubbleIncoming: Color,
    val onBubbleIncoming: Color,
    val presenceOnline: Color,
    val unreadBadge: Color,
    val onUnreadBadge: Color,
) {
    companion object {
        fun of(scheme: ColorScheme, isDark: Boolean): SignalExtendedColors {
            return SignalExtendedColors(
                verifiedShield = if (isDark) Color(0xFF25D366) else Color(0xFF00875A),
                onVerifiedShield = Color(0xFFFFFFFF),
                disappearingTimer = if (isDark) Color(0xFFFFB347) else Color(0xFFE57800),
                onDisappearingTimer = Color(0xFFFFFFFF),
                bubbleOutgoing = scheme.primaryContainer,
                onBubbleOutgoing = scheme.onPrimaryContainer,
                bubbleIncoming = scheme.surfaceContainerHigh,
                onBubbleIncoming = scheme.onSurface,
                presenceOnline = if (isDark) Color(0xFF55E78E) else Color(0xFF00875A),
                unreadBadge = scheme.primary,
                onUnreadBadge = scheme.onPrimary,
            )
        }
    }
}

val LocalSignalExtendedColors = staticCompositionLocalOf {
    SignalExtendedColors.of(StaticSchemes.lightStandard, isDark = false)
}
