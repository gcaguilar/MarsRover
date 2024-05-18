package com.gcaguilar.marsrover.presentation

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurator
import com.gcaguilar.marsrover.presentation.file.FileMissionScreen
import com.gcaguilar.marsrover.presentation.file.FileMissionViewModel
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
class FileMissionScreenshotTest {
    @get:Rule
    val coroutineRule = MainCoroutineRule()

    @get:Rule
    val composeTestRule = createScreenshotTestComposeRule()

    @get:Rule
    val roborazziRule = createRoborazziRule(composeTestRule)

    private val missionConfigurator: MissionConfigurator = mockk(relaxed = true)
    private lateinit var viewModel: FileMissionViewModel

    @Before
    fun setUp() {
        viewModel = FileMissionViewModel(
            missionConfigurator = missionConfigurator
        )
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `Should render screen with start mission disable when initialize screen`() {
        renderScreen()
    }

    private fun renderScreen() {
        composeTestRule.setContent {
            AppTheme {
                FileMissionScreen(
                    viewModel = viewModel,
                    navHostController = mockk()
                )
            }
        }
    }
}