package org.thoughtcrime.securesms.designsystem

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.dp
import org.junit.Assert.assertEquals
import org.junit.Test
import org.thoughtcrime.securesms.designsystem.shape.BubblePosition
import org.thoughtcrime.securesms.designsystem.shape.BubbleShapes

class BubbleShapesTest {

    @Test
    fun outgoingBubble_hasAsymmetricCorners() {
        val shape = BubbleShapes.shape(isOutgoing = true, position = BubblePosition.SINGLE) as RoundedCornerShape
        // 发送方右下角收窄为 6dp
        assertEquals(24.dp, shape.topStart)
        assertEquals(24.dp, shape.topEnd)
        assertEquals(6.dp, shape.bottomEnd)
        assertEquals(24.dp, shape.bottomStart)
    }

    @Test
    fun incomingBubble_hasAsymmetricCorners() {
        val shape = BubbleShapes.shape(isOutgoing = false, position = BubblePosition.SINGLE) as RoundedCornerShape
        // 接收方左下角收窄为 6dp
        assertEquals(24.dp, shape.topStart)
        assertEquals(24.dp, shape.topEnd)
        assertEquals(24.dp, shape.bottomEnd)
        assertEquals(6.dp, shape.bottomStart)
    }
}
