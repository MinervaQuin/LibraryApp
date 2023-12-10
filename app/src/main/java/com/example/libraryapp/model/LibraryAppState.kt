package com.example.libraryapp.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LibraryAppState @Inject constructor() {
    private var bookId: String = ""
    private var searchedString: String = ""


    fun setSearchedString(searchString: String){
        searchedString= searchString
    }

    fun getSearchedString(): String{
        return searchedString
    }
}