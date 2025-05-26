package com.example.myapplication.classes.modules.main.busqueda.model


sealed class SearchEvents {
    object GetFilterList: SearchEvents()
    object GetListGenres: SearchEvents()
    object ResetAll: SearchEvents()
    object ClearMovies: SearchEvents()
    data class ApplyFilters(val genresIds: List<Int>?, val dates: String?, val order: String?): SearchEvents()
    data class SearchMovies(val query: String): SearchEvents()
}