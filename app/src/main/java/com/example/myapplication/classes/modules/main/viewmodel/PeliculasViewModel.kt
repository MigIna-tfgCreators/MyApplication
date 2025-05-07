package com.example.myapplication.classes.modules.main.viewmodel

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.commonErrors.showErrs.DialogErrsInterface
import com.example.myapplication.classes.models.main.ListUIState
import com.example.myapplication.classes.models.main.MainEvents
import com.example.myapplication.classes.modules.main.routing.MainRoutingInterface
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.PeliculasRepository.PeliculasRepository
import com.example.myapplication.classes.services.network.API
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PeliculasViewModel(
    private val repository: PeliculasRepository,
    private val routing: MainRoutingInterface,
    private val errors: DialogErrsInterface,
    private val contextProvider: ContextProviderInterface
): ViewModel() {

    private var _listaPelicuas = MutableLiveData<List<PeliculaModel>>() //Permite modificar la informacion de la lista
    val listaPeliculas: LiveData<List<PeliculaModel>> = _listaPelicuas //Se va a cosumir en las listas y no se puede mdifiar

    private val _uiState = MutableStateFlow(ListUIState())
    val uiState: StateFlow<ListUIState> = _uiState.asStateFlow()

    fun addEvent(events: MainEvents, bundle: Bundle?){
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


}