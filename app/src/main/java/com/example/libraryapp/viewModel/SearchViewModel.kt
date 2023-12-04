package com.example.libraryapp.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.google.zxing.integration.android.IntentIntegrator
//import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository

): ViewModel(){
    private val _scanResult = MutableLiveData<String>()
    val scanResult: LiveData<String> get() = _scanResult
    private val _book= MutableLiveData<Book>()
    val book: LiveData<Book> get() = _book

    private var allBooks: List<Book?> = emptyList()
    private var searchedBooks : List<Book?> = emptyList()

    fun initiateScan(context: Context) {
        viewModelScope.launch {
            val integrator = IntentIntegrator(context as ComponentActivity)
            integrator.setPrompt("Escanea el codigo de barras")
            integrator.setBeepEnabled(false)
            integrator.initiateScan()
        }
    }

    fun handleScanResult(result: String?) {
        result?.let {
            _scanResult.value = it
            viewModelScope.launch {
                val books = getBooksStringMatch(it)
                if (books.isNotEmpty()){
                    _book.value = books[0] as Book
                }
            }
        }
    }

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

