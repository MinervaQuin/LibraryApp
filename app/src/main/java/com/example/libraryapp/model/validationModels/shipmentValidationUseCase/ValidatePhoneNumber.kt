package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

import android.util.Patterns
import com.example.libraryapp.model.validationModels.ValidationResult

class ValidatePhoneNumber {

    fun execute(phontNumber: String): ValidationResult{
        if(phontNumber.isBlank()){
            return ValidationResult(
                successful = false,
                errorMessage = "Campo obligatorio"
            )
        }
        if(!Patterns.PHONE.matcher(phontNumber).matches()){
            return ValidationResult(
                successful = false,
                errorMessage = "Teléfono Inválido"
            )
        }
        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }
}