package com.example.libraryapp.model.emailValidationUseCase

import android.util.Patterns

class ValidateEmail {

    fun execute(email: String): ValidationResult {
        if(email.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "El email no puede estar vacío"
            )
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "El email introducido no es válido"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}