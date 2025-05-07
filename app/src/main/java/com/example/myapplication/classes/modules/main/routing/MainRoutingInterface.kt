package com.example.myapplication.classes.modules.main.routing

import android.os.Bundle


interface MainRoutingInterface {
    fun navigateToCartelera()
    fun navigateToTop()
    fun navigateToDetalles(bundle: Bundle?)
    fun navigateToBusqueda()
    fun navigateToFavoritos()
}