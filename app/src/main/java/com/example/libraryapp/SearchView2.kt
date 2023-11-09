package com.example.libraryapp


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.material.icons.outlined.Info
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.libraryapp.ui.theme.LibraryAppTheme
import com.example.libraryapp.ui.theme.GreenApp



@Composable
fun ListOfBooks(
    modifier: Modifier = Modifier.padding(10.dp),
    names: List<String> = List(1000) { "$it" }
){
    Column {
        SearchAppBar()
        Text(text = "Resultados para: Reina Roja",
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 6.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
            modifier = Modifier.padding(bottom = 10.dp, top = 10.dp)
        ){
            items(items = names) {
                BookItem(modifier = Modifier)
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

            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.upc_scan),
                    //imageVector = Icons.Outlined.Info,
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
fun BookItem(modifier: Modifier) {
    Column(
        modifier = Modifier
        ,verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        ShowRectangle()
        BookInfo()



    }
}

@Composable
fun BookInfo(){
    Row(modifier = Modifier
        .width(180.dp),
    ) {
        Column(modifier = Modifier
            .weight(0.7f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = "Reina Roja")
            Text(text = "Juan Gómez-Jurado")
        }

        Text(text = "12,5€")
    }
}

@Composable
fun ShowRectangle() {

    Canvas(modifier = Modifier
        .size(180.dp, 250.dp)) {

        drawRect(
            color = Color.Red
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

@Preview(showBackground = true)
@Composable
fun Greeting3Preview() {
    LibraryAppTheme {
        ListOfBooks()
    }
}