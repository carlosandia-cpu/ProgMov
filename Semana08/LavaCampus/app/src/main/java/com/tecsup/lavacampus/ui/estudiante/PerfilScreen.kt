package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.data.local.entity.Usuario
import com.tecsup.lavacampus.ui.components.AvatarIniciales
import com.tecsup.lavacampus.ui.components.FilaSwitch
import com.tecsup.lavacampus.viewmodel.PerfilViewModel
import com.tecsup.lavacampus.viewmodel.SesionViewModel
import java.util.Locale

@Composable
fun PerfilScreen(
    usuario: Usuario,
    vm: PerfilViewModel,
    sesionVm: SesionViewModel,
    modifier: Modifier = Modifier,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit
) {
    val activos by vm.activos.collectAsState()
    val totales by vm.totales.collectAsState()
    val kilos by vm.kilosLavados.collectAsState()
    val modoOscuro by sesionVm.modoOscuro.collectAsState()
    val notificaciones by sesionVm.notificaciones.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
            AvatarIniciales(nombre = usuario.nombre, tamano = 80.dp)
            Text(
                usuario.nombre,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp)
            )
            Text(
                "${usuario.codigo} · ${usuario.correo}",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(activos.toString(), "Pedidos activos", Modifier.weight(1f))
            Contador(totales.toString(), "Pedidos totales", Modifier.weight(1f))
            Contador(String.format(Locale.US, "%.1f", kilos), "Kilos lavados", Modifier.weight(1f))
        }

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                FilaSwitch(
                    titulo = "Modo oscuro",
                    descripcion = "Cambia los colores de toda la app",
                    marcado = modoOscuro,
                    onCambio = sesionVm::cambiarModoOscuro
                )
                FilaSwitch(
                    titulo = "Avisos",
                    descripcion = "Te avisamos cuando tu ropa esté lista",
                    marcado = notificaciones,
                    onCambio = sesionVm::cambiarNotificaciones
                )
            }
        }

        OutlinedButton(onClick = onCambiarPerfil, modifier = Modifier.fillMaxWidth()) {
            Text("Cambiar de perfil")
        }
        OutlinedButton(
            onClick = onCerrarSesion,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error)
        ) {
            Text("Cerrar sesión")
        }
    }
}

@Composable
private fun Contador(valor: String, etiqueta: String, modifier: Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(
                valor,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(etiqueta, style = MaterialTheme.typography.labelMedium)
        }
    }
}