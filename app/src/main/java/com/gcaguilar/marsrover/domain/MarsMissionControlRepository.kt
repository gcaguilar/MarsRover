package com.gcaguilar.marsrover.domain

import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.models.ExplorationArea
import com.gcaguilar.marsrover.domain.models.MarsRover

interface MarsMissionControlRepository {
    fun initializeMission(
        marsRover: MarsRover,
        explorationArea: ExplorationArea,
        movements: List<Command>? = null
    )

    fun getRover(): MarsRover
    fun getExplorationArea(): ExplorationArea

    fun getInstructions(): List<Command>
}