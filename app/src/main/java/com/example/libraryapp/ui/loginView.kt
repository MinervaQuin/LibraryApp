package com.example.libraryapp.ui


import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.libraryapp.theme.*
import androidx.compose.ui.text.style.TextDecoration
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.model.validationModels.emailValidationUseCase.RegistrationFormEvent
import com.example.libraryapp.viewModel.signUpViewModel
import kotlinx.coroutines.launch

@Composable
fun LoginView(viewModel : loginViewModel, navController: NavController){
    val image = painterResource(R.drawable.fondo_login)
    val googleIconImageVector = ImageVector.vectorResource(id = R.drawable.vector_google)

    val context = LocalContext.current
    val formState = viewModel.formState


    val coroutineScope = rememberCoroutineScope()

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if(result.resultCode == ComponentActivity.RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = viewModel.googleAuthUiClient.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    val state by viewModel.state.collectAsState()

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }


    LaunchedEffect(key1 = Unit){
        if(viewModel.googleAuthUiClient.getSignedInUser() != null){
            navController.navigate("secondScreens")
        }
    }



    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if(state.isSignInSuccessful) {
            Toast.makeText(
                context,
                "Sesión Iniciada",
                Toast.LENGTH_LONG
            ).show()

            navController.navigate("secondScreens")
            viewModel.resetState()
        }
    }

    LaunchedEffect(key1 = context) {
        viewModel.validationEvents.collect { event ->
            when (event) {
                is signUpViewModel.ValidationEvent.Success -> {
                    Toast.makeText(
                        context,
                        "Inicio Exitoso",
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

                putTextField(
                    value = formState.email,
                    onValueChange = {viewModel.onEvent(RegistrationFormEvent.EmailChanged(it))},
                    isError = formState.emailError, placeHolder = "Correo",
                    icon = Icons.Outlined.AccountCircle,
                    visualTransformation = VisualTransformation.None,
                    keyboardType = KeyboardType.Email)
                if (formState.emailError != null) {
                    Text(
                        text = formState.emailError,
                        color = Color.Red
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                putTextField(
                    value = formState.password,
                    onValueChange = {viewModel.onEvent(RegistrationFormEvent.PasswordChanged(it))},
                    isError = formState.passwordError, placeHolder = "Contraseña",
                    icon = Icons.Outlined.Lock,
                    visualTransformation = PasswordVisualTransformation(),
                    keyboardType = KeyboardType.Password)
                if (formState.passwordError != null) {
                    Text(
                        text = formState.passwordError,
                        color = Color.Red,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "¿Has Olvidado la contraseña?")
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        /*
                        viewModel.signInWithEmail(userEmail.text,password.text){
                            navController.navigate("homePage")
                        }*/
                        viewModel.onEvent(RegistrationFormEvent.Submit)
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
                    onClick = {
                        coroutineScope.launch {
                            val signInIntentSender = viewModel.googleAuthUiClient.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
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

@Preview(showBackground = true)
@Composable
fun PreviewLoginView() {
    // Creas un NavController ficticio que no hará nada en la previsualización
    val navController = rememberNavController()
    //LoginView(navController = navController, onSignInClick = null, )
}