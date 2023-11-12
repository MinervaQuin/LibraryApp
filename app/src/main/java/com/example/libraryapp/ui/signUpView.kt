package com.example.libraryapp.ui


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
import androidx.compose.material3.AlertDialogDefaults.shape
import androidx.compose.runtime.collectAsState
import androidx.compose.foundation.shape.*
import androidx.compose.material3.*
import androidx.compose.ui.unit.dp

@Composable
fun signUpView(signUpViewModel: signUpViewModel = viewModel(), navController: NavController){
    //@TODO Sustituir el fondo por vectores animados
    val image = painterResource(R.drawable.vector_sign_up)

    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var userEmail by remember { mutableStateOf(TextFieldValue("")) }
    var userBirthDay by remember { mutableStateOf(TextFieldValue("")) }
    var userPassword by remember { mutableStateOf(TextFieldValue("")) }
    var userPasswordConfirm by remember { mutableStateOf(TextFieldValue("")) }

    var selectedDate by remember { mutableStateOf("") }



    val loading by signUpViewModel.loading.collectAsState()
    val message by signUpViewModel.message.collectAsState()

    val showFirstScreen by signUpViewModel.showFirstScreen2.collectAsState()

    BackHandler {
        if(showFirstScreen){
            navController.navigate("login") {
                // Configuraciones adicionales de navegación si las necesitas
                popUpTo("signUp") { inclusive = true }
            }
        }else{
            signUpViewModel.changeScreen()
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

        Row (
            modifier = Modifier
                .align(Alignment.Center)
                //.fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ){

            Column (
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                putText(text = "Registro", color = verdeFuerte, fontSize = 58.sp)

                if(showFirstScreen){
                    Spacer(modifier = Modifier.height(8.dp))
                    InputField(value = userName, onChange = {userName = it}, label = "Nombre de Usuario", icon = Icons.Outlined.AccountCircle)
                    Spacer(modifier = Modifier.height(8.dp))
                    InputField(value = userEmail, onChange = {userEmail = it}, label = "Correo", icon = Icons.Outlined.Email)
                    Spacer(modifier = Modifier.height(8.dp))
                    DatePickerNice()
                }
                else{
                    InputField(value = userPassword, onChange = {userPassword = it}, label = "Contraseña", icon = Icons.Outlined.Lock, visualTransformation = PasswordVisualTransformation())
                    Spacer(modifier = Modifier.height(8.dp))
                    InputField(value = userPasswordConfirm, onChange = {userPasswordConfirm = it}, label = "Confirmar contraseña", icon = Icons.Outlined.Lock, visualTransformation = PasswordVisualTransformation())
                    Row(
                        modifier = Modifier

                            .padding(16.dp),
                    ) {
                        Button(
                            onClick = { signUpViewModel.registerUser(userEmail.text, userPassword.text) },
                            modifier = Modifier

                                .height(50.dp),
                            colors = ButtonDefaults.buttonColors(verdeFuerte),
                            shape = CircleShape,
                        ) {
                            Text("Registrarse", modifier = Modifier.padding(8.dp))
                        }

                    }
                }
                Spacer(modifier = Modifier.height(8.dp))


            }
        }
        if(showFirstScreen){
            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    //.fillMaxWidth()
                    .padding(16.dp),

                ){
                Button(
                    onClick = { signUpViewModel.changeScreen() },
                    colors = ButtonDefaults.buttonColors(verdeFuerte),
                    shape = CircleShape,
                    modifier = Modifier
                        .size(75.dp),

                    ) {
                    Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "Avanzar")
                }
                Spacer(modifier = Modifier.height(150.dp))
            }
        }
        else{

        }

        
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerNice() {
    val calendarState = rememberSheetState()
    val selectedDate = remember { mutableStateOf(LocalDate.now().minusDays(3)) }
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")
    var isDialogOpen by remember { mutableStateOf(false) }

    // Suponemos que CalendarDialog es un composable que muestra un diálogo
    // y utiliza calendarState para manejar su estado.
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
    //TODO Controlar la logica cuando se introduce la fecha a mano en vez de con el calendario
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


    /*
    Button(
        onClick = {
        calendarState.show()
                  },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE7E0EC)), // Color de fondo similar al de los TextField
        modifier = Modifier.size(width = 275.dp, height = 53.dp),

        )
    {
        // El Text ahora muestra la fecha seleccionada formateada
        Row(
            modifier = Modifier.fillMaxWidth(), // Asegúrate de que el Row ocupe el ancho completo del botón
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.aligned(Alignment.Start)
        ) {
            Icon(
                imageVector = Icons.Default.DateRange, // Usa tu icono deseado
                contentDescription = "Select Date", // Descripción para accesibilidad
                modifier = Modifier.padding(end= 8.dp), // Espacio entre el icono y el texto
                tint = Color.Black // Establece el color del ícono a negro
            )
            Text(
                text = dateFormatter.format(selectedDate.value),
                color = Color(0xFF49454F) // Asegúrate de que el color del texto sea visible contra el color de fondo del botón
            )
        }
    }*/




/*
@Composable
fun DatePickerDemo(onDateSelected: (String) -> Unit) {
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // rememberDatePickerDialog es una función de extensión para recordar el DatePickerDialog
    val datePickerDialog = rememberDatePickerDialog(year, month, day) { _, year, month, day ->
        // Los meses en Calendar empiezan desde 0, por lo que se suma 1 para obtener la representación correcta
        onDateSelected("$day/${month + 1}/$year")
    }

    Button(onClick = { datePickerDialog.show() }) {
        Text(text = "Select your birth date")
    }
}

// Función de extensión para recordar el DatePickerDialog y evitar la recreación en cada recomposición
@Composable
private fun rememberDatePickerDialog(
    year: Int,
    month: Int,
    day: Int,
    onDateSet: (DatePicker, Int, Int, Int) -> Unit
): DatePickerDialog {
    val context = LocalContext.current
    return remember {
        DatePickerDialog(context, onDateSet, year, month, day).apply {
            // Configuraciones adicionales del DatePickerDialog pueden ir aquí si es necesario
        }
    }
}
*/


@Preview(showBackground = true)
@Composable
fun signUpViewPreview() {
    val navController = rememberNavController()
    signUpView(navController = navController)
}