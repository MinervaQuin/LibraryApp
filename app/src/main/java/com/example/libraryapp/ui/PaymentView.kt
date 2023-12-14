package com.example.libraryapp.ui


import android.widget.Toast
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.PaymentMethodFormEvent
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentAdressFormEvent
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.white
import com.example.libraryapp.viewModel.PaymentViewModel
import com.example.libraryapp.viewModel.shipmentViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun PaymentGateway(viewModel: PaymentViewModel = hiltViewModel()) {

    val state = viewModel.state

    val context = LocalContext.current

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect{event ->
            when(event){
                is PaymentViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Datos Guardados",
                        Toast.LENGTH_LONG
                    ).show()

                }
            }
        }
    }

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
            value = state.card,
            onValueChange = { viewModel.onEvent(PaymentMethodFormEvent.CardChanged(it)) },
            isError = state.cardError,
            placeHolder = "Número de tarjeta",
            icon = Icons.Default.CreditCard,
            visualTransformation = VisualTransformation.None,
            keyboardType = KeyboardType.Number
        )
        if (state.cardError != null) {
            Text(
                text = state.cardError,
                color = Color.Red
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

        PaymentTextField(
            value = state.name,
            onValueChange = { viewModel.onEvent(PaymentMethodFormEvent.OwnerChanged(it)) },
            isError = state.nameError,
            placeHolder = "Nombre del titular",
            icon = Icons.Default.Person,
            visualTransformation = VisualTransformation.None,
            keyboardType = KeyboardType.Text
        )
        if (state.nameError != null) {
            Text(
                text = state.nameError,
                color = Color.Red
            )
        }

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
                    value = state.expireDate,
                    onValueChange = { viewModel.onEvent(PaymentMethodFormEvent.ExpirationDateChanged(it)) },
                    isError = state.expireDateError,
                    placeHolder = "mm/aaaa",
                    icon = Icons.Default.CalendarToday,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Text,
                )
                if (state.expireDateError != null) {
                    Text(
                        text = state.expireDateError,
                        color = Color.Red
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Box(
                modifier = Modifier.width(142.dp)  // Ajusta el ancho como desees
            ) {
                PaymentTextField(
                    value = state.cvv,
                    onValueChange = { viewModel.onEvent(PaymentMethodFormEvent.CvvChanged(it)) },
                    isError = state.cvvError,
                    placeHolder = "CVV",
                    icon = Icons.Default.Lock,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Number
                )
                if (state.cvvError != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = state.cvvError,
                        color = Color.Red
                    )
                }
            }

        }

        Button(
            onClick = {
                // TODO:

                    viewModel.submitData()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 150.dp, start = 30.dp, end = 30.dp)
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
                contentDescription = null // Descripción para accesibilidad
            )
        },
    )
}


