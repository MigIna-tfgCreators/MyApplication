package com.example.myapplication.classes.models.API

import com.example.myapplication.classes.models.response.CreditosResponse
import com.example.myapplication.classes.models.response.MiembroEquipoResponse
import com.example.myapplication.classes.models.response.MiembroRepartoResponse

data class Creditos (
    val cast: List<MiembroReparto> = emptyList(),
    val crew: List<MiembroEquipo> = emptyList()
){
    companion object{
        val EMPTY = Creditos()
    }
}

data class MiembroReparto(
    val nombre: String = "",
    val personaje: String = ""
)

data class MiembroEquipo(
    val nombre: String = "",
    val trabajo: String = ""
)

val MiembroEquipoResponse.toModel: MiembroEquipo
    get(){
        return MiembroEquipo(
            nombre = nombre,
            trabajo = trabajo
        )
    }

val MiembroRepartoResponse.toModel: MiembroReparto
    get(){
        return MiembroReparto(
            nombre = nombre,
            personaje = personaje
        )
    }

val CreditosResponse.toModel: Creditos
    get(){
        return Creditos(
            cast = cast.map { it.toModel },
            crew = crew.map { it.toModel }
        )
    }
