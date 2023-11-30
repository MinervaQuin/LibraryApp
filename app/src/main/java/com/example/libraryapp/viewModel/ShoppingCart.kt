package com.example.libraryapp.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import com.example.libraryapp.model.resources.Book

object ShoppingCart {
    private lateinit var viewModel: CartViewModel
    private var _categorySelected = MutableStateFlow("")
    private lateinit var bookSelected: Book

    fun init() {
        viewModel = CartViewModel()
    }

    fun getViewModelInstance(): CartViewModel {
        return viewModel
    }
    fun setSelectedCategory(new: String){
        _categorySelected.value = new
    }
    fun getSelectedCategory(): String{
        return _categorySelected.value
    }

    fun setBookSelected(book: Book){
        this.bookSelected= book
    }
    fun getBookSelected(): Book{
        return this.bookSelected
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
