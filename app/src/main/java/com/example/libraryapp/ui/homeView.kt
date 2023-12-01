package com.example.libraryapp.ui

import CartViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.ui.theme.GreenAppOpacity
import com.example.libraryapp.viewModel.homeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.ui.res.painterResource
import coil.compose.rememberImagePainter
import com.example.libraryapp.R
import com.example.libraryapp.ui.theme.rojoSangre


@Composable
fun HomeView(
    userData: UserData?, //TODO esto hay que ponerlo en el viewModel
    onSignOut: () -> Unit,
    viewModel: homeViewModel
    ) {

    val collectionsArray by viewModel.collectionArray.collectAsState()
    val largeCollectionSamplesArray by viewModel.largeCollectionSamplesArray.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 90.dp), // Agregar padding vertical
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val chunkedCollections = collectionsArray.chunked(2)
        chunkedCollections.forEachIndexed { index, pair ->
            item {
                Row {
                    pair.forEach { collectionSample ->
                        CollectionBox(collection = collectionSample)
                    }
                }
            }

            // Usa el índice del chunk para seleccionar el LargeCollectionBox
            val largeCollectionIndex = index // O cualquier lógica que desees aplicar aquí
            if (largeCollectionIndex < largeCollectionSamplesArray.size) {
                item {
                    LargeCollectionBox(collection = largeCollectionSamplesArray[largeCollectionIndex])
                }
            }
        }

        item {
            Button(onClick = onSignOut){
                Text(text = "Cerrar Sesión")
            }
        }
        item {
            Button(onClick = {
                //viewModel.getBookAndLog("B9svfDJglRgEPyN6wSAh")
                //viewModel.getAuthorAndLog("Rkwq8a3v54TV6FSGw2n9")
                //viewModel.getCollectionAndLog("oBMLVCnbNsPQJiPexKL7")
                //viewModel.getReviewsAndLog("B9svfDJglRgEPyN6wSAh")
                //viewModel.uploadReviewTest()

            }) {
                Text(text = "Probar el Coso")
            }
        }
    }
    /*
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Row(){
            CollectionBox(collection = colecionPrueba)
            CollectionBox(collection = colecionPrueba)
        }

        Button(onClick = onSignOut){
            Text(text = "Cerrar Sesión")
        }
        Button(onClick = {
            //viewModel.getBookAndLog("B9svfDJglRgEPyN6wSAh")
            //viewModel.getAuthorAndLog("Rkwq8a3v54TV6FSGw2n9")
            //viewModel.getCollectionAndLog("oBMLVCnbNsPQJiPexKL7")
            //viewModel.getReviewsAndLog("B9svfDJglRgEPyN6wSAh")
            //viewModel.uploadReviewTest()

        }) {
            Text(text = "Probar el Coso")
        }


    }
     */


}

@Composable
fun CollectionBox(collection: homeViewModel.CollectionSamples) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(250.dp)
            .width(150.dp)
            .border(2.dp, Color.Black, shape = RectangleShape)
            .clickable {
                Log.d("CollectionBoxClick", "Route: ${collection.route}")
            },
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RectangleShape // Bordes rectos
    ) {
        Column {
            Box(
                modifier = Modifier
                    .weight(1f) // Toma la mayor parte del espacio disponible
                    .fillMaxWidth()
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = collection.imageUrl),
                    contentDescription = "Book cover",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop // Ajusta la imagen para llenar el espacio asignado
                )
            }
            Box(
                modifier = Modifier
                    .weight(0.15f) // Toma una fracción más pequeña del espacio
                    .fillMaxWidth()
                    .background(GreenAppOpacity) // Fondo negro para el texto
            ) {
                Text(
                    text = collection.title.toUpperCase(),
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center) // Centra el texto en la caja más pequeña
                        .padding(2.dp)
                )
            }
        }
    }
}

@Composable
fun LargeCollectionBox(collection: homeViewModel.LongCollectionSamples) {
    val collectionBoxWidth = 150.dp
    val padding = 8.dp
    val spacing = 4.dp
    val totalWidth = (collectionBoxWidth * 2) + (padding * 2) + spacing

    if(collection.isComplete){
        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(totalWidth)
                .height(75.dp)
                .fillMaxWidth()
                .clickable { Log.d("CollectionBoxClick", "Route: ${collection.route}") },
            elevation = CardDefaults.elevatedCardElevation(8.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Image(
                    painter = painterResource(id = collection.imageRes), // Imagen de fondo
                    contentDescription = null,
                    contentScale = ContentScale.Crop, // Asegúrate de que la imagen se ajuste correctamente
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = collection.title.toUpperCase(), // Texto en mayúsculas
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center) // Centra el texto
                        .padding(8.dp)
                )
            }
        }
    }
    else{
        Card(
            modifier = Modifier
                .padding(8.dp)
                .width(totalWidth)
                .height(75.dp)
                .fillMaxWidth()
                .clickable {
                    Log.d("CollectionBoxClick", "Route: ${collection.route}")
                }, // Se extiende al ancho máximo disponible

            elevation = CardDefaults.elevatedCardElevation(8.dp), // Ajusta la elevación según sea necesario
            shape = RoundedCornerShape(12.dp) // Esquinas redondeadas
        ) {
            Row( // Distribución horizontal
                modifier = Modifier
                    .background(collection.color)
                    // Gradiente horizontal
                    .fillMaxSize()
            ) {
                Text(
                    text = collection.title.toUpperCase(), // Texto en mayúsculas
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                        .weight(1f)
                )
                Image(
                    painter = painterResource(id = collection.imageRes),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                        .fillMaxHeight() // Hace que la imagen llene la altura disponible
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewhomeView() {
    //val colecionPrueba = Collection("Prueba", "Cositas bro", "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg")
    //CollectionBox(homeViewModel.CollectionSamples)

}