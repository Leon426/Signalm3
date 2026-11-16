package org.thoughtcrime.securesms.designsystem.shape

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp

/**
 * 头像形状状态。
 */
enum class AvatarShapeState {
    DEFAULT,    // 常态圆形
    EMPHASIZED  // 在线 / 选中态（平滑形变为有机多边形）
}

object AvatarShapes {
    val circle: Shape = CircleShape
    val cookie9: Shape = RoundedCornerShape(32)

    fun shape(state: AvatarShapeState, morphProgress: Float): Shape {
        val radiusPercent = (50 - (18 * morphProgress)).toInt().coerceIn(32, 50)
        return RoundedCornerShape(radiusPercent)
    }
}
