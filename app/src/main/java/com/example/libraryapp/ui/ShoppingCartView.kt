package com.example.libraryapp.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.white
import com.example.libraryapp.viewModel.CartViewModel


@Composable
fun BookItem(
    book: Book,
    quantity: Int,  // Añadido para representar la cantidad
    onRemoveClick: () -> Unit,
    onAddClick: () -> Unit,
    onSubtractClick: () -> Unit
) {
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
                .border(width = 1.dp, color = Color.Black)
        ) {
            AsyncImage(
                model = book.cover,
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
            Text(text = book.author_name)
            Text(text = "${book.price}€")
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Columna 3: Información del libro
        Column {
            Row {
                // Botón de quitar
                IconButton(
                    onClick = { onSubtractClick() },
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = green),
                ) {
                    Icon(Icons.Default.Remove, contentDescription = "Agregar")
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(text = quantity.toString(), fontSize = 18.sp)

                Spacer(modifier = Modifier.width(8.dp))

                // Botón de agregar
                IconButton(
                    onClick = { onAddClick() },
                    modifier = Modifier.size(30.dp),
                    colors = IconButtonDefaults.iconButtonColors(contentColor = green),
                ) {
                    Icon(Icons.Default.Add, contentDescription = "Agregar")
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
fun Cart(navController: NavController, cartViewModel: CartViewModel) {
    val cartItemsMap by cartViewModel.cartItems.collectAsState()

    LaunchedEffect(cartItemsMap) {
        cartViewModel.recalculateCart()
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

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
                items(cartItemsMap.keys.toList()) {book ->
                    val quantity = cartItemsMap[book] ?: 0

                    BookItem(
                        book = book,
                        quantity = quantity,
                        onRemoveClick = {
                            cartViewModel.removeBookFromCart(book)
                        },
                        onAddClick = {
                            cartViewModel.addBookToCart(book)
                        },
                        onSubtractClick = {
                            val newQuantity = quantity - 1
                            if (newQuantity > 0) {
                                cartViewModel.updateCartItemQuantity(book, newQuantity)
                            }
                        }
                    )
                }
            }
        }

        // Grupo de opciones para elegir el método de entrega
        DeliveryOptions(cartViewModel)

        // Calcular subtotal, gastos de envío y total
        PricingSummary(cartViewModel.cartState)

        // Botón de acción
        ActionButton(cartViewModel.cartState.value, navController)
    }
}


@Composable
fun DeliveryOptions(cartViewModel: CartViewModel) {
    val cartState = cartViewModel.cartState

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
                    selected = cartState.value.deliveryOption == CartViewModel.DeliveryOption.PICK_UP,
                    onClick = {
                        cartViewModel.updateDeliveryOption(CartViewModel.DeliveryOption.PICK_UP)
                    },
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
                    selected = cartState.value.deliveryOption == CartViewModel.DeliveryOption.HOME_DELIVERY,
                    onClick = {
                        cartViewModel.updateDeliveryOption(CartViewModel.DeliveryOption.HOME_DELIVERY)
                    },
                    colors = RadioButtonDefaults.colors(green)
                )
                Text("Envío a domicilio")
            }
        }
    }
}


@Composable
fun PricingSummary(cartState: MutableState<CartViewModel.CartState>) {
    val state = cartState.value // Obtener el valor actual de MutableState
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text("Subtotal", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text("${"%.2f".format(state.cartSubtotal.value)}€", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text("Gastos de envío", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text("${"%.2f".format(state.deliveryCost.value)}€", fontWeight = FontWeight.Bold, fontSize = 18.sp)
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentWidth()
            .padding(start = 8.dp, end = 8.dp)
    ) {
        Text("Total", fontSize = 18.sp)
        Spacer(modifier = Modifier.weight(1f))
        Text("${"%.2f".format(state.cartTotal.value)}€", fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}



@Composable
fun ActionButton(cartState: CartViewModel.CartState, navController: NavController) {
    Button(
        onClick = {
            if (cartState.deliveryOption == CartViewModel.DeliveryOption.PICK_UP) {
                // Navegar a la pantalla de selección de tienda (no implementada)
                navController.navigate("map")
            } else {
                // Navegar a la pantalla de compra (no implementada)
                navController.navigate("payment")
            }
        },
        colors = ButtonDefaults.buttonColors(white),
        modifier = Modifier
            .fillMaxWidth()
            .padding(50.dp),
        border = BorderStroke(2.dp, green)

    ) {
        if (cartState.deliveryOption == CartViewModel.DeliveryOption.PICK_UP) {
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




