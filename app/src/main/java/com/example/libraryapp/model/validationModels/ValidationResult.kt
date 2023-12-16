package com.example.libraryapp.model.validationModels

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
