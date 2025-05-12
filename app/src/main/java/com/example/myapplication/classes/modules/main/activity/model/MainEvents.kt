package com.example.myapplication.classes.modules.main.activity.model

import androidx.lifecycle.ViewModel


sealed class MainEvents {
    object ListaCartelera: MainEvents()
    object ListaTop: MainEvents()
    object ListaBusqueda: MainEvents()
    object ListaFavoritos: MainEvents()
    object AppearanceProfile: MainEvents()
}
fun <T: MainEvents> ViewModel.handleEvent(event: T, handler: (T) -> Unit){ handler(event) }