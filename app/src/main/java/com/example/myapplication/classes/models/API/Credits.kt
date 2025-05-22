package com.example.myapplication.classes.models.API

import com.example.myapplication.classes.extensions.valueOrEmpty
import com.example.myapplication.classes.models.response.CreditsResponse
import com.example.myapplication.classes.models.response.CrewMemberResponse
import com.example.myapplication.classes.models.response.CastMemberResponse

data class Credits (
    val cast: List<CastMember> = emptyList(),
    val crew: List<CrewMember> = emptyList()
)

data class CastMember(
    val nameCast: String = "",
    val characterCast: String = ""
)

data class CrewMember(
    val nameCrew: String = "",
    val jobCrew: String = ""
)

val CrewMemberResponse.toModel: CrewMember
    get(){
        return CrewMember(
            nameCrew = crewName.valueOrEmpty,
            jobCrew = crewJob.valueOrEmpty
        )
    }

val CastMemberResponse.toModel: CastMember
    get(){
        return CastMember(
            nameCast = castMovie.valueOrEmpty,
            characterCast = castCharacter.valueOrEmpty
        )
    }

val CreditsResponse.toModel: Credits
    get(){
        return Credits(
            cast = cast?.map { it.toModel }.valueOrEmpty,
            crew = crew?.map { it.toModel }.valueOrEmpty
        )
    }
