package org.thoughtcrime.securesms.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import org.thoughtcrime.securesms.designsystem.shape.BubblePosition
import org.thoughtcrime.securesms.designsystem.shape.BubbleShapes
import org.thoughtcrime.securesms.designsystem.theme.SignalDimens
import org.thoughtcrime.securesms.designsystem.theme.SignalM3

/**
 * 不对称圆角气泡容器（spec §3.1）。
 */
@Composable
fun SignalBubble(
    isOutgoing: Boolean,
    modifier: Modifier = Modifier,
    position: BubblePosition = BubblePosition.SINGLE,
    maxWidth: Dp = SignalDimens.bubbleMaxWidth,
    content: @Composable () -> Unit,
) {
    val shape = BubbleShapes.shape(isOutgoing, position)
    val containerColor = if (isOutgoing) {
        SignalM3.extendedColors.bubbleOutgoing
    } else {
        SignalM3.extendedColors.bubbleIncoming
    }
    val contentColor = if (isOutgoing) {
        SignalM3.extendedColors.onBubbleOutgoing
    } else {
        SignalM3.extendedColors.onBubbleIncoming
    }

    Surface(
        modifier = modifier.widthIn(max = maxWidth),
        shape = shape,
        color = containerColor,
        contentColor = contentColor,
    ) {
        Box {
            content()
        }
    }
}
