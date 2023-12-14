package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.FirestoreRepository
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepository
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepositoryImpl
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Order
import com.example.libraryapp.ui.bookspreview
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class comprasViewModel @Inject constructor(
    private val ordersFirebase: OrdersFirebaseRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var orders by mutableStateOf(listOf<Order>())
        private set
    var books by mutableStateOf(listOf<Book?>())
        private set

    var MapBook : MutableMap<Order, List<Book?>> = mutableMapOf()

    init {
        viewModelScope.launch {
            orders = ordersFirebase.downloadOrders()
            for (i in 0 until orders.size){
                val listaDeCadenas: List<String> = orders[i].booksOrdered.keys.toList()
                books =firestoreRepository.getAllBooks(listaDeCadenas)
                MapBook.put(orders[i],books)
            }
            /*val listaDeCadenas: List<String> = order.booksOrdered.keys.toList()

            viewModelScope.launch {
                for (i in 0 until books.size){
                    books =firestoreRepository.getAllBooks(listaDeCadenas)
                    MapBook.put(i,books)
                }
            }*/
        }
    }
    /*fun getBooksOrder(order: Order) {
        val listaDeCadenas: List<String> = order.booksOrdered.keys.toList()

        viewModelScope.launch {
            for (i in 0 until books.size){
                books =firestoreRepository.getAllBooks(listaDeCadenas)
                MapBook.put(i,books)
            }
        }
    }

    fun getbooks(): List<Book?>{
        return books
    }*/
}