package com.example.libraryapp.model.validationModels.paymentMethodUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateOwnerName {
    fun execute(name: String): ValidationResult {
        if(name.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Campo obligatorio"
            )
        }
        if(name.length < 3 || name.length > 42){
            return ValidationResult(
                successful = false,
                errorMessage = "El nombre debe tener entre 3 y 42 caracteres"
            )
        }

        if(name.any { it.isDigit()}){
            return ValidationResult(
                successful = false,
                errorMessage = "No puede contener Dígitos"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}