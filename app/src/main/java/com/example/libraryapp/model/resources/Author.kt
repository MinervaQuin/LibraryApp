package com.example.libraryapp.model.resources

class Author (
    var id: String = "",
    var name: String = "",
    var biography: String = "",
    var works: Array<Book?> = arrayOf()
)       {
    override fun toString(): String {
        val worksString = works.joinToString(separator = ", ", transform = { book ->
            book?.toString() ?: "null"
        })
        return "Author(id='$id', name='$name', biography='$biography', works=[$worksString])"
    }
}