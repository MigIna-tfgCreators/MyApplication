package com.example.myapplication.classes.models.response

import com.google.gson.annotations.SerializedName

data class CreditsResponse(
    @SerializedName("cast") val cast: List<CastMemberResponse>?,

    @SerializedName("crew") val crew: List<CrewMemberResponse>?
)

data class CastMemberResponse(
    @SerializedName("name") val castMovie: String?,
    @SerializedName("character") val castCharacter: String?,
    @SerializedName("profile_path") val castPhoto: String?
)

data class CrewMemberResponse(
    @SerializedName("name") val crewName: String?,
    @SerializedName("job") val crewJob: String?,
    @SerializedName("profile_path") val crewPhoto: String?
)
