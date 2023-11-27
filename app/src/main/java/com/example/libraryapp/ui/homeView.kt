package com.example.libraryapp.ui

import CartViewModel
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.example.libraryapp.viewModel.homeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel


@Composable
fun HomeView(
    userData: UserData?, //TODO esto hay que ponerlo en el viewModel
    onSignOut: () -> Unit,
    viewModel: homeViewModel
    ) {
    val colecionPrueba = Collection("Prueba", "Cositas bro", "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg")
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){

        CollectionBox(collection = colecionPrueba)
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

}
data class Collection(
    val title: String,
    val subtitle: String,
    val imageUrl: String
)
@Composable
fun CollectionBox(collection: Collection) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(200.dp)
            .width(120.dp),
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
                    .weight(0.2f) // Toma una fracción más pequeña del espacio
                    .fillMaxWidth()
                    .background(Color.Black) // Fondo negro para el texto
            ) {
                Text(
                    text = collection.title.toUpperCase(),
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .align(Alignment.Center) // Centra el texto en la caja más pequeña
                        .padding(8.dp)
                )
            }
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewhomeView() {
    val colecionPrueba = Collection("Prueba", "Cositas bro", "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg")
    CollectionBox(colecionPrueba)

}