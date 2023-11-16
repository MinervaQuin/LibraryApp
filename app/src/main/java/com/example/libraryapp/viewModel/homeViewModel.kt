package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.Book
import com.example.libraryapp.model.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
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
                Log.d("Coso supremo", "Precio = ${book.price}")
            } else {
                Log.d("HomeViewModel", "Book not found or error occurred")
            }
        }
    }
}

