package com.example.libraryapp.ui


import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.viewModel.CartViewModel
import com.example.libraryapp.viewModel.ShoppingCart
import com.example.libraryapp.ui.theme.GreenAppOpacity
import com.example.libraryapp.viewModel.homeViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.CollectionSamples
import com.example.libraryapp.model.resources.LongCollectionSamples
import com.example.libraryapp.model.resources.carouselImage
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.ui.theme.SearchAppBar
import com.example.libraryapp.ui.theme.rojoSangre
import com.example.libraryapp.viewModel.SearchViewModel
import kotlinx.coroutines.delay


@Composable
fun HomeView(
    userData: UserData?, //TODO esto hay que ponerlo en el viewModel
    onSignOut: () -> Unit,
    viewModel: homeViewModel,
    navController: NavHostController
    ) {
    val cartViewModel: CartViewModel = ShoppingCart.getViewModelInstance()


    val collectionsArray by viewModel.collectionArray.collectAsState()
    val largeCollectionSamplesArray by viewModel.largeCollectionSamplesArray.collectAsState()
    val carouselImageArray by viewModel.carouselImageArray.collectAsState()
    val searchViewModel : SearchViewModel = hiltViewModel()

    SearchAppBarHP(
        searchViewModel = searchViewModel,
        navController = navController
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize(), // Agregar padding vertical
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        item {
            MiCarruselConAutoScroll(carouselImageArray)
        }

        val chunkedCollections = collectionsArray.drop(2).chunked(2)
        item {
            Row(){
                CollectionBox(collection = collectionsArray[0], navController)
                CollectionBox(collection = collectionsArray[1], navController)
            }
        }
        chunkedCollections.forEachIndexed { index, pair ->
            item {
                Row {
                    pair.forEach { collectionSample ->
                        CollectionBox(collection = collectionSample, navController)
                    }
                }
            }

            // Usa el índice del chunk para seleccionar el LargeCollectionBox
            val largeCollectionIndex = index // O cualquier lógica que desees aplicar aquí
            if (largeCollectionIndex < largeCollectionSamplesArray.size) {
                item {
                    LargeCollectionBox(collection = largeCollectionSamplesArray[largeCollectionIndex], navController)
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
                viewModel.getUserDataAndLog()
            }) {
                Text(text = "Probar el Coso")
            }
        }
    }



}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MiCarruselConAutoScroll(carouselImageArray: List<carouselImage>) {
    val collectionBoxWidth = 150.dp
    val padding = 8.dp
    val spacing = 4.dp
    val totalWidth = (collectionBoxWidth * 2) + (padding * 2) + spacing

    val showAlertDialog = remember { mutableStateOf(false) }

    val pagerState = rememberPagerState(
        pageCount = { carouselImageArray.size }
    )
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            while (true) {
                delay(3000)
                val siguientePagina = (pagerState.currentPage + 1) % carouselImageArray.size
                pagerState.animateScrollToPage(siguientePagina)
            }
        }
    }
    if (showAlertDialog.value) {
        AlertDialog(
            onDismissRequest = { showAlertDialog.value = false },
            title = { Text("A donde vas, cachonda?!") },
            text = { Text("Mira no sé si tiene que ser sheila, andrew, o tú, minerva, pero aquí faltan vistas!!") },
            confirmButton = {
                Button(onClick = { showAlertDialog.value = false }) {
                    Text("Aceptar")
                }
            }
        )
    }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .width(totalWidth)
                .height(200.dp)
        ) { page ->
            Image(
                painter = painterResource(id = carouselImageArray[page].imagen),
                contentDescription = "Imagen Carrusel",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .height(150.dp)
                    .width(totalWidth)
                    .border(2.dp, Color.Black)
                    .clickable {
                        // Aquí va la lógica al hacer clic en la imagen, por ejemplo:
                        showAlertDialog.value = true
                    }
            )
        }
    }
}


@Composable
fun CollectionBox(collection: CollectionSamples, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .height(250.dp)
            .width(150.dp)
            .border(2.dp, Color.Black, shape = RectangleShape)
            .clickable {
                ShoppingCart.setSelectedCategory(collection.route)
                navController.navigate("category")
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
                    .background(collection.color) // Fondo negro para el texto
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
fun LargeCollectionBox(collection: LongCollectionSamples, navController: NavController) {
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
                .clickable {
                    ShoppingCart.setSelectedCategory(collection.route)
                    navController.navigate("category")
                },
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
                    ShoppingCart.setSelectedCategory(collection.route)
                    navController.navigate("category")
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

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBarHP(searchViewModel: SearchViewModel,
                   navController: NavHostController)
{
    var searchString by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val softwareKeyboardController = LocalSoftwareKeyboardController.current

    SelectionContainer {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 15.dp, start = 10.dp, end = 10.dp)
                .height(50.dp),

            keyboardActions = KeyboardActions(
                onDone = {
                    searchViewModel.setSearchedString(searchString)
                    Log.d("firebase", "SEARCH STRING HOME: " + searchViewModel.getSearchedString())
                    navController.navigate("SearchScreen")
                }
            ),

            value = searchString,
            onValueChange = { searchString = it },
            singleLine = true,
            leadingIcon = {
                IconButton(onClick = {
                    ShoppingCart.setNavController(navController)
                    searchViewModel.initiateScan(context)
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.upc_scan),
                        contentDescription = "Close Icon",
                        tint = GreenApp
                    )
                }
            },
            trailingIcon = {

                IconButton(onClick = {
                    Log.d("firebase", "SEARCH STRING HOME: " + searchViewModel.getSearchedString())
                    searchViewModel.setSearchedString(searchString)
                    Log.d("firebase", "SEARCH STRING HOME: " + searchViewModel.getSearchedString())
                    navController.navigate("SearchScreen")
                }) { // Ir a pagina de scan codigo de barras

                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = "Search Icon",
                        tint = Color(0xFF77CF7C)
                    )
                }
            },
            colors = TextFieldDefaults.outlinedTextFieldColors(

                unfocusedBorderColor = GreenApp,
                focusedBorderColor = GreenApp,
                cursorColor = GreenApp
            ),

            )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewhomeView() {
    //val colecionPrueba = Collection("Prueba", "Cositas bro", "https://m.media-amazon.com/images/I/6135vNR5sCL._AC_UF1000,1000_QL80_.jpg")
    //CollectionBox(homeViewModel.CollectionSamples)

}