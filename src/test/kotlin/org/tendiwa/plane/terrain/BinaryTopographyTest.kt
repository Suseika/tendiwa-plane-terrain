package org.tendiwa.plane.terrain

import org.junit.Test
import org.tendiwa.plane.grid.masks.boundedBy
import org.tendiwa.plane.grid.masks.intersection
import org.tendiwa.plane.grid.masks.union
import org.tendiwa.plane.grid.rectangles.GridRectangle
import kotlin.test.assertEquals

class BinaryTopographyTest {
    val region = GridRectangle(10, -20, 40, 50)
    val topography = BinaryTopography(
        heightmap = object : Heightmap {
            override fun elevation(x: Int, y: Int): Elevation =
                if ((x + y) % 2 == 0) {
                    Elevation(1)
                } else {
                    Elevation(-1)
                }
        },
        seaLevel = Elevation(0),
        region = region
    )

    @Test
    fun `ground and water dont intersect`() {
        topography.ground
            .intersection(topography.water)
            .apply { assert(tiles.isEmpty()) }
    }

    @Test
    fun `ground and water cover the whole region`() {
        topography.ground
            .union(topography.water)
            .boundedBy(region)
            .apply { assertEquals(region.tiles, this.tiles) }
    }
}
