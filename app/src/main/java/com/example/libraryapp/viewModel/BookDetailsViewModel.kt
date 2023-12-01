package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.BookDetailsUiState
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries.localDate
import javax.inject.Inject


@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository

): ViewModel(){
    private val _bookUiState = MutableStateFlow(BookDetailsUiState())
    val bookUiState: StateFlow<BookDetailsUiState> = _bookUiState.asStateFlow()



    fun sendReview(comment: String){
        _bookUiState.update { currentState ->
            currentState.copy(
                comment = comment,
                showDialog = false
            )
        }

    }

    fun updateRating(reviewScore: Int){
        _bookUiState.update { currentState ->
            currentState.copy(
                reviewScore = reviewScore,
            )
        }
    }

    suspend fun getReviews(bookId: String): List<Review?>{
        // Declaración de una lista mutable llamada "reviews"
        // Declaración de una lista llamada "reviews"
        val reviews: List<Review?> = firestoreRepository.getReviewsFromABook(bookId)

        Log.d("Reviews", bookId + " : " + reviews.size.toString() )
        return reviews
    }



    fun updateComment(comment: String){
        _bookUiState.update { currentState ->
            currentState.copy(
                comment = comment,
            )
        }
    }

    fun closeDialog(){
        _bookUiState.update { currentState ->

            currentState.copy(
                showDialog = false
            )
        }
    }

    fun showDialog(value: Boolean){
        _bookUiState.update { currentState ->
            currentState.copy(
                showDialog = value
            )
        }
    }


    fun getFormattedTimeAgo(commentDateTime: LocalDate?): String {
        val commentCast = LocalDateTime.of(commentDateTime, LocalTime.MIDNIGHT)

        val currentDateTime = LocalDateTime.now()
        val difference = Duration.between(commentCast, currentDateTime)

        return when {
            difference.seconds < 60 -> "hace un momento"
            difference.toMinutes() < 60 -> "hace ${difference.toMinutes()} minutos"
            difference.toHours() < 24 -> "hace ${difference.toHours()} horas"
            commentCast.toLocalDate() == currentDateTime.toLocalDate() -> "hoy"
            else -> commentCast.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }

}