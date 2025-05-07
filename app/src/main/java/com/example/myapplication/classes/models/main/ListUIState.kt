package com.example.myapplication.classes.models.main

import com.example.myapplication.classes.models.API.PeliculaModel

data class ListUIState(
    val actualList: List<PeliculaModel>? = null,
    val isLoading: Boolean = false,
    val error: String? = null,
    val url: String? = null
)
