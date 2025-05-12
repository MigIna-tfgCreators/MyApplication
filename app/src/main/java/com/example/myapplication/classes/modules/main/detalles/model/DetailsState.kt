package com.example.myapplication.classes.modules.main.detalles.model

import com.example.myapplication.classes.models.API.PeliculasDetalles
import com.example.myapplication.classes.models.response.CreditosResponse

data class DetailsState(
    val actualFilm: PeliculasDetalles? = null,
    val actualCredits: CreditosResponse? = null,
    val youtubeUrl: String? = null
)
