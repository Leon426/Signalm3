package org.thoughtcrime.securesms.designsystem.component

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.thoughtcrime.securesms.designsystem.shape.AvatarShapeState
import org.thoughtcrime.securesms.designsystem.shape.AvatarShapes
import org.thoughtcrime.securesms.designsystem.theme.SignalDimens
import org.thoughtcrime.securesms.designsystem.theme.SignalM3

/**
 * 平滑变形头像组件（spec §3.1）。
 */
@Composable
fun SignalMorphAvatar(
    name: String,
    modifier: Modifier = Modifier,
    size: Dp = SignalDimens.avatarSizeMd,
    isOnline: Boolean = false,
    shapeState: AvatarShapeState = if (isOnline) AvatarShapeState.EMPHASIZED else AvatarShapeState.DEFAULT,
) {
    val targetProgress = if (shapeState == AvatarShapeState.EMPHASIZED) 1f else 0f
    val morphProgress by animateFloatAsState(
        targetValue = targetProgress,
        animationSpec = SignalM3.motion.spatialFast,
        label = "avatarMorph",
    )

    val currentShape = AvatarShapes.shape(shapeState, morphProgress)
    val initials = name.take(2).uppercase()

    Box(modifier = modifier.size(size), contentAlignment = Alignment.Center) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            shape = currentShape,
            color = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = initials,
                    fontWeight = FontWeight.Bold,
                    fontSize = (size.value * 0.38f).sp,
                )
            }
        }

        // 在线状态指示点
        if (isOnline) {
            Box(
                modifier = Modifier
                    .size(14.dp)
                    .align(Alignment.BottomEnd)
                    .offset(x = 1.dp, y = 1.dp)
                    .clip(CircleShape)
                    .background(SignalM3.extendedColors.presenceOnline)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape),
            )
        }
    }
}
