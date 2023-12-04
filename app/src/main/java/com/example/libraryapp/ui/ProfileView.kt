package com.example.libraryapp.ui

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.libraryapp.theme.green
import com.example.libraryapp.theme.red
import com.example.libraryapp.theme.white

@Composable
fun ProfileScreen(){
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top= 10.dp, bottom = 10.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Image
        AsyncImage(
            model = "https://cdn.pixabay.com/photo/2015/10/05/22/37/blank-profile-picture-973460_1280.png",
            contentDescription = "Profile Picture",

            modifier = Modifier
                .size(250.dp)
                .clip(CircleShape)
                .border(width = 2.dp, color = Color.Black, CircleShape)
                .clickable { // TODO: clickar en la imagen
                    },
            )
        Spacer(modifier = Modifier.height(16.dp))

        Text("Nombre usuario")

        Spacer(modifier = Modifier.height(35.dp))

        // Buttons
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
            Text("Cambiar correo")
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
            Text("Cambiar contraseña")
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
                // TODO: cambiar correo
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