package br.pessoal.apppk.DataBase


import android.content.Context
import br.pessoal.apppk.DataBase.WorkHour
import br.pessoal.apppk.database.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

object DatabaseInitializer {
    fun populateWorkHoursIfEmpty(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            val workHourDao = AppDatabase.getDatabase(context).workHourDao()
            if (workHourDao.getAllWorkHours().isEmpty()) {
                workHourDao.insertWorkHour(WorkHour(startHour = 8, endHour = 12))
                workHourDao.insertWorkHour(WorkHour(startHour = 14, endHour = 18))
            }
        }
    }
}
