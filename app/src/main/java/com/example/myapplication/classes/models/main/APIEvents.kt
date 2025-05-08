package com.example.myapplication.classes.models.main

import androidx.lifecycle.ViewModel

sealed class APIEvents {
    object mostrarListado: APIEvents()
    object mostrarListadoTop: APIEvents()
    object mostrarListadoFav: APIEvents()
}
fun <T: APIEvents> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }