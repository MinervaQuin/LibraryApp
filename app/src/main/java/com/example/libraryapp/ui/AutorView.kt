package com.example.libraryapp.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.viewModel.AuthorViewModel
import com.example.libraryapp.viewModel.CategoryViewModel
import java.lang.Math.floor

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.ui.BookPreview

@Composable
fun AutorScreen (navController: NavController, ViewModel: AuthorViewModel){
    val autor = ViewModel.Autor
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    )
    {
        Spacer(modifier = Modifier.height(55.dp))
        Text(
            text = autor.name,
            style = TextStyle(
                fontSize = 36.sp,
                fontWeight = FontWeight(700),
                color = Color(0xFF000000),
                textAlign = TextAlign.Center,
            ),
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(30.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.image_21),
            contentDescription = "image description",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier
                .shadow(
                    elevation = 4.dp,
                    spotColor = Color(0x40000000),
                    ambientColor = Color(0x40000000)
                )
                .width(194.dp)
                .height(259.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )

        //biografia
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(30.dp)
                .border(
                    width = 1.dp,
                    color = Color(0xFF000000),
                    shape = RoundedCornerShape(size = 3.dp)
                )
                .width(325.dp)
                .height(203.dp),
            verticalArrangement = Arrangement.Center,
        ){
            Text(
                text = "Biografia",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(5.dp)
            )
            Text(
                text = autor.biography,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                    textAlign = TextAlign.Justify,
                ),
                modifier = Modifier
                    .padding(10.dp)
                    .verticalScroll(rememberScrollState()),
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )
            Text(
                text = "Libros de ${autor.name}",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                ),
                modifier = Modifier
                    .align(alignment = Alignment.Start)
                    .padding(5.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )
        }
        LazyRow(){
            items(1){
                for (i in 0 until autor.works.size){
                    BookPreview(autor.works[i])
                }
            }
        }
        Spacer(modifier = Modifier.height(55.dp))

    }
}

