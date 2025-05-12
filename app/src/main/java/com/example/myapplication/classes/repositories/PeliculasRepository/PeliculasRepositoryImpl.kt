package com.example.myapplication.classes.repositories.PeliculasRepository

import android.util.Log
import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.response.PeliculaResponsePaginada
import com.example.myapplication.classes.models.API.PeliculasDetalles
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.services.network.API
import com.example.myapplication.classes.services.network.PeliculaService
import com.example.myapplication.classes.services.network.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PeliculasRepositoryImpl(
    private val webService: WebService,
    private val peliculaService: PeliculaService
): PeliculasRepository {

    override suspend fun obtenerCartelera(page: Int): PeliculaResponsePaginada {
        return withContext(Dispatchers.IO) {
            val response = webService.obtenerCartelera(API.API_KEY, page, API.REGION)

            if(response.isSuccessful){
                response.body() ?: throw Exception("Error en la respuesta es nula")
            }else{
                throw Exception("Error en la respuesta")
            }
        }
    }


    override suspend fun obtenerTop(): List<PeliculaModel> {
        return withContext(Dispatchers.IO) {
            webService.obtenerTop(API.API_KEY).body()?.results?.sortedByDescending {
                it.votoPromedio
            } ?: listOf()
        }
    }

    override suspend fun buscarPeliculas(query: String, page: Int): PeliculaResponsePaginada {

        return withContext(Dispatchers.IO) {
            val response = webService.buscarPeliculas(API.API_KEY, query,"es-ES",page)

            response.body() ?: PeliculaResponsePaginada(0,0,0,emptyList())

        }
    }

    override suspend fun obtenerTrailerYouTube(idPelicula: Int): String {
        return withContext(Dispatchers.IO) {
            val response = webService.obtenerVideosPelicula(idPelicula, API.API_KEY)
            Log.d("Identificador4","${response.body().toString()}")
            if(response.isSuccessful){
                val trailer = response.body()?.resultados?.firstOrNull{
                    it.sitio == "YouTube" && it.tipo == "Trailer"
                }
                trailer?.let {
                    "https://www.youtube.com/watch?v=${it.clave}"
                } ?: "Trailer no disponible"
            }
            else{
                "Error ${response.code()}"
            }
        }
    }
    override suspend fun obtenerDetalles(id: Int): PeliculasDetalles? {
        return withContext(Dispatchers.IO) {
            val response = peliculaService.obtenerDetallesPelicula(id, API.API_KEY).execute()
            if (response.isSuccessful) response.body() else null
        }
    }

    override suspend fun obtenerCreditos(id: Int): CreditosResponse? {
        return withContext(Dispatchers.IO) {
            val response = peliculaService.obtenerCreditos(id, API.API_KEY).execute()
            if (response.isSuccessful) response.body() else null
        }
    }

}