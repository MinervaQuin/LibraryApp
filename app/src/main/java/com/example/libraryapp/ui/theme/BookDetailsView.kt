package com.example.libraryapp.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.Date


@Composable
fun BookDetailsScreen(
    reviews: List<String> = List(5) { "$it" },
    navController: NavHostController
){
    val offset = remember { mutableStateOf(0f)
    }
    Column (modifier = Modifier.verticalScroll(rememberScrollState())
    ){
        BookInitialInfo()
        BookSinopsis()
        FactSheet()
        ReviewBook("Reina Roja")
        Divider()
        ResumeOfReviews()
        Column {
            for (i in reviews) {
                UserReview()
            }
        }
    }
}



@Composable
fun ResumeOfReviews(
    mediaScore: Number = 4.5,
    rateScore: Int = 4,
    num_opi: Int = 124,

){
    Row (modifier = Modifier.padding(top=20.dp, bottom = 10.dp, start = 15.dp)){
        Box (modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.Center
            ) {
            createCircle()
            Text(text = mediaScore.toString(),
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight(700),
                    color = Color(0xFF342E37),

                    textAlign = TextAlign.Center,
                )
            )
        }
        Column (
            modifier = Modifier
                .weight(0.8f)
                .padding(start = 10.dp),
            verticalArrangement = Arrangement.Bottom
        ){
            RatingBar(currentRating = rateScore)
            Text(text = num_opi.toString() + " opiniones")
        }
    }


}

@Composable
fun createCircle(){
    Canvas(modifier = Modifier
        .size(70.dp,70.dp)) {
        drawCircle(
            color = GreenApp
        )
    }


}

@Composable
fun UserReview(
    userName: String = "Tobias",
    rate: Int = 4,
    reviewDate: Date = Date(2022, 12, 31, 23, 59, 59),
    comment: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum."
){
    Column(modifier = Modifier.padding(10.dp)) {
        Row {
            Icon(
                imageVector = Icons.Filled.Face,
                contentDescription = "user icon",
                modifier = Modifier.padding(8.dp)
            )
            Column(modifier = Modifier
                .weight(1f)
                .padding(top = 5.dp)
                .height(50.dp)
            ) {
                Text(text = userName )
                RatingBar(currentRating = rate)
                Text(text = "")
            }

        }
        Text(text = comment, maxLines = 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(5.dp))
    }


}

@Composable
fun ReviewBook(bookTitle: String){
    var myRating by remember { mutableStateOf(0) }

    Column (
        modifier = Modifier.padding(bottom = 5.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Text(
            text = "Opiniones sobre " + bookTitle,
            modifier = Modifier
                .background(Color(0xFF77CF7C)) // Change Color.Blue to the desired background color
                .padding(8.dp)
                .fillMaxWidth()
        )
        Icon(
            imageVector = Icons.Filled.Face,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(70.dp)
                .padding(top = 8.dp)
        )
        Text(text = "¿Qué te ha parecido?")
        AnimatedRatingBar(
            currentRating = myRating,
            onRatingChanged = { myRating = it }
        )
        Button(
            onClick = { /*TODO*/ }, //Ir a la páginad de hacer una opinión
            colors = ButtonDefaults.buttonColors(containerColor = GreenApp),

            ) {
            Text("Deja tu opinión")
        }


    }



}

@Composable
fun BookInitialInfo(){
    Row(
        modifier = Modifier.padding(top=10.dp, bottom = 10.dp)
    ) {
        LoadBookCover("https://m.media-amazon.com/images/I/41uWfObYYQL._SY445_SX342_.jpg")
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            BookLittleInfo()
            Button(onClick = { /*TODO*/ }) {//Ir a la pagina de la cesta
                Text(text = "Añadir a la cesta")
            }
        }



    }
}

