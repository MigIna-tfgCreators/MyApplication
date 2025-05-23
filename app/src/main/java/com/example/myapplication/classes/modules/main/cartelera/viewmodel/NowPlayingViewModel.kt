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
                NowPlayingEvents.ResetAll -> resetList()

                is NowPlayingEvents.ShowSearchedList -> showSearchedList(events.query)

                NowPlayingEvents.ShowAllList -> showAllList()
            }
        }
    }

    private fun showSearchedList(query: String){
        viewModelScope.launch {
            try{
                if( _moviesState.value.isLoading) return@launch

                lastQuery = query

                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = true)

                val currentList = _moviesState.value.actualFilms
                Log.d("NowPlayingFragment2","currrentList -> ${currentList.size}")

                val searchedMovies = currentList.filter {
                    it.movieTitle.contains(query, ignoreCase = true)
                }

                Log.d("NowPlayingFragment2","searchedList -> ${searchedMovies.size}")

                val combinedData = if(actualPage == 1)
                    searchedMovies
                else
                    (currentList + searchedMovies).distinctBy { it.movieId }

                _moviesState.value = _moviesState.value.copy(actualFilms = combinedData, isLoading = false, actualQuery = query)
                actualPage++

            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }


    private fun showAllList(){
        viewModelScope.launch {
            try {
                if( _moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = false)

                val allMovies = repository.getAllNowPlaying()
                _moviesState.value = _moviesState.value.copy(isLoading = false, actualFilms = allMovies)

                Log.d("NowPlayingFragment", "el tamaño de todo es ${allMovies.size}")

            }catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun resetList(){
        viewModelScope.launch {
            actualPage = 1
            _moviesState.value = _moviesState.value.copy(error = null)
        }
    }


}