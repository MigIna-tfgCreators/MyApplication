package com.example.myapplication.classes.models.response

import com.google.gson.annotations.SerializedName

data class CreditosResponse(
    @SerializedName("cast")
    val cast: List<MiembroReparto>,
    @SerializedName("crew")
    val crew: List<MiembroEquipo>
)

data class MiembroReparto(
    @SerializedName("name") val nombre: String,
    @SerializedName("character") val personaje: String,
    @SerializedName("profile_path") val foto: String?
)

data class MiembroEquipo(
    @SerializedName("name") val nombre: String,
    @SerializedName("job") val trabajo: String,
    @SerializedName("profile_path") val foto: String?
)
