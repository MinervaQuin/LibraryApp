package com.example.libraryapp.model.firebaseAuth

import android.content.Context
import android.util.Log
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.AuthorFb
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Collection
import com.example.libraryapp.model.resources.Review
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.lang.NullPointerException
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.concurrent.CancellationException
import java.util.regex.Pattern
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.random.Random


class FirestoreRepositoryImpl @Inject constructor(
    private val firebaseFirestore: FirebaseFirestore,
    private val auth: FirebaseAuth,
    private val storage: FirebaseStorage,
    private val oneTapClient: SignInClient, // Inyectado directamente
    private val context: Context
) : FirestoreRepository {
    override val dataBase: FirebaseFirestore?
        get() = firebaseFirestore

    override val authConection: FirebaseAuth?
        get() = auth

    override val storageDataBase: FirebaseStorage?
        get() = storage

    override val tapClient: SignInClient
        get() = oneTapClient

    override val contextFeo: Context
        get() = context

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
                var book = document.toObject(Book::class.java)
                if (book != null) {
                    book.ref = document.id
                }
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
                    book.isbn?.let { pattern.matcher(it.toString()).find() } == true)
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
                author.cover= documentSnapshot.getString("cover")?: "Not found"
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

    override suspend fun getAllAutors(): List<Author?> {
        val Array: MutableList<Author?> = mutableListOf()
        return try {
            val querySnapshot = FirebaseFirestore.getInstance()
                .collection("authors") // Reemplaza con el nombre de tu colección en Firestore
                .get()
                .await()

            for (document in querySnapshot.documents) {
                val autor = Author()

                var bookArray = getAllBooks(document.get("works") as? List<String> ?: listOf())
                autor.name = document.getString("name") ?: "Error al encontrar el nombre"
                autor.biography = document.getString("biography")?: "No se ha encontrado una Biografía"
                autor.cover= document.getString("cover")?: "Not found"
                autor.works = bookArray.toTypedArray()
                Array.add(autor)

            }
            Log.d("Firestore", Array.size.toString())
            Array
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getAllBooks failed with ", e)
            emptyList()
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
                    val review = Review(
                        document.id!!,
                        document.getString("userId")!!,
                        document.getString("userName")!!,
                        document.getDouble("score")!!,
                        document.getString("description")!!
                    )
////                    review.userId = document.getString("userId") ?: "Error"
//                    review.description = document.getString("description") ?: "No se ha encontrado una descripción"
//                    review.score = document.getDouble("score") ?: 0.0

                    val timestamp = document.getTimestamp("date")
                    val localDate = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                    review.date = localDate

                    reviews.add(review)
                }
                else{
                    reviews.add(null)
                }
            }
            reviews
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "getCollection failed with ", e)
            emptyList()
        }
    }

    override suspend fun getReviewFromUserId(bookId: String, userId: String): Review? {
        return try {
            var review: Review? = null
            // Acceder a la colección "reviews" del libro específico
            val querySnapshot = firebaseFirestore
                .collection("books")
                .document(bookId)
                .collection("reviews")
                .get()
                .await()

            // Recorrer los documentos en el resultado de la consulta
            for (document in querySnapshot.documents) {
                // Obtener el valor del campo "userId" del documento
                val userIdFromDocument = document.getString("userId")
                if (userIdFromDocument.equals(userId)) {
                    Log.d("firebase", "Este usuario tiene una review")
                    if (userIdFromDocument != null) {
                        review = Review(
                            document.id!!,
                            document.getString("userId")!!,
                            document.getString("userName")!!,
                            document.getDouble("score")!!,
                            document.getString("description")!!
                        )
//                        review.reviewId = document.id
//                        review.userId = document.getString("userId")!!
//                        review.description = document.getString("description") !!
//                        review.score = document.getDouble("score") !!

                        val timestamp = document.getTimestamp("date")
                        val localDate = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                        review.date = localDate

                    }
                }
            }

            review
        } catch (e: Exception) {
            // Manejar la excepción, por ejemplo, imprimir un mensaje de error
            e.printStackTrace()
            Log.d("firebase", "Este usuario no tiene review")
            null // Devolver una lista vacía en caso de error
        }
    }


    override suspend fun updateReview(bookId: String, reviewId: String, newData: Map<String, Any>) {
        // Referencia al documento que deseas actualizar
        val usuarioRef = firebaseFirestore.collection("books").document(bookId).collection("reviews").document(reviewId)

        var timestamp = localDateToTimestamp(LocalDate.now())
        // Actualiza el campo específico con el nuevo valor
        usuarioRef
            .update(newData)
            .addOnSuccessListener {
                // La actualización fue exitosa
            }
            .addOnFailureListener { e ->
                // Ocurrió un error al actualizar
                println("Error al actualizar campo: $e")
            }
        usuarioRef
            .update("date", timestamp)
            .addOnSuccessListener {
                // La actualización fue exitosa
            }
            .addOnFailureListener { e ->
                // Ocurrió un error al actualizar
                println("Error al actualizar campo: $e")
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
                "userName" to review.userName,
                "score" to review.score,
                "description" to review.description,
                "date" to localDateToTimestamp(review.date) // Convierte LocalDate a Timestamp
            )
            Log.d("firebase", "Valores de reviewData: $reviewData")
            firestore.collection("books").document(bookId).collection("reviews").add(reviewData as Map<String, Any>)
                .addOnSuccessListener {
                    Log.d("firebase", "Documento actualizado con éxito")
                }
                .addOnFailureListener { e ->
                    Log.e("firebase", "Error al actualizar documento", e)
                }



            Log.d("firebase", "Review subida con éxito para el libro: $bookId")
        } catch (e: Exception) {
            Log.d("firebase", "upLoadReview failed with ", e)
        }
    }

    override suspend fun uploadBookScore(bookId: String, newScore: Int){
        try {
            val firestore = FirebaseFirestore.getInstance()

            val bookRef = firestore.collection("books").document(bookId)

            // Crear un mapa con el campo que deseas actualizar
            val updateData = hashMapOf<String, Any>(
                "score" to newScore
                // Aquí puedes agregar más campos si es necesario
            )

            // Actualizar el documento con los nuevos datos
            bookRef.update(updateData)
                .addOnSuccessListener {
                    Log.d("firebase", "DocumentSnapshot successfully updated!")
                }
                .addOnFailureListener { e ->
                    Log.w("firebase", "Error updating document", e)
                }
        }catch (e: Exception) {
            Log.d("firebase", "upLoadScore failed with ", e)
        }

    }




    override suspend fun uploadBookToFirestore() {
        val newBook = Book(
            author_id = "9",
            title = "El arte de la guerra",
            author_name = "Sun Tzu",
            isbn = 9788420691206,
            sinopsis = "Oculta con su familia y otra familia judía (los Van Daan), en una buhardilla de unos almacenes de Ámsterdam durante la ocupación nazi de Holanda.  Ana Frankcon trece años, cuenta en su diario, al que llamó «Kitty», la vida del grupo. Ayudados por varios empleados de la oficina, permanecieron durante más de dos años en el achterhuis (conocido como «el anexo secreto») hasta que, finalmente, fueron delatados y detenidos. Ana escribió un diario entre el 12 de junio de 1942 y el 1 de agosto de 1944. El 4 de agosto de 1944, unos vecinos (se desconocen los nombres) delatan a los ocho escondidos en \"la casa de atrás\". Además del Diario escribió varios cuentos que han sido publicados paulatinamente desde 1960. Su hermana, Margot Frank también escribió un diario, pero nunca se encontró ningún rastro de éste." ,
            score = 2,
            cover = "https://books.google.com/books/content?id=_-nnoQEACAAJ&printsec=frontcover&img=1&zoom=1",
            price = 10.93,
            ref = ""
        )
        val newAuthor = AuthorFb(
            id = 9,
            name = "Sun Tzu",
            biography = "t",
            works = listOf<String>()


        )
        try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("books")
                .add(newBook)

                .addOnSuccessListener { documentReference ->
                    // En este punto, el libro se ha subido exitosamente a Firestore
                    // Puedes imprimir el ID del documento si es necesario
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    // Maneja los errores aquí
                    println("Error adding document: $e")
                }
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "uploadBook failed with ", e)
        }

        try {
            val firestore = FirebaseFirestore.getInstance()
            firestore.collection("authors")
                .add(newAuthor)

                .addOnSuccessListener { documentReference ->
                    // En este punto, el libro se ha subido exitosamente a Firestore
                    // Puedes imprimir el ID del documento si es necesario
                    println("DocumentSnapshot added with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    // Maneja los errores aquí
                    println("Error adding document: $e")
                }
        } catch (e: Exception) {
            Log.d("FirestoreRepository", "uploadAuthor failed with ", e)
        }
    }

    override suspend fun addNewAttribute(){
        try {
            val firestore = FirebaseFirestore.getInstance()
            val collectionReference = firestore.collection("")
            collectionReference.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        // Aquí puedes actualizar cada documento añadiendo un nuevo campo
                        val updatedData = hashMapOf(
                            "ref" to document.id,

                            // Puedes añadir más campos según tus necesidades
                        )
                        collectionReference.document(document.id).update(updatedData as Map<String, Any>)
                            .addOnSuccessListener {
                                Log.d("Firestore", "Documento actualizado con éxito")
                            }
                            .addOnFailureListener { e ->
                                Log.e("Firestore", "Error al actualizar documento", e)
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("Firestore", "Error al obtener documentos", e)
                }


        }catch (e: Exception){
            Log.e("Firestore", "No hay firebase", e)
        }
    }

    fun convertLocalDateToTimestamp(localDate: LocalDate): Timestamp {
        val zoneId = ZoneId.systemDefault()
        val epochMillis = localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
        return Timestamp(epochMillis / 1000, ((epochMillis % 1000) * 1_000_000).toInt())
    }



    /*override suspend fun uploadImageToFirebase(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("users/$userId/profilePicture.jpg")

        storageRef.putFile(imageUri).addOnSuccessListener {
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                onSuccess(uri.toString())
            }
        }.addOnFailureListener { exception ->
            onFailure(exception)
        }
    }*/


    override suspend fun signOut(){
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch(e: Exception){
            e.printStackTrace()
            if(e is CancellationException) throw e
        }
    }



    //    reviews: List<String> = List(5) { "$it" },
    val opiniones: List<String> = listOf(
    "Este libro es una joya, una trama cautivadora de principio a fin.",
    "Los personajes son tan vívidos que sientes que podrías conocerlos en la vida real.",
    "Increíblemente bien escrito, cada palabra está cuidadosamente elegida.",
    "La trama te mantiene en vilo, imposible de dejar de leer.",
    "Una historia con giros inesperados que te mantienen sorprendido.",
    "El mensaje profundo del libro me hizo reflexionar sobre la vida.",
    "El estilo del autor es único y refrescante, una delicia para los amantes de la buena escritura.",
    "Me encantó cómo la historia aborda temas actuales de manera inteligente.",
    "Los diálogos son tan auténticos que puedes escuchar a los personajes hablar.",
    "Un libro que te deja con una sensación duradera después de leer la última página.",
    "La descripción de los escenarios es tan vívida que sentí que estaba allí.",
    "Este libro es una montaña rusa emocional, te hace reír y llorar en un instante.",
    "La narrativa es envolvente, te sumerge por completo en el universo del autor.",
    "Recomendaría este libro a cualquiera que busque una lectura fascinante y conmovedora.",
    "La trama y los personajes se quedan contigo mucho después de cerrar el libro; una experiencia inolvidable.",
    )

    val users: List<String> = listOf(
        "Isabella",
        "Liam",
        "Sophia",
        "Noah",
        "Mia",
        "Ethan",
        "Olivia",
        "Oliver",
        "Ava",
        "Aiden",
        "Emma",
        "Jackson",
        "Aria",
        "Lucas",
        "Harper",
        "Benjamin",
        "Grace",
        "Mason",
        "Amelia",
        "Caleb",
        "Lily",
        "Henry",
        "Stella",
        "Samuel",
        "Zoe",
        "Leo",
        "Natalie",
        "Alexander",
        "Penelope",
        "James",
        "Eleanor",
        "Milo",
        "Aurora",
        "Sebastian",
        "Luna",
        "Owen",
        "Hazel",
        "Felix",
        "Isla",
        "Wyatt",
        "Eva",
        "Graham",
        "Nova",
        "Dylan",
        "Ivy",
        "Arthur",
        "Ayla",
        "Maxwell",
        "Nora",
        "Zachary",
        "Clara",
        "Julian",
        "Elise",
        "Carter",
        "Violet",
        "Theodore",
        "Aubrey",
        "Gabriel",
        "Adeline",
        "Oscar"
    )

    override suspend fun getReviewsFromABook(bookId: String): List<Review?> = suspendCoroutine { continuation ->
        try {
            val reviewList: MutableList<Review?> = mutableListOf()
            val db = FirebaseFirestore.getInstance()
            val mainCollectionPath = "books"
            val documentId = bookId
            val subcollectionPath = "reviews"

            val subcollectionRef = db.collection(mainCollectionPath)
                .document(documentId)
                .collection(subcollectionPath)

            subcollectionRef.get()
                .addOnSuccessListener { documents ->
                    for (document in documents) {
                        val documentData = document.data

                        try {
                            val review = Review(
                                document.id!!,
                                document.getString("userId")!!,
                                document.getString("userName")!!,
                                document.getDouble("score")!!,
                                document.getString("description")!!
                            )

                            val timestamp = document.getTimestamp("date")
                            val localDate = timestamp?.toDate()?.toInstant()?.atZone(ZoneId.systemDefault())?.toLocalDate()
                            review.date = localDate

                            reviewList.add(review)
                        }catch (e: NullPointerException){
                            Log.d("Reviews","No es una review")
                        }

//                        review.userName = document.getString("userName") ?: "Error"
//                        review.description = document.getString("description") ?: "No se ha encontrado una descripción"
//                        review.score = document.getDouble("score") ?: 0.0


                    }
//                    Log.d("Reviews", "Tamaño reviews : " + reviewList.size.toString())
                    continuation.resume(reviewList)
                }
                .addOnFailureListener { e ->
                    // Manejar errores
                    Log.d("Reviews","Error al obtener documentos de la subcolección: $e")
                    continuation.resume(emptyList())
                }
        } catch (e: Exception) {
            Log.d("Reviews","validate Review failed with ", e)
            continuation.resume(emptyList())
        }
    }

    override suspend fun deleteReviews(){
        try {

            var count = 3;
            val db = FirebaseFirestore.getInstance()
            val mainCollectionPath = "books"

// Obtener todos los documentos de la colección principal
            val mainCollectionRef = db.collection(mainCollectionPath)

            mainCollectionRef.get()
                .addOnSuccessListener { mainDocuments ->
                    for (mainDocument in mainDocuments) {
                        val mainDocumentId = mainDocument.id

                        // Obtener la subcolección para cada documento principal
                        val subcollectionPath = "reviews"
                        val subcollectionRef = mainCollectionRef.document(mainDocumentId).collection(subcollectionPath)

                        subcollectionRef.get()
                            .addOnSuccessListener { subDocuments ->
                                for (subDocument in subDocuments) {
                                        // Aquí puedes actualizar cada documento añadiendo un nuevo campo
                                        val updatedData = hashMapOf(
                                            "description" to opiniones[Random.nextInt(0, 15)],
//                                            "userName" to users[count],
                                            "userId" to "$count",
                                            "score" to Random.nextInt(0, 5).toDouble()

                                            // Puedes añadir más campos según tus necesidades
                                        )
                                        count++
                                        subcollectionRef.document(subDocument.id).update(updatedData as Map<String, Any>)
                                            .addOnSuccessListener {
                                                Log.d("Firestore", "Documento actualizado con éxito")
                                            }
                                            .addOnFailureListener { e ->
                                                Log.e("Firestore", "Error al actualizar documento", e)
                                            }

                                }
                            }
                            .addOnFailureListener { e ->
                                // Manejar errores al obtener documentos de la subcolección
                                Log.d("Reviews","Error al obtener documentos de la subcolección: $e")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    // Manejar errores al obtener documentos de la colección principal
                    Log.d("Reviews","Error al obtener documentos de la colección principal: $e")
                }


        }catch (e: Exception){

        }

    }

    override suspend fun getuser(): UserData? {
        return auth.currentUser?.run {
            UserData(
                userId = uid,
                userName = displayName,
                profilePictureUrl = photoUrl?.toString()
            )

        }

    }

}