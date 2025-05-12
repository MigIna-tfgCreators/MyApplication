package com.example.myapplication.classes.repositories.PeliculasRepository

import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.API.PeliculasDetalles
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada

interface PeliculasRepository {
    suspend fun obtenerCartelera(page: Int): PeliculaResponsePaginada
    suspend fun obtenerTop(): List<PeliculaModel>
    suspend fun buscarPeliculas(query: String, page: Int): PeliculaResponsePaginada
    suspend fun obtenerTrailerYouTube(idPelicula: Int): String
    suspend fun obtenerDetalles(id: Int): PeliculasDetalles?
    suspend fun obtenerCreditos(id: Int): CreditosResponse?
}