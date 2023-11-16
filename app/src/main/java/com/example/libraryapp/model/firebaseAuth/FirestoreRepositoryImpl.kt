package com.example.libraryapp.model.firebaseAuth

import android.util.Log
import com.example.libraryapp.model.Book
import com.example.libraryapp.model.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore): FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore

    override suspend fun getBook(bookId: String): Book? {
        return try {
            val document = firebaseFirestore.collection("books").document(bookId).get().await()
            if (document != null) {
                document.toObject(Book::class.java)
            } else {
                null
            }
        } catch (exception: Exception) {
            Log.d("FirestoreRepository", "get failed with ", exception)
            null
        }
    }
}