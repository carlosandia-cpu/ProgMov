package pe.tecsup.clinicasaludplus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@Composable
fun PantallaConfirmacion(
    medico: Medico,
    fecha: String,
    hora: String,
    onVerCitas: () -> Unit,
    onVolverInicio: () -> Unit
) {
    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("✓", style = MaterialTheme.typography.displayLarge)

            Spacer(Modifier.height(16.dp))

            Text(
                "¡Cita agendada!",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(12.dp))
            Text(medico.nombre)
            Text(medico.especialidad)
            Text("$fecha · $hora")

            Spacer(Modifier.height(28.dp))

            Button(onClick = onVerCitas) {
                Text("Ver mis citas")
            }

            TextButton(onClick = onVolverInicio) {
                Text("Volver al inicio")
            }
        }
    }
}