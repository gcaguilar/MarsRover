package com.gcaguilar.marsrover.data

import com.gcaguilar.marsrover.domain.MarsMissionControlRepository
import org.koin.dsl.module

val dataModule = module {
    single { MarsMissionMemoryCache() }
    factory<MarsMissionControlRepository> {
        MarsMissionControlRepositoryImpl(
            marsMissionMemoryCache = get()
        )
    }
}