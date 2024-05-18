package com.gcaguilar.marsrover.presentation.file

import androidx.lifecycle.ViewModel
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurationFromFileResult
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurationResult
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.io.InputStream

data class FileMissionUIState(
    val errorEvent: FileMissionErrorEvent? = null,
    val isInputFilled: Boolean = false,
    val navigationEvent: FileMissionNavigationEvent? = null
) : ViewModel() {
    val isReadyToStart = isInputFilled && errorEvent == null
}

sealed class FileMissionNavigationEvent {
    data object ToBack : FileMissionNavigationEvent()
    data object ToStartMission : FileMissionNavigationEvent()
}

sealed class FileMissionErrorEvent {
    data object InvalidFile : FileMissionErrorEvent()
    data object InvalidRoverPosition : FileMissionErrorEvent()
}

class FileMissionViewModel(
    private val missionConfigurator: MissionConfigurator
) : ViewModel() {
    private val _uiState = MutableStateFlow(FileMissionUIState())
    val uiState: StateFlow<FileMissionUIState> = _uiState.asStateFlow()

    fun onBackClicked() {
        _uiState.update {
            uiState.value.copy(
                navigationEvent = FileMissionNavigationEvent.ToBack
            )
        }
    }

    fun onStartMissionClicked() {
        _uiState.update {
            uiState.value.copy(
                navigationEvent = FileMissionNavigationEvent.ToStartMission
            )
        }
    }

    fun selectedFile(stream: InputStream?) {
        stream?.let {
            when (missionConfigurator.configure(it)) {
                MissionConfigurationResult.InvalidRoverPosition -> _uiState.update {
                    uiState.value.copy(
                        errorEvent = FileMissionErrorEvent.InvalidRoverPosition
                    )
                }

                MissionConfigurationFromFileResult.InvalidFile -> {
                    _uiState.update {
                        uiState.value.copy(
                            errorEvent = FileMissionErrorEvent.InvalidFile
                        )
                    }
                }

                MissionConfigurationResult.Success -> {
                    _uiState.update {
                        uiState.value.copy(
                            isInputFilled = true
                        )
                    }
                }
            }
        }
    }

    fun processNavigation() {
        _uiState.update {
            uiState.value.copy(
                navigationEvent = null
            )
        }
    }

    fun processError() {
        _uiState.update {
            uiState.value.copy(
                errorEvent = null
            )
        }
    }
}