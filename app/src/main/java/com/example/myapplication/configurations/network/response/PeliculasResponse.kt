package com.example.myapplication.configurations.network.response

import com.example.myapplication.classes.models.API.PeliculaModel
import com.google.gson.annotations.SerializedName

data class PeliculasResponse(
    @SerializedName("results")
    var resultados: List<PeliculaModel>
)
