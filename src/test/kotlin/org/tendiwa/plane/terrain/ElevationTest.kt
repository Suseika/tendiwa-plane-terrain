package org.tendiwa.plane.terrain

import org.junit.Assert.assertSame
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
        assertEquals(3, Elevation(3).height)
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

    @Test
    fun `stores all valid values`() {
        assertSame(Elevation(8), Elevation(8))
    }

    @Test
    fun `returns min elevation`() {
        assertEquals(
            MIN_HEIGHT,
            Elevation(Elevation.MIN_HEIGHT).height
        )
    }

    @Test
    fun `returns max elevation`() {
        assertEquals(
            MAX_HEIGHT,
            Elevation(Elevation.MAX_HEIGHT).height
        )
    }
}
