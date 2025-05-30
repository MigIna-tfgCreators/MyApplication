package com.example.myapplication.classes.modules.main.personal.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.personal.model.PersonalListEvents
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PersonalListViewModel(
    private val firebaseRepository: UserMovieRepository,
    private val repository: MoviesRepository
): ViewModel(){

    private val _moviesState = MutableStateFlow<GeneralMovieState>(GeneralMovieState())
    val moviesState: StateFlow<GeneralMovieState> = _moviesState.asStateFlow()

    private var lastQuery = ""
    private var actualPage= 1

    fun addEventPersonal(event: PersonalListEvents){
        viewModelScope.launch {
            when(event){
                PersonalListEvents.ResetAll -> resetList()

                is PersonalListEvents.AddPersonalMovie -> addPersonalMovie(event.movie, event.info)

                is PersonalListEvents.QuitPersonalMovie -> quitPersonalMovie(event.movie)

                PersonalListEvents.ShowPersonalList -> showPersonalList()

                is PersonalListEvents.ShowSearchedList -> showSearchedList(event.query)

                is PersonalListEvents.HasInPersonal -> hashPersonal(event.movie, event.info)
            }
        }
    }

    private fun resetList(){
        viewModelScope.launch {
            _moviesState.value = _moviesState.value.copy(error = null, actualPersonalMovies = emptyList())
        }
    }

    private fun addPersonalMovie(movie: Movie, extraInfo: UserMovieExtraInfo?){
        viewModelScope.launch {
            try{
                if (_moviesState.value.isLoading) return@launch
                _moviesState.value = _moviesState.value.copy(isLoading = true, error = null)

                firebaseRepository.addPersonalMovie(movie, extraInfo)
                _moviesState.value = _moviesState.value.copy(isLoading = false)

                showPersonalList()
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
                showPersonalList()
            } catch (e: Exception){
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