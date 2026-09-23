package pe.tecsup.clinicasaludplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import pe.tecsup.clinicasaludplus.ui.theme.ClinicaSaludPlusTheme

private enum class Pantalla {
    INICIO, PERFIL_MEDICO, AGENDAR, CONFIRMACION,
    MIS_CITAS, HISTORIAL, PERFIL_PACIENTE
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

                val drawer = rememberDrawerState(DrawerValue.Closed)
                val scope = rememberCoroutineScope()

                BackHandler(enabled = pantalla != Pantalla.INICIO || drawer.isOpen) {
                    if (drawer.isOpen) {
                        scope.launch { drawer.close() }
                    } else {
                        pantalla = when (pantalla) {
                            Pantalla.AGENDAR -> Pantalla.PERFIL_MEDICO
                            else -> Pantalla.INICIO
                        }
                    }
                }

                ModalNavigationDrawer(
                    drawerState = drawer,
                    drawerContent = {
                        ModalDrawerSheet {
                            Text(
                                text = "Juan Pérez\nPaciente",
                                modifier = Modifier.padding(24.dp)
                            )

                            val destinos = listOf(
                                "Inicio" to Pantalla.INICIO,
                                "Mis citas" to Pantalla.MIS_CITAS,
                                "Historial médico" to Pantalla.HISTORIAL,
                                "Perfil" to Pantalla.PERFIL_PACIENTE
                            )

                            destinos.forEach { (nombre, destino) ->
                                NavigationDrawerItem(
                                    label = { Text(nombre) },
                                    selected = pantalla == destino,
                                    onClick = {
                                        pantalla = destino
                                        scope.launch { drawer.close() }
                                    },
                                    modifier = Modifier.padding(horizontal = 12.dp)
                                )
                            }
                        }
                    }
                ) {
                    val abrirMenu = {
                        scope.launch { drawer.open() }
                        Unit
                    }

                    when (pantalla) {
                        Pantalla.INICIO -> {
                            PantallaInicio(
                                onAbrirMenu = abrirMenu,
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
                                    onVolver = {
                                        pantalla = Pantalla.PERFIL_MEDICO
                                    },
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

                        Pantalla.MIS_CITAS -> {
                            PantallaSeccion(
                                titulo = "Mis citas",
                                contenido = "Aquí aparecerán tus citas agendadas.",
                                onAbrirMenu = abrirMenu
                            )
                        }

                        Pantalla.HISTORIAL -> {
                            PantallaSeccion(
                                titulo = "Historial médico",
                                contenido = "Aquí podrás consultar las citas completadas.",
                                onAbrirMenu = abrirMenu
                            )
                        }

                        Pantalla.PERFIL_PACIENTE -> {
                            PantallaSeccion(
                                titulo = "Mi perfil",
                                contenido = "Juan Pérez\nPaciente de Clínica Salud+",
                                onAbrirMenu = abrirMenu
                            )
                        }
                    }
                }
            }
        }
    }
}