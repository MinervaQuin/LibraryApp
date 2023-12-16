package com.example.libraryapp.model.resources

class AuthorFb(
    var id: Int = 0,
    var name: String = "",
    var biography: String = "",
    var works: List<String> = emptyList()
)       {
    override fun toString(): String {
        val worksString = works.joinToString(separator = ", ", transform = { book ->
            book?.toString() ?: "null"
        })
        return "Author(id='$id', name='$name', biography='$biography', works=[$worksString])"
    }
}