package com.example.myapplication.classes.modules.main.cartelera.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.cartelera.model.NowPlayingEvents
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NowPlayingViewModel(
    private val repository: MoviesRepository,
    private val firebaseRepository: UserMovieRepository
): ViewModel()  {

    private val _moviesState = MutableStateFlow<GeneralMovieState>(GeneralMovieState())
    val moviesState: StateFlow<GeneralMovieState> = _moviesState.asStateFlow()

    private var lastQuery = ""


    fun addEventFilms(event: NowPlayingEvents){
        viewModelScope.launch {
            when(event) {
                NowPlayingEvents.ResetAll -> resetList()

                is NowPlayingEvents.ShowSearchedList -> showSearchedList(event.query)

                NowPlayingEvents.ShowTMDBList -> showTMDBList()
                NowPlayingEvents.ShowPersonalList -> showPersonalList()

                is NowPlayingEvents.AddPersonalMovie -> addPersonalMovie(event.movie, event.info)
                is NowPlayingEvents.QuitPersonalMovie -> quitPersonalMovie(event.movie)

                is NowPlayingEvents.HasInPersonal -> hashPersonal(event.movie, event.info)

                NowPlayingEvents.ShowAllList -> showAllList()
            }
        }
    }

    private fun showAllList(){
        viewModelScope.launch {
            try {
                Log.d("HOLA","AYUDA2")

                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null, isSearchMode = false)
                Log.d("HOLA","AYUDA")
                // Lanzar ambas tareas en paralelo
                val allMoviesDeferred = async { repository.getAllNowPlaying() }
                val personalListDeferred = async {
                    val personalList = firebaseRepository.getPersonalList()
                    personalList.map { repository.getMovieDetails(it.movieId) }
                }

                // Esperar resultados
                val allMovies = allMoviesDeferred.await()
                Log.d("AYUDA SEH",allMovies.size.toString())
                val personalMovies = personalListDeferred.await()
                Log.d("AYUDA SEH2",personalMovies.size.toString())

                // Actualizar estado con ambas listas
                _moviesState.value = _moviesState.value.copy(
                    isLoading = false,
                    actualMovies = allMovies,
                    actualPersonalMovies = personalMovies
                )

                Log.d("NowPlayingFragment", "All movies: ${allMovies.size}, Personal movies: ${personalMovies.size}")

            } catch (e: Exception) {
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun showTMDBList(){
        if( _moviesState.value.isLoading) return
        viewModelScope.launch {
            try {
                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = false)

                val allMovies = repository.getAllNowPlaying()
                _moviesState.value = _moviesState.value.copy(isLoading = false, actualMovies = allMovies)

                Log.d("NowPlayingFragment", "el tamaño de todo es ${allMovies.size}")

            }catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun showPersonalList(){
        if(_moviesState.value.isLoading) return
        viewModelScope.launch {
            try{

                Log.d("AYUDA","personalList.size.toString()")

                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = false, error = null)

                val personalList = firebaseRepository.getPersonalList()
                Log.d("TESTING",personalList.size.toString())
                val persList = personalList.map { repository.getMovieDetails(it.movieId) }
                val actualList = _moviesState.value.actualMovies

                _moviesState.value = _moviesState.value.copy(isLoading = false,actualPersonalMovies = persList, actualMovies = actualList)


            }catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun showSearchedList(query: String){
        viewModelScope.launch {
            try{
                if( _moviesState.value.isLoading) return@launch

                lastQuery = query

                _moviesState.value = _moviesState.value.copy(isLoading = true, isSearchMode = true)

                val currentList = _moviesState.value.actualMovies
                Log.d("NowPlayingFragment2","currrentList -> ${currentList.size}")

                val searchedMovies = currentList.filter {
                    it.movieTitle.contains(query, ignoreCase = true)
                }

                Log.d("NowPlayingFragment2","searchedList -> ${searchedMovies.size}")

//                val combinedData = if(actualPage == 1)
//                    searchedMovies
//                else
//                    (currentList?.plus(searchedMovies))?.distinctBy<Movie> { it.movieId }

                _moviesState.value = _moviesState.value.copy(actualMovies = searchedMovies, isLoading = false, actualQuery = query)

            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun resetList(){
        viewModelScope.launch {
            _moviesState.value = _moviesState.value.copy(error = null)
        }
    }

    private fun addPersonalMovie(movie: Movie, extraInfo: UserMovieExtraInfo?){
        viewModelScope.launch {
            try{
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                firebaseRepository.addPersonalMovie(movie, extraInfo)
                _moviesState.value = _moviesState.value.copy(isLoading = false)

                showAllList()
            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun quitPersonalMovie(movie: Movie){
        viewModelScope.launch {
            try{
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                firebaseRepository.quitPersonalMovie(movie)
                _moviesState.value = _moviesState.value.copy(isLoading = false)
                showAllList()
            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun hashPersonal(movie: Movie, info: UserMovieExtraInfo?){
        viewModelScope.launch {
            try{
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                val flag = firebaseRepository.checkUserMovie(movie.movieId)
                Log.d("TESTEANDOOO",flag.toString())
                _moviesState.value = _moviesState.value.copy(isLoading = false)

                if(flag) quitPersonalMovie(movie)

                else addPersonalMovie(movie, info)

            } catch (e: Exception){
                _moviesState.value = _moviesState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}