package com.example.libraryapp

import com.example.libraryapp.viewModel.CategoryViewModel
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.firebaseAuth.FirestoreRepositoryImpl
import com.example.libraryapp.model.firebaseAuth.GoogleAuthUiClient
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import com.example.libraryapp.theme.LibraryAppTheme
import com.example.libraryapp.ui.Cart
import com.example.libraryapp.ui.CategoryView
import com.example.libraryapp.ui.HomeView
import com.example.libraryapp.ui.LoginView
import com.example.libraryapp.ui.MapScreen
import com.example.libraryapp.ui.ProfileScreen
import com.example.libraryapp.ui.signUpView
import com.example.libraryapp.ui.theme.BookDetailsScreen
import com.example.libraryapp.ui.theme.BookScreen
import com.example.libraryapp.view.AutorScreen
import com.example.libraryapp.viewModel.AuthorViewModel
import com.example.libraryapp.viewModel.CartViewModel
import com.example.libraryapp.viewModel.SearchViewModel
import com.example.libraryapp.viewModel.ShoppingCart
import com.example.libraryapp.viewModel.homeViewModel
import com.example.libraryapp.viewModel.loginViewModel
import com.example.libraryapp.viewModel.profileViewModel
import com.example.libraryapp.viewModel.topBarViewModel
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import com.google.zxing.integration.android.IntentIntegrator
//import com.google.zxing.integration.android.IntentIntegrator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }


    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ShoppingCart.init()
        setContent {
            val navController = rememberNavController()
            val homeViewModel : homeViewModel = hiltViewModel()
            val topBarViewModel: topBarViewModel = hiltViewModel()
            NavHost(navController = navController, startDestination = "SearchScreen"){

                navigation(
                    startDestination = "login",
                    route = "firstScreens"
                ){
                    composable("login") {

                        val viewModel = viewModel<loginViewModel>()
                        val state by viewModel.state.collectAsStateWithLifecycle()

                        LaunchedEffect(key1 = Unit){
                            if(googleAuthUiClient.getSignedInUser() != null){
                                navController.navigate("secondScreens")
                            }
                        }

                        val launcher = rememberLauncherForActivityResult(
                            contract = ActivityResultContracts.StartIntentSenderForResult(),
                            onResult = {result ->
                                if(result.resultCode == RESULT_OK) {
                                    lifecycleScope.launch {
                                        val signInResult = googleAuthUiClient.signInWithIntent(
                                            intent = result.data ?: return@launch
                                        )
                                        viewModel.onSignInResult(signInResult)
                                    }
                                }
                            }
                        )

                        LaunchedEffect(key1 = state.isSignInSuccessful) {
                            if(state.isSignInSuccessful) {
                                Toast.makeText(
                                    applicationContext,
                                    "Sesión Iniciada",
                                    Toast.LENGTH_LONG
                                ).show()

                                navController.navigate("secondScreens")
                                viewModel.resetState()
                            }
                        }

                        LoginView(
                            navController = navController,
                            state = state,
                            onSignInClick = {
                                lifecycleScope.launch {
                                    val signInIntentSender = googleAuthUiClient.signIn()
                                    launcher.launch(
                                        IntentSenderRequest.Builder(
                                            signInIntentSender ?: return@launch
                                        ).build()
                                    )
                                }
                            }

                        )
                    }

                    composable("signUp") { signUpView(navController = navController)}
                }

                navigation(
                    startDestination = "homePage",
                    route = "secondScreens"
                ){
                    composable("homePage"){
                        Scaffold(
                            bottomBar = { BottomBar(navController = navController) },
                            topBar = { TopBar(navController = navController, topBarViewModel) },
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ){
                                    HomeView(
                                        userData = googleAuthUiClient.getSignedInUser(),
                                        onSignOut = {
                                            lifecycleScope.launch {
                                                googleAuthUiClient.signOut()
                                                Toast.makeText(
                                                    applicationContext,
                                                    "Sesión Cerrada",
                                                    Toast.LENGTH_LONG
                                                ).show()
                                                navController.popBackStack()
                                            }
                                        },
                                        viewModel = homeViewModel,
                                        navController = navController
                                    )
                                }
                            }
                        )
                    }

                    composable("cartDestination") {
                        // Contenido de la pantalla del carrito
                        val cartViewModel : CartViewModel = ShoppingCart.getViewModelInstance()
                        Scaffold(
                            bottomBar = { BottomBar(navController = navController) },
                            topBar = { TopBar(navController = navController, topBarViewModel) },
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ) {
                                    // Resto de tu contenido aquí
                                    // Puedes agregar tus componentes, como Cart(navController, viewModel), dentro de este Column
                                    Cart(navController, cartViewModel)
                                }
                            }
                        )
                    }
                    composable("Category") {
                        val viewModel : CategoryViewModel = hiltViewModel()
                        // Contenido de la pantalla del carrito
                        Scaffold(
                            bottomBar = { BottomBar(navController = navController) },
                            topBar = { TopBar(navController = navController, topBarViewModel)},
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ) {
                                    CategoryView(navController,viewModel,ShoppingCart.getSelectedCategory())
                                }
                            }
                        )
                    }

                    composable("maps"){
                        Scaffold(
                            bottomBar = { BottomBar(navController = navController) },
                            topBar = { TopBar(navController = navController, topBarViewModel)},
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ) {
                                    MapScreen()
                                }
                            }
                        )
                    }

                    composable("profile"){
                        val viewModel : profileViewModel = hiltViewModel()
                        Scaffold(
                            bottomBar = { BottomBar(navController = navController) },
                            topBar = { TopBar(navController = navController, topBarViewModel)},
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ) {
                                    ProfileScreen(viewModel, navController = navController)
                                }
                            }
                        )
                    }



                //composable("signUp") { signUpView(navController = navController)}

                    composable("bookDetailsView"){
                        Scaffold(
                            bottomBar = { BottomBar(navController = navController) },
                            topBar = { TopBar(navController = navController, topBarViewModel)},
                            content = { paddingValues ->
                                Column(
                                    modifier = Modifier
                                        .padding(paddingValues)
                                        .fillMaxSize()
                                ) {
                                    BookDetailsScreen(navController = navController)
                                }
                            }
                        )
                    }
                    composable("addReviewView"){
    //                    AddReview(navController= navController)
                    }
                    composable("cartView"){
    //                    CartScreen()
                    }

                }

                //fuera del grafo
                composable("AuthorDestination") {
                    val viewModel : AuthorViewModel = hiltViewModel()
                    // Contenido de la pantalla del carrito
                    Scaffold(
                        bottomBar = { BottomBar(navController = navController) },
                        topBar = { TopBar(navController = navController, topBarViewModel)},
                        content = { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize()
                            ) {
                                AutorScreen(navController, viewModel, ShoppingCart.getautorId())
                            }
                        }
                    )
                }
                composable("SearchScreen"){
                    Scaffold(
                        bottomBar = { BottomBar(navController = navController) },
                        topBar = { TopBar(navController = navController, topBarViewModel)},
                        content = { paddingValues ->
                            Column(
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .fillMaxSize()
                            ) {
                                BookScreen(navController = navController)
                            }
                        }
                    )
                }
            }
        }
    }
    val searchViewModel: SearchViewModel by viewModels()
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        var result = IntentIntegrator.parseActivityResult(requestCode,resultCode,data)
        if (result != null){
            if(result.contents == null) {
                Toast.makeText(this,"Cancelado", Toast.LENGTH_SHORT).show()
            }
            else{
                lifecycleScope.launch{
                    searchViewModel.handleScanResult(result.contents)
                }
                searchViewModel.isFallo.observe(this, Observer { isFallo ->
                    if (isFallo) {
                        Toast.makeText(this,"No se ha encontrado el libro", Toast.LENGTH_SHORT).show()
                    }
                })

            }

        }else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
