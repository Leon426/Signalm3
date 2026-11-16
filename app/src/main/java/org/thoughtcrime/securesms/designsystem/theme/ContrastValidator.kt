package org.thoughtcrime.securesms.designsystem.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.core.graphics.ColorUtils

/**
 * 运行时对比度校验器（spec §2.1）。
 * 保证正文文本 ≥ 4.5:1，图标/UI边界 ≥ 3.0:1。
 */
object ContrastValidator {

    data class Failure(val role: String, val required: Double, val actual: Double)

    fun validate(scheme: ColorScheme, extended: SignalExtendedColors): List<Failure> {
        val failures = mutableListOf<Failure>()

        fun check(role: String, fg: Color, bg: Color, required: Double) {
            val ratio = ColorUtils.calculateContrast(fg.toArgb(), bg.toArgb())
            if (ratio < required) {
                failures.add(Failure(role, required, ratio))
            }
        }

        // 核心文字
        check("onSurface/surface", scheme.onSurface, scheme.surface, 4.5)
        check("onPrimary/primary", scheme.onPrimary, scheme.primary, 4.5)
        check("onPrimaryContainer/primaryContainer", scheme.onPrimaryContainer, scheme.primaryContainer, 4.5)
        check("onBubbleOutgoing/bubbleOutgoing", extended.onBubbleOutgoing, extended.bubbleOutgoing, 4.5)
        check("onBubbleIncoming/bubbleIncoming", extended.onBubbleIncoming, extended.bubbleIncoming, 4.5)
        // 关键状态图标
        check("verifiedShield/surface", extended.verifiedShield, scheme.surface, 3.0)
        check("unreadBadge/surface", extended.unreadBadge, scheme.surface, 3.0)

        return failures
    }
}
