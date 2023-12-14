package com.example.libraryapp.ui

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Order
import com.example.libraryapp.theme.green
import com.example.libraryapp.ui.theme.GreenApp
import com.example.libraryapp.viewModel.comprasViewModel

@Composable
fun comprasView(navController: NavController, viewModel : comprasViewModel){

    Column (
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .fillMaxSize(),
        ){
        Text(
            text = "Mis Compras",
            style = TextStyle(
                fontSize = 43.sp,
                fontWeight = FontWeight(700),
                color = GreenApp,
                textAlign = TextAlign.Center,
            ) ,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(20.dp)
        )
        for (i in 0 until viewModel.orders.size){
            orders(viewModel.orders[i],viewModel)
        }
    }
}

@Composable
fun orders(order: Order, viewModel : comprasViewModel){
    var open by remember { mutableStateOf(false) }

    Column {
        Box(
            modifier= Modifier.fillMaxWidth()
                .border(width = 1.dp, color = Color(0xFF000000))
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
                    .padding(10.dp)
                    .clickable {
                        open = !open
                    }
            )
            {
                AsyncImage(
                    model = "https://cdn-icons-png.flaticon.com/512/4645/4645316.png",
                    contentDescription = null,
                    modifier = Modifier
                        .width(75.dp)
                        .height(75.dp),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Orden número:${order!!.orderId}",
                        style = TextStyle(
                            fontSize = 12.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xFF010101),
                        )
                    )
                    Text(
                        text = order!!.orderDate.toString(),
                        style = TextStyle(
                            fontSize = 10.sp,
                            fontWeight = FontWeight(400),
                            color = Color(0xCC010101),
                        )
                    )
                    Text(
                        text = order!!.state,
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight(400),
                            color = GreenApp,
                        )
                    )
                }
            }
            Text(
                text = "${order.total.toString()}€",
                style = TextStyle(
                    fontSize = 15.sp,
                    fontWeight = FontWeight(400),
                    color = Color.Red,
                ),
                modifier = Modifier
                    .padding(20.dp)
                    .align(alignment = Alignment.TopEnd)
            )
        }

            Column {
                if (open){
                    Log.d("","Entro")
                    val books = viewModel.MapBook
                    val book = books[order]
                    for (i in 0 until book!!.size){
                        bookspreview(book[i],order.booksOrdered.get(book[i]!!.ref))
                    }
                }
            }
    }

}

@Composable
fun bookspreview(book: Book?, cantidad: Int?){
    Box(modifier= Modifier
        .fillMaxWidth()
        .padding(end= 20.dp)
        .border(width = 1.dp, color = Color(0xFF000000))
    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(95.dp)
                .padding(10.dp)
        )
        {
            AsyncImage(
                model = book!!.cover,
                contentDescription = null,
                modifier = Modifier
                    .width(75.dp)
                    .height(75.dp),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier
                    .padding(start = 10.dp)
            ) {
                Text(
                    text = book.title,
                    style = TextStyle(
                        fontSize = 15.sp,
                        fontWeight =  FontWeight.Bold,
                        color = Color(0xFF010101),
                    )
                )
                Text(
                    text = book.author_name,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xCC010101),
                    )
                )
                Text(
                    text = "${book.price.toString()}€",
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color.Red,
                    )
                )
            }
        }
        Text(
            text = cantidad.toString(),
            style = TextStyle(
                fontSize = 12.sp,
                fontWeight = FontWeight(400),
                color = Color(0xCC010101),
            ),
            modifier = Modifier
                .padding(25.dp)
                .align(alignment = Alignment.CenterEnd)
        )
    }

}