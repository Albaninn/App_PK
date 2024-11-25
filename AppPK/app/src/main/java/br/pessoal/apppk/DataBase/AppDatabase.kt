package br.pessoal.apppk.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import br.pessoal.apppk.DataBase.UserProfile
import br.pessoal.apppk.DataBase.UserProfileDao
import br.pessoal.apppk.DataBase.WorkHourDao

@Database(entities = [UserProfile::class], version = 1, exportSchema = true)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userProfileDao(): UserProfileDao
    abstract fun workHourDao(): WorkHourDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .fallbackToDestructiveMigration() // Para lidar com a mudança na versão do banco
                    .build()
                INSTANCE = instance
                instance
            }
        }

    }
}
