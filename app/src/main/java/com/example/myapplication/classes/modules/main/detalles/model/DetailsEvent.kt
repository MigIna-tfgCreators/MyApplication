package com.example.myapplication.classes.modules.main.detalles.model

import androidx.lifecycle.ViewModel


sealed class DetailsEvent {
    data class mostrarDetalle(val id: Int): DetailsEvent()
    data class mostrarCreditos(val id: Int): DetailsEvent()
    data class mostrarTrailer(val id: Int): DetailsEvent()
}
fun <T: DetailsEvent> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }
