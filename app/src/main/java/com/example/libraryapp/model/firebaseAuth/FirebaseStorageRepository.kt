package com.example.libraryapp.model.firebaseAuth

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

interface FirebaseStorageRepository {
    val authConection: FirebaseAuth?
    val storageDataBase: FirebaseStorage?
    suspend fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit)
    suspend fun getProfileImageUrl(userId: String): String
}