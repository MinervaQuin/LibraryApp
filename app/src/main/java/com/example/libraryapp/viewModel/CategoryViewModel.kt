package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import androidx.compose.runtime.*
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.resources.Collection
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository,
    val libraryAppState: LibraryAppState
) : ViewModel() {

    private var _loading = MutableStateFlow(false)
    var loading = _loading.asStateFlow()

    val idcolecciones = mapOf(
        "Populares" to "oBMLVCnbNsPQJiPexKL7",
        "Imprescindibles" to "zUQSuupoSizxqCKsLQX8",
        "Ficción" to "ESLWeeZsPaU0fA0XR9nt",
        "No Ficción" to "bgnmJtAVQ1uQFLjz3RO5",
        "Infantil" to "5SVJKEbVOdy1jUD3YDYn",
        "Cómic y Manga" to "p8KT9DM4yIbmdV5U8k4v",
        "Misterio" to "aSN9cOFkRzmA7QtxIx6i",
        "Recomendados" to "95VaOY3DjRHtWocgCm9H",
        "Promociones" to "irEC8DDKxtesuSH7ip3g",
        "Blog" to "szbh1qqLATTVLcur2p7j",
        "Premiados" to "9kPXIbFOl8qG0rwGHnAq",
        "eBooks" to "U5SRP9P3KPzxCIGeX5hV",
        "Novedades" to "aFWNP6mASVxgPwt8wS0c",
        "Autores" to "",
        "Romance" to "BvTWsslTbG94qnQ36fmI"



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
    var selectedShort by mutableStateOf<String>("Precio ascendente")
        private set
    init {
        // Inicializa la lista de libros con datos de prueba o desde algún origen de datos
        categories = listOf("Todas Las Categorias","Imprescindibles","Ficción", "No Ficción", "Infantil","Misterio", "Romance",
            "Cómic y Manga","Populares", "Recomendados", "Promociones", "Blog", "Premiados",
            "eBooks", "Autores", "Novedades" )
    }

    fun setNewBook(book: Book){
        libraryAppState.setBook(book)
    }
    suspend fun updatebooks(Categorias : String) {
            _loading.value = true
            if(Categorias == "Todas Las Categorias"){
                novedades = firestoreRepository.getAllBooks2().toTypedArray()
                val listaLibros: List<Book> = novedades.filterNotNull()
                filtrados= listaLibros.toTypedArray()
                novedades= novedades.take(10).toTypedArray()
            }
            else{
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
            _loading.value = false
    }
    fun updateCategories(newCategories: List<String>) {
        categories = newCategories
    }
    fun updateSelectedCategory(newCategory: String) {
        selectedCategory = newCategory
        viewModelScope.launch {
            updatebooks(selectedCategory)
            updateShort(selectedShort)
        }
        ShoppingCart.setSelectedCategory(newCategory)
    }

    fun updateFiltrados(low : Int, high: Int) {
        viewModelScope.launch {
            updatebooks(selectedCategory)
            filtrados = filtrados.filter { it.price >= low }.toTypedArray()
            filtrados = filtrados.filter { it.price <= high }.toTypedArray()
            updateShort(selectedShort)
        }
    }

    fun updateShort(short :String) {
        selectedShort = short
        if (selectedShort=="Precio ascendente"){
            filtrados = filtrados.sortedBy { it.price }.toTypedArray()
        }
        if (selectedShort=="Precio descendente"){
            filtrados = filtrados.sortedByDescending { it.price }.toTypedArray()
        }
        if (selectedShort=="Mas valorados"){
            filtrados = filtrados.sortedByDescending { it.score }.toTypedArray()
        }
        if (selectedShort=="Título A-Z"){
            filtrados = filtrados.sortedBy { it.title }.toTypedArray()
        }
        if (selectedShort=="Título Z-A"){
            filtrados = filtrados.sortedByDescending { it.title }.toTypedArray()
        }

    }

}
