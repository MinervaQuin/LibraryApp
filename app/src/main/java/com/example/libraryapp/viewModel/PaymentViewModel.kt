package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.LibraryAppState
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepository
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.PaymentMethodFormEvent
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.PaymentMethodFormState
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateCard
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateCvv
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateExpirationDate
import com.example.libraryapp.model.validationModels.paymentMethodUseCase.ValidateOwnerName
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentViewModel @Inject constructor(
    private val appState: LibraryAppState,
    private val validateCard: ValidateCard,
    private val validateOwnerName: ValidateOwnerName,
    private val validateCvv: ValidateCvv,
    private val validateExpirationDate: ValidateExpirationDate,
    private val ordersFirebase: OrdersFirebaseRepository,

): ViewModel(){

    companion object {
        // Cambiar el acceso a la instancia del CartViewModel mediante el Singleton
        val instance: CartViewModel
            get() = ShoppingCart.getViewModelInstance()
    }

    var state by mutableStateOf(PaymentMethodFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    fun onEvent(event: PaymentMethodFormEvent){
        when (event){
            is PaymentMethodFormEvent.CardChanged -> {
                state = state.copy(card = event.card)
            }
            is PaymentMethodFormEvent.CvvChanged -> {
                state =  state.copy(cvv = event.cvv)
            }
            is PaymentMethodFormEvent.OwnerChanged -> {
                state = state.copy(name = event.name)
            }
            is PaymentMethodFormEvent.ExpirationDateChanged -> {
                state = state.copy(expireDate = event.date)
            }
            is PaymentMethodFormEvent.Submit -> {
                submitData()
            }
        }
    }
    fun submitData(){
        val cardResult = validateCard.execute(state.card)
        val cvvResult = validateCvv.execute(state.cvv)
        val dateResult = validateExpirationDate.execute(state.expireDate)
        val nameResult = validateOwnerName.execute(state.name)

        val hasError = listOf(
            cardResult,
            cvvResult,
            dateResult,
            nameResult
        ).any { !it.successful}

        if(hasError){
            state = state.copy(
                cardError = cardResult.errorMessage,
                cvvError = cvvResult.errorMessage,
                expireDateError = dateResult.errorMessage,
                nameError = nameResult.errorMessage
            )
            Log.d("FUnciona?", "no")
            return
        }


        viewModelScope.launch{
            Log.d("FUnciona?", "si")
            validationEventChannel.send(ValidationEvent.Success)
        }

    }

    sealed class ValidationEvent{
        object Success: ValidationEvent()
    }

    fun buy() {
        viewModelScope.launch {
            val cartData = instance.cartItems.value
            ordersFirebase.uploadCartData(cartData, appState.getAdress())
            instance.clearShoppingCart()
        }

    }
}