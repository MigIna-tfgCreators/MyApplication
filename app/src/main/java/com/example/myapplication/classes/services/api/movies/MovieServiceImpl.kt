package com.example.myapplication.classes.services.api.movies

import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.models.response.PeliculaDTO
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.models.response.VideosResponse
import com.example.myapplication.classes.services.api.APIServiceInterface
import com.example.myapplication.classes.services.api.BaseApiService

class MovieServiceImpl(
    private val bbdd: APIServiceInterface
):  MovieService, BaseApiService() {
    override suspend fun obtenerCartelera(page: Int): PeliculaResponsePaginada {
        return resumeSingle(bbdd.obtenerCartelera(page = page))
    }

    override suspend fun obtenerTop(page: Int): PeliculaResponsePaginada {
        return resumeSingle(bbdd.obtenerTop(page = page))
    }

    override suspend fun buscarPeliculas(query: String, page: Int): PeliculaResponsePaginada {
        return resumeSingle(bbdd.buscarPeliculas(query = query,page = page))
    }

    override suspend fun obtenerTrailerYouTube(idPelicula: Int): VideosResponse {
        return resumeSingle(bbdd.obtenerVideosPelicula(movieId = idPelicula))
    }

    override suspend fun obtenerDetalles(id: Int): PeliculaDTO {
        return resumeSingle(bbdd.obtenerDetallesPelicula(movieId = id))
    }

    override suspend fun obtenerCreditos(id: Int): CreditosResponse {
        return resumeSingle(bbdd.obtenerCreditos(movieId = id))
    }

}