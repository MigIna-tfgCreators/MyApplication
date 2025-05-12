package com.example.myapplication.classes.modules.main.cartelera.viewmodel

import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.modules.main.cartelera.model.CarteleraEvents
import com.example.myapplication.classes.modules.main.cartelera.routing.CarteleraRoutingInterface
import com.example.myapplication.classes.repositories.PeliculasRepository.PeliculasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CarteleraViewModel(
    private val repository: PeliculasRepository,
    private val routing: CarteleraRoutingInterface
): ViewModel()  {

    private val _movies = MutableStateFlow<PeliculaResponsePaginada?>(null)
    val movies: StateFlow<PeliculaResponsePaginada?> = _movies.asStateFlow()


    fun addEventFilms(events: CarteleraEvents, bundle: Bundle?){
        viewModelScope.launch {
            when(events) {
                is CarteleraEvents.actualizarListado -> {
                    actualizarListado(events.filtros)
                }
                CarteleraEvents.mostrarListado -> {
                    mostrarListado()
                }
                is CarteleraEvents.viajeDetalle -> routing.goToDetails(bundle)
                CarteleraEvents.resetearListado -> {
                    resetearListado()
                }
            }
        }
    }

    private fun actualizarListado(filtro: String){
        viewModelScope.launch {
            val peliculas = repository.buscarPeliculas(filtro, 1)
            _movies.value = _movies.value?.copy(results = peliculas.results)

        }
    }

    private fun mostrarListado(){
        viewModelScope.launch {

            val actualPage = (_movies.value?.page ?: 0)+1
            val newData = repository.obtenerCartelera(actualPage)

            val currentMovies = _movies.value

            val update = if(currentMovies == null){
                newData
            }else{
                newData.copy(
                    results = currentMovies.results + newData.results
                )
            }
            _movies.value = update
        }
    }

    private fun resetearListado(){
        viewModelScope.launch {
            _movies.value = _movies.value?.copy(results = repository.obtenerCartelera(1).results)
        }
    }

//    private fun mostrarListadoFav(){
//        //POr modificar
//        viewModelScope.launch {
//            val peliculasTotales = repository.obtenerCartelera()
//            _movies.value = _movies.value.copy(actualList = peliculasTotales)
//        }
//    }
//    private fun mostrarListadoTop(){
//        viewModelScope.launch {
//            val peliculasTop = repository.obtenerTop()
//            _movies.value = _movies.value.copy(actualList = peliculasTop)
//        }
//    }

}