package com.example.libraryapp.ui.theme

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.libraryapp.model.BookDetailsUiState
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.model.resources.Review
import com.example.libraryapp.theme.LibraryAppTheme
import com.example.libraryapp.viewModel.BookDetailsViewModel
import com.example.libraryapp.viewModel.ShoppingCart
import java.time.format.DateTimeFormatter


@Composable
fun BookDetailsScreen(

    navController: NavHostController,
    bookDetailsViewModel: BookDetailsViewModel = hiltViewModel(),
){
    val bookUiState by bookDetailsViewModel.bookUiState.collectAsState()
    val book = bookDetailsViewModel.libraryAppState.getBook()


    var reviews by remember { mutableStateOf<List<Review?>>(emptyList()) }


    LaunchedEffect(bookDetailsViewModel.refreshReviews) {
        bookDetailsViewModel.refreshReviews.collect { newRefreshReviewsValue ->

            try {
                reviews =
                    bookDetailsViewModel.getReviews(bookDetailsViewModel.libraryAppState.getBookId())
                Log.d("firebase", "SE HA ACTUALIZADO LAS REVIEWS")
            } catch (e: Exception) {
                Log.e("firebase", "Error en BookScreen", e)
            }
        }
    }

    Column (modifier = Modifier.verticalScroll(rememberScrollState())
    ){
//
        BookInitialInfo(book!!, navController)
        BookSinopsis(book!!.sinopsis)
        FactSheet(book!!)
        ReviewBook(book!!.title, bookDetailsViewModel, bookUiState)
        Divider()
        ResumeOfReviews(num_opi = reviews.size, rateScore = book!!.score.toDouble())
        Column (
            modifier = Modifier
                .height(400.dp)
                .verticalScroll(rememberScrollState())
        ) {
            for (review in reviews) {
                if (review != null) {
                    UserReview(review, bookDetailsViewModel = bookDetailsViewModel)
                }
            }
        }
    }
}



