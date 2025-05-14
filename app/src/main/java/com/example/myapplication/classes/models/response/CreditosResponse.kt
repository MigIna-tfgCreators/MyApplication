package com.example.myapplication.classes.models.response

import com.google.gson.annotations.SerializedName

data class CreditosResponse(
    @SerializedName("cast")
    val cast: List<MiembroRepartoResponse>,
    @SerializedName("crew")
    val crew: List<MiembroEquipoResponse>
)

data class MiembroRepartoResponse(
    @SerializedName("name") val nombre: String,
    @SerializedName("character") val personaje: String,
    @SerializedName("profile_path") val foto: String?
)

data class MiembroEquipoResponse(
    @SerializedName("name") val nombre: String,
    @SerializedName("job") val trabajo: String,
    @SerializedName("profile_path") val foto: String?
)
