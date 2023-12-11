package com.example.libraryapp.model.emailValidationUseCase

import android.util.Patterns

class ValidateTerms {

    fun execute(acceptedTerms: Boolean): ValidationResult {
        if(!acceptedTerms){
            return ValidationResult(
                successful = false,
                errorMessage = "Debe aceptar los términos y condiciones"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}