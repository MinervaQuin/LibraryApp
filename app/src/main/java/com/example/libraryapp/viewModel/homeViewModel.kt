package com.example.libraryapp.viewModel

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.model.resources.Collection
import com.example.libraryapp.model.resources.CollectionSamples
import com.example.libraryapp.model.resources.LongCollectionSamples
import com.example.libraryapp.model.resources.Review
import com.example.libraryapp.model.resources.carouselImage
import com.example.libraryapp.theme.verdeFuerte
import com.example.libraryapp.ui.theme.GreenAppOpacity
import com.example.libraryapp.ui.theme.rojoSangre
import com.example.libraryapp.ui.theme.rositaGracioso
import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,

): ViewModel(){

    private val _isFallo = MutableLiveData<Boolean>()
    private var allBooks: List<Book?> = emptyList()

    var collectionArray = MutableStateFlow<List<CollectionSamples>>(
        listOf(
            CollectionSamples("Novedades", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","Novedades"),
            CollectionSamples("Los más leídos", rositaGracioso, "https://1.bp.blogspot.com/-GXLyGMhWMJ8/XzqTBbp_udI/AAAAAAAAZaM/LyfXIxQZZgIjy62NFDuKEstQz_vs_Z9RwCNcBGAsYHQ/s1014/EbrMX4-XsAMyGlm.jpg","Populares"),
            CollectionSamples("Recomendados", rositaGracioso, "https://m.media-amazon.com/images/I/51a0PiL88RL._SY445_SX342_.jpg","Ficción"),
            CollectionSamples("Promociones", GreenAppOpacity, "https://www.nationalgeographic.com.es/medio/2022/04/23/libros_b2d310d5_1280x852.jpg","4"),
            CollectionSamples("Blog", GreenAppOpacity, "https://previews.123rf.com/images/olegdudko/olegdudko1712/olegdudko171201274/90967469-libros-y-computadora-port%C3%A1til.jpg","5"),
            CollectionSamples("Premiados", GreenAppOpacity, "https://phantom-expansion.unidadeditorial.es/ca3e81ca6c16010e5d1fc2367d9a004a/crop/0x22/552x756/resize/640/assets/multimedia/imagenes/2022/02/28/16460499793522.jpg","Imprescindibles"),
            CollectionSamples("eBooks", rositaGracioso, "https://cdn.computerhoy.com/sites/navi.axelspringer.es/public/media/image/2018/11/ereader.jpg?tf=3840x","7"),
            CollectionSamples("Autores", GreenAppOpacity, "https://cdn.zendalibros.com/wp-content/uploads/arturo-perez-reverte-1.jpg","8")
        )
    )
    val largeCollectionSamplesArray = MutableStateFlow<List<LongCollectionSamples>>(
        listOf(
            LongCollectionSamples("Cómic y Manga", rojoSangre, R.drawable.hmt_large, "Cómic y Manga", false),
            LongCollectionSamples("Infantil", verdeFuerte, R.drawable.flecha2, "Infantil", false),
            LongCollectionSamples("Todas las Categorías", rojoSangre, R.drawable.todas_categorias, "Todas Las Categorias", true),
            LongCollectionSamples("Prueba 4", rojoSangre, R.drawable.hmt_large, "Ruta2", true)

        )
    )

    val carouselImageArray = MutableStateFlow<List<carouselImage>>(
        listOf(
            carouselImage(R.drawable.carrusel1, "cosa"),
            carouselImage(R.drawable.carrusel2, "Cosa2"),
            carouselImage(R.drawable.carrusel3, "Cosa3")
        )
    )

    fun getBookAndLog(bookId: String) {
        viewModelScope.launch {
            val book: Book? = firestoreRepository.getBook(bookId)
            if (book != null) {
                Log.d("HomeViewModel", "Book: $book")
            } else {
                Log.d("HomeViewModel", "Book not found or error occurred")
            }
        }
    }
    fun getAuthorAndLog(authorId: String){
        viewModelScope.launch {
            val author: Author? = firestoreRepository.getAuthor(authorId)
            Log.d("HomeViewModel", "${author?.biography}")
            author?.works?.forEach { libro ->
                Log.d("Libro", "${libro.toString()}")
            }
        }

    }

    fun getCollectionAndLog(collectionId : String){
        viewModelScope.launch {
            val collection: Collection? = firestoreRepository.getCollection(collectionId)
            Log.d("HomeViewModel", "${collection.toString()}")
        }
    }

    fun getReviewsAndLog(bookId: String) {
        viewModelScope.launch {
            val reviews: List<Review?> = firestoreRepository.getReviews("B9svfDJglRgEPyN6wSAh")
            reviews.forEach { review ->
                if (review != null) {
                    Log.d("ReviewLog", review.toString())
                } else {
                    Log.d("ReviewLog", "Una reseña es null")
                }
            }
        }
    }

    fun getUserDataAndLog() {
        viewModelScope.launch {
            val userData: UserData? = firestoreRepository.getuser()
            if (userData != null) {
                Log.d("UserDataLog", userData.toString())
            } else {
                Log.d("UserDataLog", "No hay datos de usuario disponibles.")
            }
        }
    }




    fun initiateScan(context: Context) {
        viewModelScope.launch {
            val integrator = IntentIntegrator(context as ComponentActivity)
            integrator.setPrompt("Escanea el codigo de barras")
            integrator.setBeepEnabled(false)
            integrator.initiateScan()
        }
    }

    suspend fun handleScanResult(result: String) {
        viewModelScope.launch {
            val books = getBooksStringMatch(result)
            if (books.isNotEmpty()){
                val book = books[0] as Book
                ShoppingCart.setBookSelected(book)
                ShoppingCart.getNavController().navigate("BookDetailsView")
                _isFallo.value = false
            }
            else{
                _isFallo.value = true
            }
        }
    }

    suspend fun getBooksStringMatch(searchString: String): List<Book?> {

        try {
            // Esperar a que getAllBooks2 complete su ejecución

            if (allBooks.isEmpty()){
                allBooks = firestoreRepository.getAllBooks2()

            }
//                firestoreRepository.addASecondCollection()
            return firestoreRepository.searchAllBooks(allBooks, searchString)

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            return emptyList()
        }
    }





    //    fun uploadReviewTest(){
//        viewModelScope.launch {
//            val reviewTest = Review(userId="prueba", score = 5.4, description = "Esto es una mera prueba", date = LocalDate.now())
//            firestoreRepository.upLoadReview("B9svfDJglRgEPyN6wSAh", reviewTest)
//        }
//
//    }
    //porbar si añade libros al carrito
    suspend fun getBook(isbn: String): Book? {
        return firestoreRepository.getBook(isbn)
    }
}

