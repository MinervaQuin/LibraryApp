package com.example.libraryapp.model.resources

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
class Review (
    var reviewId: String = "",
    var userId: String,
    var userName: String,
    var score: Double,
    var description: String,
    var date: LocalDateTime? = null,
){
    override fun toString(): String {

        val dateString = date?.format(DateTimeFormatter.ISO_LOCAL_DATE) ?: "No Date"
        return "Review(userId='$userId', score=$score, description='$description', date='$dateString')"
    }
}
