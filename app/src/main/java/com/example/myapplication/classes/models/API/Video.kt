package com.example.myapplication.classes.models.API

import com.example.myapplication.classes.models.response.VideoDTO

data class Video(
    val videoKey: String = "",
    val videoSite: String = "",
    val videoType: String = ""
)

val VideoDTO.toModel: Video
    get(){
        return Video(
            videoKey = this@toModel.videoKey ?: "",
            videoSite = this@toModel.videoSite ?: "",
            videoType = this@toModel.videoType ?: ""
        )
    }


