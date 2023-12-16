package com.example.libraryapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.resources.Author
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class autoresViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val libraryAppState: LibraryAppState
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

    fun setnewAutor(autor: Author?){
        libraryAppState.setautorId(null)
        libraryAppState.setautor(autor)
    }
}