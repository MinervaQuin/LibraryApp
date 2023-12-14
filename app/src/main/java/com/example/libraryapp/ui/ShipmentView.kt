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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContactMail
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.navigation.NavController
import com.example.libraryapp.model.validationModels.emailValidationUseCase.RegistrationFormEvent
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentAdressFormEvent
import com.example.libraryapp.theme.gray
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.white
import com.example.libraryapp.viewModel.shipmentViewModel


@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ShipmentGateway(navController: NavController) {
    val viewModel: shipmentViewModel = hiltViewModel()
    val state = viewModel.state

    val context = LocalContext.current



    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect{event ->
            when(event){
                is shipmentViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Datos Guardados",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate("Payment")
                }
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(), // Agregar padding vertical
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            Text(
                text = "¿Dónde quieres recibirlo?",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = gray,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .wrapContentSize(Alignment.Center)
            )
        }


        item {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(40.dp))

                ShipmentTextField(
                    value = state.name,
                    onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.NameChanged(it))},
                    isError = state.nameError,
                    placeHolder = "Nombre del destinatario",
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

                ShipmentTextField(
                    value = state.lastName,
                    onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.lastNameChanged(it))},
                    isError = state.lastNameError,
                    placeHolder = "Apellidos del destinatario",
                    icon = Icons.Default.Person,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Text
                )
                if (state.lastNameError != null) {
                    Text(
                        text = state.lastNameError,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                ShipmentTextField(
                    value = state.adress,
                    onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.AdressChanged(it)) },
                    isError = state.adressError,
                    placeHolder = "Dirección",
                    icon = Icons.Default.House,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Text
                )
                if (state.adressError != null) {
                    Text(
                        text = state.adressError,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    // Colocar los TextField de "Código Postal" y "Provincia" en la misma fila
                    Box(
                        modifier = Modifier.width(142.dp)  // Ajusta el ancho como desees
                    ) {
                        ShipmentTextField(
                            value = state.zipCode,
                            onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.ZipCodeChanged(it)) },
                            isError = state.zipCodeError,
                            placeHolder = "Código Postal",
                            icon = Icons.Default.ContactMail,
                            visualTransformation = VisualTransformation.None,
                            keyboardType = KeyboardType.Number
                        )

                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier.width(142.dp)  // Ajusta el ancho como desees
                    ) {
                        ShipmentTextField(
                            value = state.province,
                            onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.provinceChanged(it)) },
                            isError = state.provinceError,
                            placeHolder = "Provincia",
                            icon = Icons.Default.MyLocation,
                            visualTransformation = VisualTransformation.None,
                            keyboardType = KeyboardType.Text
                        )

                    }
                }
                if(state.provinceError != null || state.zipCodeError != null){
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ){
                        if (state.zipCodeError != null) {
                            Text(
                                text = state.zipCodeError,
                                color = Color.Red
                            )
                        }
                        Spacer(modifier = Modifier.height(32.dp))
                        if (state.provinceError != null) {
                            Text(
                                text = state.provinceError,
                                color = Color.Red
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                ShipmentTextField(
                    value = state.city,
                    onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.cityChanged(it)) },
                    isError = state.cityError,
                    placeHolder = "Ciudad",
                    icon = Icons.Default.LocationCity,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Text
                )
                if (state.cityError != null) {
                    Text(
                        text = state.cityError,
                        color = Color.Red
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier.width(205.dp)  // Ajusta el ancho como desees
                    ) {
                        ShipmentTextField(
                            value = state.phoneNumber,
                            onValueChange = { viewModel.onEvent(ShipmentAdressFormEvent.PhoneNumberChanged(it)) },
                            isError = state.phoneNumberError,
                            placeHolder = "Teléfono",
                            icon = Icons.Default.Phone,
                            visualTransformation = VisualTransformation.None,
                            keyboardType = KeyboardType.Number
                        )

                    }
                }
                if (state.phoneNumberError != null) {
                    Text(
                        text = state.phoneNumberError,
                        color = Color.Red
                    )
                }
                Button(
                    onClick = {

                        viewModel.onEvent(ShipmentAdressFormEvent.Submit)

                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 80.dp, start = 30.dp, end = 30.dp)
                        .height(40.dp),
                    colors = ButtonDefaults.buttonColors(white),
                    border = BorderStroke(2.dp, green)
                ) {
                    Text("Continuar", color = gray)
                }
                Spacer(modifier = Modifier.height(48.dp))
            }
        }

    }

}



@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun ShipmentTextField(
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



