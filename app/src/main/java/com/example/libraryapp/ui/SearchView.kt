package com.example.libraryapp.ui.theme


import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.example.libraryapp.R
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.theme.LibraryAppTheme
import com.example.libraryapp.viewModel.SearchViewModel
import com.example.libraryapp.viewModel.ShoppingCart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun BookScreen(
    modifier: Modifier = Modifier.padding(10.dp),
    navController: NavHostController,
){
    val searchViewModel : SearchViewModel = hiltViewModel()
    var books by remember { mutableStateOf<List<Book?>>(emptyList()) }
    var stringSearched by remember { mutableStateOf<String>(searchViewModel.getSearchedString()) }


    LaunchedEffect(searchViewModel) {
        try {
            books = searchViewModel.getAllBooks()
        } catch (e: Exception) {
            Log.e("Firestore", "Error en BookScreen", e)
        }
    }
    val navController2 = navController
    Column {
        SearchAppBar(
            searchViewModel = searchViewModel,
            modifyState = { newValue ->
                books = newValue
            },
            updateSearchString = { newSearchString ->
                stringSearched = newSearchString
            },
            navController = navController,
            searchString = stringSearched
        )
        Text(text = if (stringSearched.length != 0) "Resultados para: " + stringSearched else "",
            fontWeight = FontWeight.Bold,
            modifier = if (stringSearched.length != 0) Modifier.padding(start = 6.dp, top=8.dp, bottom = 8.dp) else Modifier.padding(start = 0.dp))
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 180.dp),
        ){
            items(items = books) {
                BookItem(it, modifier = Modifier, navController, searchViewModel)

                if (it != null) {
                    Log.d("Firestore", it.title)
                }
            }
        }
        Spacer(modifier = Modifier.height(55.dp))
    }

}




@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchAppBar(
    searchViewModel: SearchViewModel,
    modifyState: (List<Book?>) -> Unit,
    updateSearchString: (String) -> Unit,
    navController: NavHostController,
    searchString: String
)
{
    var searchString by remember { mutableStateOf(searchString) }
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
                coroutineScope.launch {
                    try {
                        // Use withContext to switch to the IO dispatcher if needed
                        withContext(Dispatchers.IO) {
                            if (searchString.length == 0) {
                                modifyState(searchViewModel.getAllBooks())
                            } else {
                                modifyState(searchViewModel.getBooksStringMatch(searchString))
                            }
                            updateSearchString(searchString)
                        }
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error in SearchBar", e)
                    } finally {
                        softwareKeyboardController?.hide()
                    }
                }
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
                coroutineScope.launch {
                    try {
                        // Use withContext to switch to the IO dispatcher if needed
                        withContext(Dispatchers.IO) {
                            if (searchString.length == 0) {
                                modifyState(searchViewModel.getAllBooks())
                            } else {
                                modifyState(searchViewModel.getBooksStringMatch(searchString))
                            }
                            updateSearchString(searchString)
                        }
                    } catch (e: Exception) {
                        Log.e("Firestore", "Error in SearchBar", e)
                    } finally {
                        softwareKeyboardController?.hide()
                    }
                }
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



@Composable
fun BookItem(book: Book?, modifier: Modifier, navController: NavHostController, searchViewModel: SearchViewModel) {
    Column(
        modifier = Modifier
            .padding(bottom = 8.dp)
            .clickable {
                if (book != null) {
//                    ShoppingCart.setBookSelected(book)
                    searchViewModel.libraryAppState.setBookId(book.ref)
                    searchViewModel.libraryAppState.setBook(book)
                }
                navController.navigate("BookDetailsView")
            } //BookDetail(
        ,verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        if (book != null) {
            LoadBookCover(imageUrl = book.cover)
        }
        BookInfo(book)


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
fun BookInfo(book: Book?){

    Row(modifier = Modifier
        .width(180.dp)
        .padding(start = 5.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(0.7f),
            verticalArrangement = Arrangement.Center,
        ) {
            book?.let { Text(text = it.title) }
            book?.let { Text(text = it.author_name) }
        }

        Text(text = book?.price.toString() + "€", modifier = Modifier.padding(end=10.dp),
            style = TextStyle(color = Color.DarkGray, fontWeight =FontWeight.Bold)
        )
    }
}



@Preview(showBackground = true)
@Composable
fun Greeting2Preview() {
    LibraryAppTheme {
//        SearchAppBar()
    }
}

