package com.example.myapplication.classes.modules.main.detalles.model

import com.example.myapplication.classes.models.API.Credits
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.API.Video

data class DetailsState(
    val actualFilm: Movie = Movie(),
    val actualCredits: Credits = Credits(),
    val youtubeVideo: Video = Video(),
    val isLoading: Boolean = false,
    val error: String? = null
)
