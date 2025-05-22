package com.example.myapplication.classes.models.response

import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("results")
    val videoResults: List<VideoDTO>? = emptyList()
)

data class VideoDTO(
    @SerializedName("id") val videoId: String? = "",

    @SerializedName("key") val videoKey: String? = "",

    @SerializedName("name") val videoName: String? = "",

    @SerializedName("site") val videoSite: String? = "",

    @SerializedName("type") val videoType: String? = ""
)