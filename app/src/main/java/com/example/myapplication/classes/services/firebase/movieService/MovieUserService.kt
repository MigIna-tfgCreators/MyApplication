package com.example.myapplication.classes.services.firebase.movieService

import com.example.myapplication.classes.models.firebase.MovieModel
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.google.firebase.firestore.FirebaseFirestore

interface MovieUserService {
    suspend fun addPersonalMovie(movie: MovieModel, uid: String)
    suspend fun quitPersonalMovie(movie: MovieModel, uid: String)
    suspend fun getCurrentUser(): String
    suspend fun hasUserMovie(movieId: Int, uid: String): Boolean
    suspend fun getPersonalList(uid: String): List<MovieModel>
    suspend fun getMovieById(uid: String, movieId: Int): MovieModel?
    suspend fun modifyMovieData(uid: String,movieId: Int,extraInfo: UserMovieExtraInfo?): MovieModel?
    val db: FirebaseFirestore

}