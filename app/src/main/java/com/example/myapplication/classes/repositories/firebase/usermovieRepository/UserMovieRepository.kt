package com.example.myapplication.classes.repositories.firebase.usermovieRepository

import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.MovieModel
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo

interface UserMovieRepository {
    suspend fun addPersonalMovie(movie: Movie, extraInfo: UserMovieExtraInfo?)
    suspend fun quitPersonalMovie(movie: Movie)
    suspend fun getPersonalList(): List<MovieModel>
    suspend fun checkUserMovie(movieId: Int): Boolean
    suspend fun getExtraInfo(movieId: Int): UserMovieExtraInfo
    suspend fun getPersonalInformation(): List<Double>
    suspend fun updateInformation(movieId: Int, extraInfo: UserMovieExtraInfo): MovieModel?
}