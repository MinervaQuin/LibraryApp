package com.example.libraryapp.model

import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun getBook(bookId: String): Book
}