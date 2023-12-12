package com.example.libraryapp.model

import androidx.navigation.NavHostController
import com.example.libraryapp.model.resources.Author
import com.example.libraryapp.model.resources.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryAppState @Inject constructor() {
    private var bookId: String = ""
    private var searchedString: String = ""
    private var bookSelected: Book? = null
    var refreshReviews: Boolean = false
    private lateinit var navController: NavHostController
    private var autorId: String? = null
    private var autor: Author? = null


    fun setSearchedString(searchString: String){
        searchedString= searchString
    }

    fun getSearchedString(): String{
        return searchedString
    }

    fun setBookId(bookid: String){
        bookId = bookid
    }

    fun getBookId(): String{
        return bookId
    }

    fun setBook(book: Book){
        bookSelected = book
    }

    fun getBook(): Book? {
        return bookSelected
    }

    fun setNavController(new: NavHostController){
        this.navController = new
    }
    fun getNavController(): NavHostController {
        return this.navController
    }

    fun setautor(new: Author?){
        this.autor = new
    }
    fun getAutor(): Author?{
        return this.autor
    }

    fun setautorId(new: String?){
        this.autorId = new
    }
    fun getautorId(): String?{
        return this.autorId
    }
}