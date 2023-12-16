package com.example.libraryapp.model.validationModels.paymentMethodUseCase

data class PaymentMethodFormState(
    val card: String = "",
    val cardError: String? = null,
    val name: String = "",
    val nameError: String? = null,
    val expireDate: String = "",
    val expireDateError: String? = null,
    val cvv: String = "",
    val cvvError: String? = null
)
