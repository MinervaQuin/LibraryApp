package com.example.libraryapp.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.viewModel.AuthorViewModel
import com.example.libraryapp.viewModel.autoresViewModel

@Composable
fun autoresView (navController: NavController, ViewModel: autoresViewModel){
    Column(
        modifier = Modifier
            .fillMaxSize()
    ){
        Text(
            text = "Autores",
            style = TextStyle(
                fontSize = 43.sp,
                fontWeight = FontWeight(700),
                color = GreenApp,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp)
        )
    }
}