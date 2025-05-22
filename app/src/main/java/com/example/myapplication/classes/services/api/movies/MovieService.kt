package com.example.myapplication.classes.services.api.movies

import com.example.myapplication.classes.models.response.CreditsResponse
import com.example.myapplication.classes.models.response.GenreResponse
import com.example.myapplication.classes.models.response.MovieDTO
import com.example.myapplication.classes.models.response.MovieRemote
import com.example.myapplication.classes.models.response.VideosResponse

interface MovieService {
    suspend fun getNowPlaying(page: Int): MovieRemote
    suspend fun getTopRated(page: Int): MovieRemote
    suspend fun searchMovies(query: String, page: Int): MovieRemote
    suspend fun getYoutubeTrailer(movieId: Int): VideosResponse
    suspend fun getMovieDetails(id: Int): MovieDTO
    suspend fun getMovieCredits(id: Int): CreditsResponse
    suspend fun getGenresList(): GenreResponse
    suspend fun getFilterList(startDate: String, endDate: String, genres: String?, sortBy: String, page: Int): MovieRemote
}