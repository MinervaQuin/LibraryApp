package com.example.libraryapp.model

import com.example.libraryapp.model.resources.Book
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryAppState @Inject constructor() {
    private var bookId: String = ""
    private var searchedString: String = ""
    private var bookSelected: Book? = null
    var refreshReviews: Boolean = false


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
}