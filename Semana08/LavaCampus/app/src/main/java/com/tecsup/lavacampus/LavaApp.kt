package com.tecsup.lavacampus

import android.app.Application
import com.tecsup.lavacampus.data.local.DatosIniciales
import com.tecsup.lavacampus.data.local.LavaDatabase
import com.tecsup.lavacampus.data.repository.LavaRepository
import com.tecsup.lavacampus.data.repository.LavaRepositoryRoom
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class LavaApp : Application() {

    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val database: LavaDatabase by lazy { LavaDatabase.obtener(this) }

    val repository: LavaRepository by lazy { LavaRepositoryRoom(database) }

    override fun onCreate() {
        super.onCreate()
        appScope.launch { DatosIniciales.sembrar(database) }
    }
}