package com.example.myapplication.classes.modules.main.detalles.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.modules.main.detalles.model.DetailsEvent
import com.example.myapplication.classes.modules.main.detalles.model.DetailsState
import com.example.myapplication.classes.repositories.PeliculasRepository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DetailsViewModel(
    private val repository: MoviesRepository
): ViewModel() {

    private val _movie = MutableStateFlow<DetailsState>(DetailsState())
    val movie: StateFlow<DetailsState> = _movie.asStateFlow()

    fun addDetailsEvent(event: DetailsEvent){
        viewModelScope.launch {
            when(event) {
                is DetailsEvent.ShowDetails -> getDetalles(event.id)
                is DetailsEvent.ShowCredits -> getCreditos(event.id)
                is DetailsEvent.ShowTrailer -> getTrailer(event.id)
            }
        }
    }

    private fun getDetalles(id: Int){
        viewModelScope.launch {
            val newDetails = repository.getMovieDetails(id)
            _movie.value = _movie.value.copy(actualFilm =  newDetails)
        }
    }

    private fun getCreditos(id: Int){
        viewModelScope.launch {
            val newDetails = repository.getMovieCredits(id)
            _movie.value = _movie.value.copy(actualCredits =  newDetails)
        }
    }

    private fun getTrailer(id: Int){
        viewModelScope.launch {
            val list = repository.getYoutubeTrailer(id)
            val video = list.first{
                (it.videoSite == "YouTube" && it.videoType == "Trailer")
            }
            _movie.value =  _movie.value.copy(youtubeVideo = video)
        }
    }
}