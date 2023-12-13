package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

data class ShipmentFormState(
    val name: String = "",
    val lastName: String= "",
    val adress: String = "",
    val country: String ="",
    val zipCode: String = "",
    val province: String = "",
    val city: String ="",
    val phoneNumber: String = "",
    val dni: String = ""
)
