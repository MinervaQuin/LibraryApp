package com.example.libraryapp.viewModel

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.FirestoreRepository
import com.example.libraryapp.model.firebaseAuth.UserData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class profileViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
): ViewModel(
) {
    private var _userData = MutableStateFlow<UserData?>(null)
    val userData = _userData.asStateFlow()


    init {
        viewModelScope.launch {
            _userData.value = firestoreRepository.getuser()
        }
    }

    // Función para subir imagen
    fun uploadProfileImage(imageUri: Uri, onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        viewModelScope.launch {
            firestoreRepository.uploadImageToFirebase(imageUri, onSuccess, onFailure)
        }

    }
    fun getProfileImage(onSuccess: (String) -> Unit, onFailure: (Exception) -> Unit) {
        val userId = firestoreRepository.authConection?.currentUser?.uid ?: return
        firestoreRepository.getProfileImageUrl(userId, onSuccess, onFailure)
    }


}