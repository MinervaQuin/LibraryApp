package com.example.libraryapp.viewModel

import android.app.usage.UsageEvents
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
class loginViewModel: ViewModel() {
    private val _username = MutableLiveData("")
    val username: LiveData<String> = _username

    private val _password = MutableLiveData("")
    val password: LiveData<String> = _password


}

