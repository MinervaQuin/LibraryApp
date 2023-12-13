package com.example.libraryapp.ui


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.white


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PaymentGateway() {
    var cardNumber by remember { mutableStateOf("") }
    var cardHolderName by remember { mutableStateOf("") }
    var cardExpiry by remember { mutableStateOf("") }
    var cardCvv by remember { mutableStateOf("") }

    var keyboardController by remember { mutableStateOf<SoftwareKeyboardController?>(null) }
    Text(
        text = "Pasarela de Pago",
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold,
        color = gray,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp,)
            .wrapContentSize(Alignment.Center)
    )
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Spacer(modifier = Modifier.height(40.dp))

        PaymentTextField(
            value = cardNumber,
            onValueChange = { cardNumber = it },
            isError = null,
            placeHolder = "Número de tarjeta",
            icon = Icons.Default.CreditCard,
            visualTransformation = VisualTransformation.None,
            keyboardType = KeyboardType.Number
        )
        Spacer(modifier = Modifier.height(16.dp))

        PaymentTextField(
            value = cardHolderName,
            onValueChange = { cardHolderName = it },
            isError = null,
            placeHolder = "Nombre del titular",
            icon = Icons.Default.Person,
            visualTransformation = VisualTransformation.None,
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            // Colocar los TextField de "mm/aaaa" y "CVV" en la misma fila
            Box(
                modifier = Modifier.width(142.dp)  // Ajusta el ancho como desees
            ) {
                PaymentTextField(
                    value = cardExpiry,
                    onValueChange = { cardExpiry = it },
                    isError = null,
                    placeHolder = "mm/aaaa",
                    icon = Icons.Default.CalendarToday,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Number,
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier.width(142.dp)  // Ajusta el ancho como desees
            ) {
                PaymentTextField(
                    value = cardCvv,
                    onValueChange = { cardCvv = it },
                    isError = null,
                    placeHolder = "CVV",
                    icon = Icons.Default.Lock,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Number
                )
            }
        }

        Button(
            onClick = {
                // TODO:
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp, start = 30.dp, end= 30.dp)
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(white),
            border = BorderStroke(2.dp, green)
        ) {
            androidx.compose.material.Text("Finalizar compra")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PaymentTextField(
    value: String,
    onValueChange: (String) -> Unit,
    isError: String?,
    placeHolder: String,
    icon: ImageVector,
    visualTransformation: VisualTransformation,
    keyboardType: KeyboardType
) {
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        isError = isError != null,
        modifier = Modifier
            .width(300.dp)
            .height(55.dp)
            .border(2.dp, green, RoundedCornerShape(16.dp)),
        placeholder = {
            Text(text = placeHolder)
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                keyboardController?.hide() // Oculta el teclado
            }
        ),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            containerColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        visualTransformation = visualTransformation,
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Icono de cuenta" // Descripción para accesibilidad
            )
        },
    )
}


