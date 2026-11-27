package com.tecsup.avisoscampus.notificacion

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.MainActivity
import com.tecsup.avisoscampus.R
import com.tecsup.avisoscampus.model.Aviso

object Notificador {

    const val CANAL_AVISOS = "avisos"
    const val CANAL_URGENTES = "urgentes"

    fun crearCanal(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val manager = context.getSystemService(
                NotificationManager::class.java
            )

            val canalAvisos = NotificationChannel(
                CANAL_AVISOS,
                "Avisos",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(canalAvisos)

            val canalUrgentes = NotificationChannel(
                CANAL_URGENTES,
                "Urgentes",
                NotificationManager.IMPORTANCE_HIGH
            )
            manager.createNotificationChannel(canalUrgentes)
        }
    }

    fun tienePermiso(context: Context): Boolean {
        if (Build.VERSION.SDK_INT < 33) {
            return true
        }

        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.POST_NOTIFICATIONS
        ) == PackageManager.PERMISSION_GRANTED
    }

    @SuppressLint("MissingPermission")
    fun mostrar(context: Context, aviso: Aviso): Boolean {

        if (!tienePermiso(context)) {
            return false
        }

        val intent = Intent(
            context,
            MainActivity::class.java
        )

        val pendiente = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE
        )

        val canalId = if (aviso.esUrgente) CANAL_URGENTES else CANAL_AVISOS
        val prioridad = if (aviso.esUrgente) NotificationCompat.PRIORITY_HIGH else NotificationCompat.PRIORITY_DEFAULT

        val notificacion = NotificationCompat.Builder(context, canalId)
            .setSmallIcon(R.drawable.ic_notificacion)
            .setContentTitle(aviso.titulo)
            .setContentText(aviso.texto)
            .setPriority(prioridad)
            .setContentIntent(pendiente)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context)
            .notify(aviso.id, notificacion)

        return true
    }
}