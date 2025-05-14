package com.example.myapplication.classes.models.response

import com.google.gson.annotations.SerializedName


data class PeliculaDTO(
    val id: Int,
    @SerializedName("title")
    var titulo: String = "",
    @SerializedName("overview")
    var descripcion: String? = "",
    @SerializedName("poster_path")
    val imagen: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    @SerializedName("release_date")
    val fechaLanzamiento: String?,
    @SerializedName("vote_average")
    val votoPromedio: Float?,
    @SerializedName("vote_count")
    val votosTotales: Float?,
    val runtime: Int?,
    @SerializedName("genres")
    val generos: List<Genero>? = emptyList()
)

data class Genero(
    val id: Int,
    val name: String
)
