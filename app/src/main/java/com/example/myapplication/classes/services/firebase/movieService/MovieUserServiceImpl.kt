package com.example.myapplication.classes.services.firebase.movieService

import android.util.Log
import com.example.myapplication.classes.models.API.Genre
import com.example.myapplication.classes.models.firebase.MovieModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MovieUserServiceImpl: MovieUserService {

    override val db: FirebaseFirestore
        get() =  FirebaseFirestore.getInstance()

    override suspend fun addPersonalMovie(movie: MovieModel, uid: String) {
        try {
            db.collection("Usuarios").document(uid)
                .update("Lista Personal", FieldValue.arrayUnion(movie)).await()
            Log.i("Firestore", "Valor quitado de la lista personal")
        } catch (e: Exception) {
            throw Exception("No se ha podido añadir la pelicula")
        }
    }

    override suspend fun quitPersonalMovie(movie: MovieModel, uid: String) {
        try {
            db.collection("Usuarios").document(uid)
                .update("Lista Personal", FieldValue.arrayRemove(movie)).await()
            Log.i("Firestore", "Valor quitado de la lista personal")
        } catch (e: Exception) {
            throw Exception("No se ha podido quitar la pelicula")
        }
    }

    override suspend fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override suspend fun hasUserMovie(movieId: Int, uid: String): Boolean {
        val document = db.collection("Usuarios").document(uid).get().await()
        val favList = document.get("Lista Personal")
        if(favList is List<*>) {
            val list = sendPersonalList(favList)
            return list.any{ it.movieId == movieId}
        }
        return false
    }

    override suspend fun getPersonalList(uid: String): List<MovieModel> {
        return try{

            val document = db.collection("Usuarios").document(uid).get().await()
            val persList = document.get("Lista Personal")

            return if(persList is List<*>)
                sendPersonalList(persList)
            else
                emptyList()

        }catch (e: Exception) {
            throw Exception("No se ha podido conseguir la lista personal o esta vacia")
            emptyList()
        }
    }


    override suspend fun getMovieById(uid: String, movieId: Int): MovieModel? {
        val document = db.collection("Usuarios").document(uid).get().await()
        val favList = document.get("Lista Personal")

        if (favList is List<*>) {
            val personalList = sendPersonalList(favList)
            return personalList.find { it.movieId == movieId }
        }
        return null
    }

    override suspend fun sendPersonalList(list: List<*>): List<MovieModel> {
        return list.mapNotNull { item ->
            val map = item as? Map<String, Any> ?: return@mapNotNull null
            MovieModel(
                movieId = (map["movieId"] as? Number)?.toInt() ?: 0,
                ownVote = (map["ownVote"] as? Number)?.toInt() ?: 0,
                ownVoteDate = map["ownVoteDate"] as? String ?: "",
                movieAverageVotes = (map["movieAverageVotes"] as? Number)?.toFloat() ?: 0.00f,
                userReview = map["userReview"] as? String ?: ""
            )
        }
    }
}