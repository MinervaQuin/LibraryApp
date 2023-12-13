package com.example.libraryapp.model.validationModels.emailValidationUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateUserName {

    fun execute(userName: String): ValidationResult {
        if(userName.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre no puede estar vacío"
            )
        }
        if(userName.length < 3 || userName.length > 12){
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre debe tener entre 3 y 12 caracteres"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}