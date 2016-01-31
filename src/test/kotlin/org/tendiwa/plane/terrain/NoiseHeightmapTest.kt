package org.tendiwa.plane.terrain

import org.junit.Test
import org.tendiwa.math.noise.PerlinNoise
import org.tendiwa.plane.grid.constructors.GridRectangle
import org.tendiwa.plane.grid.dimensions.by

class NoiseHeightmapTest {
    @Test
    fun `generates different elevations with noise that generates different samples`() {
        val heightmap = NoiseHeightmap(
            noise = PerlinNoise(6)
        )
        GridRectangle(100 by 100)
            .tiles
            .map { heightmap.elevation(it) }
            .toSet()
            .apply { assert(size > 1) }
    }
}