@Composable
fun ResumeOfReviews(
    rateScore: Double,
    num_opi: Int,
){
    Row (modifier = Modifier.padding(top=20.dp, bottom = 10.dp, start = 15.dp)){
        Box (modifier = Modifier.weight(0.2f),
            contentAlignment = Alignment.Center
            ) {
            createCircle()
            Text(text = rateScore.toString(),
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
    review: Review,
    bookDetailsViewModel: BookDetailsViewModel
){


    var expanded by remember {mutableStateOf(false)}
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yy")
    val formattedDate = bookDetailsViewModel.getFormattedTimeAgo(review.date)

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
                    Text(text = review.userName )
                    Text(
                        text = formattedDate,
                        modifier = Modifier.padding(start = 8.dp),
                        style = TextStyle(color = Color.Gray),
                    )
                }
                val intValue = review.score
                RatingBar(currentRating = intValue)
            }

        }
        Text(text = review.description,
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
            colors = ButtonDefaults.buttonColors(containerColor = GreenApp)
            ) {
            var userReview: Review? = bookDetailsViewModel.getReviewFromUser()
            if (userReview != null) {
                Text("Edita tu opinión")
            }else{
                Text("Deja tu opinión")
            }
            AddReview(
                showDialog = bookUiState.showDialog,
                updateRating = { bookDetailsViewModel.updateRating( it ) },
                bookUiState= bookUiState,
                bookDetailsViewModel = bookDetailsViewModel,
                bookTitle = bookTitle,
                userReview = userReview ?: null
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddReview(
    bookTitle: String,
    showDialog: Boolean,
    userReview: Review?,
    updateRating: (Int) -> Unit,
    bookUiState: BookDetailsUiState,
    bookDetailsViewModel: BookDetailsViewModel,

    )
{

    var comentarioTexto by remember { mutableStateOf(userReview?.description ?: "") }

    LaunchedEffect(userReview) {
        comentarioTexto = userReview?.description ?: ""
    }


    if (showDialog) {

        Dialog(
            onDismissRequest = {
                bookDetailsViewModel.closeDialog()
            }
        ) {
            BackHandler {
                bookDetailsViewModel.closeDialog()
            }
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp)
                    .padding(16.dp)
                    .shadow(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White,
                ),
                shape = RoundedCornerShape(10.dp),

            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                    ,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        "¿Has leído " + bookTitle + "?",
                        style = TextStyle(
                            fontSize = 25.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center),
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
                            label = {
                                Text(if (!comentarioTexto.isEmpty()) "Ingresa tu comentario" else comentarioTexto)

                            },
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth()
                                .height(160.dp),
                            keyboardOptions = KeyboardOptions.Default.copy(
                                imeAction = ImeAction.Send
                            ),
                            keyboardActions = KeyboardActions(
                                onSend = {
                                    sendComment(comentarioTexto, bookUiState.reviewScore, bookDetailsViewModel)
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
                            sendComment(comentarioTexto, bookUiState.reviewScore, bookDetailsViewModel)
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

fun sendComment(comentarioTexto: String, reviewScore: Int, bookDetailsViewModel: BookDetailsViewModel) {
    bookDetailsViewModel.sendReview( comentarioTexto, reviewScore )
}


@Composable
fun BookInitialInfo(book: Book, navController: NavHostController) {
    val context = LocalContext.current
    Row(
        modifier = Modifier.padding(top=10.dp, bottom = 10.dp)
    ) {
        LoadBookCover(book.cover)
        Column(
            horizontalAlignment = Alignment.Start,
            modifier = Modifier.padding(start = 10.dp)
        ) {
            BookLittleInfo(book, navController)
            Button(onClick = { ShoppingCart.getViewModelInstance().addBookToCart(book)
                Toast.makeText(context, "Se ha añadido el libro a tu cesta", Toast.LENGTH_SHORT).show()}) {//Ir a la pagina de la cesta
                Text(text = "Añadir a la cesta")
            }
        }



    }
}


@Composable
fun BookLittleInfo(book: Book,navController: NavController){

    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.7f),
            verticalArrangement = Arrangement.Center,
        ) {
            Text(text = book.title)
            ClickableText(
                text= AnnotatedString(book.author_name),
                onClick = {
                    ShoppingCart.setautorId(book.author_id.toString())
                    navController.navigate("AuthorDestination")
                }, //pantalla información autor

                style = TextStyle(color = Color.DarkGray, fontWeight =FontWeight.Bold),
                modifier = Modifier
                    .padding(bottom = 5.dp)
                    .fillMaxWidth()

            )
            Text(text = book.editorial)
            Text(text = book.isbn.toString())
            RatingBar(currentRating = 4.0)

            Text(
                text = book.price.toString() + "€", modifier = Modifier.padding(end = 10.dp),
                style = TextStyle(
                    color = Color.DarkGray, fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                ),
            )
        }


    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookSinopsis( sinopsis: String ){
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
fun FactSheet( book: Book ){
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
                append(book.num_pag.toString())
            },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Editorial: ")
                }
                append(book.editorial)
            },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Idioma: ")
                }
                append(book.language)
            },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Encuadernación: ")
                }
                append(book.encuadernacion)
            },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("ISBN: ")
                }
                append(book.isbn.toString())
            },
            Modifier
                .padding(8.dp)
        )
        Text(
            buildAnnotatedString {
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append("Fecha de publicación: ")
                }
                append(book.publicationDate)
            },
            Modifier
                .padding(8.dp)
        )






    }

}

@Composable
fun RatingBar(
    maxRating: Double = 5.0,
    currentRating: Double,
    starsColor: Color = Color(0xFFF7D850)
){
    Row {
        for (i in 1..maxRating.toInt()) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Filled.Star
                else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i <= currentRating) starsColor
                else Color.LightGray,

                )
        }
    }
}

@Composable
fun AnimatedRatingBar(
    maxRating: Int = 5,
    currentRating: Int,
    onRatingChanged: (Int) -> Unit,
    starsColor: Color = Color(0xFFF7D850)
) {
    Row {
        for (i in 1..maxRating) {
            Icon(
                imageVector = if (i <= currentRating) Icons.Filled.Star
                else Icons.Outlined.Star,
                contentDescription = null,
                tint = if (i <= currentRating) starsColor
                else Color.LightGray,
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
//        BookInitialInfo(book)
    }
}

