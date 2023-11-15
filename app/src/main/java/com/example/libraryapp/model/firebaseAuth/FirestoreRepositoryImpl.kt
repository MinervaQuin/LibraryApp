package com.example.libraryapp.model.firebaseAuth

import com.example.libraryapp.model.Book
import com.example.libraryapp.model.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreRepositoryImpl @Inject constructor(private val firebaseFirestore: FirebaseFirestore): FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore

    override suspend fun getBook(bookId: String): Book {
        // Realiza la consulta a Firestore
        val documentSnapshot = firebaseFirestore.collection("books").document(bookId).get().await()

        // Verifica si el documento existe y tiene datos
        if (!documentSnapshot.exists()) {
            throw NoSuchElementException("No se encontró un libro con el ID: $bookId")
        }

        // Extrae los datos y crea una instancia de Book
        return documentSnapshot.toObject(Book::class.java) ?: throw Exception("Error al mapear los datos a la clase Book")
    }
}