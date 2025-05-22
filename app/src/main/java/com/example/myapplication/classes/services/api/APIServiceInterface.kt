package com.example.myapplication.classes.services.api

import com.example.myapplication.BuildConfig
import com.example.myapplication.classes.models.response.CreditsResponse
import com.example.myapplication.classes.models.response.GenreResponse
import com.example.myapplication.classes.models.response.MovieDTO
import com.example.myapplication.classes.models.response.MovieRemote
import com.example.myapplication.classes.models.response.VideosResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface APIServiceInterface {

    @GET("movie/now_playing")
    suspend fun getNowPlaying(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("page") page: Int,
        @Query("region") region: String = BuildConfig.REGION,
        @Query("language") language: String = BuildConfig.LANGUAGE
    ): Response<MovieRemote>

    @GET("movie/top_rated")
    suspend fun getTopRated(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("page") page: Int,
        @Query("region") region: String = BuildConfig.REGION
    ): Response<MovieRemote>

    @GET("search/movie")
    suspend fun searchMovies(
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("query") query: String,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("page") page: Int,
        @Query("region") region: String = BuildConfig.REGION
    ): Response<MovieRemote>

    @GET("movie/{movie_id}/videos")
    suspend fun getYoutubeVideo(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("region") region: String = BuildConfig.REGION
    ): Response<VideosResponse>

    @GET("genre/movie/list")
    suspend fun getGenresList(
        @Query("api_key") apiKey: String =  BuildConfig.API_KEY,
        @Query("region") region: String = BuildConfig.REGION,
        @Query("language") language: String = BuildConfig.LANGUAGE
    ): Response<GenreResponse>

    @GET("discover/movie")
    suspend fun getFilterList(
        @Query("primary_release_date.gte") startDate: String? = null,
        @Query("primary_release_date.lte") endDate: String? = null,
        @Query("with_genres") genres: String? = null,
        @Query("sort_by") sortBy: String,
        @Query("page") page: Int,
        @Query("api_key") apiKey: String =  BuildConfig.API_KEY,
        @Query("region") region: String = BuildConfig.REGION,
        @Query("language") language: String = BuildConfig.LANGUAGE
    ): Response<MovieRemote>


    @GET("movie/{movie_id}")
    suspend fun getMovieDetails(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("region") region: String = BuildConfig.REGION
    ): Response<MovieDTO>

    @GET("movie/{movie_id}/credits")
    suspend fun getMovieCredits(
        @Path("movie_id") movieId: Int,
        @Query("api_key") apiKey: String = BuildConfig.API_KEY,
        @Query("language") language: String = BuildConfig.LANGUAGE,
        @Query("region") region: String = BuildConfig.REGION
    ): Response<CreditsResponse>
}