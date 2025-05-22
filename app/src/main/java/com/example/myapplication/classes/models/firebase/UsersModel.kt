package com.example.myapplication.classes.models.firebase

import com.example.myapplication.classes.models.API.Movie

data class UsersModel (
    val uid: String,
    val userName: String,
    val userEmail: String,
    val userPswd: String,
    val userPersonalList: List<Movie>
)