package com.example.libraryapp.ui

import android.app.DatePickerDialog
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.R
import com.example.libraryapp.theme.verdeFuerte
import com.example.libraryapp.viewModel.signUpViewModel
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*
import androidx.compose.material3.ButtonDefaults

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun signUpView(signUpViewModel: signUpViewModel = viewModel()){
    //@TODO Sustituir el fondo por vectores animados
    val image = painterResource(R.drawable.vector_sign_up)

    var userName by remember { mutableStateOf(TextFieldValue("")) }
    var userEmail by remember { mutableStateOf(TextFieldValue("")) }
    var userBirthDay by remember { mutableStateOf(TextFieldValue("")) }
    var userPassword by remember { mutableStateOf(TextFieldValue("")) }

    var selectedDate by remember { mutableStateOf("") }


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

                Spacer(modifier = Modifier.height(8.dp))
                InputField(value = userName, onChange = {userName = it}, label = "Nombre de Usuario", icon = Icons.Outlined.AccountCircle)
                Spacer(modifier = Modifier.height(8.dp))
                InputField(value = userEmail, onChange = {userEmail = it}, label = "Correo", icon = Icons.Outlined.AccountCircle)
                Spacer(modifier = Modifier.height(8.dp))
                DatePickerNice()

                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerNice() {
    val calendarState = rememberSheetState()
    val selectedDate = remember { mutableStateOf(LocalDate.now().minusDays(3)) }
    // Se utiliza para formatear la fecha seleccionada como una cadena
    val dateFormatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy")

    CalendarDialog(
        state = calendarState,
        selection = CalendarSelection.Date { date ->
            // Actualizar el estado de la fecha seleccionada
            selectedDate.value = date
        },
        config = CalendarConfig(
            monthSelection = true,
            yearSelection = true
        )
    )

    Button(
        onClick = {
        calendarState.show()
                  },
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.surface)
        )
    {
        // El Text ahora muestra la fecha seleccionada formateada
        Text(text = dateFormatter.format(selectedDate.value))
    }
}


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
    signUpView()
}