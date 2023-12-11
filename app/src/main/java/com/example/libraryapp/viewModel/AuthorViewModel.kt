package com.example.libraryapp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class AuthorViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val libraryAppState: LibraryAppState
) : ViewModel() {
    var autor by mutableStateOf(Author())
        private set

    init {
    }

    fun updateAutor(){
        val id =  libraryAppState.getautorId()
        if(id==null){
            autor=libraryAppState.getAutor() as Author
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
    fun setNewBook(book: Book){
        libraryAppState.setBook(book)
    }
}