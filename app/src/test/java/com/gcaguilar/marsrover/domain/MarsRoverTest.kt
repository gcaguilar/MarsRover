package com.gcaguilar.marsrover.domain

import com.gcaguilar.marsrover.domain.models.Coordinate
import com.gcaguilar.marsrover.domain.models.MarsRover
import com.gcaguilar.marsrover.domain.models.Orientation
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class MarsRoverTest {
    private lateinit var defaultMarsRover: MarsRover

    @Before
    fun setUp() {
        defaultMarsRover = MarsRover(
            position = Coordinate(1, 3)
        )
    }

    @Test
    fun `Should orientation be east when turn right given a mars rover with north orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.EAST)

        defaultMarsRover.rotateRight()

        assertEquals(exceptedMars, defaultMarsRover)
    }

    @Test
    fun `Should orientation be west when turn left given a mars rover with north orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.WEST)

        defaultMarsRover.rotateLeft()

        assertEquals(exceptedMars, defaultMarsRover)
    }

    @Test
    fun `Should orientation be south when turn right given a mars rover east north orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.SOUTH)
        val otherMarsRover = defaultMarsRover.copy(orientation = Orientation.EAST)

        otherMarsRover.rotateRight()

        assertEquals(exceptedMars, otherMarsRover)
    }

    @Test
    fun `Should orientation be north when turn left given a mars rover east orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.NORTH)
        val otherMarsRover = defaultMarsRover.copy(orientation = Orientation.EAST)

        otherMarsRover.rotateLeft()

        assertEquals(exceptedMars, otherMarsRover)
    }

    @Test
    fun `Should orientation be west when turn right given a mars rover south orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.WEST)
        val otherMarsRover = defaultMarsRover.copy(orientation = Orientation.SOUTH)

        otherMarsRover.rotateRight()

        assertEquals(exceptedMars, otherMarsRover)
    }

    @Test
    fun `Should orientation be east when turn left given a mars rover south orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.EAST)
        val otherMarsRover = defaultMarsRover.copy(orientation = Orientation.SOUTH)

        otherMarsRover.rotateLeft()

        assertEquals(exceptedMars, otherMarsRover)
    }

    @Test
    fun `Should orientation be north when turn left given a mars rover west orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.NORTH)
        val otherMarsRover = defaultMarsRover.copy(orientation = Orientation.WEST)

        otherMarsRover.rotateRight()

        assertEquals(exceptedMars, otherMarsRover)
    }

    @Test
    fun `Should orientation be south when turn left given a mars rover west orientation`() {
        val exceptedMars = defaultMarsRover.copy(orientation = Orientation.SOUTH)
        val otherMarsRover = defaultMarsRover.copy(orientation = Orientation.WEST)

        otherMarsRover.rotateLeft()

        assertEquals(exceptedMars, otherMarsRover)
    }
}