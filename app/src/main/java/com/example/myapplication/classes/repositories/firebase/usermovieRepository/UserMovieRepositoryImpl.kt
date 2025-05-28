package com.example.myapplication.classes.repositories.firebase.usermovieRepository

import android.util.Log
import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.extensions.valueOrZero
import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.classes.models.API.Movie
import com.example.myapplication.classes.models.firebase.MovieModel
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.example.myapplication.classes.models.firebase.toFirebaseModel
import com.example.myapplication.classes.services.firebase.movieService.MovieUserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import kotlin.Int

class UserMovieRepositoryImpl(
    private val bbdd: MovieUserService
):  UserMovieRepository{
    override suspend fun addPersonalMovie(movie: Movie, extraInfo: UserMovieExtraInfo?) {
        withContext(Dispatchers.IO) {
            Log.d("AYUDAME","ME Crean")
            val movieFB = movie.toFirebaseModel

            movieFB.ownVote = extraInfo?.ownVote
            movieFB.ownVoteDate = extraInfo?.ownVoteDate
            movieFB.userReview = extraInfo?.userReview

            bbdd.addPersonalMovie(movieFB, bbdd.getCurrentUser())
        }
    }

    override suspend fun quitPersonalMovie(movie: Movie) {
        return withContext(Dispatchers.IO) {
            Log.d("AYUDAME","ME MUERO")
            val movieFB = bbdd.getMovieById(bbdd.getCurrentUser(),movie.movieId)

            if(movieFB != null)
                bbdd.quitPersonalMovie(movieFB, bbdd.getCurrentUser())
        }
    }

    override suspend fun getPersonalList(): List<MovieModel> {
        return withContext(Dispatchers.IO) {
            val list = bbdd.getPersonalList(bbdd.getCurrentUser())
            Log.d("AYUDA SII",list.size.toString())
            list
        }
    }

    override suspend fun checkUserMovie(movieId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            bbdd.hasUserMovie(movieId,bbdd.getCurrentUser())
        }
    }

}