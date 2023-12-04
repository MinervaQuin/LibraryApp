package com.example.libraryapp.viewModel

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.MainActivity
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
    private val _isFallo = MutableLiveData<Boolean>()
    val isFallo get() = _isFallo
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

    suspend fun handleScanResult(result: String) {
        viewModelScope.launch {
            val books = getBooksStringMatch(result)
            if (books.isNotEmpty()){
                val book = books[0] as Book
                ShoppingCart.setBookSelected(book)
                ShoppingCart.getNavController().navigate("BookDetailsView")
                _isFallo.value = false
            }
            else{
                _isFallo.value = true
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

