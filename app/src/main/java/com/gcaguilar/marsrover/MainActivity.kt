package com.gcaguilar.marsrover

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.gcaguilar.marsrover.presentation.file.FILE_MISSION
import com.gcaguilar.marsrover.presentation.file.FileMissionScreen
import com.gcaguilar.marsrover.presentation.manual.InputMissionScreen
import com.gcaguilar.marsrover.presentation.manual.MANUAL_MISSION
import com.gcaguilar.marsrover.presentation.mission.FROM_FILE_ARGUMENT_KEY
import com.gcaguilar.marsrover.presentation.mission.MISSION
import com.gcaguilar.marsrover.presentation.mission.MissionScreen
import com.gcaguilar.marsrover.presentation.selector.MISSION_SELECTOR
import com.gcaguilar.marsrover.presentation.selector.MissionSelectorScreen
import com.gcaguilar.marsrover.ui.theme.AppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            AppTheme {
                NavHost(navController = navController, startDestination = MISSION_SELECTOR) {
                    composable(MISSION_SELECTOR) {
                        MissionSelectorScreen(
                            navHostController = navController,
                        )
                    }
                    composable(FILE_MISSION) {
                        FileMissionScreen(
                            navHostController = navController,
                        )
                    }
                    composable(
                        route = "${MISSION}?fromFile={fromFile}",
                        arguments = listOf(navArgument(FROM_FILE_ARGUMENT_KEY) {
                            type = NavType.BoolType
                        })
                    ) { backStackEntry ->
                        val comeFromFile = backStackEntry.arguments?.getBoolean("fromFile") ?: false
                        MissionScreen(
                            navHostController = navController,
                            fromFile = comeFromFile
                        )
                    }
                    composable(MANUAL_MISSION) {
                        InputMissionScreen(
                            navHostController = navController,
                        )
                    }
                }
            }
        }
    }
}
