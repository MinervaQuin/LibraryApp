package com.example.libraryapp.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.ui.theme.RatingBar
import com.example.libraryapp.viewModel.CartViewModel
import com.example.libraryapp.viewModel.ShoppingCart
import kotlinx.coroutines.launch

@Composable
fun BookPreview (obra: Book, navController: NavController){
    val cartViewModel: CartViewModel = ShoppingCart.getViewModelInstance()
    Box(modifier = Modifier
        .padding(10.dp)
        .width(180.dp)
        .height(330.dp)
        .shadow(elevation = 4.dp, spotColor = Color(0x40000000), ambientColor = Color(0x40000000))
        .border(width = 1.dp, color = Color(0xFF000000))
        .clickable {
            ShoppingCart.setBookSelected(obra)
            navController.navigate("BookDetailsView")
        }
    )
    {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ){
            AsyncImage(
                model = obra!!.cover,
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
                text = obra.title,
                style = TextStyle(
                    fontSize = 14.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF000000),
                )
            )
            Text(
                text = obra.author_name,
                style = TextStyle(
                    fontSize = 10.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF000000),
                )
            )
            Text(
                text = "${obra.price} €",
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight(400),
                    color = Color(0xFF841F0B)
                )
            )
            Row {
                val rate = obra.score.toDouble()
                val fillStars = Math.floor(rate / 2)
                RatingBar (currentRating = fillStars)

/*                val rate = obra.score.toDouble()
                val fillStars = Math.floor (rate / 2)
                val halfStars = rate % 2
                val unfilledStars = 5 - (fillStars + halfStars)
                iconpainter(R.drawable.openmoji_star,fillStars.toInt())
                iconpainter(R.drawable.openmoji_half_star,halfStars.toInt())
                iconpainter(R.drawable.openmoji_black_star,unfilledStars.toInt())*/
            }
        }
        Button(
            onClick = { cartViewModel.addBookToCart(obra) },
            colors= ButtonDefaults.buttonColors(
                containerColor=Color(0xBFFC5F5F)
            ),
            modifier= Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
        ){
            Text(text = "Añadir al carrito")
        }
    }
}

@Composable
fun BookPreviewWide (obra : Book, navController: NavController) {
    val cartViewModel: CartViewModel = ShoppingCart.getViewModelInstance()
    Box(modifier = Modifier
        .padding(10.dp)
        .fillMaxWidth()
        .height(275.dp)
        .shadow(
            elevation = 4.dp,
            spotColor = Color(0x40000000),
            ambientColor = Color(0x40000000)
        )
        .border(width = 1.dp, color = Color(0xFF000000))
        .clickable {
            ShoppingCart.setBookSelected(obra)
            navController.navigate("BookDetailsView")
        }
    ) {
        Row {
            AsyncImage(
                model = obra!!.cover,
                contentDescription = "image description",
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .width(120.dp)
                    .height(195.dp)
                    .padding(15.dp)
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                Text(
                    text = obra.title,
                    style = TextStyle(
                        fontSize = 14.sp,
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
                    text = obra.author_name,
                    style = TextStyle(
                        fontSize = 12.sp,
                        fontWeight = FontWeight(400),
                        color = Color(0xFF000000),

                        )
                )
                Row {
                    val rate = obra.score.toDouble()
                    val fillStars = Math.floor(rate / 2)
                    RatingBar (currentRating = fillStars)
/*                    val halfStars = rate % 2
                    val unfilledStars = 5 - (fillStars + halfStars)
                    iconpainter(R.drawable.openmoji_star, fillStars.toInt())
                    iconpainter(R.drawable.openmoji_half_star, halfStars.toInt())
                    iconpainter(R.drawable.openmoji_black_star, unfilledStars.toInt())*/
                }
            }

        }
        Column(
            modifier= Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ){
            Text(
                text = "${obra.price} €",
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF841F0B),
                ),
                modifier = Modifier
                    .align(Alignment.End)
                    .padding(5.dp)
            )
            Button(
                onClick = { cartViewModel.addBookToCart(obra) },
                colors= ButtonDefaults.buttonColors(
                containerColor=Color(0xBFFC5F5F)
            ),
                modifier= Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Text(text = "Añadir a la cesta")
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

/*@Preview(showBackground = true)
@Composable
fun Previewview() {
    BookPreviewWide(Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 3, "Tapa Dura", 20.0))
}*/