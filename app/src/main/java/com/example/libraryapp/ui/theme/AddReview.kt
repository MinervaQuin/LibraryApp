package com.example.libraryapp.ui.theme

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.SemanticsProperties.ImeAction
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.text.input.ImeAction

@Composable
fun AddReview() {


}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyDialog(bookTitle: String = "Reina Roja") {
    // State to track whether the dialog is open or closed
    var showDialog by remember { mutableStateOf(false) }
    var myRating by remember { mutableStateOf(0) }
    var comentarioTexto by remember { mutableStateOf("") }
    var comentarios: MutableList<String> by remember { mutableStateOf(mutableListOf()) }


    // Button to open the dialog
    Button(onClick = { showDialog = true }) {
        Text("Open Dialog")
    }

    // Dialog composable
    if (showDialog) {
        Dialog(
            onDismissRequest = {
                showDialog = false
            },

        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
                    .background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("¿Has leído " + bookTitle + "?",
                    style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold)
                )
                Text("Deja tu opinión para ayudar a otros lectores",
                    style = TextStyle(fontSize = 15.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
                )
                AnimatedRatingBar(currentRating = myRating, onRatingChanged = {myRating = it} ) //conectar con la pantalla anterior

                OutlinedTextField(value= "", onValueChange = {} ,
                    modifier = Modifier.fillMaxWidth()
                        .padding(start = 15.dp, end = 15.dp),
                    colors = TextFieldDefaults.outlinedTextFieldColors(

                        unfocusedBorderColor = GreenApp,
                        focusedBorderColor = GreenApp,
                        cursorColor = GreenApp
                    ))

                OutlinedTextField(
                    value = comentarioTexto,
                    onValueChange = { comentarioTexto = it },
                    label = { Text("Ingresa tu comentario") },
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = androidx.compose.ui.text.input.ImeAction.Send

                    ),
                    keyboardActions = KeyboardActions(
                        onSend = {
                            // Agregar el comentario a la lista
                            Log.d("asdf", comentarioTexto)
                            myRating = 0
                            comentarios.add(comentarioTexto)
                            comentarioTexto = ""
                        }
                    )
                )
                Button(
                    onClick = { showDialog = false}
                ) {
                    Text("Enviar")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Greeting6Preview() {
    LibraryAppTheme {
        MyDialog()
    }
}