@Composable
fun BookLittleInfo(authorName: String = "Juán Gómez-Jurando",
                   bookTitle: String = "Reina Roja",
                   editorial: String = "DEBOLSILLO",
                   isbn: Number = 9788466335362,
                   bookPrice: Number = 12.5){
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Column(modifier = Modifier
            .weight(0.7f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = bookTitle)
            ClickableText(
                text= AnnotatedString(authorName),
                onClick = {}, //pantalla información autor

                style = TextStyle(color = Color.DarkGray, fontWeight =FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth()

            )
            Text(text = editorial)
            Text(text = isbn.toString())
            RatingBar(currentRating = 4)

            Text(text = bookPrice.toString() + "€", modifier = Modifier.padding(end=10.dp),
                style = TextStyle(color = Color.DarkGray, fontWeight =FontWeight.Bold,
                    fontSize = 20.sp),
                )
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSinopsis(sinopsis: String = "Antonia Scott es una mujer muy especial. Tiene un don que es al mismo tiempo una maldición: una extraordinaria inteligencia. Gracias a ella ha salvado decenas de vidas, pero también lo ha perdido todo. Hoy se parapeta contra el mundo en su piso casi vacío de Lavapiés, del que no piensa volver a salir. Ya no queda nada ahí fuera que le interese lo más mínimo.\n" +
        "\n" +
        "El inspector Jon Gutiérrez está acusado de corrupción, suspendido de empleo y sueldo. Es un buen policía metido en un asunto muy feo, y ya no tiene mucho que perder. Por eso acepta la propuesta de un misterioso desconocido: ir a buscar a Antonia y sacarla de su encierro, conseguir que vuelva a hacer lo que fuera que hiciera antes, y el desconocido le ayudará a limpiar su nombre. Un encargo extraño aunque aparentemente fácil.\n" +
        "\n" +
        "Pero Jon se dará cuenta en seguida de que con Antonia nada es fácil."){
    var expanded by remember { mutableStateOf(false) }

    Column() {
        Text(
            text = "Sinopsis",
            modifier = Modifier
                .background(Color(0xFF77CF7C)) // Change Color.Blue to the desired background color
                .padding(8.dp)
                .fillMaxWidth()
        )
        Text(
            text = sinopsis,
            maxLines = if (expanded) Int.MAX_VALUE else 10,
            overflow = TextOverflow.Ellipsis ,
            modifier = Modifier.padding(8.dp)
        )

    }
    ClickableText(
        text= if (expanded) AnnotatedString("Leer menos") else AnnotatedString("Leer más"),
        onClick = {expanded = !expanded},

        style = TextStyle(color = GreenApp, fontWeight =FontWeight.Bold, textDecoration = TextDecoration.Underline ),
        modifier = Modifier
            .padding(start = 8.dp, bottom = 5.dp)
            .fillMaxWidth()

    )



}


@Composable
fun FactSheet(
    num_pag: Int = 304,
    editorial: String = "DEBOLSILLO",
    idioma: String = "Castellano",
    encuadernacion: String = "Tapa blanda",
    isbn: Number = 9788466335362,
    publicationDate: Number = 2011
){
    Column() {
        Text(
            text = "Detalles del Libro",
            modifier = Modifier
                .background(Color(0xFF77CF7C)) // Change Color.Blue to the desired background color
                .padding(8.dp)
                .fillMaxWidth()
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Número de páginas: ")
                }
                append(num_pag.toString())
            },
            Modifier
                .padding(8.dp)
        )
        Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Editorial: ")
                    }
                    append(editorial)
                },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Idioma: ")
                }
                append(idioma)
            },
            Modifier
                .padding(8.dp)
        )
        Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("Encuadernación: ")
                    }
                    append(encuadernacion.toString())
                },
            Modifier
                .padding(8.dp)
        )
        Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append("ISBN: ")
                    }
                    append(isbn.toString())
                },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Fecha de publicación: ")
                }
                append(publicationDate.toString())
            },
            Modifier
                .padding(8.dp)
        )






    }



}

@Composable
fun RatingBar(
    maxRating: Int = 5,
    currentRating: Int,
    starsColor: Color = Color(0xFFF7D850)
){
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Filled.Star
                              else Icons.Outlined.Star,
                                contentDescription = null,
                tint = if (i <= currentRating) starsColor
                              else Color.Unspecified,

            )
        }
    }
}

@Composable
fun AnimatedRatingBar(
    maxRating: Int = 5,
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    starsColor: Color = Color.Yellow
) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Filled.Star
                else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i <= currentRating) starsColor
                else Color.Unspecified,
                modifier = Modifier
                    .clickable { onRatingChanged(i) }
                    .padding(4.dp)
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun Greeting4Preview() {
    LibraryAppTheme {
        BookInitialInfo()
    }
}

