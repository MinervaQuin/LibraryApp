package com.example.libraryapp.viewModel

import com.example.libraryapp.model.FirestoreRepository

object ShoppingCart {
    private lateinit var firestoreRepository: FirestoreRepository
    private lateinit var viewModel: CartViewModel

    fun init(firestoreRepository: FirestoreRepository) {
        this.firestoreRepository = firestoreRepository
        viewModel = CartViewModel(firestoreRepository)
    }

    fun getViewModelInstance(): CartViewModel {
        return viewModel
    }
}

/* acceder al carrito desde otra vista
val shoppingCart: CartViewModel = ShoppingCart.getViewModel()

#añadir libros desde pero con la base de datos
viewModelScope.launch {
    shoppingCart.addBookToCartFromDatabase("B9svfDJglRgEPyN6wSAh")
}

#o si ya tienes el objeto libro añadirlo directamente
val bookToAdd = shoppingCart.addBookToCart(bookToAdd)

#por si una vista necesita visualizar el carrito actualizado
cartViewModel.cartItems.collect { cartItems ->
}

// Inicializar el objeto singleton con la instancia de FirestoreRepository
ShoppingCart.init(FirestoreRepository)
*/
