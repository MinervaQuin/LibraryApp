package com.example.libraryapp.model.firebaseAuth

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class EmailAuthUiClient @Inject constructor(
    //need a context?
    private val auth: FirebaseAuth
){
    suspend fun signInWithEmail(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun registerUser(email: String, password: String, username: String): Result<Unit> {
        return try {
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()
            val userProfileChangeRequest = userProfileChangeRequest {
                displayName = username
            }
            authResult.user?.updateProfile(userProfileChangeRequest)?.await()
            Result.success(Unit) // Devuelve Unit en caso de éxito
        } catch (e: Exception) {
            Result.failure(e) // Devuelve la excepción en caso de error
        }
    }

}