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
            val movieFB = movie.toFirebaseModel


            movieFB.extraInfo = extraInfo

            bbdd.addPersonalMovie(movieFB, bbdd.getCurrentUser())
        }
    }

    override suspend fun quitPersonalMovie(movie: Movie) {
        return withContext(Dispatchers.IO) {
            val movieFB = bbdd.getMovieById(bbdd.getCurrentUser(),movie.movieId)

            if(movieFB != null)
                bbdd.quitPersonalMovie(movieFB, bbdd.getCurrentUser())
        }
    }

    override suspend fun getPersonalList(): List<MovieModel> {
        return withContext(Dispatchers.IO) {
            bbdd.getPersonalList(bbdd.getCurrentUser()).sortedByDescending { it.extraInfo?.ownVote }
        }
    }

    override suspend fun checkUserMovie(movieId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            bbdd.hasUserMovie(movieId,bbdd.getCurrentUser())
        }
    }

    override suspend fun getExtraInfo(movieId: Int): UserMovieExtraInfo {
        return withContext(Dispatchers.IO) {
            val movieFB = bbdd.getMovieById(bbdd.getCurrentUser(),movieId)
            movieFB?.extraInfo ?: UserMovieExtraInfo()
        }
    }

    override suspend fun getPersonalInformation(): List<Double> {
        return withContext(Dispatchers.IO) {
            val list = bbdd.getPersonalList(bbdd.getCurrentUser())
            val moviesCount = list.size
            var averageVotes = 0.00

            for(movie in list){
                averageVotes += movie.extraInfo?.ownVote.valueOrZero
            }

            averageVotes = averageVotes/moviesCount

            listOf<Double>(moviesCount.toDouble(), averageVotes)

        }
    }

}