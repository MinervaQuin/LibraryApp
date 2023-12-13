package com.example.libraryapp.viewModel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.resources.Book
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
) : ViewModel() {

    private val _cartItems = mutableMapOf<Book, Int>()
    private val _cartItemsStateFlow = MutableStateFlow(_cartItems.toMap())
    val cartItems: StateFlow<Map<Book, Int>> = _cartItemsStateFlow.asStateFlow()
    val cartState = mutableStateOf(CartState())


    companion object {
        // Cambiar el acceso a la instancia del CartViewModel mediante el Singleton
        val instance: CartViewModel
            get() = ShoppingCart.getViewModelInstance()
    }

    fun addBookToCart(book: Book) {
        val existingBook = _cartItems.keys.find { it.title == book.title }

        if (existingBook != null) {
            // El libro ya está en el carrito, aumenta la cantidad en 1
            _cartItems[existingBook] = (_cartItems[existingBook] ?: 0) + 1
        } else {
            // El libro no está en el carrito, agrégalo con una cantidad de 1
            _cartItems[book] = 1
        }

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
