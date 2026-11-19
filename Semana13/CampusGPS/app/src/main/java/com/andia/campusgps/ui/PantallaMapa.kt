
package com.andia.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.ui.theme.*
import com.andia.campusgps.util.formatearDistancia
import com.andia.campusgps.viewmodel.MapaViewModel

@Composable
private fun PantallaBienvenida(
    onComenzar: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(
                        AzulNoche,
                        AzulPrincipal,
                        AzulNoche
                    )
                )
            )
            .padding(28.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "📍",
                fontSize = 78.sp
            )

            Text(
                text = "CampusGPS",
                color = Blanco,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 38.sp
            )

            Text(
                text = "Tu campus, más cerca",
                color = AzulSuave,
                fontSize = 20.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(12.dp))

            Text(
                text = "Explora, descubre y encuentra los lugares de tu campus desde un solo mapa.",
                color = Blanco,
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                lineHeight = 23.sp
            )
        }

        Button(
            onClick = onComenzar,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Blanco,
                contentColor = AzulNoche
            ),
            shape = RoundedCornerShape(20.dp)
        ) {
            Text(
                text = "Comenzar  →",
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaMapa(vm: MapaViewModel) {

    var mostrarInicio by rememberSaveable {
        mutableStateOf(true)
    }

    if (mostrarInicio) {
        PantallaBienvenida(
            onComenzar = { mostrarInicio = false }
        )
        return
    }

    val estado by vm.estado.collectAsState()
    val lugares by vm.lugares.collectAsState()
    val seleccionado by vm.seleccionado.collectAsState()
    val masCercano by vm.masCercano.collectAsState()

    val contexto = LocalContext.current

    var mostrarLugares by rememberSaveable {
        mutableStateOf(false)
    }

    val lanzador = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            vm.buscar()
        } else {
            vm.permisoDenegado()
        }
    }

    fun tienePermiso(): Boolean {
        val fino = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val aproximado = ContextCompat.checkSelfPermission(
            contexto,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        return fino || aproximado
    }

    fun obtenerUbicacion() {
        if (tienePermiso()) {
            vm.buscar()
        } else {
            lanzador.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    fun abrirAjustes() {
        val intent = Intent(
            Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        ).apply {
            data = Uri.fromParts(
                "package",
                contexto.packageName,
                null
            )
        }
        contexto.startActivity(intent)
    }

    val lugarCercano = lugares.firstOrNull {
        it.lugar.id == masCercano?.id
    }

    val textoDistancia = lugarCercano?.metros?.let {
        formatearDistancia(it)
    } ?: "Sin calcular"

    Scaffold(
        containerColor = FondoApp,
        topBar = {
            Surface(
                color = AzulNoche,
                shadowElevation = 6.dp
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(
                            horizontal = 18.dp,
                            vertical = 13.dp
                        ),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = AzulPrincipal
                    ) {
                        Box(
                            modifier = Modifier.size(44.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "⌖",
                                fontSize = 29.sp,
                                color = Blanco
                            )
                        }
                    }

                    Column(Modifier.weight(1f)) {
                        Text(
                            text = "CampusGPS",
                            color = Blanco,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 21.sp
                        )

                        Text(
                            text = "Explora tu campus",
                            color = AzulSuave,
                            fontSize = 11.sp
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(50),
                        color = AzulPrincipal.copy(alpha = 0.35f)
                    ) {
                        Text(
                            text = "5 lugares",
                            modifier = Modifier.padding(
                                horizontal = 11.dp,
                                vertical = 7.dp
                            ),
                            color = Blanco,
                            fontSize = 11.sp
                        )
                    }
                }
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

            // Mapa principal
            MapaUbicacion(
                estado = estado,
                lugares = lugares,
                seleccionado = seleccionado,
                masCercano = masCercano,
                onSeleccionar = { id ->
                    vm.seleccionar(id)
                },
                onUbicacion = {
                    obtenerUbicacion()
                },
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            )

            // Panel inferior
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = Blanco,
                shape = RoundedCornerShape(
                    topStart = 28.dp,
                    topEnd = 28.dp
                ),
                shadowElevation = 14.dp
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(11.dp)
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text(
                                text = "DESTINO MÁS CERCANO",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoSecundario,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "Explora los alrededores",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextoPrincipal
                            )
                        }

                        TextButton(
                            onClick = {
                                mostrarLugares = true
                            }
                        ) {
                            Text(
                                "Ver los 5",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // Tarjeta del más cercano
                    if (lugarCercano != null) {
                        Card(
                            onClick = {
                                vm.seleccionar(
                                    lugarCercano.lugar.id
                                )
                            },
                            shape = RoundedCornerShape(18.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = AzulClaro
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(9.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                LugarImagen(
                                    lugar = lugarCercano.lugar,
                                    modifier = Modifier.size(64.dp)
                                )

                                Column(Modifier.weight(1f)) {
                                    Text(
                                        text = lugarCercano.lugar.nombre,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = TextoPrincipal
                                    )

                                    Text(
                                        text = lugarCercano.lugar.categoria,
                                        fontSize = 12.sp,
                                        color = TextoSecundario
                                    )

                                    Text(
                                        text = textoDistancia,
                                        fontSize = 13.sp,
                                        color = VerdeCampus,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Obtén tu ubicación para descubrir el destino más cercano.",
                            color = TextoSecundario,
                            fontSize = 13.sp
                        )
                    }

                    // Botones principales
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {

                        Button(
                            onClick = {
                                obtenerUbicacion()
                            },
                            enabled = estado !is UbicacionUi.Buscando,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Text(
                                text = if (estado is UbicacionUi.Buscando)
                                    "Buscando..."
                                else
                                    "Mi ubicación",
                                fontSize = 12.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                masCercano?.let {
                                    vm.seleccionar(it.id)
                                }
                            },
                            enabled = masCercano != null,
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(14.dp),
                            contentPadding = PaddingValues(
                                horizontal = 8.dp,
                                vertical = 12.dp
                            )
                        ) {
                            Text(
                                text = "Ir al más cercano",
                                fontSize = 12.sp,
                                maxLines = 1
                            )
                        }
                    }

                    // Indicador de estado
                    when (val actual = estado) {

                        is UbicacionUi.Lista -> {
                            Text(
                                text = "Ubicación actualizada correctamente",
                                color = VerdeCampus,
                                fontSize = 11.sp
                            )
                        }

                        is UbicacionUi.Buscando -> {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        is UbicacionUi.Error -> {
                            MensajeError(
                                error = actual,
                                onAbrirAjustes = {
                                    abrirAjustes()
                                },
                                onReintentar = {
                                    obtenerUbicacion()
                                }
                            )
                        }

                        else -> {
                            Text(
                                text = "Pulsa Mi ubicación para comenzar",
                                fontSize = 11.sp,
                                color = TextoSecundario
                            )
                        }
                    }
                }
            }
        }
    }

    // Panel deslizable de los cinco destinos
    if (mostrarLugares) {
        ModalBottomSheet(
            onDismissRequest = {
                mostrarLugares = false
            },
            containerColor = FondoApp,
            sheetState = rememberModalBottomSheetState(
                skipPartiallyExpanded = true
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
            ) {
                Text(
                    text = "Lugares del campus",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = TextoPrincipal,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )

                Text(
                    text = "5 destinos disponibles · Ordenados por distancia",
                    fontSize = 12.sp,
                    color = TextoSecundario,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 6.dp
                    )
                )

                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    onClick = { id ->
                        vm.seleccionar(id)
                        mostrarLugares = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 450.dp)
                )
            }
        }
    }
}
