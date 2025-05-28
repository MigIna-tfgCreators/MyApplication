package com.example.myapplication.classes.models.firebase

import com.example.myapplication.classes.models.API.Movie
import java.time.LocalDate

data class MovieModel(
    val movieId: Int = 0,
    var ownVote: Int? = null,
    var ownVoteDate: String? = null,
    var movieAverageVotes: Float? = null,
    var userReview: String? = null
)

data class UserMovieExtraInfo(
    val ownVote: Int,
    val ownVoteDate: String = LocalDate.now().toString(),
    val userReview: String
)

val Movie.toFirebaseModel: MovieModel
    get(){
        return MovieModel(
            movieId = this.movieId,
            movieAverageVotes = this.movieAverageVote,
            ownVote = null,
            ownVoteDate = "",
            userReview = ""
        )
    }