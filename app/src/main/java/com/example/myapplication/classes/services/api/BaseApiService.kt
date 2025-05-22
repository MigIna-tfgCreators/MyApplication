package com.example.myapplication.classes.services.api

import retrofit2.Response
import kotlin.coroutines.Continuation
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

open class BaseApiService {

    suspend fun <T> resumeSingle(response: Response<T>): T {
        return suspendCoroutine { continuation ->
            val body = response.body()
            if (response.isSuccessful && body != null) {
                continuation.resumeWith(Result.success(body))
            } else {
                val errorMessage = extractErrorMessage(response)
                resumeWithException(continuation, errorMessage)
            }
        }
    }

    suspend fun resumeEmpty(response: Response<Unit>) {
        return suspendCoroutine { continuation ->
            if (response.isSuccessful) {
                continuation.resumeWith(Result.success(Unit))
            } else {
                val errorMessage = extractErrorMessage(response)
                resumeWithException(continuation, errorMessage)
            }
        }
    }

    suspend fun <T> resumeList(response: Response<List<T>>): List<T> {
        return suspendCoroutine { continuation ->
            val body = response.body()
            if (response.isSuccessful) {
                continuation.resumeWith(Result.success(body ?: listOf()))
            } else {
                val errorMessage = extractErrorMessage(response)
                resumeWithException(continuation, errorMessage)
            }
        }
    }
    private fun extractErrorMessage(response: Response<*>): String {
        return try {
            response.errorBody()?.string()?.takeIf { it.isNotBlank() }
                ?: "Ha ocurrido un error inesperado (${response.code()})"
        } catch (e: Exception) {
            "Error al leer el cuerpo de error: ${e.localizedMessage}"
        }
    }

    fun <T> resumeWithException(continuation: Continuation<T>, message: String?) {
        continuation.resumeWithException(Exception(message))
    }
}