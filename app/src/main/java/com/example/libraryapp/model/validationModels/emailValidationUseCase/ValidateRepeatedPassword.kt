package com.example.libraryapp.model.validationModels.emailValidationUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateRepeatedPassword {

    fun execute(passWord: String, repeatedPassword: String): ValidationResult {
        if(passWord != repeatedPassword){
            return ValidationResult(
                successful = false,
                errorMessage = "Las contraseñas deben coincidir"
            )
        }

        return ValidationResult(
            successful = true
        )
    }
}