package com.example.myapplication.classes.modules.main.activity.model

import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.classes.models.API.Movie

data class GeneralMovieState(
    val isLoading: Boolean = false,
    val error: String? = null,

    val actualMovies: List<Movie> = emptyList(),
    val actualPersonalMovies: List<Movie>? = emptyList(),

    val genresList: List<Genre> = emptyList(),
    val genresListApplied: List<Int> = emptyList(),

    val isSearchMode: Boolean? = false,

    val actualQuery: String? = "",
    val dates: String? = "&",
    val order: String? = ""

)
