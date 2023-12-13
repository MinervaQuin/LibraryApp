package com.example.libraryapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentFormState

class shipmentViewModel: ViewModel() {

    var state by mutableStateOf(ShipmentFormState())
}