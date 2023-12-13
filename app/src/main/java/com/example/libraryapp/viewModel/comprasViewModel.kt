package com.example.libraryapp.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.libraryapp.model.firebaseAuth.OrdersFirebaseRepositoryImpl
import com.example.libraryapp.model.resources.Order
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class comprasViewModel @Inject constructor(
    private val firestoreRepository: OrdersFirebaseRepositoryImpl,
) : ViewModel() {
    var orders by mutableStateOf(listOf<Order>())
        private set
    init {
        viewModelScope.launch {
            orders = firestoreRepository.downloadOrders()
        }
    }
}