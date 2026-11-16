package org.thoughtcrime.securesms.designsystem.shape

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * 连续消息流中的气泡位置。
 */
enum class BubblePosition {
    SINGLE,
    FIRST,
    MIDDLE,
    LAST
}

/**
 * 不对称圆角气泡生成器（spec §3.1）。
 */
object BubbleShapes {

    fun shape(isOutgoing: Boolean, position: BubblePosition): Shape {
        val large = 24.dp
        val small = 6.dp

        return if (isOutgoing) {
            // 我发送的消息：右侧圆角收窄
            when (position) {
                BubblePosition.SINGLE -> RoundedCornerShape(large, large, small, large)
                BubblePosition.FIRST -> RoundedCornerShape(large, large, small, large)
                BubblePosition.MIDDLE -> RoundedCornerShape(large, small, small, large)
                BubblePosition.LAST -> RoundedCornerShape(large, small, small, large)
            }
        } else {
            // 对方发送的消息：左侧圆角收窄
            when (position) {
                BubblePosition.SINGLE -> RoundedCornerShape(large, large, large, small)
                BubblePosition.FIRST -> RoundedCornerShape(large, large, large, small)
                BubblePosition.MIDDLE -> RoundedCornerShape(large, large, small, small)
                BubblePosition.LAST -> RoundedCornerShape(large, large, small, small)
            }
        }
    }
}
