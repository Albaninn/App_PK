package br.pessoal.apppk.DataBase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import br.pessoal.apppk.R
import br.pessoal.apppk.database.AppDatabase
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        db = AppDatabase.getDatabase(this)

        // Exemplo de sincronização após login
        lifecycleScope.launch {
            FirebaseSyncManager.syncRoomToFirestore(db)
            FirebaseSyncManager.syncFirestoreToRoom(db)
        }
    }
}
