package com.example.libraryapp

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.libraryapp.ui.theme.LibraryAppTheme
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.firestore
import kotlin.math.log

class MainActivity : ComponentActivity() {
    val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
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