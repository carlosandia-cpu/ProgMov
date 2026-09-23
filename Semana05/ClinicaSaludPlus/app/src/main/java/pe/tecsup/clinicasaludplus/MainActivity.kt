package pe.tecsup.clinicasaludplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import pe.tecsup.clinicasaludplus.ui.theme.ClinicaSaludPlusTheme

data class Medico(
    val nombre: String,
    val especialidad: String,
    val calificacion: String
)

private val medicos = listOf(
    Medico("Dra. Ana Torres", "Cardiología", "4.9"),
    Medico("Dr. Luis Vega", "Pediatría", "4.7"),
    Medico("Dra. Rosa Díaz", "Dermatología", "4.8")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ClinicaSaludPlusTheme {
                PantallaInicio()
            }
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun PantallaInicio() {
    var especialidad by remember { mutableStateOf("Todas") }
    val especialidades = listOf("Todas", "Cardiología", "Pediatría", "Dermatología")
    val visibles = medicos.filter {
        especialidad == "Todas" || it.especialidad == especialidad
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
            Text("Hola, Juan 👋", style = MaterialTheme.typography.titleLarge)
            Text(
                "Especialidades",
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
                "Médicos disponibles",
                modifier = Modifier.padding(vertical = 12.dp),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                items(visibles) { medico ->
                    Card(modifier = Modifier.fillMaxWidth()) {
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