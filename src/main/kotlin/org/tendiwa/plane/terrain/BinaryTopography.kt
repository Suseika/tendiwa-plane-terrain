package org.tendiwa.plane.terrain

import org.tendiwa.plane.grid.masks.*
import org.tendiwa.plane.grid.rectangles.GridRectangle

/**
 * Topography that divides a region into mutually exclusive ground and water
 * that together cover the whole region.
 */
class BinaryTopography(
    private val heightmap: Heightmap,
    private val seaLevel: Elevation,
    region: GridRectangle
) {
    val ground: BoundedGridMask =
        ArrayGridMask(
            GridMask { x, y -> heightmap.elevation(x, y) > seaLevel }
                .boundedBy(region)
        )

    val water: BoundedGridMask =
        ArrayGridMask(ground.inverse.boundedBy(region))
}
