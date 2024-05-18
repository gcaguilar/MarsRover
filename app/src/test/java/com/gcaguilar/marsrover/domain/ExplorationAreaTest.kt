package com.gcaguilar.marsrover.domain

import com.gcaguilar.marsrover.domain.models.Coordinate
import com.gcaguilar.marsrover.domain.models.ExplorationArea
import junit.framework.TestCase
import org.junit.Assert
import org.junit.Test

private val defaultTopRightCorner = Coordinate(5, 5)

class ExplorationAreaTest {
    @Test
    fun `Should validate the exploration area values given a top right position`() {
        val expectedTopLeftCorner = Coordinate(-defaultTopRightCorner.x, defaultTopRightCorner.y)
        val expectedBottomLeftCorner =
            Coordinate(-defaultTopRightCorner.x, -defaultTopRightCorner.y)
        val expectedBottomRightCorner =
            Coordinate(defaultTopRightCorner.x, -defaultTopRightCorner.y)

        val explorationArea = ExplorationArea(
            Coordinate(
                x = defaultTopRightCorner.x,
                y = defaultTopRightCorner.y
            )
        )

        TestCase.assertEquals(expectedTopLeftCorner, explorationArea.topLeft)
        TestCase.assertEquals(expectedBottomLeftCorner, explorationArea.bottomLeft)
        TestCase.assertEquals(expectedBottomRightCorner, explorationArea.bottomRight)
    }

    @Test
    fun `Should validate the exploration area values given another top right position`() {
        val otherTopRightCorner = Coordinate(30, 30)
        val expectedTopLeftCorner = Coordinate(-otherTopRightCorner.x, otherTopRightCorner.y)
        val expectedBottomLeftCorner =
            Coordinate(-otherTopRightCorner.x, -otherTopRightCorner.y)
        val expectedBottomRightCorner =
            Coordinate(otherTopRightCorner.x, -otherTopRightCorner.y)

        val explorationArea = ExplorationArea(
            Coordinate(
                x = otherTopRightCorner.x,
                y = otherTopRightCorner.y
            )
        )

        TestCase.assertEquals(expectedTopLeftCorner, explorationArea.topLeft)
        TestCase.assertEquals(expectedBottomLeftCorner, explorationArea.bottomLeft)
        TestCase.assertEquals(expectedBottomRightCorner, explorationArea.bottomRight)
    }

    @Test
    fun `Should return the position is inside exploration area given a valid position`() {
        val explorationArea = ExplorationArea(
            Coordinate(
                x = defaultTopRightCorner.x,
                y = defaultTopRightCorner.y
            )
        )
        val result = explorationArea.isInsideExplorationArea(
            Coordinate(
                x = 1,
                y = 3,
            )
        )

        TestCase.assertTrue(result)
    }

    @Test
    fun `Should return the position is inside exploration area given a invalid position`() {
        val explorationArea = ExplorationArea(
            Coordinate(
                x = defaultTopRightCorner.x,
                y = defaultTopRightCorner.y
            )
        )
        val result = explorationArea.isInsideExplorationArea(
            Coordinate(
                x = -10,
                y = 3,
            )
        )

        Assert.assertFalse(result)
    }
}