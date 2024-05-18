package com.gcaguilar.marsrover.presentation.file

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.gcaguilar.marsrover.R
import com.gcaguilar.marsrover.presentation.mission.MISSION
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.io.InputStream

const val FILE_MISSION = "FileMission"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileMissionScreen(
    navHostController: NavHostController,
    viewModel: FileMissionViewModel = koinViewModel()
) {
    val contenxt = LocalView.current.context
    val state = viewModel.uiState.collectAsState()
    val openDocumentLauncher: ActivityResultLauncher<Array<String>> =
        rememberLauncherForActivityResult(ActivityResultContracts.OpenDocument()) { uri: Uri? ->
            viewModel.selectedFile(funObtainStream(uri, contenxt))
        }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val resources = LocalContext.current.resources

    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.file_mission_title))
                },
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.onBackClicked()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back)
                        )
                    }
                })
        },
        content = {
            Column(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(
                    onClick = {
                        // TODO API 21 application/json can't select JSON
                        openDocumentLauncher.launch(arrayOf("*/*"))
                    },
                    content = {
                        Text(stringResource(R.string.choose_your_file))
                    }
                )

                Spacer(modifier = Modifier.height(16.dp))
                Log.d("Test", state.value.isReadyToStart.toString())
                Button(
                    enabled = state.value.isReadyToStart,
                    onClick = {
                        viewModel.onStartMissionClicked()
                    },
                    content = {
                        Text(text = stringResource(id = R.string.start_mission))
                    }
                )
            }
        })

    state.value.navigationEvent?.let {
        when (it) {
            FileMissionNavigationEvent.ToBack -> navHostController.popBackStack()
            FileMissionNavigationEvent.ToStartMission -> navHostController.navigate(
                "${MISSION}?fromFile=true"
            )
        }
        viewModel.processNavigation()
    }

    state.value.errorEvent?.let {
        val errorMessage = when (it) {
            FileMissionErrorEvent.InvalidFile -> resources.getString(R.string.invalid_file)
            FileMissionErrorEvent.InvalidRoverPosition -> resources.getString(R.string.invalid_rover_position)
        }
        scope.launch {
            snackbarHostState.showSnackbar(errorMessage)
        }
        viewModel.processError()
    }
}

private fun funObtainStream(
    uri: Uri?,
    context: Context
): InputStream? {
    return uri?.let {
        context.contentResolver.openInputStream(it)
    }
}