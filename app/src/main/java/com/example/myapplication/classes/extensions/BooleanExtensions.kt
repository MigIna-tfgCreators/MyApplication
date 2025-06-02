package com.example.myapplication.classes.extensions

val Boolean?.valueOrFalse: Boolean
    get() = this ?: false