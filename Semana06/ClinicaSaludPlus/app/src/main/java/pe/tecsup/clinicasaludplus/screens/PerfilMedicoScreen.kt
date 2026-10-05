package pe.tecsup.clinicasaludplus

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaPerfilMedico(
    medico: Medico,
    onVolver: () -> Unit,
    onAgendar: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Perfil del médico") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("← Volver")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp)
        ) {
            Text(
                text = medico.nombre,
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))
            Text(medico.especialidad)
            Text("★ ${medico.calificacion}")

            Spacer(modifier = Modifier.height(24.dp))
            Text(
                "Atención especializada en " +
                        "${medico.especialidad.lowercase()}. " +
                        "Puedes reservar una consulta en los horarios disponibles."
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = onAgendar,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Agendar cita")
            }
        }
    }
}