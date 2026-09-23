package com.example.navlabia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.navlabia.navigation.AppNavigation
import com.example.navlabia.ui.theme.NavLabIATheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            NavLabIATheme {
                AppNavigation()
            }
        }
    }
}
