package org.tendiwa.plane.terrain

import org.tendiwa.math.noise.Noise2D
import org.tendiwa.math.noise.PerlinNoise

/**
 * Heightmap generated from Perlin noise.
 */
data class NoiseHeightmap(val noise: Noise2D) : Heightmap {
    companion object {
        private val SAMPLE_SHIFT =
            PerlinNoise.MAX_VALUE - Elevation.MAX_HEIGHT
    }

    override fun elevation(x: Int, y: Int): Elevation =
        Elevation(
            noise.at(x.toDouble(), y.toDouble()) - SAMPLE_SHIFT
        )
}

