package com.example.myapplication.classes.extensions

val<T> List<T>?.valueOrEmpty: List<T>
    get() = this ?: emptyList()