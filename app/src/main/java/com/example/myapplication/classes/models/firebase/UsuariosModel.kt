package com.example.myapplication.classes.models.firebase

import com.example.myapplication.classes.models.API.Pelicula

data class UsuariosModel (
    var uid: String,
    var nombre: String,
    var correo: String,
    var contraseña: String,
    var listaPersonal: List<Pelicula>
)