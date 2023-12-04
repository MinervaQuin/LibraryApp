package com.example.libraryapp.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun MapScreen() {
    val uni = LatLng(28.07337573999047, -15.451748844763351)
    val marker = MarkerState(position = uni)
    val cameraPositionState = rememberCameraPositionState{
        position = CameraPosition.fromLatLngZoom(uni,10f)
    }
    GoogleMap (
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(
            state = marker,
            title = "Escuela de Ingeniería Informática"
        )
    }
}