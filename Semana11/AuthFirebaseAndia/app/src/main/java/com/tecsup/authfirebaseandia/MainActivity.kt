package com.tecsup.authfirebaseandia

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.tecsup.authfirebaseandia.ui.theme.AuthFirebaseAndiaTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AuthFirebaseAndiaTheme {
                AuthApp()
            }
        }
    }
}