@Composable
fun Greeting() {
    val db = FirebaseFirestore.getInstance()
    Column {
        Button(onClick = fetchFromDB(db)) {
            Text(text = "hols")
        }

        val query: Query = db.collection("authors")
            .whereArrayContains("name","Arturo Pérez-Reverte")

        Button(onClick = getInfo(db, query)) {
            Text(text = "query compuesta")
        }
        Button(onClick = { composteQuery("Miguel") }) {
            Text(text = "query super compuesta")
        }
    }


}

fun fetchFromDB(db: FirebaseFirestore): () -> Unit {
    return {
        db.collection("authors")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.i("ad","BOOK")
                    Log.i("firebase", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }

    }
}

fun getInfo(db: FirebaseFirestore, query: Query, ): () -> Unit {
    return{
        query.get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.i("firebase", "${document.id} => ${document.data}")
                }
            }
            .addOnFailureListener { exception ->
                Log.w(ContentValues.TAG, "Error getting documents.", exception)
            }
    }
}

fun composteQuery(authorName: String){
    val db = FirebaseFirestore.getInstance()

// Define el nombre del autor que deseas consultar
    val authorName = authorName

// Realiza una consulta en la colección "authors" para obtener el ID del autor
    val authorQuery: Task<QuerySnapshot> = db.collection("authors")
        .whereArrayContains("name", authorName)
        .get()

// Realiza una consulta en la colección "books" utilizando el ID del autor obtenido en la consulta anterior
    val booksQuery: Task<QuerySnapshot> = authorQuery.continueWithTask { authorQueryResult ->
        val authorDocs = authorQueryResult.result?.documents ?: emptyList()
        val authorDoc = authorDocs.firstOrNull()

        if (authorDoc != null) {
            val authorId = authorDoc.get("id")
            return@continueWithTask db.collection("books")
                .whereEqualTo("author_id", authorId)
                .get()
        } else {
            Log.i("firebase", "NO HAY LIBROS")

            null
        }
    }

// Combina los resultados de ambas consultas
    Tasks.whenAll(booksQuery, authorQuery).addOnSuccessListener {
        val booksResult = booksQuery.result?.documents ?: emptyList()
        val authorResult = authorQuery.result?.documents ?: emptyList()

        // Ahora tienes los documentos de libros y autor relacionados
        // Puedes procesar los resultados según sea necesario
        for (book in booksResult) {
            // Procesa los documentos de libros
            Log.i("firebase", "${book.id} => ${book.data}")
        }
        for (author in authorResult) {
            // Procesa los documentos de autores
            Log.i("firebase", "${author.id} => ${author.data}")
        }
    }.addOnFailureListener { exception ->
        Log.i("firebase", "ERROR")

    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    LibraryAppTheme {
        //ComposeQuadrantApp()
        Greeting()
    }
}