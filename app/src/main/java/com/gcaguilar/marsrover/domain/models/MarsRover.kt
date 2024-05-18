package com.gcaguilar.marsrover.domain.models

private const val MovementSize = 1
private val initialPosition = Coordinate(1, 3)
private val initialOrientation = Orientation.NORTH

data class MarsRover(
    private var position: Coordinate = initialPosition,
    private var orientation: Orientation = initialOrientation
) {
    val currentPosition: Coordinate
        get() = position

    val currentOrientation: Orientation
        get() = orientation

    fun rotateLeft() {
        orientation = when (currentOrientation) {
            Orientation.NORTH -> Orientation.WEST
            Orientation.EAST -> Orientation.NORTH
            Orientation.SOUTH -> Orientation.EAST
            Orientation.WEST -> Orientation.SOUTH
        }
    }

    fun rotateRight() {
        orientation = when (currentOrientation) {
            Orientation.NORTH -> Orientation.EAST
            Orientation.EAST -> Orientation.SOUTH
            Orientation.SOUTH -> Orientation.WEST
            Orientation.WEST -> Orientation.NORTH
        }
    }

    fun calculateNewPosition(): Coordinate {
        return when (currentOrientation) {
            Orientation.NORTH -> position.copy(y = position.y + MovementSize)
            Orientation.SOUTH -> position.copy(y = position.y - MovementSize)
            Orientation.EAST -> position.copy(x = position.x + MovementSize)
            Orientation.WEST -> position.copy(x = position.x - MovementSize)
        }
    }

    fun move(coordinate: Coordinate) {
        position = coordinate
    }
}
