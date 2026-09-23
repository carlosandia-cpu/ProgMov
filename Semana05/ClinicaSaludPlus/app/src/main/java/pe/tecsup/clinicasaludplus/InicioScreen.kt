package pe.tecsup.clinicasaludplus

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio(onMedicoClick: (Medico) -> Unit) {
    var especialidad by remember { mutableStateOf("Todas") }

    val especialidades = listOf(
        "Todas", "Cardiología", "Pediatría", "Dermatología"
    )

    val medicosVisibles = medicos.filter { medico ->
        especialidad == "Todas" || medico.especialidad == especialidad
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Clínica Salud+") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Hola, Juan 👋",
                style = MaterialTheme.typography.titleLarge
            )

            Text(
                text = "Especialidades",
                modifier = Modifier.padding(top = 20.dp),
                fontWeight = FontWeight.Bold
            )

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(especialidades) { opcion ->
                    FilterChip(
                        selected = especialidad == opcion,
                        onClick = { especialidad = opcion },
                        label = { Text(opcion) }
                    )
                }
            }

            Text(
                text = "Médicos disponibles",
                modifier = Modifier.padding(vertical = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(medicosVisibles) { medico ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onMedicoClick(medico) }
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(medico.nombre, fontWeight = FontWeight.Bold)
                            Text(medico.especialidad)
                            Text("★ ${medico.calificacion}")
                        }
                    }
                }
            }
        }
    }
}