package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.RegistrationFormEvent
import com.example.libraryapp.model.emailValidationUseCase.ValidateEmail
import com.example.libraryapp.model.emailValidationUseCase.ValidatePassword
import com.example.libraryapp.model.emailValidationUseCase.ValidateRepeatedPassword
import com.example.libraryapp.model.emailValidationUseCase.ValidateTerms
import com.example.libraryapp.model.emailValidationUseCase.ValidateUserName
import com.example.libraryapp.model.firebaseAuth.EmailAuthUiClient
import com.example.libraryapp.model.resources.registrationFormState
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject


@HiltViewModel
class signUpViewModel @Inject constructor(
    private val validateUserName: ValidateUserName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val validateTerms: ValidateTerms,
    private val emailService: EmailAuthUiClient
): ViewModel(){

    var state by mutableStateOf(registrationFormState())

    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private var _navigateToNextScreen = MutableStateFlow(false)
    var navigateToNextScreen = _navigateToNextScreen.asStateFlow()

    private var _showFirstScreen2 = MutableStateFlow(true)
    var showFirstScreen2 = _showFirstScreen2.asStateFlow()



    fun onEvent(event: RegistrationFormEvent) {
        when(event) {
            is RegistrationFormEvent.NameChanged -> {
                state = state.copy(name = event.name)
            }
            is RegistrationFormEvent.EmailChanged -> {
                state = state.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                state = state.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                state = state.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.AcceptTerms -> {
                state = state.copy(acceptedTerms = event.isAccepted)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }


    private fun submitData() {
        val nameResult = validateUserName.execute((state.name))
        val emailResult = validateEmail.execute(state.email)
        val passwordResult = validatePassword.execute(state.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            state.password, state.repeatedPassword
        )
        val termsResult = validateTerms.execute(state.acceptedTerms)

        val hasError = listOf(
            nameResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        if(hasError) {
            state = state.copy(
                nameError = nameResult.errorMessage,
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage,
                repeatedPasswordError = repeatedPasswordResult.errorMessage,
                termsError = termsResult.errorMessage
            )
            return
        }
        viewModelScope.launch {
            //validationEventChannel.send(ValidationEvent.Success)
        }
        registerUser(state.email, state.password, state.name)
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
        data class Failed(val errorMessage: String) : ValidationEvent()
    }


    fun changeScreen(){
        if(_showFirstScreen2.value){
            _showFirstScreen2.value = false
        }else{
            _showFirstScreen2.value = true
        }
    }

    private fun registerUser(email: String, password: String, userName: String) {
        viewModelScope.launch {
            val result = emailService.registerUser(email, password, userName)
            result.fold(
                onSuccess = {
                    // Manejo del caso de éxito
                    validationEventChannel.send(ValidationEvent.Success)
                },
                onFailure = { exception ->
                    // Manejo del caso de error
                    when (exception) {
                        is FirebaseAuthUserCollisionException -> {
                            validationEventChannel.send(ValidationEvent.Failed("Error, el correo ya está en uso"))
                        }
                        else -> {
                            validationEventChannel.send(ValidationEvent.Failed("Error, vuelva a intentarlo más tarde"))
                        }
                    }
                }
            )
        }
    }

}