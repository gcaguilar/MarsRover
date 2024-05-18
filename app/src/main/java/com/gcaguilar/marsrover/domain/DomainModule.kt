package com.gcaguilar.marsrover.domain

import com.gcaguilar.marsrover.domain.usecase.MissionConfigurator
import com.gcaguilar.marsrover.domain.usecase.MissionExecutor
import org.koin.dsl.module

val domainModule = module {
    factory {
        MissionConfigurator(
            missionControl = get()
        )
    }
    factory {
        MissionExecutor(
            marsMissionControlRepository = get()
        )
    }
}