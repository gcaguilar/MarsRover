package com.gcaguilar.marsrover.domain.usecase

import com.gcaguilar.marsrover.domain.MarsMissionControlRepository
import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.models.Coordinate
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MissionExecutor(
    private val marsMissionControlRepository: MarsMissionControlRepository,
) {
    suspend operator fun invoke(command: Command): Flow<MissionInformation> = flow {
        emit(executeCommand(command))
    }

    suspend operator fun invoke(): Flow<MissionInformation> = flow {
        marsMissionControlRepository.getInstructions().forEach { instruction ->
            emit(executeCommand(instruction))
        }
    }

    private fun executeCommand(command: Command): MissionInformation {
        val marsRover = marsMissionControlRepository.getRover()
        val explorationArea = marsMissionControlRepository.getExplorationArea()
        when (command) {
            Command.M -> {
                val newPosition = marsRover.calculateNewPosition()
                if (explorationArea.isInsideExplorationArea(newPosition)) {
                    marsRover.move(newPosition)
                } else {
                    throw IllegalArgumentException("Action outside the scanning range")
                }
            }

            Command.L -> marsRover.rotateLeft()
            Command.R -> marsRover.rotateRight()
        }

        return MissionInformation(
            currentPosition = marsRover.currentPosition,
            currentOrientation = marsRover.currentOrientation.name
        )
    }
}

data class MissionInformation(
    val currentPosition: Coordinate,
    val currentOrientation: String
)
