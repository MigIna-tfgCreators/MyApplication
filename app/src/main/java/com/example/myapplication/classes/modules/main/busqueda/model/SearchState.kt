package com.example.myapplication.classes.modules.main.busqueda.model

import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.classes.models.API.Movie

data class SearchState(
    val genresList: List<Genre> = emptyList(),
    val actualFilms: List<Movie> = emptyList(),
    val genresListApplied: List<Int> = emptyList(),
    val dates: String = "&",
    val order: String = "",
    val isLoading: Boolean = false,
    val isSearchMode: Boolean = false,
    val showText: String? = "",
    val actualQuery: String = ""
)

