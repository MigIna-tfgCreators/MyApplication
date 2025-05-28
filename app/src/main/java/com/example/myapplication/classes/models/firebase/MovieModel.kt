package com.example.myapplication.classes.models.firebase

import com.example.myapplication.classes.models.API.Movie
import java.time.LocalDate

data class MovieModel(
    val movieId: Int,
    var ownVote: Int?,
    var ownVoteDate: String?,
    var movieAverageVotes: Float?,
    var userReview: String?
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