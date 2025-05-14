package com.example.myapplication.classes.models.API

import com.example.myapplication.classes.models.response.Genero
import com.example.myapplication.classes.models.response.MiembroEquipoResponse
import com.example.myapplication.classes.models.response.MiembroRepartoResponse
import com.example.myapplication.classes.models.response.PeliculaDTO
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Pelicula(
    var id: Int = 0,
    @SerializedName("title")
    var nombrePelicula: String = "",
    @SerializedName("overview")
    var descripcion: String? = "",
    @SerializedName("poster_path")
    var poster: String? = "",
    @SerializedName("release_date")
    var fechaLanzamiento: String? = "",
    @SerializedName("vote_average")
    var votoPromedio: Float? = 0.0f,
    @SerializedName("vote_count")
    var totalVotos: Float? = 0.0f,
    @SerializedName("genres")
    val generos: List<Genero>? = emptyList()

): Serializable{
    companion object{
        val EMPTY = Pelicula()
    }
}

val PeliculaDTO.toModel: Pelicula
    get(){
        return Pelicula(
            id = id,
            nombrePelicula = titulo,
            descripcion = descripcion,
            poster = imagen,
            fechaLanzamiento = fechaLanzamiento,
            votoPromedio = votoPromedio,
            totalVotos = votosTotales,
            generos = this.generos?.map { Genero(id = it.id, name = it.name) }
        )
    }
