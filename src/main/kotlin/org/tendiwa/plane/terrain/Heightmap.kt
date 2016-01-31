package org.tendiwa.plane.terrain

import org.tendiwa.plane.grid.tiles.Tile

/**
 * Maps coordinates to height above sea level.
 */
interface Heightmap {
    fun height(x: Int, y: Int): Int

    fun height(tile: Tile): Int =
        height(tile.x, tile.y)
}
