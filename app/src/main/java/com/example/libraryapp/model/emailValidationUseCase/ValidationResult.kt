package com.example.libraryapp.model.emailValidationUseCase

data class ValidationResult(
    val successful: Boolean,
    val errorMessage: String? = null
)
