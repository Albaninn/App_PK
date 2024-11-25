package br.pessoal.apppk

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import br.pessoal.apppk.DataBase.UserProfileDao
import br.pessoal.apppk.database.AppDatabase
import br.pessoal.apppk.screen.BancoHorasScreen
import br.pessoal.apppk.screen.BaseScreen
import br.pessoal.apppk.screen.inicio_perfil.LoginScreen
import br.pessoal.apppk.screen.inicio_perfil.ProfileScreen
import br.pessoal.apppk.screen.inicio_perfil.RegistrationScreen
import br.pessoal.apppk.ui.theme.AppPKTheme

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavigationComponent(
    navController: NavHostController,
    isDarkTheme: Boolean,
    onThemeChange: (Boolean) -> Unit,
    userProfileDao: UserProfileDao
) {
    NavHost(navController = navController, startDestination = "registration_screen") {
        composable("registration_screen") { RegistrationScreen(
            navController,
            db = TODO()
        ) }
        composable("login_screen") { LoginScreen(
            navController,
            db = TODO()
        ) }
        composable(
            route = "second_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            BaseScreen(
                navController = navController,
                username = username,
                isDarkTheme = isDarkTheme,
                onThemeChange = onThemeChange,
                db = userProfileDao // Passar DAO para BaseScreen
            )
        }
        composable("profile_screen/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            ProfileScreen(navController, username)
        }
//        composable("tela_1") { AdmScreen(navController) }
        composable(
            route = "tela_2/{username}",
            arguments = listOf(navArgument("username") { type = NavType.StringType })
        ) { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: "Desconhecido"
            BancoHorasScreen(navController, username)
        }
//        composable("tela_3") { CheckListScreen(navController) }
//        // Nova tela PreUtilizacaoVeiculosScreen, para inserir Placa e Modelo
//        composable("pre_utilizacao_de_veiculos") {
//            VerificacaoVeiculosScreen(navController)
//        }
//        // Tela de Utilização de Veículos que agora recebe os parâmetros Placa e Modelo
//        composable(
//            route = "utilizacao_de_veiculos/{placa}/{modelo}",
//            arguments = listOf(
//                navArgument("placa") { type = NavType.StringType },
//                navArgument("modelo") { type = NavType.StringType }
//            )
//        ) { backStackEntry ->
//            val placa = backStackEntry.arguments?.getString("placa") ?: ""
//            val modelo = backStackEntry.arguments?.getString("modelo") ?: ""
//            VistoriaVeiculosScreen(navController, placa, modelo)
//        }
    }
}

@Preview
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun AppPerkons() {
    val isSystemDarkTheme = isSystemInDarkTheme()
    var isDarkTheme by remember { mutableStateOf(isSystemDarkTheme) }

    // Inicializa o banco de dados
    val context = LocalContext.current
    val db = remember { AppDatabase.getDatabase(context) } // Função para obter o banco
    val userProfileDao = db.userProfileDao() // Instância do DAO

    AppPKTheme(darkTheme = isDarkTheme) {
        val navController = rememberNavController()
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NavigationComponent(
                navController = navController,
                isDarkTheme = isDarkTheme,
                onThemeChange = { isDarkTheme = it },
                userProfileDao = userProfileDao // Passa o DAO aqui
            )
        }
    }
}
