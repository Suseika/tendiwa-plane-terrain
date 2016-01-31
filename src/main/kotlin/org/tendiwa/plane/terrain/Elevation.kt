package org.tendiwa.plane.terrain

import org.tendiwa.tools.argumentConstraint

data class Elevation private constructor(val height: Int) {
    companion object : (Int) -> Elevation {
        override fun invoke(index: Int): Elevation {
            argumentConstraint(
                index,
                { it in MIN_HEIGHT..MAX_HEIGHT },
                {
                    "Height must be between " +
                        "$MIN_HEIGHT and $MAX_HEIGHT inclusive"
                }

            )
            return heights[index - MIN_HEIGHT]
        }

        val MIN_HEIGHT = 0

        val MAX_HEIGHT = 256

        private val heights: Array<Elevation> = IntRange(MIN_HEIGHT, MAX_HEIGHT)
            .map { Elevation(it) }
            .toTypedArray()
    }

    operator fun compareTo(other: Elevation) : Int =
        this.height - other.height
}
