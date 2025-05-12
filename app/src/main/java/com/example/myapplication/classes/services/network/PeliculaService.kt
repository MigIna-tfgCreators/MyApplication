package com.example.myapplication.classes.services.network

import com.example.myapplication.classes.models.API.PeliculasDetalles
import com.example.myapplication.classes.models.response.CreditosResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PeliculaService {

    @GET("movie/{movie_id}")
    fun obtenerDetallesPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Call<PeliculasDetalles>

    @GET("movie/{movie_id}/credits")
    fun obtenerCreditos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Call<CreditosResponse>
}