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
                resumeWithException(continuation, response.errorBody()?.toString())
            }
        }
    }

    suspend fun resumeEmpty(response: Response<Unit>) {
        return suspendCoroutine { continuation ->
            if (response.isSuccessful) {
                continuation.resumeWith(Result.success(Unit))
            } else {
                resumeWithException(continuation, response.errorBody()?.toString())
            }
        }
    }

    suspend fun <T> resumeList(response: Response<List<T>>): List<T> {
        return suspendCoroutine { continuation ->
            val body = response.body()
            if (response.isSuccessful) {
                continuation.resumeWith(Result.success(body ?: listOf()))
            } else {
                resumeWithException(continuation, response.errorBody()?.toString())
            }
        }
    }

    fun <T> resumeWithException(continuation: Continuation<T>, message: String?) {
        continuation.resumeWithException(Exception(message))
    }
}