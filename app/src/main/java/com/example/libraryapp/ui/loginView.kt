package com.example.libraryapp.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.R
import com.example.libraryapp.viewModel.loginViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.libraryapp.theme.*
import androidx.compose.ui.text.style.TextDecoration


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginView(loginViewModel : loginViewModel = viewModel()){
    //@TODO: En vez de 1 solo elemento como fondo, hacer que sean 3 con animación de movimiento
    val image = painterResource(R.drawable.fondo_login)

    var userEmail by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val textFieldModifier = Modifier
        .background(Color.LightGray, RoundedCornerShape(8.dp))
        .padding(horizontal = 16.dp, vertical = 8.dp)

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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                putText(text = "Inicia", color = verdeFuerte, fontSize = 58.sp)
                putText(text = " Sesión", color = verdeFuerte, fontSize = 58.sp)

                Spacer(modifier = Modifier.height(8.dp))



                //TODO Hacer que al darle al enter cambie el focus en vez de poner un puto enter
                TextField(
                    value = userEmail,
                    onValueChange = { userEmail = it},
                    label = {
                        Text(text = "Correo")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Email,
                            contentDescription = "Correo"
                        )
                    },
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextField(
                    value = password,
                    onValueChange = { password = it},
                    label = {
                        Text(text = "Contraseña")
                    },
                    shape = RoundedCornerShape(20.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        disabledTextColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent
                    ),
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = "Contraseña"
                        )
                    }
                )

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "¿Has Olvidado la contraseña?")

            }
        }

        Row(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                //.fillMaxWidth()
                .padding(16.dp),

        ){
            Button(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.buttonColors(verdeFuerte),
                shape = CircleShape,
                modifier = Modifier
                    .size(75.dp),

                ) {
                Icon(imageVector = Icons.Outlined.ArrowForward, contentDescription = "Avanzar")
            }
            Spacer(modifier = Modifier.height(150.dp))
        }

        Row (
            modifier = Modifier
                .align(Alignment.BottomEnd)
                //.fillMaxWidth()
                .padding(16.dp),
        ){
            Text(text = "No estás registrado? ")
            Text(
                text = "Registrate",
                fontWeight = FontWeight.Bold, // Esto pone el texto en negrita
                textDecoration = TextDecoration.Underline // Esto subraya el texto
                )
            Spacer(modifier = Modifier.height(50.dp))
        }
        Spacer(modifier = Modifier.height(100.dp))
    }
}

@Composable
fun putText(
    text: String,
    fontSize: TextUnit,  // Parámetro adicional para el tamaño de la fuente
    modifier: Modifier = Modifier,
    color: Color = Color.Black,  // Parámetro de color con valor predeterminado
    fontWeight: FontWeight = FontWeight.Bold  // Parámetro de fontWeight con valor predeterminado
) {
    Text(
        text = text,
        modifier = modifier,
        style = TextStyle(
            fontSize = fontSize,
            fontWeight = fontWeight,
            lineHeight = 50.sp,
            color = color
        )
    )
}

@Preview(showBackground = true)
@Composable
fun LoginViewPreview() {
    LoginView()
}
/*
            OutlinedTextField(
                value = username,
                onValueChange = { username = it },
                label = { Text("Usuario") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                modifier = Modifier

                    .padding(4.dp),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.outlinedTextFieldColors(Color.LightGray)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(), //creo que aquí se llama a algo
                shape = RoundedCornerShape(20.dp)

            )
             */