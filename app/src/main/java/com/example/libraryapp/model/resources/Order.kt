package com.example.libraryapp.model.resources

data class Order(
    val orderId: String = "",
    val booksOrdered: Map<String, Int> = emptyMap(), // Cambiado a Map
    val orderDate: String = "",
    val total: Double = 0.0,
    val shipmentAdress: String = "",
    val state: String = ""
)