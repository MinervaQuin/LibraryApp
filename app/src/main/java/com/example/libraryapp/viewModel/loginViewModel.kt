package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.SignInResult
import com.example.libraryapp.model.firebaseAuth.SignInState
import com.example.libraryapp.model.firebaseAuth.UserData
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException

class loginViewModel: ViewModel() {

    var userEmail: String = ""
    var userPassword: String = ""

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    private val auth: FirebaseAuth = Firebase.auth //esto debería estar en el model?

    fun onSignInResult(result: SignInResult) {
        _state.update { it.copy(
            isSignInSuccessful = result.data != null,
            signInError = result.errorMessage
        ) }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    fun updateCredentials(newEmail: String, newPassword: String) {
        userEmail = newEmail
        userPassword = newPassword
        Log.d("LoginViewModel", "Email actualizado: $newEmail, contraseña : $newPassword")
    }

    /*
    suspend fun signInWithEmail(email: String, password: String): SignInResult {
        return try {
            val user = auth.signInWithEmailAndPassword(email, password).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }
    */
     fun signInWithEmail(email: String, passworld: String, home: () -> Unit) = viewModelScope.launch{
         try {
             auth.signInWithEmailAndPassword(email, passworld)
                 .addOnCompleteListener {task ->
                     if( task.isSuccessful){
                         Log.d("Login Email", "LOGEADO PUTO")
                         home()
                     }else{
                         Log.d("Login Email", "SOCORRO")
                     }
                 }
         }
         catch (e: Exception){
             Log.d("Login Email", "${e.message}")
         }
    }

}
