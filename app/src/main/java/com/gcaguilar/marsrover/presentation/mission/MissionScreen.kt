package com.gcaguilar.marsrover.presentation.mission

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gcaguilar.marsrover.R
import com.gcaguilar.marsrover.ui.BackPressHandler
import com.gcaguilar.marsrover.ui.ButtonWithIcon
import com.gcaguilar.marsrover.ui.CustomTopAppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val MISSION = "Mission"
const val FROM_FILE_ARGUMENT_KEY = "fromFile"
private const val START_MISSION_KEY = "StartMission"

@Composable
fun MissionScreen(
    navHostController: NavHostController,
    fromFile: Boolean,
    viewModel: MissionViewModel = koinViewModel()
) {
    val state = viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.mission),
                onBackClicked = {
                    viewModel.onBackClicked()
                })
        },
        content = {
            missionController(
                modifier = Modifier.padding(it),
                movements = state.value.movements,
                onTurnLeft = viewModel::turnLeft,
                onTurnRight = viewModel::turnRight,
                onMove = viewModel::move
            )
        }
    )

    BackPressHandler(onBackPressed = {
        viewModel.onBackClicked()
    })

    if (fromFile) {
        LaunchedEffect(key1 = START_MISSION_KEY) {
            viewModel.startMissionFromFile()
        }
    }

    state.value.navigationEvents?.let {
        navHostController.popBackStack()
    }

    state.value.errorEvents?.let {
        scope.launch {
            snackbarHostState.showSnackbar(resources.getString(R.string.movement_out_ouf_mission_area))
        }
        viewModel.processError()
    }
}

@Composable
private fun missionController(
    movements: List<String>,
    onTurnLeft: () -> Unit,
    onTurnRight: () -> Unit,
    onMove: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(movements) { movement ->
                Text(text = movement)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ButtonWithIcon(
                onClick = onTurnRight,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                text = stringResource(id = R.string.turn_right)
            )
            ButtonWithIcon(
                onClick = onTurnLeft,
                icon = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                text = stringResource(id = R.string.turn_left)
            )
            ButtonWithIcon(
                onClick = onMove,
                icon = Icons.Filled.KeyboardArrowUp,
                text = stringResource(id = R.string.move)
            )
        }
    }
}