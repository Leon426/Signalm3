package org.thoughtcrime.securesms.designsystem

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import org.thoughtcrime.securesms.designsystem.theme.ContrastValidator
import org.thoughtcrime.securesms.designsystem.theme.SignalExtendedColors
import org.thoughtcrime.securesms.designsystem.theme.StaticSchemes

class ContrastValidatorTest {

    @Test
    fun staticSchemes_allPassAccessibilityThresholds() {
        val schemes = listOf(
            "lightStandard" to StaticSchemes.lightStandard,
            "lightMedium" to StaticSchemes.lightMedium,
            "lightHigh" to StaticSchemes.lightHigh,
            "darkStandard" to StaticSchemes.darkStandard,
            "darkMedium" to StaticSchemes.darkMedium,
            "darkHigh" to StaticSchemes.darkHigh,
        )

        schemes.forEach { (name, scheme) ->
            val isDark = name.startsWith("dark")
            val extended = SignalExtendedColors.of(scheme, isDark)
            val failures = ContrastValidator.validate(scheme, extended)
            assertTrue("Scheme $name has contrast failures: $failures", failures.isEmpty())
        }
    }

    @Test
    fun contrastValidator_detectsLowContrastOnSurface() {
        // 构造极端低对比配色方案
        val base = StaticSchemes.lightStandard
        val badScheme = base.copy(
            onSurface = base.surface // 前景和背景同色
        )
        val extended = SignalExtendedColors.of(badScheme, isDark = false)
        val failures = ContrastValidator.validate(badScheme, extended)

        assertTrue(failures.isNotEmpty())
        assertTrue(failures.any { it.role == "onSurface/surface" })
        assertEquals(4.5, failures.first { it.role == "onSurface/surface" }.required, 0.01)
    }
}
