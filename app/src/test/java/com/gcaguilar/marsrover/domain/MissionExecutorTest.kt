package com.gcaguilar.marsrover.domain

import app.cash.turbine.test
import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.models.Coordinate
import com.gcaguilar.marsrover.domain.models.ExplorationArea
import com.gcaguilar.marsrover.domain.models.MarsRover
import com.gcaguilar.marsrover.domain.models.Orientation
import com.gcaguilar.marsrover.domain.usecase.MissionExecutor
import com.gcaguilar.marsrover.domain.usecase.MissionInformation
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Test

class MissionExecutorTest {
    private val marsMissionControlRepository: MarsMissionControlRepository = mockk(relaxed = true)
    private lateinit var missionExecutor: MissionExecutor

    @Test
    fun `Should return 13N as final position given 12 as initial point and some orders`() =
        runBlocking {
            val marsRover = MarsRover(Coordinate(1, 2))
            val explorationArea = ExplorationArea(Coordinate(5, 5))
            every { marsMissionControlRepository.getRover() } returns marsRover
            every { marsMissionControlRepository.getExplorationArea() } returns explorationArea
            every { marsMissionControlRepository.getInstructions() } returns listOf(
                Command.L,
                Command.M,
                Command.L,
                Command.M,
                Command.L,
                Command.M,
                Command.L,
                Command.M,
                Command.M
            )
            missionExecutor = MissionExecutor(marsMissionControlRepository)

            missionExecutor().test {
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 2),
                        currentOrientation = Orientation.WEST.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(0, 2),
                        currentOrientation = Orientation.WEST.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(0, 2),
                        currentOrientation = Orientation.SOUTH.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(0, 1),
                        currentOrientation = Orientation.SOUTH.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(0, 1),
                        currentOrientation = Orientation.EAST.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 1),
                        currentOrientation = Orientation.EAST.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 1),
                        currentOrientation = Orientation.NORTH.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 2),
                        currentOrientation = Orientation.NORTH.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 3),
                        currentOrientation = Orientation.NORTH.name
                    ), awaitItem()
                )
                awaitComplete()
            }
        }

    @Test
    fun `Should throw exception given 12 as initial point and some orders that it is driven off the map`() =
        runBlocking {
            val marsRover = MarsRover(Coordinate(1, 2))
            val explorationArea = ExplorationArea(Coordinate(5, 5))
            every { marsMissionControlRepository.getRover() } returns marsRover
            every { marsMissionControlRepository.getExplorationArea() } returns explorationArea
            every { marsMissionControlRepository.getInstructions() } returns listOf(
                Command.M,
                Command.M,
                Command.M,
                Command.M
            )
            missionExecutor = MissionExecutor(marsMissionControlRepository)

            missionExecutor().test {
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 3),
                        currentOrientation = Orientation.NORTH.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 4),
                        currentOrientation = Orientation.NORTH.name
                    ), awaitItem()
                )
                assertEquals(
                    MissionInformation(
                        currentPosition = Coordinate(1, 5),
                        currentOrientation = Orientation.NORTH.name
                    ), awaitItem()
                )
                awaitError()
            }
        }
}