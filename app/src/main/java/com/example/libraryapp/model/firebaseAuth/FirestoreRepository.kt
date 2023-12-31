package com.example.libraryapp.model.firebaseAuth

import android.content.Context
import android.net.Uri
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Collection
import com.example.libraryapp.model.resources.Review
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import java.time.LocalDate

interface FirestoreRepository {
    val dataBase: FirebaseFirestore?
    val authConection: FirebaseAuth?
    val storageDataBase: FirebaseStorage?
    val tapClient: SignInClient?
    val contextFeo: Context
    suspend fun getBook(bookId: String): Book?
    suspend fun getAllBooks(booksIds: List<String>): List<Book?>
    suspend fun getAllBooks2(): List<Book?>

    suspend fun searchAllBooks(allBooks: List<Book?>, searchString: String): List<Book?>

    suspend fun getAuthor(authorId: String) : Author?
    suspend fun getAllAutors(): List<Author?>
    suspend fun getCollection(collectionId: String) : Collection?
    suspend fun getReviews(bookId: String): List<Review?>

    suspend fun upLoadReview(bookId: String, review: Review)

/*
    suspend fun upLoadUserInfo(field: String): Unit

     */
    suspend fun timestampToLocalDate(timestamp: Timestamp): LocalDate
    suspend fun localDateToTimestamp(date: LocalDate?): Timestamp?

    suspend fun uploadBookToFirestore()
    suspend fun addNewAttribute()
    suspend fun getReviewsFromABook(bookId: String): List<Review?>
    suspend fun getReviewFromUserId(bookId: String, userId: String): Review?
    suspend fun updateReview(bookId: String, reviewId: String, newData: Map<String, Any>)

    suspend fun getuser(): UserData?

    suspend fun signOut()
    suspend fun uploadBookScore(bookId: String, newScore: Int)
}