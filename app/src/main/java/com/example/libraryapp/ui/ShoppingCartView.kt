package com.example.libraryapp.ui


import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = green),
        title = {
            Text(
                text = "Palabras en Papel",
                fontSize = 20.sp,
                color = gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentSize(Alignment.Center)
            )
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    // Maneja el evento de hacer clic en el icono de navegación (por ejemplo, el botón de menú).
                }
            ) {
                Icon(imageVector = Icons.Default.Menu, contentDescription = "Menú",
                    tint = gray
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    // Maneja el evento de hacer clic en el icono de carrito de la compra.
                }
            ) {
                Icon(imageVector = Icons.Default.ShoppingCart, contentDescription = "Carrito",
                    tint = gray
                )
            }
        }
    )
}

@Composable
fun BottomBar() {
    BottomAppBar(
        containerColor = green,
        modifier = Modifier
            .fillMaxWidth()
            .height(65.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    // Acción cuando se presiona el icono de usuario
                }
            ) {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Usuario",
                    tint = gray,
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = {
                    // Acción cuando se presiona el botón de inicio (casa)
                }
            ) {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Inicio",
                    tint = gray,
                    modifier = Modifier.size(32.dp)
                )
            }
            IconButton(
                onClick = {
                    // Acción cuando se presiona el icono de localización
                }
            ) {
                Icon(imageVector = Icons.Default.LocationOn, contentDescription = "Localización",
                    tint = gray,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}



data class Book(val title: String, val author: String, val price: Double, val imageUrl: String, var quantity: Int = 1)


@Composable
fun BookItem(book: Book, onRemoveClick: () -> Unit, onAddClick: () -> Unit, onSubtractClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Columna 1: Imagen
        Box(
            modifier = Modifier
                .width(75.dp)
                .height(100.dp)
        ) {
            AsyncImage(
                model = book.imageUrl,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna 2: Información del libro
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(text = book.title, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text(text = "${book.author}")
            Text(text = "${book.price}€")
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna 3: Información del libro
        Column {
            Row {
                // Botón de agregar
                IconButton(
                    onClick = { onAddClick() },
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = green),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
                }
                Spacer(modifier = Modifier.width(8.dp))

                Text(text = book.quantity.toString(), fontSize = 18.sp)

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de quitar
                IconButton(
                    onClick = { onSubtractClick() },
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = green),
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Agregar")
                }
            }

            Spacer(modifier = Modifier.width(20.dp))

            // Botón de eliminar
            IconButton(
                onClick = { onRemoveClick() },
                modifier = Modifier.size(30.dp).offset(x = 27.dp),
                colors = IconButtonDefaults.iconButtonColors(contentColor = gray),
            ) {
                Icon(Icons.Default.Delete, contentDescription = "Eliminar")
            }
        }
    }
}

@Composable
fun Cart(cartState: CartState, booksInCart: MutableList<Book>) {
    val booksInCartState = remember { mutableStateListOf(*booksInCart.toTypedArray()) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.height(55.dp))
        // Título
        Text(
            text = "Tu Pedido",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = gray,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .wrapContentSize(Alignment.Center)
        )

        // Ordenar la lista por título
        booksInCartState.sortBy { it.title }

        // Lista de libros en el carrito
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .border(1.25.dp, green)
        ) {
            // Lista de libros en el carrito
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(booksInCartState) { book ->
                    BookItem(
                        book = book,
                        onRemoveClick = {
                            booksInCartState.remove(book)
                        },
                        onAddClick = { // Incrementar cantidad del libro
                            book.quantity++
                            // Importante: Notificar el cambio en la lista mutable
                            booksInCartState.remove(book)
                            booksInCartState.add(book)
                        },
                        onSubtractClick = { // Reducir cantidad del libro
                            if (book.quantity > 1) {
                                book.quantity--
                                // Importante: Notificar el cambio en la lista mutable
                                booksInCartState.remove(book)
                                booksInCartState.add(book)
                            }
                        }
                    )
                }
            }
        }

        // Grupo de opciones para elegir el método de entrega
        DeliveryOptions(cartState)

        // Calcular subtotal, gastos de envío y total
        PricingSummary(booksInCartState, cartState.deliveryOption)

        // Botón de acción
        ActionButton(cartState.deliveryOption)
        Spacer(modifier = Modifier.height(50.dp))
    }
}

