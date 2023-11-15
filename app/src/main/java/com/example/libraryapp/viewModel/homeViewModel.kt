package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            try {
                val book = firestoreRepository.getBook(bookId)
                // Imprimir los detalles del libro en el Log
                Log.d("BookViewModel", "Libro encontrado: ${book.title}, Autor: ${book.author_name}")
            } catch (e: Exception) {
                Log.e("BookViewModel", "Error al obtener el libro", e)
            }
        }
    }
}