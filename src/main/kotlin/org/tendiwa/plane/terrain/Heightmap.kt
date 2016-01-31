package org.tendiwa.plane.terrain

import org.tendiwa.plane.grid.tiles.Tile

/**
 * Maps coordinates to height above sea level.
 */
interface Heightmap {
    fun elevation(x: Int, y: Int): Elevation

    fun elevation(tile: Tile): Elevation =
        elevation(tile.x, tile.y)
}

