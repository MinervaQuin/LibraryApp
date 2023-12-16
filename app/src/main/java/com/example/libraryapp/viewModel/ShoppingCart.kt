package com.example.libraryapp.viewModel

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.libraryapp.model.resources.Author
import kotlinx.coroutines.flow.MutableStateFlow
import com.example.libraryapp.model.resources.Book

object ShoppingCart {
    private lateinit var viewModel: CartViewModel
    private var _categorySelected = MutableStateFlow("")


    fun init(viewModelRecivido: CartViewModel) {
        viewModel = viewModelRecivido
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

}

