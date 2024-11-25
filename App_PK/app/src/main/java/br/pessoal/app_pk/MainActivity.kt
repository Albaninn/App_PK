package br.pessoal.app_pk

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import br.pessoal.app_pk.screen.ListaBH
import br.pessoal.app_pk.screen.NovaHora
import br.pessoal.app_pk.ui.theme.App_PKTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            App_PKTheme {

            }
        }
    }
}

