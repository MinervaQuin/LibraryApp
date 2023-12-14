package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepository
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentAdressFormEvent
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ShipmentFormState
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateAdress
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateDNI
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateName
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidatePhoneNumber
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidateZipCode
import com.example.libraryapp.model.validationModels.shipmentValidationUseCase.ValidatedShipmentAdress
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
    private val libraryAppState: LibraryAppState,

) : ViewModel() {

    var state by mutableStateOf(ShipmentFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: ShipmentAdressFormEvent){
        when(event){
            is ShipmentAdressFormEvent.AdressChanged -> {
                state = state.copy(adress = event.adress)
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
            is ShipmentAdressFormEvent.cityChanged -> {
                state = state.copy(city = event.city)
            }
            is ShipmentAdressFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData(){
        val adressResult = validateAdress.execute(state.adress)
        val nameResult = validateName.execute(state.name)
        val lastNameResult = validateName.execute(state.lastName)
        val zipCodeResult = validateZipCode.execute(state.zipCode)
        val provinceResult = validateName.execute(state.province)
        val cityResult = validateName.execute(state.city)
        val phoneNumberResult = validatePhoneNumber.execute(state.phoneNumber)

        val hasError = listOf(
            adressResult,
            nameResult,
            lastNameResult,
            zipCodeResult,
            provinceResult,
            cityResult,
            adressResult,
            phoneNumberResult
        ).any { !it.successful }

        if(hasError){
            Log.d("Hola", "adios")
            state = state.copy(
                adressError = adressResult.errorMessage,
                nameError = nameResult.errorMessage,
                lastNameError = lastNameResult.errorMessage,
                zipCodeError = zipCodeResult.errorMessage,
                provinceError = provinceResult.errorMessage,
                cityError = cityResult.errorMessage,
                phoneNumberError = phoneNumberResult.errorMessage,

            )
            Log.d("Error: ", "${state}")
            return
        }
        val sender = ValidatedShipmentAdress(
            addressResult = state.adress,
            nameResult = state.name,
            lastNameResult = state.lastName,
            zipCodeResult = state.zipCode,
            provinceResult = state.province,
            cityResult = state.city,
            phoneNumberResult = state.phoneNumber
        )
        libraryAppState.setAdress(sender)
        viewModelScope.launch {
            validationEventChannel.send(ValidationEvent.Success)
        }

    }
    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }


}