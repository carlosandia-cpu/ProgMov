package com.tecsup.lavacampus.ui.estudiante

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocalLaundryService
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.tecsup.lavacampus.model.Rol
import com.tecsup.lavacampus.ui.components.LavaButton
import com.tecsup.lavacampus.ui.components.LavaTextField
import com.tecsup.lavacampus.ui.theme.LavaTheme
import com.tecsup.lavacampus.viewmodel.SesionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    vm: SesionViewModel,
    onRegistro: () -> Unit,
    onIngreso: (Rol) -> Unit
) {
    val modoOscuro by vm.modoOscuro.collectAsState()
    val error by vm.error.collectAsState()
    val aviso by vm.aviso.collectAsState()

    var rolNombre by rememberSaveable { mutableStateOf(Rol.CLIENTE.name) }
    var correo by rememberSaveable { mutableStateOf("") }
    var clave by rememberSaveable { mutableStateOf("") }
    val rol = Rol.valueOf(rolNombre)

    // El botón y los colores cambian según el perfil elegido
    LavaTheme(rol = rol, modoOscuro = modoOscuro) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .systemBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.LocalLaundryService,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(72.dp)
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    "LavaCampus",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    "Inicia sesión para continuar",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                aviso?.let {
                    Spacer(Modifier.height(16.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = it,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }

                Spacer(Modifier.height(24.dp))

                val opciones = listOf(Rol.CLIENTE to "Estudiante", Rol.ADMIN to "Administrador")
                SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                    opciones.forEachIndexed { indice, (r, texto) ->
                        SegmentedButton(
                            selected = rol == r,
                            onClick = {
                                rolNombre = r.name
                                vm.limpiarError()
                            },
                            shape = SegmentedButtonDefaults.itemShape(indice, opciones.size),
                            colors = SegmentedButtonDefaults.colors(
                                activeContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                activeContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        ) {
                            Text(texto)
                        }
                    }
                }
                Spacer(Modifier.height(20.dp))

                LavaTextField(
                    valor = correo,
                    onCambio = { correo = it; vm.limpiarError() },
                    etiqueta = "Correo",
                    icono = Icons.Default.Email,
                    teclado = KeyboardType.Email,
                    hayError = error != null
                )
                Spacer(Modifier.height(12.dp))
                LavaTextField(
                    valor = clave,
                    onCambio = { clave = it; vm.limpiarError() },
                    etiqueta = "Contraseña",
                    icono = Icons.Default.Lock,
                    esClave = true,
                    hayError = error != null
                )

                error?.let {
                    Spacer(Modifier.height(10.dp))
                    Text(
                        text = it,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.fillMaxWidth()
                    )
                }

                Spacer(Modifier.height(24.dp))
                LavaButton(
                    texto = "Ingresar",
                    onClick = { vm.login(correo, clave, rol, onIngreso) }
                )

                // Solo los estudiantes se registran; el administrador es una cuenta fija
                if (rol == Rol.CLIENTE) {
                    Spacer(Modifier.height(8.dp))
                    TextButton(onClick = onRegistro) {
                        Text("¿No tienes cuenta? Regístrate")
                    }
                }
            }
        }
    }
}