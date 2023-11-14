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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.libraryapp.model.BookDetailsUiState
import com.example.libraryapp.theme.LibraryAppTheme
import com.example.libraryapp.viewModel.BookDetailsViewModel
import java.time.LocalDateTime
import java.time.Month
import java.time.format.DateTimeFormatter


@Composable
fun BookDetailsScreen(
    reviews: List<String> = List(5) { "$it" },
    dates: List<LocalDateTime> = listOf(
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 12, 30, 0),
        LocalDateTime.of(2023, Month.NOVEMBER, 13, 12, 30, 0),
        LocalDateTime.of(2023, Month.NOVEMBER, 5, 12, 30, 0),
        LocalDateTime.of(2023, Month.NOVEMBER, 14, 10, 40, 0),
        LocalDateTime.of(2023, Month.JULY, 14, 12, 30, 0)
    ),
    navController: NavHostController,
    bookDetailsViewModel: BookDetailsViewModel = viewModel()
){
    val offset = remember { mutableStateOf(0f) }
    val bookUiState by bookDetailsViewModel.bookUiState.collectAsState()

    Column (modifier = Modifier.verticalScroll(rememberScrollState())
    ){

        BookInitialInfo()
        BookSinopsis()
        FactSheet()
        ReviewBook("Reina Roja", bookDetailsViewModel, bookUiState)
        Divider()
        ResumeOfReviews()
        Column {
            for (i in dates) {
                UserReview(reviewDate = i, bookDetailsViewModel = bookDetailsViewModel)
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
    reviewDate: LocalDateTime,
    comment: String = "Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.",
    bookDetailsViewModel: BookDetailsViewModel
){
    var expanded by remember {mutableStateOf(false)}
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    val formattedDate = bookDetailsViewModel.getFormattedTimeAgo(reviewDate)

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
                Row {
                    Text(text = userName )
                    Text(text = formattedDate,
                        modifier = Modifier.padding(start = 8.dp),
                        style = TextStyle(color = Color.Gray),
                        )
                }
                RatingBar(currentRating = rate)
            }

        }
        Text(text = comment,
            maxLines = if (expanded) Int.MAX_VALUE else 3,
            overflow = TextOverflow.Ellipsis,
            textAlign = TextAlign.Justify,
            modifier = Modifier.padding(5.dp))

        ClickableText(
            text= if (expanded) AnnotatedString("Leer menos") else AnnotatedString("Leer más"),
            onClick = {expanded = !expanded},

            style = TextStyle(color = GreenApp, fontWeight =FontWeight.Bold, textDecoration = TextDecoration.Underline ),
            modifier = Modifier
                .padding(start = 8.dp, bottom = 5.dp)
                .fillMaxWidth()

        )
    }
}

@Composable
fun ReviewBook(
    bookTitle: String,
    bookDetailsViewModel: BookDetailsViewModel,
    bookUiState: BookDetailsUiState
){
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
            currentRating = bookUiState.reviewScore,
            onRatingChanged = { bookDetailsViewModel.updateRating(it) }
        )
        Button(
            onClick = { bookDetailsViewModel.showDialog(true) }, //Ir a la páginad de hacer una opinión
            colors = ButtonDefaults.buttonColors(containerColor = GreenApp),

            ) {
            Text("Deja tu opinión")
            AddReview(showDialog = bookUiState.showDialog,
                sendComment = { bookDetailsViewModel.sendReview( it )  },
                updateRating = { bookDetailsViewModel.updateRating( it ) },
                bookUiState= bookUiState,
                bookDetailsViewModel = bookDetailsViewModel,
            )
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReview(
    bookTitle: String = "Reina Roja",
    showDialog: Boolean,
    sendComment: (String) -> Unit,
    updateRating: (Int) -> Unit,
    bookUiState: BookDetailsUiState,
    bookDetailsViewModel: BookDetailsViewModel,

    )
{
    var comentarioTexto by remember { mutableStateOf("") }
    // Dialog composable
    if (showDialog) {
        Dialog(
            onDismissRequest = {}
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)

                    .shadow(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                shape = RoundedCornerShape(16.dp),

                ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 10.dp)
                    //.background(color = Color.White)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "¿Has leído " + bookTitle + "?",
                        style = TextStyle(fontSize = 25.sp, fontWeight = FontWeight.Bold,),
                        color = Color.Black
                    )
                    Text(
                        "Deja tu opinión para ayudar a otros lectores",
                        style = TextStyle(
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                    )
                    AnimatedRatingBar(
                        currentRating = bookUiState.reviewScore,
                        onRatingChanged = { bookDetailsViewModel.updateRating(it)  }) //conectar con la pantalla anterior
                    SelectionContainer {
                        OutlinedTextField(
                            value = comentarioTexto,
                            onValueChange = { comentarioTexto = it },
                            label = { Text("Ingresa tu comentario") },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(160.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    sendComment(comentarioTexto)
                                    comentarioTexto = ""
                                }
                            ),
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                unfocusedBorderColor = GreenApp,
                                focusedBorderColor = GreenApp,
                                cursorColor = GreenApp
                            )
                        )
                    }
                    Button(
                        onClick = {
                            sendComment(comentarioTexto)
                            comentarioTexto = ""
                        },
                        colors = ButtonDefaults.buttonColors(GreenApp)
                    ) {
                        Text("Enviar") //conectar con la pantalla
                    }

                }
            }
        }
    }
}


@Composable
fun BookInitialInfo() {
    Row(
        modifier = Modifier.padding(top=10.dp, bottom = 10.dp)
    ) {
        LoadBookCover("https://m.media-amazon.com/images/I/41uWfObYYQL._SY445_SX342_.jpg")
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            BookLittleInfo()
            Button(onClick = { /**/ }) {//Ir a la pagina de la cesta
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

