package com.example.myapplication.classes.modules.main.detalles.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.modules.main.detalles.model.DetailsEvent
import com.example.myapplication.classes.modules.main.detalles.model.DetailsState
import com.example.myapplication.classes.repositories.PeliculasRepository.PeliculasRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetallesViewModel(
    private val repository: PeliculasRepository
): ViewModel() {

    private val _movie = MutableStateFlow<DetailsState>(DetailsState())
    val movie: StateFlow<DetailsState> = _movie.asStateFlow()

    fun addDetailsEvent(event: DetailsEvent){
        viewModelScope.launch {
            when(event) {
                is DetailsEvent.mostrarDetalle -> getDetalles(event.id)
                is DetailsEvent.mostrarCreditos -> getCreditos(event.id)
                is DetailsEvent.mostrarTrailer -> getTrailer(event.id)
            }
        }
    }

    private fun getDetalles(id: Int){
        viewModelScope.launch {
            val newDetails = repository.obtenerDetalles(id)
            _movie.value = _movie.value.copy(actualFilm =  newDetails)
            Log.d("IdentificadorDetails",newDetails.toString())
        }
    }

    private fun getCreditos(id: Int){
        viewModelScope.launch {
            val newDetails = repository.obtenerCreditos(id)
            _movie.value = _movie.value.copy(actualCredits =  newDetails)
            Log.d("IdentificadorCreditos",newDetails.toString())
        }
    }

    private fun getTrailer(id: Int){
        viewModelScope.launch {
            val listado = repository.obtenerTrailerYouTube(id)
            val video = listado.first{
                (it.sitio == "YouTube" && it.tipo == "Trailer")
            }
            _movie.value =  _movie.value.copy(youtubeVideo = video)
        }
    }
}