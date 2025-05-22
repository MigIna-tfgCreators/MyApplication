package com.example.myapplication.classes.repositories.PeliculasRepository

import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.models.API.Credits
import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.API.Video
import com.example.myapplication.classes.models.API.toModel
import com.example.myapplication.classes.services.api.movies.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepositoryImpl(
    private val service: MovieService
): MoviesRepository {

    override suspend fun getNowPlaying(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            val response = service.getNowPlaying(page)

            response.movieResults?.map { it.toModel }.valueOrEmpty
        }
    }


    override suspend fun getTopRated(page: Int): List<Movie> {
        return withContext(Dispatchers.IO) {
            val response = service.getTopRated(page)

            response.movieResults?.map { it.toModel }.valueOrEmpty
        }
    }


    override suspend fun searchMovies(query: String, page: Int): List<Movie> {

        return withContext(Dispatchers.IO) {
            val response = service.searchMovies(query,page)

            response.movieResults?.map { it.toModel }.valueOrEmpty
        }
    }

    override suspend fun getYoutubeTrailer(movieId: Int): List<Video> {
        return withContext(Dispatchers.IO) {

            val response = service.getYoutubeTrailer(movieId)

            response.videoResults?.map { it.toModel }.valueOrEmpty
        }
    }
    override suspend fun getMovieDetails(id: Int): Movie {
        return withContext(Dispatchers.IO) {
            service.getMovieDetails(id).toModel
        }
    }

    override suspend fun getMovieCredits(id: Int): Credits {
        return withContext(Dispatchers.IO) {
            service.getMovieCredits(id).toModel
        }
    }

    override suspend fun getListGenres(): List<Genre> {
        return withContext(Dispatchers.IO) {
            val response = service.getGenresList()
            response.listGenres?.map { it }.valueOrEmpty
        }
    }

    override suspend fun getFilterList(page: Int, genres: List<Genre>,dates: String, sortBy: String): List<Movie> {
        return withContext(Dispatchers.IO) {
            val genreParams = genres.joinToString(separator = ",") { it.genreId.toString() }

            val endDate = if(!dates.substringAfter("&").isEmpty())
                dates.substringAfter("&")
            else
                ""

            val startDate = if(!dates.substringBefore("&").isEmpty())
                dates.substringBefore("&")
            else
                ""
            val response = service.getFilterList(page = page, genres = genreParams, endDate = endDate, startDate = startDate, sortBy = sortBy)
            response.movieResults?.map { it.toModel } ?: emptyList()
        }
    }

}