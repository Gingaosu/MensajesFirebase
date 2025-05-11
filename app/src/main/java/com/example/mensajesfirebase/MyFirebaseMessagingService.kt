package com.example.mensajesfirebase

import android.content.Intent
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        val title = remoteMessage.notification?.title ?: "Sin título"
        val body = remoteMessage.notification?.body ?: "Sin mensaje"

        // Enviar mensaje como broadcast local
        val intent = Intent("FCM_MESSAGE")
        intent.putExtra("title", title)
        intent.putExtra("body", body)
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
    }
}
