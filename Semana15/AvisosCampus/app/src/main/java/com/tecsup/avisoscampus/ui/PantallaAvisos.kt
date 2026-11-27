package com.tecsup.avisoscampus.ui

import android.Manifest
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.tecsup.avisoscampus.model.PermisoUi
import com.tecsup.avisoscampus.notificacion.Notificador
import com.tecsup.avisoscampus.viewmodel.AvisosViewModel
import android.content.Intent
import android.content.IntentFilter
import androidx.core.content.ContextCompat
import com.tecsup.avisoscampus.receiver.ModoAvionReceiver

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAvisos(vm: AvisosViewModel) {

    val contexto = LocalContext.current
    val permiso by vm.permiso.collectAsState()
    val faltan by vm.faltan.collectAsState()
    val escuchando by vm.escuchandoAvion.collectAsState()

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { concedido ->
        vm.permisoRespondido(concedido)
    }

    LaunchedEffect(Unit) {
        if (Notificador.tienePermiso(contexto)) {
            vm.permisoRespondido(true)
        }
    }

    LaunchedEffect(Unit) {
        vm.eventos.collect { aviso ->
            val mostrado = Notificador.mostrar(contexto, aviso)
            // TODO Paso 8: manejar permiso denegado
        }
    }

    DisposableEffect(escuchando) {

        if (escuchando) {

            val receptor = ModoAvionReceiver { activo ->
                vm.alCambiarModoAvion(activo)
            }

            ContextCompat.registerReceiver(
                contexto,
                receptor,
                IntentFilter(Intent.ACTION_AIRPLANE_MODE_CHANGED),
                ContextCompat.RECEIVER_NOT_EXPORTED
            )

            onDispose {
                contexto.unregisterReceiver(receptor)
            }

        } else {
            onDispose { }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AvisosCampus") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {

            TarjetaAvisos("Permiso de notificaciones") {

                when (permiso) {
                    PermisoUi.SinPedir -> Text(
                        text = "Sin pedir",
                        color = Color.Gray
                    )

                    PermisoUi.Concedido -> Text(
                        text = "Permitido",
                        color = Color(0xFF2E7D32)
                    )

                    PermisoUi.Denegado -> Text(
                        text = "Denegado",
                        color = Color(0xFFC62828)
                    )
                }

                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= 33) {
                            lanzador.launch(
                                Manifest.permission.POST_NOTIFICATIONS
                            )
                        } else {
                            vm.permisoRespondido(true)
                        }
                    }
                ) {
                    Text("Permitir notificaciones")
                }
            }

            TarjetaAvisos("Avisos") {

                Button(onClick = { vm.avisarAhora() }) {
                    Text("Mostrar aviso")
                }
            }

            TarjetaAvisos("Recordatorio") {

                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { vm.recordarEn() },
                        enabled = faltan == null
                    ) {
                        Text("Recordar en 10 s")
                    }

                    OutlinedButton(
                        onClick = { vm.cancelarRecordatorio() },
                        enabled = faltan != null
                    ) {
                        Text("Cancelar")
                    }
                }

                faltan?.let { segundos ->
                    Text("Faltan $segundos s")
                }
            }

            TarjetaAvisos("Receptor de anuncios") {

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Escuchar el modo avión")

                    Switch(
                        checked = escuchando,
                        onCheckedChange = {
                            vm.cambiarEscucha(it)
                        }
                    )
                }
            }

            TarjetaAvisos("Historial") {

                // TODO Paso 7: mostrar historial

                Text("Sin avisos todavía")
            }
        }
    }
}

@Composable
fun TarjetaAvisos(
    titulo: String,
    contenido: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )

            contenido()
        }
    }
}