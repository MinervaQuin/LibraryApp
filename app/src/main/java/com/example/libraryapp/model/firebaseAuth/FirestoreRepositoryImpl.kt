package com.example.libraryapp.model.firebaseAuth

import android.util.Log
import com.example.libraryapp.model.Author
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
            Log.d("FirestoreRepository", "getBook failed with ", exception)
            null
        }
    }

    override suspend fun getAuthor(authorId: String): Author? {

        return try {
            val documentSnapshot = firebaseFirestore.collection("authors").document(authorId).get().await()

            if (documentSnapshot.exists()) {

                val author = Author()
                var bookArray: MutableList<Book?> = mutableListOf()

                author.name = documentSnapshot.getString("name") ?: "No se ha encontrado un nombre"
                author.biography = documentSnapshot.getString("biography")?: "No se ha encontrado una Biografía"

                val arrayAux = documentSnapshot.get("works") as? List<String> ?: listOf()
                Log.d("coso array", "$arrayAux")
                arrayAux.forEach() {book ->
                    bookArray.add(getBook(book))
                }
                author.works = bookArray.toTypedArray()
                author
            } else {
                null
            }
        } catch (exception: Exception) {
            Log.d("FirestoreRepository", "getAuthor failed with ", exception)
            null // Retorna null en caso de error
        }

    }
    suspend fun getCollection(collectionId: String) : Collection?{

    }
}