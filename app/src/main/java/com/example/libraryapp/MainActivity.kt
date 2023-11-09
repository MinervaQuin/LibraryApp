package com.example.libraryapp

import android.app.Activity.RESULT_OK
import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.libraryapp.theme.LibraryAppTheme
import com.example.libraryapp.ui.LoginView
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.libraryapp.ui.signUpView
import com.example.libraryapp.viewModel.loginViewModel
import androidx.lifecycle.lifecycleScope
import com.example.libraryapp.model.firebaseAuth.GoogleAuthUiClient
import com.example.libraryapp.ui.HomeView
import com.google.android.gms.auth.api.identity.Identity
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
    val db = Firebase.firestore
    public val googleAuthUiClient by lazy {
        GoogleAuthUiClient(
            context = applicationContext,
            oneTapClient = Identity.getSignInClient(applicationContext)
        )
    }

    /*override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting()
                }
            }
        }
    }*/
    /*
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LibraryAppTheme {
                // Use a Surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colorScheme.background) {
                    LoginView() // Aquí colocamos nuestro composable de inicio de sesión
                }
            }
        }
    }*/
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "login"){
                composable("login") {

                    val viewModel = viewModel<loginViewModel>()
                    val state by viewModel.state.collectAsStateWithLifecycle()

                    LaunchedEffect(key1 = Unit){
                        if(googleAuthUiClient.getSignedInUser() != null){
                            navController.navigate("homePage")
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

                            navController.navigate("homePage")
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

                composable("homePage"){
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
                        }

                    )
                }

                composable("signUp") { signUpView(navController = navController)}
            }
        }
    }
}

@Composable
fun librearyApp(){

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

