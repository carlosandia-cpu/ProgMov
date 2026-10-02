package pe.tecsup.clinicasaludplus

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import pe.tecsup.clinicasaludplus.navigation.AppNavigation
import pe.tecsup.clinicasaludplus.ui.theme.ClinicaSaludPlusTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ClinicaSaludPlusTheme {
                AppNavigation()
            }
        }
    }
}