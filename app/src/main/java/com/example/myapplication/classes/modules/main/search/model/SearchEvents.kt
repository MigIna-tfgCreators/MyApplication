package com.example.myapplication.classes.modules.main.search.model

import com.example.myapplication.classes.models.API.Movie


sealed class SearchEvents {
    object GetFilterList: SearchEvents()
    object GetListGenres: SearchEvents()
    object ResetAll: SearchEvents()
    object ClearMovies: SearchEvents()
    data class ApplyFilters(val genresIds: List<Int>?, val dates: String?, val order: String?): SearchEvents()
    data class SearchMovies(val query: String): SearchEvents()
    data class AddPersonalMovie(val movie: Movie?): SearchEvents()
    data class QuitPersonalMovie(val movie: Movie?): SearchEvents()
}