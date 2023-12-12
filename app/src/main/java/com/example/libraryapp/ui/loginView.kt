package com.example.libraryapp.ui


import android.widget.Toast
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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.libraryapp.theme.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.model.firebaseAuth.SignInState

@Composable
fun LoginView(loginViewModel : loginViewModel = viewModel(), navController: NavController,state: SignInState, onSignInClick: () -> Unit){
    //@TODO: En vez de 1 solo elemento como fondo, hacer que sean 3 con animación de movimiento
    val image = painterResource(R.drawable.fondo_login)
    val googleIconImageVector = ImageVector.vectorResource(id = R.drawable.vector_google)



    var userEmail by remember { mutableStateOf(TextFieldValue("")) }
    var password by remember { mutableStateOf(TextFieldValue("")) }

    val context = LocalContext.current
    LaunchedEffect(key1 = state.signInError){
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
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
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                putText(text = "Inicia", color = verdeFuerte, fontSize = 58.sp)
                putText(text = " Sesión", color = verdeFuerte, fontSize = 58.sp)

                Spacer(modifier = Modifier.height(8.dp))



                //TODO Hacer que al darle al enter cambie el focus en vez de poner un puto enter
                InputField(value = userEmail, onChange = {userEmail = it}, label = "Correo", icon = Icons.Outlined.Email)

                Spacer(modifier = Modifier.height(8.dp))

                InputField(value = password, onChange = {password = it}, label = "Contraseña", icon = Icons.Outlined.Lock, visualTransformation = PasswordVisualTransformation())

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "¿Has Olvidado la contraseña?")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        //loginViewModel.updateCredentials(userEmail.text, password.text)
                        loginViewModel.signInWithEmail(userEmail.text,password.text){
                            navController.navigate("homePage")
                        }
                    },
                    modifier = Modifier
                        .height(50.dp) // Altura del botón
                        .width(300.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = verdeFuerte)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp) // Padding horizontal para el contenido
                    ) {
                        Icon(
                            imageVector = ImageVector.vectorResource(R.drawable.login_vector),
                            contentDescription = "Icono de Iniciar Sesión",
                            modifier = Modifier.size(24.dp) // Tamaño del ícono
                        )
                        Spacer(Modifier.width(8.dp)) // Espacio entre el ícono y el texto
                        Text(
                            text = "Iniciar Sesión",
                            fontSize = 16.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = onSignInClick,
                    modifier = Modifier
                        .width(300.dp)
                        .height(50.dp), // Altura del botón
                    colors = ButtonDefaults.buttonColors(containerColor = verdeFuerte)

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .padding(horizontal = 16.dp) // Padding horizontal para el contenido
                    ) {
                        Icon(
                            imageVector = googleIconImageVector,
                            contentDescription = "Icono de Google",
                            modifier = Modifier.size(24.dp) // Tamaño del ícono
                        )
                        Spacer(Modifier.width(8.dp)) // Espacio entre el ícono y el texto
                        Text(
                            text = "Iniciar Sesión con Google",
                            fontSize = 16.sp
                        )
                    }
                }



            }
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
                textDecoration = TextDecoration.Underline, // Esto subraya el texto
                modifier = Modifier.clickable {
                    navController.navigate("signUp") {
                        popUpTo("login") { inclusive = true }
                    }
                }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    value: TextFieldValue,
    onChange: (TextFieldValue) -> Unit,
    modifier: Modifier = Modifier,
    label: String,
    icon: ImageVector,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    TextField(
        value = value,
        onValueChange = onChange,
        label = { Text(text = label) },
        shape = RoundedCornerShape(20.dp),
        colors = TextFieldDefaults.textFieldColors(
            disabledTextColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent
        ),
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null
            )
        },
        visualTransformation = visualTransformation,
        modifier = modifier,

    )
}

@Preview(showBackground = true)
@Composable
fun PreviewLoginView() {
    // Creas un NavController ficticio que no hará nada en la previsualización
    val navController = rememberNavController()
    //LoginView(navController = navController, onSignInClick = null, )
}