package org.tendiwa.plane.terrain.networks

import org.junit.Test
import org.tendiwa.graphs.connectivity.connectivityComponents
import org.tendiwa.graphs.connectivity.isConnected
import org.tendiwa.plane.grid.constructors.GridRectangle
import org.tendiwa.plane.grid.constructors.commonHull
import org.tendiwa.plane.grid.dimensions.by
import org.tendiwa.plane.grid.masks.move
import org.tendiwa.plane.grid.masks.union
import org.tendiwa.plane.grid.rectangles.grow
import kotlin.test.assertEquals

class InterplaceNetworkTest {
    val network =
        (0..2)
            .map { GridRectangle(10 by 10).move(20 * it, 20) }
            .let {
                rectangles ->
                InterplaceNetwork(
                    rectangles
                        .map { BorderedGridPlace(it, { i, tile -> i % 20 == 0 }) },
                    rectangles.reduce { a, b -> a.commonHull(b) }
                )
            }

    @Test
    fun `connects all places if walkable mask is connected`() {
        assert(network.graph.isConnected())
    }

    @Test
    fun `can create a disconnected network in a disconnected walkable mask`() {
        val topPlace1 =
            BorderedGridPlace(
                GridRectangle(10 by 10).move(0, 100),
                { i, tile -> i == 20 }
            )
        val topPlace2 =
            BorderedGridPlace(
                GridRectangle(10 by 10).move(20, 100),
                { i, tile -> i == 20 }
            )
        val topWalkable =
            topPlace1.mask.commonHull(topPlace2.mask).grow(1)
        val bottomPlace1 =
            BorderedGridPlace(
                GridRectangle(10 by 10).move(0, 0),
                { i, tile -> i == 20 }
            )
        val bottomPlace2 =
            BorderedGridPlace(
                GridRectangle(10 by 10).move(20, 0),
                { i, tile -> i == 20 }
            )
        val bottomWalkable =
            bottomPlace1.mask.commonHull(bottomPlace2.mask).grow(1)
        InterplaceNetwork(
            listOf(topPlace1, topPlace2, bottomPlace1, bottomPlace2),
            topWalkable.union(bottomWalkable)
        )
            .apply {
                assertEquals(2, graph.connectivityComponents().size)
                assertEquals(2, paths().size)
            }
    }

    @Test
    fun `creates paths`() {
        assertEquals(2, network.paths().size)
    }
}
