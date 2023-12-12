package com.example.libraryapp.ui

import android.app.Activity
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.red
import com.example.libraryapp.theme.white
import com.example.libraryapp.viewModel.profileViewModel
import android.Manifest
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.compose.ui.layout.ContentScale
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@Composable
fun ProfileScreen(
    viewModel: profileViewModel,
    navController : NavController
){
    val userData by viewModel.userData.collectAsState()
    val imageUrl = remember { mutableStateOf("") }

    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var showImagePickerDialog by remember { mutableStateOf(false) }

    val REQUEST_CAMERA_PERMISSION = 1 //TODO Esta variable me da que hay que cambiarla, huele a chapuza
    /*
    Aviso a navegantes, este código es más frágil y volátil que el gobierno actual
    cuando lo escribí, solo Dios y yo sabíamos como funcionaba, ahora solo Dios
    lo tiene claro, por favor, no intenteis "refactorizar" ni mucho menos "hacerlo más eficiente"
     */

    val imageUrlState = remember { mutableStateOf("") }


    val cropImageLauncher = rememberLauncherForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            val croppedImageUri = result.uriContent
            croppedImageUri?.let { uri ->
                viewModel.uploadProfileImage(uri, onSuccess = { newImageUrl ->
                    viewModel.getProfileImage(
                        onSuccess = { fetchedUrl ->
                            imageUrlState.value = fetchedUrl
                            Toast.makeText(
                                context,
                                "Imagen Actualizada",
                                Toast.LENGTH_LONG
                            ).show()
                        },
                        onFailure = { exception ->
                            Toast.makeText(
                                context,
                                "Error al bajar la imagen",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    )
                }, onFailure = { exception ->
                    Toast.makeText(
                        context,
                        "Error al subir la imagen",
                        Toast.LENGTH_LONG
                    ).show()
                })
            }
        } else {
            // Manejar error en el recorte
            val exception = result.error
            // Opcional: Manejar el error
        }
    }

    // Lanzador para seleccionar imagen de la galería
    val imagePickerLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { cropImageLauncher.launch(CropImageContractOptions(it, CropImageOptions())) }
    }

    // Lanzador para tomar foto con la cámara
    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            // Aquí manejas la imagen capturada usando imageUri
            imageUri?.let { uri ->
                cropImageLauncher.launch(CropImageContractOptions(uri, CropImageOptions()))
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getProfileImage(
            onSuccess = { url ->
                imageUrlState.value = url
            },
            onFailure = { exception ->
                // Manejar el error, por ejemplo, mostrar un mensaje
            }
        )
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted: Boolean ->
            if (isGranted) {
                // Permiso concedido, proceder con la acción
                imageUri = viewModel.createImageUri(context)
                imageUri?.let { uri ->
                    takePictureLauncher.launch(uri)
                }
            } else {
                // Permiso denegado, mostrar un mensaje
                Toast.makeText(
                    context,
                    "No se puede abrir la cámara",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    )

    if (showImagePickerDialog) {
        AlertDialog(
            onDismissRequest = { showImagePickerDialog = false },
            title = { Text("Seleccionar Imagen") },
            text = { Text("Elige de dónde quieres seleccionar la imagen.") },
            confirmButton = {
                TextButton(onClick = {
                    imagePickerLauncher.launch("image/*")
                    showImagePickerDialog = false
                }) {
                    Text("Galería")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    when (PackageManager.PERMISSION_GRANTED) {
                        ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) -> {
                            // Permiso ya concedido, proceder con la acción
                            imageUri = viewModel.createImageUri(context)
                            imageUri?.let { uri ->
                                takePictureLauncher.launch(uri)
                            }
                        }
                        else -> {
                            // Solicitar permiso
                            permissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    }
                    showImagePickerDialog = false
                }) {
                    Text("Cámara")
                }
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 10.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        AsyncImage(
            model = imageUrlState.value,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color.Black, CircleShape)
                .clickable {
                    // TODO: Acciones al hacer clic en la imagen
                },
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = userData?.userName ?: "Error")

        Spacer(modifier = Modifier.height(35.dp))

        // Buttons
        Button(
            onClick = {
                showImagePickerDialog = true
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end= 30.dp, start =30.dp )
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(white),
            border = BorderStroke(2.dp, green)
        ) {
            Text("Cambiar Foto de Perfil")
        }

        Spacer(modifier = Modifier.height(15.dp))

        Button(
            onClick = {
                // TODO: cambiar correo
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(end= 30.dp, start =30.dp )
                .height(40.dp),
            colors = ButtonDefaults.buttonColors(white),
            border = BorderStroke(2.dp, green)
        ) {
            Text("Mis compras")
        }

        Spacer(modifier = Modifier.height(85.dp))

        // Logout Button
        Button(
            onClick = {
                viewModel.logMeOutPLEASE()
                Toast.makeText(
                    context,
                    "Sesión Cerrada",
                    Toast.LENGTH_LONG
                ).show()
                viewModel.viewModelScope.launch {
                    delay(1000)
                }
                navController.navigate("firstScreens")
                //navController.popBackStack()
            },
            modifier = Modifier
                .height(40.dp)
                .width(180.dp)
                .padding(end = 10.dp)
                .align(Alignment.End),
            colors = ButtonDefaults.buttonColors(white),
            border = BorderStroke(2.dp, red )
        ) {
            Text("Cerrar sesión")
        }

    }
}

