package com.example.libraryapp.model

class Author (
    var id: String = "",
    var name: String = "",
    var biography: String = "",
    var works: Array<Book?> = arrayOf()
)       {
}