package com.example.myapplication.classes.services.firebase.movieService

import android.util.Log
import com.example.myapplication.classes.models.firebase.MovieModel
import com.example.myapplication.classes.models.firebase.UserMovieExtraInfo
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class MovieUserServiceImpl: MovieUserService {

    override val db: FirebaseFirestore
        get() =  FirebaseFirestore.getInstance()

    override suspend fun addPersonalMovie(movie: MovieModel, uid: String) {
        try {
            db.collection("Usuarios").document(uid)
                .collection("Lista Personal").document(movie.movieId.toString()).set(movie).await()

            Log.i("Firestore", "Valor quitado de la lista personal")
        } catch (e: Exception) {
            throw Exception("No se ha podido añadir la pelicula")
        }
    }

    override suspend fun quitPersonalMovie(movie: MovieModel, uid: String) {
        try {
            db.collection("Usuarios").document(uid)
                .collection("Lista Personal").document(movie.movieId.toString()).delete().await()

            Log.i("Firestore", "Valor quitado de la lista personal")
        } catch (e: Exception) {
            throw Exception("No se ha podido quitar la pelicula")
        }
    }

    override suspend fun getCurrentUser(): String = FirebaseAuth.getInstance().currentUser?.uid.toString()


    override suspend fun hasUserMovie(movieId: Int, uid: String): Boolean {
        val movie = getMovieById(uid, movieId)
        return movie != null
    }

    override suspend fun getPersonalList(uid: String): List<MovieModel> {
        return try{

            val snapshot = db.collection("Usuarios").document(uid).collection("Lista Personal").get().await()

            snapshot.documents.mapNotNull { doc -> doc.toObject(MovieModel::class.java) }

        } catch (e: Exception) {
            Log.e("Firestore", "Error al obtener lista personal: ${e.message}", e)
            throw Exception("No se ha podido conseguir la lista personal o está vacía: ${e.message}")
        }
    }


    override suspend fun getMovieById(uid: String, movieId: Int): MovieModel? {
        return try{
            val document = db.collection("Usuarios").document(uid).collection("Lista Personal").document(movieId.toString()).get().await()

            document.toObject(MovieModel::class.java)
        } catch (e: Exception){ null }
    }

    override suspend fun modifyMovieData(uid: String,extraInfo: UserMovieExtraInfo?): MovieModel? {
        return try{

            val document = db.collection("Usuarios").document(uid).collection("Lista Personal")
                .document("extraInfo").update(mapOf(
                    "ownVote" to extraInfo?.ownVote,
                    "ownVoteDate" to extraInfo?.ownVoteDate,
                    "userReview" to extraInfo?.userReview
                )).await()

null
        } catch (e: Exception){ null }
    }
}