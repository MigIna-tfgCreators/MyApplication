package com.example.myapplication.classes.models.API

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class PeliculaModel(
    @SerializedName("id")
    var id:String,
    @SerializedName("original_title")
    var nombrePelicula:String,
    @SerializedName("overview")
    var descripcion: String,
    @SerializedName("poster_path")
    var poster: String,
    @SerializedName("release_date")
    var fechaLanzamiento: String,
    @SerializedName("vote_average")
    var votoPromedio: String,
    @SerializedName("vote_count")
    var totalVotos: String
): Serializable