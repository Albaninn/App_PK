package br.pessoal.apppk.DataBase

import androidx.room.Entity
import androidx.room.PrimaryKey

//val userCredentials = mutableMapOf(
//    "l.serenato" to UserProfile("Lucas Albano Ribas Serenato", "1234", "lk.serenato@example.com","adm"),
//    "lorenna.j" to UserProfile("Lorenna Judit", "qwe123", "lohve@example.com","user"),
//    "cesar.a" to UserProfile("Cesar Augusto Serenato", "senha", "cesar@example.com","coord"),
//    "mirian.a" to UserProfile("Mirian Albano Ribas", "senha", "mirian@example.com","user")
//)

@Entity(tableName = "user_profiles")
data class UserProfile(
    @PrimaryKey val username: String,
    val fullName: String,
    val password: String,
    val email: String,
    val accessLevel: String
)