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
import com.example.libraryapp.model.resources.Review
import com.example.libraryapp.theme.verdeFuerte
import com.example.libraryapp.ui.theme.GreenAppOpacity
import com.example.libraryapp.ui.theme.rojoSangre
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(){
    data class CollectionSamples(
        val title: String,
        val color: Color,
        val imageUrl: String,
        val route: String
    )
    data class LongCollectionSamples(
        val title: String,
        val color: Color,
        val imageRes: Int, // Cambiado de imageUrl a imageRes
        val route: String,
        val isComplete: Boolean
    )

    var collectionArray = MutableStateFlow<List<CollectionSamples>>(
        listOf(
            CollectionSamples("Prueba 1", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","1"),
            CollectionSamples("Prueba 2", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","2"),
            CollectionSamples("Prueba 3", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","3"),
            CollectionSamples("Prueba 4", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","4"),
            CollectionSamples("Prueba 5", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","5"),
            CollectionSamples("Prueba 6", GreenAppOpacity, "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg","6"),
        )
    )
    val largeCollectionSamplesArray = MutableStateFlow<List<LongCollectionSamples>>(
        listOf(
            LongCollectionSamples("Prueba 1", rojoSangre, R.drawable.hmt_large, "Ruta1", false),
            LongCollectionSamples("Prueba 2", verdeFuerte, R.drawable.hmt_large, "Ruta2", false),
            LongCollectionSamples("Prueba 3", rojoSangre, R.drawable.hmt_large, "Ruta2", true),
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

    fun uploadReviewTest(){
        viewModelScope.launch {
            val reviewTest = Review(userId="prueba", score = 5.4, description = "Esto es una mera prueba", date = LocalDate.now())
            firestoreRepository.upLoadReview("B9svfDJglRgEPyN6wSAh", reviewTest)
        }

    }
}

