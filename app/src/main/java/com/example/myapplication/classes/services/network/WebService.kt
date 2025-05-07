package com.example.myapplication.classes.services.network

import com.example.myapplication.classes.models.response.PeliculasResponse
import com.example.myapplication.classes.models.response.VideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @GET("now_playing")
    suspend fun obtenerCartelera(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Response<PeliculasResponse>

    @GET("top_rated")
    suspend fun obtenerTop(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Response<PeliculasResponse>

    @GET("{movie_id}/videos")
    suspend fun obtenerVideosPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Response<VideosResponse>
}