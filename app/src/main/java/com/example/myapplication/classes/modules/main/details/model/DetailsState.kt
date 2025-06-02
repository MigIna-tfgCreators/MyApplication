package com.example.myapplication.classes.modules.main.details.model

import com.example.myapplication.classes.models.API.Credits
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.API.Video
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo

data class DetailsState(
    val actualFilm: Movie = Movie(),
    val actualCredits: Credits = Credits(),
    val youtubeVideo: Video = Video(),
    val extraInfo: UserMovieExtraInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
