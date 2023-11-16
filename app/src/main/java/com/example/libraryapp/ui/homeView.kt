package com.example.libraryapp.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.viewModel.homeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun HomeView(
    userData: UserData?,
    onSignOut: () -> Unit,
    viewModel: homeViewModel
    ) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        println(userData)
        if(userData?.profilePictureUrl != null){
            AsyncImage(
                model = userData.profilePictureUrl,
                contentDescription = "Imagen de Perfil",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(userData?.userName != null) {
            Text(
               text = userData.userName,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        if(userData?.userId != null){
            Text(
                text = userData.userId,
                textAlign = TextAlign.Center,
                fontSize = 36.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        Button(onClick = onSignOut){
            Text(text = "Cerrar Sesión")
        }
        Button(onClick = { viewModel.getBookAndLog("B9svfDJglRgEPyN6wSAh") }) {
            Text(text = "obtener libro")
        }
    }
}