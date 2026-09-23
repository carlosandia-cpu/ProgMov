package pe.tecsup.clinicasaludplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import pe.tecsup.clinicasaludplus.ui.theme.ClinicaSaludPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClinicaSaludPlusTheme {
                var medicoSeleccionado by remember {
                    mutableStateOf<Medico?>(null)
                }

                BackHandler(enabled = medicoSeleccionado != null) {
                    medicoSeleccionado = null
                }

                if (medicoSeleccionado == null) {
                    PantallaInicio(
                        onMedicoClick = { medico ->
                            medicoSeleccionado = medico
                        }
                    )
                } else {
                    PantallaPerfilMedico(
                        medico = medicoSeleccionado!!,
                        onVolver = {
                            medicoSeleccionado = null
                        }
                    )
                }
            }
        }
    }
}