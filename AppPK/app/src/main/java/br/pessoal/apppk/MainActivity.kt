package br.pessoal.apppk

//import android.os.Bundle
//import androidx.activity.ComponentActivity
//import androidx.activity.compose.setContent
//import androidx.activity.enableEdgeToEdge
//
//class MainActivity : ComponentActivity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
//        setContent {
//            AppPerkons()
//        }
//    }
//}

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.pessoal.apppk.DataBase.UserProfile
import br.pessoal.apppk.DataBase.UserProfileDao
import br.pessoal.apppk.database.AppDatabase
import com.google.firebase.FirebaseApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = AppDatabase.getDatabase(this)
        val userDao = database.userProfileDao()

        // Exemplo de inserção inicial
        CoroutineScope(Dispatchers.IO).launch {
            userDao.insertUser(
                UserProfile(
                    username = "example_user",
                    fullName = "Example User",
                    password = "password123",
                    email = "example@example.com",
                    accessLevel = "user"
                )
            )
        }

        FirebaseApp.initializeApp(this)

        setContent {
            AppPerkons()
        }
    }
}