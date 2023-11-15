package com.example.libraryapp.viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.Author
import com.example.libraryapp.model.Book
import androidx.compose.runtime.*


class CategoryViewModel : ViewModel() {


    var novedades by mutableStateOf(arrayOf<Book>())
        private set
    var filtrados by mutableStateOf(arrayOf<Book>())
        private set
    var categories by mutableStateOf(listOf<String>())
        private set
    var selectedCategory by mutableStateOf<String>("")
        private set
    init {
        // Inicializa la lista de libros con datos de prueba o desde algún origen de datos
        categories = listOf("Fiction", "Non-Fiction", "Mystery", "Science Fiction", "Fantasy")
        selectedCategory = categories.firstOrNull().toString()
        novedades = getNovedadesNew(selectedCategory)
        filtrados = getBookFiltrados(selectedCategory)
    }

    private fun getBookFiltrados(Categorias : String): Array<Book> {
        if(Categorias == "Mystery"){
            return arrayOf(
                Book(12, 15, "Stephen King", "Misery1", "hola hola", 3, "Tapa Dura", 35),
                Book(12, 15, "Stephen King", "Misery2", "hola hola", 7, "Tapa Dura", 40),
                Book(12, 15, "Stephen King", "Misery3", "hola hola", 7, "Tapa Dura", 45),
            )
        }
        return arrayOf(
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego1", "hola hola", 3, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego2", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego3", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego4", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego5", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego6", "hola hola", 7, "Tapa Dura", 20),
        )

    }

    private fun getNovedadesNew(Categorias : String): Array<Book> {
        if(Categorias == "Mystery"){
            return arrayOf(
                Book(12, 15, "Stephen King", "Misery1", "hola hola", 3, "Tapa Dura", 35),
                Book(12, 15, "Stephen King", "Misery2", "hola hola", 7, "Tapa Dura", 40),
                Book(12, 15, "Stephen King", "Misery3", "hola hola", 7, "Tapa Dura", 45),
            )
        }
        return arrayOf(
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego1", "hola hola", 3, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego2", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego3", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego4", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego5", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego6", "hola hola", 7, "Tapa Dura", 20),
        )
    }
    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
    }
    fun updateSelectedCategory(newCategory: String) {
        selectedCategory = newCategory
        filtrados = getBookFiltrados(selectedCategory)
        novedades = getNovedadesNew(selectedCategory)
    }

}
