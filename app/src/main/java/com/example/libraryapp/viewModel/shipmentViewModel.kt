package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentAdressFormEvent
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentFormState
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateAdress
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateDNI
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateName
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidatePhoneNumber
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateZipCode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class shipmentViewModel @Inject constructor(
    private val validateZipCode: ValidateZipCode,
    private val validateName: ValidateName,
    private val validatePhoneNumber: ValidatePhoneNumber,
    private val validateAdress: ValidateAdress,
    private val validateDNI: ValidateDNI
) : ViewModel() {

    var state by mutableStateOf(ShipmentFormState())
    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()
    fun onEvent(event: ShipmentAdressFormEvent){
        when(event){
            is ShipmentAdressFormEvent.AdressChanged -> {
                state = state.copy(adress = event.adress)
            }
            is ShipmentAdressFormEvent.DNIChanged -> {
                state = state.copy(dni = event.dni)
            }
            is ShipmentAdressFormEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is ShipmentAdressFormEvent.ZipCodeChanged -> {
                state = state.copy(zipCode = event.zip)
            }
            is ShipmentAdressFormEvent.PhoneNumberChanged -> {
                state = state.copy(phoneNumber = event.phone)
            }
            is ShipmentAdressFormEvent.lastNameChanged -> {
                state = state.copy(lastName = event.lastName)
            }
            is ShipmentAdressFormEvent.provinceChanged -> {
                state = state.copy(province = event.province)
            }
            is ShipmentAdressFormEvent.countryChanged -> {
                state = state.copy(country = event.country)
            }
            is ShipmentAdressFormEvent.cityChanged -> {
                state = state.copy(event.city)
            }
            is ShipmentAdressFormEvent.Submit -> {
                Log.d("shipmenadressformevent", "funciona")
            }
        }
    }

    private fun submitData(){
        val adressResult = validateAdress.execute(state.adress)
        val dniResult = validateDNI.execute(state.dni)
        val nameResult = validateName.execute(state.name)
        val lastNameResult = validateName.execute(state.lastName)
        val zipCodeResult = validateZipCode.execute(state.zipCode)
        val provinceResult = validateName.execute(state.province)
        val countryResult = validateName.execute(state.country)
        val cityResult = validateName.execute(state.city)
        val phoneNumberResult = validatePhoneNumber.execute(state.phoneNumber)

        val hasError = listOf(
            adressResult,
            dniResult,
            nameResult,
            lastNameResult,
            zipCodeResult,
            provinceResult,
            countryResult,
            cityResult,
            adressResult,
            phoneNumberResult
        ).any { !it.successful }

        if(hasError){
            state = state.copy(
                adressError = adressResult.errorMessage,
                nameError = nameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                countryError = countryResult.errorMessage,
                zipCodeError = zipCodeResult.errorMessage,
                provinceError = provinceResult.errorMessage,
                cityError = cityResult.errorMessage,
                phoneNumberError = phoneNumberResult.errorMessage,
                dniError = dniResult.errorMessage

            )
            return
        }
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }

    }
    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }
}