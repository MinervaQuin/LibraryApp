package com.example.libraryapp.model.validationModels.emailValidationUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

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