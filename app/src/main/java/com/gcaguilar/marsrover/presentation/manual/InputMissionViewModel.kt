package com.gcaguilar.marsrover.presentation.manual

import androidx.lifecycle.ViewModel
import com.gcaguilar.marsrover.domain.models.Orientation
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurationResult
import com.gcaguilar.marsrover.domain.usecase.MissionConfigurator
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

sealed class NavigationEventInputMission {
    data object ToBack : NavigationEventInputMission()
    data object ToMission : NavigationEventInputMission()
}

sealed class ErrorEventInputMission {
    data object InvalidRoverPosition : ErrorEventInputMission()
}


data class InputMissionConfigurationUIState(
    val marsInitialPositionX: Int? = null,
    val marsInitialPositionY: Int? = null,
    val marsInitialOrientation: Orientation = Orientation.NORTH,
    val topRightCornerX: Int? = null,
    val topRightCornerY: Int? = null,
    val navigationEvent: NavigationEventInputMission? = null,
    val errorEventInputMission: ErrorEventInputMission? = null
) {
    val isMissionBeAbleToConfigure: Boolean =
        (marsInitialPositionX != null && marsInitialPositionY != null &&
                topRightCornerX != null && topRightCornerY != null)
}

class InputMissionViewModel(
    private val missionConfigurator: MissionConfigurator,
) : ViewModel() {
    private val _uiState: MutableStateFlow<InputMissionConfigurationUIState> =
        MutableStateFlow(InputMissionConfigurationUIState())
    val uiState: StateFlow<InputMissionConfigurationUIState> = _uiState.asStateFlow()

    fun configureMission() {
        val result = missionConfigurator.configure(
            marsInitialPositionX = uiState.value.marsInitialPositionX!!,
            marsInitialPositionY = uiState.value.marsInitialPositionY!!,
            marsInitialOrientation = uiState.value.marsInitialOrientation,
            topRightCornerX = uiState.value.topRightCornerX!!,
            topRightCornerY = uiState.value.topRightCornerY!!
        )

        if (result is MissionConfigurationResult.Success) {
            _uiState.update {
                uiState.value.copy(
                    navigationEvent = NavigationEventInputMission.ToMission
                )
            }
        } else {
            _uiState.update {
                uiState.value.copy(
                    errorEventInputMission = ErrorEventInputMission.InvalidRoverPosition
                )
            }
        }
    }

    fun setMarsXposition(position: String?, isNegative: Boolean) {
        _uiState.update {
            uiState.value.copy(
                marsInitialPositionX = formatPosition(position, isNegative)
            )
        }
    }

    fun setMarsYposition(position: String?, isNegative: Boolean) {
        _uiState.update {
            uiState.value.copy(
                marsInitialPositionY = formatPosition(position, isNegative)
            )
        }
    }

    fun setMarsOrientation(orientation: String) {
        val newOrientation = when (orientation) {
            Orientation.NORTH.name -> Orientation.NORTH
            Orientation.SOUTH.name -> Orientation.SOUTH
            Orientation.WEST.name -> Orientation.WEST
            else -> Orientation.EAST
        }
        _uiState.update {
            uiState.value.copy(
                marsInitialOrientation = newOrientation
            )
        }
    }

    fun setTopRightXCorner(position: String?, isNegative: Boolean) {
        _uiState.update {
            uiState.value.copy(
                topRightCornerX = formatPosition(position, isNegative)
            )
        }
    }

    fun setTopRightYCorner(position: String?, isNegative: Boolean) {
        _uiState.update {
            uiState.value.copy(
                topRightCornerY = formatPosition(position, isNegative)
            )
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
                errorEventInputMission = null
            )
        }
    }
}

private fun formatPosition(position: String?, isNegative: Boolean): Int? {
    return if (!position.isNullOrEmpty()) {
        if (isNegative) -position.toInt() else position.toInt()
    } else {
        null
    }
}


