package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

data class ValidatedShipmentAdress(
    val addressResult: String = "",
    val nameResult: String = "",
    val lastNameResult: String = "",
    val zipCodeResult: String = "",
    val provinceResult: String = "",
    val cityResult: String = "",
    val phoneNumberResult: String = ""
)
