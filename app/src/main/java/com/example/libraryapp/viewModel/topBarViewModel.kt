package com.example.libraryapp.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.firebaseAuth.UserData
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class topBarViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel() {

    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()

    private var _profilePictureUrl = MutableStateFlow<String?>(null)
    val profilePictureUrl = _profilePictureUrl.asStateFlow()

    fun getProfileImage() {
        viewModelScope.launch {
            val userId = firestoreRepository.authConection?.currentUser?.uid
            if (userId != null) {
                try {
                    val url = firestoreRepository.getProfileImageUrl(userId)
                    _profilePictureUrl.value = url // Actualiza la variable de estado
                } catch (e: Exception) {
                    _profilePictureUrl.value = "https://i.imgur.com/xiL43UU.jpeg"
                }
            }
        }
    }

    init {
        viewModelScope.launch {
            _userData.value = firestoreRepository.getuser()
        }
        Log.d("Topbar", "BASURA")
    }

}