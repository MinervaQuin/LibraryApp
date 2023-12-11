package com.example.libraryapp.viewModel

import androidx.lifecycle.ViewModel
import com.example.libraryapp.model.FirestoreRepository
import javax.inject.Inject

class autoresViewModel @Inject constructor(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

}