package com.example.myapplication.classes.modules.main.search.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.modules.main.activity.model.GeneralMovieState
import com.example.myapplication.classes.modules.main.search.model.SearchEvents
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.api.moviesRepository.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MoviesRepository,
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

            is SearchEvents.AddPersonalMovie -> TODO()
            is SearchEvents.QuitPersonalMovie -> TODO()
        }
    }

    private fun resetAll(){
        viewModelScope.launch {
            actualPage = 1
            _searchState.value = _searchState.value.copy(actualMovies = emptyList(), genresListApplied = emptyList(), dates = "&", order = "popularity.desc", error = null)
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

                val newData = repository.getFilterList(actualPage, _searchState.value.genresListApplied,
                    _searchState.value.dates.toString(), _searchState.value.order.toString()
                )

                val currentMovies = _searchState.value.actualMovies
                val allMovies = currentMovies + newData

                _searchState.value = _searchState.value.copy(actualMovies = allMovies, isLoading = false)

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

                if(query != lastQuery){
                    actualPage = 1
                    _searchState.value = _searchState.value.copy(actualMovies = emptyList())
                }
                lastQuery = query

                _searchState.value = _searchState.value.copy(isLoading = true, isSearchMode = true)

                val searchedMovies = repository.searchMovies(query, actualPage)

                val currentList = _searchState.value.actualMovies

                val updatedList = if(actualPage == 1) searchedMovies else currentList?.plus(searchedMovies).valueOrEmpty


                _searchState.value = _searchState.value.copy(actualMovies = updatedList, isLoading = false, actualQuery = query )
                actualPage++
            }catch (e: Exception){
                _searchState.value = _searchState.value.copy( isLoading = false, error = context.currentActivity?.getString(R.string.api_error_get_films_query).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(error = null)
            }
        }
    }

}