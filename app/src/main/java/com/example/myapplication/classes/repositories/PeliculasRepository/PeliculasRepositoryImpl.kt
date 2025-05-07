package com.example.myapplication.classes.repositories.PeliculasRepository

import com.example.myapplication.classes.models.API.PeliculaModel
import com.example.myapplication.classes.models.API.PeliculasDetalles
import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.services.network.API
import com.example.myapplication.classes.services.network.PeliculaService
import com.example.myapplication.classes.services.network.WebService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.koin.core.component.KoinComponent

class PeliculasRepositoryImpl: PeliculasRepository, KoinComponent {

    private val webService: WebService by inject()
    private val peliculaService: PeliculaService by inject()

    override suspend fun obtenerCartelera(): List<PeliculaModel> {
        return withContext(Dispatchers.IO) {
            webService.obtenerCartelera(API.API_KEY).body()?.resultados?.sortedByDescending {
                it.votoPromedio
            } ?: listOf()
        }
    }

    override suspend fun obtenerTop(): List<PeliculaModel> {
        return withContext(Dispatchers.IO) {
            webService.obtenerTop(API.API_KEY).body()?.resultados?.sortedByDescending {
                it.votoPromedio
            } ?: listOf()
        }
    }

    override suspend fun obtenerTrailerYouTube(idPelicula: Int): String {
        return withContext(Dispatchers.IO) {
            val response = webService.obtenerVideosPelicula(idPelicula, API.API_KEY)
            if(response.isSuccessful){
                val trailer = response.body()?.resultados?.firstOrNull{
                    it.sitio == "Youtube" && it.tipo == "Trailer"
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
        return try {
            val response = peliculaService.obtenerDetallesPelicula(id, API.API_KEY).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun obtenerCreditos(id: Int): CreditosResponse? {
        return try {
            val response = peliculaService.obtenerCreditos(id, API.API_KEY).execute()
            if (response.isSuccessful) response.body() else null
        } catch (e: Exception) {
            null
        }
    }

}