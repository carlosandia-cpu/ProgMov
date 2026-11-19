
package com.andia.campusgps

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

import com.andia.campusgps.data.LugaresRepositoryLocal
import com.andia.campusgps.data.UbicacionRepositoryGoogle
import com.andia.campusgps.ui.PantallaMapa
import com.andia.campusgps.ui.theme.CampusGPSTheme
import com.andia.campusgps.viewmodel.CampusViewModelFactory
import com.andia.campusgps.viewmodel.MapaViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val ubicacionRepo = UbicacionRepositoryGoogle(
            applicationContext
        )

        val lugaresRepo = LugaresRepositoryLocal()

        val factory = CampusViewModelFactory(
            ubicacionRepo = ubicacionRepo,
            lugaresRepo = lugaresRepo
        )

        setContent {
            CampusGPSTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val vm: MapaViewModel = viewModel(
                        factory = factory
                    )

                    PantallaMapa(vm = vm)
                }
            }
        }
    }
}
