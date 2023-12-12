package com.example.libraryapp.viewModel

import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.RegistrationFormEvent
import com.example.libraryapp.model.emailValidationUseCase.ValidateEmail
import com.example.libraryapp.model.emailValidationUseCase.ValidatePassword
import com.example.libraryapp.model.emailValidationUseCase.ValidateRepeatedPassword
import com.example.libraryapp.model.emailValidationUseCase.ValidateTerms
import com.example.libraryapp.model.emailValidationUseCase.ValidateUserName
import com.example.libraryapp.model.firebaseAuth.EmailAuthUiClient
import com.example.libraryapp.model.firebaseAuth.GoogleAuthUiClient
import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.example.libraryapp.model.firebaseAuth.SignInState
import com.example.libraryapp.model.firebaseAuth.UserData
import com.example.libraryapp.model.resources.registrationFormState
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
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


    var userEmail: String = ""
    var userPassword: String = ""

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
            nameResult,
            emailResult,
            passwordResult,
            repeatedPasswordResult,
            termsResult
        ).any { !it.successful }

        if(hasError) {
            formState = formState.copy(
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
    }

    fun resetState() {
        _state.update { SignInState() }
    }

     fun signInWithEmail(email: String, passworld: String, home: () -> Unit) = viewModelScope.launch{
         try {
             auth.signInWithEmailAndPassword(email, passworld)
                 .addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         Log.d("Login Email", "LOGEADO PUTO")
                         home()
                     } else {
                         // Imprimir el mensaje de error en caso de fallo
                         val errorMessage = task.exception?.message ?: "Error desconocido"
                         Log.d("Login Email", "Error al iniciar sesión: $errorMessage")
                     }
                 }
         } catch (e: Exception) {
             Log.d("Login Email", "Error de excepción: ${e.message}")
         }
         catch (e: Exception){
             Log.d("Login Email", "${e.message}")
         }
    }

}
