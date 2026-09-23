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

private enum class Pantalla {
    INICIO, PERFIL_MEDICO, AGENDAR, CONFIRMACION
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClinicaSaludPlusTheme {
                var pantalla by remember { mutableStateOf(Pantalla.INICIO) }
                var medicoSeleccionado by remember { mutableStateOf<Medico?>(null) }
                var fechaConfirmada by remember { mutableStateOf("") }
                var horaConfirmada by remember { mutableStateOf("") }

                BackHandler(enabled = pantalla != Pantalla.INICIO) {
                    pantalla = when (pantalla) {
                        Pantalla.PERFIL_MEDICO -> Pantalla.INICIO
                        Pantalla.AGENDAR -> Pantalla.PERFIL_MEDICO
                        Pantalla.CONFIRMACION -> Pantalla.INICIO
                        Pantalla.INICIO -> Pantalla.INICIO
                    }
                }

                when (pantalla) {
                    Pantalla.INICIO -> {
                        PantallaInicio(
                            onMedicoClick = { medico ->
                                medicoSeleccionado = medico
                                pantalla = Pantalla.PERFIL_MEDICO
                            }
                        )
                    }

                    Pantalla.PERFIL_MEDICO -> {
                        medicoSeleccionado?.let { medico ->
                            PantallaPerfilMedico(
                                medico = medico,
                                onVolver = { pantalla = Pantalla.INICIO },
                                onAgendar = { pantalla = Pantalla.AGENDAR }
                            )
                        }
                    }

                    Pantalla.AGENDAR -> {
                        medicoSeleccionado?.let { medico ->
                            PantallaAgendarCita(
                                medico = medico,
                                onVolver = { pantalla = Pantalla.PERFIL_MEDICO },
                                onConfirmar = { fecha, hora ->
                                    fechaConfirmada = fecha
                                    horaConfirmada = hora
                                    pantalla = Pantalla.CONFIRMACION
                                }
                            )
                        }
                    }

                    Pantalla.CONFIRMACION -> {
                        medicoSeleccionado?.let { medico ->
                            PantallaConfirmacion(
                                medico = medico,
                                fecha = fechaConfirmada,
                                hora = horaConfirmada,
                                onVolverInicio = {
                                    pantalla = Pantalla.INICIO
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}