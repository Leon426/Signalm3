package org.thoughtcrime.securesms.designsystem.theme

import androidx.compose.runtime.Immutable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * 4dp 基准间距与尺寸 Token。
 */
@Immutable
object SignalDimens {
    val spacingXs: Dp = 4.dp
    val spacingSm: Dp = 8.dp
    val spacingMd: Dp = 12.dp
    val spacingLg: Dp = 16.dp
    val spacingXl: Dp = 24.dp
    val spacingXxl: Dp = 32.dp

    val avatarSizeSm: Dp = 36.dp
    val avatarSizeMd: Dp = 52.dp
    val avatarSizeLg: Dp = 96.dp

    val bubbleMaxWidth: Dp = 480.dp
    val bubbleCornerLarge: Dp = 24.dp
    val bubbleCornerSmall: Dp = 6.dp

    val listItemHeight: Dp = 76.dp
    val searchBarHeight: Dp = 56.dp
    val minTouchTarget: Dp = 48.dp
}
