import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow


data class Book(val title: String, val author: String, val price: Double, val imageUrl: String)
data class CartItem(val book: Book, var quantity: MutableState<Int> = mutableStateOf(1))
class CartViewModel : ViewModel() {
    private val _cartItems = MutableStateFlow<List<CartItem>>(emptyList())
    val cartItems: StateFlow<List<CartItem>> = _cartItems
    val cartState = mutableStateOf(CartState())
    private val _cartItemsStateFlow = MutableStateFlow(Unit)

    private val _booksInStock = listOf(
        Book("El señor de los anillos", "J.R.R. Tolkien", 19.99, "https://m.media-amazon.com/images/I/91ddMPYKaYL._SY342_.jpg"),
        Book("Cien años de soledad", "Gabriel García Márquez", 14.99, "https://m.media-amazon.com/images/I/91ddMPYKaYL._SY342_.jpg"),
        Book("1984", "George Orwell", 12.99, "https://m.media-amazon.com/images/I/91ddMPYKaYL._SY342_.jpg"),
        Book("Don Quijote de la Mancha", "Miguel de Cervantes", 16.99, "https://m.media-amazon.com/images/I/91ddMPYKaYL._SY342_.jpg")
        // Puedes agregar más libros según sea necesario
    )

    init {
        // Inicializar el carrito con algunos libros
        _booksInStock.forEach { book ->
            addBookToCart(book)
        }
    }

    fun addBookToCart(book: Book) {
        val existingItem = cartItems.value.find { it.book == book }

        if (existingItem != null) {
            existingItem.quantity.value++
        } else {
            _cartItems.value = cartItems.value + CartItem(book, mutableStateOf(1))
        }
        recalculateCart()
    }

    fun removeBookFromCart(cartItem: CartItem) {
        val updatedItems = cartItems.value.toMutableList()
        updatedItems.remove(cartItem)
        _cartItems.value = updatedItems
        recalculateCart() // Asegúrate de recalcular el carrito después de eliminar un artículo
    }


    fun updateCartItemQuantity(cartItem: CartItem, newQuantity: Int) {
        val updatedItems = _cartItems.value.toMutableList()
        val index = updatedItems.indexOf(cartItem)

        if (index != -1) {
            updatedItems[index] = cartItem.copy(quantity = mutableStateOf(newQuantity))
            _cartItems.value = updatedItems
            recalculateCart()
        }
    }

    fun recalculateCart() {
        val subtotal = _cartItems.value.sumOf { it.book.price * it.quantity.value }
        val deliveryCost = if (cartState.value.deliveryOption == DeliveryOption.PICK_UP) 0.0 else 2.99
        val total = subtotal + deliveryCost

        cartState.value.cartTotal.value = total
        cartState.value.cartSubtotal.value = subtotal
        cartState.value.deliveryCost.value = deliveryCost

        _cartItemsStateFlow.value = Unit
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

