package org.tendiwa.plane.terrain

import org.junit.Rule
import org.junit.Test
import org.junit.rules.ExpectedException
import org.tendiwa.plane.terrain.Elevation.Companion.MAX_HEIGHT
import org.tendiwa.plane.terrain.Elevation.Companion.MIN_HEIGHT
import org.tendiwa.tools.expectIllegalArgument
import kotlin.test.assertEquals

class ElevationTest {
    @JvmField @Rule val expectRule = ExpectedException.none()

    @Test
    fun `contains cached values`() {
        assertEquals(3, Elevation(3).index)
    }

    @Test
    fun `fails to return a height that is too low`() {
        expectRule.expectIllegalArgument("Height must be between")
        Elevation(MIN_HEIGHT - 1)
    }

    @Test
    fun `fails to return a height that is too high`() {
        expectRule.expectIllegalArgument("Height must be between")
        Elevation(MAX_HEIGHT + 1)
    }
}
