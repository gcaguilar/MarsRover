package com.gcaguilar.marsrover.presentation

import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurator
import com.gcaguilar.marsrover.presentation.manual.InputMissionScreen
import com.gcaguilar.marsrover.presentation.manual.InputMissionViewModel
import com.gcaguilar.marsrover.testrules.MainCoroutineRule
import com.gcaguilar.marsrover.testrules.createRoborazziRule
import com.gcaguilar.marsrover.testrules.createScreenshotTestComposeRule
import com.gcaguilar.marsrover.ui.theme.AppTheme
import com.github.takahirom.roborazzi.RobolectricDeviceQualifiers
import io.mockk.mockk
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
class InputMissionScreenshotTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val composeTestRule = createScreenshotTestComposeRule()

    @get:Rule
    val roborazziRule = createRoborazziRule(composeTestRule)

    private val missionConfigurator: MissionConfigurator = mockk(relaxed = true)
    private lateinit var viewModel: InputMissionViewModel

    @Before
    fun setUp() {
        viewModel = InputMissionViewModel(
            missionConfigurator = missionConfigurator
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Should render screen with empty fields and disabled start mission button when initialize`() {
        renderScreen()
    }

    @Test
    fun `Should render screen with disable button when introduce mars rover x coordinate`() {
        renderScreen()
        composeTestRule.onNodeWithText("X initial Rover position").performTextInput("1")

    }

    @Test
    fun `Should render screen with disable button when introduce mars rover x and y coordinate`() {
        renderScreen()
        composeTestRule.onNodeWithText("X initial Rover position").performTextInput("1")
        composeTestRule.onNodeWithText("Y initial Rover position").performTextInput("1")

    }

    @Test
    fun `Should render screen with disable button when introduce mars rover x and y coordinate and x exploration area`() {
        renderScreen()
        composeTestRule.onNodeWithText("X initial Rover position").performTextInput("1")
        composeTestRule.onNodeWithText("Y initial Rover position").performTextInput("1")
        composeTestRule.onNodeWithText("X topRightCorner").performTextInput("1")
    }

    @Test
    fun `Should render screen with enable button when all coordinates are introduced`() {
        renderScreen()
        composeTestRule.onNodeWithText("X initial Rover position").performTextInput("1")
        composeTestRule.onNodeWithText("Y initial Rover position").performTextInput("1")
        composeTestRule.onNodeWithText("X topRightCorner").performTextInput("1")
        composeTestRule.onNodeWithText("Y topRightCorner").performTextInput("1")
    }


    private fun renderScreen() {
        composeTestRule.setContent {
            AppTheme {
                InputMissionScreen(
                    viewModel = viewModel,
                    navHostController = mockk()
                )
            }
        }
    }
}