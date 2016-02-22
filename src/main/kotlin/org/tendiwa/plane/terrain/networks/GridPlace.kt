package org.tendiwa.plane.terrain.networks

import org.tendiwa.plane.grid.masks.GridMask
import org.tendiwa.plane.grid.tiles.Tile

interface GridPlace {
    val mask: GridMask
    val exits: Set<Tile>
}
