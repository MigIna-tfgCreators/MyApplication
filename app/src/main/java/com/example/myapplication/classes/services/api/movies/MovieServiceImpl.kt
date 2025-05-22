package com.example.myapplication.classes.services.api.movies

import com.example.myapplication.classes.models.response.CreditsResponse
import com.example.myapplication.classes.models.response.GenreResponse
import com.example.myapplication.classes.models.response.MovieDTO
import com.example.myapplication.classes.models.response.MovieRemote
import com.example.myapplication.classes.models.response.VideosResponse
import com.example.myapplication.classes.services.api.APIServiceInterface
import com.example.myapplication.classes.services.api.BaseApiService

class MovieServiceImpl(
    private val bbdd: APIServiceInterface
):  MovieService, BaseApiService() {
    override suspend fun getNowPlaying(page: Int): MovieRemote {
        return resumeSingle(bbdd.getNowPlaying(page = page))
    }

    override suspend fun getTopRated(page: Int): MovieRemote {
        return resumeSingle(bbdd.getTopRated(page = page))
    }

    override suspend fun searchMovies(query: String, page: Int): MovieRemote {
        return resumeSingle(bbdd.searchMovies(query = query,page = page))
    }

    override suspend fun getYoutubeTrailer(movieId: Int): VideosResponse {
        return resumeSingle(bbdd.getYoutubeVideo(movieId = movieId))
    }

    override suspend fun getMovieDetails(id: Int): MovieDTO {
        return resumeSingle(bbdd.getMovieDetails(movieId = id))
    }

    override suspend fun getMovieCredits(id: Int): CreditsResponse {
        return resumeSingle(bbdd.getMovieCredits(movieId = id))
    }

    override suspend fun getGenresList(): GenreResponse {
        return resumeSingle(bbdd.getGenresList())
    }

    override suspend fun getFilterList(startDate: String, endDate: String, genres: String?, sortBy: String, page: Int): MovieRemote {
        val safeStartDate = startDate.takeIf { it.isNotBlank() }
        val safeEndDate = endDate.takeIf { it.isNotBlank() }

        return resumeSingle(bbdd.getFilterList(startDate = safeStartDate, endDate = safeEndDate, genres = genres, sortBy = sortBy, page = page))
    }

}