package com.example.libraryapp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import androidx.compose.runtime.*
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.resources.Collection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    val idcolecciones = mapOf(
        "Populares" to "oBMLVCnbNsPQJiPexKL7",
        "Imprescindibles" to "zUQSuupoSizxqCKsLQX8",
        "Ficción" to "ESLWeeZsPaU0fA0XR9nt",
        "No Ficción" to "bgnmJtAVQ1uQFLjz3RO5",
        "Infantil" to "5SVJKEbVOdy1jUD3YDYn",
        "Cómic y Manga" to "p8KT9DM4yIbmdV5U8k4v",
        "Misterio" to "aSN9cOFkRzmA7QtxIx6i9+"
    )
    var coleccion by mutableStateOf(Collection())
        private set
    var novedades by mutableStateOf(arrayOf<Book?>())
        private set
    var filtrados by mutableStateOf(arrayOf<Book>())
        private set
    var categories by mutableStateOf(listOf<String>())
        private set
    var selectedCategory by mutableStateOf<String>("")
        private set
    init {
        // Inicializa la lista de libros con datos de prueba o desde algún origen de datos
        categories = listOf("Imprescindibles","Ficción", "No Ficción", "Infantil","Misterio", "Cómic y Manga","Populares")
    }

/*    private fun getBookFiltrados(Categorias : String): Array<Book> {
        if(Categorias == "Ficción"){
            return arrayOf(
                Book(12, 15, "Stephen King", "Misery1", "hola hola", 3, "Tapa Dura", 35.0),
                Book(12, 15, "Stephen King", "Misery2", "hola hola", 7, "Tapa Dura", 40.0),
                Book(12, 15, "Stephen King", "Misery3", "hola hola", 7, "Tapa Dura", 45.0),
            )
        }
        return arrayOf(
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego1", "hola hola", 3, "Tapa Dura", 20.0),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego2", "hola hola", 7, "Tapa Dura", 20.0),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego3", "hola hola", 7, "Tapa Dura", 20.0),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego4", "hola hola", 7, "Tapa Dura", 20.0),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego5", "hola hola", 7, "Tapa Dura", 20.0),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego6", "hola hola", 7, "Tapa Dura", 20.0),
        )

    }
*/
    private fun updatebooks(Categorias : String) {
        viewModelScope.launch {
            val colection: Collection? = firestoreRepository.getCollection(idcolecciones[Categorias]?:"")
            if (colection != null) {
                coleccion=colection
            }
            else{
                coleccion= Collection()
            }
            novedades = coleccion.books.take(10).toTypedArray()
            filtrados = coleccion.books as Array<Book>
        }
    }
    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
    }
    fun updateSelectedCategory(newCategory: String) {
        selectedCategory = newCategory
        updatebooks(selectedCategory)
    }

}
