package com.gcaguilar.marsrover.domain.models

enum class Orientation {
    NORTH, SOUTH, EAST, WEST;
}

fun String.toOrientation(): Orientation {
    return when (this) {
        "L" -> Orientation.WEST
        "R" -> Orientation.EAST
        "N" -> Orientation.NORTH
        "S" -> Orientation.SOUTH
        else -> throw IllegalArgumentException("Invalid orientation")
    }
}