package com.example.myapplication.classes.services.authUserService

import com.google.firebase.firestore.FirebaseFirestore

interface AuthService {
    suspend fun session(): String?
    suspend fun register(name: String,email: String, pswd: String): Boolean
    suspend fun login(email: String, pswd: String): Boolean
    suspend fun checkUser(email: String, pswd: String, create: Boolean): String?
    val db: FirebaseFirestore
}