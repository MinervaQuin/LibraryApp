package com.example.libraryapp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthorViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {
    var autor by mutableStateOf(Author())
        private set

    init {
    }

    fun updateAutor(id: String?){
        if(id==null){
            autor=ShoppingCart.getAutor() as Author
        }
        else{
            viewModelScope.launch {
                val author: Author? = firestoreRepository.getAuthor(id)
                if (author != null) {
                    autor=author
                }
            }   
        }
    }
}