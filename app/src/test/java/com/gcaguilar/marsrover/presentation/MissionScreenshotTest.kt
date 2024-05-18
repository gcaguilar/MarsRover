package com.gcaguilar.marsrover.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.models.Coordinate
import com.gcaguilar.marsrover.domain.usecase.MissionExecutor
import com.gcaguilar.marsrover.domain.usecase.MissionInformation
import com.gcaguilar.marsrover.presentation.mission.MissionScreen
import com.gcaguilar.marsrover.presentation.mission.MissionViewModel
import com.gcaguilar.marsrover.testrules.MainCoroutineRule
import com.gcaguilar.marsrover.testrules.createRoborazziRule
import com.gcaguilar.marsrover.testrules.createScreenshotTestComposeRule
import com.gcaguilar.marsrover.ui.theme.AppTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.core.context.GlobalContext.stopKoin
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(AndroidJUnit4::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(qualifiers = RobolectricDeviceQualifiers.Pixel5)
class MissionScreenshotTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val composeTestRule = createScreenshotTestComposeRule()

    @get:Rule
    val roborazziRule = createRoborazziRule(composeTestRule)

    private val missionExecutor: MissionExecutor = mockk(relaxed = true)
    private lateinit var viewModel: MissionViewModel

    @Before
    fun setUp() {
        viewModel = MissionViewModel(
            missionExecutor = missionExecutor
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Should render screen with instructions when initialize coming from file`() {
        coEvery { missionExecutor() } returns flowOf(
            MissionInformation(
                currentPosition = Coordinate(1, 2),
                currentOrientation = "North"
            )
        )

        renderFromFileScreen()
    }

    @Test
    fun `Should render screen without instructions when initialize coming from manual input`() {
        coEvery { missionExecutor(Command.L) } returns flowOf(
            MissionInformation(
                currentPosition = Coordinate(1, 2),
                currentOrientation = "North"
            )
        )

        renderScreen()
    }

    private fun renderFromFileScreen() {
        composeTestRule.setContent {
            AppTheme {
                MissionScreen(
                    viewModel = viewModel,
                    fromFile = true,
                    navHostController = mockk()
                )
            }
        }
    }

    private fun renderScreen() {
        composeTestRule.setContent {
            AppTheme {
                MissionScreen(
                    viewModel = viewModel,
                    fromFile = false,
                    navHostController = mockk()
                )
            }
        }
    }
}