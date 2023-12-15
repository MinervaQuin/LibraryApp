package com.example.libraryapp.model.firebaseAuth

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        // Maneja aquí el mensaje FCM recibido
    }

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        // Envía el token FCM a tu servidor, si es necesario

    }
}