package org.thoughtcrime.securesms.designsystem.motion

import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import org.thoughtcrime.securesms.designsystem.theme.MotionPreference

/**
 * 物理弹簧阻尼动效规范。
 */
@Immutable
data class SignalMotion(
    val level: MotionPreference,
    val spatialFast: AnimationSpec<Float>,
    val spatialStandard: AnimationSpec<Float>,
    val effectsFade: AnimationSpec<Float>,
) {
    companion object {
        fun of(level: MotionPreference): SignalMotion {
            return when (level) {
                MotionPreference.FULL -> SignalMotion(
                    level = level,
                    spatialFast = spring(dampingRatio = 0.7f, stiffness = 800f),
                    spatialStandard = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow),
                    effectsFade = tween(durationMillis = 200),
                )
                MotionPreference.REDUCED -> SignalMotion(
                    level = level,
                    spatialFast = tween(durationMillis = 150),
                    spatialStandard = tween(durationMillis = 200),
                    effectsFade = tween(durationMillis = 150),
                )
                MotionPreference.NONE -> SignalMotion(
                    level = level,
                    spatialFast = snap(),
                    spatialStandard = snap(),
                    effectsFade = snap(),
                )
            }
        }
    }
}

val LocalSignalMotion = staticCompositionLocalOf {
    SignalMotion.of(MotionPreference.FULL)
}
