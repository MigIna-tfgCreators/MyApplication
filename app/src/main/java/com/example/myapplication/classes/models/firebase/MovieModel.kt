package com.example.myapplication.classes.models.firebase

import com.example.myapplication.classes.models.API.Genre

data class MovieModel(
    val movieId: Int?,
    val movieTitle: String?,
    val moviePoster: String?,
    val movieReleaseDate: String?,
    val movieGenres: List<Genre>?,
    val ownVote: Float?,
    val ownVoteDate: String?,
    val movieAverageVotes: Float?,
    val userReview: String?
)