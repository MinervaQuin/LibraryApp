package com.example.libraryapp.viewModel

import androidx.navigation.NavController
import androidx.navigation.NavHostController
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.libraryapp.model.resources.Book

object ShoppingCart {
    private lateinit var viewModel: CartViewModel
    private var _categorySelected = MutableStateFlow("")
    private lateinit var bookSelected: Book
    private lateinit var navController: NavHostController
    private lateinit var autorId: String


    fun init() {
        viewModel = CartViewModel()
    }

    fun getViewModelInstance(): CartViewModel {
        return viewModel
    }
    fun setSelectedCategory(new: String){
        _categorySelected.value = new
    }
    fun getSelectedCategory(): String{
        return _categorySelected.value
    }
    fun setautorId(new: String){
        this.autorId = new
    }
    fun getautorId(): String{
        return this.autorId
    }
    fun setNavController(new: NavHostController){
        this.navController = new
    }
    fun getNavController(): NavHostController{
        return this.navController
    }

    fun setBookSelected(book: Book){
        this.bookSelected= book
    }
    fun getBookSelected(): Book{
        return this.bookSelected
    }
}