@Composable
fun DeliveryOptions(cartState: CartState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .wrapContentSize(Alignment.CenterEnd)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                        .height(32.dp)
            ) {
                RadioButton(
                    selected = cartState.deliveryOption == DeliveryOption.PICK_UP,
                    onClick = { cartState.deliveryOption = DeliveryOption.PICK_UP },
                    colors = RadioButtonDefaults.colors(green)
                )
                Text("Recoger en tienda")
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .height(32.dp)
            ) {
                RadioButton(
                    selected = cartState.deliveryOption == DeliveryOption.HOME_DELIVERY,
                    onClick = { cartState.deliveryOption = DeliveryOption.HOME_DELIVERY },
                    colors = RadioButtonDefaults.colors(green)
                )
                Text("Envío a domicilio")
            }
        }
    }
}

@Composable
fun PricingSummary(booksInCart: List<Book>, deliveryOption: DeliveryOption) {
    val subtotal = booksInCart.sumOf { it.price * it.quantity}
    val deliveryCost = if (deliveryOption == DeliveryOption.PICK_UP) 0.0 else 2.99
    val total = subtotal + deliveryCost

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text("Subtotal", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text("${"%.2f".format(subtotal)}€", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text("Gastos de envío", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text("${"%.2f".format(deliveryCost)}€", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text("Total", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text("${"%.2f".format(total)}€", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}



@Composable
fun ActionButton(deliveryOption: DeliveryOption) {
    Button(
        onClick = {
            if (deliveryOption == DeliveryOption.PICK_UP) {
                // Navegar a la pantalla de selección de tienda (no implementada)
            } else {
                // Navegar a la pantalla de compra (no implementada)
            }
        },
        colors = ButtonDefaults.buttonColors(white),
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        border = BorderStroke(2.dp, green)

    ) {
        if (deliveryOption == DeliveryOption.PICK_UP) {
            Text("Elegir tienda",
                color= gray
            )
        } else {
            Text("Ir a pagar",
                color= gray
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun CartScreen() {
    val cartState = remember { CartState() }
    val booksInCart = remember { mutableStateListOf<Book>() }

    // Agrega libros al carrito de compras (puedes modificar esta lista según tus necesidades)
    booksInCart.add(Book("Libro 1", "Autor 1", 19.99, "https://m.media-amazon.com/images/I/51F0ZfflZKL._SY445_SX342_.jpg"))
    booksInCart.add(Book("Libro 2", "Autor 2", 20.99, "https://m.media-amazon.com/images/I/51F0ZfflZKL._SY445_SX342_.jpg"))
    booksInCart.add(Book("Libro 3", "Autor 3", 18.99, "https://m.media-amazon.com/images/I/51F0ZfflZKL._SY445_SX342_.jpg"))
    booksInCart.add(Book("Libro 4", "Autor 4", 30.99, "https://m.media-amazon.com/images/I/51F0ZfflZKL._SY445_SX342_.jpg"))
    Scaffold(
        topBar = {
            // Parte superior de la pantalla (TopBar)
            TopBar()
        },
        bottomBar = {
            // Parte inferior de la pantalla (BottomBar)
            BottomBar()
        }
    ) {
        // Contenido principal
        Box(
            modifier = Modifier
                .fillMaxSize()
        ) {
            Cart(cartState, booksInCart)
        }
    }
}

@Preview
@Composable
fun CartPreview() {
    CartScreen()
}

enum class DeliveryOption {
    PICK_UP,
    HOME_DELIVERY
}

class CartState {
    var deliveryOption by mutableStateOf(DeliveryOption.PICK_UP)
}
