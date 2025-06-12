package com.example.myapplication.classes.modules.main.search.model

import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo


sealed class SearchEvents {
    object GetFilterList: SearchEvents()
    object GetListGenres: SearchEvents()
    object ResetAll: SearchEvents()
    object ResetFav: SearchEvents()
    object ClearMovies: SearchEvents()
    object ClearErrors: SearchEvents()
    data class ApplyFilters(val genresIds: List<Int>?, val dates: String?, val order: String?): SearchEvents()
    data class SearchMovies(val query: String): SearchEvents()
    data class HasInPersonal(val movie: Movie, val info: UserMovieExtraInfo?): SearchEvents()
    data class CheckMovie(val movie: Movie): SearchEvents()

}