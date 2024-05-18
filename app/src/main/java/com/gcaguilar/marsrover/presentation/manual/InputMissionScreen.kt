package com.gcaguilar.marsrover.presentation.manual

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gcaguilar.marsrover.R
import com.gcaguilar.marsrover.domain.models.Orientation
import com.gcaguilar.marsrover.presentation.mission.MISSION
import com.gcaguilar.marsrover.ui.CustomTopAppBar
import com.gcaguilar.marsrover.ui.RadioGroupWithSelectable
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

const val MANUAL_MISSION = "manual"

@Composable
fun InputMissionScreen(
    navHostController: NavHostController,
    viewModel: InputMissionViewModel = koinViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalContext.current.resources
    val state = viewModel.uiState.collectAsState()
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            CustomTopAppBar(
                title = stringResource(id = R.string.configure_mission),
                onBackClicked = {
                    navHostController.popBackStack()
                })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            MissionConfigurator(
                modifier = Modifier.weight(1f),
                marsInitialPositionX = state.value.marsInitialPositionX?.toString() ?: "",
                marsInitialPositionY = state.value.marsInitialPositionY?.toString() ?: "",
                topRightCornerX = state.value.topRightCornerX?.toString() ?: "",
                topRightCornerY = state.value.topRightCornerY?.toString() ?: "",
                orientation = state.value.marsInitialOrientation.name,
                onSetMarsXposition = viewModel::setMarsXposition,
                onSetMarsYposition = viewModel::setMarsYposition,
                onSetTopRightXCorner = viewModel::setTopRightXCorner,
                onSetTopRightYCorner = viewModel::setTopRightYCorner,
                onItemSelected = viewModel::setMarsOrientation,
            )

            Button(
                enabled = state.value.isMissionBeAbleToConfigure,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                onClick = {
                    viewModel.configureMission()
                },
                content = {
                    Text(stringResource(R.string.start_mission))
                }
            )
        }
    }

    state.value.navigationEvent?.let {
        when (it) {
            NavigationEventInputMission.ToBack -> navHostController.popBackStack()
            NavigationEventInputMission.ToMission -> navHostController.navigate(
                "${MISSION}?fromFile=false"
            )
        }
        viewModel.processNavigation()
    }

    state.value.errorEventInputMission?.let {
        scope.launch {
            snackbarHostState.showSnackbar(resources.getString(R.string.invalid_rover_position))
        }
        viewModel.processError()
    }
}

@Composable
private fun MissionConfigurator(
    marsInitialPositionX: String,
    marsInitialPositionY: String,
    topRightCornerX: String,
    topRightCornerY: String,
    orientation: String,
    onSetMarsXposition: (String?, Boolean) -> Unit,
    onSetMarsYposition: (String?, Boolean) -> Unit,
    onSetTopRightXCorner: (String?, Boolean) -> Unit,
    onSetTopRightYCorner: (String?, Boolean) -> Unit,
    onItemSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var isXRoverPositionNegative by remember { mutableStateOf(false) }
    var isYRoverPositionNegative by remember { mutableStateOf(false) }
    var isXAreaPositionNegative by remember { mutableStateOf(false) }
    var isYAreaPositionNegative by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = stringResource(R.string.mars_information)
        )
        TextField(
            value = marsInitialPositionX,
            onValueChange = { value -> onSetMarsXposition(value, isXRoverPositionNegative) },
            label = { Text(stringResource(R.string.x_initial_rover_position)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.NumberPassword,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                IconButton(onClick = { isXRoverPositionNegative = !isXRoverPositionNegative }) {
                    Icon(
                        imageVector = if (isXRoverPositionNegative) {
                            ImageVector.vectorResource(id = R.drawable.ic_remove)
                        } else {
                            Icons.Filled.Add
                        },
                        contentDescription = null
                    )
                }
            }
        )
        TextField(
            value = marsInitialPositionY,
            onValueChange = { value -> onSetMarsYposition(value, isYRoverPositionNegative) },
            label = { Text(stringResource(R.string.y_initial_rover_position)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                IconButton(onClick = { isYRoverPositionNegative = !isYRoverPositionNegative }) {
                    Icon(
                        imageVector = if (isYRoverPositionNegative) {
                            ImageVector.vectorResource(id = R.drawable.ic_remove)
                        } else {
                            Icons.Filled.Add
                        },
                        contentDescription = null
                    )
                }
            }
        )

        RadioGroupWithSelectable(
            modifier = Modifier,
            items = Orientation.entries.map { it.name },
            selection = orientation,
            onItemClick = onItemSelected
        )

        Text(text = stringResource(R.string.exploration_area_information))
        TextField(
            value = topRightCornerX,
            onValueChange = { value ->
                onSetTopRightXCorner(
                    value,
                    isXAreaPositionNegative
                )
            },
            label = { Text(stringResource(R.string.x_toprightcorner)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            leadingIcon = {
                IconButton(onClick = {
                    isXAreaPositionNegative = !isXAreaPositionNegative
                }) {
                    Icon(
                        imageVector = if (isXAreaPositionNegative) {
                            ImageVector.vectorResource(id = R.drawable.ic_remove)
                        } else {
                            Icons.Filled.Add
                        },
                        contentDescription = null
                    )
                }
            }
        )
        TextField(
            value = topRightCornerY,
            onValueChange = { value ->
                onSetTopRightYCorner(
                    value,
                    isYAreaPositionNegative
                )
            },
            label = { Text(stringResource(R.string.y_toprightcorner)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            leadingIcon = {
                IconButton(onClick = {
                    isYAreaPositionNegative = !isYAreaPositionNegative
                }) {
                    Icon(
                        imageVector = if (isYAreaPositionNegative) {
                            ImageVector.vectorResource(id = R.drawable.ic_remove)
                        } else {
                            Icons.Filled.Add
                        },
                        contentDescription = null
                    )
                }
            }
        )
    }
}