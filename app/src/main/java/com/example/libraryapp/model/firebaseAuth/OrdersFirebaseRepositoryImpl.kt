package com.example.libraryapp.model.firebaseAuth

import android.util.Log
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Order
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OrdersFirebaseRepositoryImpl (
    private val db: FirebaseFirestore,
    private val auth: FirebaseAuth
): OrdersFirebaseRepository{
    override val dataBase: FirebaseFirestore?
        get() = db

    override val authConection: FirebaseAuth?
        get() = auth


    override suspend fun prepareCartData(cartItems: Map<Book, Int>): Map<String, Int> {
        Log.d("prepareCartData", "Funciona?")
        return cartItems.entries.associate { (book, quantity) ->
            book.ref to quantity
        }

    }

    override suspend fun uploadCartData(cartItems: Map<Book, Int>) {
        val preparedCartData = prepareCartData(cartItems)
        val orderId = System.currentTimeMillis().toString() // Ejemplo usando timestamp
        val orderRef = db.collection("users").document(auth.currentUser!!.uid)
            .collection("orders").document(orderId)

        // Crear objeto Order
        val order = Order(
            orderId = orderId,
            booksOrdered = preparedCartData,
            orderDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
            total = cartItems.entries.sumOf { (book, quantity) -> book.price * quantity },
            shipmentAdress = "Tu dirección de envío aquí", // Ajustar según sea necesario
            state = "En proceso"
        )

        // Subir el objeto Order a Firestore
        orderRef.set(order)
            .addOnSuccessListener { Log.d("UploadCart", "Order $orderId successfully uploaded") }
            .addOnFailureListener { e -> Log.w("UploadCart", "Error uploading order $orderId", e) }
    }
    override suspend fun downloadOrders(): List<Order> {
        return withContext(Dispatchers.IO) {
            val ordersList = mutableListOf<Order>()
            val userId = auth.currentUser?.uid ?: return@withContext emptyList()

            try {
                val snapshot = db.collection("users").document(userId)
                    .collection("orders").get().await()

                for (document in snapshot.documents) {
                    document.toObject(Order::class.java)?.let { order ->
                        ordersList.add(order)
                    }
                }
            } catch (e: Exception) {
                Log.e("OrdersFirebaseRepository", "Error downloading orders", e)
            }
            ordersList
        }
    }
}