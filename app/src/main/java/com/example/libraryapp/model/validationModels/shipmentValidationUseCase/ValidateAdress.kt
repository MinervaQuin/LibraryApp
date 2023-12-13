package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateAdress {

    fun execute(name: String): ValidationResult{
        if(name.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Campo obligatorio"
            )
        }
        if(name.length < 3){
            return ValidationResult(
                successful = false,
                errorMessage = "La dirección debe tener más de 3 carácteres"
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}