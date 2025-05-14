package com.example.myapplication.classes.repositories.PeliculasRepository

import com.example.myapplication.classes.models.API.Creditos
import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.models.API.Video
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada

interface PeliculasRepository {
    suspend fun obtenerCartelera(page: Int): List<Pelicula>
    suspend fun obtenerTop(page: Int): List<Pelicula>
    suspend fun buscarPeliculas(query: String, page: Int): List<Pelicula>
    suspend fun obtenerTrailerYouTube(idPelicula: Int): List<Video>
    suspend fun obtenerDetalles(id: Int): Pelicula
    suspend fun obtenerCreditos(id: Int): Creditos
}