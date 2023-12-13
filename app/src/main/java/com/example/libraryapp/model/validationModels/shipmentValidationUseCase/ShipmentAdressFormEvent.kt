package com.example.libraryapp.model.validationModels.shipmentValidationUseCase

import com.example.libraryapp.model.validationModels.emailValidationUseCase.RegistrationFormEvent

sealed class ShipmentAdressFormEvent {
    data class AdressChanged(val adress: String): ShipmentAdressFormEvent()
    data class DNIChanged(val dni: String): ShipmentAdressFormEvent()
    data class NameChanged(val name: String): ShipmentAdressFormEvent()
    data class PhoneNumberChanged(val phone: String): ShipmentAdressFormEvent()
    data class ZipCodeChanged(val zip: String): ShipmentAdressFormEvent()
    data class lastNameChanged(val lastName: String): ShipmentAdressFormEvent()
    data class countryChanged(val country: String): ShipmentAdressFormEvent()
    data class provinceChanged(val province: String): ShipmentAdressFormEvent()
    data class cityChanged(val city: String): ShipmentAdressFormEvent()

    object Submit: ShipmentAdressFormEvent()
}