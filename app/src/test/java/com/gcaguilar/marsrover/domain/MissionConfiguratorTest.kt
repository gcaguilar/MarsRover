package com.gcaguilar.marsrover.domain

import com.gcaguilar.marsrover.domain.models.Orientation
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurationFromFileResult
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurationResult
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurator
import io.mockk.mockk
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.io.ByteArrayInputStream


class MissionConfiguratorTest {
    private val missionControl: MarsMissionControlRepository = mockk(relaxed = true)
    private lateinit var missionConfigurator: MissionConfigurator

    @Before
    fun setUp() {
        missionConfigurator = MissionConfigurator(
            missionControl
        )
    }

    @Test
    fun `Should return mission configuration success given a stream with valid rover position`() {
        val validInputStream = " {\n" +
                "\"topRightCorner\": {\n" +
                "\"x\" : 5, \"y\" : 5 },\n" +
                "\"roverPosition\": {\n" +
                "\"x\" : 5, \"y\" : 5 }, \"roverDirection\": \"N\",\n" +
                "\"movements\" : \"LMLMLMLMM\" }\n"
        val inputStream = ByteArrayInputStream(validInputStream.toByteArray())

        val result = missionConfigurator.configure(inputStream)

        assertEquals(MissionConfigurationResult.Success, result)
    }

    @Test
    fun `Should return mission configuration invalid rover position given a stream with invalid rover position`() {
        val validInputStream = " {\n" +
                "\"topRightCorner\": {\n" +
                "\"x\" : 1, \"y\" : 1 },\n" +
                "\"roverPosition\": {\n" +
                "\"x\" : 5, \"y\" : 5 }, \"roverDirection\": \"N\",\n" +
                "\"movements\" : \"LMLMLMLMM\" }\n"
        val inputStream = ByteArrayInputStream(validInputStream.toByteArray())

        val result = missionConfigurator.configure(inputStream)

        assertEquals(MissionConfigurationResult.InvalidRoverPosition, result)
    }

    @Test
    fun `Should return mission configuration invalid file given a stream with missing fields`() {
        val validInputStream = " {\n" +
                "\"topRightCorner\": {\n" +
                "\"x\" : 1, \"y\" : 1 },\n" +
                "\"roverPosition\": {\n" +
                "\"x\" : 5, \"y\" : 5 }, \"roverDirection\": \"N\", }"
        val inputStream = ByteArrayInputStream(validInputStream.toByteArray())

        val result = missionConfigurator.configure(inputStream)

        assertEquals(MissionConfigurationFromFileResult.InvalidFile, result)
    }

    @Test
    fun `Should return mission configuration success given a valid coordinates`() {
        val x = 1
        val y = 2
        val topXright = 5
        val topYright = 5
        val orientation = Orientation.NORTH
        val result = missionConfigurator.configure(
            marsInitialPositionX = x,
            marsInitialPositionY = y,
            marsInitialOrientation = orientation,
            topRightCornerX = topXright,
            topRightCornerY = topYright,
        )

        assertEquals(MissionConfigurationResult.Success, result)
    }

    @Test
    fun `Should return mission configuration invalid rover position given mars coordinates out of exploration area`() {
        val x = 5
        val y = 5
        val topXright = 1
        val topYright = 2
        val orientation = Orientation.NORTH
        val result = missionConfigurator.configure(
            marsInitialPositionX = x,
            marsInitialPositionY = y,
            marsInitialOrientation = orientation,
            topRightCornerX = topXright,
            topRightCornerY = topYright,
        )

        assertEquals(MissionConfigurationResult.InvalidRoverPosition, result)
    }

}