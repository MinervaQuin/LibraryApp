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
    var selectedCategory by mutableStateOf<String?>(null)
        private set
    init {
        // Inicializa la lista de libros con datos de prueba o desde algún origen de datos
        categories = listOf("Fiction", "Non-Fiction", "Mystery", "Science Fiction", "Fantasy")
        novedades = getNovedadesNew()
        filtrados = getBookFiltrados()
        selectedCategory = categories.firstOrNull()
    }

    private fun getBookFiltrados(): Array<Book> {
        return arrayOf(
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 3, "Tapa Dura", 20)
        )
    }

    private fun getNovedadesNew(): Array<Book> {
        return arrayOf(
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 3, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 7, "Tapa Dura", 20),
            Book(1, 10, "Arturo Pérez Reverte", "Linea de fuego", "hola hola", 7, "Tapa Dura", 20),
        )
    }
    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
    }
    fun updateSelectedCategory(newCategory: String) {
        selectedCategory = newCategory
    }

}
