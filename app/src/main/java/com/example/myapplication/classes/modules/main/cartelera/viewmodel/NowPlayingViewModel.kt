package com.example.myapplication.classes.modules.main.cartelera.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.modules.main.cartelera.model.NowPlayingEvents
import com.example.myapplication.classes.modules.main.cartelera.model.NowPlayingState
import com.example.myapplication.classes.repositories.PeliculasRepository.MoviesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NowPlayingViewModel(
    private val repository: MoviesRepository
): ViewModel()  {

    private val _moviesState = MutableStateFlow<NowPlayingState>(NowPlayingState())
    val moviesState: StateFlow<NowPlayingState> = _moviesState.asStateFlow()

    var actualPage = 1
    private var lastQuery = ""

    fun addEventFilms(events: NowPlayingEvents){
        viewModelScope.launch {
            when(events) {
                NowPlayingEvents.ShowList -> showList()

                NowPlayingEvents.ResetAll -> resetList()

                is NowPlayingEvents.ShowSearchedList -> showSearchedList(events.query)
            }
        }
    }

    private fun showSearchedList(query: String){
        viewModelScope.launch {
            try{
                if( _moviesState.value.isLoading) return@launch

                if(query != lastQuery){
                    actualPage = 1
                    _moviesState.value = _moviesState.value.copy(actualFilms = emptyList())
                }
                lastQuery = query

                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = true)

                val searchedMovies = repository.searchMovies(query, actualPage)

                val currentList = _moviesState.value.actualFilms
                val allData = if(actualPage == 1) searchedMovies else currentList + searchedMovies

                _moviesState.value = _moviesState.value.copy(actualFilms = allData, isLoading = false, actualQuery = query)
                actualPage++

            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun showList(){
        viewModelScope.launch {
            try{
                if( _moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = false)

                val newData = repository.getNowPlaying(actualPage)
                Log.d("NowPlayingViewModel", "Fetched ${newData.size} movies")


                val currentMovies = _moviesState.value.actualFilms

                val allMovies = currentMovies + newData

                _moviesState.value = _moviesState.value.copy(isLoading = false, actualFilms = allMovies)
                actualPage++

            }catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun resetList(){
        viewModelScope.launch {
            actualPage = 1
            _moviesState.value = _moviesState.value.copy(actualFilms = emptyList(), error = null)
        }
    }


}