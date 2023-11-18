package com.example.libraryapp.model.resources

class Collection (
    var name: String = "",
    var description: String = "",
    var books: Array<Book?> = arrayOf()
){
    override fun toString(): String {

        val booksString = books.joinToString(separator = ", ", transform = { book ->
            book?.toString() ?: "null"
        })

        return "Collection(name='$name', description='$description', books=[$booksString])"
    }
}