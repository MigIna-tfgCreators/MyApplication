package com.example.myapplication.classes.modules.main.cartelera.model

import com.example.myapplication.classes.models.API.Movie

data class NowPlayingState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val actualFilms: List<Movie> = emptyList(),
    val isSearchMode: Boolean = false,
    val actualQuery: String = ""
)
