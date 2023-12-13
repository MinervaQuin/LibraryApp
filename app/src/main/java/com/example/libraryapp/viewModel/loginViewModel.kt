package com.example.libraryapp.viewModel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.validationModels.emailValidationUseCase.RegistrationFormEvent
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateEmail
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidatePassword
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateRepeatedPassword
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateTerms
import com.example.libraryapp.model.validationModels.emailValidationUseCase.ValidateUserName
import com.example.libraryapp.model.firebaseAuth.EmailAuthUiClient
import com.example.libraryapp.model.firebaseAuth.GoogleAuthUiClient
import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.example.libraryapp.model.firebaseAuth.SignInState
import com.example.libraryapp.model.resources.registrationFormState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class loginViewModel @Inject constructor(
    private val validateUserName: ValidateUserName,
    private val validateEmail: ValidateEmail,
    private val validatePassword: ValidatePassword,
    private val validateRepeatedPassword: ValidateRepeatedPassword,
    private val validateTerms: ValidateTerms,
    private val emailService: EmailAuthUiClient,
    val googleAuthUiClient: GoogleAuthUiClient
): ViewModel() {


    private val validationEventChannel = Channel<signUpViewModel.ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow()

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    var formState by mutableStateOf(registrationFormState())

    private val auth: FirebaseAuth = Firebase.auth //esto debería estar en el model?

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun onEvent(event: RegistrationFormEvent) {
        when(event) {
            is RegistrationFormEvent.NameChanged -> {
                formState = formState.copy(name = event.name)
            }
            is RegistrationFormEvent.EmailChanged -> {
                formState = formState.copy(email = event.email)
            }
            is RegistrationFormEvent.PasswordChanged -> {
                formState = formState.copy(password = event.password)
            }
            is RegistrationFormEvent.RepeatedPasswordChanged -> {
                formState = formState.copy(repeatedPassword = event.repeatedPassword)
            }
            is RegistrationFormEvent.AcceptTerms -> {
                formState = formState.copy(acceptedTerms = event.isAccepted)
            }
            is RegistrationFormEvent.Submit -> {
                submitData()
            }
        }
    }

    private fun submitData() {
        val nameResult = validateUserName.execute((formState.name))
        val emailResult = validateEmail.execute(formState.email)
        val passwordResult = validatePassword.execute(formState.password)
        val repeatedPasswordResult = validateRepeatedPassword.execute(
            formState.password, formState.repeatedPassword
        )
        val termsResult = validateTerms.execute(formState.acceptedTerms)

        val hasError = listOf(
            emailResult,
            passwordResult
        ).any { !it.successful }

        if(hasError) {
            formState = formState.copy(
                emailError = emailResult.errorMessage,
                passwordError = passwordResult.errorMessage
            )
            Log.d("LoginView", "No exito")
            return
        }
        viewModelScope.launch {
            //validationEventChannel.send(ValidationEvent.Success)
        }
        signInWithEmail(formState.email, formState.password)
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun signInWithEmail(email: String, password: String) = viewModelScope.launch {
        try {
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("Login Email", "LOGEADO PUTO")

                        // Lanzar una nueva coroutine para enviar un evento
                        viewModelScope.launch {
                            validationEventChannel.send(signUpViewModel.ValidationEvent.Success)
                        }
                    } else {
                        val errorMessage = task.exception?.message ?: "Error desconocido"
                        Log.d("Login Email", "Error al iniciar sesión: $errorMessage")
                        // Lanzar una nueva coroutine para enviar un evento
                        viewModelScope.launch {
                            validationEventChannel.send(signUpViewModel.ValidationEvent.Failed(errorMessage))
                        }
                    }
                }
        } catch (e: Exception) {
            Log.d("Login Email", "Error de excepción: ${e.message}")
            viewModelScope.launch {
                validationEventChannel.send(signUpViewModel.ValidationEvent.Failed(e.message ?: "Error desconocido"))
            }
        }
    }

    sealed class ValidationEvent {
        object Success: ValidationEvent()
        data class Failed(val errorMessage: String) : ValidationEvent()
    }

}
