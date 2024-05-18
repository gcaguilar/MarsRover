package com.gcaguilar.marsrover.data

import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.models.ExplorationArea
import com.gcaguilar.marsrover.domain.models.MarsRover

class MarsMissionMemoryCache {
    private var memoryMarsRover: MarsRover? = null
    private var memoryExplorationArea: ExplorationArea? = null
    private var movements: List<Command> = emptyList()

    fun initializeMission(
        marsRover: MarsRover,
        explorationArea: ExplorationArea,
        inputMovements: List<Command>?
    ) {
        memoryMarsRover = marsRover
        memoryExplorationArea = explorationArea
        movements = inputMovements ?: emptyList()
    }

    fun getMarsRover(): MarsRover {
        return memoryMarsRover ?: throw IllegalStateException("MarsRover not initialized")
    }

    fun getExplorationArea(): ExplorationArea {
        return memoryExplorationArea
            ?: throw IllegalStateException("ExplorationArea not initialized")
    }

    fun getInstructions() = movements
}