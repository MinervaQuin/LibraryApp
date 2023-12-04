package com.example.libraryapp.viewModel

import kotlinx.coroutines.flow.MutableStateFlow
import com.example.libraryapp.model.resources.Book

object ShoppingCart {
    private lateinit var viewModel: CartViewModel
    private var _categorySelected = MutableStateFlow("")
    private lateinit var bookSelected: Book

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

    fun setBookSelected(book: Book){
        this.bookSelected= book
    }
    fun getBookSelected(): Book{
        return this.bookSelected
    }
}

