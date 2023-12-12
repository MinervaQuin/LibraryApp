package com.example.libraryapp.model.emailValidationUseCase

import android.util.Patterns

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