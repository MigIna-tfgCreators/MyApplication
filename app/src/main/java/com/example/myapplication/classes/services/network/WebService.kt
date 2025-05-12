package com.example.myapplication.classes.services.network

import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.models.response.VideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WebService {
    @GET("movie/now_playing")
    suspend fun obtenerCartelera(
        @Query("api_key") apiKey: String,
        @Query("page") page: Int,
        @Query("region") region: String,
        @Query("language") language: String = "es-ES"
    ): Response<PeliculaResponsePaginada>

    @GET("movie/top_rated")
    suspend fun obtenerTop(
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Response<PeliculaResponsePaginada>

    @GET("search/movie")
    suspend fun buscarPeliculas(
        @Query("api_key") apiKey: String,
        @Query("query") query: String,
        @Query("language") language: String = "es-ES",
        @Query("page") page: Int = 1
    ): Response<PeliculaResponsePaginada>

    @GET("movie/{movie_id}/videos")
    suspend fun obtenerVideosPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String,
        @Query("language") language: String = "es-ES"
    ): Response<VideosResponse>
}