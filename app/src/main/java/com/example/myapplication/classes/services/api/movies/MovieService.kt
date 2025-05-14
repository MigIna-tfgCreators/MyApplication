package com.example.myapplication.classes.services.api.movies

import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.models.response.PeliculaDTO
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.models.response.VideosResponse

interface MovieService {
    suspend fun obtenerCartelera(page: Int): PeliculaResponsePaginada
    suspend fun obtenerTop(page: Int): PeliculaResponsePaginada
    suspend fun buscarPeliculas(query: String, page: Int): PeliculaResponsePaginada
    suspend fun obtenerTrailerYouTube(idPelicula: Int): VideosResponse
    suspend fun obtenerDetalles(id: Int): PeliculaDTO
    suspend fun obtenerCreditos(id: Int): CreditosResponse
}