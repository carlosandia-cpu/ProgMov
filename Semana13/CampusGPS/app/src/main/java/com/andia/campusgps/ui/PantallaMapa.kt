package com.andia.campusgps.ui

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.andia.campusgps.model.UbicacionUi
import com.andia.campusgps.ui.theme.*
import com.andia.campusgps.util.formatearDistancia
import com.andia.campusgps.viewmodel.MapaViewModel

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
    val categoriaSeleccionada by vm.categoriaSeleccionada.collectAsState()
    val radioSeleccionado by vm.radioSeleccionado.collectAsState()
    val radioMetros by vm.radioMetros.collectAsState()

    var esModoOscuro by rememberSaveable {
        mutableStateOf(false)
    }

    val fondoPantalla = if (esModoOscuro) Color(0xFF111C2D) else FondoApp
    val fondoPanel = if (esModoOscuro) Color(0xFF1D3049) else Blanco
    val fondoTarjeta = if (esModoOscuro) Color(0xFF2B4360) else AzulClaro
    val textoPrincipalModo = if (esModoOscuro) Color(0xFFF1F6FF) else TextoPrincipal
    val textoSecundarioModo = if (esModoOscuro) Color(0xFFB7C9DD) else TextoSecundario
    val verdeModo = if (esModoOscuro) Color(0xFF6DE8BF) else VerdeCampus
    val azulAcento = if (esModoOscuro) Color(0xFF5CA8FF) else AzulPrincipal
    val bordeModo = if (esModoOscuro) Color(0xFF47617F) else BordeSuave

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
        containerColor = fondoPantalla,
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
                radioMetros = radioMetros,
                esModoOscuro = esModoOscuro,
                onToggleModoOscuro = { esModoOscuro = !esModoOscuro },
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
                color = fondoPanel,
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
                                color = textoSecundarioModo,
                                letterSpacing = 1.sp
                            )

                            Text(
                                text = "Explora los alrededores",
                                fontSize = 19.sp,
                                fontWeight = FontWeight.Bold,
                                color = textoPrincipalModo
                            )
                        }

                        TextButton(
                            onClick = { mostrarLugares = true },
                            colors = ButtonDefaults.textButtonColors(
                                contentColor = azulAcento
                            )
                        ) {
                            Text(
                                "Ver lugares",
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
                                containerColor = fondoTarjeta
                            ),
                            border = BorderStroke(1.dp, bordeModo)
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
                                        color = textoPrincipalModo
                                    )

                                    Text(
                                        text = lugarCercano.lugar.categoria,
                                        fontSize = 12.sp,
                                        color = textoSecundarioModo
                                    )

                                    Text(
                                        text = textoDistancia,
                                        fontSize = 13.sp,
                                        color = verdeModo,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        Text(
                            text = "Obtén tu ubicación para descubrir el destino más cercano.",
                            color = textoSecundarioModo,
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
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AzulPrincipal,
                                contentColor = Blanco,
                                disabledContainerColor = bordeModo,
                                disabledContentColor = textoSecundarioModo
                            ),
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
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = textoPrincipalModo,
                                disabledContentColor = textoSecundarioModo
                            ),
                            border = BorderStroke(1.dp, bordeModo),
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
                                color = verdeModo,
                                fontSize = 11.sp
                            )
                        }

                        is UbicacionUi.Buscando -> {
                            LinearProgressIndicator(
                                modifier = Modifier.fillMaxWidth(),
                                color = azulAcento,
                                trackColor = bordeModo
                            )
                        }

                        is UbicacionUi.Error -> {
                            MensajeError(
                                error = actual,
                                esModoOscuro = esModoOscuro,
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
                                color = textoSecundarioModo
                            )
                        }
                    }
                }
            }
        }
    }

    // Panel deslizable de los destinos
    if (mostrarLugares) {
        ModalBottomSheet(
            onDismissRequest = {
                mostrarLugares = false
            },
            containerColor = fondoPantalla,
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
                    color = textoPrincipalModo,
                    modifier = Modifier.padding(
                        horizontal = 20.dp
                    )
                )

                Text(
                    text = "${lugares.size} destinos disponibles · Ordenados por distancia",
                    fontSize = 12.sp,
                    color = textoSecundarioModo,
                    modifier = Modifier.padding(
                        horizontal = 20.dp,
                        vertical = 4.dp
                    )
                )

                // Filtro por Categoría
                Text(
                    text = "Categoría",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoSecundarioModo,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 6.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vm.categorias) { cat ->
                        val seleccionada = cat == categoriaSeleccionada
                        FilterChip(
                            selected = seleccionada,
                            onClick = {
                                vm.seleccionarCategoria(cat)
                            },
                            label = {
                                Text(
                                    text = cat,
                                    fontSize = 13.sp,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = fondoTarjeta,
                                labelColor = textoPrincipalModo,
                                selectedContainerColor = AzulPrincipal,
                                selectedLabelColor = Blanco
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (seleccionada) azulAcento else bordeModo
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                // Filtro por Radio de Cercanía
                Text(
                    text = "Radio de cercanía",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = textoSecundarioModo,
                    modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 4.dp)
                )
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(vm.radios) { rad ->
                        val seleccionada = rad == radioSeleccionado
                        FilterChip(
                            selected = seleccionada,
                            onClick = {
                                vm.seleccionarRadio(rad)
                            },
                            label = {
                                Text(
                                    text = rad,
                                    fontSize = 13.sp,
                                    fontWeight = if (seleccionada) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = fondoTarjeta,
                                labelColor = textoPrincipalModo,
                                selectedContainerColor = AzulPrincipal,
                                selectedLabelColor = Blanco
                            ),
                            border = androidx.compose.foundation.BorderStroke(
                                width = 1.dp,
                                color = if (seleccionada) azulAcento else bordeModo
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                ListaLugares(
                    lugares = lugares,
                    seleccionado = seleccionado,
                    sinUbicacionParaRadio = radioMetros != null && estado !is UbicacionUi.Lista,
                    esModoOscuro = esModoOscuro,
                    onClick = { id ->
                        vm.seleccionar(id)
                        mostrarLugares = false
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 420.dp)
                )
            }
        }
    }
}
