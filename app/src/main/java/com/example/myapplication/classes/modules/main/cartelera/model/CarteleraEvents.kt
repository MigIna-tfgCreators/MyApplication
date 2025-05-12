package com.example.myapplication.classes.modules.main.cartelera.model

import androidx.lifecycle.ViewModel

sealed class CarteleraEvents {
    object mostrarListado: CarteleraEvents()
    object resetearListado: CarteleraEvents()
    data class actualizarListado(val filtros: String): CarteleraEvents()
    object viajeDetalle: CarteleraEvents()
}
fun <T: CarteleraEvents> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }
