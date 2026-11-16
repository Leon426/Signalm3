package org.thoughtcrime.securesms.designsystem.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.thoughtcrime.securesms.designsystem.motion.LocalSignalMotion
import org.thoughtcrime.securesms.designsystem.motion.SignalMotion

/**
 * Signalm3 统一设计系统主题入口。
 */
@Composable
fun SignalM3Theme(
    config: SignalThemeConfig = SignalThemeConfig(),
    content: @Composable () -> Unit,
) {
    val context = LocalContext.current
    val dark = config.darkTheme || isSystemInDarkTheme()

    val scheme = remember(config, dark) {
        if (config.useDynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val dynamic = if (dark) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
            val extended = SignalExtendedColors.of(dynamic, dark)
            val failures = ContrastValidator.validate(dynamic, extended)
            if (failures.isEmpty()) {
                dynamic
            } else {
                // 对比度不达标整套安全回退
                if (dark) StaticSchemes.darkStandard else StaticSchemes.lightStandard
            }
        } else {
            if (config.highContrast) {
                if (dark) StaticSchemes.darkHigh else StaticSchemes.lightHigh
            } else {
                if (dark) StaticSchemes.darkStandard else StaticSchemes.lightStandard
            }
        }
    }

    val extendedColors = remember(scheme, dark) {
        SignalExtendedColors.of(scheme, dark)
    }

    val motion = remember(config.motionPreference) {
        SignalMotion.of(config.motionPreference)
    }

    CompositionLocalProvider(
        LocalSignalExtendedColors provides extendedColors,
        LocalSignalMotion provides motion,
    ) {
        MaterialTheme(
            colorScheme = scheme,
            shapes = SignalShapes.material,
            typography = SignalTypography,
            content = content,
        )
    }
}

/**
 * 访问扩展语义色的简便语法。
 */
object SignalM3 {
    val extendedColors: SignalExtendedColors
        @Composable
        @ReadOnlyComposable
        get() = LocalSignalExtendedColors.current

    val dimens: SignalDimens
        get() = SignalDimens

    val motion: SignalMotion
        @Composable
        @ReadOnlyComposable
        get() = LocalSignalMotion.current
}
