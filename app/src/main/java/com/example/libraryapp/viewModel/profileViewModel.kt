package com.example.libraryapp.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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

}