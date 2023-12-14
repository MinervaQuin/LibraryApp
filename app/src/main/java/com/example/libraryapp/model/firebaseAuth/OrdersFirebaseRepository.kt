package com.example.libraryapp.model.firebaseAuth

import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface OrdersFirebaseRepository {
    val dataBase: FirebaseFirestore?
    val authConection: FirebaseAuth?

    suspend fun prepareCartData(cartItems: Map<Book, Int>): Map<String, Any>
    suspend fun uploadCartData(cartItems: Map<Book, Int>, adress: String)
    suspend fun downloadOrders(): List<Order>

}