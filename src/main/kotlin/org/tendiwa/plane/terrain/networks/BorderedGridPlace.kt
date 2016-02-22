package org.tendiwa.plane.terrain.networks

import org.tendiwa.plane.grid.algorithms.outlines.outline
import org.tendiwa.plane.grid.masks.BoundedGridMask
import org.tendiwa.plane.grid.tiles.Tile

/**
 * [GridPlace] with exits placed on a 1-tile wide inward buffer of the [mask].
 */
class BorderedGridPlace
/**
 * @param mask Mask of the [GridPlace].
 * @param isExit Predicate for determining the exit tiles of the buffer.
 */
(
    override val mask: BoundedGridMask,
    isExit: (Int, Tile) -> Boolean
) : GridPlace {
    override val exits: Set<Tile> =
        mask
            .outline()
            .components
            .map { it.filterIndexed(isExit) }
            .flatMap { it }
            .toSet()
}

