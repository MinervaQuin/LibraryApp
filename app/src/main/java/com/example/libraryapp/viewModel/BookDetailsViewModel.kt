package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.BookDetailsUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

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
        Log.d("fav", comment)
    }

    fun updateRating(reviewScore: Int){
        _bookUiState.update { currentState ->
            currentState.copy(
                reviewScore = reviewScore,
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
}