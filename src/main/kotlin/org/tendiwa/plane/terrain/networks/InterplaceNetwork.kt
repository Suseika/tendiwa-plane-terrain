package org.tendiwa.plane.terrain.networks

import org.jgrapht.UndirectedGraph
import org.jgrapht.graph.UnmodifiableUndirectedGraph
import org.tendiwa.graphs.SimpleGraphWithoutAutoEdges
import org.tendiwa.graphs.edges
import org.tendiwa.graphs.vertices
import org.tendiwa.plane.grid.algorithms.pathfinding.GridPath
import org.tendiwa.plane.grid.algorithms.pathfinding.astar.path
import org.tendiwa.plane.grid.algorithms.waves.DestinationTile
import org.tendiwa.plane.grid.algorithms.waves.Wave
import org.tendiwa.plane.grid.algorithms.waves.closestTile
import org.tendiwa.plane.grid.masks.FiniteGridMask
import org.tendiwa.plane.grid.masks.GridMask
import org.tendiwa.plane.grid.masks.difference
import org.tendiwa.plane.grid.masks.union
import org.tendiwa.plane.grid.metrics.GridMetric
import org.tendiwa.plane.grid.segments.GridSegment
import org.tendiwa.plane.grid.tiles.Tile

/**
 * Connects [GridPlace]s into a graph where edges are the connections between
 * closest exits of pairs of closest places, so that no two places are
 * connected twice.
 */
class InterplaceNetwork
/**
 * @param places Places that are connected by this network.
 * @param walkable Space over which paths between places are laid.
 */
(
    places: List<GridPlace>,
    walkable: GridMask
) {
    val graph: UnmodifiableUndirectedGraph<GridPlace, GridSegment>

    val walkable: GridMask =
        places
            .flatMap { it.exits }
            .toSet()
            .let { FiniteGridMask(it) }
            .let { walkable.union(it) }

    init {
        val exitsToPlaces: Map<Tile, GridPlace> =
            places
                .flatMap {
                    place ->
                    place.exits.map { exit -> Pair(exit, place) }
                }
                .toMap()
        val allExits =
            places
                .flatMap { it.exits }
                .toSet()
        val connections: List<NeighborConnection> =
            places
                .flatMap {
                    place ->
                    place.exits.map {
                        exit ->
                        Wave(
                            start = exit,
                            walkable = this.walkable.difference(place.mask),
                            metric = GridMetric.KING
                        )
                            .closestTile {
                                allExits.contains(it)
                                    && !place.exits.contains(it)
                            }
                            ?.let { NeighborConnection(exit, it) }
                    }
                }
                .filterNotNull()
        val connectionGraph: UndirectedGraph<GridPlace, NeighborConnection> =
            SimpleGraphWithoutAutoEdges<GridPlace, NeighborConnection>()
                .apply {
                    connections.forEach {
                        tryConnecting(it, exitsToPlaces)
                    }
                }
        graph = SimpleGraphWithoutAutoEdges<GridPlace, GridSegment>()
            .apply {
                connectionGraph.vertices.forEach {
                    addVertex(it)
                }
                connectionGraph.edges.forEach {
                    addEdge(
                        connectionGraph.getEdgeSource(it),
                        connectionGraph.getEdgeTarget(it),
                        GridSegment(it.source, it.destination.tile)
                    )
                }
            }
            .let { UnmodifiableUndirectedGraph(it) }
    }

    private fun UndirectedGraph<GridPlace, NeighborConnection>.tryConnecting(
        it: NeighborConnection,
        exitsToPlaces: Map<Tile, GridPlace>
    ) {
        val v1 = exitsToPlaces[it.source]
        val v2 = exitsToPlaces[it.destination.tile]
        addVertex(v1)
        addVertex(v2)
        if (!containsEdge(v1, v2)) {
            addEdge(v1, v2, it)
        } else if (getEdge(v1, v2).length > it.length) {
            removeEdge(v1, v2)
            addEdge(v1, v2, it)
        }
    }
}

fun InterplaceNetwork.connections(): Set<GridSegment> =
    graph.edges

fun InterplaceNetwork.places(): Set<GridPlace> =
    graph.vertices

fun InterplaceNetwork.paths(): List<GridPath> =
    connections()
        .map { walkable.path(it.start, it.end) }
        .filterNotNull()

private data class NeighborConnection(
    val source: Tile,
    val destination: DestinationTile
) {
    val length: Int = destination.distance
}
