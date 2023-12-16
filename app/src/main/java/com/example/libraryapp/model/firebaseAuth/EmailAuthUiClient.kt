package com.example.libraryapp.model.firebaseAuth

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.R
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject

class EmailAuthUiClient @Inject constructor(
    //need a context?
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorageRepository


){
    private val imageUri = Uri.parse("android.resource://com.example.libraryapp/${R.drawable.fotopredefinida}")
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
            // Crear usuario
            val authResult = auth.createUserWithEmailAndPassword(email, password).await()

            // Actualizar perfil del usuario
            val userProfileChangeRequest = userProfileChangeRequest {
                displayName = username
            }
            authResult.user?.updateProfile(userProfileChangeRequest)?.await()

            // Subir imagen a Firebase
            storage.uploadImageToFirebase(imageUri, onSuccess = {
                // Manejo de éxito al subir imagen
            }, onFailure = { e ->
                // Manejo de error al subir imagen
                throw e // Lanza una excepción para ser capturada por el bloque catch
            })

            Result.success(Unit) // Devuelve Unit en caso de éxito
        } catch (e: Exception) {
            Result.failure(e) // Devuelve la excepción en caso de error
        }
    }

}