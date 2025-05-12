package com.example.myapplication.classes.modules.main.activity.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.example.myapplication.classes.modules.main.activity.routing.MainRoutingInterface
import com.example.myapplication.classes.modules.main.activity.model.MainEvents
import com.example.myapplication.classes.repositories.authUserRepository.AuthRepository

class PeliculasViewModel(
    private val repository: AuthRepository,
    private val routing: MainRoutingInterface
): ViewModel() {

    fun addEventNavegation(events: MainEvents, bundle: Bundle?){
        when(events) {
            MainEvents.ListaBusqueda -> {
                routing.navigateToBusqueda()
            }
            MainEvents.ListaCartelera -> {
                routing.navigateToCartelera()
            }
            MainEvents.ListaFavoritos -> {
                routing.navigateToFavoritos()
            }
            MainEvents.ListaTop -> {
                routing.navigateToTop()
            }
            MainEvents.AppearanceProfile -> {
                //Todavía por implementar
            }
        }
    }




}