package com.example.myapplication.classes.modules.main.search.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.search.model.SearchEvents
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import com.example.myapplication.classes.repositories.firebase.usermovieRepository.UserMovieRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MoviesRepository,
    private val firebaseRepository: UserMovieRepository,
    private val context: ContextProviderInterface
): ViewModel()  {
    private val _searchState = MutableStateFlow<GeneralMovieState>(GeneralMovieState())
    val searchState: StateFlow<GeneralMovieState> = _searchState.asStateFlow()

    internal var actualPage = 1
    private var lastQuery = ""

    fun addEvent(event: SearchEvents){
        when(event) {
            is SearchEvents.ApplyFilters -> applyFilters(event.genresIds, event.dates, event.order)

            SearchEvents.ClearMovies -> clearMovies()

            SearchEvents.GetFilterList -> getFilterList()

            SearchEvents.GetListGenres -> getListGenres()

            SearchEvents.ResetAll -> resetAll()

            is SearchEvents.SearchMovies -> getSearchedList(event.query)

            is SearchEvents.CheckMovie -> checkMovie(event.movie)

            is SearchEvents.HasInPersonal -> hashInPersonal(event.movie, event.info)

            SearchEvents.ResetFav -> resetFav()
            SearchEvents.ClearErrors -> _searchState.value = _searchState.value.copy(error = null)
        }
    }
    private fun resetFav(){
        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(error = null, isInPersonalList = null)
        }
    }


    private fun resetAll(){
        viewModelScope.launch {
            actualPage = 1
            _searchState.value = _searchState.value.copy(actualMovies = emptyList(), genresListApplied = emptyList(), dates = "&", order = "popularity.desc", error = null, isInPersonalList = null)
        }
    }

    private fun clearMovies(){
        viewModelScope.launch {
            actualPage = 1
            lastQuery = ""
            _searchState.value = _searchState.value.copy(actualMovies = emptyList(), error = null)
        }
    }

    private fun getFilterList() {
        viewModelScope.launch {
            try{
                if (_searchState.value.isLoading) return@launch
                _searchState.value = _searchState.value.copy(isLoading = true, isSearchMode = false)




                val filterListDeferred =async {
                    val list = repository.getFilterList(
                        actualPage, _searchState.value.genresListApplied,
                        _searchState.value.dates.toString(), _searchState.value.order.toString()
                    ).filter { it.movieAverageVote != null && it.movieAverageVote >0.0 }
                        .sortedByDescending { it.movieTotalVotes }


                    list
                }
                val personalListDeferred = async {
                    val personalList = firebaseRepository.getPersonalList()
                    personalList.map { repository.getMovieDetails(it.movieId) }
                }

                val filterList = filterListDeferred.await()
                val personalList = personalListDeferred.await()

                val allMovies = _searchState.value.actualMovies + filterList



                _searchState.value = _searchState.value.copy(actualMovies = allMovies,
                    actualPersonalMovies = personalList, isLoading = false)

                actualPage++
            }catch (e: Exception) {
                _searchState.value = _searchState.value.copy( isLoading = false, error = context.currentActivity?.getString(R.string.api_error_get_films_filter).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(error = null)
            }
        }
    }

    private fun applyFilters(list: List<Int>?, dates: String?, order: String?){
        viewModelScope.launch {
            _searchState.value = _searchState.value.copy(genresListApplied = list ?: listOf(), dates = dates ?: "&", order = order ?: "popularity.desc")
        }
    }

    private fun getListGenres() {
        viewModelScope.launch {

            try{
                _searchState.value = _searchState.value.copy(isLoading = true)

                val listGenres = repository.getListGenres()
                _searchState.value = _searchState.value.copy(genresList = listGenres, isLoading = false)

            }catch (e: Exception){
                _searchState.value = _searchState.value.copy( isLoading = false, error = context.currentActivity?.getString(R.string.api_error_get_genres).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(error = null)
            }
        }
    }

    private fun getSearchedList(query: String){
        viewModelScope.launch {
            try{
                if(_searchState.value.isLoading) return@launch

                _searchState.value = _searchState.value.copy(isLoading = true, isSearchMode = true)

                val cleanQuery = query.trim()

                val isNewSearch = lastQuery != query
                if(isNewSearch){
                    actualPage = 1
                    lastQuery =cleanQuery
                    _searchState.value = _searchState.value.copy(actualMovies = emptyList())

                }

                val searchedMoviesDeferred = async {
                    repository.searchMovies(cleanQuery, actualPage)
                        .filter { it.movieAverageVote != null && it.movieAverageVote > 0.0 }
                        .sortedByDescending { it.movieTotalVotes }
                }
                val personalListDeferred = async {
                    firebaseRepository.getPersonalList().map { repository.getMovieDetails(it.movieId) }
                }
                val searchedMovies = searchedMoviesDeferred.await()
                val personalList  = personalListDeferred.await()


                val currentList = _searchState.value.actualMovies

                val updatedList = if(actualPage == 1) searchedMovies else currentList + searchedMovies


                _searchState.value = _searchState.value.copy(actualMovies = updatedList, actualPersonalMovies = personalList,
                    isLoading = false, actualQuery = query )
                actualPage++
            }catch (e: Exception){
                _searchState.value = _searchState.value.copy( isLoading = false, error = context.currentActivity?.getString(R.string.api_error_get_films_query).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(error = null)
            }
        }
    }


    private fun addPersonalMovie(movie: Movie, extraInfo: UserMovieExtraInfo?){
        viewModelScope.launch {
            try{
                if (_searchState.value.isLoading) return@launch
                _searchState.value = _searchState.value.copy(isLoading = true, error = null)

                firebaseRepository.addPersonalMovie(movie, extraInfo)
                _searchState.value = _searchState.value.copy(isLoading = false)

                if(_searchState.value.isSearchMode == true) getSearchedList(lastQuery)
                else getFilterList()

            } catch (e: Exception){
                _searchState.value = _searchState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun quitPersonalMovie(movie: Movie){
        viewModelScope.launch {
            try{
                if (_searchState.value.isLoading) return@launch
                _searchState.value = _searchState.value.copy(isLoading = true, error = null)

                firebaseRepository.quitPersonalMovie(movie)
                _searchState.value = _searchState.value.copy(isLoading = false)

                if(_searchState.value.isSearchMode == true) getSearchedList(lastQuery)
                else getFilterList()

            } catch (e: Exception){
                _searchState.value = _searchState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun hashInPersonal(movie: Movie, info: UserMovieExtraInfo?){
        viewModelScope.launch {
            try{
                if (_searchState.value.isLoading) return@launch
                _searchState.value = _searchState.value.copy(isLoading = true, error = null)

                val flag = firebaseRepository.checkUserMovie(movie.movieId)
                _searchState.value = _searchState.value.copy(isLoading = false)

                if(flag) quitPersonalMovie(movie)

                else addPersonalMovie(movie, info)

            } catch (e: Exception){
                _searchState.value = _searchState.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    private fun checkMovie(movie: Movie){
        viewModelScope.launch {
            try{
                if (_searchState.value.isLoading) return@launch
                _searchState.value = _searchState.value.copy(isLoading = true, error = null)

                val check = firebaseRepository.checkUserMovie(movie.movieId)
                _searchState.value = _searchState.value.copy(isLoading = false, isInPersonalList = check)

            } catch (e: Exception){
                _searchState.value = _searchState.value.copy(isLoading = false, error = e.message)
            }
        }
    }
}