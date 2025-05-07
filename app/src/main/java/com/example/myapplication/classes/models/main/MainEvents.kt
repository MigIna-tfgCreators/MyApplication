package com.example.myapplication.classes.models.main

import androidx.lifecycle.ViewModel


sealed class MainEvents {
    object ListaCartelera: MainEvents()
    object ListaTop: MainEvents()
    object ListaBusqueda: MainEvents()
    object ListaFavoritos: MainEvents()
    object Perfil: MainEvents()
    object Detalle: MainEvents()
    object Salir: MainEvents()
}
fun <T: MainEvents> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }