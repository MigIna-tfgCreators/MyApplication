package com.example.myapplication.classes.repositories.PeliculasRepository

import com.example.myapplication.classes.models.API.Credits
import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.API.Video

interface MoviesRepository {
    suspend fun getNowPlaying(page: Int): List<Movie>
    suspend fun getAllNowPlaying(): List<Movie>
    suspend fun getTopRated(page: Int): List<Movie>
    suspend fun searchMovies(query: String, page: Int): List<Movie>
    suspend fun getYoutubeTrailer(movieId: Int): List<Video>
    suspend fun getMovieDetails(id: Int): Movie
    suspend fun getMovieCredits(id: Int): Credits
    suspend fun getListGenres(): List<Genre>
    suspend fun getFilterList(page: Int, genresIds: List<Int>, dates: String, sortBy: String): List<Movie>
}