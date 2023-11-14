package com.example.libraryapp.viewModel

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.BookDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.w3c.dom.Comment
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class BookDetailsViewModel: ViewModel() {
    private val _bookUiState = MutableStateFlow(BookDetailsUiState())
    val bookUiState: StateFlow<BookDetailsUiState> = _bookUiState.asStateFlow()



    fun sendReview(comment: String){
        _bookUiState.update { currentState ->
            currentState.copy(
                comment = comment,
                showDialog = false
            )
        }
        Log.d("fav", _bookUiState.value.comment)
        Log.d("fav", comment)

    }

    fun updateRating(reviewScore: Int){
        _bookUiState.update { currentState ->
            currentState.copy(
                reviewScore = reviewScore,
            )
        }
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

    fun getFormattedTimeAgo(commentDateTime: LocalDateTime): String {
        val currentDateTime = LocalDateTime.now()
        val difference = Duration.between(commentDateTime, currentDateTime)

        return when {
            difference.seconds < 60 -> "hace un momento"
            difference.toMinutes() < 60 -> "hace ${difference.toMinutes()} minutos"
            difference.toHours() < 24 -> "hace ${difference.toHours()} horas"
            commentDateTime.toLocalDate() == currentDateTime.toLocalDate() -> "hoy"
            else -> commentDateTime.format(
                DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        }
    }




}