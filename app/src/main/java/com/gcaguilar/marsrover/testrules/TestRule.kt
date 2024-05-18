package com.gcaguilar.marsrover.testrules

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onRoot
import com.github.takahirom.roborazzi.RoborazziOptions
import com.github.takahirom.roborazzi.RoborazziRule

fun createRoborazziRule(composeTestRule: AndroidComposeTestRule<*, *>): RoborazziRule =
    RoborazziRule(
        composeRule = composeTestRule,
        captureRoot = composeTestRule.onRoot(),
        options = RoborazziRule.Options(
            outputDirectoryPath = "screenshots",
            roborazziOptions = RoborazziOptions(
                recordOptions = RoborazziOptions.RecordOptions(
                    resizeScale = 0.5
                ),
                compareOptions = RoborazziOptions.CompareOptions(
                    changeThreshold = 0.01F
                )
            ),
            captureType = RoborazziRule.CaptureType.LastImage(onlyFail = false)
        )
    )

fun createScreenshotTestComposeRule() = createAndroidComposeRule<ComponentActivity>()
