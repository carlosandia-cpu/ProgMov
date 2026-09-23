package pe.tecsup.clinicasaludplus

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaAgendarCita(
    medico: Medico,
    onVolver: () -> Unit,
    onConfirmar: (String, String) -> Unit
) {
    var fechaElegida by remember { mutableStateOf<String?>(null) }
    var horaElegida by remember { mutableStateOf<String?>(null) }

    val fechas = remember {
        val localeEs = Locale.forLanguageTag("es-ES")
        val formatter = SimpleDateFormat("EEE d", localeEs)
        (0..2).map { offset ->
            val calendar = Calendar.getInstance().apply {
                add(Calendar.DAY_OF_YEAR, offset)
            }
            formatter.format(calendar.time)
                .replace(".", "")
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(localeEs) else it.toString() }
        }
    }
    val horas = listOf("9:00 a. m.", "10:30 a. m.", "3:00 p. m.")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Agendar cita") },
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
                text = "Cita con ${medico.nombre}",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(28.dp))
            Text("Selecciona una fecha", fontWeight = FontWeight.Bold)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(fechas) { fecha ->
                    FilterChip(
                        selected = fechaElegida == fecha,
                        onClick = { fechaElegida = fecha },
                        label = { Text(fecha) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))
            Text("Selecciona una hora", fontWeight = FontWeight.Bold)

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(horas) { hora ->
                    FilterChip(
                        selected = horaElegida == hora,
                        onClick = { horaElegida = hora },
                        label = { Text(hora) }
                    )
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    val fecha = fechaElegida
                    val hora = horaElegida
                    if (fecha != null && hora != null) {
                        onConfirmar(fecha, hora)
                    }
                },
                enabled = fechaElegida != null && horaElegida != null,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Confirmar cita")
            }
        }
    }
}