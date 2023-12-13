package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

data class ShipmentFormState(
    val name: String = "",
    val nameError: String? = null,
    val lastName: String= "",
    val lastNameError: String? = null,
    val adress: String = "",
    val adressError: String? = null,
    val country: String ="",
    val countryError: String? = null,
    val zipCode: String = "",
    val zipCodeError: String? = null,
    val province: String = "",
    val provinceError: String? = null,
    val city: String ="",
    val cityError: String? = null,
    val phoneNumber: String = "",
    val phoneNumberError: String? = null,
    val dni: String = "",
    val dniError: String? = null
)
