package com.example.myapplication.classes.services.api

import com.example.myapplication.BuildConfig
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.models.response.PeliculaDTO
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.models.response.VideosResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIServiceInterface {

    @GET("movie/{movie_id}")
    suspend fun obtenerDetallesPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = API.LANGUAGE,
        @Query("region") region: String = API.REGION
    ): Response<PeliculaDTO>

    @GET("movie/{movie_id}/credits")
    suspend fun obtenerCreditos(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = API.LANGUAGE,
        @Query("region") region: String = API.REGION
    ): Response<CreditosResponse>

    @GET("movie/now_playing")
    suspend fun obtenerCartelera(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int,
        @Query("region") region: String = API.REGION,
        @Query("language") language: String = API.LANGUAGE
    ): Response<PeliculaResponsePaginada>

    @GET("movie/top_rated")
    suspend fun obtenerTop(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = API.LANGUAGE,
        @Query("page") page: Int = 1,
        @Query("region") region: String = API.REGION
    ): Response<PeliculaResponsePaginada>

    @GET("search/movie")
    suspend fun buscarPeliculas(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("language") language: String = API.LANGUAGE,
        @Query("page") page: Int = 1,
        @Query("region") region: String = API.REGION
    ): Response<PeliculaResponsePaginada>

    @GET("movie/{movie_id}/videos")
    suspend fun obtenerVideosPelicula(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = API.LANGUAGE,
        @Query("region") region: String = API.REGION
    ): Response<VideosResponse>
}