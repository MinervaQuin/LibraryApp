package com.example.libraryapp.ui.theme


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.libraryapp.R
import com.example.libraryapp.theme.LibraryAppTheme


@Composable
fun ListOfBooks(
    names: List<String> = List(1000) { "$it" },
    modifier: Modifier = Modifier.padding(10.dp),
    navController: NavHostController,
    bookTitle: String = "Reina Roja"
){
    val navController2 = navController
    Column {
        SearchAppBar()
        Text(text = "Resultados para: " + bookTitle,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 6.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
        ){
            items(items = names) {
                BookItem(modifier = Modifier, navController)
            }
        }
    }

}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchAppBar() {
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp)
            .height(40.dp),

        value = "",
        onValueChange = {},
        leadingIcon = {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Search Icon",
                tint = Color(0xFF77CF7C)
            )
        },
        trailingIcon = {

            IconButton(onClick = { /*TODO*/ }) { // Ir a pagina de scan codigo de barras
                Icon(
                    painter = painterResource(id = R.drawable.upc_scan),
                    contentDescription = "Close Icon",
                    tint = GreenApp
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


@Composable
fun BookItem(modifier: Modifier, navController: NavHostController) {
    Column(
        modifier = Modifier
            .clickable { navController.navigate("BookDetailsView") } //BookDetail(
        ,verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        //ShowRectangle()
        LoadBookCover("https://m.media-amazon.com/images/I/41uWfObYYQL._SY445_SX342_.jpg")

        //LoadBookCover(imageUrl = "http://books.google.com/books/content?id=8U2oAAAAQBAJ&printsec=frontcover&img=1&zoom=1&edge=curl&source=gbs_api")

        //LoadBookCover(imageUrl = "https://books.google.com/books/content?id=8U2oAAAAQBAJ&printsec=frontcover&img=1&zoom=1")

        BookInfo()


    }
}


@Composable
fun LoadBookCover(imageUrl: String){
    Box(
        modifier = Modifier
            .width(180.dp)
            .height(250.dp),
        contentAlignment = Alignment.CenterStart
    ) {

        AsyncImage(
            model = imageUrl,
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.FillHeight
        )
    }
}
@Composable
fun BookInfo(bookTitle: String = "Reina Roja",
             authorName: String = "Juan Gómez-Jurado",
             bookPrice: Number = 12.5){

    Row(modifier = Modifier
        .width(180.dp),
    ) {
        Column(modifier = Modifier
            .weight(0.7f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = bookTitle)
            Text(text = authorName)
        }

        Text(text = bookPrice.toString() + "€", modifier = Modifier.padding(end=10.dp),
            style = TextStyle(color = Color.DarkGray, fontWeight =FontWeight.Bold)
        )
    }
}

@Composable
fun ShowRectangle() {

    Canvas(modifier = Modifier
        .size(180.dp, 250.dp)) {

        drawRect(
            color = GreenApp
        )
    }


}

@Preview(showBackground = true)
@Composable
fun Greeting2Preview() {
    LibraryAppTheme {
        SearchAppBar()
    }
}

