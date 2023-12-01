package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Collection
import com.example.libraryapp.model.resources.CollectionSamples
import com.example.libraryapp.model.resources.LongCollectionSamples
import com.example.libraryapp.model.resources.Review
import com.example.libraryapp.theme.verdeFuerte
import com.example.libraryapp.ui.theme.GreenAppOpacity
import com.example.libraryapp.ui.theme.rojoSangre
import com.example.libraryapp.ui.theme.rositaGracioso
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(){


    var collectionArray = MutableStateFlow<List<CollectionSamples>>(
        listOf(
            CollectionSamples("Novedades", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","Novedades"),
            CollectionSamples("Los más leídos", rositaGracioso, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","Populares"),
            CollectionSamples("Recomendados", rositaGracioso, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","Ficción"),
            CollectionSamples("Promociones", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","4"),
            CollectionSamples("Blog", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","5"),
            CollectionSamples("Premiados", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","Imprescindibles"),
            CollectionSamples("eBooks", rositaGracioso, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","7"),
            CollectionSamples("Autores", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","8")
        )
    )
    val largeCollectionSamplesArray = MutableStateFlow<List<LongCollectionSamples>>(
        listOf(
            LongCollectionSamples("Cómic y Manga", rojoSangre, R.drawable.hmt_large, "Cómic y Manga", false),
            LongCollectionSamples("Infantil", verdeFuerte, R.drawable.hmt_large, "Infantil", false),
            LongCollectionSamples("Todas las Categorías", rojoSangre, R.drawable.hmt_large, "Todas Las Categorias", true),
            LongCollectionSamples("Prueba 4", rojoSangre, R.drawable.hmt_large, "Ruta2", true)

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

