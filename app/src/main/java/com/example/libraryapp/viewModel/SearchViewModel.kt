package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository

): ViewModel(){

    private var allBooks: List<Book?> = emptyList()
    private var searchedBooks : List<Book?> = emptyList()
    suspend fun getBooksStringMatch(searchString: String): List<Book?> {

         try {
                // Esperar a que getAllBooks2 complete su ejecución

             if (allBooks.isEmpty()){
                 allBooks = firestoreRepository.getAllBooks2()

             }
//                firestoreRepository.addASecondCollection()
                 return firestoreRepository.searchAllBooks(allBooks, searchString)

         } catch (e: Exception) {
             Log.e("Firestore", "Error en getBooksStringMatch", e)
             return emptyList()
         }
    }

    suspend fun getAllBooks(): List<Book?> {
        try {
            // Esperar a que getAllBooks2 complete su ejecución

            if (allBooks.isEmpty()){
                allBooks = firestoreRepository.getAllBooks2()
            }
            searchedBooks = allBooks
            return searchedBooks

        } catch (e: Exception) {
            Log.e("Firestore", "Error en getBooksStringMatch", e)
            return emptyList()
        }
    }

    fun getBookList(): List<Book?> {
        return searchedBooks
    }





}

