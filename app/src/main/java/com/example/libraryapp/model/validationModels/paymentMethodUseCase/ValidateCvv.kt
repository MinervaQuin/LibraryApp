package com.example.libraryapp.model.validationModels.paymentMethodUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateCvv {
    fun execute(cvv: String): ValidationResult {
        // Verificar que el CVV tenga la longitud correcta y solo contenga dígitos
        if (!cvv.matches(Regex("\\d{3,4}"))) {
            return ValidationResult(
                successful = false,
                errorMessage = "CVV inválido"
            )
        }

        // El CVV es válido
        return ValidationResult(successful = true)
    }
}