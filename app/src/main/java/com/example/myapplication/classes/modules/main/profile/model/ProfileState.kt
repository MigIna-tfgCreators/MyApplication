package com.example.myapplication.classes.modules.main.profile.model

data class ProfileState(
    val isLoading: Boolean = false,
    val error: String? = null,
    val userName: String? = null,
    val averageVotes: Double? = null,
    val totalPersonalFilms: Int? = null
)