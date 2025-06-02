package com.example.myapplication.classes.models.firebase

import com.example.myapplication.classes.extensions.valueOrNoReview
import com.example.myapplication.classes.models.API.Movie
import java.time.LocalDate

data class MovieModel(
    val movieId: Int = 0,
    var movieAverageVotes: Float? = null,
    var extraInfo: UserMovieExtraInfo? = null
)

data class UserMovieExtraInfo(
    val ownVote: Int = 0,
    val ownVoteDate: String = LocalDate.now().toString(),
    val userReview: String? = String().valueOrNoReview
)

val Movie.toFirebaseModel: MovieModel
    get(){
        return MovieModel(
            movieId = this.movieId,
            movieAverageVotes = this.movieAverageVote,
            extraInfo = UserMovieExtraInfo(
                ownVote = 0,
                ownVoteDate = "",
                userReview = String().valueOrNoReview
            )
        )
    }