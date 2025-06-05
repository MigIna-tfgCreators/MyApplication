package com.example.myapplication.classes.extensions

val String.extractClaveYoutube: String
    get() = Regex("v=([^&]+)").find(this)?.groupValues?.get(1).valueOrEmpty

val String?.valueOrEmpty: String
    get() = this ?: ""

val String?.valueOrNoReview: String
    get() = if(this.isNullOrBlank()) "Sin reseña"
            else this
