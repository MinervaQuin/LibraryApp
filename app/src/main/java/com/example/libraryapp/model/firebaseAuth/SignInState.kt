package com.example.libraryapp.model.firebaseAuth

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)