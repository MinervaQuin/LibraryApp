package com.example.libraryapp.model.validationModels.paymentMethodUseCase

import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentAdressFormEvent

sealed class PaymentMethodFormEvent {
    data class CardChanged(val card: String): PaymentMethodFormEvent()
    data class CvvChanged(val cvv: String): PaymentMethodFormEvent()
    data class ExpirationDateChanged(val date: String): PaymentMethodFormEvent()
    data class OwnerChanged(val name: String): PaymentMethodFormEvent()

    object Submit: PaymentMethodFormEvent()
}