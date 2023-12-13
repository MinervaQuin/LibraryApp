package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateZipCode {

    fun execute(zipCode: String): ValidationResult{
        if(zipCode.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Campo obligatorio"
            )
        }
        if(zipCode.length != 5){
            return ValidationResult(
                successful = false,
                errorMessage = "CP Inválido"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}