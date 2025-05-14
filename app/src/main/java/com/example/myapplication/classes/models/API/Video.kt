package com.example.myapplication.classes.models.API

import com.example.myapplication.classes.models.response.VideoDTO
import com.google.gson.annotations.SerializedName

data class Video(
    val clave: String = "",
    val sitio: String = "",
    val tipo: String = ""
){
    companion object{
        val EMPTY = Video()
    }
}

val VideoDTO.toModel: Video
    get(){
        return Video(
            clave = clave,
            sitio = sitio,
            tipo = tipo
        )
    }


