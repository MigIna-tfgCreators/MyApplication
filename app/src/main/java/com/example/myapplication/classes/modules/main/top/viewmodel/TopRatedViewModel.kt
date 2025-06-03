package com.example.myapplication.classes.modules.main.top.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.top.model.TopRatedEvents
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TopRatedViewModel(
    private val repository: MoviesRepository,
    private val firebaseRepository: UserMovieRepository
): ViewModel() {

    private val _moviesState = MutableStateFlow(GeneralMovieState())
    val moviesState: StateFlow<GeneralMovieState> = _moviesState.asStateFlow()

    private var lastQuery = ""
    private var actualPage = 1

    fun addEvent(event: TopRatedEvents) {
        viewModelScope.launch {
            when (event) {
                TopRatedEvents.ResetAll -> resetList()

                is TopRatedEvents.ShowSearchedList -> showSearchedList(event.query)

                TopRatedEvents.ShowAllList -> showAllList()

                is TopRatedEvents.AddPersonalMovie -> addPersonalMovie(event.movie, event.info)
                is TopRatedEvents.QuitPersonalMovie -> quitPersonalMovie(event.movie)
                is TopRatedEvents.HasInPersonal -> hashPersonal(event.movie, event.info)

                is TopRatedEvents.CheckMovie -> checkMovie(event.movie)
            }
        }
    }

    private fun showAllList() {
        viewModelScope.launch {
            try {
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null, isSearchMode = false, isInPersonalList = null)

                val allMoviesDeferred = async { repository.getTopRated(page = actualPage) }
                val personalListDeferred = async {
                    val personalList = firebaseRepository.getPersonalList()
                    personalList.map { repository.getMovieDetails(it.movieId) }
                }

                val currentMovies = _moviesState.value.actualMovies
                val allMovies = if(actualPage == 1) allMoviesDeferred.await() else currentMovies + allMoviesDeferred.await()

                val personalMovies = personalListDeferred.await()

                _moviesState.value = _moviesState.value.copy(
                    isLoading = false,
                    actualMovies = allMovies,
                    actualPersonalMovies = personalMovies
                )
                actualPage++

            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun showSearchedList(query: String) {
        viewModelScope.launch {
            try {
                if (_moviesState.value.isLoading) return@launch

                lastQuery = query
                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = true)

                val currentList = _moviesState.value.actualMovies

                val searchedMovies = currentList.filter {
                    it.movieTitle.contains(query, ignoreCase = true)
                }

                _moviesState.value = _moviesState.value.copy(
                    actualMovies = searchedMovies,
                    isLoading = false,
                    actualQuery = query
                )

            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun resetList() {
        viewModelScope.launch {
            _moviesState.value = _moviesState.value.copy(error = null, isInPersonalList = null)
        }
    }

    private fun addPersonalMovie(movie: Movie, extraInfo: UserMovieExtraInfo?) {
        viewModelScope.launch {
            try {
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                firebaseRepository.addPersonalMovie(movie, extraInfo)
                _moviesState.value = _moviesState.value.copy(isLoading = false)

                showAllList()

            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun quitPersonalMovie(movie: Movie) {
        viewModelScope.launch {
            try {
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                firebaseRepository.quitPersonalMovie(movie)
                _moviesState.value = _moviesState.value.copy(isLoading = false)

                showAllList()

            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun hashPersonal(movie: Movie, info: UserMovieExtraInfo?) {
        viewModelScope.launch {
            try {
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                val exists = firebaseRepository.checkUserMovie(movie.movieId)
                _moviesState.value = _moviesState.value.copy(isLoading = false)

                if (exists) quitPersonalMovie(movie)
                else addPersonalMovie(movie, info)

            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun checkMovie(movie: Movie){
        viewModelScope.launch {
            try{
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                val check = firebaseRepository.checkUserMovie(movie.movieId)
                _moviesState.value = _moviesState.value.copy(isLoading = false, isInPersonalList = check)

            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}