package com.example.libraryapp.model.resources

import java.time.LocalDate
import java.time.format.DateTimeFormatter
class Review (
    var userId: String = "0",
    var userName: String = "user",
    var score: Double = 0.0,
    var description: String = "Error",
    var date: LocalDate? = null
){
    override fun toString(): String {

        val dateString = date?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "No Date"
        return "Review(userId='$userId', score=$score, description='$description', date='$dateString')"
    }
}
