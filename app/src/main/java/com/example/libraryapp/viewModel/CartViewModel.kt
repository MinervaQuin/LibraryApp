package com.example.libraryapp.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _cartItems = mutableMapOf<Book, Int>()
    private val _cartItemsStateFlow = MutableStateFlow(_cartItems.toMap())
    val cartItems: StateFlow<Map<Book, Int>> = _cartItemsStateFlow.asStateFlow()
    val cartState = mutableStateOf(CartState())

    /* para probar el funcionamiento descomentar esto
    init {
        // Inicializar el carrito con algunos libros desde la base de datos
        viewModelScope.launch {
            addBookToCartFromDatabase("B9svfDJglRgEPyN6wSAh")
            // Puedes agregar más libros según sea necesario
        }
    }
    */

    suspend fun addBookToCartFromDatabase(isbn: String) {
        val book: Book? = firestoreRepository.getBook(isbn)

        if (book != null) {
            val existingQuantity = _cartItems[book] ?: 0
            _cartItems[book] = existingQuantity + 1

            recalculateCart()
            updateCartItems()
        }
    }

    fun addBookToCart(book: Book) {
        val existingQuantity = _cartItems[book] ?: 0
        _cartItems[book] = existingQuantity + 1

        recalculateCart()
    }

    fun removeBookFromCart(book: Book) {
        _cartItems.remove(book)
        recalculateCart()
        updateCartItems()
    }

    fun updateCartItemQuantity(book: Book, newQuantity: Int) {
        _cartItems[book] = newQuantity
        recalculateCart()
        updateCartItems()
    }

    private fun updateCartItems() {
        _cartItemsStateFlow.value = _cartItems.toMap()
    }

    fun recalculateCart() {
        val subtotal = _cartItems.entries.sumOf { (book, quantity) -> book.price * quantity }
        val deliveryCost = if (cartState.value.deliveryOption == DeliveryOption.PICK_UP) 0.0 else 2.99
        val total = subtotal + deliveryCost

        cartState.value.cartTotal.value = total
        cartState.value.cartSubtotal.value = subtotal
        cartState.value.deliveryCost.value = deliveryCost

        // Actualizar el StateFlow con el nuevo mapa
        _cartItemsStateFlow.value = _cartItems.toMap()
    }


    fun updateDeliveryOption(deliveryOption: DeliveryOption) {
        cartState.value = cartState.value.copy(deliveryOption = deliveryOption)
        recalculateCart()
    }

    data class CartState(
        var deliveryOption: DeliveryOption = DeliveryOption.PICK_UP,
        var cartSubtotal: MutableState<Double> = mutableStateOf(0.0),
        var deliveryCost: MutableState<Double> = mutableStateOf(0.0),
        var cartTotal: MutableState<Double> = mutableStateOf(0.0)
    )

    enum class DeliveryOption {
        PICK_UP,
        HOME_DELIVERY
    }
}
