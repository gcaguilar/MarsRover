package com.gcaguilar.marsrover.data

import com.gcaguilar.marsrover.domain.MarsMissionControlRepository
import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.models.ExplorationArea
import com.gcaguilar.marsrover.domain.models.MarsRover

class MarsMissionControlRepositoryImpl(
    private val marsMissionMemoryCache: MarsMissionMemoryCache
) : MarsMissionControlRepository {
    override fun initializeMission(
        marsRover: MarsRover,
        explorationArea: ExplorationArea,
        movements: List<Command>?
    ) {
        marsMissionMemoryCache.initializeMission(marsRover, explorationArea, movements)
    }

    override fun getRover(): MarsRover = marsMissionMemoryCache.getMarsRover()

    override fun getExplorationArea(): ExplorationArea = marsMissionMemoryCache.getExplorationArea()
    override fun getInstructions(): List<Command> {
        return marsMissionMemoryCache.getInstructions()
    }
}