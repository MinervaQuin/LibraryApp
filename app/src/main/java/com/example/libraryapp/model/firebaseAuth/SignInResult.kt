package com.example.libraryapp.model.firebaseAuth

data class SignInResult(
    val data: UserData?,
    val errorMessage: String?
)

data class UserData(
    val userId: String,
    val userName: String?,
    val profilePictureUrl: String?
)
