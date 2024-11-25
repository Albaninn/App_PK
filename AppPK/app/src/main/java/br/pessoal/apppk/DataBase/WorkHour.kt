package br.pessoal.apppk.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "work_hours")
data class WorkHour(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val startHour: Int,
    val endHour: Int
)
