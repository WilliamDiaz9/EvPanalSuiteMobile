package com.duoc.evpanalsuitemobile.util

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat

// Id del canal donde llegan los avisos de tickets
private const val CHANNEL_ID = "tickets"

// Crea el canal de notificaciones (Android 8 en adelante lo exige)
fun createNotificationChannel(context: Context) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            CHANNEL_ID,
            "Avisos de tickets",
            NotificationManager.IMPORTANCE_DEFAULT
        )
        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }
}

// Muestra una notificación en la barra del teléfono
fun showTicketNotification(context: Context, title: String, text: String) {
    // En Android 13 en adelante hay que tener el permiso concedido
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        val granted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
        if (!granted) return
    }

    val notification = NotificationCompat.Builder(context, CHANNEL_ID)
        .setSmallIcon(android.R.drawable.ic_dialog_info)
        .setContentTitle(title)
        .setContentText(text)
        .setAutoCancel(true)
        .build()

    val manager = context.getSystemService(NotificationManager::class.java)
    // El id cambia cada vez para que no se reemplace la notificación anterior
    manager.notify(System.currentTimeMillis().toInt(), notification)
}
