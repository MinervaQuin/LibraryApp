package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

import com.example.libraryapp.model.validationModels.ValidationResult

class ValidateDNI {

    fun execute(dni: String): ValidationResult {
        if (dni.isBlank()) {
            return ValidationResult(
                successful = false,
                errorMessage = "Campo obligatorio"
            )
        }

        // La expresión regular para validar el DNI español
        val dniRegex = Regex("^[0-9]{8}[A-Za-z]$")

        if (!dniRegex.matches(dni)) {
            return ValidationResult(
                successful = false,
                errorMessage = "DNI Inválido"
            )
        }

        // Verificación adicional: calcular la letra del DNI y comprobar si coincide
        if (!isDNILetterCorrect(dni)) {
            return ValidationResult(
                successful = false,
                errorMessage = "DNI Inválido"
            )
        }

        return ValidationResult(
            successful = true,
            errorMessage = null
        )
    }


}
private fun isDNILetterCorrect(dni: String): Boolean {
    val dniLetters = "TRWAGMYFPDXBNJZSQVHLCKE"
    val numbers = dni.take(8).toIntOrNull() ?: return false
    val letter = dni.takeLast(1).uppercase()
    return dniLetters[numbers % 23].toString() == letter
}