package com.example.libraryapp.model.firebaseAuth

import android.net.Uri
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseStorageImpl @Inject constructor(
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
): FirebaseStorageRepository{

    override val authConection: FirebaseAuth?
        get() = auth

    override val storageDataBase: FirebaseStorage?
        get() = storage
    override suspend fun uploadImageToFirebase(
        imageUri: Uri,
        onSuccess: (String) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("users/$userId/profilePicture.jpg")

        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }


    override suspend fun getProfileImageUrl(userId: String): String {

        val storageRef = storage.reference.child("users/$userId/profilePicture.jpg")
        return try {
            val uri = storageRef.downloadUrl.await()
            uri.toString()
        } catch (exception: StorageException) {

            if (exception.errorCode == StorageException.ERROR_OBJECT_NOT_FOUND) {

                throw exception
            } else {

                throw exception
            }
        }
    }
}