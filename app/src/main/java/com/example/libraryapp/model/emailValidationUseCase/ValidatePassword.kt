package com.example.libraryapp.model.emailValidationUseCase

import android.util.Patterns

class ValidatePassword {

    fun execute(passWord: String): ValidationResult {
        if(passWord.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña no puede estar vacía"
            )
        }
        if(passWord.length < 8){
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos 8 carácteres"
            )
        }
        val containsLettersAndDigits = passWord.any {it.isDigit()} && passWord.any{it.isLetter()}

        if(!containsLettersAndDigits){
            return ValidationResult(
                successful = false,
                errorMessage = "La contraseña debe tener al menos una letra y un dígito"
            )
        }
        return ValidationResult(
            successful = true
        )
    }
}