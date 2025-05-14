package com.example.myapplication.classes.modules.main.detalles.model

import com.example.myapplication.classes.models.API.Creditos
import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.models.API.Video

data class DetailsState(
    val actualFilm: Pelicula = Pelicula.EMPTY,
    val actualCredits: Creditos = Creditos.EMPTY,
    val youtubeVideo: Video = Video.EMPTY
)
