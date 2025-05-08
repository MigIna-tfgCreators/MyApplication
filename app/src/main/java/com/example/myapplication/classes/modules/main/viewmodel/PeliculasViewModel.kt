package com.example.myapplication.classes.modules.main.viewmodel

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.commonErrors.showErrs.DialogErrsInterface
import com.example.myapplication.classes.models.main.APIEvents
import com.example.myapplication.classes.models.main.ListUIState
import com.example.myapplication.classes.models.main.MainEvents
import com.example.myapplication.classes.modules.main.routing.MainRoutingInterface
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.PeliculasRepository.PeliculasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PeliculasViewModel(
    private val repository: PeliculasRepository,
    private val routing: MainRoutingInterface,
    private val errors: DialogErrsInterface,
    private val contextProvider: ContextProviderInterface
): ViewModel() {

    private val _uiState = MutableStateFlow(ListUIState())
    val uiState: StateFlow<ListUIState> = _uiState.asStateFlow()

    fun addEventNavegation(events: MainEvents, bundle: Bundle?){
        when(events) {
            MainEvents.Detalle -> {
                routing.navigateToDetalles(bundle)
            }
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
            MainEvents.Perfil -> TODO()
            MainEvents.Salir -> TODO()
        }
    }

    fun addEventFilms(events: APIEvents){
        viewModelScope.launch {
            when(events) {
                APIEvents.mostrarListado -> {
                    mostrarListado()
                }
                APIEvents.mostrarListadoFav -> {
                    mostrarListadoFav()
                }
                APIEvents.mostrarListadoTop -> {
                    mostrarListadoTop()
                }
            }
        }
    }

    private fun mostrarListado(){
        viewModelScope.launch {
            val peliculasTotales = repository.obtenerCartelera()
            _uiState.value = _uiState.value.copy(actualList = peliculasTotales)
        }
    }
    private fun mostrarListadoFav(){
        //POr modificar
        viewModelScope.launch {
            val peliculasTotales = repository.obtenerCartelera()
            _uiState.value = _uiState.value.copy(actualList = peliculasTotales)
        }
    }
    private fun mostrarListadoTop(){
        viewModelScope.launch {
            val peliculasTop = repository.obtenerTop()
            _uiState.value = _uiState.value.copy(actualList = peliculasTop)
        }
    }


}