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
import com.example.libraryapp.R
import com.example.libraryapp.model.Author
import com.example.libraryapp.model.Book
import com.example.libraryapp.theme.Green
import java.lang.Math.floor

@Composable
fun AutorScreen (autor: Author){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    )
    {
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
                text = autor.Biografia,
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
            items(autor.obras.size){
                for (i in 0 until autor.obras.size){
                    BookPreview(autor.obras[i])
                }
            }
        }

    }
}

@Composable
fun BookPreview (obra : Book ){
    Box(modifier = Modifier
        .padding(10.dp)
        .width(180.dp)
        .height(330.dp)
        .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .border(width = 1.dp, color = Color(0xFF000000))
        .clickable {
        }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            Image(
                painter = painterResource(id = R.drawable.image_22),
                contentDescription = "image description",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(102.dp)
                    .height(163.2.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = obra.cover,
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                    )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(2.dp)
                    .background(Color.Black)
            )
            Text(
                text = obra.title,
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                    )
            )
            Text(
                text = obra.author_name,
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                )
            )
            Text(
                text = "${obra.price} €",
                style = TextStyle(
                    fontSize = 8.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF841F0B)
                    )
            )
            Row {
                val rate = obra.score.toDouble()
                val fillStars = floor (rate / 2)
                val halfStars = rate % 2
                val unfilledStars = 5 - (fillStars + halfStars)
                iconpainter(R.drawable.openmoji_star,fillStars.toInt())
                iconpainter(R.drawable.openmoji_half_star,halfStars.toInt())
                iconpainter(R.drawable.openmoji_black_star,unfilledStars.toInt())
            }
        }
    }
}


//Función que dibuja el drawable pasado por parametro n veces
@Composable
fun iconpainter (id : Int, rep: Int){
    repeat (rep) {
        Icon(
            painter = painterResource(id),
            contentDescription = null,
            tint= Color.Unspecified,
            modifier = Modifier
                .size(20.dp)
        )
    }
}
