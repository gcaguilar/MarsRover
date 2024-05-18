package com.gcaguilar.marsrover.presentation

import com.gcaguilar.marsrover.presentation.file.FileMissionViewModel
import com.gcaguilar.marsrover.presentation.manual.InputMissionViewModel
import com.gcaguilar.marsrover.presentation.mission.MissionViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presentationModule = module {
    viewModel {
        InputMissionViewModel(
            missionConfigurator = get(),
        )
    }

    viewModel {
        MissionViewModel(
            missionExecutor = get()
        )
    }

    viewModel {
        FileMissionViewModel(
            missionConfigurator = get()
        )
    }
}