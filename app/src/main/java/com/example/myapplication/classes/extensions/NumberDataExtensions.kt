package com.example.myapplication.classes.extensions

val Int?.valueOrZero: Int
    get() = this ?: 0

val Double?.valueOrZero: Double
    get() = this ?: 0.0

val Float?.valueOrZero: Float
    get() = this ?: 0.0f