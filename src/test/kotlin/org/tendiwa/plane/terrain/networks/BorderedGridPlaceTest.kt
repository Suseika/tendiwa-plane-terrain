package org.tendiwa.plane.terrain.networks

import org.junit.Test
import org.tendiwa.plane.grid.constructors.GridRectangle
import org.tendiwa.plane.grid.dimensions.by
import kotlin.test.assertEquals

class BorderedGridPlaceTest {
    @Test
    fun `exits contain only tiles that satisfy the predicate`() {
        BorderedGridPlace(
            GridRectangle(27 by 24),
            { index, tile -> index % 20 == 0 }
        )
            .exits
            .apply { assertEquals(5, size) }
    }
}
