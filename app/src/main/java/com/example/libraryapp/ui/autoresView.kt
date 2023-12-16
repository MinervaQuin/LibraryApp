package com.example.libraryapp.ui

import android.app.ProgressDialog
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.ui.theme.RatingBar
import com.example.libraryapp.viewModel.AuthorViewModel
import com.example.libraryapp.viewModel.CartViewModel
import com.example.libraryapp.viewModel.ShoppingCart
import com.example.libraryapp.viewModel.autoresViewModel

@Composable
fun autoresView(navController: NavController, viewModel: autoresViewModel) {
    val loading by viewModel.loading.collectAsState()
    val context = LocalContext.current
    if (loading) {
        DisposableEffect(Unit) {
            val progressDialog = ProgressDialog(context)
            progressDialog.setTitle("Cargando...")
            progressDialog.setMessage("Por favor, espere")
            progressDialog.setCancelable(true)
            progressDialog.show()

            onDispose {
                progressDialog.dismiss()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = "Autores",
            style = TextStyle(
                fontSize = 43.sp,
                fontWeight = FontWeight(700),
                color = GreenApp,
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp)
        )
        for (i in 0 until viewModel.autores.size step 2) {
            val autor1 = viewModel.autores.getOrNull(i)
            val autor2 = viewModel.autores.getOrNull(i + 1)

            Row(
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
            ) {
                if (autor1 != null) {
                    autorPreview(autor = autor1, navController = navController,viewModel)
                }
                if (autor2 != null) {
                    autorPreview(autor = autor2, navController = navController,viewModel)
                }
            }
        }
    }
}

@Composable
fun autorPreview(autor: Author?, navController: NavController,viewModel: autoresViewModel) {
    Box(
        modifier = Modifier
            .padding(10.dp)
            .width(150.dp)
            .height(220.dp)
            .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
            .border(width = 1.dp, color = Color(0xFF000000))
            .clickable {
                viewModel.setnewAutor(autor)
                navController.navigate("AuthorDestination")
            }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            AsyncImage(
                model = autor!!.cover,
                contentDescription = null,
                modifier = Modifier
                    .width(102.dp)
                    .height(163.2.dp)
                    .align(Alignment.CenterHorizontally),
                contentScale = ContentScale.Crop
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )
            Text(
                text = autor.name,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                )
            )
        }
    }
}
