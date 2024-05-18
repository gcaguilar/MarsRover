package com.gcaguilar.marsrover.presentation.mission

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gcaguilar.marsrover.domain.models.Command
import com.gcaguilar.marsrover.domain.usecase.MissionExecutor
import com.gcaguilar.marsrover.domain.usecase.MissionInformation
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed class MissionErrors {
    data object InvalidMovement : MissionErrors()
}

sealed class MissionEventNavigation {
    data object ToBack : MissionEventNavigation()
}

data class MissionUiState(
    val movements: List<String> = emptyList(),
    val navigationEvents: MissionEventNavigation? = null,
    val errorEvents: MissionErrors? = null
)

class MissionViewModel(
    private val missionExecutor: MissionExecutor
) : ViewModel() {
    private val _uiState: MutableStateFlow<MissionUiState> =
        MutableStateFlow(MissionUiState())
    val uiState: StateFlow<MissionUiState> = _uiState.asStateFlow()

    private val commandExceptionHandler = CoroutineExceptionHandler { _, _ ->
        _uiState.update {
            uiState.value.copy(
                errorEvents = MissionErrors.InvalidMovement
            )
        }
    }

    private fun executeMovement(command: Command) {
        viewModelScope.launch(commandExceptionHandler) {
            missionExecutor(command).collect { info ->
                _uiState.update {
                    uiState.value.copy(
                        movements = uiState.value.movements.plus(formattedMovementCoordination(info))
                    )
                }
            }
        }
    }

    fun turnLeft() {
        executeMovement(Command.L)
    }

    fun turnRight() {
        executeMovement(Command.R)
    }

    fun move() {
        executeMovement(Command.M)
    }


    fun startMissionFromFile() {
        viewModelScope.launch {
            missionExecutor().collect { info ->
                _uiState.update {
                    uiState.value.copy(
                        movements = uiState.value.movements.plus(formattedMovementCoordination(info))
                    )
                }
            }
        }
    }

    private fun formattedMovementCoordination(info: MissionInformation): String {
        return "X: ${info.currentPosition.x} Y:${info.currentPosition.y} Orientation:${info.currentOrientation}"
    }

    fun onBackClicked() {
        _uiState.update {
            uiState.value.copy(
                navigationEvents = MissionEventNavigation.ToBack
            )
        }
    }

    fun processError() {
        _uiState.update {
            uiState.value.copy(
                errorEvents = null
            )
        }
    }
}