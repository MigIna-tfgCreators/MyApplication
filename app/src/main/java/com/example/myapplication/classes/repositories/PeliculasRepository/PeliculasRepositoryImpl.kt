package com.example.myapplication.classes.repositories.PeliculasRepository

import android.util.Log
import com.example.myapplication.classes.models.API.Creditos
import com.example.myapplication.classes.models.API.Pelicula
import com.example.myapplication.classes.models.API.Video
import com.example.myapplication.classes.models.API.toModel
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.services.api.API
import com.example.myapplication.classes.services.api.movies.MovieService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PeliculasRepositoryImpl(
    private val service: MovieService
): PeliculasRepository {

    override suspend fun obtenerCartelera(page: Int): List<Pelicula> {
        return withContext(Dispatchers.IO) {
            val response = service.obtenerCartelera(page)

            response.results.map { it.toModel }
        }
    }


    override suspend fun obtenerTop(page: Int): List<Pelicula> {
        return withContext(Dispatchers.IO) {
            val response = service.obtenerTop(page)

            response.results.map { it.toModel }
        }
    }


    override suspend fun buscarPeliculas(query: String, page: Int): List<Pelicula> {

        return withContext(Dispatchers.IO) {
            val response = service.buscarPeliculas(query,page)

            response.results.map { it.toModel }
        }
    }

    override suspend fun obtenerTrailerYouTube(idPelicula: Int): List<Video> {
        return withContext(Dispatchers.IO) {

            val response = service.obtenerTrailerYouTube(idPelicula)

            response.resultados.map { it.toModel }
        }
    }
    override suspend fun obtenerDetalles(id: Int): Pelicula {
        return withContext(Dispatchers.IO) {
            service.obtenerDetalles(id).toModel
        }
    }

    override suspend fun obtenerCreditos(id: Int): Creditos {
        return withContext(Dispatchers.IO) {
            service.obtenerCreditos(id).toModel
        }
    }

}