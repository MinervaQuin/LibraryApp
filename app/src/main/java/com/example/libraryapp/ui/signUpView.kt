package com.example.libraryapp.ui


import android.util.Log
import android.widget.CheckBox
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.R
import com.example.libraryapp.theme.verdeFuerte
import com.example.libraryapp.viewModel.signUpViewModel
import androidx.compose.material3.Button
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.shape.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.libraryapp.model.RegistrationFormEvent
import com.example.libraryapp.ui.theme.rojoSangre
import com.example.libraryapp.ui.theme.rositaGracioso
import kotlinx.coroutines.flow.collect

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signUpView(viewModel: signUpViewModel = hiltViewModel(), navController: NavController){

    val image = painterResource(R.drawable.vector_sign_up)

    val state = viewModel.state
    val context = LocalContext.current

    val selectedDate = remember { mutableStateOf(LocalDate.now().minusDays(3)) }

    val shouldNavigate by viewModel.navigateToNextScreen.collectAsState()
    val showFirstScreen by viewModel.showFirstScreen2.collectAsState()

    LaunchedEffect(shouldNavigate) {
        if (shouldNavigate == true) {
            navController.navigate("login") {
                // Configuraciones adicionales de navegación si las necesitas
                popUpTo("seconScreens") { inclusive = true }
            }
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is signUpViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Registro Exitoso :D",
                        Toast.LENGTH_LONG
                    ).show()
                    navController.navigate("secondScreens")
                }
                is signUpViewModel.ValidationEvent.Failed -> {
                    Toast.makeText(
                        context,
                        event.errorMessage, // Usa el mensaje de error de la clase Failed
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }

    BackHandler {
        if(showFirstScreen){
            navController.navigate("login") {
                // Configuraciones adicionales de navegación si las necesitas
                popUpTo("signUp") { inclusive = true }
            }
        }else{
            viewModel.changeScreen()
        }

    }

    Box(modifier = Modifier.fillMaxSize()) {
        // Colocamos la imagen de fondo
        Image(
            painter = image,
            contentDescription = null, // Descripción para accesibilidad
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop // Ajusta la imagen al tamaño del Box, recortando si es necesario
        )

        Row(
            modifier = Modifier
                .align(Alignment.Center)
                //.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                putText(text = "Registro", color = verdeFuerte, fontSize = 58.sp)
                Spacer(modifier = Modifier.height(32.dp))
                putTextField(
                    value = state.name,
                    onValueChange = {viewModel.onEvent(RegistrationFormEvent.NameChanged(it))},
                    isError = state.nameError, placeHolder = "Nombre de Usuario",
                    icon = Icons.Outlined.AccountCircle,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Email)
                if (state.nameError != null) {
                    Text(
                        text = state.nameError,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                putTextField(
                    value = state.email,
                    onValueChange = {viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))},
                    isError = state.emailError, placeHolder = "Correo",
                    icon = Icons.Outlined.AccountCircle,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Email)
                if (state.emailError != null) {
                    Text(
                        text = state.emailError,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                putTextField(
                    value = state.password,
                    onValueChange = {viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))},
                    isError = state.passwordError, placeHolder = "Contraseña",
                    icon = Icons.Outlined.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password)
                if (state.passwordError != null) {
                    Text(
                        text = state.passwordError,
                        color = Color.Red,
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                putTextField(
                    value = state.repeatedPassword,
                    onValueChange = {viewModel.onEvent(RegistrationFormEvent.RepeatedPasswordChanged(it))},
                    isError = state.repeatedPasswordError, placeHolder = "Repetir Contraseña",
                    icon = Icons.Outlined.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password)
                if (state.repeatedPasswordError != null) {
                    Text(
                        text = state.repeatedPasswordError,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = state.acceptedTerms,
                                onCheckedChange = { viewModel.onEvent(RegistrationFormEvent.AcceptTerms(it)) }
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Tengo más de 18 años",
                                modifier = Modifier.align(Alignment.CenterVertically) // Alineación vertical del texto
                            )
                        }
                        if (state.termsError != null) {
                            Text(
                                text = state.termsError,
                                color = Color.Red
                            )
                        }
                    }
                }
                Button(
                    onClick = {
                        viewModel.onEvent(RegistrationFormEvent.Submit)
                    },
                    modifier = Modifier
                        .height(50.dp), // Altura del botón
                    colors = ButtonDefaults.buttonColors(containerColor = rojoSangre)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp) // Padding horizontal para el contenido
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.signup_vector),
                            contentDescription = "Icono de Registrarse",
                            modifier = Modifier.size(24.dp) // Tamaño del ícono
                        )
                        Spacer(Modifier.width(8.dp)) // Espacio entre el ícono y el texto
                        Text(
                            text = "Registrarse",
                            fontSize = 16.sp
                        )
                    }
                }

            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun putTextField(
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
            .height(55.dp),
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
            disabledIndicatorColor = Color.Transparent
        ),
        shape = RoundedCornerShape(16.dp),
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = "Icono de cuenta" // Descripción para accesibilidad
            )
        },
        visualTransformation = visualTransformation
    )
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerNice() {
    val calendarState = rememberSheetState()
    val selectedDate = remember { mutableStateOf(LocalDate.now().minusDays(3)) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
    var isDialogOpen by remember { mutableStateOf(false) }

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            selectedDate.value = date
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )

    // TextField que muestra la fecha y abre el diálogo al hacer clic
    TextField(
        value = dateFormatter.format(selectedDate.value),
        onValueChange = { /* No hacer nada para prevenir la edición */ },
        readOnly = true, // Hace que el TextField sea de solo lectura
        leadingIcon = {
            IconButton(onClick = { calendarState.show() }) {
                Icon(
                    imageVector = Icons.Default.DateRange,
                    contentDescription = "Select Date",
                    tint = Color.Black
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            textColor = Color.Black,
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        interactionSource = remember { MutableInteractionSource() }
            .also { interactionSource ->
                LaunchedEffect(interactionSource) {
                    interactionSource.interactions.collect {
                        if (it is PressInteraction.Release) {
                            calendarState.show()
                        }
                    }
                }
            }
    )
}
@Preview(showBackground = true)
@Composable
fun signUpViewPreview() {
    val navController = rememberNavController()
    //signUpView(navController = navController)
}