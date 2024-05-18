package com.gcaguilar.marsrover.domain.models

enum class Command {
    M, L, R
}

fun Char.toCommand(): Command = when (this) {
    'M' -> Command.M
    'L' -> Command.L
    'R' -> Command.R
    else -> throw IllegalArgumentException("Invalid command")
}