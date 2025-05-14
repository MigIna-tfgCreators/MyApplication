package com.example.myapplication.classes.modules.main.activity.model

import com.example.myapplication.classes.models.API.Pelicula

data class ListUIState(
    val actualList: List<Pelicula>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val url: String? = null
)