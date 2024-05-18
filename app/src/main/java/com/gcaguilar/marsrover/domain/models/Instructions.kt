package com.gcaguilar.marsrover.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class Instructions(
    val roverPosition: RoverPosition,
    val topRightCorner: TopRightCorner,
    val movements: String,
    val roverDirection: String
)

@Serializable
data class TopRightCorner(
    val x: Int,
    val y: Int
)

@Serializable
data class RoverPosition(
    val x: Int,
    val y: Int
)
