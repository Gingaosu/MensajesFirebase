package com.example.mensajesfirebase

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.mensajesfirebase.ui.theme.MensajesFirebaseTheme
import com.google.firebase.messaging.FirebaseMessaging

class MainActivity : ComponentActivity() {

    private val mensajes = mutableStateListOf<String>()

    private lateinit var fcmReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MensajesFirebaseTheme {
                // Mostrar mensajes acumulados
                Surface(modifier = Modifier.fillMaxSize()) {
                    PantallaMensajes(mensajes)
                }
            }
        }

        // Obtener token
        FirebaseMessaging.getInstance().token
            .addOnSuccessListener { Log.d("FCM_TOKEN", it) }
            .addOnFailureListener { Log.e("FCM_TOKEN", "Error al obtener token", it) }

        // Inicializar BroadcastReceiver
        fcmReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val title = intent?.getStringExtra("title")
                val body = intent?.getStringExtra("body")
                if (!title.isNullOrBlank() || !body.isNullOrBlank()) {
                    mensajes.add("${title ?: ""}: ${body ?: ""}")
                }
            }
        }

        // Registrar receiver
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(fcmReceiver, IntentFilter("FCM_MESSAGE"))
    }

    override fun onDestroy() {
        super.onDestroy()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(fcmReceiver)
    }
}

@Composable
fun PantallaMensajes(mensajes: List<String>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)
    ) {
        Text(
            "Mensajes recibidos:",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxSize()
        ) {
            items(mensajes) { mensaje ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Text(
                        text = mensaje,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
}
