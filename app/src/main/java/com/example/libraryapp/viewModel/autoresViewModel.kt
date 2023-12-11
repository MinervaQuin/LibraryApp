package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class autoresViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    var autores by mutableStateOf(arrayOf<Author?>())
        private set
    private var _loading = MutableStateFlow(false)
    var loading = _loading.asStateFlow()
    init{
        viewModelScope.launch {
            _loading.value = true
            autores = firestoreRepository.getAllAutors().toTypedArray()
            _loading.value = false
        }
    }
}