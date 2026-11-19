
package com.andia.centrotareas

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import com.andia.centrotareas.data.TareasRepositoryLocal
import com.andia.centrotareas.ui.PantallaTareas
import com.andia.centrotareas.ui.theme.CentroTareasTheme
import com.andia.centrotareas.viewmodel.TareasViewModel
import com.andia.centrotareas.viewmodel.TareasViewModelFactory

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            CentroTareasTheme {

                val tareasViewModel: TareasViewModel =
                    viewModel(
                        factory = TareasViewModelFactory(
                            TareasRepositoryLocal()
                        )
                    )

                PantallaTareas(
                    viewModel = tareasViewModel
                )
            }
        }
    }
}
