package com.example.libraryapp.viewModel

import android.content.Context
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import com.example.libraryapp.model.firebaseAuth.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.resources.Book
import com.google.zxing.integration.android.IntentIntegrator
//import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val libraryAppState: LibraryAppState,

    ): ViewModel(){
    private val _isFallo = MutableLiveData<Boolean>()
    val isFallo get() = _isFallo
    private var allBooks: List<Book?> = emptyList()
    private var searchedBooks : List<Book?> = mutableListOf()

    private val _booksLoaded = MutableLiveData<Boolean>()
    val booksLoaded: LiveData<Boolean> get() = _booksLoaded

    init {
        setSearchedString(libraryAppState.getSearchedString())
        viewModelScope.launch {

            getBooksStringMatch(libraryAppState.getSearchedString())
            _booksLoaded.postValue(true)
        }
    }

    fun getSearchedBooks(): List<Book?> {
        return searchedBooks
    }

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
                libraryAppState.setBook(book)
                libraryAppState.getNavController().navigate("BookDetailsView")
                _isFallo.value = false
            }
            else{
                _isFallo.value = true
            }
        }
    }

    suspend fun getBooksStringMatch(searchString: String): List<Book?> {

         try {
             if (allBooks.isEmpty()){
                 allBooks = firestoreRepository.getAllBooks2()
             }
             searchedBooks=firestoreRepository.searchAllBooks(allBooks, searchString)

         } catch (e: Exception) {
             Log.e("Firestore", "Error en getBooksStringMatch", e)
             searchedBooks= emptyList()
         }
        return searchedBooks
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

    fun setSearchedString( searchString: String){
        libraryAppState.setSearchedString(searchString)
    }

    fun getSearchedString(): String {
        return libraryAppState.getSearchedString()
    }

    fun setNewNavController(new: NavHostController){
        libraryAppState.setNavController(new)
    }

}

