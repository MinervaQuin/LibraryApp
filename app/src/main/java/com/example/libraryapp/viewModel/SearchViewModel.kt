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
class SearchViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(){


        suspend fun getBooksStringMatch(): List<Book?> {
            try {
                // Esperar a que getAllBooks2 complete su ejecución
                return firestoreRepository.getAllBooks2()
            } catch (e: Exception) {
                Log.e("Firestore", "Error en getBooksStringMatch", e)
                return emptyList()
            }
        }

}

