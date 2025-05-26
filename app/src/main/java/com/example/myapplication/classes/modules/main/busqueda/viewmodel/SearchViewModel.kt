package com.example.myapplication.classes.modules.main.busqueda.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.R
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.modules.main.busqueda.model.SearchEvents
import com.example.myapplication.classes.modules.main.busqueda.model.SearchState
import com.example.myapplication.classes.providers.ContextProviderInterface
import com.example.myapplication.classes.repositories.PeliculasRepository.MoviesRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SearchViewModel(
    private val repository: MoviesRepository,
    private val context: ContextProviderInterface
): ViewModel()  {
    private val _searchState = MutableStateFlow<SearchState>(SearchState())
    val searchState: StateFlow<SearchState> = _searchState.asStateFlow()

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

        }
    }

    private fun resetAll(){
        viewModelScope.launch {
            actualPage = 1
            _searchState.value = _searchState.value.copy(actualFilms = emptyList(), genresListApplied = emptyList(), dates = "&", order = "popularity.desc", showText = null)
        }
    }

    private fun clearMovies(){
        viewModelScope.launch {
            actualPage = 1
            lastQuery = ""
            _searchState.value = _searchState.value.copy(actualFilms = emptyList(), showText = null)
        }
    }

    private fun getFilterList() {
        viewModelScope.launch {
            try{
                if (_searchState.value.isLoading) return@launch
                _searchState.value = _searchState.value.copy(isLoading = true, isSearchMode = false)

                val newData = repository.getFilterList(actualPage, _searchState.value.genresListApplied, _searchState.value.dates, _searchState.value.order)

                val currentMovies = _searchState.value.actualFilms
                val allMovies = currentMovies + newData

                _searchState.value = _searchState.value.copy(actualFilms = allMovies, isLoading = false)

                actualPage++
            }catch (e: Exception) {
                _searchState.value = _searchState.value.copy( isLoading = false, showText = context.currentActivity?.getString(R.string.api_error_get_films_filter).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(showText = null)
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
                _searchState.value = _searchState.value.copy( isLoading = false, showText = context.currentActivity?.getString(R.string.api_error_get_genres).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(showText = null)
            }
        }
    }

    private fun getSearchedList(query: String){
        viewModelScope.launch {
            try{
                if(_searchState.value.isLoading) return@launch

                if(query != lastQuery){
                    actualPage = 1
                    _searchState.value = _searchState.value.copy(actualFilms = emptyList())
                }
                lastQuery = query

                _searchState.value = _searchState.value.copy(isLoading = true, isSearchMode = true)

                val searchedMovies = repository.searchMovies(query, actualPage)

                val currentList = _searchState.value.actualFilms
                val updatedList = if(actualPage == 1) searchedMovies else currentList + searchedMovies


                _searchState.value = _searchState.value.copy(actualFilms = updatedList, isLoading = false, actualQuery = query)
                actualPage++
            }catch (e: Exception){
                _searchState.value = _searchState.value.copy( isLoading = false, showText = context.currentActivity?.getString(R.string.api_error_get_films_query).valueOrEmpty)
                delay(500)
                _searchState.value = _searchState.value.copy(showText = null)
            }
        }
    }

}