package com.gcaguilar.marsrover.domain.models

data class ExplorationArea(
    val topRight: Coordinate,
) {
    val topLeft = Coordinate(x = -topRight.x, y = topRight.y)
    val bottomLeft = Coordinate(x = -topRight.x, y = -topRight.y)
    val bottomRight = Coordinate(x = topRight.x, y = -topRight.y)

    fun isInsideExplorationArea(coordinate: Coordinate): Boolean {
        return (coordinate.x in topLeft.x..topRight.x &&
                coordinate.y in bottomLeft.y..topRight.y)
    }
}

