package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Collection
import com.example.libraryapp.model.resources.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class homeViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(){
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

