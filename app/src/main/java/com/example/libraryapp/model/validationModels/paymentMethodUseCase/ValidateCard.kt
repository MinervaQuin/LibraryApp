package com.example.libraryapp.model.validationModels.paymentMethodUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateCard {
    fun execute(card: String): ValidationResult {
        if (card.any { !it.isDigit() } || card.length != 16) {
            return ValidationResult(
                successful = false,
                errorMessage = "Número de tarjeta inválido. Longitud Incorrecta"
            )
        }

        val sum = card.reversed().mapIndexed { index, c ->
            val digit = c.toString().toInt()
            if (index % 2 == 0) {
                digit
            } else {
                val doubled = digit * 2
                if (doubled > 9) doubled - 9 else doubled
            }
        }.sum()

        return if (sum % 10 == 0) {
            ValidationResult(successful = true)
        } else {
            ValidationResult(
                successful = false,
                errorMessage = "Número de tarjeta inválido."
            )
        }
    }
}