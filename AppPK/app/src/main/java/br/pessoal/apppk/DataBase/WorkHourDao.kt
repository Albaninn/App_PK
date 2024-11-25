package br.pessoal.apppk.DataBase

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorkHourDao {
    @Insert
    suspend fun insertWorkHour(workHour: WorkHour)

    @Query("SELECT * FROM work_hours")
    suspend fun getAllWorkHours(): List<WorkHour>
}
