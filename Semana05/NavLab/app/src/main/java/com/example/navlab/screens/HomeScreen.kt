package com.example.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.navlab.navigation.Screen

@Composable
fun HomeScreen(navController: NavController) {
    val scrollState = rememberScrollState()

    // Paleta de tonos morados para el encabezado y acentos
    val purpleHeaderDark = Color(0xFF4A148C)
    val purpleHeaderLight = Color(0xFF7B1FA2)
    val purpleAccent = Color(0xFF6A1B9A)
    val cardBgList = Color(0xFFF3E5F5)
    val cardBgProfile = Color(0xFFEDE7F6)

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
        ) {
            // Encabezado destacado en tonos morados
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(
                        RoundedCornerShape(
                            bottomStart = 32.dp,
                            bottomEnd = 32.dp,
                        ),
                    )
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                purpleHeaderDark,
                                purpleHeaderLight,
                            ),
                        ),
                    )
                    .padding(horizontal = 24.dp, vertical = 40.dp),
            ) {
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start,
                ) {
                    // Badge inicial
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "NL",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    Text(
                        text = "¡Bienvenido a NavLab!",
                        fontSize = 16.sp,
                        color = Color.White.copy(alpha = 0.85f),
                        fontWeight = FontWeight.Medium,
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Pantalla Tecsup",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Selecciona una de las opciones a continuación para comenzar a navegar.",
                        fontSize = 14.sp,
                        color = Color.White.copy(alpha = 0.75f),
                        lineHeight = 20.sp,
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Sección con las dos tarjetas grandes y claras
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp),
            ) {
                Text(
                    text = "Acciones disponibles",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onBackground,
                )

                // Tarjeta 1: Ver lista de elementos
                ElevatedCard(
                    onClick = { navController.navigate(Screen.List.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = cardBgList,
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Badge con ícono textual
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(purpleAccent),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "≡",
                                color = Color.White,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "Ver lista de elementos",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = purpleHeaderDark,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Explora y gestiona todos los ítems registrados.",
                                fontSize = 13.sp,
                                color = Color.DarkGray,
                                lineHeight = 18.sp,
                            )
                        }

                        Text(
                            text = "→",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = purpleAccent,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }

                // Tarjeta 2: Mi perfil
                ElevatedCard(
                    onClick = { navController.navigate(Screen.Profile.route) },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.elevatedCardColors(
                        containerColor = cardBgProfile,
                    ),
                    elevation = CardDefaults.elevatedCardElevation(
                        defaultElevation = 4.dp,
                    ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Badge con ícono textual
                        Box(
                            modifier = Modifier
                                .size(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(purpleAccent),
                            contentAlignment = Alignment.Center,
                        ) {
                            Text(
                                text = "👤",
                                color = Color.White,
                                fontSize = 22.sp,
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f),
                        ) {
                            Text(
                                text = "Mi perfil",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = purpleHeaderDark,
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Accede a tu información personal y del usuario.",
                                fontSize = 13.sp,
                                color = Color.DarkGray,
                                lineHeight = 18.sp,
                            )
                        }

                        Text(
                            text = "→",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = purpleAccent,
                            modifier = Modifier.padding(start = 8.dp),
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}
