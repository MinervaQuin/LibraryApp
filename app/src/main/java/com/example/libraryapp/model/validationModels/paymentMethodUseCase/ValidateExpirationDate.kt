package com.example.libraryapp.model.validationModels.paymentMethodUseCase

import com.example.libraryapp.model.validationModels.ValidationResult
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class ValidateExpirationDate {
    fun execute(expirationDate: String): ValidationResult {
        // Verificar el formato MM/YY
        if (!expirationDate.matches(Regex("\\d{2}/\\d{2}"))) {
            return ValidationResult(
                successful = false,
                errorMessage = "Formato Incorrecto"
            )
        }

        try {
            // Convertir la cadena a un objeto YearMonth
            val formatter = DateTimeFormatter.ofPattern("MM/yy")
            val date = YearMonth.parse(expirationDate, formatter)

            // Verificar si la fecha de expiración ya pasó
            if (date.isBefore(YearMonth.now())) {
                return ValidationResult(
                    successful = false,
                    errorMessage = "La fecha de expiración ya ha pasado."
                )
            }
        } catch (e: DateTimeParseException) {
            throw e
            return ValidationResult(
                successful = false,
                errorMessage = "Error"
            )
        }

        // La fecha de expiración es válida
        return ValidationResult(successful = true)
    }
}