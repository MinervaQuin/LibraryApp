package com.example.libraryapp.model

import com.google.firebase.firestore.FirebaseFirestore

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    suspend fun getBook(bookId: String): Book?

    suspend fun getAuthor(authorId: String) : Author?
    suspend fun getCollection(collectionId: String) : Collection?/*
    suspend fun getReviews(bookId: String): List<review>
    suspend fun upLoadReview(bookId: String): Unit
    suspend fun getBooksFromCollection(collectionId: String): List<Book>
    suspend fun upLoadUserInfo(field: String): Unit

     */
}