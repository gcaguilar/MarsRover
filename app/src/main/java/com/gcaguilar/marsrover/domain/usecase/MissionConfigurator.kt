package com.gcaguilar.marsrover.domain.usecase

import com.gcaguilar.marsrover.domain.MarsMissionControlRepository
import com.gcaguilar.marsrover.domain.models.Coordinate
import com.gcaguilar.marsrover.domain.models.ExplorationArea
import com.gcaguilar.marsrover.domain.models.Instructions
import com.gcaguilar.marsrover.domain.models.MarsRover
import com.gcaguilar.marsrover.domain.models.Orientation
import com.gcaguilar.marsrover.domain.models.toCommand
import com.gcaguilar.marsrover.domain.models.toOrientation
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import java.io.InputStream

private const val INVALID_POSITION = "Invalid Rover Position"

sealed interface MissionConfigurationResult {
    data object Success : MissionConfigurationResult
    data object InvalidRoverPosition : MissionConfigurationResult
}

sealed class MissionConfigurationFromFileResult : MissionConfigurationResult {
    data object InvalidFile : MissionConfigurationFromFileResult()
}

class MissionConfigurator(
    private val missionControl: MarsMissionControlRepository
) {
    @OptIn(ExperimentalSerializationApi::class)
    fun configure(inputStream: InputStream): MissionConfigurationResult {
        return try {
            val instructions = Json.decodeFromStream<Instructions>(inputStream)
            val explorationArea = initializeExplorationArea(
                x = instructions.topRightCorner.x,
                y = instructions.topRightCorner.y
            )
            val marsRover = initializeRover(
                x = instructions.roverPosition.x,
                y = instructions.roverPosition.y,
                orientation = instructions.roverDirection.toOrientation(),
                explorationArea = explorationArea
            )

            val movements = instructions.movements.toList().map { it.toCommand() }

            missionControl.initializeMission(marsRover, explorationArea, movements)

            MissionConfigurationResult.Success
        } catch (e: Exception) {
            evaluateErrorCause(e)
        }
    }

    fun configure(
        marsInitialPositionX: Int,
        marsInitialPositionY: Int,
        marsInitialOrientation: Orientation,
        topRightCornerX: Int,
        topRightCornerY: Int
    ): MissionConfigurationResult {
        return try {
            val explorationArea = initializeExplorationArea(
                x = topRightCornerX,
                y = topRightCornerY
            )

            val marsRover = initializeRover(
                x = marsInitialPositionX,
                y = marsInitialPositionY,
                orientation = marsInitialOrientation,
                explorationArea = explorationArea
            )

            missionControl.initializeMission(marsRover, explorationArea)
            MissionConfigurationResult.Success
        } catch (e: IllegalArgumentException) {
            MissionConfigurationResult.InvalidRoverPosition
        }
    }

    private fun initializeRover(
        x: Int,
        y: Int,
        orientation: Orientation? = null,
        explorationArea: ExplorationArea
    ): MarsRover {
        if (explorationArea.isInsideExplorationArea(Coordinate(x, y))) {
            return MarsRover(
                Coordinate(
                    x = x,
                    y = y
                ),
                orientation = orientation ?: Orientation.NORTH
            )
        } else {
            throw IllegalArgumentException(INVALID_POSITION)
        }
    }

    private fun initializeExplorationArea(x: Int, y: Int): ExplorationArea {
        return ExplorationArea(
            Coordinate(
                x = x,
                y = y
            )
        )
    }

    private fun evaluateErrorCause(e: Exception): MissionConfigurationResult {
        return if (e is IllegalArgumentException) {
            if (e.message == INVALID_POSITION) {
                MissionConfigurationResult.InvalidRoverPosition
            } else {
                MissionConfigurationFromFileResult.InvalidFile
            }
        } else {
            MissionConfigurationFromFileResult.InvalidFile
        }
    }
}