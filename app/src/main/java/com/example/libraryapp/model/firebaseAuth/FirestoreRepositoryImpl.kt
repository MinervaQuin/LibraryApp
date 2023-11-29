package com.example.libraryapp.model.firebaseAuth

import android.util.Log
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Collection
import com.example.libraryapp.model.resources.Review
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.regex.Matcher
import java.util.regex.Pattern
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
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getBook failed with ", e)
            null
        }
    }

    override suspend fun getAllBooks(booksIds: List<String>): List<Book?> {
        val bookArray: MutableList<Book?> = mutableListOf()
        return try {
            booksIds.forEach { bookId ->
                val book = getBook(bookId)
                if(book != null){
                    bookArray.add(book)
                }
            }
            bookArray
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getAllBooks failed with ", e)
            emptyList()
        }
    }

    override suspend fun getAllBooks2(): List<Book?> {
        val bookArray: MutableList<Book?> = mutableListOf()
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection("books") // Reemplaza con el nombre de tu colección en Firestore
                .get()
                .await()

            for (document in querySnapshot.documents) {
                document.getString("title")?.let { Log.d("Firestore", it) }
                val book = document.toObject(Book::class.java)
                bookArray.add(book)
            }
            Log.d("Firestore", bookArray.size.toString())
            bookArray
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getAllBooks failed with ", e)
            emptyList()
        }
    }

    override suspend fun searchAllBooks(allBooks: List<Book?>, searchString: String): List<Book?> {
        val bookArray: MutableList<Book?> = mutableListOf()

        return try {
            val regexPattern: String = "$searchString.*$"
            val pattern: Pattern = Pattern.compile(regexPattern, Pattern.CASE_INSENSITIVE)

            allBooks.forEach { book ->
                if (isBookMatching(book, pattern)) {
                    Log.d("FirestoreRepository", "Coincidencia encontrada")
                    bookArray.add(book)
                } else {
                    Log.d("FirestoreRepository", "No se encontró coincidencia")
                }
            }
            bookArray


            return bookArray
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getAllBooks failed with ", e)
            emptyList()
        }
    }

    private fun isBookMatching(book: Book?, pattern: Pattern): Boolean {
        // Verifica si hay coincidencia en cualquier atributo del libro
        if (book != null) {
            return (book?.title?.let { pattern.matcher(it).find() } == true ||
                    book.author_name?.let { pattern.matcher(it).find() } == true ||
                    book.ISBN?.let { pattern.matcher(it.toString()).find() } == true)
        }
        return false
    }




    override suspend fun getAuthor(authorId: String): Author? {

        return try {
            val documentSnapshot = firebaseFirestore.collection("authors").document(authorId).get().await()

            if (documentSnapshot.exists()) {

                val author = Author()

                var bookArray = getAllBooks(documentSnapshot.get("works") as? List<String> ?: listOf())
                author.name = documentSnapshot.getString("name") ?: "No se ha encontrado un nombre"
                author.biography = documentSnapshot.getString("biography")?: "No se ha encontrado una Biografía"

                author.works = bookArray.toTypedArray()
                author
            } else {
                null
            }
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getAuthor failed with ", e)
            null
        }

    }
    override suspend fun getCollection(collectionId: String) : Collection?{
        return try {
            val documentSnapshot = firebaseFirestore.collection("collections").document(collectionId).get().await()

            if (documentSnapshot.exists()){
                var collection = Collection()

                collection.name = documentSnapshot.getString("name") ?: "Error al encontrar el nombre"
                collection.description = documentSnapshot.getString("description") ?: "Error al encontrar la descripción"

                var bookArray = getAllBooks(documentSnapshot.get("books") as? List<String> ?: listOf())
                collection.books = bookArray.toTypedArray()

                collection
            }else{
                //@TODO mensaje de error aqui
                null
            }

            }
        catch (e: Exception) {
            Log.d("FirestoreRepository", "getCollection failed with ", e)
            null
        }
    }

    override suspend fun getReviews(bookId: String): List<Review?> {
        return try {
            val reviews = mutableListOf<Review?>()
            val querySnapshot = firebaseFirestore.collection("books").document("$bookId").collection("reviews").get().await()

            for (document in querySnapshot.documents) {
                if(document.exists()){
                    val review = Review()
                    review.userId = document.getString("userId") ?: "Error"
                    review.description = document.getString("description") ?: "No se ha encontrado una descripción"
                    review.score = document.getDouble("score") ?: 0.0

                    val timestamp = document.getTimestamp("date")
                    val localDate = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                    review.date = localDate

                    reviews.add(review)
                }
                else{
                    reviews.add(Review())
                }
            }
            reviews
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getCollection failed with ", e)
            emptyList()
        }
    }

    override suspend fun localDateToTimestamp(date: LocalDate?): Timestamp? {
        return date?.let {
            val zonedDateTime = it.atStartOfDay(ZoneId.systemDefault())
            Timestamp(zonedDateTime.toEpochSecond(), 0)
        }
    }

    override suspend fun timestampToLocalDate(timestamp: Timestamp): LocalDate {

        val date = Date(timestamp.seconds * 1000 + timestamp.nanoseconds / 1000000)

        return date.toInstant()
            .atZone(ZoneId.systemDefault())
            .toLocalDate()
    }
    override suspend fun upLoadReview(bookId: String, review: Review) {
        try {
            val firestore = FirebaseFirestore.getInstance()

            // Preparar los datos del objeto Review para Firestore
            val reviewData = hashMapOf(
                "userId" to review.userId,
                "score" to review.score,
                "description" to review.description,
                "date" to localDateToTimestamp(review.date) // Convierte LocalDate a Timestamp
            )

            // Crear un nuevo documento en Firestore
            firestore.collection("books").document(bookId)
                .collection("reviews").document() // Firestore generará un ID de documento
                .set(reviewData)
                .await()

            Log.d("FirestoreRepository", "Review subida con éxito para el libro: $bookId")
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "upLoadReview failed with ", e)
        }
    }

}